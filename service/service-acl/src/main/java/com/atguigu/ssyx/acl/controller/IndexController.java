package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-26 22:27
 * @Description:
 **/
//@CrossOrigin//添加此注解可以解决 跨域问题：前端和后端的 协议、ip、端口号中有不同
@Api(tags = "登录接口")//为了有中文的提示
@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {

    /**
     * 1.login登录
     * @return
     */
    //admin/acl/index/login
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(){
        //返回token值
        Map<String,String> map = new HashMap<>();
        map.put("token","token-admin");
        return Result.ok(map);
    }

    /**
     * 2.getInfo获取信息
     * @return
     */
    //url:/admin/acl/index/info
    @ApiOperation("获取信息")
    @GetMapping("/info")
    public Result info(){
        Map<String,String> map = new HashMap<>();
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    /**
     * 3.logout退出
     * @return
     */
    //url: '/admin/acl/index/logout'
    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok(null);
    }

}
