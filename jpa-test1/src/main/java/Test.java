package jpa_test.beans;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("123", 123);
        map.put(new String("123"), 123);
        System.out.println(map.size());

    }
}
