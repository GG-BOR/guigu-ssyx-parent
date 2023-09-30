package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuImage;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> getImageListBySkuId(Long id);

    void saveImageList(SkuInfo skuInfo, List<SkuImage> skuImagesList);
}
