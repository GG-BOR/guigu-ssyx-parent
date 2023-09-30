package com.atguigu.ssyx.search.service.impl;

import com.atguigu.ssyx.client.activity.ActivityFeignClient;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.enums.SkuType;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.search.repository.SkuRepository;
import com.atguigu.ssyx.search.service.SkuService;

import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-09 21:09
 * @Description:
 **/
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 上架
     * @param skuId
     */
    @Override
    public void upperSku(Long skuId) {
        //1 通过远程调用，根据skuid获取相关信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //skuInfo为空时就直接返回
        if(skuInfo==null){
            return;
        }
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        //2 获取数据封装SkuEs对象
        SkuEs skuEs = new SkuEs();
        //封装分类
        if(category!=null){
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        //封装sku信息
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        }
        skuRepository.save(skuEs);
    }

    /**
     * 下架
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        //skuRepository继承ElasticsearchRepository直接掉用其父类的方法
        skuRepository.deleteById(skuId);
    }

    /**
     * 获取爆款商品
     * @return
     */
    @Override
    public List<SkuEs> findHotSkuList() {
        //find read get开头
        //关联条件关键字
        //0 代表第一页
        Pageable pageable = PageRequest.of(0, 10);
        Page<SkuEs> pageModel = skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    /**
     * 查询分类商品
     * @param pageable
     * @param skuEsQueryVo
     * @return
     */
    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        //1 向SkuEsQueryVo设置wareId，当前登录用户的仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());

        Page<SkuEs> pageModel = null;
        //2 调用SkuRepository方法，根据springData命名规则定义方法，进行条件查询
        //// 判断keyword是否为空，如果为空，根据仓库id+分类id查询
        String keyword = skuEsQueryVo.getKeyword();
        if(StringUtils.isEmpty(keyword)){
            pageModel = skuRepository.findByCategoryIdAndWareId(skuEsQueryVo.getWareId(),skuEsQueryVo.getCategoryId(),
                    pageable);
        }else{
            //// 如果keyword不为空根据仓库id+keyword进行查询
            pageModel = skuRepository.findByKeywordAndWareId(keyword,skuEsQueryVo.getWareId(),pageable);
        }
        //3 查询商品参加优惠活动
        List<SkuEs> skuEsList = pageModel.getContent();
        //得到所有skuId列表
        List<Long> skuIdList = skuEsList.stream().map(item -> item.getId()).collect(Collectors.toList());
        //根据skuId列表远程调用，调用service-activity里面的接口得到数据
        //返回Map<Long,List<String>>
        ////map集合key就是skuId值，Long类型
        ////map集合value是List集合，sku参与活动里面多个规则名称列表
        ////一个商品参加一个活动，一个活动里面可以有多个规则
        Map<Long,List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);
        //封装获取数据到skuEs里面 ruleList属性里面
        if(skuIdToRuleListMap!=null){
            skuEsList.forEach(skuEs -> {
                skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
            });

        }
        return pageModel;
    }

    /**
     * 更新商品热度
     * @param skuId
     */
    @Override
    public void incrHotScore(Long skuId) {
        String key = "hotScore";
        //redis保存数据，每次+1
        Double hotScore = redisTemplate.opsForZSet().incrementScore(key, "skuId:" + skuId, 1);
        //每十次向es中更新数据
        if(hotScore%10==0){
            Optional<SkuEs> optional = skuRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            //向es中更新数据
            skuRepository.save(skuEs);
        }

    }
}
