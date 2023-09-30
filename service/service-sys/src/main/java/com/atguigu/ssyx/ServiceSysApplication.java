package com.atguigu.ssyx;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-31 19:17
 * @Description:
 **/
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableDiscoveryClient//添加Nacos客户端注解
public class ServiceSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSysApplication.class,args);
    }
}
