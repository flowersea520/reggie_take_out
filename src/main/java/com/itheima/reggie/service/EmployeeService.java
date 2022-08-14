package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Employee;

// 这个是继承，可以使业务层快速开发，可以直接用他的方法调用数据层
//MP提供了一个Service接口和实现类，分别是:IService和ServiceImpl,后者是对前者的一个具体实现。
//        MP已经帮我们把业务层的一些基础的增删改查都已经实现了，可以直接进行使用
public interface EmployeeService extends IService<Employee> {
}
