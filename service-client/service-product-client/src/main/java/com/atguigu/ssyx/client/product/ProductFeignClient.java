package com.atguigu.ssyx.client.product;

import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.product.SkuStockLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    //根据分类id获取分类信息
    @GetMapping("api/product/inner/getCategory/{categoryId}")
    Category getCategory(@PathVariable("categoryId") Long categoryId);

    //根据skuid获取sku信息
    @GetMapping("api/product/inner/getSkuInfo/{skuInfoId}")
    SkuInfoVo getSkuInfo(@PathVariable("skuInfoId") Long skuInfoId);

    //根据skuId列表获取sku信息列表
    @PostMapping("/api/product/inner/findSkuInfoList")
    List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    //关键字查询sku信息
    @GetMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    List<SkuInfo> findSkuInfoByKeyword(@PathVariable String keyword) ;

    //根据categoryId列表获取category信息列表
    @PostMapping("/api/product/inner/finCategoryList")
    List<Category> findCategoryList(List<Long> categoryIdList);

    //获取所有分类
    @GetMapping("/api/product/inner/findAllCategoryList")
    List<Category> findAllCategoryList();

    //获取新人专享商品
    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList();

    //根据skuId获取sku全部信息
    @GetMapping("/api/product/inner/getSkuInfoVo/{skuId}")
    SkuInfoVo getSkuInfoVo(@PathVariable("skuId") Long skuId);

    //验证和锁定库存
    @PostMapping("/api/product/inner/checkAndLock/{orderNo}")
    Boolean checkAndLock(@RequestBody List<SkuStockLockVo> skuStockLockVoList, @PathVariable String orderNo);
}
