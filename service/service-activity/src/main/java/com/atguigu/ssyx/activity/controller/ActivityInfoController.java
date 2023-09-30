package com.atguigu.ssyx.activity.controller;


import com.atguigu.ssyx.activity.service.ActivityInfoService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.activity.ActivityInfo;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-09-11
 */
@ApiOperation("营销活动接口")
@RestController
@RequestMapping("/admin/activity/activityInfo")
//@CrossOrigin
public class ActivityInfoController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit){
        Page<ActivityInfo> pageParam = new Page<>(page,limit);
        IPage<ActivityInfo> pageList = activityInfoService.selectPage(pageParam);
        return Result.ok(pageList);
    }

    @ApiOperation(value = "获取活动")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ActivityInfo activityInfo = activityInfoService.getById(id);
        activityInfo.setActivityTypeString(activityInfo.getActivityType().getComment());
        return Result.ok(activityInfo);
    }

    @ApiOperation("添加活动")
    @PostMapping("/save")
    public Result save(@RequestBody ActivityInfo activityInfo){
        activityInfoService.save(activityInfo);
        return Result.ok();
    }

    @ApiOperation("修改活动")
    @PutMapping("/update")
    public Result updateById(@RequestBody ActivityInfo activityInfo){
        activityInfoService.updateById(activityInfo);
        return Result.ok();
    }

    @ApiOperation("删除活动")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        activityInfoService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value="批量删除活动")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        activityInfoService.removeByIds(idList);
        return Result.ok();
    }

    //todo 营销活动规则相关接口
    //1 根据活动id获取活动规则数据
    @ApiOperation("获取活动规则")
    @GetMapping("/findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable Long id){
        Map<String,Object> activityRuleMap = activityInfoService.findActivityRuleList(id);
        return Result.ok(activityRuleMap);
    }
    //2 在活动里面添加规则数据
    @ApiOperation("添加活动规则")
    @PostMapping("/saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo){
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok();
    }
    //3 根据关键字查询匹配sku信息
    @ApiOperation("关键字查询sku信息")
    @GetMapping("/findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable String keyword){
        List<SkuInfo> skuInfoList = activityInfoService.findSkuInfoByKeyword(keyword);
        return Result.ok(skuInfoList);
    }
}

