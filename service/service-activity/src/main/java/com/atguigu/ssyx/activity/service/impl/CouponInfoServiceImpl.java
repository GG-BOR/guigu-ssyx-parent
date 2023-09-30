package com.atguigu.ssyx.activity.service.impl;


import com.atguigu.ssyx.activity.mapper.CouponInfoMapper;
import com.atguigu.ssyx.activity.mapper.CouponRangeMapper;
import com.atguigu.ssyx.activity.mapper.CouponUseMapper;
import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.enums.CouponRangeType;
import com.atguigu.ssyx.enums.CouponStatus;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.activity.CouponRange;
import com.atguigu.ssyx.model.activity.CouponUse;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-09-11
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponRangeMapper couponRangeMapper;

    @Autowired
    private CouponUseMapper couponUseMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 优惠券分页查询
     * @param pageParam
     * @return
     */
    @Override
    public IPage<CouponInfo> getPageList(Page<CouponInfo> pageParam) {
        IPage<CouponInfo> couponInfoPage = baseMapper.selectPage(pageParam, null);
        //从page中获取列表信息
        List<CouponInfo> couponInfoList = couponInfoPage.getRecords();
        couponInfoList.stream().forEach(item->{
            //设置优惠券类型
            item.setCouponTypeString(item.getCouponType().getComment());
            CouponRangeType rangeType = item.getRangeType();
            if(rangeType!=null){
                item.setRangeTypeString(rangeType.getComment());
            }
        });

        return couponInfoPage;
    }

    /**
     * 根据id查询优惠券
     * @param id
     * @return
     */
    @Override
    public CouponInfo getCouponInfo(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if(couponInfo.getRangeType() != null){
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    /**
     * 查询优惠券规则数据
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        //第一步 根据优惠券id查询优惠券基本信息 coupon_info表
        CouponInfo couponInfo = baseMapper.selectById(id);
        //第二步 根据优惠券id查询coupon_range 查询里面对应range_id
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(
                new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id));
        //couponRangeList获取所有rang_id
        //// 如果规则类型 SKU rang_id就是skuId值
        //// 如果规则类型 CATEGORY rang_id就是分类Id值
        List<Long> rangIdList =
                couponRangeList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());

        Map<String,Object> result = new HashMap<>();
        //第三步 分别判断封装不同数据
        if(!CollectionUtils.isEmpty(rangIdList)){
            if(couponInfo.getRangeType()==CouponRangeType.SKU){
                //// 如果规则类型师SKU，得到skuId，
                // 远程调用根据多个skuId值获取对应sku信息
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangIdList);
                result.put("skuInfoList",skuInfoList);
            }else if(couponInfo.getRangeType()==CouponRangeType.CATEGORY){
                //// 如果规则类型是分类，得到分类Id，
                // 远程调用根据多个分类Id值获取对应分类信息
                List<Category> categoryList = productFeignClient.findCategoryList(rangIdList);
                result.put("categoryList",categoryList);
            }
        }
        return result;
    }

    /**
     * 添加优惠券规则
     * @param couponRuleVo
     */
    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        //根据优惠券id删除规则数据
        couponRangeMapper.delete(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId,
                couponRuleVo.getCouponId()));
        //更新优惠券基本信息
        CouponInfo couponInfo = baseMapper.selectById(couponRuleVo.getCouponId());

        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        baseMapper.updateById(couponInfo);
        //添加优惠券新规则数据
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            couponRange.setCouponId(couponRuleVo.getCouponId());
            //添加
            couponRangeMapper.insert(couponRange);
        }
    }

    /**
     * 根据skuId+userId查询优惠券信息
     * @param skuId
     * @param userId
     * @return
     */
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {

        //远程调用：根据skuId获取skuInfo
        SkuInfoVo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(null == skuInfo) return new ArrayList<>();
        //根据条件查询：skuId+分类id+userId
        List<CouponInfo> couponInfoList = baseMapper.selectCouponInfoList(skuInfo.getId(),skuInfo.getCategoryId(),
                userId);
        return couponInfoList;
    }

    /**
     * 获取购物车可以使用优惠券
     * @param cartInfoList
     * @param userId
     * @return
     */
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        //1 根据userId获取用户全部优惠券
        //coupon_use coupon_info
        List<CouponInfo> userAllCouponInfoList = baseMapper.selectCartCouponInfoList(userId);
        if(CollectionUtils.isEmpty(userAllCouponInfoList)){
            return new ArrayList<CouponInfo>();
        }
        //2 从第一步返回list集合中，获取所有优惠券id列表
        List<Long> couponIdList = userAllCouponInfoList.stream()
                .map(couponInfo -> couponInfo.getId()).collect(Collectors.toList());
        //3 查询优惠券对应的范围 coupon_range
        //couponRangeList
        LambdaQueryWrapper<CouponRange> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CouponRange::getCouponId,couponIdList);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(wrapper);

        //4 获取优惠券id 对应skuId列表
        //优惠券id进行分组，得到map集合
        //   Map<Long,List<Long>>
        Map<Long,List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList,couponRangeList);

        //5 遍历全部优惠券集合， 判断优惠券类型
        // 全场通用 sku和分类
        BigDecimal reduceAmount = new BigDecimal(0);
        CouponInfo optimalCouponInfo = null;
        for (CouponInfo couponInfo : userAllCouponInfoList) {
            //全场通用
            if(CouponRangeType.ALL == couponInfo.getRangeType()){
                //判断是否满足优惠使用门槛
                //计算购物车商品的价格
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }else{
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
                //满足使用范围购物项
                List<CartInfo> currentCartInfoList = cartInfoList.stream()
                        .filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId()))
                        .collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect().intValue() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        //6 返回List<CouponInfo>
        if(null!= optimalCouponInfo){
            optimalCouponInfo.setIsOptimal(1);
        }
        return userAllCouponInfoList;
    }

    /**
     * 获取购物车对应优惠券
     * @param cartInfoList
     * @param couponId
     * @return
     */
    @Override
    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId) {
        //根据优惠券id查询基本信息
        CouponInfo couponInfo = baseMapper.selectById(couponId);
        if(couponInfo==null)return null;
        //根据couponId查询对应CouponRange数据
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(
                new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, couponId));
        //对应sku信息
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList,couponRangeList);
        //遍历map，得到value值，封装到couponInfo对象
        List<Long> skuIdList = couponIdToSkuIdMap.entrySet().iterator().next().getValue();
        couponInfo.setSkuIdList(skuIdList);
        return couponInfo;
    }

    /**
     * 更新优惠券使用状态
     * @param couponId
     * @param userId
     * @param orderId
     */
    @Override
    public void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId) {
        //根据couponId查询优惠券信息
        CouponUse couponUse = couponUseMapper.selectOne(
                new LambdaQueryWrapper<CouponUse>()
                        .eq(CouponUse::getCouponId, couponId)
                        .eq(CouponUse::getUserId, userId)
                        .eq(CouponUse::getOrderId, orderId));
        //设置修改值
        couponUse.setCouponStatus(CouponStatus.USED);//优惠券已使用
        //调用方法修改
        couponUseMapper.updateById(couponUse);

    }

    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    //4 获取优惠券id 对应skuId列表
    //优惠券id进行分组，得到map集合
    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList) {
        Map<Long,List<Long>> couponIdToSkuIdMap = new HashMap<>();
        //couponRangeList数据处理，根据优惠券id分组
        Map<Long, List<CouponRange>> couponRangeToRangeListMap =
                couponRangeList.stream().collect(Collectors.groupingBy(couponRange -> couponRange.getRangeId()));
        //遍历map集合
        Iterator<Map.Entry<Long, List<CouponRange>>> iterator = couponRangeToRangeListMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Long, List<CouponRange>> entry = iterator.next();
            Long couponId = entry.getKey();
            List<CouponRange> rangeList = entry.getValue();

            //创建集合 set
            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : rangeList) {
                    //判断
                    if(couponRange.getRangeType() == CouponRangeType.SKU
                            && couponRange.getRangeId().longValue()==cartInfo.getSkuId()){
                            skuIdSet.add(cartInfo.getSkuId());
                    } else if (couponRange.getRangeType()== CouponRangeType.CATEGORY
                            && couponRange.getRangeId().longValue()==cartInfo.getCategoryId()) {
                            skuIdSet.add(cartInfo.getSkuId());
                    }
                }
            }
            couponIdToSkuIdMap.put(couponId,new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;
    }


}
