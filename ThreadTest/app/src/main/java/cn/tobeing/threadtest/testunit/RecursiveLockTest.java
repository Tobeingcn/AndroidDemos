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
        MyLocker locker=new MyLocker();
        locker.synccount();
        locker.count3();
        d("MyLocker","测试结束，测试通过");
    }
    private class MyLocker {
        private int i=10;
        private Object locker=new Object();
        public void count(String message){
        }
        public synchronized void synccount(){
            d("MyLocker","synccount="+i);
            while (i++<100) {
                count2();
                synccount();
            }
        }
        public void count2(){
            synchronized (this){
                d("MyLocker","count2="+i);
            }
        }
        public void count3(){
            d("MyLocker","count3="+i);
            synchronized (locker){
                while (i++<100) {
                    count3();
                }
            }
        }
    }
}
