package com.geccocrawler.socks5.test;

import java.util.concurrent.*;

public class Test {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                System.out.println("thread created");
                return new Thread(r);
            }
        });
        ScheduledFuture future = executorService.schedule(() -> System.out.println("hello"), 1, TimeUnit.SECONDS);
        try {
            System.out.println("get : " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
