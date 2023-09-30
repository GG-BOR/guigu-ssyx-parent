package com.atguigu.ssyx.cart.controller;

import com.atguigu.ssyx.cart.service.CartInfoService;
import com.atguigu.ssyx.client.activity.ActivityFeignClient;
import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-22 17:10
 * @Description:
 **/
@Api("购物车")
@RestController
@RequestMapping("api/cart")
public class CartApiController {

    @Autowired
    private CartInfoService cartInfoService;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    //添加商品到购物车
    //添加内容：当前登录用户id:skuId，商品数量:skuNum
    @GetMapping("addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("skuNum") Integer skuNum){
        Long userId = AuthContextHolder.getUserId();
        //redis中的hash类型：key:userId->key:skuId,value:skuNum Map<String,Map<String,Object>>
        cartInfoService.addToCart(userId,skuId,skuNum);
        return Result.ok();
    }

    //根据skuId删除购物车
    @DeleteMapping("deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(skuId,userId);
        return Result.ok();
    }

    //清空购物车
    @DeleteMapping("deleteAllCart")
    public Result deleteAllCart(){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok();
    }

    //批量删除购物车 多个skuId
    @DeleteMapping("batchDeleteCart")
    public Result batchDeleteCart(@RequestBody List<Long> skuIdList){
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchDeleteCart(userId,skuIdList);
        return Result.ok();
    }

    //购物车列表
    @GetMapping("cartList")
    public Result cartList(){
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        return Result.ok(cartInfoList);
    }

    //查询带优惠卷的购物车
    @GetMapping("activityCartList")
    public Result activityCartList() {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        OrderConfirmVo orderTradeVo = activityFeignClient.findCartActivityAndCoupon(cartInfoList, userId);
        return Result.ok(orderTradeVo);
    }

    //1 根据skuId选中
    @GetMapping("checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable Long skuId,
                            @PathVariable Integer isChecked){
        //获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.checkCart(userId,skuId,isChecked);
        return Result.ok();
    }

    //2 全选
    @GetMapping("checkAllCart/{isChecked}")
    public Result checkAllCart(@PathVariable(value = "isChecked") Integer isChecked) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        // 调用更新方法
        cartInfoService.checkAllCart(userId, isChecked);
        return Result.ok();
    }

    //3 批量选中
    @PostMapping("batchCheckCart/{isChecked}")
    public Result batchCheckCart(@RequestBody List<Long> skuIdList
                                ,@PathVariable(value = "isChecked") Integer isChecked){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchCheckCart(skuIdList, userId, isChecked);
        return Result.ok();
    }

    //获取当前用户购物车选中购物项
    /**
     * 根据用户Id 查询购物车列表
     */
    @GetMapping("inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId){
        return cartInfoService.getCartCheckedList(userId);
    }
}
