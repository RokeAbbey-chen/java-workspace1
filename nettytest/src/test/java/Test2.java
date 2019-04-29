import org.junit.Test;
import org.junit.internal.runners.statements.RunAfters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test2 {

    @Test
    public void test() throws InterruptedException {
        Object lock = new Object();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        synchronized (lock) {
            lock.notify();
            lock.notify();
            lock.notify();
        }

        for (int i = 0;  i < 5; i ++){
            executorService.execute(new Consummer(lock));
        }
        TimeUnit.SECONDS.sleep(1);
        synchronized (lock) {
            lock.notifyAll();
        }
//        for (int i = 0;  i < 1; i ++){
//            executorService.execute(new Productor(lock));
//        }
//        executorService.shutdown();
        TimeUnit.SECONDS.sleep(50);

    }

    //缓冲区为1的生产者消费者
    public static class Consummer implements Runnable{
        private static AtomicInteger num = new AtomicInteger();
        private Object lock;
        private int id;

        public Consummer(Object lock) {
            this.lock = lock;
            id = num.incrementAndGet();
        }

        public void consume() throws InterruptedException {
            synchronized (lock){
                for (int i = 0; i < 3; i ++) {
                    System.out.println("consume-id : " + id + ", wait");
                    lock.wait();
                    System.out.println("consume-id : " + id);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }

        @Override
        public void run() {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Productor implements Runnable{
        private static AtomicInteger num = new AtomicInteger();
        private Object lock;
        private int id;

        public Productor(Object lock) {
            this.lock = lock;
            id = num.incrementAndGet();
        }

        public void product() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
            for (int i = 0; i < 3; i ++){
                synchronized (lock){
                    System.out.println("product-id : " + id);
                    lock.notify();
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }

        @Override
        public void run() {
            try {
                product();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
