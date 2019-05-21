package com.cn.kbyd.mybatch.mapper;

import com.cn.kbyd.mybatch.entity.Student;

public interface StudentMapper {
    int insert(Student record);

    int insertSelective(Student record);
}