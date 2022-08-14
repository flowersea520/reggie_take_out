package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService; // 注入菜品dish业务层接口对象

    @Autowired
    private SetmealService setmealService; // 创建Setmeal套餐的业务层接口对象

    /**
     *  根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
//        创建查询菜品实体类dish类条件的对象
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper= new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询 这里是等值查询所以用eq ：相当于sql：where ？ = ？
        // 在数据库相当于执行的语句是：select count(*) from dish where category_id = #{id}; -- 后面这个id是传入的参数id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        // 符合条件查询的菜品数（行数），调用菜品dish的业务层接口的count方法，参数为queryWrapper条件
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // 查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            // 已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        // 根据条件，查询符合套餐数（行数）
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            // 已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");

        }

        // 正常删除分类
        super.removeById(id); // super是父类方法
    }
}
