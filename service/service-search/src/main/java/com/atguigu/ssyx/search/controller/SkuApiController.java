package com.atguigu.ssyx.search.controller;

import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.search.service.SkuService;
import com.atguigu.ssyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-09 21:08
 * @Description:
 **/
@Api(tags = "上下架接口")
@RestController
@RequestMapping("api/search/sku")
//@CrossOrigin
public class SkuApiController {
    @Autowired
    private SkuService skuService;
    //上架
    @GetMapping("inner/upperSku/{skuId}")
    public Result upperSku(@PathVariable Long skuId){
        skuService.upperSku(skuId);
        return Result.ok();
    }
    //下架
    @GetMapping("inner/lowerSku/{skuId}")
    public Result lowerSku(@PathVariable Long skuId){
        skuService.lowerSku(skuId);
        return Result.ok();
    }

    //查询分类
    @GetMapping("{page}/{limit}")
    public Result listSku(@PathVariable Integer page,
                          @PathVariable Integer limit,
                          SkuEsQueryVo skuEsQueryVo){
        //创建pageable对象，0代表第一页
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<SkuEs> pageMode = skuService.search(pageable,skuEsQueryVo);
        return Result.ok(pageMode);
    }
}
