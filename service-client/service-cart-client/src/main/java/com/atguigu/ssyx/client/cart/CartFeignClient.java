package com.atguigu.ssyx.client.cart;

import com.atguigu.ssyx.model.order.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-cart")
public interface CartFeignClient {

    /**
     * 根据用户Id 查询购物车列表
     */
    @GetMapping("api/cart/inner/getCartCheckedList/{userId}")
    List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId);
}
