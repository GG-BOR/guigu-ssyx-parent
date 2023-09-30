package com.atguigu.ssyx.home.controller;

import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.product.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-19 12:30
 * @Description:
 **/
@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {

    @Autowired
    private ProductFeignClient productFeignClient;

    //查询分类信息
    @ApiOperation("获取分类信息")
    @GetMapping("category")
    public Result categoryList(){
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        return Result.ok(categoryList);
    }
}
