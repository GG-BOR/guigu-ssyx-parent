package com.atguigu.ssyx.home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-21 14:46
 * @Description:自定义线程池
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,                       //核心线程数
                5,                                  //最大线程数
                2,                                  //当线程数大于核心数时，这是多余的空闲线程在终止前等待新任务的最长时间
                TimeUnit.SECONDS,                   //时间单位
                new ArrayBlockingQueue<>(3),//在执行任务之前用于保存任务的队列
                new ThreadPoolExecutor.AbortPolicy()//拒绝策略
                // ThreadPoolExecutor自带的拒绝策略如下：
                //   1. AbortPolicy(默认)：直接抛出RejectedExecutionException异常阻止系统正常运行
                //   2. CallerRunsPolicy：“调用者运行”一种调节机制，该策略既不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
                //   3. DiscardOldestPolicy：抛弃队列中等待最久的任务，然后把当前任务加人队列中 尝试再次提交当前任务。
                //   4. DiscardPolicy：该策略默默地丢弃无法处理的任务，不予任何处理也不抛出异常。 如果允许任务丢失，这是最好的一种策略。

        );
        return threadPoolExecutor;
    }

}
