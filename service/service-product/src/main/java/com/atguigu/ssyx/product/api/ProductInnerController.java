package com.atguigu.ssyx.product.api;

import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.service.CategoryService;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.product.SkuStockLockVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-09 22:12
 * @Description:
 **/
@RestController
@RequestMapping("/api/product")
public class ProductInnerController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuInfoService skuInfoService;

    //根据分类id获取分类信息
    @GetMapping("/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId){
        Category category = categoryService.getById(categoryId);
        return category;
    }

    //根据skuId获取sku信息
    @GetMapping("/inner/getSkuInfo/{skuInfoId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuInfoId){
        return skuInfoService.getSkuInfo(skuInfoId);
    }

    //根据skuId列表获取sku信息列表
    @PostMapping("inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList){
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoList(skuIdList);
        return skuInfoList;
    }

    //关键字查询sku信息
    @GetMapping("/inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable String keyword){
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoByKeyword(keyword);
        return skuInfoList;
    }


    //根据categoryId列表获取category信息列表
    @PostMapping("/inner/finCategoryList")
    public List<Category> findCategoryList(@RequestBody List<Long> categoryIdList){
        return categoryService.listByIds(categoryIdList);
    }

    //获取所有分类
    @GetMapping("/inner/findAllCategoryList")
    public List<Category> findAllCategoryList(){
        List<Category> list = categoryService.list();
        return list;
    }

    //获取新人专享商品
    @GetMapping("inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList(){
        List<SkuInfo> list = skuInfoService.findNewPersonSkuInfoList();
        return list;
    }
    //根据skuId获取sku信息
    @ApiOperation(value = "根据skuId获取sku信息")
    @GetMapping("inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable("skuId") Long skuId) {
        return skuInfoService.getSkuInfo(skuId);
    }

    //验证和锁定库存
    @ApiOperation(value = "锁定库存")
    @PostMapping("inner/checkAndLock/{orderNo}")
    public Boolean checkAndLock(@RequestBody List<SkuStockLockVo> skuStockLockVoList, @PathVariable String orderNo) {
        return skuInfoService.checkAndLock(skuStockLockVoList, orderNo);
    }



}
