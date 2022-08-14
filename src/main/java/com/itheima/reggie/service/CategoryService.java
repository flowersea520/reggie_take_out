package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    // 在对应的业务层接口中定义自己的方法
    public void remove(Long id);
}
