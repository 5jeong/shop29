package com.toy2.shop29.qna.repository;

import com.toy2.shop29.qna.dto.ParentQnaTypeDto;
import java.util.List;

public interface ParentQnaTypeDao {

    // Create
    int insert(ParentQnaTypeDto parentQnaTypeDto);

    // Read
    ParentQnaTypeDto select(String parentQnaTypeId);
    List<ParentQnaTypeDto> selectAll();

    // Update
    int update(ParentQnaTypeDto parentQnaTypeDto);

    // Delete
    int delete(String parentQnaTypeId);
    int deleteAll();
}
