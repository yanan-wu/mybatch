package com.cn.kbyd.mybatch.mapper;

import com.cn.kbyd.mybatch.entity.UserInfo;

import java.util.List;

public interface UserInfoMapper {

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectAll();
}