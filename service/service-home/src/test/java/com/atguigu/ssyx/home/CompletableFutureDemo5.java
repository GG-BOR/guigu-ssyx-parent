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
//组合
public class CompletableFutureDemo5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //1 任务1 返回结果1024
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int value = 1024;
            System.out.println(Thread.currentThread().getName()+":start..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("任务1:" + value);
            System.out.println(Thread.currentThread().getName()+":end..");
            return value;
        }, executorService);
        //2 任务2 返回结果200
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            int value = 200;
            System.out.println(Thread.currentThread().getName() + ":start..");
            System.out.println("任务1:" + value);
            System.out.println(Thread.currentThread().getName() + ":end..");
            return value;
        }, executorService);
        // allOf：等待所有任务完成
        CompletableFuture<Void> all = CompletableFuture.allOf(futureA, futureB);
        all.get();
        System.out.println("over...");


    }
}
