package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParentQnaTypeDaoTest {
    /*
        * 테스트 시나리오 목록
        - CREATE
            1. insert
                시나리오 1) 부모 문의유형 레코드 추가 - 성공
                시나리오 2) 부모 문의유형 레코드 추가 - 예외던짐 - NotNull 컬럼이 Null
                시나리오 3) 부모 문의유형 레코드 추가 - 예외던짐 - 기본키 컬럼 중복
        - READ
            1. select
                시나리오 1) 부모 문의유형 레코드 단건 조회 - 성공 - ParentQnaTypeDto 객체 반환
                시나리오 2) 부모 문의유형 레코드 단건 조회 - 성공 - 사용여부가 True인 레코드만 조회
                시나리오 3) 부모 문의유형 레코드 단건 조회 - 실패 - null 반환
            2. selectAll
                시나리오 1) 부모 문의유형 레코드 전체 조회 - 성공 - List<ParentQnaTypeDto> 객체 반환
                시나리오 2) 부모 문의유형 레코드 전체 조회 - 성공 - 사용여부가 True인 레코드들만 조회
                시나리오 3) 부모 문의유형 레코드 전체 조회 - 실패 - 빈 List 반환
            3. count
                시나리오 1) 부모 문의유형 레코드 수 조회 - 성공
        - UPDATE
            1. update
                시나리오 1) 부모 문의유형 레코드 수정 - 성공
                시나리오 2) 부모 문의유형 레토드 수정 - 예외던짐 - 조회된 레코드 없음
                시나리오 3) 부모 문의유형 레코드 수정 - 예외던짐 - NotNull 컬럼이 Null
        - DELETE
            1. delete
                시나리오 1) 부모 문의유형 레코드 삭제 - 성공
                시나리오 2) 부모 문의유형 레코드 삭제 - 실패 - 조회된 레코드 없음
            2. deleteAll
                시나리오 1) 부모 문의유형 레코드 전체 삭제 - 성공
     */


    @Autowired
    private ParentQnaTypeDao parentQnaTypeDao;

    /**
        "admin" : User 테이블 內 고정 데이터
     */
    private String userId = "admin";


    // 테스트 실행 전, ParentQnaType 테이블 레코드 전체 삭제
    @BeforeEach
    void deleteAll(){
        parentQnaTypeDao.deleteAll();
        assertTrue(parentQnaTypeDao.count() == 0);
    }


    @DisplayName("부모 문의유형 레코드 추가 - 성공")
    @Test
    void insert_1(){
        // 1단계 데이터 선택 -> dto 객체 생성
        ParentQnaTypeDto dto = createSampleDto("TEST_ID");

        // 2단계 데이터 처리 -> 삽입
        int rowCnt = parentQnaTypeDao.insert(dto);

        // 3단계 검증 -> (1) 결과값 == 1, (2) 전체 레코드 수 == 1
        assertTrue(rowCnt == 1);
        assertTrue(parentQnaTypeDao.count() == 1);
    }

    @DisplayName("부모 문의유형 레코드 추가 - 예외던짐 - NotNull 컬럼이 Null")
    @ParameterizedTest()
    @ValueSource(strings = {
            "parentQnaTypeId",
            "name",
            "description",
            "createdId",
            "updatedId"
    })
    void insert_2(String columnName){
        parentQnaTypeDao.deleteAll();
        assertTrue(parentQnaTypeDao.count() == 0);

        // 1단계 데이터 선택 -> dto 객체 생성
        ParentQnaTypeDto dto = createSampleDto("TEST_ID");

        // 2단계 데이터 처리 -> NotNull 컬럼 Null 처리
        switch (columnName){
            case "parentQnaTypeId": dto.setParentQnaTypeId(null); break;
            case "name": dto.setName(null); break;
            case "description": dto.setDescription(null); break;
            case "createdId": dto.setCreatedId(null); break;
            case "updatedId": dto.setUpdatedId(null); break;
        }

        // 3단계 검증 -> 예외 발생
        assertThrows(DataAccessException.class, () -> {parentQnaTypeDao.insert(dto);});
    }

    @DisplayName("부모 문의유형 레코드 추가 - 예외던짐 - 기본키 컬럼 중복")
    @Test
    void insert_3() {
        parentQnaTypeDao.deleteAll();
        assertTrue(parentQnaTypeDao.count() == 0);

        // 1단계 데이터 선택 -> dto 객체 생성
        ParentQnaTypeDto dto = createSampleDto("TEST_ID");
        parentQnaTypeDao.insert(dto);

        // 2단계 데이터 처리 -> 중복된 기본키를 가진 객체 삽입

        // 3단계 검증 -> 예외 발생
        assertThrows(DataAccessException.class, () -> {parentQnaTypeDao.insert(dto);});
    }

    @DisplayName("부모 문의유형 레코드 단건 조회 - 성공 - ParentQnaTypeDto 객체 반환")
    @Test
    void select_1(){
        // 1단계 데이터 선택 -> (1) dto 객체 3건 생성, (2) 조회할 ID 지정
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }

        String targetId = "TEST_2"; // (2) 조회할 parentQnaTypeId 지정

        // 2단계 데이터 처리 -> 삽입된 레코드들 중, 특정 레코드 조회
        ParentQnaTypeDto selectedDto = parentQnaTypeDao.select(targetId);

        // 3단계 검증 -> (1)반환된 객체는 null이 아님, (2) 반환된 객체의 ID가 targetId와 일치
        assertTrue(selectedDto != null);
        assertTrue(selectedDto.getParentQnaTypeId().equals(targetId));
    }

    @DisplayName("부모 문의유형 레코드 단건 조회 - 성공 - 사용여부가 True인 레코드만 조회")
    @Test
    void select_2(){
        // 1단계 데이터 선택 -> 사용여부가 false 레코드 1건 삽입
        String id = "TEST";
        ParentQnaTypeDto dto = createSampleDto(id);
        dto.setIsActive(false);
        parentQnaTypeDao.insert(dto);

        // 2단계 데이터 처리 -> 사용여부가 true인 레코드만 조회
        ParentQnaTypeDto selectedDto = parentQnaTypeDao.select(id);

        // 3단계 검증 -> 반환된 객체는 null
        assertTrue(selectedDto == null);
    }

    @DisplayName("부모 문의유형 레코드 단건 조회 - 실패 - null 반환")
    @Test
    void select_3(){
        // 1단계 데이터 선택 -> (1) dto 객체 3건 생성, (2) 존재하지 않는 ID 지정
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }

        String targetId = "TEST_4";

        // 2단계 데이터 처리 -> 삽입된 레코드들 중, 존재하지 않는 ID로 조회
        ParentQnaTypeDto selectedDto = parentQnaTypeDao.select(targetId);

        // 3단계 검증 -> 반환된 객체는 null
        assertTrue(selectedDto == null);
    }

    @DisplayName("부모 문의유형 레코드 전체 조회 - 성공 - List<ParentQnaTypeDto> 객체 반환")
    @Test
    void selectAll_1(){
        // 1단계 데이터 선택 -> dto 객체 3건 생성
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }

        // 2단계 데이터 처리 -> 테이블 전체 조회
        List<ParentQnaTypeDto> list = parentQnaTypeDao.selectAll();

        // 3단계 검증 -> (1)반환된 List 객체는 null이 아님, (2) List 객체의 크기가 3
        assertTrue(list != null);
        assertTrue(list.size() == 3);
    }

    @DisplayName("부모 문의유형 레코드 전체 조회 - 성공 - 사용여부가 True인 레코드들만 조회")
    @Test
    void selectAll_2(){
        // 1단계 데이터 선택 -> 사용여부가 true 레코드 3건, false 레코드 1건 삽입
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }
        ParentQnaTypeDto dto = createSampleDto("TEST_4");
        dto.setIsActive(false);
        parentQnaTypeDao.insert(dto);

        // 2단계 데이터 처리 -> 사용여부가 true인 레코드들만 조회
        List<ParentQnaTypeDto> list = parentQnaTypeDao.selectAll();

        // 3단계 검증
        // (1) 반환된 List 객체는 null이 아님
        assertTrue(list != null);
        // (2) List 객체의 크기가 3
        assertTrue(list.size() == 3);
        // (3) List 객체의 모든 레코드들이 사용여부가 true
        for(ParentQnaTypeDto selectedDto : list){
            assertTrue(selectedDto.getIsActive());
        }
    }

    @DisplayName("부모 문의유형 레코드 전체 조회 - 실패 - 빈 List 반환")
    @Test
    void selectAll_3(){
        // 1단계 데이터 선택 -> @BeforeEach 메소드로 테이블 레코드 전체 삭제

        // 2단계 데이터 처리 -> 테이블 전체 조회
        List<ParentQnaTypeDto> list = parentQnaTypeDao.selectAll();

        // 3단계 검증 -> 반환된 List 객체는 null이 아님, List 객체의 크기가 0
        assertTrue(list != null);
        assertTrue(list.size() == 0);
    }

    @DisplayName("부모 문의유형 레코드 수 조회 - 성공")
    @Test
    void count_1(){
        // 1단계 데이터 선택 -> dto 객체 3건 생성
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }

        // 2단계 데이터 처리 -> 테이블 레코드 수 조회
        int count = parentQnaTypeDao.count();

        // 3단계 검증 -> 레코드 수 == 3
        assertTrue(count == 3);
    }

    @DisplayName("부모 문의유형 레코드 수정 - 성공")
    @Test
    void update_1(){
        // 1단계 데이터 선택
        // (1) dto 객체 생성 이후, 레코드 추가
        String id = "TEST_ID";
        ParentQnaTypeDto dto = createSampleDto(id);
        parentQnaTypeDao.insert(dto);
        // (2) 객체의 특정 컬럼값 수정
        ParentQnaTypeDto insertedDto = parentQnaTypeDao.select(id);
        insertedDto.setName(dto.getName() + "_MODIFIED");

        // 2단계 데이터 처리 -> 수정된 객체 업데이트
        // (1)생성일시 & 수정일시 비교를 위해 1초 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        // (2) 수정된 객체 업데이트
        int rowCnt = parentQnaTypeDao.update(insertedDto);

        // 3단계 검증
        // (1) 결과값 == 1
        assertTrue(rowCnt == 1);
        // (2) 수정된 객체 조회 후, 컬럼값 수정여부 확인
        ParentQnaTypeDto updatedDto = parentQnaTypeDao.select(id);
        assertTrue(!updatedDto.getName().equals(dto.getName()));
        // (3) 수정일시 & 생성일시 불일치 여부 확인
        assertTrue(!updatedDto.getUpdatedTime().equals(insertedDto.getCreatedTime()));
    }

    @DisplayName("부모 문의유형 레코드 삭제 - 성공")
    @Test
    void delete_1(){
        // 1단계 데이터 선택
        // (1) 레코드 3건 삽입
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }
        // (2) 삽입된 레코드들 중, 삭제될 ID 지정
        String targetId = "TEST_2";

        // 2단계 데이터 처리 -> 레코드 삭제
        int rowCnt = parentQnaTypeDao.delete(targetId);

        // 3단계 검증
        // (1) 결과값 == 1
        assertTrue(rowCnt == 1);
        // (2) 전체 레코드 수 == 2
        assertTrue(parentQnaTypeDao.count() == 2);
        // (3) 삭제된 ID로 조회 시, null 반환
        assertTrue(parentQnaTypeDao.select(targetId) == null);
    }

    @DisplayName("부모 문의유형 레코드 삭제 - 실패 - 조회된 레코드 없음")
    @Test
    void delete_2(){
        // 1단계 데이터 선택
        // (1) 레코드 3건 삽입
        for(int i = 0; i < 3; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }
        // (2) 존재하지 않는 ID 지정
        String targetId = "TEST_4";

        // 2단계 데이터 처리 -> 레코드 삭제
        int rowCnt = parentQnaTypeDao.delete(targetId);

        // 3단계 검증
        // (1) 결과값 == 0
        assertTrue(rowCnt == 0);
        // (2) 전체 레코드 수 == 3
        assertTrue(parentQnaTypeDao.count() == 3);
    }

    @DisplayName("부모 문의유형 레코드 전체 삭제 - 성공")
    @Test
    void deleteAll_1(){
        // 1단계 데이터 선택 -> 레코드 3건 삽입, 3건 삽입 확인
        int cnt = 3;
        for(int i = 0; i < cnt; i++){
            ParentQnaTypeDto dto = createSampleDto("TEST_" + i);
            parentQnaTypeDao.insert(dto);
        }
        assertTrue(parentQnaTypeDao.count() == cnt);

        // 2단계 데이터 처리 -> 레코드 전체 삭제
        int rowCnt = parentQnaTypeDao.deleteAll();

        // 3단계 검증
        // (1) 결과값 == 3
        assertTrue(rowCnt == cnt);
        // (2) 전체 레코드 수 == 0
        assertTrue(parentQnaTypeDao.count() == 0);
    }

    // 부모 문의유형 객체 생성
    private ParentQnaTypeDto createSampleDto(String parentQnaTypeId){
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