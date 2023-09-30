package com.atguigu.ssyx.search.receive;

import com.atguigu.ssyx.constant.MqConst;
import com.atguigu.ssyx.search.service.SkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-11 15:08
 * @Description:
 **/
@Component
public class SkuReceiver {

    @Autowired
    private SkuService skuService;

    /**
     * es得到上架通知
     * @param skuId
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_UPPER,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_UPPER}
    ))
    public void upperSku(Long skuId, Message message, Channel channel) throws IOException {
        if(skuId != null){
            //调用方法上架商品
            skuService.upperSku(skuId);
        }
        //通过basicAck方法确认消息已经被正确处理，这样RabbitMQ会把这条消息从队列中删除
        //消息的deliveryTag（消息在RabbitMQ中的唯一标识）和是否可重入
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * es商品下架
     * @param skuId
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_LOWER,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_LOWER}
    ))
    public void lowerSku(Long skuId, Message message, Channel channel) throws IOException {
        if(skuId!=null){
            //调用方法下架
            skuService.lowerSku(skuId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
