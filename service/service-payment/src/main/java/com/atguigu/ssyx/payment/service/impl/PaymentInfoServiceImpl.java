package com.atguigu.ssyx.payment.service.impl;

import com.atguigu.ssyx.client.order.OrderFeignClient;
import com.atguigu.ssyx.common.exception.SsysException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.constant.MqConst;
import com.atguigu.ssyx.enums.PaymentStatus;
import com.atguigu.ssyx.enums.PaymentType;
import com.atguigu.ssyx.model.order.OrderInfo;
import com.atguigu.ssyx.model.order.PaymentInfo;
import com.atguigu.ssyx.payment.mapper.paymentInfoMapper;
import com.atguigu.ssyx.payment.service.PaymentInfoService;
import com.atguigu.ssyx.service.RabbitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-28 20:49
 * @Description:
 **/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<paymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RabbitService rabbitService;
    /**
     * 根据orderNo查询支付记录
     * @param orderNo
     * @return
     */
    @Override
    public PaymentInfo getPaymentInfoByOrderNo(String orderNo) {
        PaymentInfo paymentInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, orderNo));
        return paymentInfo;
    }

    /**
     * 添加支付记录
     * @param orderNo
     * @return
     */
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        //远程调用，根据orderNo查询订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderNo);
        if(orderInfo==null){
            throw new SsysException(ResultCodeEnum.DATA_ERROR);
        }
        //封装到PaymentInfo
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(PaymentType.WEIXIN);
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setOrderNo(orderInfo.getOrderNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
        String subject = "userID"+orderInfo.getUserId()+"下订单";
        paymentInfo.setSubject(subject);
        //paymentInfo.setTotalAmount(order.getTotalAmount());
        //todo 为了测试统一支付0.01元
        paymentInfo.setTotalAmount(new BigDecimal("0.01"));

        baseMapper.insert(paymentInfo);
        return paymentInfo;
    }

    /**
     * 3.1支付成功，修改支付记录表状态：已经支付
     * 3.2支付成功，修改订单记录已经支付，库存扣减
     * @param orderNo
     * @param resultMap
     */
    @Override
    public void paySuccess(String orderNo, Map<String, String> resultMap) {
        //1 查询当前订单支付记录表状态是否已经支付
        PaymentInfo paymentInfo =
                baseMapper.selectOne(new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo,orderNo));
        if(paymentInfo.getPaymentStatus()!=PaymentStatus.UNPAID){
            return;
        }
        //2 如果支付记录表支付状态没有支付，更新
        paymentInfo.setPaymentStatus(PaymentStatus.PAID);
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));//交易编码
        paymentInfo.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfo);
        //3 整合RabbitMQ实现 修改订单记录已经支付, 库存扣减
        rabbitService.sendMessage(MqConst.EXCHANGE_PAY_DIRECT,MqConst.ROUTING_PAY_SUCCESS,orderNo);
    }
}
