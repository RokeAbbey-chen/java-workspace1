package test;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Test5 {

    @Test
    public void test1(){
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        List<String> ss = Arrays.asList("紫编10.25*10.25", "11.33300", "3.00000", "33.99900", "2018-07-11");
        List<String> s2 = Arrays.asList("红编11.5*11.5", "11.00000", "3.00000", "33.00000","2018-07-11");

        for (String s : ss){
//            writer.write(String.format("%-30s", s), );
        }
        System.out.println();
        for (String s : s2){
            System.out.print(String.format("%-30s", s));
        }
        System.out.println();

    }
}
