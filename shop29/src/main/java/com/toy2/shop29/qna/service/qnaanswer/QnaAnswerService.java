package com.toy2.shop29.qna.service.qnaanswer;

public interface QnaAnswerService {

    // [CREATE] 1:1 문의 답변 등록
    void createQnaAnswer(int qnaId, String adminId, String answerContent);

    // [UPDATE] 1:1 문의 답변 수정
    void updateQnaAnswer(int qnaAnswerId, String adminId, String answerContent);

    // [DELETE] 1:1 문의 답변 삭제
    void deleteQnaAnswer(int qnaAnswerId, String adminId);
}
