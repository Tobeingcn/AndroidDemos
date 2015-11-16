package cn.tobeing.threadtest.testunit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunzheng on 15/11/16.
 */
public class GuantityThreadUnit extends AbstractTestUnit{
    private List<Thread > threadList=new ArrayList<>();
    public GuantityThreadUnit(){
        super("大量线程测试");
    }
    public GuantityThreadUnit(String name){
        super(name);
    }
    @Override
    public void run() {
        for(int i=0;i<10000;i++){
            d(getName(), "申请线程数" + i);
            Thread thread=new Thread();
            threadList.add(thread);
        }
    }
}
