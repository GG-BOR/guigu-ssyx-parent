package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.product.SkuStockLockVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-02
 */
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    void saveSkuInfo(SkuInfoVo skuInfoVo);

    SkuInfoVo getSkuInfo(Long id);

    void updateSkuInfo(SkuInfoVo skuInfoVo);

    void check(Long id, Integer status);

    void publish(Long id, Integer status);

    void isNewPerson(Long id, Integer status);

    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    List<SkuInfo> findNewPersonSkuInfoList();

    //根据skuId获取sku信息
    SkuInfoVo getSkuInfoVo(Long skuId);

    //验证和锁定库存
    Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo);
    //扣减库存成功，更新订单状态
    void minusStock(String orderNo);
}
