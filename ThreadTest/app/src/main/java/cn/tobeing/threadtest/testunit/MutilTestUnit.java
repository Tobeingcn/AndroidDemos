package cn.tobeing.threadtest.testunit;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunzheng on 15/11/16.
 */
public class MutilTestUnit extends AbstractTestUnit{
    private static final String TAG="MutilTestUnit";
    private List<AbstractTestUnit> subTestUnits;
    public MutilTestUnit(){
        this("批量测试线程");
    }
    public MutilTestUnit(String name){
        super(name);
        subTestUnits=new ArrayList<>();
    }
    @Override
    public void run() {
        d(TAG,"start:"+getName());
        for(AbstractTestUnit testUnit:subTestUnits){
            d(TAG,"start,sunTestUnit:"+testUnit.getName());
            testUnit.run();
        }
    }
}
