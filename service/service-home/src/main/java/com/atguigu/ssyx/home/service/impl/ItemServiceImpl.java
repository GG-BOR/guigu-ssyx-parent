package com.atguigu.ssyx.home.service.impl;

import com.atguigu.ssyx.client.activity.ActivityFeignClient;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.client.search.SkuFeignClient;
import com.atguigu.ssyx.home.config.ThreadPoolConfig;
import com.atguigu.ssyx.home.service.ItemService;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-21 12:55
 * @Description:
 **/
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ThreadPoolConfig threadPoolConfig;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private SkuFeignClient skuFeignClient;
    /**
     * 获取sku详细信息
     *
     * @param skuId
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> item(Long skuId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        ThreadPoolExecutor threadPoolExecutor = threadPoolConfig.threadPoolExecutor();
        //查询skuInfo
        CompletableFuture<SkuInfoVo> skuInfoVoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //远程调用获取sku对应数据
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(skuId);
            result.put("skuInfoVo", skuInfoVo);
            return skuInfoVo;
        },threadPoolExecutor);

        //sku对应优惠券信息
        CompletableFuture<Void> activityCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用获取优惠券信息
            Map<String, Object> activityMap = activityFeignClient.findActivityAndCoupon(skuId,userId);
            result.putAll(activityMap);
        },threadPoolExecutor);

        //更新商品热度
        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用更新热度
            skuFeignClient.incrHotScore(skuId);
        },threadPoolExecutor);

        //任务组合 allOf() 所有线程结束才会结束
        CompletableFuture.allOf(skuInfoVoCompletableFuture,
                                activityCompletableFuture,
                                hotCompletableFuture)
                .join();
        return result;
    }
}
