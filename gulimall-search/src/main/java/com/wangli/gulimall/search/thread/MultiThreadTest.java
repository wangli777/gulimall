package com.wangli.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @author WangLi
 * @date 2021/7/13 20:17
 * @description
 */
public class MultiThreadTest {
    public static final ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main......start.....");

//        CompletableFuture.runAsync(() -> System.out.println("任务1开始..."), service);
//
        /**
         * 异步方法完成后的处理 一、whenComplete和exceptionally
         * whenComplete 可以处理正常和异常的计算结果
         * exceptionally 处理异常情况。
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            int i = 10 / 0;
//            System.out.println("任务2执行结果：" + i);
//            return i;
//        }, service).whenComplete((res, err) -> {
//            //虽然能得到异常信息，但是没法修改返回数据
//            System.out.println("任务2完成了，结果是：" + res + "异常是：" + err);
//        }).exceptionally(err -> {
//            //可以感知异常，同时返回默认值
//            System.out.println("任务2运行出错,异常是：" + err);
//            return 10;
//        });
//        System.out.println(future.get());


        /**
         * 异步方法完成后的处理 二、handle
         * 可对结果做最后的处理（可处理异常），可改变返回值。
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务3执行结果：" + i);
//            return i;
//        }, service).handle((res, err) -> {
//            if (res != null) {
//                //异步任务执行成功，返回结果*2
//                return res * 2;
//            }
//            if (err != null) {
//                //异步任务执行出错，返回结果0，可以拿到异常
//                System.out.println("任务3完成了，结果是：" + res + "异常是：" + err);
//                return 0;
//            }
//            return 0;
//        });
//        System.out.println(future.get());

        /**
         * 线程串行化
         * 获取到上一个任务的执行结果，再同步/异步执行操作
         * thenApply 方法：当一个线程依赖另一个线程时，获取上一个任务返回的结果，并返回当前任务的返回值。
         * thenAccept 方法：消费处理结果。接收任务的处理结果，并消费处理，无返回结果。
         * thenRun 方法：只要上面的任务执行完成，就开始执行 thenRun，只是处理完任务后，执行thenRun 的后续操作
         * 带有 Async 默认是异步执行的。
         *
         */
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务1执行结果：" + i);
//            return i;
//        }, service).thenApplyAsync(res -> {
//            //获取到上一个任务的执行结果，再异步执行操作
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            System.out.println("任务2运行中..." + res);
//            return "Hello:" + res;
//        });

        /**
         * 两任务组合-都要完成
         * 两个任务必须都完成，触发该任务。
         * thenCombine：组合两个 future，获取两个 future 的返回结果，并返回当前任务的返回值
         * thenAcceptBoth：组合两个 future，获取两个 future 任务的返回结果，然后处理任务，没有
         * 返回值。
         * runAfterBoth：组合两个 future，不需要获取 future 的结果，只需两个 future 处理完任务后，
         * 处理该任务。
         */
//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务1执行结果：" + i);
//            return i;
//        }, service);
//
//        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//            System.out.println("任务2执行结束");
//            return "hello";
//        }, service);

        //future01和future02两个异步任务都执行完，再执行
        //1、runAfterBothAsync 无入参、无返回值
//        future01.runAfterBothAsync(future02, () -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            System.out.println("任务3执行结束...");
//        }, service);

        //2、thenAcceptBothAsync 有入参、无返回值
//        future01.thenAcceptBothAsync(future02, (r1, r2) -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            String r3 = r1 + r2;
//            System.out.println("任务3执行结束..." + r3);
//        }, service);

        //3、thenCombineAsync 有入参、有返回值
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (r1, r2) -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            String r3 = r1 + r2;
//            System.out.println("任务3执行结束..." + r3);
//            return r3;
//        }, service);


        /**
         * 两任务组合-其中一个完成
         * 当两个任务中，任意一个 future 任务完成的时候，执行任务。
         * applyToEither：两个任务有一个执行完成，获取它的返回值，处理任务并有新的返回值。
         * acceptEither：两个任务有一个执行完成，获取它的返回值，处理任务，没有新的返回值。
         * runAfterEither：两个任务有一个执行完成，不需要获取 future 的结果，处理任务，也没有返
         * 回值
         */
//        CompletableFuture<String > future01 = CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            System.out.println("任务1执行结束");
//            return "future01";
//        }, service);
//
//        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//            System.out.println("任务2执行结束");
//            return "future02";
//        }, service);
        //1、applyToEitherAsync 有入参（任意一个异步任务执行完成的结果），有返回值
//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            System.out.println("任务3执行结束..." + res);
//            return "Hello" + res;
//        }, service);

        //2、acceptEitherAsync 有入参，无返回值
//        CompletableFuture<Void> future = future01.acceptEitherAsync(future02, res -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            System.out.println("任务3执行结束..." + res);
//        }, service);

        //2、acceptEitherAsync 无入参，无返回值
//        CompletableFuture<Void> future = future01.runAfterEitherAsync(future02, () -> {
//            System.out.println("任务3线程：" + Thread.currentThread().getId());
//            System.out.println("任务3执行结束..." );
//        }, service);


        /**
         * 多任务组合
         * allOf：等待所有任务完成
         * anyOf：只要有一个任务完成
         */

        CompletableFuture<String> picSearchFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品图片信息，当前线程：" + Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "goods.jpg";
        }, service);

        CompletableFuture<String> attrSearchFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品属性，当前线程：" + Thread.currentThread().getId());

            return "8+256";
        }, service);

        CompletableFuture<String> descSearchFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品介绍，当前线程：" + Thread.currentThread().getId());
            return "小米11";
        }, service);

//        CompletableFuture<Void> future = CompletableFuture.allOf(picSearchFuture, attrSearchFuture, descSearchFuture);
//        //阻塞等待所有异步任务完成
//        future.get();

        CompletableFuture<Object> future = CompletableFuture.anyOf(picSearchFuture, attrSearchFuture, descSearchFuture);
        //阻塞等待任意一个异步任务完成
        future.get();

//        System.out.println("main......end.....\n=="+picSearchFuture.get()+"\n=="+attrSearchFuture.get()+"\n=="+descSearchFuture.get());
        System.out.println("main......end.....\n==" + future.get());

    }

    private static void threadPool() {
        //手动创建线程池
//        核心线程数、
//        最大线程数、
//        超过核心线程数的线程空闲存活时间、
//        时间单位、
//        任务阻塞队列、
//        线程工厂、
//        超过任务队列后的任务拒绝执行策略
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                20,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        Executors.newCachedThreadPool();//可缓存的线程池，核心线程数为0，说明所有线程都是可以释放的。无任务60s后释放 可创建的最大线程数为最大整数值，可能会导致oom
        Executors.newFixedThreadPool(10);//核心线程数和最大线程数固定大小。可接收的任务最大为最大的整数值，可能会导致oom
        Executors.newSingleThreadExecutor();//核心线程数和最大线程数固定为1。可接收的任务最大为最大的整数值，可能会导致oom
        Executors.newScheduledThreadPool(10);


    }

}
