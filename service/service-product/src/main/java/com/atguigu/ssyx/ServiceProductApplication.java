package com.atguigu.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-02 15:19
 * @Description:
 **/

@SpringBootApplication
@EnableSwagger2WebMvc
@EnableDiscoveryClient//添加Nacos客户端 的注解
public class ServiceProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class, args);
    }
}
