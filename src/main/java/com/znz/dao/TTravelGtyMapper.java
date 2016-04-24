package com.znz.dao;

import com.znz.model.TTravelGty;
import com.znz.vo.TravelGtyQueryVO;

import java.util.List;

public interface TTravelGtyMapper {
    int deleteByPrimaryKey(String uid);

    int insert(TTravelGty record);

    int insertSelective(TTravelGty record);

    TTravelGty selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(TTravelGty record);

    int updateByPrimaryKey(TTravelGty record);

    List<TTravelGty> selectByPage(TravelGtyQueryVO queryQueryVO);
}