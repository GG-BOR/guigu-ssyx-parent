package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuAttrValue;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SkuAttrValue> getAttrValueListBySkuId(Long id);

    void saveAttrValueList(SkuInfo skuInfo, List<SkuAttrValue> skuAttrValueList);
}
