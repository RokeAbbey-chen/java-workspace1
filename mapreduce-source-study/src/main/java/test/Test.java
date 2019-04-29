package test;

import java.security.AccessController;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static int value = 1;
    public static void main(String[] args){
        String[] strs = {"a", "b", "c"};
        List<String> l = Arrays.asList(strs);
        System.out.println(l);
        List<Integer> l2 = Arrays.asList(new Integer(1), 2, 3);
        System.out.println(l2);

//        AccessController.doPrivileged(null);
    }
}
