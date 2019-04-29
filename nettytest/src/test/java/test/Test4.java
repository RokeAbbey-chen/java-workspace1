package test;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;

public class Test4 {

    interface I {
        int i = 1, ii = out("ii", 2);
    }

    interface J extends I{
        int j = out("j", 3), jj = out("jj", 4);
        int jjj = 1 + 2 + 3;
    }

    interface K extends J{
        int k = out("k", 5);
    }

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchFieldException {

        System.out.println(J.i);
        System.out.println(K.jjj);
        System.out.println("-------------");
        System.out.println(K.j);
        System.out.println("-------------");
        System.out.println(Modifier.isFinal(K.class.getField("j").getModifiers()));


    }

    public static int out(String s, int i){
        System.out.println(s + "=" + i);
        return i;
    }

    @Test
    public void test() throws ClassNotFoundException {
        Class.forName("test.A", false, getClass().getClassLoader());
        System.out.println("test finish");
    }

}

class A {
    static {
        System.out.println("initialized !!");
    }
}
