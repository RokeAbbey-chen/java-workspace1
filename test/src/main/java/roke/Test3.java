package roke;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public class Test3 {
    public static void main(String[] args){
        main1();
    }
    private static void main1(){
        int result = Arrays.binarySearch(eloConfig, 1011, new Comparator<Serializable>() {
            @Override
            public int compare(Serializable o1, Serializable o2) {
                Integer c1 = 0, c2 = 0;
                if (o1 instanceof double[]){
                    c1 = (int)((double[])o1)[0];
                    c2 = (int)o2;
                } else {
                    c1 = (int)o1;
                    c2 = (int)((double[]) o2)[0];
                }
                return c1.compareTo(c2);
            }
        });
        System.out.println("result = " + result);

    }
    private static double[][] eloConfig = {
            {1000, 415.821451509, 266.875162611},
            {1050, 482.430394894, 316.271442762},
            {1100, 652.609073222, 359.296165583},
            {1150, 844.833215396, 378.413847884},
            {1200, 992.55378546, 372.624711377},
            {1250, 1094.94413626, 368.33869754},
            {1300, 1185.59421441, 367.83089429},
            {1350, 1260.06037968, 364.520292497},
            {1400, 1328.02105412, 361.412772183},
            {1450, 1393.89453564, 354.54457667},
            {1500, 1453.36289464, 343.747691525},
            {1550, 1509.06205674, 338.065621689}
    };


}
