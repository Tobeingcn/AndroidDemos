package cn.tobeing.threadtest.testunit;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sunzheng on 15/11/16.
 */
public class ReentrantLockTest extends AbstractTestUnit{

    public ReentrantLockTest(){
        super("递归死锁测试");
    }

    @Override
    public void run() {
        Counter counter=new Counter();
        for (int i=0;i<100000;i++){
            counter.count();
        }
    }

    private class Counter{
        ReentrantLock lock=new ReentrantLock();
        private int mCount=0;

        public void count(){
            lock.lock();
            try{
                mCount++;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}
