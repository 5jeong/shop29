package com.toy2.shop29.qna.service.qnaanswer;

import com.toy2.shop29.qna.domain.QnaAnswerDto;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.repository.qnaasnwer.QnaAnswerDao;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QnaAnswerServiceImpl implements QnaAnswerService {

    QnaAnswerDao qnaAnswerDao;
    QnaDao qnaDao;
    UserMapper userMapper;
    private final String ROLE_ADMIN = "관리자";

    public QnaAnswerServiceImpl(QnaAnswerDao qnaAnswerDao, QnaDao qnaDao, UserMapper userMapper) {
        this.qnaAnswerDao = qnaAnswerDao;
        this.qnaDao = qnaDao;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public void createQnaAnswer(int qnaId, String adminId, String answerContent) throws RuntimeException {
        // 1. adminId에 해당하는 계정의 권한이 "관리자"가 아닐 경우, 권한 없음
        UserDto user = userMapper.findById(adminId);
        if(!user.getUserRole().equals(ROLE_ADMIN)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 2. 문의글에 대한 답변이 이미 있을 경우, 잘못된 요청
        if(qnaAnswerDao.selectBy(qnaId, false) != null){
            throw new IllegalArgumentException("이미 답변이 등록된 문의글입니다.");
        }

        // 3. qnaId에 해당하는 문의글이 존재하지 않을 경우, 잘못된 요청
        if(qnaDao.select(qnaId,false) == null){
            throw new IllegalArgumentException("존재하지 않는 문의글입니다.");
        }

        // 4. 정상적인 경우, 답변 등록
        QnaAnswerDto qnaAnswerDto = QnaAnswerDto.builder()
                .qnaId(qnaId)
                .adminId(adminId)
                .content(answerContent)
                .createdId(adminId)
                .updatedId(adminId)
                .build();
        qnaAnswerDao.insert(qnaAnswerDto);
    }

    @Transactional
    @Override
    public void updateQnaAnswer(int qnaAnswerId, String adminId, String answerContent) throws RuntimeException {
        // 1. adminId에 해당하는 계정의 권한이 "관리자"가 아닐 경우, 권한 없음
        if(!userMapper.findById(adminId).getUserRole().equals(ROLE_ADMIN)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 2. qnaAnswerId에 해당하는 답변이 존재하지 않을 경우, 잘못된 요청
        QnaAnswerDto qnaAnswerDto = qnaAnswerDao.select(qnaAnswerId, false);
        if(qnaAnswerDto == null){
            throw new IllegalArgumentException("존재하지 않는 답변입니다.");
        }

        // 3. qnaAnswerId에 해당하는 답변의 adminId와 adminId가 다를 경우, 권한 없음
        if(!qnaAnswerDto.getAdminId().equals(adminId)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 4. 정상적인 경우, 답변 수정
        qnaAnswerDto.setContent(answerContent);
        qnaAnswerDto.setUpdatedId(adminId);
        qnaAnswerDao.update(qnaAnswerDto);
    }

    @Transactional
    @Override
    public void deleteQnaAnswer(int qnaAnswerId, String adminId) throws RuntimeException{
        // 1. adminId에 해당하는 계정의 권한이 "관리자"가 아닐 경우, 권한 없음
        if(!userMapper.findById(adminId).getUserRole().equals(ROLE_ADMIN)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 2. qnaAnswerId에 해당하는 답변이 존재하지 않을 경우, 잘못된 요청
        QnaAnswerDto qnaAnswerDto = qnaAnswerDao.select(qnaAnswerId, false);
        if(qnaAnswerDto == null){
            throw new IllegalArgumentException("존재하지 않는 답변입니다.");
        }

        // 3. qnaAnswerId에 해당하는 답변의 adminId와 adminId가 다를 경우, 권한 없음
        if(!qnaAnswerDto.getAdminId().equals(adminId)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 4. 정상적인 경우, 답변 삭제
        qnaAnswerDao.softDelete(qnaAnswerId, adminId);
    }
}
