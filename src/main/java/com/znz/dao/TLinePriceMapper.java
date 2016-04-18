package com.znz.dao;

import com.znz.model.TLinePrice;

public interface TLinePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TLinePrice record);

    int insertSelective(TLinePrice record);

    TLinePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TLinePrice record);

    int updateByPrimaryKey(TLinePrice record);
}