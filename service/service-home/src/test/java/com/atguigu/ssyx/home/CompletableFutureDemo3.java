package com.atguigu.ssyx.home;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-09-20 16:53
 * @Description:
 **/
//supplyAsync支持返回方法
//whenComplete是由执行当前的任务的线程来执行whenComplete任务
public class CompletableFutureDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        System.out.println("main begin ...");
        //CompletableFuture创建异步对象
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int value = 1024;
            System.out.println("value:" + value);
            return value;
        }, executorService).whenComplete((result,exception)->{
            System.out.println("whenComplete:"+result);
            System.out.println("exception:"+exception);
        });
        System.out.println("main over....");
    }
}
