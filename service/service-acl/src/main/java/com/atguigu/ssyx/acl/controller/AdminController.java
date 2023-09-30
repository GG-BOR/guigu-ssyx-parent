package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-27 21:31
 * @Description:
 **/
@Api(tags = "用户接口")
@RestController
@RequestMapping("/admin/acl/user")
//@CrossOrigin//跨域
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    RoleService roleService;

    //给某个用户分配角色
    //参数用户id 和 多个角色id
    @ApiOperation("用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId,
                           @RequestParam Long[] roleId){
        roleService.saveAdminRole(adminId,roleId);
        return Result.ok(null);
    }

    //获取某个用户的所有角色,根据用户id查询用户分配角色列表
    @ApiOperation("获取用户角色")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId){
        //返回map集合包含两部分数据：所有角色 和 用户分配角色列表
        Map<String,Object> map = roleService.getRoleByAdminId(adminId);
        return Result.ok(map);
    }

    //1.用户列表
    @ApiOperation("用户列表")
    @GetMapping("{current}/{limit}")
    public Result list(@PathVariable Long current,
                       @PathVariable Long limit,
                       AdminQueryVo adminQueryVo){
        Page<Admin> pageParam = new Page<>(current,limit);
        //相当于在adminService的方法中有current、limit、username、name四个参数,后面两个是adminQueryVo中给的
        IPage<Admin> pageModel = adminService.selectUserPage(pageParam,adminQueryVo);
        return Result.ok(pageModel);
    }
    //2.id查询用户
    @ApiOperation("id查询用户")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }
    //3.添加用户
    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result add(@RequestBody Admin admin){
        //获取密码
        String password = admin.getPassword();
        //对输入密码进行加密 MD5
        String encrypt = MD5.encrypt(password);
        //设置到admin对象里面
        admin.setPassword(encrypt);
        //调用方法添加
        boolean save = adminService.save(admin);
        if(save){
            return Result.ok(null);
        }
        return Result.fail(null);
    }

    //4.修改删除
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        boolean update = adminService.updateById(admin);
        if(update){
            return Result.ok(null);
        }return Result.fail(null);
    }

    //5.id删除
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean b = adminService.removeById(id);
        if(b){
            return Result.ok(null);
        }return Result.fail(null);
    }
    //6.批量删除
    @ApiOperation("批量删除用户")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long>ids){
        boolean b = adminService.removeByIds(ids);
        if(b){
            return Result.ok(null);
        }return Result.fail(null);
    }
}
