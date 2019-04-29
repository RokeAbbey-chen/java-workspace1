
package roke;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.*;
public class Test1{

    public static void main(String[] args) throws Exception{
        Test1 t = new Test1();
        t.print();
    }

    public void print() throws Exception{

        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Test1.class.getResourceAsStream("xxx.dat")));
                 PrintWriter p = new PrintWriter(System.out)
            ) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    p.println(line);
                }
            } catch (Exception e) {
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Test1.class.getResourceAsStream("/xxx.dat")));
                 PrintWriter p = new PrintWriter(System.out)
            ) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    p.println(line);
                }
            } catch (Exception e) {
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

