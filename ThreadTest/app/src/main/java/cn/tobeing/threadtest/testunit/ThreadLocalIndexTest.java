package cn.tobeing.threadtest.testunit;

/**
 * Created by sunzheng on 15/11/16.
 */
public class ThreadLocalIndexTest extends AbstractTestUnit {
    public ThreadLocalIndexTest() {
        super("ThreadLocalIndexTest");
    }

    private static Index num = new Index();
    //创建一个Index类型的本地变量
    private static ThreadLocal<Index> local = new ThreadLocal<Index>() {
        @Override
        protected Index initialValue() {
            return num;
        }
    };

    @Override
    public void run() {
        Thread[] threads = new Thread[5];
        for (int j = 0; j < 5; j++) {
            threads[j] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //取出当前线程的本地变量，并累加1000次
                    Index index = local.get();
                    for (int i = 0; i < 1000; i++) {
                        index.increase();
                    }
                    d(Thread.currentThread().getName() + " : " + index.num);

                }
            }, "Thread-" + j);
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    static class Index {
        int num;

        public void increase() {
            num++;
        }
    }
}
