package com.atguigu.ssyx.client.search;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-search")
public interface SkuFeignClient {

    //获取爆款商品
    @GetMapping("api/search/hotSku/inner/findHotSkuList")
    List<SkuEs> findHotSkuList();

    //更新商品热度
    @GetMapping("api/search/hotSku/inner/incrHotScore/{skuId}")
    Boolean incrHotScore(@PathVariable("skuId") Long skuId);
}
