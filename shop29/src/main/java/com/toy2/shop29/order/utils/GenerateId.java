package com.toy2.shop29.order.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenerateId {
    public static String generateId() {
        // 현재 날짜와 시간을 YYMMDDhhmmss 형식으로 포맷팅
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateStr = sdf.format(new Date());

        // 8자리 랜덤 숫자 생성
        Random random = new Random();
        int randomNumber = random.nextInt(100000000); // 0 ~ 99999999 사이의 랜덤 숫자 생성
        String randomStr = String.format("%08d", randomNumber); // 8자리로 포맷팅 (부족한 자리수는 0으로 채움)

        // 두 문자열 결합
        return dateStr + "_" + randomStr;
    }
}
