package com.atguigu.ssyx.cart.receive;

import com.atguigu.ssyx.cart.service.CartInfoService;
import com.atguigu.ssyx.constant.MqConst;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-28 15:32
 * @Description:order订单的mq接收端cart购物车
 **/
@Component
public class CartReceiver {
    @Autowired
    private CartInfoService cartInfoService;
    //要和发送端保持一致
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_DELETE_CART,durable = "true"),//队列
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER_DIRECT),//交换机
            key = {MqConst.ROUTING_DELETE_CART}//key
    ))
    public void deleteCart(Long userId, Message message, Channel channel) throws IOException {
        if(userId != null){
            cartInfoService.deleteCartCheck(userId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


}
