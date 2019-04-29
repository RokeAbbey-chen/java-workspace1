package util;


public class Test{

    public static void main(String[] args){

        Test t = new Test();
        for (int i = 0; i < 10; i ++){
            t.a(i);
        }
    }

    public void a(int n){
        final int num = n;
        new Helper(){
            @Override
            public void doSth(){
                System.out.println(a);
            }

        }.do();
    }
}

interface Helper{

    public void doSth();

}
