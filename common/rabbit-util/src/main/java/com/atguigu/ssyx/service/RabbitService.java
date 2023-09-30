package com.atguigu.ssyx.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-10 22:50
 * @Description:
 **/
@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息的方法
     * @param exchange 交换机
     * @param routingKey 路由
     * @param message 消息
     * @return
     */
    public boolean sendMessage(String exchange,String routingKey,Object message){
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
        return true;
    }

}
