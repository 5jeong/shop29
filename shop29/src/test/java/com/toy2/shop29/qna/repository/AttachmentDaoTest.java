package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.domain.*;
import com.toy2.shop29.qna.repository.attachment.AttachmentDao;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qna.QnaDao;
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
public class AttachmentDaoTest {

    @Autowired
    AttachmentDao attachmentDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    QnaDao qnaDao;
    @Autowired
    UserMapper userMapper;

    private String userId = "admin";
    private ParentQnaTypeDto sampleParentQnaType;
    private QnaTypeDto sampleQnaType;
    private QnaDto sampleQna;

    @BeforeEach
    void setUp(){
        userMapper.deleteUser(userId);
        parentQnaTypeDao.deleteAll();
        qnaTypeDao.deleteAll();
        attachmentDao.deleteAll();
        qnaDao.deleteAll();

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

    @DisplayName("첨부파일 단건 추가 동작 테스트")
    @Test
    void insert_1(){
        // 1단계 데이터 선택
        AttachmentDto dto = AttachmentDto.builder()
                .tableId(sampleQna.getQnaId().toString())
                .tableName(AttachmentTableName.QNA)
                .fileName("fileName")
                .filePath("filePath")
                .size(1000)
                .extension("jpg")
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();

        // 2단계 데이터 처리
        int rowCnt = attachmentDao.insert(dto);

        // 3단계 검증
        assertTrue(rowCnt == 1);
        assertTrue(dto.getAttachmentId() != null);
        assertTrue(dto.getAttachmentId() > 0);
    }

    @DisplayName("첨부파일 다건 추가 동작 테스트")
    @Test
    void insertAll_1(){
        // 1단계 데이터 선택
        List<AttachmentDto> list = new ArrayList<>();
        int size = 5;
        for(int i = 0; i < size; i++){
            AttachmentDto dto = AttachmentDto.builder()
                    .tableId(sampleQna.getQnaId().toString())
                    .tableName(AttachmentTableName.QNA)
                    .fileName("fileName")
                    .filePath("filePath")
                    .size(1000)
                    .extension("jpg")
                    .createdId(userId)
                    .updatedId(userId)
                    .build();
            list.add(dto);
        }

        // 2단계 데이터 처리
        int rowCnt = attachmentDao.insertList(list);

        // 3단계 검증
        List<AttachmentDto> selectedList = attachmentDao.selectAll(null);

        assertTrue(rowCnt == size);
        assertTrue(selectedList.size() == size);
    }

    @DisplayName("첨부파일 이름으로 조회 - 동작 테스트")
    @Test
    void seletByFileName(){
        // 1단계 데이터 선택 - 파일이름이 다른 레코드 2건 추가
        String[] fileNames = {"fileName1", "fileName2"};
        for(String fileName : fileNames){
            AttachmentDto dto = AttachmentDto.builder()
                    .tableId(sampleQna.getQnaId().toString())
                    .tableName(AttachmentTableName.QNA)
                    .fileName(fileName)
                    .filePath("filePath")
                    .size(1000)
                    .extension("jpg")
                    .isActive(true)
                    .createdId(userId)
                    .updatedId(userId)
                    .build();
            attachmentDao.insert(dto);
        }

        // 2단계 데이터 처리
        AttachmentDto selectedDto = attachmentDao.selectByFileName(fileNames[0]);

        // 3단계 검증
        assertTrue(selectedDto != null);
        assertTrue(selectedDto.getFileName().equals(fileNames[0]));
    }

    @DisplayName("첨부파일 수정 동작 테스트")
    @Test
    void update_1(){
        // 1단계 데이터 선택
        AttachmentDto dto = AttachmentDto.builder()
                .tableId(sampleQna.getQnaId().toString())
                .tableName(AttachmentTableName.QNA)
                .fileName("fileName")
                .filePath("filePath")
                .size(1000)
                .extension("jpg")
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();
        attachmentDao.insert(dto);
        AttachmentDto selectedDto = attachmentDao.select(dto.getAttachmentId(), null);

        // 2단계 데이터 처리
        selectedDto.setIsActive(false);
        attachmentDao.update(selectedDto);

        // 3단계 검증
        AttachmentDto updatedDto = attachmentDao.select(selectedDto.getAttachmentId(), null);
        assertTrue(updatedDto != null);
        assertTrue(updatedDto.getIsActive() == false);
    }

    @DisplayName("첨부파일 여러건 softDelete 동작 테스트")
    @Test
    void softDeleteList_1(){
        // 1단계 데이터 선택
        // 1-1. 첨부파일 여러건 추가
        List<AttachmentDto> list = new ArrayList<>();
        int size = 5;
        // 파일 이름 다르게
        // select order by 추가 여부 확인 -> select 순서 및 요소 일치여부 확인
        for(int i = 0; i < size; i++){
            AttachmentDto dto = AttachmentDto.builder()
                    .tableId(sampleQna.getQnaId().toString())
                    .tableName(AttachmentTableName.QNA)
                    .fileName("fileName")
                    .filePath("filePath")
                    .size(1000)
                    .extension("jpg")
                    .isActive(true)
                    .createdId(userId)
                    .updatedId(userId)
                    .build();
            list.add(dto);
        }
        int insertCnt = attachmentDao.insertList(list);
        assertTrue(insertCnt == size);

        // 1-2. 추가된 첨부파일 리스트 조회
        List<AttachmentDto> selectedList = attachmentDao.selectAll(null);
        assertTrue(selectedList.size() == size);
        List<Integer> attachmentIds = new ArrayList<>();
        for(AttachmentDto dto : selectedList){
            attachmentIds.add(dto.getAttachmentId());
        }

        // 2단계 데이터 처리
        int rowCnt = attachmentDao.softDeleteList(attachmentIds);

        // 3단계 검증
        assertTrue(rowCnt == size);
        List<AttachmentDto> deletedList = attachmentDao.selectAll(null);
        assertTrue(deletedList.size() == size);
        for(AttachmentDto dto : deletedList){
            assertTrue(dto.getIsActive() == false);
        }

    }


}
