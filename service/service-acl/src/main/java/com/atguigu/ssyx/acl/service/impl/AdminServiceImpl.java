package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.AdminMapper;
import com.atguigu.ssyx.acl.service.AdminService;

import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-27 21:33
 * @Description:
 **/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public IPage<Admin> selectUserPage(Page<Admin> pageParam, AdminQueryVo adminQueryVo) {
        String username = adminQueryVo.getUsername();
        String name = adminQueryVo.getName();
        //LambdaQueryWrapper用于构建SQL查询的辅助类
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper();
        if(!StringUtils.isEmpty(username)){
            wrapper.eq(Admin::getUsername,username);
        }
        if(!StringUtils.isEmpty(name)){
            wrapper.like(Admin::getName,name);
        }
        IPage<Admin> adminPage = baseMapper.selectPage(pageParam, wrapper);
        return adminPage;

    }
}
