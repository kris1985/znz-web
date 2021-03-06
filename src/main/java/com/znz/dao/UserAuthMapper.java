package com.znz.dao;

import com.znz.model.UserAuth;
import com.znz.vo.AuthFileVO;

import java.util.List;

public interface UserAuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    UserAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);

    List<UserAuth> listByUserId(int userId);

    void deleteByUserId(int userId);
}