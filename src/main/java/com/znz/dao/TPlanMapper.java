package com.znz.dao;

import com.znz.model.TPlan;
import com.znz.model.TTravelZyx;
import com.znz.vo.PlanQueryVO;

import java.util.List;

public interface TPlanMapper {
    int deleteByPrimaryKey(String uid);

    int insert(TPlan record);

    int insertSelective(TPlan record);

    TPlan selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(TPlan record);

    int updateByPrimaryKey(TPlan record);

    List<TTravelZyx> selectByPage(PlanQueryVO queryQueryVO);
}