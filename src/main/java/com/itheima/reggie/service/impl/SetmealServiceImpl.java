package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     *  新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();// 获取菜品套餐实体类信息的集合对象
        // 用链式编程来遍历它, 将参数的id赋值给setmealDishes集合中去，然后将构成一个新的集合
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId()); // 这里得到的id是套餐setmeal_id，将setmeal_id赋值给菜品套餐表中去
            return item;
        }).collect(Collectors.toList()); // 这个方法是构成一个新的集合，还是setmealDishes的对象名

        // 保存套餐和菜品的关联信息，操作setmeal_dish, 执行insert操作

        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     *  删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    // 由于删除了两张表，所以加一个事务注解，保证数据的一致性
    @Transactional
    public void removeWithDish(List<Long> ids) {

        // 执行条件的sql为： select count(*) from setmeal where id in (1,2,3) and status = 1
        // 查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1); // 查询起售的商品

        int count = this.count(queryWrapper); // 这个count是mq框架的方法

        if (count > 0){
            // 如果不能删除，抛出一个业务异常，自己定义了
            throw  new CustomException("套餐正在售卖中，不能删除");
        }
        // 如果可以删除，先删除套餐表中的数据-- setmeal表
        this.removeByIds(ids); // 这个removeByIds是mq提供的方法，参数提供一个集合，把集合中符合条件的都删除

        // 删除关系表中的数据---setmeal_dish表
        // delete from setmeal_dish where setmeal_id in (1,2,3) 这里的123是集合中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 相当于sqldelete from setmeal_dish where setmeal_id in (1,2,3)
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        // 删除关系表中的数据--setmeal_dish 删除哪张表，就调用对应的业务层
        setmealDishService.remove(lambdaQueryWrapper);

    }

    /**
     * 根据Id查询套餐信息（用于套餐管理的修改功能）
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //查询套餐基本信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        //查询套餐菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     *  套餐修改功能
     * @param setmealDto
     * @return
     */

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //更新setmeal_dish表信息delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //更新setmeal_dish表信息insert操作
        List<SetmealDish> SetmealDishes = setmealDto.getSetmealDishes();

        SetmealDishes = SetmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(SetmealDishes);
    }


}
