package com.toy2.shop29.qna.service.qna;

import com.toy2.shop29.order.domain.response.OrderHistoryResponseDTO;
import com.toy2.shop29.order.service.OrderService;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.service.product.ProductService;
import com.toy2.shop29.qna.domain.AttachmentTableName;
import com.toy2.shop29.qna.domain.QnaDto;
import com.toy2.shop29.qna.domain.request.QnaCreateRequest;
import com.toy2.shop29.qna.domain.response.QnaAdminResponse;
import com.toy2.shop29.qna.domain.response.QnaDetailResponse;
import com.toy2.shop29.qna.domain.response.QnaResponse;
import com.toy2.shop29.qna.repository.qna.QnaDao;
import com.toy2.shop29.qna.service.attachment.AttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QnaServiceImpl implements QnaService{

    /**
     * 첨부파일 최대 등록 가능 개수
     */

    @Value("${qna.attachment.max-cnt}")
    public int MAX_ATTACHMENT_CNT;

    private QnaDao qnaDao;
    private AttachmentService attachmentService;
    private OrderService orderService;
    private ProductService productService;

    public QnaServiceImpl(QnaDao qnaDao, AttachmentService attachmentService, OrderService orderService, ProductService productService){
        this.qnaDao = qnaDao;
        this.attachmentService = attachmentService;
        this.orderService = orderService;
        this.productService = productService;
    }

    // [READ] 1:1 문의 전체조회
    @Override
    public List<QnaResponse> findQnaListAll(String userId) throws RuntimeException {
        return findQnaList(userId, null, null);
    }

    // [READ] 1:1 문의 전체조회(유저) <- 유저 페이지 정보제공 목적
    @Override
    public List<QnaResponse> findQnaList(String userId, Integer limit, Integer offset) throws RuntimeException {
        // 유저는 삭제된(isActive = false) 1:1 문의를 볼 수 없음
        List<QnaDto> qnaDtos = qnaDao.selectAllWith(userId, limit, offset,true);
        List<QnaResponse> qnaResponses = qnaDtos.stream().map(QnaResponse::of).toList();
        return qnaResponses;
    }

    // [READ] 1:1 문의 전체조회(관리자) <- 관리자 페이지 정보제공 목적
    @Override
    public List<QnaAdminResponse> findQnaListWithFilter(int limit, int offset, String qnaTypeId, Boolean isAnswered) throws RuntimeException {
        List<QnaDto> qnaDtos = qnaDao.selectAllWithFilter(limit, offset, qnaTypeId, isAnswered, null);
        List<QnaAdminResponse> qnaAdminResponses = qnaDtos.stream().map(QnaAdminResponse::of).toList();
        return qnaAdminResponses;
    }

    // [READ] 1:1 문의 상세조회(관리자) <- 관리자 답글 작성 페이지 정보제공 목적
    @Override
    public QnaDetailResponse findQnaDetail(int qnaId) throws RuntimeException {
        QnaDto qnaDto = qnaDao.selectWith(qnaId);
        return QnaDetailResponse.of(qnaDto);
    }

    @Override
    public int countByUserId(String userId) throws RuntimeException {
        return qnaDao.countByUserId(userId);
    }

    @Override
    public int countForAdminWithFilter(String qnaTypeId, Boolean isAnswered) throws RuntimeException {
        return qnaDao.countForAdminWithFilter(qnaTypeId,isAnswered);
    }

    // [CREATE] 1:1 문의 등록
    @Transactional
    @Override
    public void createQna(QnaCreateRequest request, String userId) throws RuntimeException {
        // 0. 주문ID와 상품ID가 존재하는지 확인
        if(request.getOrderId() != null){
            try{
                List<OrderHistoryResponseDTO> orderHistorys= orderService.getOrderHistory(userId);
                boolean isExist = orderHistorys.stream().anyMatch(OrderHistoryResponseDTO -> OrderHistoryResponseDTO.getOrderId().equals(request.getOrderId()));
                if(!isExist){
                    throw new IllegalArgumentException("주문ID가 존재하지 않습니다.");
                }
            }catch (Exception e){
                throw new IllegalArgumentException("주문ID가 존재하지 않습니다.",e);
            }
        }

        if (request.getProductId() != null) {
            ProductWithCategoriesDto product = productService.findProductWithCategories(request.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("상품ID가 존재하지 않습니다.");
            }
        }

        // 1. 1:1 문의 등록
        QnaDto qnaDto = QnaDto.builder()
                .qnaTypeId(request.getQnaTypeId())
                .userId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .orderId(request.getOrderId())
                .productId(request.getProductId())
                .build();
        int insertCnt = qnaDao.insert(qnaDto);
        if(insertCnt != 1){
            throw new RuntimeException("1:1문의 테이블 등록 실패");
        }

        // 2. 첨부파일 등록
        List<String> attachmentNames = request.getAttachmentNames();
        // 2-1. 첨부파일이 없을 경우, 종료
        if(attachmentNames.isEmpty()){
            return;
        }

        // 2-2. 첨부파일이 최대 등록 가능 개수를 초과할 경우, 예외
        if(attachmentNames.size() > MAX_ATTACHMENT_CNT){
            throw new IllegalArgumentException("첨부파일은 최대 "+MAX_ATTACHMENT_CNT+"개까지 등록 가능합니다.");
        }

        // 2-4. 첨부파일 등록
        attachmentService.createAttachments(qnaDto.getUserId(),qnaDto.getQnaId().toString(), AttachmentTableName.QNA, attachmentNames);
    }

    // [DELETE] 1:1 문의 삭제 <- softDelete
    @Transactional
    @Override
    public void deleteQna(int qnaId, String userId) throws RuntimeException {
        // 1. qnaId에 해당하는 1:1 문의가 존재하는지 확인
        QnaDto qnaDto = qnaDao.select(qnaId, false);
        if(qnaDto == null){
            throw new IllegalArgumentException("해당 1:1 문의가 존재하지 않습니다.");
        }

        // 2. 문의글의 작성자가 userId가 아닐 경우, 권한 없음
        if(!qnaDto.getUserId().equals(userId)){
            throw new IllegalArgumentException("해당 1:1 문의를 삭제할 권한이 없습니다.");
        }

        // 3. 1:1 문의 삭제
        int deleteCnt = qnaDao.softDelete(qnaId, userId);
        if(deleteCnt != 1){
            throw new RuntimeException("1:1 문의 삭제에 실패하였습니다");
        }

        // 4. 첨부파일 삭제
        attachmentService.deleteAttachmentsBy(userId, Integer.toString(qnaId), AttachmentTableName.QNA);
    }
}
