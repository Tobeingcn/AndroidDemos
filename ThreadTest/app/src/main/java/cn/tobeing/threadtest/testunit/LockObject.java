package cn.tobeing.threadtest.testunit;

import android.util.Log;

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
        for(int i=0;i<1000;i++){
            Log.d("","");
        }
    }
    public static LockObject countStatic(String message){
        return null;
    }
}
