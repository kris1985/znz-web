package com.znz.dao;

import com.znz.model.TAttractions;
import com.znz.model.THotel;
import com.znz.vo.HotelQueryQueryVO;

import java.util.List;

public interface THotelMapper {
    int deleteByPrimaryKey(String id);

    int insert(THotel record);

    int insertSelective(THotel record);

    THotel selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(THotel record);

    int updateByPrimaryKey(THotel record);

    List<THotel> selectByPage(HotelQueryQueryVO queryQueryVO);

}