package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.domain.UserForQnaDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {

    SqlSession session;
    String namespace = "com.toy2.shop29.qna.UserMapper.";

    public UserDao(SqlSession session) {
        this.session = session;
    }

    // READ
    public UserForQnaDto select(String userId) {
        return session.selectOne(namespace + "select",userId);
    }
}
