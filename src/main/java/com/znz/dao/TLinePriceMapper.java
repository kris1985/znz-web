package com.znz.dao;

import com.znz.model.TLinePrice;

import java.util.List;

public interface TLinePriceMapper {
    int deleteByPrimaryKey(String id);

    int insert(TLinePrice record);

    int insertSelective(TLinePrice record);

    TLinePrice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TLinePrice record);

    int updateByPrimaryKey(TLinePrice record);

    List<TLinePrice> selectByLineId(String lineId);
}