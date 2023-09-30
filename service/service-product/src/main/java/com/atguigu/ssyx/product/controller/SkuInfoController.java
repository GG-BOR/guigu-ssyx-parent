package com.atguigu.ssyx.product.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
@Api(tags="商品Sku管理")
@RestController
@RequestMapping("/admin/product/skuInfo")
//@CrossOrigin
public class SkuInfoController {
    @Autowired
    SkuInfoService skuInfoService;

    @ApiOperation("获取sku分页列表")
    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit,
                              SkuInfoQueryVo skuInfoQueryVo){
        Page<SkuInfo> pageParam = new Page<>(page,limit);
        IPage<SkuInfo> pageModel = skuInfoService.selectPageSkuInfo(pageParam,skuInfoQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("添加商品sku信息")
    @PostMapping("/save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok();
    }

    @ApiOperation("获取商品sku信息")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        SkuInfoVo skuInfoVo = skuInfoService.getSkuInfo(id);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation("修改商品sku信息")
    @PutMapping("/update")
    public Result update(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        skuInfoService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeByIds(idList);
        return Result.ok();
    }
    //0->未审核；1->审核通过
    @ApiOperation("商品审核")
    @GetMapping("/check/{id}/{status}")
    public Result check(@PathVariable Long id,
                        @PathVariable Integer status){
        skuInfoService.check(id,status);
        return Result.ok();
    }
    //0->下架；1->上架
    @ApiOperation("商品上架")
    @GetMapping("/publish/{id}/{status}")
    public Result publish(@PathVariable Long id,
                          @PathVariable Integer status){
        skuInfoService.publish(id,status);
        return Result.ok();
    }

    @ApiOperation("新人专享")
    @GetMapping("isNewPerson/{id}/{status}")
    public Result isNewPerson(@PathVariable("id") Long id,
                              @PathVariable("status") Integer status) {
        skuInfoService.isNewPerson(id, status);
        return Result.ok();
    }

}

