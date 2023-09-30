package com.atguigu.ssyx.activity.service;


import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-11
 */
public interface CouponInfoService extends IService<CouponInfo> {


    IPage<CouponInfo> getPageList(Page<CouponInfo> pageParam);

    CouponInfo getCouponInfo(Long id);

    Map<String, Object> findCouponRuleList(Long id);

    void saveCouponRule(CouponRuleVo couponRuleVo);

    List<CouponInfo> findCouponInfoList(Long skuId, Long userId);
    //获取购物车可以使用优惠券
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);
    //获取购物车对应优惠券
    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);
    //更新优惠券使用状态
    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
}
