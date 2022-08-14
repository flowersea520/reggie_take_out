package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     *  添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据：{}", shoppingCart);

        // 设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();// 获取当前线程的用户id（对应的是user表），用来当作用户id
        shoppingCart.setUserId(currentId);

        // 查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();// 从请求的参数中获取菜品id

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId); // 查询线程id的值

        if (dishId !=null){
            // 前端代表传过来的是菜品id，添加到购物车的是菜品dish
            queryWrapper.eq(ShoppingCart::getDishId,dishId);


        }else {
            // 添加到购物车的是套餐
            // 定义查询条件
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 查询当前菜品或者套餐是否在购物车中
        // SQL: select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);// 根据条件调用业务层方法查询

        if (cartServiceOne !=null){
            // 不为空，说明查到了，这个菜品/套餐已存在，，在原来的基础上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne); // 更新数据库对象

        }else {
            // 为空，就代表没查到，菜品/套餐不存在，则添加到购物车，数据默认就是1
            shoppingCart.setNumber(1);
            // 设置入库的create_time
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);// 将对象添加进去
            cartServiceOne = shoppingCart; // cartServiceOne是根据queryWrapper条件查询到的对象，将新的参数对象赋值
        }


        return R.success(cartServiceOne);
    }

    /**
     *  查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件为，根据用户id查询，查询值为：用线程获取用户的id值 // 不用的用户购物车信息都不一样
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
                        // 这里使用升序排序可以，将最先加入购物车的排前面
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);// 升序排序，从小到大，从最早的日期到最新（晚）的日期
                    // list方法在mp中是查询一个列表
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);// 根据用户id查询购物车

        return R.success(list);


    }

    /**
     *  清空购物车
     * @return
     */

    @DeleteMapping("clean")
    public R<String> clean() {
        // Sql:delete * from shop_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId()); // 得到用户id
        // 根据id删除
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");

    }

    /**
     *  删除购物车菜品
     * @param shoppingCart
     * @return
     */

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        if (setmealId!=null){
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        Integer number = one.getNumber();
        if(number==1){
            shoppingCartService.remove(queryWrapper);
        }else {
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }


}