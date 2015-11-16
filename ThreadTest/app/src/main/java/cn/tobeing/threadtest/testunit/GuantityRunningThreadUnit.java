package cn.tobeing.threadtest.testunit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunzheng on 15/11/16.
 */
public class GuantityRunningThreadUnit extends AbstractTestUnit{
    private List<Thread > threadList=new ArrayList<>();
    public GuantityRunningThreadUnit(){
        this("大量持续运行线程测试");
    }
    public GuantityRunningThreadUnit(String name){
        super(name);
    }
    @Override
    public void run() {
        for(int i=0;i<Integer.MAX_VALUE;i++){
            d(getName(), "申请线程数" + i);
            Thread thread=new RunningThread();
            thread.start();
            threadList.add(thread);
        }
    }
    private class RunningThread extends Thread{
        @Override
        public void run() {
            int i=100;
            while (true){
                i++;
            }
        }
    }
}
