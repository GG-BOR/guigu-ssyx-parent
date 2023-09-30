package com.atguigu.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-26 21:38
 * @Description:权限管理模块启动类
 **/
@SpringBootApplication
@EnableSwagger2WebMvc
@EnableDiscoveryClient//添加Nacos客户端注解
public class ServiceAclApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class,args);
    }
}
