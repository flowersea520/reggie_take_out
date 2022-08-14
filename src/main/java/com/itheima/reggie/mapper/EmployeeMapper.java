package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

// Employ 的数据层
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
