package cn.tobeing.kugousdcard;

/**
 * Created by sunzheng on 16/3/29.
 */
public class StorageMessage {
    private static StringBuilder stringBuilder=new StringBuilder();
    private static String Tips;
    public synchronized static void add(String message){
        stringBuilder.append(message);
        stringBuilder.append("\n");
    }
    public synchronized static String getMessage(){
        return stringBuilder.toString();
    }
    public synchronized static void clear(){
        stringBuilder=new StringBuilder();
    }

    public static String getTips() {
        return Tips;
    }

    public static void setTips(String tips) {
        Tips = tips;
    }
}
