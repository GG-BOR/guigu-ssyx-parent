package com.atguigu.ssyx.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-24 17:29
 * @Description:
 * MybatisPlus配置类
 **/

@EnableTransactionManagement
@Configuration
@MapperScan("com.atguigu.ssyx.*.mapper")
public class MybatisPlusConfig {
    /**
     * mp插件
     */
    @Bean
    public MybatisPlusInterceptor optimisticLockerInnerInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //向Mybatis过滤器链中添加分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
