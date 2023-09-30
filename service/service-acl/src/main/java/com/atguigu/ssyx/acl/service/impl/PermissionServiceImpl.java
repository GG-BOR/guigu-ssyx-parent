package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.service.RolePermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-29 16:08
 * @Description:
 **/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    //递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //创建当前菜单的子菜单的列表
        List<Long> idList = new ArrayList<>();
        //重点：递归找当前菜单的子菜单
        this.getAllPermissionId(id,idList);
        //菜单列表中最后加入一个菜单的id
        idList.add(id);
    }

    //重点：递归找当前菜单的子菜单
    //第一个参数是当前菜单id
    //第二个参数最终封装list集合，包含所有菜单id
    private void getAllPermissionId(Long id, List<Long> idList) {
        //根据当前菜单id查询下面子菜单
        //select * from permission where pid=2
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        //调用getPid方法参数是id，
        wrapper.eq(Permission::getPid,id);
        List<Permission> permissions = baseMapper.selectList(wrapper);

        //递归查询是否还有子菜单，有继续递归查询
        //将列表转换为Stream数据流，并且遍历，item代表列表中的子菜单
        permissions.stream().forEach(item->{
            //封装菜单id到idList集合里面
            idList.add(item.getId());
            //递归
            this.getAllPermissionId(item.getId(),idList);
        });
    }

    //查询所有菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1.查询所有菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2.转换要求数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }
    @Autowired
    RolePermissionService rolePermissionService;
    /**
     * 获取某个角色的所有权限（菜单），根据角色id查询角色分配菜单列表
     * @param roleId
     * @return
     */
    //查询所有菜单和角色分配的菜单
    @Override
    public Map<String, Object> getPermissionByRoleId(Long roleId) {
        //1.查询permission表中所有的菜单并返回列表
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //1.1调用PermissionHelper工具类转换要求的数据格式是tree类型的有层级的
        List<Permission> buildAllPermissionList = PermissionHelper.buildPermission(allPermissionList);

        //2.根据角色id查询角色已经分配的菜单列表
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        //查询RolePermission表中的role_id
        wrapper.eq(RolePermission::getRoleId,roleId);
        //2.1根据这个查询条件返回角色分配的菜单列表
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);

        //2.2通过第一步返回集合，获取所有给角色分配过的菜单id的列表
        List<Long> PermissionIdsList = rolePermissionList.stream()
                                    .map(item -> item.getPermissionId())
                                    .collect(Collectors.toList());
        //2.3创建list集合用来存放角色分配的菜单
        List<Permission> assignPermissionList = new ArrayList<>();

        for(Permission permission:buildAllPermissionList){
            //从第一层级开始找
            if(permission.getPid()==0){
                if(PermissionIdsList.contains(permission.getId())){
                    //如果菜单的id在角色分配过的菜单id列表中就将状态设置为选中
                    permission.setSelect(true);
                    assignPermissionList.add(permission);
                }
                //递归一直找到下一层级的符合要求菜单id
                this.setPermissionChildren(permission.getChildren(),PermissionIdsList);
            }

        }
        Map<String,Object> result = new HashMap<>();
        //所有菜单列表
        result.put("allPermissionList",buildAllPermissionList);
        result.put("assignPermissionList",assignPermissionList);
        return result;
    }

    private void setPermissionChildren(List<Permission> children, List<Long> permissionIdsList) {
        for(Permission permission:children){
            if(permissionIdsList.contains(permission.getId())){
                permission.setSelect(true);
            }
            this.setPermissionChildren(permission.getChildren(),permissionIdsList);
        }
    }

    /**
     * 分配角色权限
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void saveRolePermission(Long roleId, Long[] permissionIds) {
        //1.删除角色已经分配过的菜单数据
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        //调用父类的IService类中的方法是mybatis-plus的特性
        rolePermissionService.remove(wrapper);
        //2.给用户分配角色
        List<RolePermission> list = new ArrayList<>();
        for(Long permissionId:permissionIds){
            //创建实体类对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            list.add(rolePermission);
        }
        //批量保存
        rolePermissionService.saveBatch(list);
    }
}
