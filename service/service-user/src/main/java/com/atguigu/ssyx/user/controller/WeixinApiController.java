package com.atguigu.ssyx.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.common.constant.RedisConst;
import com.atguigu.ssyx.common.exception.SsysException;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.common.utils.JwtHelper;
import com.atguigu.ssyx.enums.UserType;
import com.atguigu.ssyx.enums.user.User;
import com.atguigu.ssyx.user.mapper.UserDeliveryMapper;
import com.atguigu.ssyx.user.service.UserService;
import com.atguigu.ssyx.user.utils.ConstantPropertiesUtil;
import com.atguigu.ssyx.user.utils.HttpClientUtils;
import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import com.atguigu.ssyx.vo.user.UserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-16 14:23
 * @Description:
 **/
@Api("微信用户登录授权接口")
@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    //微信授权登录服务
    @ApiOperation(value = "微信登录获取openId(小程序)")
    @GetMapping("wxLogin/{code}")
    public Result loginWx(@PathVariable String code){
        //1 得到微信返回code临时票据值

        //2 得到code + 小程序id + 小程序密钥 请求微信接口服务
        //// 使用HttpClient工具请求
        //小程序id
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        //小程序密钥
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        //get请求
        //拼接请求地址+参数
        StringBuffer url = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String tokenUrl = String.format(url.toString(),
                                        wxOpenAppId,
                                        wxOpenAppSecret,
                                        code);
        //HttpClient发送get请求
        String result = null;
        try {
            result = HttpClientUtils.get(tokenUrl);
        } catch (Exception e) {
            throw new SsysException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //3 请求微信接口服务，返回两个值seesion_key 和openId(微信唯一标识)
        JSONObject jsonObject = JSONObject.parseObject(result);
        String seesionKey = jsonObject.getString("seesion_key");
        String openId = jsonObject.getString("openid");
        //4 添加微信用户信息到数据库里面
        //// 操作user表
        //// 判断是否是第一次使用微信授权登录：如何判断？openId
        User user = userService.getUserByOpenId(openId);
        // 如果没有查到用户信息,那么调用微信个人信息获取的接口
        if(null == user){
            user = new User();
            user.setOpenId(openId);
            user.setNickName(openId);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }
        //5 根据userId查询提货点和团长信息
        //// 提货点 user表  user_delivery表
        //// 团长  leader表
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressByUserId(user.getId());

        //6 使用JWT工具根据userId和userName生成token字符串
        String token = JwtHelper.createToken(user.getId(), user.getNickName());
        //7 获取当前登录用户信息
        UserLoginVo userLoginVo = userService.getUserLoginVo(user.getId());
        ////放到Redis里面，设置有效时间
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX+user.getId(),
                                        userLoginVo,
                                        RedisConst.USERKEY_TIMEOUT,
                                        TimeUnit.DAYS);
        //8 需要数据封装到map里面
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);                           //用户信息
        map.put("token",token);                         //userId和userName生成token字符串
        map.put("leaderAddressVo",leaderAddressVo);     //提货点和团长信息
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok();
    }
}
