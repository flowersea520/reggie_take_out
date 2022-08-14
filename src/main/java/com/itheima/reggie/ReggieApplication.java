package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
// 在启动类中配置扫描Servlet组件的注解
@ServletComponentScan // 扫描WebFilter注解，创建过滤器
// 开启事务
@EnableTransactionManagement
public class ReggieApplication {
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        // 打印日志
        log.info("项目启动成功");
    }
}
