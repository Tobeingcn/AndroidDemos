package cn.tobeing.filescan;

import cn.tobeing.filescan.task.ScanTask;

/**
 * Created by sunzheng on 16/7/7.
 */
public class ScanUtil {

    private static ScanUtil instance;

    public static synchronized ScanUtil getInstance() {
        if (instance == null) {
            instance = new ScanUtil();
        }
        return instance;
    }

    private ScanUtil() {

    }

    /**
     * 搜索文件
     */
    public void startScan(ScanTask task) {
        Thread.currentThread().getThreadGroup().getParent();
    }
}
