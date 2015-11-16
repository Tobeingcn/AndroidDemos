package cn.tobeing.threadtest.testunit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunzheng on 15/11/16.
 */
public abstract class AbstractTestUnit implements Runnable{
    private static final String TAG="TestUnit";
    private String mName;

    public AbstractTestUnit(String name){
        this.mName=name;
    }
    public String  getName(){
        return mName;
    }
    public void setName(String name){
        this.mName=name;
    }
    public void startTest(){
        Thread t=new Thread(this);
        t.start();
    }
    protected  void d(String tag,String message){
        Log.d(TAG, tag + "" + message);
    }
    protected  void d(String message){
        this.d("",message);
    }
}
