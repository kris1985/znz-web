package com.znz.dao;

import com.znz.model.TProduct;
import com.znz.vo.ProductQueryVO;

import java.util.List;

public interface TProductMapper {
    int deleteByPrimaryKey(String id);

    int insert(TProduct record);

    int insertSelective(TProduct record);

    TProduct selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TProduct record);

    int updateByPrimaryKey(TProduct record);

    List<TProduct> selectByPage(ProductQueryVO productQueryVO);
}