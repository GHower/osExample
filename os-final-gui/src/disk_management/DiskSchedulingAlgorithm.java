package disk_management;


import config.OSConfig;

import java.util.ArrayList;
import java.util.Vector;

public class DiskSchedulingAlgorithm {
    static int current_cursor=0;//当前的游标
    static int cursor_direction=1;// 游标方向

    public static String diskSchedulingAlgorithm(Vector<Integer> disks, int flag, ArrayList<String> files) {
        switch (OSConfig.DISK_SCHEDULE_FLAG) {
            case 1:
                return SCAN(disks, flag, files);
            case 2:
                return CSCAN(disks, flag, files);
            case 0:
            default:
                return FCFS(disks, flag, files);
        }
    }

    // 先来先服务算法（FCFS）
    // flag :0-读文件；1-抹除并写文件；2-抹除文件
    public static String FCFS(Vector<Integer> disks, int flag, ArrayList<String> files) {
        if (flag == 0) {
            String info = "";
            for (int diskID : disks) {
                info += OSConfig.MyDisk[diskID].substring(1);
            }
            return info;
        } else if (flag == 1) {
            int i = 0;
            System.err.println(disks.size());
            for (int diskID : disks) {
                System.err.println(diskID + "   " + files.get(i));
                OSConfig.MyDisk[diskID] = "$";
                OSConfig.MyDisk[diskID] += files.get(i);
                i++;
            }
            return null;

        } else {
            for (int diskID : disks) {
                OSConfig.MyDisk[diskID] = "$";
            }
            return null;
        }

    }

    // 扫描算法（SCAN）
    public static String SCAN(Vector<Integer> disks, int flag, ArrayList<String> files) {
        Vector<Integer> disks2 = new Vector<>(disks);
        if (flag == 0) {
            String info = "";
            for (int diskID : disks2) {
                info += OSConfig.MyDisk[diskID].substring(1);
            }
            return info;
        } else if (flag == 1) {
            int i = 0;
            int size = disks2.size();
            int total = 0;
            while (i < size) {
                // 改变游标方向
                if (current_cursor > OSConfig.MyDisk.length-1 || current_cursor < 0) {
                    cursor_direction = -cursor_direction;
                }
                // 磁头到了指定位置
                if (disks2.contains(current_cursor)) {
                    OSConfig.MyDisk[current_cursor] = "$";
                    OSConfig.MyDisk[current_cursor] += files.get(i);
                    disks2.remove(Integer.valueOf(current_cursor));
                    i++;
                }
                current_cursor += cursor_direction;
                total += 1;
            }
            return null;
        } else {
            for (int diskID : disks) {
                OSConfig.MyDisk[diskID] = "$";
            }
            return null;
        }
    }

    // TODO:循环扫描算法（CSCAN）
    public static String CSCAN(Vector<Integer> disks, int flag, ArrayList<String> files) {
        return null;
    }

}
