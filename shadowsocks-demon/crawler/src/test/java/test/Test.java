package test;

import bean.Crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    @org.junit.Test
    public void test1(){
        String url = "美女";
        String result = null;
        try {
            result = URLEncoder.encode(url, "gb2312");
            System.out.println("result : " + result);
            result = URLEncoder.encode(url, "utf8");
            System.out.println("result : " + result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test2(){
        Pattern p = Pattern.compile("a");
        String s = "aaa";
        Matcher m = p.matcher(s);
        int i = 0;
        while (m.find()){
            System.out.println(++i);
        }

    }

    @org.junit.Test
    public void test3(){
        Crawler c = new Crawler();
        try {
            c.crawl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @org.junit.Test
    public void test4(){
        int i = 0 << 29;
        System.out.println(i);
    }


    @org.junit.Test
    public void test5(){
        System.out.println(returnTest());
    }

    public int returnTest(){
        try{
            return 1;
        } finally {
            return 2;
        }
    }

    @org.junit.Test
    public void test6() throws IOException {
        String s = "aaaa\nbbbb\ncccc";
        BufferedReader reader = new BufferedReader(new StringReader(s));
        String line;
        while((line = reader.readLine()) != null){
            System.out.print(line);
        }
    }

    @org.junit.Test
    public void test7() throws NoSuchFieldException {
        Field f = EN.class.getDeclaredField("A");
        System.out.println(f.getName() + f.isEnumConstant() + Modifier.isStatic(f.getModifiers()));

    }

    private static enum EN {
        A(){
            public void a(){
                System.out.println("a");
            }
        }, B(){}

    }

    @org.junit.Test
    public void test8(){
        System.out.println(getEServerCode());
    }

    private List<Integer> getEServerCode(){
        List<Integer> result = new ArrayList<>();
        Field[] fields = EServer.class.getFields();
        try {
            Method getCode = EServer.class.getMethod("getCode");
            for (Field f : fields){
                f.setAccessible(true);
                if (f.isEnumConstant()) {
                    result.add((Integer) getCode.invoke(f.get(EServer.class)));
                }
            }
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}
