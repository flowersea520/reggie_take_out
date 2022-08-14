package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  自定义元数据对象处理器
 */

@Component // 被Spring容器管理
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {// MetaObjectHandler元对象处理程序的意思

    /**
     *  插入操作，自动填充
     * @param metaObject
     */
    // 重写填充公共字段填充的方法（插入时填充）
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】。。。");
        log.info(metaObject.toString()); // 使用日志打印该对象
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 暂时写死，这个数据库中的id类型是bigint，对应的java类型是Long
//        metaObject.setValue("createUser",new Long(1));
//        metaObject.setValue("updateUser",new Long(1));
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     *  更新操作，自动填充
     * @param metaObject
     */

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】。。。");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);


//      修改时填充：只要填修改时间和修改人就行
        // 第一个参数是字段名-- 对应的实体类的属性名， 第二个参数是，值
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 本来写死了，现在替换用ThreadLocal的工具类BaseContext
//        metaObject.setValue("updateUser",new Long(1));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
