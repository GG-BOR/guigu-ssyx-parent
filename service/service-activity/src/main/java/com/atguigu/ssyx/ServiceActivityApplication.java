package com.atguigu.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-11 22:36
 * @Description:
 **/
@SpringBootApplication
//@EnableDiscoveryClient,@EnableFeignClients以开启服务发现和Feign客户端的功能，从而实现微服务之间的互相调用和通信。
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityApplication.class,args);
    }
}
