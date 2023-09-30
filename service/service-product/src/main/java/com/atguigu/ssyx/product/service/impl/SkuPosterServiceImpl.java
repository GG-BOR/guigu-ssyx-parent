package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.product.SkuPoster;
import com.atguigu.ssyx.product.mapper.SkuPosterMapper;
import com.atguigu.ssyx.product.service.SkuPosterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster> implements SkuPosterService {

    /**
     * 根据id查询商品海报列表
     * @param id
     * @return
     */
    @Override
    public List<SkuPoster> getPosterListBySkuId(Long id) {
        LambdaQueryWrapper<SkuPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId,id);
        List<SkuPoster> skuPosters = baseMapper.selectList(wrapper);
        return skuPosters;
    }

    /**
     * 添加商品海报列表
     */
    @Override
    public void savePosterList(SkuInfo skuInfo,List<SkuPoster> skuPosterList) {
        //判断sku海报的列表是否为空
        if(!CollectionUtils.isEmpty(skuPosterList)){
            for (SkuPoster skuPoster:skuPosterList) {
                //海报表中设置外键skuId
                skuPoster.setSkuId(skuInfo.getId());
            }
        }
    }
}
