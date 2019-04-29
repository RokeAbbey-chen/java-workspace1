package com.geccocrawler.socks5.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test2 {


    public static void main(String[] args) {
        RobotController.Controller timeController = new CompositionController(
                new RobotController.Controller[]{
                        new DefaultController(10 * 1000, 10),
                        new DefaultController(20 * 1000, 35)
                });
        DefaultController scoresController = new DefaultController(11500, 300);
        RobotController robotController = new RobotController(scoresController, timeController){
            private double scoresSum = 0;
            @Override
            public void callBack(double scores, double curTimemes, double timeSum){
                scoresSum += scores;
//                if(scores >= 10  && curTimemes > 0) {
                    System.out.println("scores = " + scores + " scoresSum = " + scoresSum
                            + ", curTimems = " + curTimemes + ", timeSum = " + timeSum);
//                }
            }

            private double scoresRemainder = 0;
            @Override
            public double scoresCorrect(double scores, boolean hasNext){
                double[] nums = {scores, scoresRemainder};
                correct(nums, hasNext, 10);
                scoresRemainder = nums[1];
                return nums[0];
            }

            private void correct(double[] nums, boolean hasNext, int modder){
                double num = nums[0];
                double remainder = nums[1];
                remainder += num * 100 % ( 100 * modder ) / 100.0;
                num = (long) num / modder * modder;
                if (remainder >= modder || !hasNext) {
                    if (!hasNext){
                        remainder += 0.5; //防止浮点数因精度问题被截断导致向下取整
                    }
                    num += (long) remainder / modder * modder;
                    remainder = remainder - (long) remainder / modder * modder;
                }
                nums[0] = num;
                nums[1] = remainder;
            }
        };
        robotController.start();
    }



}

class RobotController {


    private Controller timeController;
    private Controller scoresController;
    private ScheduledExecutorService executor;
    private int WORKERS_COUNT = 1;
    private boolean zeroTimeIntervalRun = false;


    public RobotController(Controller controller){
        this(controller, controller);
    }

    public RobotController(Controller scoresController, Controller timeController){
        this(scoresController, timeController, null);
    }

    public RobotController(Controller scoresController, Controller timeController, ScheduledExecutorService executor){
        this.scoresController = scoresController;
        this.timeController = timeController;
        this.executor = executor;
    }

    public void start(){
        if (timeController == null){
            throw new NullPointerException("timeController can't be null");
        }

        if (scoresController == null){
            throw new NullPointerException("scoresController can't be null");
        }

        if (executor == null){
            executor = Executors.newScheduledThreadPool(WORKERS_COUNT);
        }

        if (timeController.hasNext()) {
            final double firstTimems = timeCorrect(timeController.next(), timeController.hasNext());
            executor.schedule(new Runnable() {
                double timemsSum = firstTimems;
                double curTimems = firstTimems;
                double remainingScores = 0;
                @Override
                public void run() {
                    double scores = 0;
                    if (scoresController.hasNext()){
                        scores = scoresCorrect(scoresController.next(), scoresController.hasNext());
                    }
                    remainingScores += scores;

                    if (zeroTimeIntervalRun || curTimems > 0){
                        RobotController.this.callBack(remainingScores, curTimems, timemsSum);
                        remainingScores = 0;
                    }
                    if (timeController.hasNext()) {
                        curTimems = timeCorrect(timeController.next(), timeController.hasNext());
                        timemsSum += curTimems;
                        executor.schedule(this, (long)curTimems, TimeUnit.MILLISECONDS);
                    }
                }
            }, (long)firstTimems, TimeUnit.MILLISECONDS);
        }
    }

    public double scoresCorrect(double scores, boolean hasNext){
        return scores;
    }

    public double timeCorrect(double timems, boolean hasNext){
        return timems;
    }

    public void callBack(double scores, double timems, double pastTimemsSum){

    }


    public interface Controller{

        public double next();

        public boolean hasNext();

        interface Function{

            public double next(double x);

            public Function getIntegralFunction();
        }
    }

}

class DefaultController extends BaseController{


    private int target;
    private int computeTimesLimit;
    private double step;
    private double lastValue = 0;
    private double lastX = 0;
    private static final double T = 3 * Math.PI;
    private static final double MAX_X = 7 * T;
    private static final double d2cRatio = 1.5;
    private static final int DEBUG_NONE = 0;
    private static final int DEBUG_INFO = 1;
    private static final int DEBUG_WARNING = 1 << 1;
    private static final int DEBUG_DEBUG = 1 << 2;
    private static final int DEBUG_LEVEL = DEBUG_NONE;

    public DefaultController(int target, int computeTimesLimit) {
        /*
         *
         * 这是一个将目标值target拆分成computeTimesLimite个数字的对象
         *
         * 其相关函数写死为 f(x) = c * sin(a * x + b) + d. (选用此函数纯粹是因为其具有周期性, 且有上下限，较好控制)
         * 其积分函数为:
         * F(x) = c * 1/a * -cos(a * x + b) + d * x
         * 每次的生成的数字为 f(x)在[x0, x1]区间的面积, 即F(x1) - F(x0), 注意此值可能为负数，如果需要面积一直为正值, 需要调节d比c大才能保证面积不为负数(1)
         *
         * 假定选取的周期为T, 则 a = 2p / T, a为角频率, p为pi
         * MAX_X则定位当拆分次数达到computeTimesLimit次的时候所达到的最大x坐标
         * 为了方便 直接设b = 0;
         * 因此调节用的常量就只有a, c, d, 其中a 通过T来调节， 由于后续step的计算方式， T也可以任意（只要不要太奇葩即可）;
         * 其中d的值在条件(1)范围内任意, 此处取d = d2cRatio * c
         * 接下来计算c
         * 易得方程F(MAX_X) - F(0) = target
         * 即 : c * T * (- 1) + d2cRatio * c * MAX_X  - c * T * (-1) = target
         * (d2cRatio * MAX_X ) * c =  target
         * c = target/(d2cRatio * MAX_X )
         * =>{
         *      c / a = target/(d2cRatio * MAX_X ) * T / 2p
         *      d = d2cRatio * c
         *   }
         * F(MAX_X) = T * target / (d2cRatio * MAX_X) * -cos( MAX_X / T) + target / (d2cRatio * MAX_X) * x
         */
        super(new CosineFunction(2 * Math.PI / T, 0,
                T * target /(d2cRatio * MAX_X * 2 * Math.PI),
                d2cRatio * target /(d2cRatio * MAX_X )){

            @Override
            public double next(double x){
                return super.next(x) + d * x - d;
            }
        });
        this.target = target;
        this.computeTimesLimit = computeTimesLimit;
        this.step = MAX_X / computeTimesLimit;
        this.lastValue = func.next(0);
        if ((DEBUG_LEVEL & DEBUG_DEBUG) == 1) {
            System.out.println("lastValue : " + lastValue);
            CosineFunction cosine = (CosineFunction) func;
            System.out.println(Math.cos(MAX_X));
            System.out.println(" check : " + (func.next(MAX_X) - lastValue) + ", " + (cosine.c * Math.cos(cosine.a * MAX_X) + cosine.d * MAX_X - lastValue));
            System.out.println("c = " + ((CosineFunction)func).c );
            System.out.println("d = " + ((CosineFunction)func).d );
            System.out.println("step = " + step);
        }
    }

    @Override
    public double next(){

        CosineFunction cosine = (CosineFunction) func;
        double value = func.next(lastX + step);
        if ((DEBUG_LEVEL & DEBUG_DEBUG) == 1) {
            System.out.println("lastValue : " + lastValue);
            System.out.println("value = " + value + ", cos = " + (cosine.c * Math.cos(cosine.a * (lastX + step)) + cosine.d * (lastX + step)));
        }
        double result = value - lastValue;
        lastValue = value;
        updateX();
        return result;
    }

    @Override
    public boolean hasNext(){
        return lastX + step <= MAX_X || doubleEquals(lastX + step, MAX_X, 0.001d);
    }

    @Override
    public void updateX(){
        lastX += step;
        if ((DEBUG_LEVEL & DEBUG_DEBUG) == 1) {
            System.out.println("lastX = " + lastX);
        }
    }

}

class CompositionController extends BaseController{

    RobotController.Controller[] controllers;
    private int index = 0;


    public CompositionController(RobotController.Controller[] controllers ) {
        super(null);
        this.controllers = controllers.clone();
    }

    @Override
    public double next(){
        checkIndex();
        if (index >= controllers.length){
            return 0;
        }
        return controllers[index].next();
    }

    @Override
    public boolean hasNext(){
        checkIndex();
        return index < controllers.length && controllers[index].hasNext();
    }

    private void checkIndex(){
        while (index < controllers.length
                && ( controllers[index] == null || !controllers[index].hasNext())){
            index ++;
        }
    }
}

class DadishuController extends BaseController{

    public DadishuController(double target, int times) {
        super(null);
    }

}


class BaseController implements RobotController.Controller{

    protected double x = 0;
    protected Function func;

    public BaseController(Function func){
        this.func = func;
    }

    @Override
    public double next() {
        double result = func.next(x);
        updateX();
        return result;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    protected void updateX(){
        x += 1;
    }

    public static boolean doubleEquals(double a, double b, double precisions ){
        return Math.abs(a - b) <= Math.abs(precisions);
    }


    public static class SineFunction implements Function{

        protected double a;
        protected double b;
        protected double c;
        protected double d;


        public SineFunction(){
            this(1);
        }

        public SineFunction(double a){
            this(a, 0);
        }

        public SineFunction(double a, double b){
            this(a, b, 1);
        }

        public SineFunction(double a, double b, double c){
            this(a, b, c, 0);
        }

        public SineFunction(double a, double b, double c, double d){
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public double next(double x) {
            return c * Math.sin(a * x + b) + d;
        }

        @Override
        public Function getIntegralFunction() {
            return null;
        }

        public double getA() {
            return a;
        }

        public void setA(double a) {
            this.a = a;
        }

        public double getB() {
            return b;
        }

        public void setB(double b) {
            this.b = b;
        }

        public double getC() {
            return c;
        }

        public void setC(double c) {
            this.c = c;
        }

        public double getD() {
            return d;
        }

        public void setD(double d) {
            this.d = d;
        }
    }

    public static class CosineFunction extends SineFunction{

        public CosineFunction(double a) {
            super(- a, Math.PI / 2);
        }

        public CosineFunction(double a, double b) {
            super(- a, b + Math.PI / 2);
        }

        public CosineFunction(double a, double b, double c) {
            super(- a, b + Math.PI / 2, c);
        }

        public CosineFunction(double a, double b, double c, double d) {
            super(- a, b + Math.PI / 2, c, d);
        }

    }

    public static class LinearFunction implements Function{

        double xStep = 1;
        double slope = 1;
        double d = 0;
        double x;

        public LinearFunction(double xStep, double slope, double d){
            this.xStep = xStep;
            this.slope = slope;
            this.d = d;
        }

        @Override
        public double next(double x) {
            return x * slope + d;
        }

        @Override
        public Function getIntegralFunction() {
            return null;
        }
    }

}





