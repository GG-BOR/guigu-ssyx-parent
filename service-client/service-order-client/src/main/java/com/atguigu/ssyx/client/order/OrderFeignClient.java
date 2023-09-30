package com.atguigu.ssyx.client.order;

import com.atguigu.ssyx.model.order.OrderInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-28 23:45
 * @Description:
 **/
@FeignClient("service-order")
public interface OrderFeignClient {

    @ApiOperation("根据orderNo查询订单信息")
    @GetMapping("/api/order/inner/getOrderInfo/{orderNo}")
    OrderInfo getOrderInfo(@PathVariable("orderNo") String orderNo);
}
