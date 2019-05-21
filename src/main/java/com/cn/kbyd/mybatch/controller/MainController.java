package com.cn.kbyd.mybatch.controller;


import com.cn.kbyd.mybatch.entity.UserInfo;
import com.cn.kbyd.mybatch.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/main")
public class MainController {
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @RequestMapping("/getUserInfo")
    public List<UserInfo> getUserInfo(){
        log.info("查询用户信息");
        return userInfoService.getUserInfo();
    }
}
