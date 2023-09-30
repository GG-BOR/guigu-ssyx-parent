package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-27 14:42
 * @Description:
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    AdminRoleService adminRoleService;
    //1.角色列表(条件分页查询)
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值(角色名称)
        String roleName = roleQueryVo.getRoleName();

        //创建mp条件角色
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();

        //判断条件值是否为空，不为封装查询条件
        if(!StringUtils.isEmpty(roleName)){
            //模糊查询角色名称
            wrapper.like(Role::getRoleName,roleName);
        }
        //调用方法实现条件分页查询,
        IPage<Role> roleIPage = baseMapper.selectPage(pageParam,wrapper);
        //返回分页对象
        return roleIPage;
    }

    /**
     * 查询了role表中的所有角色，并将它们以列表形式返回，存储在allRolesList中。
     * 根据用户ID（adminId）查询了用户分配的角色列表。首先，它在admin_role表（假设这是用户角色关系表）中查询了满足条件的记录，然后通过Stream API和Lambda表达式，提取了这些记录的角色ID，并将它们以列表形式返回，存储在roleIdsList中。
     * 创建了一个新的列表assignRoleList，用于存储用户配置的角色。
     * 遍历了所有角色列表allRolesList，对于每个角色，检查其ID是否在roleIdsList中存在。如果存在，则将该角色添加到assignRoleList中。
     * 最后，将结果封装到一个Map中并返回。这个Map包含两个键值对："allRolesList"对应的值是allRolesList，"assignRoles"对应的值是assignRoleList。
     * 这段代码的目的是找出用户配置的角色，并将这些角色及其所有角色返回为一个Map。
     * @param adminId
     * @return
     */
    //2.查询所有角色 和 用户分配角色列表
    @Override
    public Map<String, Object> getRoleByAdminId(Long adminId) {
        //1.查询role表中的所有角色返回列表格式
        List<Role> allRolesList = baseMapper.selectList(null);

        //2 根据用户id查询用户分配角色列表=
        //2.1 根据用户id查询 用户角色关系表 admin_role 查询用户分配角色id列表
        //LambdaQueryWrapper<AdminRole>加入这个泛型表示查询AdminRole表
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper();
        //设置查询条件，根据用户id adminId
        wrapper.eq(AdminRole::getAdminId,adminId);
        //根据这个查询条件返回用户分配的角色列表.
        //这个状态属于是查询到了满足这个条件所有行，接下来要查找到对应的roleId
        List<AdminRole> adminRolesList = adminRoleService.list(wrapper);

        //2.2 通过第一步返回集合，获取所有角色id的列表List<AdminRole> -- List<Long>
        List<Long> roleIdsList = adminRolesList.stream()
                                                .map(item -> item.getRoleId())
                                                .collect(Collectors.toList());
        //2.3创建新的list集合，用于存储用户配置角色
        List<Role> assignRoleList = new ArrayList<>();

        //2.4遍历所有角色列表allRolesList,得到每个角色
        //判断所有角色里面是否包含已经分配角色id，封装到2.3里面新的list集合
        for(Role role:allRolesList){
            //判断角色列表中是否有
            if(roleIdsList.contains(role.getId())){
                assignRoleList.add(role);
            }
        }

        //封装到map，返回
        Map<String,Object> result = new HashMap<>();
        //所有角色列表
        result.put("allRolesList",allRolesList);
        //用户分配角色列表
        result.put("assignRoles",assignRoleList);
        return result;
    }

    //用户分配角色
    @Override
    public void saveAdminRole(Long adminId, Long[] roleIds) {
        //1.删除用户已经分配过的角色数据
        //根据用户id删除admin_role表里面对应数据
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        adminRoleService.remove(wrapper);

        //2.给用户分配角色
        List<AdminRole> list = new ArrayList<>();
        for (Long roleId: roleIds) {
            //先创建实体类对象
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            //将实体类加入到列表中后可以统一添加到数据库中
            list.add(adminRole);
        }
        //统一保存
        adminRoleService.saveBatch(list);
    }
}
