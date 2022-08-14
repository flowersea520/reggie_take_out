package com.itheima.reggie.common;

/**
 *  自定义业务异常类
 */
public class CustomException extends RuntimeException{ // 继承运行时异常类，要使用他的方法

    public CustomException(String message){ // 定义有参构造器，当创建对象时：给参数，就会调用父类构造器传进去
        super(message); // 父类构造器
    }

}
