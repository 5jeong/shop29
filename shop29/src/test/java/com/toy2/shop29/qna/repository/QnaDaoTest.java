package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.domain.*;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
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
public class QnaDaoTest {

    @Autowired
    private ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    private QnaTypeDao qnaTypeDao;
    @Autowired
    private QnaDao qnaDao;
    @Autowired
    private QnaAnswerDao qnaAnswerDao;
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private UserMapper userMapper;

    private String USER_ID = "admin";
    private ParentQnaTypeDto sampleParentQnaType;
    private QnaTypeDto sampleQnaType;

    void setUpQnaType(){
        sampleParentQnaType = ParentQnaTypeDto.builder()
                .parentQnaTypeId("parentQnaTypeId")
                .name("name")
                .description("description")
                .isActive(true)
                .createdId(USER_ID)
                .updatedId(USER_ID)
                .build();
        parentQnaTypeDao.insert(sampleParentQnaType);
        sampleQnaType = QnaTypeDto.builder()
                .qnaTypeId("qnaTypeId")
                .parentQnaTypeId(sampleParentQnaType.getParentQnaTypeId())
                .name("name")
                .description("description")
                .isActive(true)
                .createdId(USER_ID)
                .updatedId(USER_ID)
                .build();
        qnaTypeDao.insert(sampleQnaType);
    }

    @BeforeEach
    void before(){
        parentQnaTypeDao.deleteAll();
        qnaTypeDao.deleteAll();
        attachmentDao.deleteAll();
        qnaAnswerDao.deleteAll();
        qnaDao.deleteAll();

        userMapper.deleteUser(USER_ID);
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUserId(USER_ID);
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

        setUpQnaType();
    }

    // 1단계 데이터 선택
    // 2단계 데이터 처리
    // 3단계 검증

    @DisplayName("insert 기능 테스트")
    @Test
    void insert(){
        // 1단계 데이터 선택 -> dto 객체 생성
        QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());

        // 2단계 데이터 처리
        int rowCnt = qnaDao.insert(dto);

        // 3단계 검증
        assertTrue(rowCnt == 1);
        assertTrue(dto.getQnaId() != null);
        assertTrue(dto.getQnaId() > 0);
    }

    @DisplayName("selectAllWith 기능 테스트")
    @Test
    void selectAllWith_1(){
        // 1단계 데이터 선택
        int size = 10;
        List<QnaDto> list = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
            list.add(dto);
            qnaDao.insert(dto);

            QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                    .adminId(USER_ID)
                    .qnaId(dto.getQnaId())
                    .content("답변")
                    .createdId(USER_ID)
                    .updatedId(USER_ID)
                    .build();
            qnaAnswerDao.insert(qnaAnswerDto);

            AttachmentDto attachmentDto = AttachmentDto.builder()
                    .tableId(dto.getQnaId().toString())
                    .tableName(AttachmentTableName.QNA)
                    .filePath("path")
                    .fileName("name")
                    .size(100)
                    .extension("jpg")
                    .createdId(USER_ID)
                    .updatedId(USER_ID)
                    .build();
            assertTrue(attachmentDao.insert(attachmentDto) == 1);
            assertTrue(attachmentDao.insert(attachmentDto) == 1);
        }

        // 2단계 데이터 처리
        int limit = 4;
        int offset = 3;
        boolean isActive = true;
        String userId = USER_ID;
        List<QnaDto> selectedList = qnaDao.selectAllWith(userId, limit, offset, isActive);

        // 3단계 검증
        assertTrue(selectedList != null);
        assertTrue(selectedList.size() == limit);
        int baseQnaId = list.get(0).getQnaId();
        for(int i=0; i<selectedList.size(); i++){
            QnaDto selectedDto = selectedList.get(i);
            assertTrue(selectedDto.getUserId().equals(userId));
            assertTrue(selectedDto.getQnaTypeId().equals(sampleQnaType.getQnaTypeId()));
            assertTrue(selectedDto.getQnaAnswer() != null);
            assertTrue(selectedDto.getQnaType() != null);
            assertTrue(selectedDto.getAttachments().size() == 2);
        }
    }

    @DisplayName("selectAllWith 기능 테스트 - 답변과 첨부파일이 없는 경우")
    @Test
    void selectAllWith_2(){
        // 1단계 데이터 선택
        int size = 10;
        List<QnaDto> list = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
            list.add(dto);
            qnaDao.insert(dto);
        }

        // 2단계 데이터 처리
        int limit = 4;
        int offset = 3;
        boolean isActive = true;
        String userId = USER_ID;
        List<QnaDto> selectedList = qnaDao.selectAllWith(userId, limit, offset, isActive);

        // 3단계 검증
        assertTrue(selectedList != null);
        assertTrue(selectedList.size() == limit);
        int baseQnaId = list.get(0).getQnaId();
        for(int i=0; i<selectedList.size(); i++){
            QnaDto selectedDto = selectedList.get(i);
            assertTrue(selectedDto.getUserId().equals(userId));
            assertTrue(selectedDto.getQnaTypeId().equals(sampleQnaType.getQnaTypeId()));
            assertTrue(selectedDto.getQnaAnswer() == null);
            assertTrue(selectedDto.getAttachments().size() == 0);
        }
    }

//    @DisplayName("selectAllWithFilter 기능 테스트")
//    @Test
//    void selectAllWithFilter(){
//        // 1단계 데이터 선택
//        int size = 10;
//        List<QnaDto> list = new ArrayList<>(size);
//        for(int i=0; i<size; i++){
//            QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
//            list.add(dto);
//            qnaDao.insert(dto);
//
//            QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
//                    .adminId(USER_ID)
//                    .qnaId(dto.getQnaId())
//                    .content("답변")
//                    .createdId(USER_ID)
//                    .updatedId(USER_ID)
//                    .build();
//            qnaAnswerDao.insert(qnaAnswerDto);
//        }
//
//        // 2단계 데이터 처리
//        int limit = 4;
//        int offset = 3;
//        boolean isActive = true;
//        List<QnaDto> selectedList = qnaDao.selectAllWithFilter(limit, offset, null, null, isActive);
//
//        // 3단계 검증
//        assertTrue(selectedList != null);
//        assertTrue(selectedList.size() == limit);
//        int baseQnaId = list.get(0).getQnaId();
//        for(int i=0; i<selectedList.size(); i++){
//            QnaDto selectedDto = selectedList.get(i);
//            assertTrue(selectedDto.getQnaId().equals(baseQnaId + offset + i));
//            assertTrue(selectedDto.getQnaTypeId().equals(sampleQnaType.getQnaTypeId()));
//            assertTrue(selectedDto.getUser() != null);
//            assertTrue(selectedDto.getQnaAnswer() != null);
//            assertTrue(selectedDto.getQnaType() != null);
//        }
//    }

    @DisplayName("selectWith 기능 테스트")
    @Test
    void selectWith_1(){
        // 1단계 데이터 선택
        QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
        qnaDao.insert(dto);

        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .adminId(USER_ID)
                .qnaId(dto.getQnaId())
                .content("답변")
                .createdId(USER_ID)
                .updatedId(USER_ID)
                .build();
        assertTrue(qnaAnswerDao.insert(qnaAnswerDto) == 1);

        int attachmentSize = 2;
        for(int i = 0; i < attachmentSize; i++){
            AttachmentDto attachmentDto = AttachmentDto.builder()
                    .tableId(dto.getQnaId().toString())
                    .tableName(AttachmentTableName.QNA)
                    .filePath("path")
                    .fileName("name")
                    .size(100)
                    .extension("jpg")
                    .createdId(USER_ID)
                    .updatedId(USER_ID)
                    .build();
            assertTrue(attachmentDao.insert(attachmentDto) == 1);
        }

        // 2단계 데이터 처리
        QnaDto selectedDto = qnaDao.selectWith(dto.getQnaId());

        // 3단계 검증
        assertTrue(selectedDto != null);
        assertTrue(selectedDto.getQnaId().equals(dto.getQnaId()));
        assertTrue(selectedDto.getUserId().equals(USER_ID));
        assertTrue(selectedDto.getQnaTypeId().equals(sampleQnaType.getQnaTypeId()));
        assertTrue(selectedDto.getQnaType() != null);
        assertTrue(selectedDto.getQnaType().getParentQnaType() != null);
        assertTrue(selectedDto.getQnaType().getParentQnaType().getParentQnaTypeId()
                .equals(sampleParentQnaType.getParentQnaTypeId()));
        assertTrue(selectedDto.getQnaAnswer() != null);
        assertTrue(selectedDto.getAttachments().size() == attachmentSize);
    }

    @DisplayName("update 기능 테스트")
    @Test
    void update(){
        // 1단계 데이터 선택 -> dto 객체 생성
        QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
        qnaDao.insert(dto);

        // 2단계 데이터 처리
        dto.setTitle("수정된 제목");
        dto.setContent("수정된 내용");
        int rowCnt = qnaDao.update(dto);

        // 3단계 검증
        assertTrue(rowCnt == 1);
        QnaDto selectedDto = qnaDao.select(dto.getQnaId(),null);
        assertTrue(selectedDto.getTitle().equals(dto.getTitle()));
        assertTrue(selectedDto.getContent().equals(dto.getContent()));
    }

    @DisplayName("softDelete 기능 테스트")
    @Test
    void softDelete(){
        // 1단계 데이터 선택
        QnaDto dto = createSampleDto(USER_ID, sampleQnaType.getQnaTypeId());
        qnaDao.insert(dto);

        // 2단계 데이터 처리
        qnaDao.softDelete(dto.getQnaId(), dto.getUserId());

        // 3단계 검증
        QnaDto selectedDto = qnaDao.select(dto.getQnaId(),null);
        assertTrue(selectedDto.isDeleted());
    }

    QnaDto createSampleDto(String userId, String qnaTypeId){
        QnaDto dto = QnaDto.builder()
                .userId(userId)
                .qnaTypeId(qnaTypeId)
                .title("제목")
                .content("내용")
                .orderId(null)
                .productId(null)
                .build();
        return dto;
    }
}
