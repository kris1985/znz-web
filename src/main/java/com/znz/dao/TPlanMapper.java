package com.znz.dao;

import com.znz.model.TPlan;

public interface TPlanMapper {
    int deleteByPrimaryKey(String uid);

    int insert(TPlan record);

    int insertSelective(TPlan record);

    TPlan selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(TPlan record);

    int updateByPrimaryKey(TPlan record);
}