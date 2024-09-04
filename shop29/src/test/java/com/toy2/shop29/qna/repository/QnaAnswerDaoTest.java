package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaAnswerDto;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnaasnwer.QnaAnswerDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaAnswerDaoTest {

    @Autowired
    QnaAnswerDao qnaAnswerDao;
    @Autowired
    QnaDao qnaDao;
    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    UserMapper userMapper;

    private String userId = "admin";
    private ParentQnaTypeDto sampleParentQnaType;
    private QnaTypeDto sampleQnaType;
    private QnaDto sampleQna;

    @BeforeEach
    void setUp(){
        qnaTypeDao.deleteAll();
        parentQnaTypeDao.deleteAll();
        qnaAnswerDao.deleteAll();
        qnaDao.deleteAll();
        userMapper.deleteUser(userId);

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId(userId);
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

        sampleParentQnaType = ParentQnaTypeDto.builder()
                .parentQnaTypeId("parentQnaTypeId")
                .name("name")
                .description("description")
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();
        parentQnaTypeDao.insert(sampleParentQnaType);

        sampleQnaType = QnaTypeDto.builder()
                .qnaTypeId("qnaTypeId")
                .parentQnaTypeId(sampleParentQnaType.getParentQnaTypeId())
                .name("name")
                .description("description")
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();
        qnaTypeDao.insert(sampleQnaType);

        sampleQna = QnaDto.builder()
                .userId(userId)
                .qnaTypeId(sampleQnaType.getQnaTypeId())
                .title("title")
                .content("content")
                .createdId(userId)
                .updatedId(userId)
                .build();
        qnaDao.insert(sampleQna);
    }


    @DisplayName("insert 동작 테스트")
    @Test
    void insert_1(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .adminId(userId)
                .qnaId(sampleQna.getQnaId())
                .content("content")
                .createdId(userId)
                .updatedId(userId)
                .build();

        // 2단계 데이터 처리
        int rowCnt = qnaAnswerDao.insert(dto);

        // 3단계 검증
        assertTrue(rowCnt == 1);
        assertTrue(qnaAnswerDao.count() == 1);
    }

    @DisplayName("select 동작 테스트")
    @Test
    void select_1(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .adminId(userId)
                .qnaId(sampleQna.getQnaId())
                .content("content")
                .createdId(userId)
                .updatedId(userId)
                .build();
        int rowCnt = qnaAnswerDao.insert(dto);
        assertTrue(rowCnt == 1);

        // 2단계 데이터 처리
        QnaAnswerDto selectedDto = qnaAnswerDao.select(dto.getQnaAnswerId(), null);

        // 3단계 검증
        assertTrue(selectedDto != null);
        assertTrue(selectedDto.getQnaAnswerId().equals(dto.getQnaAnswerId()));
    }

    @DisplayName("selectAll 동작 테스트")
    @Test
    void selectAll_1(){
        // 1단계 데이터 선택
        List<QnaAnswerDto> list = new ArrayList<>();
        int size = 5;
        for(int i = 0; i < size; i++){
            QnaAnswerDto dto = QnaAnswerDto.builder()
                    .adminId(userId)
                    .qnaId(sampleQna.getQnaId())
                    .content("content")
                    .createdId(userId)
                    .updatedId(userId)
                    .build();
            list.add(dto);
            qnaAnswerDao.insert(dto);
        }

        // 2단계 데이터 처리
        List<QnaAnswerDto> selectedList = qnaAnswerDao.selectAll(null);

        // 3단계 검증
        assertTrue(selectedList.size() == size);
    }

    @DisplayName("update 동작 테스트")
    @Test
    void update_1(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .adminId(userId)
                .qnaId(sampleQna.getQnaId())
                .content("content")
                .createdId(userId)
                .updatedId(userId)
                .build();
        qnaAnswerDao.insert(dto);
        QnaAnswerDto selectedDto = qnaAnswerDao.select(dto.getQnaAnswerId(), null);
        selectedDto.setContent("updatedContent");

        // 2단계 데이터 처리
        int rowCnt = qnaAnswerDao.update(selectedDto);

        // 3단계 검증
        QnaAnswerDto updatedDto = qnaAnswerDao.select(dto.getQnaAnswerId(), null);
        assertTrue(rowCnt == 1);
        assertTrue(updatedDto.getContent().equals(selectedDto.getContent()));
    }

    @DisplayName("softDelete 동작 테스트")
    @Test
    void softDelete_1(){
        // 1단계 데이터 선택
        QnaAnswerDto dto = QnaAnswerDto.builder()
                .adminId(userId)
                .qnaId(sampleQna.getQnaId())
                .content("content")
                .createdId(userId)
                .updatedId(userId)
                .build();
        qnaAnswerDao.insert(dto);

        // 2단계 데이터 처리
        int rowCnt = qnaAnswerDao.softDelete(dto.getQnaAnswerId(), userId);

        // 3단계 검증
        assertTrue(rowCnt == 1);
        QnaAnswerDto selectedDto = qnaAnswerDao.select(dto.getQnaAnswerId(), null);
        assertTrue(selectedDto != null);
        assertTrue(selectedDto.isDeleted() == true);
        assertTrue(selectedDto.getDeletedId() != null);
        assertTrue(selectedDto.getDeletedId().equals(userId));
        assertTrue(selectedDto.getDeletedTime() != null);
    }


}
