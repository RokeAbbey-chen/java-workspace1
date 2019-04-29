package test;

import java.util.ServiceLoader;

public class ServiceLoaderTest {
    public static void main(String[] args) {
        ServiceLoader<IService> sl = ServiceLoader.load(IService.class);
        for(IService s : sl){
            System.out.println(s.getSchema() + ":" + s.sayHello());
        }
        String cls = System.getProperty("java.system.class.loader");
        System.out.println(cls);

//        System.out.println((char)('\uD800' - 0));
//        System.out.println((int)('?'));
    }
}
