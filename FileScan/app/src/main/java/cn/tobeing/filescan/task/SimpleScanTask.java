package cn.tobeing.filescan.task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunzheng on 16/7/7.
 */
public class SimpleScanTask implements ScanTask {

    private String[] paths;

    public SimpleScanTask(String path) {
        this(new String[]{path});
    }

    public SimpleScanTask(String... path) {
        paths = path;
    }

    @Override
    public String[] getScanPath() {
        return paths;
    }

    @Override
    public void scanFile(String filePath) {

    }
}
