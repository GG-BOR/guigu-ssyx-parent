package com.atguigu.ssyx.client.activity;

import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.order.CartInfoVo;
import com.atguigu.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "service-activity")
public interface ActivityFeignClient {

    @PostMapping("/api/activity/inner/findActivity")
    Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    //根据skuId获取营销数据和优惠券
    @GetMapping("/api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
     Map<String,Object> findActivityAndCoupon(@PathVariable Long skuId,
                                              @PathVariable Long userId);
    //获取购物车满足条件的促销与优惠券信息
    @PostMapping("/api/activity/inner/findCartActivityAndCoupon/{userId}")
    OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList,
                                             @PathVariable Long userId);
    //获取购物车对应规则数据
    @PostMapping("/api/activity/inner/findCartActivityList")
    List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoParamList);

    //获取购物车对应优惠券
    @PostMapping("/api/activity/inner/findRangeSkuIdList/{couponId}")
    CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList,
                                         @PathVariable("couponId") Long couponId);
    //如果当前订单使用优惠券，更新优惠券状态

    @GetMapping("/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId,
                                             @PathVariable("userId")Long userId,
                                             @PathVariable("orderId")Long orderId);
}
