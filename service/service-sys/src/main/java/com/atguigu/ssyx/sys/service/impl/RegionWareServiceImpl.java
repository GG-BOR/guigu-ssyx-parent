package com.atguigu.ssyx.sys.service.impl;


import com.atguigu.ssyx.common.exception.SsysException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.model.sys.RegionWare;
import com.atguigu.ssyx.sys.mapper.RegionWareMapper;
import com.atguigu.ssyx.sys.service.RegionWareService;
import com.atguigu.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-31
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Override
    public IPage<RegionWare> selectPageRegionWare(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {
        //获得关键字
        String keyword = regionWareQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        //当关键字不为空时
        if(!StringUtils.isEmpty(keyword)){
            //封装条件
            //根据区域名称 或者 仓库名称查询
            wrapper.like(RegionWare::getRegionName,keyword)
                    .or().like(RegionWare::getWareName,keyword);
        }
        Page<RegionWare> regionWarePage = baseMapper.selectPage(pageParam, wrapper);
        return regionWarePage;
    }

    /**
     * 添加开通区域
     * @param regionWare
     */
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        //判断区域是否开通
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionWare::getRegionId,regionWare.getRegionId());
        Integer count = baseMapper.selectCount(wrapper);
        //该区域已开通的话就报错
        if(count>0){
            throw new SsysException(ResultCodeEnum.REGION_OPEN);
        }
        //否则就添加该区域
        baseMapper.insert(regionWare);
    }

    /**
     * 取消开通区域
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        //根据id获得实体类
        RegionWare regionWare = baseMapper.selectById(id);
        //开通状态重新赋值
        regionWare.setStatus(status);
        //更新数据
        baseMapper.updateById(regionWare);


    }
}
