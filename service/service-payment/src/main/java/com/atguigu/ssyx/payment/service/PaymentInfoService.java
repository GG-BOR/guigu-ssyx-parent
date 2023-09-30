package com.atguigu.ssyx.payment.service;

import com.atguigu.ssyx.enums.PaymentType;
import com.atguigu.ssyx.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentInfoService extends IService<PaymentInfo> {
    //根据orderNo查询支付记录
    PaymentInfo getPaymentInfoByOrderNo(String orderNo);
    //添加支付记录
    PaymentInfo savePaymentInfo(String orderNo);
    //3.1支付成功，修改支付记录表状态：已经支付
    //3.2支付成功，修改订单记录已经支付，库存扣减
    void paySuccess(String outTradeNo, Map<String, String> resultMap);
}
