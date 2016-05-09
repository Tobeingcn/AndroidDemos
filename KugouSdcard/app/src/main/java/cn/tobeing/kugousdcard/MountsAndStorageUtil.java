package cn.tobeing.kugousdcard;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by sunzheng on 16/3/29.
 */
public class MountsAndStorageUtil {
    /**
     * 曾亮使用的临时类，以后记得删除
     *
     * @return
     */
    public static String fetchMountsInfo() {
        StorageMessage.setTips("fetchMountsInfo...");
        StorageMessage.add("fetchMountsInfo");
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            TestShell(new String[]{"ls"},"/");
            String[] args = {
                    "/system/bin/cat", "/proc/mounts"
            };
            result = cmdexe.run(args, "/system/bin/");
            if(!TextUtils.isEmpty(result)){
                StorageMessage.add("sucess");
                StorageMessage.add("=========================================");
                StorageMessage.add(result);
                StorageMessage.add("=========================================");
            }else{
                StorageMessage.add("fail.1");
                TestShell(new String[]{"ls","/proc/"},"/");
                TestShell(new String[]{"ls","/system/bin/"},"/");
                TestShell(new String[]{"cat","/proc/mounts"},"/");
                TestShell(new String[]{"more","/proc/mounts"},"/");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            StorageMessage.add(ex.getMessage());
            StorageMessage.add("fetchMountsInfo.fail");
            TestShell(new String[]{"ls","/proc/"},"/");
            TestShell(new String[]{"ls","/system/bin/"},"/");
            TestShell(new String[]{"cat","/proc/mounts"},"/");
            TestShell(new String[]{"more","/proc/mounts"},"/");
        }
        return result;
    }
    private static String TestShell(String[] args,String wordDir){
        StorageMessage.add("TestShell:"+toString(args));
        String result = null;
        StorageMessage.setTips("TestShell");
        CMDExecute cmdexe = new CMDExecute();
        try {
            result = cmdexe.run(args, wordDir);
        }catch (Exception e){
            StorageMessage.add(e.getMessage());
            StorageMessage.add("TestShell.fail");
            e.toString();
        }
        StorageMessage.add("TestShell.result:"+result);

        return result;
    }
    public static String toString(String[] args){
        StringBuilder stringBuilder=new StringBuilder();
        if(args!=null){
            for (String s:args){
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }
    /**
     * 获取多余的挂载点
     *
     * @return ArrayList.get(0)是多余的挂载点，如/mnt/sdcard/sd-ext/
     *         get(1)是有效的存储目录，如/mnt/sdcard/
     */
    public static ArrayList<HashSet<String>> getRepeatMountsAndStorage() {
        ArrayList<HashSet<String>> results = getRepeatMountsAndStorage(null);
        if (results.get(1).contains("/mnt/sdcard/") && !results.get(0).contains("/sdcard/")) {
            results.get(0).add("/sdcard/");
        }
        if (results.get(1).isEmpty()) {
            results.get(1).add(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return results;
    }

    /**
     * 获取多余的挂载点
     *
     * @return ArrayList.get(0)是多余的挂载点，如/mnt/sdcard/sd-ext/
     *         get(1)是有效的存储目录，如/mnt/sdcard/
     */
    public static ArrayList<HashSet<String>> getRepeatMountsAndStorage(String noUse) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        ArrayList<HashSet<String>> result = new ArrayList<HashSet<String>>(2);
        HashSet<String> repeatMounts = new HashSet<String>();
        HashSet<String> totalStorages = new HashSet<String>();
        HashSet<String> effectiveStorages = new HashSet<String>();
        ArrayList<StorageInfo> storageInfos = getAllSdcards(storagePath);
        for (StorageInfo info : storageInfos) {
            String path = info.path;
            if (!path.endsWith("/")) {
                path += "/";
            }
            totalStorages.add(path);
            if (info.type == StorageInfo.TYPE.Available) {
                effectiveStorages.add(path);
            } else if (info.type == StorageInfo.TYPE.RepeatMount) {
                repeatMounts.add(path);
            }
        }
        repeatMounts.removeAll(effectiveStorages);
        result.add(repeatMounts);
        result.add(effectiveStorages);
        result.add(totalStorages);
        return result;
    }

    public static class StorageInfo {
        public static enum TYPE {
            Available, SubStorage, RepeatMount
        }

        public StorageInfo() {

        }

        public String path;

        public String device;

        public long size = 0; // 磁盘大小

        public TYPE type = TYPE.Available;

        public String line;

        @Override
        public String toString() {
            return String.format("[StorageInfo:device:%s;path:%s;size:%s;type:%s;line:%s]", device,
                    path, size, type, line);
        }
    }

    /**
     * 最新的获取外置SDCard的方法之前的方法以后都不可用
     *
     * @return
     */
    private static ArrayList<StorageInfo> getAllSdcards(String storagePath) {
        String mountInfo = fetchMountsInfo();
        ArrayList<StorageInfo> storageList = paraserSdcards(mountInfo);
        StorageMessage.add("初步检测获得如下SD卡");
        StorageMessage.add("storagePath="+storagePath);
        for (StorageInfo storageInfo:storageList){
            StorageMessage.add("=====");
            StorageMessage.add("storageInfo="+storageInfo.toString());
        }
        File file = new File(storagePath);
        if (!isPathContain(storageList, storagePath) && file.exists()) {
            StorageInfo info = new StorageInfo();
            info.device = "sdcard";
            info.path = storagePath + "/";
            info.size = file.getTotalSpace();
            storageList.add(info);
        }
        storageList = filterSubSdcards(storageList, storagePath);
        storageList = filterSameSdcards(storageList, storagePath);
        StorageMessage.add("最终路径获得如下SD卡");
        StorageMessage.add("storagePath="+storagePath);
        for (StorageInfo storageInfo:storageList){
            StorageMessage.add("=====");
            StorageMessage.add("storageInfo="+storageInfo.toString());
        }
        return storageList;
    }

    private static boolean isPathContain(ArrayList<StorageInfo> storageList,
                                         final String storagePath) {
        for (int i = 0; i < storageList.size(); i++) {
            if (storageList.get(i).path.equals(storagePath + "/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤相同的文件夹，如果两个文件夹的大小相同并且文件夹中的目录结构有90%是相同的那么就认为这两个文件夹是同一个文件夹
     *
     * @param storageList
     * @return
     */
    private static ArrayList<StorageInfo> filterSameSdcards(ArrayList<StorageInfo> storageList,
                                                            final String storagePath) {
        // 按照文件夹的大小进行排序
        Collections.sort(storageList, new Comparator<StorageInfo>() {
            @Override
            public int compare(StorageInfo lhs, StorageInfo rhs) {
                if (lhs.path.equals(rhs.path)) {
                    return lhs.type.ordinal() - rhs.type.ordinal();
                }
                int result = (int) (lhs.size - rhs.size);
                if (result != 0) {
                    return result;
                }
                if (lhs.path.equals(storagePath + "/")) {
                    return -1;
                } else if (rhs.path.equals(storagePath + "/")) {
                    return 1;
                } else {
                    return lhs.type.ordinal() - rhs.type.ordinal();
                }
            }
        });
        StorageInfo info1, info2;
        for (int i = 0, length = storageList.size(); i < length - 1; i++) {
            info1 = storageList.get(i);
            info2 = storageList.get(i + 1);
            if (info1.size == info2.size) {
                for (int j = i; j < length - 1; j++) {
                    info2 = storageList.get(j + 1);
                    if (info1.size != info2.size) {
                        break;
                    }
                    if (info2.type != StorageInfo.TYPE.Available) {
                        continue;
                    }
                    if (isSameDir(info1, info2) || isSameDir(info1, info2)
                            || isSameDir(info1, info2)) {// 如果不是重复挂在点，判断多次，防止因扫描过程中Sd卡中的文件被改变
                        info2.type = StorageInfo.TYPE.RepeatMount;
                    }
                }
            }
        }
        return storageList;
    }

    /**
     * 判断两个路径是否为同一个路径，如果两个路径的文件结构相同的话，就认为两个文件相同
     *
     * @param
     * @param
     * @return
     */
    private static boolean isSameDir(StorageInfo info1, StorageInfo info2) {
        if (info1.path.equals(info2.path)) {
            return true;
        }
        File file = new File(info2.path);
        if (!file.exists() || !file.isDirectory()) {
            return true;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return true;
        }
        int sameFileCounts = 0;
        File subFile, compareFile;
        for (int i = 0; i < files.length; i++) {
            subFile = files[i];
            compareFile = new File(info1.path, subFile.getName());
            if (compareFile.exists() && subFile.exists()) {
                if (subFile.lastModified() == compareFile.lastModified()) {
                    sameFileCounts++;
                }
            } else if (!compareFile.exists() && !subFile.exists()) {
                sameFileCounts++;
            }
            if (i > 10) {
                if (((float) sameFileCounts) / i > 0.99) {
                    return true;
                } else if (((float) sameFileCounts) / i < 0.01) {
                    return false;
                }
            }
        }
        if (((float) sameFileCounts) / files.length > 0.9) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 过滤掉挂在点中的自目录，某些手机中某张SD卡是别的Sd卡的子目录
     *
     * @param storageList
     * @return
     */
    private static ArrayList<StorageInfo> filterSubSdcards(ArrayList<StorageInfo> storageList,
                                                           String storagePath) {
        // 过滤SD卡重名路径
        Collections.sort(storageList, new Comparator<StorageInfo>() {
            @Override
            public int compare(StorageInfo lhs, StorageInfo rhs) {
                return lhs.path.compareTo(rhs.path);
            }
        });
        for (int i = 0, length = storageList.size(); i < length - 1; i++) {
            if (storageList.get(i + 1).path.contains(storageList.get(i).path)) {
                storageList.get(i + 1).type = StorageInfo.TYPE.SubStorage;
            }
        }
        return storageList;

    }

    /**
     * 解析挂在点信息，解析到的为全部的挂在点，可能有重复的
     *
     * @param mountInfo
     * @return
     */
    private static ArrayList<StorageInfo> paraserSdcards(String mountInfo) {
        String reg = "(?i).*(media|vold|fuse).*(vfat|ntfs|exfat|fat32|ext3|ext4|fuse|sdcardfs).*rw.*";
        ArrayList<StorageInfo> storageInfo = new ArrayList<MountsAndStorageUtil.StorageInfo>();
        if (TextUtils.isEmpty(mountInfo)) {
            return storageInfo;
        }
        final String[] lines = mountInfo.split("\n");
        for (String line : lines) {
            String lowercaseline = line.toLowerCase();
            if (line.contains("secure"))
                continue;
            if (line.contains("asec"))
                continue;
            // htc的blinkFeed路径
            if (line.contains("/blinkfeed"))
                continue;
            // 某款三星的机器，外置SD卡的信息为/mnt/media_rw/extSdCard /storage/extSdCard
            // if (line.contains("media"))
            // continue;
            //|| line.contains("data")
            if (line.contains("system") || line.contains("cache") || line.contains("sys")
                    || line.contains("tmpfs") || line.contains("shell")
                    || line.contains("root") || line.contains("acct") || line.contains("proc")
                    || line.contains("misc") || line.contains("obb")) {
                continue;
            }
            if (!lowercaseline.startsWith("/")) {
                continue;
            }
            if (!lowercaseline.matches(reg)) {
                continue;
            }
            // 根据空格分割一条记录为几个字符串
            String[] parts = line.split(" ");
            String device = null;
            String path = null;
            for (String part : parts) {
                if (part.startsWith("/")) {
                    if (part.toLowerCase().contains("vold") || part.toLowerCase().contains("fuse")
                            || part.toLowerCase().contains("media")
                            || part.toLowerCase().contains("/data/share")) {
                        device = part;
                    } else {
                        path = part;
                    }
                }
            }
            if (device != null && path != null && !path.contains("shell")) {
                File file = new File(path);
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                // 路径对应的文件必须要存在,排除掉mounts表里面不存在的文件
                if (file.exists() && file.canRead()) {
                    StorageInfo info = new StorageInfo();
                    info.device = device;
                    info.path = path;
                    info.size = file.getTotalSpace();
                    storageInfo.add(info);
                }
            }
        }
        return storageInfo;

    }

    /**
     * 返回mounts信息，给曾亮测试下载路径修改用的，其他人不要使用
     *
     * @return
     */
    public static ArrayList<StorageInfo> getAllMountsInfo() {
        String mountInfo = fetchMountsInfo();
        ArrayList<StorageInfo> storageList = paraserSdcards(mountInfo);
        return storageList;
    }

    /**
     * 亮亮用的方法，以后记得删除 查找所有的sdcard根路径
     *
     * @return
     */
    public static ArrayList<String> findAllSDcardPaths() {
        HashSet<String> sdcardPath = MountsAndStorageUtil.getRepeatMountsAndStorage().get(1);
        ArrayList<String> list = new ArrayList<String>();
        Iterator<String> it = sdcardPath.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (File.separatorChar == path.charAt(path.length() - 1)) {
                path = path.substring(0, path.length() - 1);
            }
            list.add(path);
        }
        final String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (lhs.equals(storagePath)) {
                    return -1;
                } else if (rhs.equals(storagePath)) {
                    return 1;
                } else {
                    return lhs.compareTo(rhs);
                }
            }
        });
        return list;

    }
}