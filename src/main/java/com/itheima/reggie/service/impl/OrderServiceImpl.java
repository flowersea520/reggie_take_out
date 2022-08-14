package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;
    // 注入订单明细的对象
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     *  用户下单
     * @param orders
     */
    @Transactional // 由于要操作多次数据库，和多张表，要保证事务的一致性，
    public void submit(Orders orders) {

        // 获得当前用户id
        Long userId = BaseContext.getCurrentId();

        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        // 根据条件userId查询购物车的数据（所以调用购物车的业务层对象）
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);// 查到的是一个集合

        if (shoppingCarts == null || shoppingCarts.size() == 0) { // 如果购物车对象为null，或者购物车对象的数量为0
            // 就抛异常
            throw new CustomException("购物车为空，不能下单");

        }
        // 查询用户数据
        User user = userService.getById(userId);

        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }
        //IdWorker 是mp提供的类，使用其getId方法，生成唯一id，当作我们的订单号
        long orderId = IdWorker.getId(); // 订单号

        /**
         * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
         * AtomicInteger 是对int 类型的一个封装，提供原子性的访问和更新操作
         * 其原子性的操作实现是基于CAS (compare-and-swap)技术
         * 如果当前数值不变，代码没有其他线程进行并发修改，则成功更新。
         */
        // 这里简单理解就是创建了一个更加安全的Integer类，起始值为0，这个类在多线程保证安全
        AtomicInteger amount = new AtomicInteger(0); // amout是金额对象


        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        // 向订单表插入数据，一条数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
//        订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//计算总金额
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee()); // 获取收货人属性对象
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        // 向订单表插入数据，一条数据
        this.save(orders);

        // 向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);// saveBatch是mp添加一个集合对象，list()是查询一个集合

        // 清空购物车数据
        shoppingCartService.remove(wrapper);

    }
}