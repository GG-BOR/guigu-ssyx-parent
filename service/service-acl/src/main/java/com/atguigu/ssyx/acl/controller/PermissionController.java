package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-29 16:07
 * @Description:
 **/
@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
//@CrossOrigin //跨域
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 给某个角色分配权限
     * @param roleId  角色id
     * @param permissionId  菜单id
     * @return
     */
    @ApiOperation("分配角色菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long roleId,
                           @RequestParam Long[] permissionId){
        permissionService.saveRolePermission(roleId,permissionId);
        return Result.ok(null);
    }

    /**
     * 获取某个角色的所有权限（菜单），根据角色id查询角色分配权限列表
     * @param roleId 角色id
     * @return
     */
    @ApiOperation("获取角色菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        Map<String,Object> map = permissionService.getPermissionByRoleId(roleId);
        return Result.ok(map.get("allPermissionList"));
    }

    //查询所有菜单
    @ApiOperation("查询所有菜单")
    @GetMapping
    public Result list(){
        List<Permission> list =  permissionService.queryAllPermission();
        return Result.ok(list);
    }
    //添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody Permission permission){
        boolean save = permissionService.save(permission);
        if(save){
            return Result.ok(null);
        }return Result.fail(null);
    }
    //修改菜单
    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission){
        boolean save = permissionService.updateById(permission);
        if(save){
            return Result.ok(null);
        }return Result.fail(null);
    }

    //删除菜单
    @ApiOperation("递归删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        permissionService.removeChildById(id);
        return Result.ok(null);
    }
}
