package com.znz.dao;

import com.znz.model.TAttractions;
import com.znz.vo.AttractionsQueryVO;

import java.util.List;

public interface TAttractionsMapper {
    int deleteByPrimaryKey(String id);

    int insert(TAttractions record);

    int insertSelective(TAttractions record);

    TAttractions selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TAttractions record);

    int updateByPrimaryKey(TAttractions record);

    List<TAttractions> selectByPage(AttractionsQueryVO attractionsQueryVO);

    TAttractions selectByProdName(String productName);
}