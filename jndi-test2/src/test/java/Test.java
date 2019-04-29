import sun.misc.Launcher;

import javax.net.ServerSocketFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.SortedMap;

public class Test {
    public static void main(String[] args) {
//        SortedMap s = null;
//        s.tailMap()
// 6, 1, 3, 2, 4, 7
//        Properties p = null;
//        p.put
//        Test.class.getClassLoader().getResource()

//        try {
//            URL u = new URL("file:///C:/Users/Administrator/Desktop/111.html");
//            System.out.println(new File(u.getFile()).getAbsolutePath());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

//        URL[] urls = ((URLClassLoader)Test.class.getClassLoader()).getURLs();
//        for (URL url : urls){
//            System.out.println(url.toString());
//        }
//        try {
//
//            URL u = new URL("file:/F:/");
//            URLClassLoader cl = (URLClassLoader) Test.class.getClassLoader();
//            for (URL url : cl.getURLs()){
//                System.out.println(url.toString());
//            }
//
//            Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//            m.setAccessible(true);
//            m.invoke(cl, u);
//            System.out.println("----------------------------");
//            for (URL url : cl.getURLs()){
//                System.out.println(url.toString());
//            }
//
//            Class c = cl.loadClass("HelloWorld");
//            System.out.println(c.newInstance());
//            System.out.println(cl);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:a");
            System.out.println(dateFormat.format(new Date()));
            ServerSocket ss = new ServerSocket(8081);
            System.out.println(ss.getLocalPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
