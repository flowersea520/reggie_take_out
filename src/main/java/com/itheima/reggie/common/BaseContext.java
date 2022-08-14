package com.itheima.reggie.common;

/**
 *   基于ThreadLocal封装工具类，用户保存和获取当前登录用户id（不会混淆，也不会覆盖，这个ThreadLcal只对当前线程有效
 */
public class BaseContext {
    // 这里用静态的static，因为是一个工具类，容易不用创建对象，用类名调用即可
    // 这个线程id值是Long，所以泛型是Long
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) { // 这个方法是在获取到session中调用，将id传给这个方法
        threadLocal.set(id); // 将session中的id存入到线程中
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
