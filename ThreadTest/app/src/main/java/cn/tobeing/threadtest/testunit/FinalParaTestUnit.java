package cn.tobeing.threadtest.testunit;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by sunzheng on 15/12/9.
 */
public class FinalParaTestUnit extends AbstractTestUnit{
    public FinalParaTestUnit() {
        super("final 参数内存泄漏");
    }
    public FinalParaTestUnit(String name) {
        super(name);
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap=;
                    test(newByte);
                }
            }
        }).start();
    }
    public void test(final Byte[] bytes){
        d("FinalParaTestUnit",bytes+"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                d("FinalParaTestUnit","hhh="+bytes+"");
            }
        }).start();
    }
}
