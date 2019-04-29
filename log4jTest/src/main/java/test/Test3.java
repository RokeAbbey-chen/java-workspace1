package test;

public class Test3 {
    public static void main(String[] args) {
        char[] cs = {'\uD802', '\uDC12'};
        String str = new String(cs);
        System.out.println(str + " " + str.length());
    }
}
