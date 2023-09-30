package com.atguigu.ssyx.user.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-15 22:34
 * @Description:
 **/
@Component

public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;

    @Value("${wx.open.app_secret}")
    private String appSecret;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;

    //在配置类设置完后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID=appId;
        WX_OPEN_APP_SECRET=appSecret;
    }
}
