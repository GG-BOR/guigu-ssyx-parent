package com.atguigu.ssyx.search.api;

import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-18 15:12
 * @Description:
 **/
@RestController
@RequestMapping("api/search/hotSku")
public class HotSkuApiController {

    @Autowired
    private SkuService skuService;
    //获取爆款商品
    @GetMapping("inner/findHotSkuList")
    public List<SkuEs> findHotSkuList(){
        return skuService.findHotSkuList();
    }

    //更新商品热度
    @GetMapping("inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId){
        skuService.incrHotScore(skuId);
        return true;
    }

}
