package com.toy2.shop29.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "EC001", "잘못된 입력 값"),
    METHOD_NOT_ALLOWED(405, "EC002", "허용되지 않은 메서드"),
    ENTITY_NOT_FOUND(400, "EC003", "엔티티 찾을 수 없음"),
    INTERNAL_SERVER_ERROR(500, "EC004", "서버 오류"),
    INVALID_TYPE_VALUE(400, "EC005", "잘못된 유형 값"),
    HANDLE_ACCESS_DENIED(403, "EC006", "액세스가 거부됨"),
    FORBIDDEN_ACCESS(403, "EC007", "비정상적 접근"),

    // 장바구니
    PRODUCT_NOT_EXISTS(400, "C001", "존재하지 않는 상품 ID"),
    CREATE_CART_ERROR(400, "C002", "장바구니 생성 오류"),

    // 주문
    OUT_OF_STOCK(409, "OD01", "상품 재고가 부족합니다."),
    ADD_ITEM_ERROR(400, "OD01", "결제 중 상품 오류가 발생하였습니다.");


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}
