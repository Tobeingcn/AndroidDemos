package cn.tobeing.filescan;

import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunzheng on 16/7/7.
 */
public class Utils {

    private static List<String> musicFilesExt = Arrays.asList(new String[]{"mp3", "m4a", "flac", "wma", "acc"});

    public static boolean isMusicFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        if (fileName.contains(".")) {
            String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (TextUtils.isEmpty(extName)) {
                return false;
            }
            return musicFilesExt.contains(extName.toLowerCase());
        }
        return false;
    }

    public static boolean isMusicFile(File file) {
        return isMusicFile(file.getName());
    }
}
