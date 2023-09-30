package com.atguigu.ssyx.sys.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.sys.Region;
import com.atguigu.ssyx.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-08-31
 */
@Api(tags = "区域接口")
@RestController
@RequestMapping("/admin/sys/region")
//@CrossOrigin
public class RegionController {
    @Autowired
    RegionService regionService;

    /**
     * 根据区域关键字查询区域列表信息
     * @param keyword
     * @return
     */
    @ApiOperation("根据区域关键字查询区域列表信息")
    @GetMapping("/findRegionByKeyword/{keyword}")
    public Result findRegionByKeyword(@PathVariable String keyword){
        List<Region> list = regionService.getRegionByKeyword(keyword);
        return Result.ok(list);
    }
}

