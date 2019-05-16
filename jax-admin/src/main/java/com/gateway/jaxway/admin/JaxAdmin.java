package com.gateway.jaxway.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author huaili
 * @Date 2019/4/3 18:58
 * @Description JaxAdmin启动类
 **/
@SpringBootApplication
@MapperScan("com.gateway.jaxway.admin.dao.mapper")
public class JaxAdmin {
    public static void main(String[] ags){
        SpringApplication.run(JaxAdmin.class);
    }
}
