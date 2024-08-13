package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaTypeDaoTest {

    @Autowired
    private QnaTypeDao qnaTypeDao;
    @Autowired
    private ParentQnaTypeDao parentQnaTypeDao;

    /**
        "admin" : User 테이블 內 고정 데이터
     */
    private String userId = "admin";

    // 테스트 실행 전, QnaType 테이블 레코드 전체 삭제
    @BeforeEach
    void deleteAll(){
        qnaTypeDao.deleteAll();
        assertTrue(qnaTypeDao.count() == 0);
        parentQnaTypeDao.deleteAll();
        assertTrue(parentQnaTypeDao.count() == 0);
    }

    @DisplayName("문의유형 레코드 추가 - 성공")
    @Test
    void insert_1(){
        // 1단계 데이터 선택 -> dto 객체 생성
        QnaTypeDto dto = createSampleDto("TEST_ID","PARENT_TEST_ID");

        // 2단계 데이터 처리 -> 삽입
        int rowCnt = qnaTypeDao.insert(dto);

        // 3단계 검증 -> (1) 결과값 == 1, (2) 전체 레코드 수 == 1
        assertTrue(rowCnt == 1);
        assertTrue(qnaTypeDao.count() == 1);
    }

    @DisplayName("문의유형 레코드 추가 - 예외던짐 - NotNull 컬럼이 Null")
    @ParameterizedTest()
    @ValueSource(strings = {
            "qnaTypeId",
            "parentQnaTypeId",
            "name",
            "description",
            "createdId",
            "updatedId"
    })
    void insert_2(String columnName){
        // 1단계 데이터 선택 -> dto 객체 생성
        QnaTypeDto dto = createSampleDto("TEST_ID","PARENT_TEST_ID");

        // 2단계 데이터 처리 -> NotNull 컬럼 Null 처리
        switch (columnName){
            case "qnaTypeId": dto.setQnaTypeId(null); break;
            case "parentQnaTypeId": dto.setParentQnaTypeId(null); break;
            case "name": dto.setName(null); break;
            case "description": dto.setDescription(null); break;
            case "createdId": dto.setCreatedId(null); break;
            case "updatedId": dto.setUpdatedId(null); break;
        }

        // 3단계 검증 -> 예외 발생
        assertThrows(DataAccessException.class, () -> {qnaTypeDao.insert(dto);});
    }

    @DisplayName("문의유형 레코드 추가 - 예외던짐 - 기본키 컬럼 중복")
    @Test
    void insert_3() {
        // 1단계 데이터 선택 -> dto 객체 생성
        QnaTypeDto dto = createSampleDto("TEST_ID","PARENT_TEST_ID");
        qnaTypeDao.insert(dto);

        // 2단계 데이터 처리 -> 중복된 기본키를 가진 객체 삽입

        // 3단계 검증 -> 예외 발생
        assertThrows(DataAccessException.class, () -> {qnaTypeDao.insert(dto);});
    }

    @DisplayName("select 기능 테스트")
    @Test
    void select(){
        // 1단계 데이터 선택 -> dto 객체 생성
        ParentQnaTypeDto parentDto = createSampleParentDto("PARENT_TEST_ID");
        parentQnaTypeDao.insert(parentDto);
        QnaTypeDto dto = createSampleDto("TEST_ID","PARENT_TEST_ID");
        qnaTypeDao.insert(dto);

        // 2단계 데이터 처리 -> 조회
        QnaTypeDto selectedDto = qnaTypeDao.select("TEST_ID", true, null);

        // 3단계 검증 -> (1) 조회된 객체의 qnaTypeId == "TEST_ID"
        assertTrue(selectedDto.getQnaTypeId().equals("TEST_ID"));
    }

    @DisplayName("selectAll 성공 테스트")
    @Test
    void selectAll(){
        // 1단계 데이터 선택 -> dto 객체 생성
        ParentQnaTypeDto parentDto = createSampleParentDto("PARENT_TEST_ID");
        parentQnaTypeDao.insert(parentDto);
        for(int i = 0; i < 3; i++){
            QnaTypeDto dto = createSampleDto("TEST_ID" + i,"PARENT_TEST_ID");
            qnaTypeDao.insert(dto);
        }

        // 2단계 데이터 처리 -> 전체 조회
        List<QnaTypeDto> list = qnaTypeDao.selectAll(true, null);

        // 3단계 검증 -> (1) 조회된 레코드 수 == 3
        assertTrue(list.size() == 3);
        for(int i = 0; i < 3; i++){
            assertTrue(list.get(i).getParentQnaType() != null);
            assertTrue(list.get(i).getParentQnaType().getName() != null);
        }
    }


    // 문의유형 객체 생성
    private QnaTypeDto createSampleDto(String qnaTypeId, String parentQnaTypeId){
        return QnaTypeDto.builder()
                .qnaTypeId(qnaTypeId)
                .parentQnaTypeId(parentQnaTypeId)
                .name("테스트")
                .description("테스트")
                .isOrderIdActive(false)
                .isProductIdActive(false)
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();
    }
    // 부모문의유형 객체 생성
    private ParentQnaTypeDto createSampleParentDto(String parentQnaTypeId){
        return ParentQnaTypeDto.builder()
                .parentQnaTypeId(parentQnaTypeId)
                .name("테스트")
                .description("테스트")
                .isActive(true)
                .createdId(userId)
                .updatedId(userId)
                .build();
    }
}