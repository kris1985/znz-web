package com.znz.dao;

import com.znz.model.TAttractions;
import com.znz.vo.AttractionsQueryVO;

import java.util.List;

public interface TAttractionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAttractions record);

    int insertSelective(TAttractions record);

    TAttractions selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TAttractions record);

    int updateByPrimaryKey(TAttractions record);

    List<TAttractions> selectByPage(AttractionsQueryVO attractionsQueryVO);

    TAttractions selectByProdName(Integer id);
}