package Test;

import java.util.concurrent.LinkedBlockingQueue;

public class Test3 {

    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<>(10);
        q.poll();

        for (int i = 0; i < 20; i ++){
            q.offer(i);
        }

        System.out.println(q.size());
        System.out.println(q);

    }
}
