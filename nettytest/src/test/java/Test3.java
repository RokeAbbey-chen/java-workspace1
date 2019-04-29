import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test3 {

    @Test
    public void test3() throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        service.scheduleAtFixedRate(new Runnable() {
            volatile int i = 0;
            volatile int j = 0;
            @Override
            public void run() {
                System.out.println("run!!!");

                for (i = 0; i < 1000 * 1000; i ++){
                    for ( j = 0; j < 1000 * 1000; j ++);
                }
                System.out.println("finish!!!");

//                try {
////                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void test2() throws UnsupportedEncodingException {
        byte[] bs = "1a你".getBytes();
        for (byte b : bs){
            System.out.print(b + " ");
        }

        System.out.println();
        bs = "1a你".getBytes("UTF-8");
        for (byte b : bs){
            System.out.print(b + " ");
        }
        System.out.println();
        System.out.println(new String(bs));


    }


    @Test
    public void test4(){

        int m = 10, n = 15;
        Object[][] ord = new Object[m][n];
        Object[][] out = new Object[n][m];
        int k = 0;
        for (int i = 0; i < m; i ++){
            for (int j = 0; j < n; j ++){
                ord[i][j] = k ++;
                out[j][m - i - 1] = ord[i][j];
            }
        }
        for (int i = 0; i < n; i ++){
            for (int j = 0; j < m; j ++){
                System.out.print(out[i][j] + " ");
            }
            System.out.println();
        }


    }


    @Test
    public void test5(){
        int m = 10, n = 15;
        Object[][] ord = new Object[m][n];
        Object[][] out = new Object[m][n];
        int k = 0;
        for (int i = 0; i < m; i ++){
            for (int j = 0; j < n; j ++){
                ord[i][j] = k ++;
                out[i][n - j - 1] = ord[i][j];
            }
        }
        for (int i = 0; i < m; i ++){
            for (int j = 0; j < n; j ++){
                System.out.print(out[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        System.out.println(Type.ONE.getId());
        try {
//            Constructor<Type>[] cons = (Constructor<Type>[]) Type.class.getDeclaredConstructors();
//            for (Constructor<Type> con : cons) {
//                con.setAccessible(true);
//                Class[] ps = con.getParameterTypes();
//                for (Class p : ps) {
//                    System.out.println(p.getName() + "  " + p);
//                }
//
//                System.out.println("--------------------");
//            }
//
//            System.out.println(Type.class.getSuperclass().getCanonicalName());
            Type e = Type.ONE;
            Field f = e.getClass().getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, 10);
            System.out.println(e.getId());
            Constructor<Type> t = Type.class.getDeclaredConstructor(String.class, int.class, int.class);
            t.setAccessible(true);
            Type temp = t.newInstance("TWO", 0, 9);
            System.out.println(temp.getId());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static class T extends ArrayList<Integer> implements Cloneable{
        private int id;
        private T(int i ){
            this.id = i;
        }

        private T(Integer i){
            this.id = i;
        }

        public int getId() {
            return id;
        }

        public Object clone(){
            return super.clone();
        }
    }

    public enum Type implements Cloneable{
        ONE(1);

        private int id;
        Type(int i){
            this.id = i;
        }

        public int getId() {
            return id;
        }

    }
}
