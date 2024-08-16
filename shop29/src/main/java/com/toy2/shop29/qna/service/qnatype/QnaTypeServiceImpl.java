package com.toy2.shop29.qna.service.qnatype;

import com.toy2.shop29.qna.domain.ParentQnaTypeDto;
import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QnaTypeServiceImpl implements QnaTypeService {

    QnaTypeDao qnaTypeDao;

    public QnaTypeServiceImpl(QnaTypeDao qnaTypeDao) {
        this.qnaTypeDao = qnaTypeDao;
    }

    @Override
    public List<QnaTypeDto> findAll() throws RuntimeException {
        return qnaTypeDao.selectAll(false, true);
    }

    @Override
    public Map<String, List<QnaTypeDto>> findAllWithParentForUser() throws RuntimeException {
        // 1. 일반 회원은 사용중인 문의유형만 조회할 수 있다.
        List<QnaTypeDto> qnaTypeDtos = qnaTypeDao.selectAll(true, true);

        // 부모 문의유형 셋
        Set<String> parentQnaTypeNameSet = new HashSet<>();
        for(QnaTypeDto qnaTypeDto : qnaTypeDtos){
            parentQnaTypeNameSet.add(qnaTypeDto.getParentQnaType().getName());
        }

        // 부모 문의유형별 자식 문의유형 리스트
        Map<String, List<QnaTypeDto>> parentQnaTypeMap = new HashMap<>();
        for(String parentQnaTypeName : parentQnaTypeNameSet){
            List<QnaTypeDto> childQnaTypeList = new LinkedList<>();
            for(QnaTypeDto qnaTypeDto : qnaTypeDtos){
                if(qnaTypeDto.getParentQnaType().getName().equals(parentQnaTypeName)){
                    childQnaTypeList.add(qnaTypeDto);
                }
            }
            parentQnaTypeMap.put(parentQnaTypeName, childQnaTypeList);
        }

        return parentQnaTypeMap;
    }

    @Override
    // TODO : 이 메서드는 관리자에서 조회하기 위한 목적이기 때문에, userId를 받아 관리자인지 확인하는 로직을 추가해야함
    public List<QnaTypeDto> findAllWithParentForAdmin() throws RuntimeException {
        // 1. 관리자는 모든 문의유형을 조회할 수 있다.
        return qnaTypeDao.selectAll(true, null);
    }

    // 문의 답글 작성에서, 문의글의 문의유형을 조회하는데 사용됨
    @Override
    // TODO : 이 메서드는 관리자에서 조회하기 위한 목적이기 때문에, userId를 받아 관리자인지 확인하는 로직을 추가해야함
    public QnaTypeDto findByQnaTypeIdWithParent(String qnaTypeId) throws RuntimeException {
        return qnaTypeDao.select(qnaTypeId, true, null);
    }
}
