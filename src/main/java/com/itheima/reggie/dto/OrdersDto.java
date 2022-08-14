package com.itheima.reggie.dto;

import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.ShoppingCart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    // 订单详情数据
    private List<OrderDetail> OrderDetail = new ArrayList<>();
    private int sumNum;

}
