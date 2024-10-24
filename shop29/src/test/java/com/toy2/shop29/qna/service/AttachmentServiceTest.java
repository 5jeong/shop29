package com.toy2.shop29.qna.service;

import com.toy2.shop29.product.dao.product.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.qna.domain.*;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.attachment.AttachmentService;
import com.toy2.shop29.qna.util.FileUploadHandler;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(
        properties = {
                "file.upload.file-path=test/",
                "file.upload.temp-file-path=test/temp/",
        }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttachmentServiceTest {

    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    QnaDao qnaDao;
    @Autowired
    AttachmentDao attachmentDao;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProductDao productDao;

    // 강제로 IOException 을 발생시켜, 롤백 처리를 테스트하기 위함
    @SpyBean
    FileUploadHandler fileUploadHandler;

    @Value("${file.upload.temp-file-path}")
    private String TEMP_FILE_PATH;
    @Value("${file.upload.test-file-name}")
    private String TEST_FILE_NAME;
    @Value("${file.upload.test-file-path}")
    private String TEST_FILE_PATH;
    @Value("${file.upload.file-path}")
    private String FILE_PATH;
    private String TEST_FILE_TYPE = "image/jpeg";

    private QnaDto qnaDto;
    private String ADMIN_ID = "admin";
    private String USER_ROLE_ADMIN = "관리자";

    /*
        * 테스트 시나리오
        - saveMultipartFile(MultipartFile multipartFile)
            1) 성공 - 파일 저장소에 파일 저장
            2) 예외 - MultipartFile 이 null이거나 비어있다면 예외
        - createAttachments(String userId, int tableId, AttachmentTableName tableName, List<String> attachmentNames)
            1) 성공 - 첨부파일 테이블에 여러건 저장
            1-1) 성공 - 사용자 테이블 연동
            1-2) 성공 - 상품 테이블 연동
            2) 성공 - 첨부파일 이름 리스트가 비어있을 경우, 종료
            3) 첨부파일이 파일 저장소에 없을 경우, 하당 파일은 무시
            4) 예외 - 첨부파일 이름이 동일한 레코드가 첨부파일 테이블에 있을 경우
            5) 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드가 없을 경우
            6) 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드의 소유자가 본인이 아닌 경우
        - findAttachmentsBy(String tableId, AttachmentTableName tableName)
            1) 성공 - 첨부파일 조회
            1-1) 성공 - 사용자 테이블 연동
            1-2) 성공 - 상품 테이블 연동
        - deleteAttachmentsBy(String userId, int tableId, AttachmentTableName tableName)
            1) 성공 - 첨부파일 여러건 삭제
            1-1) 성공 - 사용자 테이블 연동
            1-2) 성공 - 상품 테이블 연동
            (미완료)2) 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드가 없을 경우
            (미완료)3) 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드의 소유자가 본인이 아닌 경우
            (미완료)4) 롤백 & 예외 - 테이블 레코드 생성 중 예외 발생시
     */

    @BeforeEach
    void before() throws IOException {
        qnaTypeDao.deleteAll();
        assertTrue(qnaTypeDao.count() == 0);
        parentQnaTypeDao.deleteAll();
        assertTrue(parentQnaTypeDao.count() == 0);
        qnaDao.deleteAll();
        assertTrue(qnaDao.count() == 0);

        userMapper.deleteUser(ADMIN_ID);
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId(ADMIN_ID);
        userRegisterDto.setEmail("testuser@example.com");
        userRegisterDto.setPassword("password123");
        userRegisterDto.setUserName("손흥민");
        userRegisterDto.setPostalCode("12345");
        userRegisterDto.setAddressLine1("경기 화성시");
        userRegisterDto.setAddressLine2("201호");
        userRegisterDto.setAddressReference("");
        userRegisterDto.setPhoneNumber("010-1234-9999");
        userRegisterDto.setGender(1); // 1은 남자
        userRegisterDto.setBirthDate("1990-02-12");
        assertTrue(userMapper.insertUser(userRegisterDto) == 1);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", ADMIN_ID);
        map.put("userRole", USER_ROLE_ADMIN);
        assertTrue(userMapper.updateUserRoleForTest(map) == 1);

        // 1. 부모 문의유형 등록
        ParentQnaTypeDto parentQnaTypeDto = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(parentQnaTypeDao.insert(parentQnaTypeDto) == 1);


        // 2. 문의유형 등록
        QnaTypeDto qnaTypeDto = QnaTypeDto.builder()
                .qnaTypeId("QNA_TYPE_1")
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("문의유형1")
                .description("문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaTypeDao.insert(qnaTypeDto) == 1);

        // 3. 문의글 등록
        QnaDto qDto = QnaDto.builder()
                .userId(ADMIN_ID)
                .qnaTypeId("QNA_TYPE_1")
                .title("문의글 제목")
                .content("문의글 내용")
                .build();
        assertTrue(qnaDao.insert(qDto) == 1);
        qnaDto = qDto;

        // 첨부파일 테이블 초기화
        attachmentDao.deleteAll();
        assertTrue(attachmentDao.count() == 0);

        // 파일 저장소 초기화
        cleanFileStorage();
    }

    @AfterEach
    void after() throws IOException {
        // 파일 저장소 초기화
        cleanFileStorage();
    }

    private void cleanFileStorage() throws IOException {
        List<String> filePathList = List.of(FILE_PATH, TEMP_FILE_PATH);
        fileUploadHandler.deleteAllFilesFrom(filePathList);
    }

    @DisplayName("파일 저장소에 파일 저장 - 성공")
    @Test
    void saveMultipartFile_1() throws IOException {
        // 1단계 데이터 선택 -> MultipartFile 생성
        File file = createTestFile();
        MultipartFile multipartFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_NAME,TEST_FILE_TYPE , Files.readAllBytes(file.toPath()));

        // 2단계 데이터 처리 -> 파일 저장
        String fileUrl = attachmentService.saveMultipartFile(multipartFile);

        // 3단계 검증
        // 3-1. 파일 저장소에 파일이 저장되었는지 확인
        assertTrue(fileUrl != null);
        File foundFile = fileUploadHandler.getFileFromUrl(fileUrl);
        assertTrue(foundFile != null);
        assertTrue(foundFile.exists());
    }

    @DisplayName("파일 저장소에 파일 저장 - 예외 - MultipartFile 이 null이거나 비어있다면 예외")
    @Test
    void saveMultipartFile_2(){
        // 1단계 데이터 선택 -> MultipartFile 생성
        MultipartFile multipartFile = new MockMultipartFile("file", new byte[0]);

        // 2단계 데이터 처리 & 3단계 검증 -> 예외 확인
        assertThrows(RuntimeException.class, () -> {
            attachmentService.saveMultipartFile(multipartFile);
        });
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 첨부파일 테이블에 여러건 저장")
    @Test
    void createAttachments_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);

        // when
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, savedFileUrls);

        // then -> 저장된 레코드의 속성이 채워졌는지 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(qnaDto.getQnaId().toString(), AttachmentTableName.QNA, true);
        assertTrue(savedAttachments != null);
        assertTrue(savedAttachments.size() == fileCnt);
        for(int i = 0; i < savedAttachments.size(); i++){
            AttachmentDto attachmentDto = savedAttachments.get(i);
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getSize() > 0);
            assertTrue(attachmentDto.getWidth() > 0);
            assertTrue(attachmentDto.getHeight() > 0);
            assertTrue(fileUploadHandler.getExtension(TEST_FILE_NAME).equalsIgnoreCase(attachmentDto.getExtension()));
            assertTrue(attachmentDto.getFilePath() != null);
        }
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 사용자 테이블 연동")
    @Test
    void createAttachments_1_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);

        // when
        attachmentService.createAttachments(ADMIN_ID, ADMIN_ID, AttachmentTableName.USER, savedFileUrls);

        // then -> 저장된 레코드의 속성이 채워졌는지 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(ADMIN_ID, AttachmentTableName.USER, true);
        assertTrue(savedAttachments != null);
        assertTrue(savedAttachments.size() == fileCnt);
        for(int i = 0; i < savedAttachments.size(); i++){
            AttachmentDto attachmentDto = savedAttachments.get(i);
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getSize() > 0);
            assertTrue(attachmentDto.getWidth() > 0);
            assertTrue(attachmentDto.getHeight() > 0);
            assertTrue(fileUploadHandler.getExtension(TEST_FILE_NAME).equalsIgnoreCase(attachmentDto.getExtension()));
            assertTrue(attachmentDto.getFilePath() != null);
        }
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 상품 테이블 연동")
    @Test
    void createAttachments_1_2() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);

        ProductDto productDto = new ProductDto(
                123456789,
                123456,
                "상품명",
                1234,
                1234,
                1234,
                1234,
                1,
                ADMIN_ID,
                ADMIN_ID
        );
        productDto.setStartDate(new Date(2000));

        assertTrue(productDao.insert(productDto) == 1);

        // when
        attachmentService.createAttachments(ADMIN_ID, Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, savedFileUrls);

        // then -> 저장된 레코드의 속성이 채워졌는지 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, true);
        assertTrue(savedAttachments != null);
        assertTrue(savedAttachments.size() == fileCnt);
        for(int i = 0; i < savedAttachments.size(); i++){
            AttachmentDto attachmentDto = savedAttachments.get(i);
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getSize() > 0);
            assertTrue(attachmentDto.getWidth() > 0);
            assertTrue(attachmentDto.getHeight() > 0);
            assertTrue(fileUploadHandler.getExtension(TEST_FILE_NAME).equalsIgnoreCase(attachmentDto.getExtension()));
            assertTrue(attachmentDto.getFilePath() != null);
        }
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 첨부파일 이름 리스트가 비어있을 경우, 종료")
    @Test
    void createAttachments_2() throws IOException {
        // given -> 빈 리스트 생성
        List<MultipartFile> multipartFiles = new LinkedList<>();

        // when
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, null);

        // then
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(qnaDto.getQnaId().toString(), AttachmentTableName.QNA, true);
        assertTrue(savedAttachments.size() == multipartFiles.size());
    }

    @DisplayName("첨부파일 여러건 저장 - 성공 - 첨부파일이 파일 저장소에 없을 경우, 해당 파일은 무시")
    @Test
    void createAttachments_3() throws IOException, InterruptedException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 존재하지 않는 파일명 생성
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        String notExistFileURL = "notExistFileURL";
        assertTrue(fileUploadHandler.getFileFromUrl(notExistFileURL) == null);
        savedFileUrls.add(notExistFileURL);

        // when
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, savedFileUrls);

        // then -> 파일이 실제 파일 저장소에 있는 경우의 건만 테이블 레코드로 저장됨
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(qnaDto.getQnaId().toString(), AttachmentTableName.QNA, true);
        assertTrue(savedAttachments.size() == fileCnt);
    }

    @DisplayName("첨부파일 여러건 저장 - 예외 - 첨부파일 이름이 동일한 레코드가 첨부파일 테이블에 있을 경우")
    @Test
    void createAttachments_4() throws IOException {
        // given
        // 1. 첨부파일 저장소에 파일 저장
        File file = createTestFile();
        List<String> saveFileUrls = new LinkedList<>();

        MultipartFile multipartFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_NAME, TEST_FILE_TYPE, Files.readAllBytes(file.toPath()));
        String fileUrl = attachmentService.saveMultipartFile(multipartFile);
        assertTrue(fileUrl != null);
        saveFileUrls.add(fileUrl);

        // 2. 첨부파일 테이블에 레코드 저장
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, saveFileUrls);

        // when -> 동일한 이름의 파일을 첨부파일 테이블에 다시 저장
        // then -> 예외발생
        assertThrows(RuntimeException.class, () -> {
            attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, saveFileUrls);
        });
    }

    @DisplayName("첨부파일 여러건 저장 - 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드가 없을 경우")
    @Test
    void createAttachments_5() throws IOException {
        // given
        // 1. 첨부파일 저장소에 파일 저장
        File file = createTestFile();
        List<String> savedFileNames = new LinkedList<>();

        MultipartFile multipartFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_NAME, TEST_FILE_TYPE, Files.readAllBytes(file.toPath()));
        String savedFileName = attachmentService.saveMultipartFile(multipartFile);
        assertTrue(savedFileName != null);
        savedFileNames.add(savedFileName);

        // 2. 존재하지 않는 tableId 지정
        String notExistTableId = "9999";
        assertTrue(qnaDao.select(Integer.parseInt(notExistTableId), null) == null);

        // when -> 존재하지 않는 tableId 와 첨부파일 레코드 연결
        // then -> 예외발생
        assertThrows(RuntimeException.class, () -> {
            attachmentService.createAttachments(ADMIN_ID, notExistTableId, AttachmentTableName.QNA, savedFileNames);
        });
    }

    @DisplayName("첨부파일 여러건 저장 - 예외 - tableName에 해당하는 테이블에서 tableId에 해당하는 레코드의 소유자가 본인이 아닌 경우")
    @Test
    void createAttachments_6() throws IOException {
        // given
        // 1. 첨부파일 저장소에 파일 저장
        File file = createTestFile();
        List<String> savedFileNames = new LinkedList<>();

        MultipartFile multipartFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_NAME, TEST_FILE_TYPE, Files.readAllBytes(file.toPath()));
        String savedFileName = attachmentService.saveMultipartFile(multipartFile);
        assertTrue(savedFileName != null);
        savedFileNames.add(savedFileName);

        // 2. 대상 테이블 레코드의 소유자와 다른 userId 지정
        String notOwnerUserId = "notOwner";
        assertTrue(!qnaDto.getUserId().equals(notOwnerUserId));

        // when -> 존재하지 않는 tableId 와 첨부파일 레코드 연결
        // then -> 예외발생
        assertThrows(RuntimeException.class, () -> {
            attachmentService.createAttachments(notOwnerUserId, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, savedFileNames);
        });
    }

    @DisplayName("첨부파일 조회 - 성공 - 첨부파일 조회")
    @Test
    void findAttachmentsBy_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, savedFileUrls);

        // when
        List<AttachmentDto> savedAttachments = attachmentService.findAttachmentsBy(qnaDto.getQnaId().toString(), AttachmentTableName.QNA);

        // then
        assertTrue(savedAttachments.size() == fileCnt);
        for(AttachmentDto attachmentDto : savedAttachments){
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getTableId().equals(qnaDto.getQnaId().toString()));
            assertTrue(attachmentDto.getTableName().equals(AttachmentTableName.QNA));
        }
    }

    @DisplayName("첨부파일 조회 - 성공 - 사용자 테이블 연동")
    @Test
    void findAttachmentsBy_1_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        attachmentService.createAttachments(ADMIN_ID, ADMIN_ID, AttachmentTableName.USER, savedFileUrls);

        // when
        List<AttachmentDto> savedAttachments = attachmentService.findAttachmentsBy(ADMIN_ID, AttachmentTableName.USER);

        // then
        assertTrue(savedAttachments.size() == fileCnt);
        for(AttachmentDto attachmentDto : savedAttachments){
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getTableId().equals(ADMIN_ID));
            assertTrue(attachmentDto.getTableName().equals(AttachmentTableName.USER));
        }
    }
    
    @DisplayName("첨부파일 조회 - 성공 - 상품 테이블 연동")
    @Test
    void findAttachmentsBy_1_2() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        
        ProductDto productDto = new ProductDto(
                123456789,
                123456,
                "상품명",
                1234,
                1234,
                1234,
                1234,
                1,
                ADMIN_ID,
                ADMIN_ID
        );
        productDto.setStartDate(new Date(2000));
        assertTrue(productDao.insert(productDto) == 1);
        attachmentService.createAttachments(ADMIN_ID, Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, savedFileUrls);
        
        // when
        List<AttachmentDto> savedAttachments = attachmentService.findAttachmentsBy(Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT);

        // then
        assertTrue(savedAttachments.size() == fileCnt);
        for(AttachmentDto attachmentDto : savedAttachments){
            assertTrue(savedFileUrls.stream().anyMatch(fileUrl -> fileUrl.equals(attachmentDto.getFilePath())));
            assertTrue(attachmentDto.getTableId().equals(Integer.toString(productDto.getProductId())));
            assertTrue(attachmentDto.getTableName().equals(AttachmentTableName.PRODUCT));
        }
    }

    @DisplayName("첨부파일 여러건 삭제 - 성공 - 첨부파일 여러건 삭제")
    @Test
    void deleteAttachmentsBy_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        attachmentService.createAttachments(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA, savedFileUrls);

        // when
        attachmentService.deleteAttachmentsBy(ADMIN_ID, qnaDto.getQnaId().toString(), AttachmentTableName.QNA);

        // then -> 첨부파일 테이블 레코드 삭제 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(qnaDto.getQnaId().toString(), AttachmentTableName.QNA, true);
        assertTrue(savedAttachments.size() == 0);
    }

    @DisplayName("첨부파일 여러건 삭제 - 성공 - 사용자 테이블 연동")
    @Test
    void deleteAttachmentsBy_1_1() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);
        attachmentService.createAttachments(ADMIN_ID, ADMIN_ID, AttachmentTableName.USER, savedFileUrls);
        assertTrue(attachmentDao.selectAllBy(ADMIN_ID, AttachmentTableName.USER, true).size() == fileCnt);
        // when
        attachmentService.deleteAttachmentsBy(ADMIN_ID, ADMIN_ID, AttachmentTableName.USER);

        // then -> 첨부파일 테이블 레코드 삭제 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(ADMIN_ID, AttachmentTableName.USER, true);
        assertTrue(savedAttachments.size() == 0);
    }

    @DisplayName("첨부파일 여러건 삭제 - 성공 - 상품 테이블 연동")
    @Test
    void deleteAttachmentsBy_1_2() throws IOException {
        // given -> 첨부파일 저장소에 2개 파일 저장 & 첨부파일 테이블에 2개 레코드 저장
        int fileCnt = 2;
        List<String> savedFileUrls = uploadTwoTestFile(fileCnt);

        ProductDto productDto = new ProductDto(
                123456789,
                123456,
                "상품명",
                1234,
                1234,
                1234,
                1234,
                1,
                ADMIN_ID,
                ADMIN_ID
        );
        productDto.setStartDate(new Date(2000));
        assertTrue(productDao.insert(productDto) == 1);
        attachmentService.createAttachments(ADMIN_ID, Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, savedFileUrls);
        assertTrue(attachmentDao.selectAllBy(Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, true).size() == fileCnt);

        // when
        attachmentService.deleteAttachmentsBy(ADMIN_ID, Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT);

        // then -> 첨부파일 테이블 레코드 삭제 확인
        List<AttachmentDto> savedAttachments = attachmentDao.selectAllBy(Integer.toString(productDto.getProductId()), AttachmentTableName.PRODUCT, true);
        assertTrue(savedAttachments.size() == 0);
    }

    private List<String> uploadTwoTestFile(int fileCnt) throws IOException {
        File file = createTestFile();
        List<String> savedFileUrls = new LinkedList<>();
        for(int i = 0; i < fileCnt; i++){
            MultipartFile multipartFile = new MockMultipartFile(
                    TEST_FILE_NAME,
                    TEST_FILE_NAME, TEST_FILE_TYPE,
                    Files.readAllBytes(file.toPath()));
            String fileUrl = attachmentService.saveMultipartFile(multipartFile);
            assertTrue(fileUrl != null);
            savedFileUrls.add(fileUrl);
        }
        return savedFileUrls;
    }

    File createTestFile() {
        return fileUploadHandler.downloadFile(TEST_FILE_PATH, TEST_FILE_NAME);
    }
}
