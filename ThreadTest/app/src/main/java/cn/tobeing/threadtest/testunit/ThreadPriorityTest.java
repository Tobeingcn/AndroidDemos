package cn.tobeing.threadtest.testunit;

import java.util.TreeMap;

import cn.tobeing.threadtest.util.TimeComsuerUtil;

/**
 * Created by sunzheng on 15/11/17.
 */
public class ThreadPriorityTest extends AbstractTestUnit{
    private static final int TEST_COUNT=100;
    public ThreadPriorityTest(){
        super("线程优先级测试");
    }
    @Override
    public void run() {
        for(int i=0;i<10;i++) {
            Thread t1 = new Thread(new Runnable1("高优先级线程"+i));
            t1.setPriority(Thread.MAX_PRIORITY);
            t1.start();
        }
        Thread t2=new Thread(runnable2);
        Thread t3=new Thread(runnable3);
        t2.start();
        t3.start();
    }
    private class Runnable1 implements Runnable{
        private String mName;
        public Runnable1(String name){
            this.mName=name;
        }
        @Override
        public void run() {
            for (int i=0;i<TEST_COUNT;i++){
                d("我是一个"+mName+":"+i);
                time();
            }
        }
    };
    private Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            for (int i=0;i<TEST_COUNT;i++){
                d("run2我是一个兵，造福老百姓"+i);
                time();
            }
        }
    };
    private Runnable runnable3=new Runnable() {
        @Override
        public void run() {
            for (int i=0;i<TEST_COUNT;i++){
                d("run3我是一个兵，造福老百姓"+i);
                time();
            }
        }
    };
    private void time(){
        for (int i=0;i<TEST_COUNT;i++){
            for (int j=0;i<TEST_COUNT;i++){
                for (int k=0;i<TEST_COUNT;i++){
                    if((i+j+k)> new Object().hashCode()){
                        return;
                    }
                }
            }
        }
    }
}
