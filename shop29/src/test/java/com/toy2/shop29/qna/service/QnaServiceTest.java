package com.toy2.shop29.qna.service;

import com.toy2.shop29.qna.domain.AttachmentTableName;
import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.domain.request.QnaCreateRequest;
import com.toy2.shop29.qna.domain.response.QnaAdminResponse;
import com.toy2.shop29.qna.domain.response.QnaDetailResponse;
import com.toy2.shop29.qna.domain.response.QnaResponse;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnaasnwer.QnaAnswerDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.attachment.AttachmentService;
import com.toy2.shop29.qna.service.qna.QnaService;
import com.toy2.shop29.qna.service.qnaanswer.QnaAnswerService;
import com.toy2.shop29.qna.util.FileUploadHandler;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaServiceTest {

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
    QnaAnswerDao qnaAnswerDao;
    @Autowired
    QnaAnswerService qnaAnswerService;
    @Autowired
    FileUploadHandler fileUploadHandler;
    @Autowired
    private QnaService qnaService;
    @Autowired
    private UserMapper userMapper;

    private String ADMIN_ID = "admin";
    private String USER_ID = "test1";
    private String OTHER_USER_ID = "test2";

    private final String TEST_FILE_NAME = "sample-img.PNG";
    private final String TEST_FILE_PATH = "static/test/";

    private ParentQnaTypeDto parentQnaTypeActive;
    private QnaTypeDto qnaTypeActive;
    private QnaTypeDto qnaTypeInactive;
    private List<String> savedFileNames;

    void init() throws IOException {
        // 부모 문의유형, 문의유형 초기화
        parentQnaTypeDao.deleteAll();
        qnaTypeDao.deleteAll();
        fileUploadHandler.deleteAllFiles();
        userMapper.deleteUser(ADMIN_ID);
        userMapper.deleteUser(USER_ID);
        userMapper.deleteUser("admin2");
        userMapper.deleteUser(OTHER_USER_ID);

        // 0. 계정 생성
        UserRegisterDto userRegisterDto1 = new UserRegisterDto();
        userRegisterDto1.setUserId(ADMIN_ID);
        userRegisterDto1.setEmail("testuser1@example.com");
        userRegisterDto1.setPassword("password123");
        userRegisterDto1.setUserName("손흥민");
        userRegisterDto1.setPostalCode("12345");
        userRegisterDto1.setAddressLine1("경기 화성시");
        userRegisterDto1.setAddressLine2("201호");
        userRegisterDto1.setAddressReference("");
        userRegisterDto1.setPhoneNumber("010-1234-9991");
        userRegisterDto1.setGender(1); // 1은 남자
        userRegisterDto1.setBirthDate("1990-02-12");
        assertTrue(userMapper.insertUser(userRegisterDto1) == 1);
        Map<String,Object> map = new HashMap<>();
        map.put("userId", ADMIN_ID);
        map.put("userRole", "관리자");
        assertTrue(userMapper.updateUserRoleForTest(map) == 1);

        UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setUserId(USER_ID);
        userRegisterDto2.setEmail("testuser2@example.com");
        userRegisterDto2.setPassword("password123");
        userRegisterDto2.setUserName("손흥민");
        userRegisterDto2.setPostalCode("12345");
        userRegisterDto2.setAddressLine1("경기 화성시");
        userRegisterDto2.setAddressLine2("201호");
        userRegisterDto2.setAddressReference("");
        userRegisterDto2.setPhoneNumber("010-1234-9992");
        userRegisterDto2.setGender(1); // 1은 남자
        userRegisterDto2.setBirthDate("1990-02-12");
        assertTrue(userMapper.insertUser(userRegisterDto2) == 1);

        UserRegisterDto userRegisterDto3 = new UserRegisterDto();
        userRegisterDto3.setUserId(OTHER_USER_ID);
        userRegisterDto3.setEmail("testuser3@example.com");
        userRegisterDto3.setPassword("password123");
        userRegisterDto3.setUserName("손흥민");
        userRegisterDto3.setPostalCode("12345");
        userRegisterDto3.setAddressLine1("경기 화성시");
        userRegisterDto3.setAddressLine2("201호");
        userRegisterDto3.setAddressReference("");
        userRegisterDto3.setPhoneNumber("010-1234-9993");
        userRegisterDto3.setGender(1); // 1은 남자
        userRegisterDto3.setBirthDate("1990-02-12");
        assertTrue(userMapper.insertUser(userRegisterDto3) == 1);

        // 1. 부모 문의유형 등록
        parentQnaTypeActive = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1_active")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(parentQnaTypeDao.insert(parentQnaTypeActive) == 1);

        // 2. 문의유형 등록
        // 2-1. active == true
        qnaTypeActive = QnaTypeDto.builder()
                .qnaTypeId("QNA_TYPE_1")
                .parentQnaTypeId(parentQnaTypeActive.getParentQnaTypeId())
                .name("문의유형1_active")
                .description("문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaTypeDao.insert(qnaTypeActive) == 1);

        // 2-2. active == false
        qnaTypeInactive = QnaTypeDto.builder()
                .qnaTypeId("QNA_TYPE_2")
                .parentQnaTypeId(parentQnaTypeActive.getParentQnaTypeId())
                .name("문의유형2_inactive")
                .description("문의유형2 설명")
                .isActive(false)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaTypeDao.insert(qnaTypeInactive) == 1);

        // 3. 파일 저장소에 샘플 첨부파일 저장
        savedFileNames = new ArrayList<>();
        fileUploadHandler.saveFile(createTestFile(), TEST_FILE_NAME);
        assertTrue(fileUploadHandler.getFile(TEST_FILE_NAME) != null);
        savedFileNames.add(TEST_FILE_NAME);
    }

    @BeforeEach
    void before() throws IOException {
        init();

        // DB 테이블 데이터 초기화
        attachmentDao.deleteAll();
        qnaAnswerDao.deleteAll();
        qnaDao.deleteAll();
    }

    @AfterEach
    void after() throws IOException {
        fileUploadHandler.deleteAllFiles();
    }

    @DisplayName("1:1 문의 전체조회(유저) - 성공 - 자신의 1:1 문의만 조회")
    @Test
    void findQnaList_1() throws IOException {
        // 1단계 데이터 선택
        // 1-1. 1:1 문의 등록 4건 - USER_ID 가 등록

        int myQnaCnt = 4;
        for (int i = 0; i < myQnaCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
            // 1-1-1. 첨부파일 등록
            String fileName = Math.random() + TEST_FILE_NAME;
            saveTestFile(fileName);
            List<String> fileNames = List.of(fileName);
            attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId(), AttachmentTableName.QNA, fileNames);
            // 1-1-2. 답변 등록
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(),ADMIN_ID, "답변" + i);
        }
        // 1-2. 1:1 문의 등록 4건 - OTHER_USER_ID 가 등록
        int otherQnaCnt = 4;
        for (int i = 0; i < otherQnaCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeInactive.getQnaTypeId(),"1:1 문의" + i,OTHER_USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
            // 1-2-1. 첨부파일 등록
            String fileName = Math.random() + TEST_FILE_NAME;
            saveTestFile(fileName);
            List<String> fileNames = List.of(fileName);
            attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId(), AttachmentTableName.QNA, fileNames);
            // 1-2-2. 답변 등록
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(), ADMIN_ID,"답변" + i);
        }

        // 2단계 데이터 처리 - 1:1 문의 전체조회(유저)
        List<QnaResponse> qnaResponses = qnaService.findQnaList(USER_ID, 10, 0);

        // 3단계 검증
        // 3-1. 1:1 문의 전체조회(유저) 결과 검증 - 자신의 1:1 문의만 조회
        assertTrue(qnaResponses != null);
        assertTrue(qnaResponses.size() == myQnaCnt);
        qnaResponses.forEach(qnaResponse -> {
            // 3-2. QnaResponse(응답) DTO 검증
            // 3-2-1. 문의유형 검증
            assertTrue(qnaResponse.getQnaTypeId().equals(qnaTypeActive.getQnaTypeId()));
            assertTrue(qnaResponse.getQnaTypeName().equals(qnaTypeActive.getName()));
            // 3-2-2. 1:1 문의 검증
            assertTrue(qnaResponse.getTitle() != null);
            assertTrue(qnaResponse.getContent() != null);
            assertTrue(qnaResponse.getCreatedTime() != null);
            // 3-2-3. 문의답변 검증
            assertTrue(qnaResponse.getAnswerContent() != null);
            assertTrue(qnaResponse.getAnswerCreatedTime() != null);
            // 3-2-3. 첨부파일 URL 리스트 검증
            assertTrue(qnaResponse.getAttachmentPaths().size() == 1);
        });
    }

    @DisplayName("1:1 문의 전체조회(유저) - 성공 - 자신의 1:1 문의가 없을 경우")
    @Test
    void findQnaList_2() {
        // 1단계 데이터 선택
        // 1-1. 1:1 문의 등록 4건 - OTHER_USER_ID 가 등록
        for (int i = 0; i < 4; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, OTHER_USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
        }

        // 2단계 데이터 처리 - 1:1 문의 전체조회(유저)
        List<QnaResponse> qnaResponses = qnaService.findQnaList(USER_ID, 10, 0);

        // 3단계 검증
        // 3-1. 1:1 문의 전체조회(유저) 결과 검증 - 자신의 1:1 문의가 없을 경우
        assertTrue(qnaResponses != null);
        assertTrue(qnaResponses.size() == 0);
    }

    @DisplayName("1:1 문의 전체조회(유저) - 성공 - 답변과 첨부파일이 없는 경우")
    @Test
    void findQnaList_3() {
        // 1단계 데이터 선택
        // 1-1. 1:1 문의 등록 4건 - USER_ID 가 등록
        int myQnaCnt = 4;
        for (int i = 0; i < myQnaCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
        }

        // 2단계 데이터 처리 - 1:1 문의 전체조회(유저)
        List<QnaResponse> qnaResponses = qnaService.findQnaList(USER_ID, 10, 0);

        // 3단계 검증
        // 3-1. 1:1 문의 전체조회(유저) 결과 검증 - 답변과 첨부파일이 없는 경우
        assertTrue(qnaResponses != null);
        assertTrue(qnaResponses.size() == myQnaCnt);
        qnaResponses.forEach(qnaResponse -> {
            // 3-2. QnaResponse(응답) DTO 검증
            // 3-2-1. 문의유형 검증
            assertTrue(qnaResponse.getQnaTypeId().equals(qnaTypeActive.getQnaTypeId()));
            assertTrue(qnaResponse.getQnaTypeName().equals(qnaTypeActive.getName()));
            // 3-2-2. 1:1 문의 검증
            assertTrue(qnaResponse.getTitle() != null);
            assertTrue(qnaResponse.getContent() != null);
            assertTrue(qnaResponse.getCreatedTime() != null);
            // 3-2-3. 문의답변 검증
            assertTrue(qnaResponse.getAnswerContent() == null);
            assertTrue(qnaResponse.getAnswerCreatedTime() == null);
            // 3-2-3. 첨부파일 URL 리스트 검증
            assertTrue(qnaResponse.getAttachmentPaths().size() == 0);
        });
    }

    @DisplayName("1:1 문의 전체조회(관리자) - 성공 - 모든문의 조회")
    @Test
    void findQnaListWithFilter_1() throws IOException {
        // 1단계 데이터 선택
        // 1-1. 1:1 문의 등록 4건 - USER_ID 가 등록, 문의유형 active
        int userQnaCnt = 4;

        for (int i = 0; i < 4; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
            // 1-1-1. 첨부파일 등록
            String fileName = Math.random() + TEST_FILE_NAME;
            saveTestFile(fileName);
            List<String> fileNames = List.of(fileName);
            attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId(), AttachmentTableName.QNA, fileNames);
            // 1-1-2. 답변 등록
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(),ADMIN_ID, "답변" + i);
        }

        // 1-2. 1:1 문의 등록 4건 - OTHER_USER_ID 가 등록, 문의유형 inactive
        int otherUserQnaCnt = 4;
        for (int i = 0; i < 4; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeInactive.getQnaTypeId(), "1:1 문의" + i, OTHER_USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
            // 1-1-1. 첨부파일 등록
            String fileName = Math.random() + TEST_FILE_NAME;
            saveTestFile(fileName);
            List<String> fileNames = List.of(fileName);
            attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId(), AttachmentTableName.QNA, fileNames);
            // 1-1-2. 답변 등록
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(),ADMIN_ID, "답변" + i);
        }

        // 2단계 데이터 처리
        List<QnaAdminResponse> qnaResponses = qnaService.findQnaListWithFilter( 10, 0, null, null);

        // 3단계 검증
        assertTrue(qnaResponses != null);
        // 모든문의 조회여부
        assertTrue(qnaResponses.size() == userQnaCnt + otherUserQnaCnt);
        // QnaAdminResponse DTO 객체 검증
        for(QnaAdminResponse qnaResponse : qnaResponses){
            // 문의유형 검증
            assertTrue(qnaResponse.getQnaTypeId() != null);
            assertTrue(qnaResponse.getQnaTypeName() != null);
            // 1:1 문의 검증
            assertTrue(qnaResponse.getTitle() != null);
            assertTrue(qnaResponse.getCreatedTime() != null);
            // 유저 정보 검증
            assertTrue(qnaResponse.getUserId() != null);
            assertTrue(qnaResponse.getUserName() != null);
            // 문의답변 검증
            assertTrue(qnaResponse.getAdminId() != null);
            assertTrue(qnaResponse.getAnswerContent() != null);
            assertTrue(qnaResponse.getAnswerCreatedTime() != null);
        }
    }

    @DisplayName("1:1 문의 전제조회(관리자) - 성공 - 문의유형 필터")
    @Test
    void findQnaListWithFilter_2(){
        // 1단계 데이터 선택 -> 2가지 문의유형에 따른 문의글 등록
        // 1-1. '문의유형 A' 에 해당하는 1:1 문의 3건 등록
        int qnaTypeACnt = 3;
        for (int i = 0; i < qnaTypeACnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
        }

        // 1-2. '문의유형 B'에 해당하는 1:1 문의 5건 등록
        int qnaTypeBCnt = 5;
        for (int i = 0; i < qnaTypeBCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeInactive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
        }

        // 2단계 데이터 처리
        List<QnaAdminResponse> responseListTypeA = qnaService.findQnaListWithFilter(10, 0, qnaTypeActive.getQnaTypeId(), null);
        List<QnaAdminResponse> responseListTypeB = qnaService.findQnaListWithFilter(10, 0, qnaTypeInactive.getQnaTypeId(), null);

        // 3단계 검증
        // 3-1. '문의유형 A'에 해당하는 1:1 문의 조회
        assertTrue(responseListTypeA != null);
        assertTrue(responseListTypeA.size() == qnaTypeACnt);
        for(QnaAdminResponse qnaResponse : responseListTypeA){
            // 문의유형 확인
            assertTrue(qnaResponse.getQnaTypeId().equals(qnaTypeActive.getQnaTypeId()));
        }

        // 3-2. '문의유형 B'에 해당하는 1:1 문의 조회
        assertTrue(responseListTypeB != null);
        assertTrue(responseListTypeB.size() == qnaTypeBCnt);
        for(QnaAdminResponse qnaResponse : responseListTypeB){
            // 문의유형 확인
            assertTrue(qnaResponse.getQnaTypeId().equals(qnaTypeInactive.getQnaTypeId()));
        }
    }

    @DisplayName("1:1 문의 전제조회(관리자) - 성공 - 답변여부 필터")
    @Test
    void findQnaListWithFilter_3(){
        // 1단계 데이터 선택
        // 1-1. 답변이 있는 1:1 문의 3건 등록
        int answeredQnaCnt = 3;
        for (int i = 0; i < answeredQnaCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
            // 1-1-1. 답변 등록
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(),ADMIN_ID, "답변" + i);
        }
        // 1-2. 답변이 없는 1:1 문의 5건 등록
        int notAnsweredQnaCnt = 5;
        for (int i = 0; i < notAnsweredQnaCnt; i++) {
            QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의" + i, USER_ID);
            assertTrue(qnaDao.insert(qnaDto) == 1);
        }

        // 2단계 데이터 처리
        List<QnaAdminResponse> responseListAnswered = qnaService.findQnaListWithFilter(10, 0, null, true);
        List<QnaAdminResponse> responseListNotAnswered = qnaService.findQnaListWithFilter(10, 0, null, false);

        // 3단계 검증
        // 3-1. 답변이 있는 1:1 문의 조회
        assertTrue(responseListAnswered != null);
        assertTrue(responseListAnswered.size() == answeredQnaCnt);
        for(QnaAdminResponse qnaResponse : responseListAnswered){
            // 답변여부 확인
            assertTrue(qnaResponse.getAdminId() != null);
        }
        // 3-2. 답변이 없는 1:1 문의 조회
        assertTrue(responseListNotAnswered != null);
        assertTrue(responseListNotAnswered.size() == notAnsweredQnaCnt);
        for(QnaAdminResponse qnaResponse : responseListNotAnswered){
            // 답변여부 확인
            assertTrue(qnaResponse.getAdminId() == null);
        }
    }

    @DisplayName("1:1 문의 상세조회(관리자) - 성공")
    @Test
    void findQnaDetail_1() throws IOException {
        // 1단계 데이터 선택
        // 1-1. 1:1 문의 등록
        QnaDto qnaDto = createQnaDto(qnaTypeActive.getQnaTypeId(), "1:1 문의", USER_ID);
        assertTrue(qnaDao.insert(qnaDto) == 1);
        // 1-2 답변 등록
        qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(),ADMIN_ID, "답변");
        // 1-3 첨부파일 등록
        attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId(), AttachmentTableName.QNA, savedFileNames);

        // 2단계 데이터 처리
        QnaDto selectedQna = qnaDao.selectWith(qnaDto.getQnaId());
        QnaDetailResponse qnaResponse = qnaService.findQnaDetail(qnaDto.getQnaId());

        // 3단계 검증
        assertTrue(qnaResponse != null);
        // QnaDetailResponse DTO 객체 검증
        // 문의유형 검증
        assertTrue(qnaResponse.getQnaTypeId().equals(qnaTypeActive.getQnaTypeId()));
        assertTrue(qnaResponse.getQnaTypeName().equals(qnaTypeActive.getName()));
        assertTrue(qnaResponse.getQnaTypeIsActive().equals(qnaTypeActive.isActive()));
        // 부모 문의유형 검증
        assertTrue(qnaResponse.getParentQnaTypeId().equals(parentQnaTypeActive.getParentQnaTypeId()));
        assertTrue(qnaResponse.getParentQnaTypeName().equals(parentQnaTypeActive.getName()));
        assertTrue(qnaResponse.getParentQnaTypeIsActive().equals(parentQnaTypeActive.isActive()));
        // 주문ID와 상품ID 검증
        assertTrue(qnaResponse.getOrderId() == selectedQna.getOrderId());
        assertTrue(qnaResponse.getProductId() == selectedQna.getProductId());
        // 1:1 문의 검증
        assertTrue(qnaResponse.getQnaId() == selectedQna.getQnaId());
        assertTrue(qnaResponse.getTitle().equals(selectedQna.getTitle()));
        assertTrue(qnaResponse.getCreatedTime() != null);
        // 첨부파일 URL 리스트 검증
        assertTrue(qnaResponse.getAttachmentPaths().size() == savedFileNames.size());
        // 유저 정보 검증
        assertTrue(qnaResponse.getUserId().equals(selectedQna.getUserId()));
        assertTrue(qnaResponse.getUserName().equals(selectedQna.getUser().getUserName()));
        // 문의답변 검증
        assertTrue(qnaResponse.getAdminId().equals(ADMIN_ID));
        assertTrue(qnaResponse.getAnswerContent().equals(selectedQna.getQnaAnswer().getContent()));
        assertTrue(qnaResponse.getAnswerCreatedTime() != null);
    }

    @DisplayName("1:1 문의 등록 - 성공")
    @Test
    void createQna_1() throws IOException {
        // 1단계 데이터 선택
        // 1-1. QnaCreateRequest 객체 생성
        QnaCreateRequest request = QnaCreateRequest.builder()
                .qnaTypeId(qnaTypeActive.getQnaTypeId())
                .title("1:1 문의 제목")
                .content("1:1 문의 내용")
                .attachmentNames(savedFileNames)
                .build();

        // 2단계 데이터 처리
        qnaService.createQna(request, USER_ID);

        // 3단계 검증
        // 3-1. 1:1 문의 등록 결과 검증
        List<QnaDto> qnaList = qnaDao.selectAllWith(USER_ID, 10, 0,null);
        assertTrue(qnaList != null);
        assertTrue(qnaList.size() == 1);
        QnaDto qnaDto = qnaList.get(0);
        // 3-2. 1:1 문의 검증
        assertTrue(qnaDto.getQnaTypeId().equals(qnaTypeActive.getQnaTypeId()));
        assertTrue(qnaDto.getTitle().equals(request.getTitle()));
        assertTrue(qnaDto.getContent().equals(request.getContent()));
        // 3-1-2. 첨부파일 검증
        assertTrue(qnaDto.getAttachments().size() == savedFileNames.size());
    }

//    @DisplayName("1:1 문의 등록 - 롤백 & 예외")
//    @Test
//    void createQna_2() {
//        // 1단계 데이터 선택
//
//        // 2단계 데이터 처리
//
//        // 3단계 검증
//    }
//
//    @DisplayName("1:1 문의 삭제 - 성공")
//    @Test
//    void deleteQna_1() {
//        // 1단계 데이터 선택
//
//        // 2단계 데이터 처리
//
//        // 3단계 검증
//    }
//
//    @DisplayName("1:1 문의 삭제 - 롤백 & 예외")
//    @Test
//    void deleteQna_2() {
//        // 1단계 데이터 선택
//
//        // 2단계 데이터 처리
//
//        // 3단계 검증
//    }


    // 테스트 목적 함수
    QnaDto createQnaDto(String qnaTypeId, String title,String userId){
        return QnaDto.builder()
                .qnaTypeId(qnaTypeId)
                .title(title)
                .content(title)
                .userId(userId)
                .createdId(userId)
                .updatedId(userId)
                .build();
    }

    void saveTestFile(String fileName) throws IOException {
        fileUploadHandler.saveFile(createTestFile(), fileName);
    }

    File createTestFile() {
        return Paths.get(TEST_FILE_PATH + TEST_FILE_NAME).toFile();
    }
}
