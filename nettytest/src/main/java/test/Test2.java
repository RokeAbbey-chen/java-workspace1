package test;


import java.io.UnsupportedEncodingException;

public class Test2 {

    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] bs = "狗".getBytes();
        System.out.println(bs.length);
        StringBuilder sb = new StringBuilder();
        System.out.println(bs[0] + " " + bs[1] + " " + bs[2]);
        sb.append(new String(new byte[]{bs[0]}, "ISO_8859_1"));
        sb.append(new String(new byte[]{bs[1]}, "ISO_8859_1"));
        sb.append(new String(new byte[]{bs[2]}, "ISO_8859_1"));
        System.out.println(sb.toString());
        System.out.println(sb.toString().getBytes().length);
    }
}
