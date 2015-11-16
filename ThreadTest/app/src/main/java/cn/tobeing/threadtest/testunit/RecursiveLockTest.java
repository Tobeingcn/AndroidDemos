package cn.tobeing.threadtest.testunit;

/**
 * Created by sunzheng on 15/11/16.
 */
public class RecursiveLockTest extends AbstractTestUnit{
    public RecursiveLockTest(){
        super("递归锁方法测试");
    }
    @Override
    public void run() {

    }
    private class MyLocker {

        public void count(String message){

        }
        public synchronized void count(){
        }
        public void count2(){
            synchronized (this){

            }
        }
    }
}
