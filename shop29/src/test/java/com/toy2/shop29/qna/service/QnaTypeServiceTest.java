package com.toy2.shop29.qna.service;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.parentqnatype.ParentQnaTypeDao;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import com.toy2.shop29.qna.service.qnatype.QnaTypeService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaTypeServiceTest {

    @Autowired
    QnaTypeService qnaTypeService;

    @Autowired
    QnaTypeDao qnaTypeDao;
    @Autowired
    ParentQnaTypeDao parentQnaTypeDao;

    List<QnaTypeDto> qnaTypeDtos = new LinkedList<>();
    List<ParentQnaTypeDto> parentQnaTypeDtos = new LinkedList<>();

    int activeQnaTypeCnt;
    int inactiveQnaTypeCnt;

    @PostConstruct
    void init(){
        parentQnaTypeDao.deleteAll();
        qnaTypeDao.deleteAll();

        ParentQnaTypeDto pDto1 = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_1")
                .name("부모문의유형1")
                .description("부모문의유형1 설명")
                .isActive(true)
                .createdId("admin")
                .updatedId("admin")
                .build();
        parentQnaTypeDtos.add(pDto1);
        parentQnaTypeDao.insert(pDto1);

        ParentQnaTypeDto pDto2 = ParentQnaTypeDto.builder()
                .parentQnaTypeId("PARENT_QNA_TYPE_2")
                .name("부모문의유형2")
                .description("부모문의유형2 설명")
                .isActive(false)
                .createdId("admin")
                .updatedId("admin")
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
                    .createdId("admin")
                    .updatedId("admin")
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
                    .createdId("admin")
                    .updatedId("admin")
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

    @DisplayName("문의유형 전체조회 -> 사용자용")
    @Test
    void findAllWithParentForUser() {
        // 1단계 데이터 선택 -> init() 메서드에서 수행

        // 2단계 데이터 처리
        List<QnaTypeDto> typeList = qnaTypeService.findAllWithParentForUser();

        // 3단계 검증 -> 사용자는 사용중인 문의유형만 조회가능
        assertTrue(typeList != null);
        assertTrue(typeList.size() == activeQnaTypeCnt);
        for(QnaTypeDto qnaTypeDto : typeList){
            // 문의유형 사용여부 확인
            assertTrue(qnaTypeDto.isActive());
            // 부모문의유형 사용여부 확인
            assertTrue(qnaTypeDto.getParentQnaType() != null);
            assertTrue(qnaTypeDto.getParentQnaType().isActive());
        }
    }

    @DisplayName("문의유형 전체조회 -> 관리자용")
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
