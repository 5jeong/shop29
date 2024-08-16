package com.toy2.shop29.qna.service.qnatype;

import com.toy2.shop29.qna.domain.QnaTypeDto;
import com.toy2.shop29.qna.repository.qnatype.QnaTypeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QnaTypeServiceImpl implements QnaTypeService {

    QnaTypeDao qnaTypeDao;

    public QnaTypeServiceImpl(QnaTypeDao qnaTypeDao) {
        this.qnaTypeDao = qnaTypeDao;
    }

    @Override
    public List<QnaTypeDto> findAllWithParentForUser() {
        // 1. 일반 회원은 사용중인 문의유형만 조회할 수 있다.
        return qnaTypeDao.selectAll(true, true);
    }

    @Override
    public List<QnaTypeDto> findAllWithParentForAdmin() {
        // 1. 관리자는 모든 문의유형을 조회할 수 있다.
        return qnaTypeDao.selectAll(true, null);
    }

    // 문의 답글 작성에서, 문의글의 문의유형을 조회하는데 사용됨
    @Override
    public QnaTypeDto findByQnaTypeIdWithParent(String qnaTypeId) {
        return qnaTypeDao.select(qnaTypeId, true, null);
    }
}
