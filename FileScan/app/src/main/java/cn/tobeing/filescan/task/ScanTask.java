package cn.tobeing.filescan.task;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by sunzheng on 16/7/7.
 */
public interface ScanTask {

    /**
     * return files that need to scan
     */
    String[] getScanPath();

    /**
     * scan filepath
     * */
    void scanFile(String filePath);


}
