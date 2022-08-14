package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * annotations 是注释的意思
 */
                    // 只要类上面加了这两个注解，就会被这个类处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody // 将响应的数据以json数据返回  注解作用：设置当前控制器方法响应内容为当前返回值（将返回值转换为json），无需解析
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) // 拦截异常，拦截类型为sql异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //  日志输出异常打印信息
      log.error(ex.getMessage());

      if (ex.getMessage().contains("Duplicate entry")){
          // 如果异常信息包含了 Duplicate entry这个字符串信息，就以空格 来分割异常信息，
          String[] split = ex.getMessage().split(" ");
          // Duplicate entry 'zhangsan' for key 'employee.idx_username'
          // 异常信息中的索引为2的值，就是username账号，提示账号已经存在
          String mes = split[2] + "已存在";
          return R.error(mes);
      }

        return R.error("未知错误");
    }

    /**
     *  异常处理方法 -- 处理 自定义的CustomException
     * @return
     */
    @ExceptionHandler(CustomException.class) // 拦截异常，拦截类型为sql异常
    public R<String> exceptionHandler(CustomException ex){
        //  日志输出异常打印信息
        log.error(ex.getMessage()); // 这里是调用父类的方法


        return R.error(ex.getMessage());
    }


}
