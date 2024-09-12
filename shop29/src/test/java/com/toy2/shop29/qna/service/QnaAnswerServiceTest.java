package com.toy2.shop29.qna.service;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaAnswerDto;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnaasnwer.QnaAnswerDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.qnaanswer.QnaAnswerService;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaAnswerServiceTest {

    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    QnaDao qnaDao;
    @Autowired
    QnaAnswerDao qnaAnswerDao;
    @Autowired
    QnaAnswerService qnaAnswerService;
    @Autowired
    UserMapper userMapper;

    private String ADMIN_ID = "admin";
    private String OTHER_ADMIN_ID = "admin2";
    private String USER_ID = "test1";
    QnaDto qnaDto;


    @BeforeEach
    void before(){
        // 0. 데이터 초기화
        qnaAnswerDao.deleteAll();
        qnaDao.deleteAll();
        qnaTypeDao.deleteAll();
        parentQnaTypeDao.deleteAll();
        userMapper.deleteUser(ADMIN_ID);
        userMapper.deleteUser(OTHER_ADMIN_ID);
        userMapper.deleteUser("test2");
        userMapper.deleteUser(USER_ID);

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
        map.put("userRole", "ROLE_ADMIN");
        assertTrue(userMapper.updateUserRoleForTest(map) == 1);

        UserRegisterDto userRegisterDto2 = new UserRegisterDto();
        userRegisterDto2.setUserId(OTHER_ADMIN_ID);
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
        Map<String,Object> map2 = new HashMap<>();
        map2.put("userId", OTHER_ADMIN_ID);
        map2.put("userRole", "관리자");
        assertTrue(userMapper.updateUserRoleForTest(map2) == 1);

        UserRegisterDto userRegisterDto3 = new UserRegisterDto();
        userRegisterDto3.setUserId(USER_ID);
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
        ParentQnaTypeDto parentQnaTypeDto = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        parentQnaTypeDao.insert(parentQnaTypeDto);
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
        qnaTypeDao.insert(qnaTypeDto);
        // 3. 문의글 등록
        QnaDto qDto = QnaDto.builder()
                .userId(ADMIN_ID)
                .qnaTypeId("QNA_TYPE_1")
                .title("문의글 제목")
                .content("문의글 내용")
                .build();
        qnaDao.insert(qDto);
        qnaDto = qDto;
    }

    @DisplayName("문의 답변 등록 - 성공")
    @Test
    void createQnaAnswer_1(){
        // 1. 데이터 선택
        // 2. 데이터 처리
        qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(), ADMIN_ID, "답변 내용");
        QnaDto qDto = qnaDao.selectWith(qnaDto.getQnaId());
        // 3. 검증
        assertTrue(qDto != null);
        assertTrue(qDto.getQnaAnswer() != null);
        assertTrue(qDto.getQnaAnswer().getQnaId().equals(qnaDto.getQnaId()));
    }

    @DisplayName("문의 답변 등록 - 예외 - 문의글에 대한 답변이 이미 있을 경우")
    @Test
    void createQnaAnswer_2(){
        // 1. 데이터 선택
        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(qnaAnswerDto) == 1);
        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(), ADMIN_ID, "답변 내용");
        });
    }

    @DisplayName("문의 답변 등록 - 예외 - adminId에 해당하는 계정의 권한이 '관리자'가 아닐 경우")
    @Test
    void createQnaAnswer_3(){
        // 1. 데이터 선택
        String userId = USER_ID;

        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId(), userId, "답변 내용");
        });
    }

    @DisplayName("문의 답변 등록 - 예외 - 문의글이 존재하지 않을 경우")
    @Test
    void createQnaAnswer_4(){
        // 1. 데이터 선택 -> init()에 의해 문의글 레코드는 1건만 존재

        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.createQnaAnswer(qnaDto.getQnaId() + 100 , ADMIN_ID, "답변 내용");
        });
    }

    @DisplayName("문의 답변 수정 - 성공")
    @Test
    void updateQnaAnswer_1(){
        // 1. 데이터 선택
        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(qnaAnswerDto) == 1);
        QnaAnswerDto qaDto = qnaAnswerDao.select(qnaAnswerDto.getQnaAnswerId(), false);
        String userId = ADMIN_ID;
        String content = "수정된 답변 내용";

        // 2. 데이터 처리
        qnaAnswerService.updateQnaAnswer(qaDto.getQnaAnswerId(), ADMIN_ID, content);

        // 3. 검증
        QnaAnswerDto updatedQnaAnswerDto = qnaAnswerDao.select(qaDto.getQnaAnswerId(), false);
        assertTrue(updatedQnaAnswerDto != null);
        assertTrue(updatedQnaAnswerDto.getContent().equals(content));
    }

    @DisplayName("문의 답변 수정 - 예외 - 답변이 존재하지 않을 경우")
    @Test
    void updateQnaAnswer_2(){
        // 1. 데이터 선택 -> init()에 의해 답변 레코드는 모두 삭제
        int qnaAnswerId = 1;
        String userId = ADMIN_ID;

        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.updateQnaAnswer(qnaAnswerId, userId, "답변 내용");
        });
    }

    @DisplayName("문의 답변 수정 - 예외 - adminId에 해당하는 계정의 권한이 '관리자'가 아닐 경우")
    @Test
    void updateQnaAnswer_3(){
        // 1. 데이터 선택
        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(qnaAnswerDto) == 1);
        String userId = USER_ID;

        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.updateQnaAnswer(qnaAnswerDto.getQnaAnswerId(), userId, "답변 내용");
        });
    }

    @DisplayName("문의 답변 수정 - 예외 - 답변의 adminId와 adminId가 다를 경우")
    @Test
    void updateQnaAnswer_4(){
        // 1. 데이터 선택
        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(qnaAnswerDto) == 1);
        String userId = OTHER_ADMIN_ID;

        // 2. 데이터 처리
        // 3. 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.updateQnaAnswer(qnaAnswerDto.getQnaAnswerId(), userId, "답변 내용");
        });
    }

    @DisplayName("문의 답변 삭제 - 성공")
    @Test
    void deleteQnaAnswer_1(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(dto) == 1);

        // 2단계 데이터 처리
        qnaAnswerService.deleteQnaAnswer(dto.getQnaAnswerId(), ADMIN_ID);

        // 3단계 검증
        QnaAnswerDto deletedQnaAnswerDto = qnaAnswerDao.select(dto.getQnaAnswerId(), true);
        assertTrue(deletedQnaAnswerDto != null);
        assertTrue(deletedQnaAnswerDto.isDeleted());
    }

    @DisplayName("문의 답변 삭제 - 예외 - 답변이 존재하지 않을 경우")
    @Test
    void deleteQnaAnswer_2(){
        // 1단계 데이터 선택 -> init()에 의해 답변 레코드는 모두 삭제
        int qnaAnswerId = 1;

        // 2단계 데이터 처리
        // 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.deleteQnaAnswer(qnaAnswerId, ADMIN_ID);
        });
    }

    @DisplayName("문의 답변 삭제 - 예외 - adminId에 해당하는 계정의 권한이 '관리자'가 아닐 경우")
    @Test
    void deleteQnaAnswer_3(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(dto) == 1);
        String userId = USER_ID;

        // 2단계 데이터 처리
        // 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.deleteQnaAnswer(dto.getQnaAnswerId(), userId);
        });
    }

    @DisplayName("문의 답변 삭제 - 예외 - 답변의 adminId와 adminId가 다를 경우")
    @Test
    void deleteQnaAnswer_4(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .qnaId(qnaDto.getQnaId())
                .adminId(ADMIN_ID)
                .content("답변 내용")
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(dto) == 1);
        String userId = OTHER_ADMIN_ID;

        // 2단계 데이터 처리
        // 3단계 검증
        assertThrows(IllegalArgumentException.class, () -> {
            qnaAnswerService.deleteQnaAnswer(dto.getQnaAnswerId(), userId);
        });
    }
}
