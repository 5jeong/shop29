package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ParentQnaTypeDaoImpl implements ParentQnaTypeDao{

    private SqlSession session;
    private String namespace = "com.toy2.shop29.qna.ParentQnaTypeMapper.";

    public ParentQnaTypeDaoImpl(SqlSession session){
        this.session = session;
    }

    /*
        CREATE
     */
    @Override
    public int insert(ParentQnaTypeDto parentQnaTypeDto) {
        int rowCnt;
        try {
            rowCnt = session.insert(namespace + "insert", parentQnaTypeDto);
        }catch (DataAccessException e){
            rowCnt = 0;
            e.printStackTrace();
        }
        return rowCnt;
    }

    /*
        READ
     */
    @Override
    public ParentQnaTypeDto select(String parentQnaTypeId) {
        ParentQnaTypeDto parentQnaTypeDto;
        try {
            parentQnaTypeDto = session.selectOne(namespace + "select", parentQnaTypeId);
        }catch (DataAccessException e){
            parentQnaTypeDto = null;
            e.printStackTrace();
        }
        return parentQnaTypeDto;
    }

    @Override
    public List<ParentQnaTypeDto> selectAll() {
        List<ParentQnaTypeDto> list;
        try {
            list = session.selectList(namespace + "selectAll");
        }catch (DataAccessException e){
            list = List.of();
            e.printStackTrace();
        }
        return list;
    }

    /*
        UPDATE
     */
    @Override
    public int update(ParentQnaTypeDto parentQnaTypeDto) {
        int rowCnt;
        try {
            rowCnt = session.update(namespace + "update", parentQnaTypeDto);
        }catch (DataAccessException e) {
            rowCnt = 0;
            e.printStackTrace();
        }
        return rowCnt;
    }

    /*
        DELETE
     */
    @Override
    public int delete(String parentQnaTypeId) {
        int rowCnt;
        try {
            rowCnt = session.delete(namespace + "delete", parentQnaTypeId);
        }catch (DataAccessException e) {
            rowCnt = 0;
            e.printStackTrace();
        }
        return rowCnt;
    }

    @Override
    public int deleteAll() {
        int rowCnt;
        try {
            rowCnt = session.delete(namespace + "deleteAll");
        }catch (DataAccessException e){
            rowCnt = 0;
            e.printStackTrace();
        }
        return rowCnt;
    }
}
