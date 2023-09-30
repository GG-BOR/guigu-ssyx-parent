package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {
    //递归删除菜单
    void removeChildById(Long id);

    //查询所有菜单
    List<Permission> queryAllPermission();


    Map<String, Object> getPermissionByRoleId(Long roleId);

    void saveRolePermission(Long roleId, Long[] permissionId);
}
