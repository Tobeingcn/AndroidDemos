package cn.tobeing.notificationdemos;

import android.util.Log;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sunzheng on 15/11/11.
 * 耗时分析工具
 */
public class TimeComsuerUtil {
    private static final String TAG="TimeComsuerUtil";
    private static TimeComsuerUtil instance;

    private Map<String,TimeConsumer> timeCahe;

    public static  TimeComsuerUtil getInstance(){
        if(instance==null){
            synchronized (TimeComsuerUtil.class){
                if(instance==null){
                    instance= new TimeComsuerUtil();
                }
            }
        }
        return instance;
    }
    private TimeComsuerUtil(){
        timeCahe= new HashMap<String,TimeConsumer>();
    }
    public void start(String key){
        Log.d(TAG, "start," + key);
        if(!timeCahe.containsKey(key)){
            timeCahe.put(key,new TimeConsumer(key));
        }
        TimeConsumer consumer=timeCahe.get(key);
        if(consumer!=null){
            consumer.start();
        }
    }
    public void end(String key){
        TimeConsumer consumer=timeCahe.get(key);
        if(consumer!=null){
          long time= consumer.end();
            Log.d(TAG, "endin," + key + ":"+time);
        }
    }
    public void printResult(){
        for (String key:timeCahe.keySet()){
            timeCahe.get(key).printResult();
        }
    }
    public void printResultAll(){
        for (String key:timeCahe.keySet()){
            timeCahe.get(key).printResultAll();
        }
    }
    public void reset(){
        timeCahe.clear();
    }

    private class TimeConsumer{
        private String key="";
        private long average=0;
        private int count=0;
        public long startTime=-1;
        private long mSum=0;
        private ArrayList<Long> timeList;
        public TimeConsumer(String key){
            this.key=key;
            timeList=new ArrayList<Long>();
        }
        public void start(){
            startTime=System.currentTimeMillis();
        }
        public long end(){
            if(startTime!=-1){
                long time=System.currentTimeMillis()-startTime;
                mSum+=time;
                count++;
                average=mSum/count;
                timeList.add(time);
                startTime=-1;
                return time;

            }
            return -1;
        }
        public void printResult(){
            Log.d(TAG,String.format("key[平均耗时%d,次数%d]\n",average,count));
        }
        public void printResultAll(){
            Log.d(TAG,String.format("key=%s[平均耗时%d,次数%d]",key,average,count));
            StringBuilder sb=new StringBuilder("详细数据｛");
            for (Long time:timeList){
                sb.append(time);
                sb.append(',');
            }
            sb.append("}");
            Log.d(TAG,sb.toString());
        }

    }
}
