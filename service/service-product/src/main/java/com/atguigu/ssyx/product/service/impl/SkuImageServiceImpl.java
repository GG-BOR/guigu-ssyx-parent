package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.SkuImage;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.mapper.SkuImageMapper;
import com.atguigu.ssyx.product.service.SkuImageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {
    /**
     *  根据id查询商品图片列表
     * @param id
     * @return
     */
    @Override
    public List<SkuImage> getImageListBySkuId(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId,id);
        List<SkuImage> skuImages = baseMapper.selectList(wrapper);
        return skuImages;
    }

    /**
     * 判断sku图片的列表是否为空
     * @param skuInfo
     * @param skuImagesList
     */
    @Override
    public void saveImageList(SkuInfo skuInfo, List<SkuImage> skuImagesList) {
        //判断sku图片的列表是否为空
        if(!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage:skuImagesList) {
                //海报表中设置外键skuId
                skuImage.setSkuId(skuInfo.getId());
            }
        }
    }
}
