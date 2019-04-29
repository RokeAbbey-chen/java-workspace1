import org.junit.Test;

public class Test1 {

    @Test
    public void test1(){
        ((Test1) new B()).a(1);
    }

    public void a(int ...i){
        System.out.println("method2");
    }
}

class B extends Test1{

    public void a(int i){
        System.out.println("method1");
    }
}
