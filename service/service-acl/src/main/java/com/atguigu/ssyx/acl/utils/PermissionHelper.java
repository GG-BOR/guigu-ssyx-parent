package com.atguigu.ssyx.acl.utils;

import com.atguigu.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-30 21:34
 * @Description:
 **/
public class PermissionHelper {
    public static List<Permission> buildPermission(List<Permission> allList) {
        //创建最终数据封装List集合
        List<Permission> trees = new ArrayList<>();
        //遍历所有菜单list集合，得到第一层数据，pid=0
        for(Permission permission:allList){
            //pid=0数据是第一层数据
            if(permission.getPid()==0){
                permission.setLevel(1);
                //调用方法，从第一层开始往下找
                trees.add(findChildren(permission,allList));
            }
        }
        return trees;
    }

    //递归往下找
    //permission上层节点，从这里往下找
    //allList所有菜单
    private static Permission findChildren(Permission permission, List<Permission> allList) {
        permission.setChildren(new ArrayList<Permission>());
        //遍历allList所有菜单数据
        //判断：当前节点pid是否和上一层的id一样，封装，递归往下找
        for(Permission it:allList){
            //当前节点id=pid是否一样
            if(it.getPid().longValue()==permission.getId().longValue()){
                int level = permission.getLevel()+1;
                it.setLevel(level);
                //如果没有下一层级了
                if(permission.getChildren()==null){
                    //直接设置初值
                    permission.setChildren(new ArrayList<Permission>());
                }
                //封装下一层数据
                permission.getChildren().add(findChildren(it,allList));

            }
        }
        return permission;
    }
}
