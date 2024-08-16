package com.toy2.shop29.qna.domain;

public enum AttachmentTableName {
    QNA("qna");

    private final String value;

    AttachmentTableName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}