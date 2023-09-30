package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
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
 * @create: 2023-08-27 14:39
 * @Description:
 **/
@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
//@CrossOrigin
public class RoleController {
    //注入service
    @Autowired
    RoleService roleService;


    //1.角色列表（条件分页查询）
    @ApiOperation("角色条件分页查询")
    @GetMapping("/{current}/{limit}")
    public Result pageList(@PathVariable Long current,
                           @PathVariable Long limit,
                           RoleQueryVo roleQueryVo){
        //1.创建page对象，传递当前页和每页的记录数
        //current:当前页
        //limit:每页显示记录数
        Page<Role> pageParam = new Page<>(current,limit);
        //2.调用service方法实现条件分页查询，返回分页对象
        IPage<Role> pageModel = roleService.selectRolePage(pageParam,roleQueryVo);
        return Result.ok(pageModel);
    }

    //2.获取某个角色
    @ApiOperation("获取角色")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
    //3.保存一个新角色
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        boolean save = roleService.save(role);
        if(save){
            return Result.ok(null);
        }return Result.fail(null);
    }

    //4. 更新一个角色
    @ApiOperation("更新角色")
    @PutMapping("/update")
    public Result updateById(@RequestBody Role role){
        boolean b = roleService.updateById(role);
        if(b){
            return Result.ok(role);
        }return Result.fail(role);
    }

    //5.删除某个角色
    @ApiOperation("删除角色")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id){
        boolean b = roleService.removeById(id);
        if(b){
            return Result.ok(null);
        }return Result.fail(null);
    }

    //6.批量删除多个角色
    //json数组[1,2,3] --- java的List集合
    @ApiOperation("批量删除角色")
    @DeleteMapping("/batchRemove")
    public Result removeRoles(@RequestBody List<Long> ids){
        boolean b = roleService.removeByIds(ids);
        if(b){
            return Result.ok(null);
        }return Result.fail(null);
    }



}
