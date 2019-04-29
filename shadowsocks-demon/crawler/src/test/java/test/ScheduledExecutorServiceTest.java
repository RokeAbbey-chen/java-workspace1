package test;

import org.junit.Test;

import java.util.concurrent.*;

public class ScheduledExecutorServiceTest {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new MyThread(r){
                @Override
                public void run(){
                    System.out.println("thread : " + Thread.currentThread().getName() + " 罗克制造 质量保证 安全放心");
                    System.out.println("thread.parent : " + parent.getName() + ", this : " + this);
                    super.run();
                    System.out.println("谢谢您的使用");
                }
            };
        }
    });

    private int time = 0;
    private boolean start = false;
    @Test
    public void test1(){
//        executor.schedule(() -> {
//            System.out.println("schedule : " + time ++);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 1, TimeUnit.SECONDS);


        ScheduledFuture future = executor.scheduleAtFixedRate(() ->{

            System.out.println("thread : " + Thread.currentThread().getName() + ", scheduleAtFixedRate : " + time ++);
            try {
//                new Exception().printStackTrace();
                while (!start) {
                    Thread.sleep(5 * 1000);
//                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);

//        executor.scheduleWithFixedDelay(() ->{
//            System.out.println("scheduleWithFixedDelay : " + time ++);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 0, 2, TimeUnit.SECONDS);
        try {
            Thread.sleep(10*1000);
            System.out.println("cancel future!!");
            future.cancel(true);
            Thread.sleep(10000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static class MyThread extends Thread{

        Thread parent = null;

        public MyThread(){
            this(null);
        }

        public MyThread(Runnable r){
            this(r, Thread.currentThread());
        }

        public MyThread(Thread parent){
            this(null, parent);
        }

        public MyThread(Runnable r, Thread parent){
            super(r);
            this.parent = parent;
        }
    }
}
