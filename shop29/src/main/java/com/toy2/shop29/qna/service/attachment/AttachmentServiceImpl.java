package com.toy2.shop29.qna.service.attachment;

import com.toy2.shop29.qna.domain.AttachmentDto;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.util.FileUploadHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * AttachmentService 는 QnaService에서 사용될 목적으로 만들어짐
 * 따라서, 계정 권한과 관련된 부분은 고려하지 않았음
 */
@Service
public class AttachmentServiceImpl implements AttachmentService{

    FileUploadHandler fileUploadHandler;
    AttachmentDao attachmentDao;
    QnaDao qnaDao;

    public AttachmentServiceImpl(FileUploadHandler fileUploadHandler, AttachmentDao attachmentDao, QnaDao qnaDao) {
        this.fileUploadHandler = fileUploadHandler;
        this.attachmentDao = attachmentDao;
        this.qnaDao = qnaDao;
    }

    // * DB에 파일정보를 저장한 이후, 저장소에 파일을 저장하는 이유
    // - 파일 저장소에 파일을 저장한 이후, DB 트랜잭션에서 예외 발생시 롤백이 어려움
    // - 따라서, DB에 파일정보를 저장한 이후, 파일 저장소에 파일을 저장하는 것이 안전함
    @Transactional(rollbackFor = IOException.class)
    @Override
    public void createAttachments(int qnaId, List<File> files) throws IllegalArgumentException, IOException {
        // 0. files가 null이거나, 비어있을 경우, 예외
        if(files == null || files.isEmpty()){
            throw new IllegalArgumentException("files가 null이거나, 비어있습니다.");
        }

        // 1. qnaId에 해당하는 문의글이 존재하는지 확인
        QnaDto qnaDto = qnaDao.select(qnaId, false);
        if(qnaDto == null){
            throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
        }

        // 2. 파일에서 메타정보를 추출하여, 첨부파일 DTO 객체 리스트 생성, DB 저장
        List<AttachmentDto> attachmentDtos = new LinkedList<>();
        try{
            for(File file : files){
                assert file != null;

                // 파일 정보 추출
                String fileName = file.getName(); // 파일명
                String savedFileName = fileUploadHandler.generateTimestampedFileName(fileName); // 저장될 파일명
                int[] imageSize = fileUploadHandler.getImageSize(file); // width, height
                long fileSize = fileUploadHandler.getFileSize(file); // 파일 크기
                String extension = fileUploadHandler.getExtension(fileName); // 확장자
                String fileUrl = fileUploadHandler.createFileUrl(savedFileName); // 파일접근 URL

                // 첨부파일 DTO 객체 생성
                AttachmentDto attachmentDto = AttachmentDto.builder()
                        .qnaId(qnaId)
                        .fileName(savedFileName)
                        .size(fileSize)
                        .width(imageSize[0])
                        .height(imageSize[1])
                        .extension(extension)
                        .filePath(fileUrl)
                        .createdId(qnaDto.getUserId())
                        .updatedId(qnaDto.getUserId())
                        .build();
                attachmentDtos.add(attachmentDto);
            }

            // DB 저장
            int rowCnt = attachmentDao.insertList(attachmentDtos);
            assert rowCnt == attachmentDtos.size();
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }

        assert attachmentDtos.size() == files.size();

        // 3. 파일 저장소에 파일 저장
        List<String> savedFileNameList = new LinkedList<>();
        for(int i = 0; i < files.size(); i++){
            try{
                // 파일 저장
                File file = files.get(i);
                String savedFileName = attachmentDtos.get(i).getFileName();
                fileUploadHandler.saveFile(file, savedFileName);
                savedFileNameList.add(savedFileName);
            }catch (IOException e){
                e.printStackTrace();
                // 예외 발생시, 저장된 파일들 삭제
                deleteAllFiles(savedFileNameList);
                throw e;
            }
        }
    }

    private void deleteAllFiles(List<String> savedFileNames){
        for(int i = 0; i < savedFileNames.size(); i++){
            try {
                fileUploadHandler.deleteFile(savedFileNames.get(i));
            } catch (IOException e) {
                e.printStackTrace();

                // 후속 처리를 위해, 미삭제 파일명 출력
                for(int j = i; j < savedFileNames.size(); j++){
                    System.out.println("미삭제 파일명 : " + savedFileNames.get(j));
                }
            }
        }
    }

    @Transactional(rollbackFor = IOException.class)
    @Override
    public void deleteAttachmentsBy(int qnaId) throws IOException {
        // 1. qnaId에 해당하는 문의글이 존재하는지 확인
        QnaDto qnaDto = qnaDao.select(qnaId, false);
        if(qnaDto == null){
            throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
        }

        // 2. qnaId에 해당하는 첨부파일 리스트를 조회, 리스트가 비어있을 경우, 종료
        List<AttachmentDto> attachmentDtos = attachmentDao.selectAllBy(qnaId, true);
        if(attachmentDtos.isEmpty()){
            return;
        }

        // 3. 테이블에서 첨부파일 리스트 softDelete
        List<Integer> attachmentIds = new LinkedList<>();
        for(AttachmentDto attachmentDto : attachmentDtos){
            attachmentIds.add(attachmentDto.getAttachmentId());
        }
        int rowCnt = attachmentDao.softDeleteList(attachmentIds);
        assert rowCnt == attachmentIds.size();

        // 4. 파일 삭제 실패시 복구를 위해, 파일 복사본들 생성
        List<String> savedFileNames = new LinkedList<>();
        for(AttachmentDto attachmentDto : attachmentDtos){
            savedFileNames.add(attachmentDto.getFileName());
        }

        List<String> copiedFileNames = new LinkedList<>();
        for(String savedFileName : savedFileNames){
            File originFile = fileUploadHandler.getFile(savedFileName);
            fileUploadHandler.copyFile(originFile);
            copiedFileNames.add(originFile.getName());
        }

        // 5. 파일 저장소에서 첨부파일 리스트 삭제
        Queue<String> deletedFileNames = new LinkedList<>();
        for(String savedFileName : savedFileNames){
            try{
                fileUploadHandler.deleteFile(savedFileName);
                deletedFileNames.add(savedFileName);
            }catch (IOException e){
                // 예외 발생하여, 파일 삭제 실패시, 앞서 삭제된 파일들 복구
                e.printStackTrace();
                try{
                    while(!deletedFileNames.isEmpty()){
                        String deletedFileName = deletedFileNames.poll();
                        fileUploadHandler.restoreFileFromBackup(deletedFileName);
                    }
                }catch (IOException e2){
                    // 파일 복구에 실패할 경우,
                    // 미 복구된 파일명 로깅 필요 -> 후속 수작업 복구를 위함
                    e2.printStackTrace();
                    for(String notDeletedFileNames : deletedFileNames){
                        System.out.println("미복구 파일명 : " + notDeletedFileNames);
                    }
                    throw e2;
                }
            }
        }

        // 6. 복구를 위해 저장한, 파일 복사본들 삭제
        deleteAllFiles(copiedFileNames);
    }
}
