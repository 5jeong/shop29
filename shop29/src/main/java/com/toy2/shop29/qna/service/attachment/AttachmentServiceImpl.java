package com.toy2.shop29.qna.service.attachment;

import com.toy2.shop29.product.dao.product.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.qna.domain.AttachmentDto;
import com.toy2.shop29.qna.domain.AttachmentTableName;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.util.FileUploadHandler;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * AttachmentService 는 QnaService에서 사용될 목적으로 만들어짐
 * 따라서, 계정 권한과 관련된 부분은 고려하지 않았음
 */
@Service
public class AttachmentServiceImpl implements AttachmentService{

    @Value("${file.default-mime-type}")
    private String DEFAULT_MIME_TYPE;

    FileUploadHandler fileUploadHandler;
    AttachmentDao attachmentDao;
    QnaDao qnaDao;
    UserMapper userMapper;
    ProductDao productDao;
    private final String USER_ROLE_ADMIN = "관리자";

    public AttachmentServiceImpl(FileUploadHandler fileUploadHandler,
                                 AttachmentDao attachmentDao,
                                 QnaDao qnaDao,
                                 UserMapper userMapper,
                                 ProductDao productDao) {
        this.fileUploadHandler = fileUploadHandler;
        this.attachmentDao = attachmentDao;
        this.qnaDao = qnaDao;
        this.userMapper = userMapper;
        this.productDao = productDao;
    }

    /**
     * MultipartFile에 대한 유효성 검사는 Controller에서 수행
     */
    @Override
    public String saveMultipartFile(MultipartFile multipartFile) throws RuntimeException {
        // 1. MultipartFile 이 null이거나 비어있다면 예외
        if(multipartFile == null || multipartFile.isEmpty()){
            throw new IllegalArgumentException("MultipartFile이 비어있습니다.");
        }

        // 2. 저장될 파일명 생성
        String savedFileName = fileUploadHandler.generateTimestampedFileName(multipartFile.getOriginalFilename());

        // 3. 파일 저장소에 파일 저장
        String filePath;
        try{
            return fileUploadHandler.saveMultipartFile(multipartFile, savedFileName);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.",e);
        }
    }

    @Transactional
    @Override
    public void createAttachments(
            String userId,
            String tableId,
            AttachmentTableName tableName,
            List<String> attachmentURLs) throws RuntimeException {
        // 1. 첨부파일 URL 리스트가 비어있을 경우, 종료
        if(attachmentURLs == null || attachmentURLs.isEmpty()){
            return;
        }

        // 2. 첨부파일 URL 리스트를 순회하며, 첨부파일이 파일 저장소에 저장되있는지 확인
        // 첨부파일이 존재하지 않을 경우, 해당 첨부파일은 제외함 -> 즉, 존재하는 첨부파일에 대해서만 작업 수행
        Map<String, File> savedFileMap = new HashMap<>();
        for(String attachmentURL : attachmentURLs){
            File file = fileUploadHandler.getFileFromUrl(attachmentURL);
            if(file != null && file.exists()){
                savedFileMap.put(attachmentURL, file);
            }
        }

        // 3. 만약 첨부파일이 이미 첨부파일 테이블의 레코드에 저장된 상태라면,
        // 하나의 첨부파일에 대해 여러 레코드가 연결될 수 있기 때문에, 예외 던짐
        for(String fileUrl : savedFileMap.keySet()){
            String savedFileName = fileUploadHandler.extractFileNameFromUrl(fileUrl);
            AttachmentDto attachmentDto = attachmentDao.selectByFileName(savedFileName);
            if(attachmentDto != null){
                throw new IllegalArgumentException("첨부파일이 이미 다른 레코드에 연결되어 있습니다.");
            }
        }

        // 4. tableName에 해당하는 테이블에서 tableId에 해당하는 레코드 확인 및 본인여부 확인
        checkTableIdValidate(userId, tableId, tableName);

        // 5. 첨부파일들에 대한 메타정보를 추출하여, 첨부파일 DTO 객체들 생성
        // 파일 저장소에 저장된 파일은, 유효성 검사가 완료된 파일이기 때문에,
        // 별도의 파일 유효성 검사는 수행하지 않음
        List<AttachmentDto> attachmentDtos = new LinkedList<>();
        for(Map.Entry<String, File> entry : savedFileMap.entrySet()){
            String fileURL = entry.getKey();
            File file = entry.getValue();
            Integer[] widthAndHeight = fileUploadHandler.getImageSize(file);
            AttachmentDto dto = AttachmentDto.builder()
                    .tableId(tableId)
                    .tableName(tableName)
                    .fileName(fileUploadHandler.extractFileNameFromUrl(fileURL))
                    .filePath(fileURL)
                    .width(widthAndHeight[0])
                    .height(widthAndHeight[1])
                    .size(fileUploadHandler.getFileSize(file))
                    .extension(fileUploadHandler.getExtension(file.getName()))
                    .createdId(userId)
                    .updatedId(userId)
                    .build();
            attachmentDtos.add(dto);
        }

        // 6. 첨부파일들을 첨부파일 테이블에 저장
        int insertedCnt = attachmentDao.insertList(attachmentDtos);
        if(insertedCnt != attachmentDtos.size()){
            // 실제 저장된 개수와 저장할 개수가 다를 경우,
            // 예외를 발생시키지는 않고, 로깅만 수행하도록함
        }
    }

    @Override
    public List<AttachmentDto> findAttachmentsBy(String tableId, AttachmentTableName tableName) throws RuntimeException {
        // tableId와 tableName에 해당하는 첨부파일 리스트를 조회
        return attachmentDao.selectAllBy(tableId, tableName,true);
    }

    private void checkTableIdValidate(String userId, String tableId, AttachmentTableName tableName) throws IllegalArgumentException {
        switch (tableName){
            case QNA:
                QnaDto qnaDto = qnaDao.select(Integer.parseInt(tableId), null);
                if(qnaDto == null){
                    throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
                }
                if(!qnaDto.getUserId().equals(userId)){
                    throw new IllegalArgumentException("첨부파일 생성권한이 없습니다.");
                }
                break;
            case USER:
                UserDto userDto = userMapper.findById(tableId);
                if(userDto == null){
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }

                if(!userDto.getUserId().equals(userId)){
                    throw new IllegalArgumentException("본인의 프로필 이미지만 등록할 수 있습니다.");
                }
                break;
            case PRODUCT:
                ProductDto productDto = productDao.select(Integer.parseInt(tableId));
                if(productDto == null){
                    throw new IllegalArgumentException("존재하지 않는 상품입니다.");
                }

                UserDto userDto1 = userMapper.findById(userId);
                if(userDto1 == null){
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }
                if(!userDto1.getUserRole().equals(USER_ROLE_ADMIN)){
                    throw new IllegalArgumentException("관리자만 상품의 이미지를 등록할 수 있습니다.");
                }
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 테이블명입니다.");
        }
    }

    @Transactional
    @Override
    public void deleteAttachmentsBy(String userId, String tableId, AttachmentTableName tableName) throws RuntimeException {
        // 1. tableName에 해당하는 테이블에서 tableId에 해당하는 레코드가 있는지 확인
        checkTableIdValidate(userId, tableId, tableName);

        // 2. tableId와 tableName에 해당하는 첨부파일 리스트를 조회, 리스트가 비어있을 경우, 종료
        List<AttachmentDto> attachmentDtos = attachmentDao.selectAllBy(tableId, tableName,true);
        if(attachmentDtos.isEmpty()){
            return;
        }

        // 3. 테이블에서 첨부파일 리스트 softDelete
        List<Integer> attachmentIds = new LinkedList<>();
        for(AttachmentDto attachmentDto : attachmentDtos){
            attachmentIds.add(attachmentDto.getAttachmentId());
        }
        int deletedCnt = attachmentDao.softDeleteList(attachmentIds);
        if(deletedCnt != attachmentIds.size()){
            // 실제 삭제된 개수와 삭제할 개수가 다를 경우,
            // 예외를 발생시키지는 않고, 로깅만 수행하도록함
        }

        /*
            파일 저장소에 있는 파일은 이 메서드에서 삭제하지 않음
            지정된 시간에 배치를 통해, 첨부파일 테이블에 있는 파일들과 실제 파일 저장소에 있는 파일들을 비교하여
            첨부파일 테이블에 있는 파일이 실제 파일 저장소에 없을 경우, 삭제하는 작업을 수행함
            - 파일 저장소에 모든 파일이 한 폴더에 있는 경우, 파일목록 조회에 시간이 오래 걸릴 수 있음
            - 따라서, 추후 일자별 폴더를 생성하여 파일을 저장하는 방식으로 변경해야함
         */
    }
}
