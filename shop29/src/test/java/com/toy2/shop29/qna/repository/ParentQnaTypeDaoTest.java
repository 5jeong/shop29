package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParentQnaTypeDaoTest {

    @SpyBean
    private SqlSession sqlSession;

    @Autowired
    private ParentQnaTypeDao parentQnaTypeDao;
    private String userId = "admin";
    private String parentQnaTypeIdPrefix = "TEST_";

    @Test
    @DisplayName("insert 테스트 - 성공(1 반환)")
    void insert_1(){
        // given
        parentQnaTypeDao.deleteAll();
        ParentQnaTypeDto dto = ParentQnaTypeDto.builder()
                .parent_qna_type_id("TEST")
                .name("테스트")
                .description("테스트")
                .is_active(true)
                .created_id(userId)
                .updated_id(userId)
                .build();

        // when
        int result = parentQnaTypeDao.insert(dto);

        // then
        assertTrue(result == 1);
    }

    @Test
    @DisplayName("insert 테스트 - 실패(0 반환) - 기본키가 null인 경우")
    void insert_2(){
        // given
        parentQnaTypeDao.deleteAll();
        ParentQnaTypeDto dto = ParentQnaTypeDto.builder()
                .name("테스트")
                .description("테스트")
                .is_active(true)
                .created_id(userId)
                .updated_id(userId)
                .build();

        // when
        int result = parentQnaTypeDao.insert(dto);

        // then
        assertTrue(result == 0);
    }

    private void insertSample(int sampleCnt){
        for(int i = 0; i < sampleCnt; i++){
            ParentQnaTypeDto dto = ParentQnaTypeDto.builder()
                    .parent_qna_type_id(parentQnaTypeIdPrefix + i)
                    .name("테스트")
                    .description("테스트")
                    .is_active(true)
                    .created_id(userId)
                    .updated_id(userId)
                    .build();
            parentQnaTypeDao.insert(dto);
        }
    }

    @Test
    @DisplayName("selectAll 테스트 - 성공")
    void selectAll_1(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(5);

        // when
        List<ParentQnaTypeDto> result = parentQnaTypeDao.selectAll();

        // then
        assertTrue(result != null);
        assertTrue(result.size() == 5);
    }

    @Test
    @DisplayName("selectAll 테스트 - 예외처리(빈 List 반환)")
    void selectAll_2(){
        // given
        doThrow(new DataAccessException("test") {})
                .when(sqlSession)
                .selectList(Mockito.anyString());

        // when
        List<ParentQnaTypeDto> result = parentQnaTypeDao.selectAll();

        // then
        assertTrue(result != null);
        assertTrue(result.size() == 0);
    }

    @Test
    @DisplayName("select 테스트 - 조회 성공")
    void select_1(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(5);
        String targetId = parentQnaTypeIdPrefix + 3;

        // when
        ParentQnaTypeDto result = parentQnaTypeDao.select(targetId);

        // then
        assertTrue(result != null);
        assertTrue(result.getParent_qna_type_id().equals(targetId));
    }

    @Test
    @DisplayName("select 테스트 - 조회 실패 - null 반환")
    void select_2(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(5);
        String targetId = parentQnaTypeIdPrefix + 10;

        // when
        ParentQnaTypeDto result = parentQnaTypeDao.select(targetId);

        // then
        assertTrue(result == null);
    }

    @Test
    @DisplayName("deleteAll 테스트 - 성공(삭제된 row 수 반환)")
    void deleteAll_1(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(5);

        // when
        int result = parentQnaTypeDao.deleteAll();

        // then
        assertTrue(result == 5);
    }

    @Test
    @DisplayName("delete 테스트 - 삭제된 조회 수 반환")
    void delete_1(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(5);
        String targetId = parentQnaTypeIdPrefix + 3;

        // when
        int result = parentQnaTypeDao.delete(targetId);

        // then
        assertTrue(result == 1);
    }

    @Test
    @DisplayName("update 테스트 - ID에 해당하는 로우가 있으며, 변경 성공시 1 반환")
    void update_1(){
        // given
        parentQnaTypeDao.deleteAll();
        insertSample(2);
        String targetId = parentQnaTypeIdPrefix + 1;
        ParentQnaTypeDto dto = parentQnaTypeDao.select(targetId);
        String afterName = "변경된 이름";
        dto.setName(afterName);

        // when
        int result = parentQnaTypeDao.update(dto);
        ParentQnaTypeDto updatedDto = parentQnaTypeDao.select(targetId);

        // then
        assertTrue(result == 1);
        assertTrue(updatedDto != null);
        assertTrue(updatedDto.getName().equals(afterName));
    }

    @Test
    @DisplayName("update 테스트 - ID에 해당하는 로우가 없을 경우, 0 반환")
    void update_2(){
        // given
        parentQnaTypeDao.deleteAll();
        ParentQnaTypeDto dto = ParentQnaTypeDto.builder()
                .parent_qna_type_id("TEST")
                .name("테스트")
                .description("테스트")
                .is_active(true)
                .created_id(userId)
                .updated_id(userId)
                .build();

        // when
        int result = parentQnaTypeDao.update(dto);

        // then
        assertTrue(result == 0);
    }
}
