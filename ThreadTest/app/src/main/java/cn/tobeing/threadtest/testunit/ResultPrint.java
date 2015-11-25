package cn.tobeing.threadtest.testunit;

import cn.tobeing.threadtest.util.TimeComsuerUtil;

/**
 * Created by sunzheng on 15/11/17.
 */
public class ResultPrint extends AbstractTestUnit{
    public ResultPrint(){
        super("输出结果");
    }

    @Override
    public void run() {
        TimeComsuerUtil.getInstance().printResultAll();
        TimeComsuerUtil.getInstance().reset();
    }
}
