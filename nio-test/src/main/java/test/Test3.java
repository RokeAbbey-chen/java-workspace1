package test;

import java.util.Map;

public class Test3 {

    private int[][] levelMap = new int[100][];
    private DerivedFunction calScores;
    private DerivedFunction calTimeGap;
    private static final QuadraticDerivedFunction quadratic = new QuadraticDerivedFunction();

    {
        for (int i = 0; i < levelMap.length; i ++){
            levelMap[i] = new int[]{i * 100, (i + 1) * 100};
        }
    }



    public int nextScores(Robot robot){

    }


    private int calculateScores(){

    }

    class Robot{

        int curScores;
        int targetScores;

    }

    interface DerivedFunction{
        public double cal();
    }

    public static class QuadraticDerivedFunction implements DerivedFunction{

        private double a;
        private double b;

        private QuadraticDerivedFunction(double a, double b){
            this.a = a;
            this.b = b;
        }

        private QuadraticDerivedFunction(int x, int top){
            
        }

        @Override
        public double cal() {
            return 0;
        }
    }

    public static class SineDerivedFunction implements DerivedFunction{

        @Override
        public double cal() {
            return 0;
        }
    }

}
