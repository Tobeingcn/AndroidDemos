package cn.tobeing.threadtest.testunit;

/**
 * Created by sunzheng on 15/11/16.
 */
public class LockObject {
    public static enum Action{
        syncClass,syncStaticMethod,syncObjecy,syncMethod,lockObject,locker
    }
    public LockObject(){

    }
    public static class Counter{

        public static void count(String message,int Count){

        }
    }
    public static synchronized void syncStaticMethod(){

    }
    public static LockObject countStatic(String message){

    }
}
