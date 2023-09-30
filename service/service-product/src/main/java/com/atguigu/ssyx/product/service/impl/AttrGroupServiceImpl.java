package com.atguigu.ssyx.product.service.impl;


import com.atguigu.ssyx.model.product.AttrGroup;
import com.atguigu.ssyx.product.mapper.AttrGroupMapper;
import com.atguigu.ssyx.product.service.AttrGroupService;
import com.atguigu.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {

    /**
     * 获取分页列表
     * @param pageParam
     * @param attrGroupQueryVo
     * @return
     */
    @Override
    public IPage<AttrGroup> selectPageAttrGroup(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo) {
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        String name = attrGroupQueryVo.getName();
        if(!StringUtils.isEmpty(name)){
            wrapper.like(AttrGroup::getName,name);
        }
        IPage<AttrGroup> attrGroupPage = baseMapper.selectPage(pageParam, wrapper);
        return attrGroupPage;
    }

    /**
     * 获取全部属性分组
     * @return
     */
    @Override
    public List<AttrGroup> findAllList() {
        //1.正则表达式
/*        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AttrGroup::getSort);*/

        QueryWrapper<AttrGroup> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        List<AttrGroup> attrGroups = baseMapper.selectList(wrapper);
        return attrGroups;
    }
}
