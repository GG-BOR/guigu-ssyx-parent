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
//计算完成回调方法
 //带有Async代表着异步：指不在同一个线程中执行
public class CompletableFutureDemo4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //1 任务1 返回结果1024
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int value = 1024;
            System.out.println("任务1:" + value);
            return value;
        }, executorService);
        //2 任务2 获取任务1返回结果
        ////thenApply 方法：当一个线程依赖另一个线程时，获取上一个任务返回的结果，并返回当前任务的返回值
        CompletableFuture<Integer> futureB = futureA.thenApplyAsync((res) -> {
            System.out.println("任务2:" + res);
            return res;
        }, executorService);
        //3 任务3 往下执行
        ////thenRun只要上面任务完成就开始执行thenRun方法
        futureA.thenRunAsync(()->{
            System.out.println("任务3:");
        },executorService);

    }
}
