package test;

import java.util.LinkedList;
import java.util.List;

public class Test2 {
//    private static LinkedList<String> debug = new LinkedList<>();

    public static void main(String[] args) {
        Test2 t = new Test2();
        t.solution();
    }
    public void solution(){
        List<Integer> list = new LinkedList<>();
        for(int i = 100000; i <= 999999; i++){
            if(i % 100 == 0){ continue;}
            if(isVampileNum(i)){ list.add(i);}
        }
        System.out.println(list);
    }

    private static boolean isVampileNum(int n){
        int[] ordArray = getArray(n);
        int[] tempArray = new int[10];
        return combineNum(n, ordArray, tempArray, 0, 2);
    }

    private static boolean combineNum(int ordNum, int[] ordArray, int[] tempArray, int lastV, int times){
        if(times <= 0){
            if(lastV < 10 || lastV >= 100){ return false; }
            int f1 = lastV;
            int f2 = ordNum / f1;
            if(f1 * f2 == ordNum){
                int[] f1Array = tempArray;
                int[] f2Array = getArray(f2);
                boolean flag = true;
                for(int i = 0; i < 10 && flag; i ++){
                    flag = f1Array[i] + f2Array[i] == ordArray[i];
                }
                return flag;
            }
        }
        for(int i = 0; i < ordArray.length; i ++){
            if(tempArray[i] < ordArray[i] && lastV + i > 0){
                tempArray[i] ++;
                boolean flag = combineNum(ordNum, ordArray, tempArray, lastV * 10 + i, times - 1);
                tempArray[i] --;
                if(flag) { return true; }
            }
        }
        return false;
    }
    private static int[] getArray(int v){
        int[] a = new int[10];
        do{
            a[v % 10] ++;
            v /= 10;
        }while(v > 0);
        return a;
    }


}
