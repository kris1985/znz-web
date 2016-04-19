package com.znz.dao;

import com.znz.model.TLinePrice;

public interface TLinePriceMapper {
    int deleteByPrimaryKey(String id);

    int insert(TLinePrice record);

    int insertSelective(TLinePrice record);

    TLinePrice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TLinePrice record);

    int updateByPrimaryKey(TLinePrice record);
}