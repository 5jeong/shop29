package com.toy2.shop29.qna.service;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.qnatype.QnaTypeService;
import com.toy2.shop29.users.domain.UserRegisterDto;
import com.toy2.shop29.users.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaTypeServiceTest {

    @Autowired
    QnaTypeService qnaTypeService;

    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;
    @Autowired
    UserMapper userMapper;

    List<QnaTypeDto> qnaTypeDtos = new LinkedList<>();
    List<ParentQnaTypeDto> parentQnaTypeDtos = new LinkedList<>();

    int activeQnaTypeCnt;
    int inactiveQnaTypeCnt;

    final String ADMIN_ID = "admin";

    @BeforeEach
    void init(){
        parentQnaTypeDao.deleteAll();
        qnaTypeDao.deleteAll();
        userMapper.deleteUser(ADMIN_ID);

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

        ParentQnaTypeDto pDto1 = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        parentQnaTypeDtos.add(pDto1);
        parentQnaTypeDao.insert(pDto1);

        ParentQnaTypeDto pDto2 = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_2")
                .name("부모문의유형2")
                .description("부모문의유형2 설명")
                .isActive(false)
                .createdId(ADMIN_ID)
                .updatedId(ADMIN_ID)
                .build();
        parentQnaTypeDtos.add(pDto2);
        parentQnaTypeDao.insert(pDto2);

        int qnaTypeCnt = 10;
        for(int i = 0; i < qnaTypeCnt; i++){
            QnaTypeDto qDto = QnaTypeDto.builder()
                    .qnaTypeId("QNA_TYPE_" + (i + 1))
                    .parentQnaTypeId(parentQnaTypeDtos.get(0).getParentQnaTypeId())
                    .name("문의유형" + (i + 1))
                    .description("문의유형" + (i + 1) + " 설명")
                    .isOrderIdActive(true)
                    .isProductIdActive(true)
                    .isActive(i % 2 == 0 ? true : false)
                    .createdId(ADMIN_ID)
                    .updatedId(ADMIN_ID)
                    .build();
            qnaTypeDtos.add(qDto);
            qnaTypeDao.insert(qDto);
        }
        for(int i = 0; i < qnaTypeCnt; i++){
            QnaTypeDto qDto = QnaTypeDto.builder()
                    .qnaTypeId("QNA_TYPE_" + (qnaTypeCnt + i + 1))
                    .parentQnaTypeId(parentQnaTypeDtos.get(1).getParentQnaTypeId())
                    .name("문의유형" + (i + 1))
                    .description("문의유형" + (i + 1) + " 설명")
                    .isOrderIdActive(true)
                    .isProductIdActive(true)
                    .isActive(i % 2 == 0 ? true : false)
                    .createdId(ADMIN_ID)
                    .updatedId(ADMIN_ID)
                    .build();
            qnaTypeDtos.add(qDto);
            qnaTypeDao.insert(qDto);
        }
        calculateQnaTypeCnt();
    }

    void calculateQnaTypeCnt(){
        activeQnaTypeCnt = 0;
        inactiveQnaTypeCnt = 0;
        for(QnaTypeDto qnaTypeDto : qnaTypeDtos){
            ParentQnaTypeDto parent = parentQnaTypeDtos.stream()
                    .filter(p -> p.getParentQnaTypeId().equals(qnaTypeDto.getParentQnaTypeId()))
                    .findFirst()
                    .orElse(null);
            if(qnaTypeDto.isActive() && parent.isActive()){
                activeQnaTypeCnt++;
            }else{
                inactiveQnaTypeCnt++;
            }
        }
    }

    @DisplayName("문의유형 전체조회 -> 관리자용 : 모든 문의유형 조회가능")
    @Test
    void findAllWithParentForAdmin() {
        // 1단계 데이터 선택 -> init() 메서드에서 수행

        // 2단계 데이터 처리
        List<QnaTypeDto> typeList = qnaTypeService.findAllWithParentForAdmin();

        // 3단계 검증 -> 관리자는 모든 문의유형 조회가능
        assertTrue(typeList != null);
        assertTrue(typeList.size() == qnaTypeDtos.size());
        for(QnaTypeDto qnaTypeDto : typeList){
            // 부모문의유형 사용여부 확인
            assertTrue(qnaTypeDto.getParentQnaType() != null);
        }
    }

    @DisplayName("특정 문의유형ID를 기준으로, 문의유형 & 부모문의유형 조회")
    @Test
    void findByQnaTypeIdWithParent() {
        // 1단계 데이터 선택 -> init() 메서드에서 수행

        // 2단계 데이터 처리
        // 3단계 검증
        for(QnaTypeDto qnaTypeDto : qnaTypeDtos) {
            QnaTypeDto type = qnaTypeService.findByQnaTypeIdWithParent(qnaTypeDto.getQnaTypeId());
            assertTrue(type != null);
            assertTrue(type.getParentQnaType() != null);
        }
    }
}
