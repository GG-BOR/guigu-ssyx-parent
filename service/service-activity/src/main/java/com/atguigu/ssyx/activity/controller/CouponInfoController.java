package com.atguigu.ssyx.activity.controller;


import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netflix.loadbalancer.PingUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-09-11
 */
@Api("优惠券接口")
@RestController
@RequestMapping("/admin/activity/couponInfo")
//@CrossOrigin
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("优惠券分页查询")
    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit){
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        IPage<CouponInfo> pageModel = couponInfoService.getPageList(pageParam);
        return Result.ok(pageModel);
    }

    @ApiOperation("添加优惠券")
    @PostMapping("/save")
    public Result save(@RequestBody CouponInfo couponInfo){
        couponInfoService.save(couponInfo);
        return Result.ok();
    }

    @ApiOperation("根据id查询优惠券")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        CouponInfo couponInfo = couponInfoService.getCouponInfo(id);
        return Result.ok(couponInfo);
    }

    @ApiOperation("查询优惠券规则数据")
    @GetMapping("/findCouponRuleList/{id}")
    public Result findCouponRuleList(@PathVariable Long id){
        Map<String,Object> couponInfoMap = couponInfoService.findCouponRuleList(id);
        return Result.ok(couponInfoMap);
    }

    @ApiOperation("添加优惠券规则")
    @PostMapping("/saveCouponRule")
    public Result saveCouponRule(@RequestBody CouponRuleVo couponRuleVo){
        couponInfoService.saveCouponRule(couponRuleVo);
        return Result.ok();
    }

}

