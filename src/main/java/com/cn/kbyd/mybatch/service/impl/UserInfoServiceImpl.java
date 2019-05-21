package com.cn.kbyd.mybatch.service.impl;


import com.cn.kbyd.mybatch.entity.UserInfo;
import com.cn.kbyd.mybatch.mapper.UserInfoMapper;
import com.cn.kbyd.mybatch.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserInfoServiceImpl
 * @Description
 * @Author yanan.wu
 * @Date 2019/5/14 19:18
 **/
@Slf4j
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> getUserInfo() {
        log.info("----获取用户---");
        return userInfoMapper.selectAll();
    }
}
