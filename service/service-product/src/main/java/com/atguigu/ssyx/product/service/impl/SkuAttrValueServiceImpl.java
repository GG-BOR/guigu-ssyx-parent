package com.atguigu.ssyx.product.service.impl;

import com.atguigu.ssyx.model.product.SkuAttrValue;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.product.SkuPoster;
import com.atguigu.ssyx.product.mapper.SkuAttrValueMapper;
import com.atguigu.ssyx.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {
    /**
     * 根据id查询平台属性信息列表
     * @param id
     * @return
     */
    @Override
    public List<SkuAttrValue> getAttrValueListBySkuId(Long id) {
        LambdaQueryWrapper<SkuAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId,id);
        List<SkuAttrValue> skuAttrValues = baseMapper.selectList(wrapper);
        return skuAttrValues;
    }

    /**
     * 判断sku平台属性的列表是否为空，并设置外键
     * @param skuInfo
     * @param skuAttrValueList
     */
    @Override
    public void saveAttrValueList(SkuInfo skuInfo, List<SkuAttrValue> skuAttrValueList) {
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for(SkuAttrValue skuAttrValue:skuAttrValueList){
                //设置商品SkuId
                skuAttrValue.setSkuId(skuInfo.getId());
            }
        }
    }
}
