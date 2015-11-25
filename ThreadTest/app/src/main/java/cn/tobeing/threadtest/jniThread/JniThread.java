package cn.tobeing.threadtest.jniThread;

/**
 * Created by sunzheng on 15/11/17.
 */
public class JniThread {

    static{
        System.loadLibrary("JniThread");
    }
    public native void start();
}
