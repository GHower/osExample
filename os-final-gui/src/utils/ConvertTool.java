package utils;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConvertTool {
    // 转换显示方式
    public static String formatSize(Integer num, boolean suffix) {
        String result = "";
        String suf = "B";
        double i = Double.valueOf(num);
        double kb = 1024;
        double mb = kb * 1024;
        double gb = mb * 1024;
        /*实现保留小数点两位*/
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(2);
        if (i >= gb) {
            result = df.format(i / gb);
            suf = "GB";
        } else if (i >= mb) {
            result = df.format(i / mb);
            suf = "MB";
        } else if (i >= kb) {
            result = String.format("%.2f", num / kb);
            suf = "KB";
        } else {
            result = num + "";
        }
        return result + (suffix ? suf : "");
    }

    public static String formatDecimal(double num, int suffix) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(suffix);
        return df.format(num);
    }

    // 磁盘三维地址转换,C、H、S分别是磁柱、磁头、扇区的编号
    // 磁盘转序号,track:每个柱面的磁道数 sector: 磁道的扇区数 size:位图阵列大小
    public static int DiskToSequence_X(int c, int h, int s, int track, int sector, int size) {
        return (c * track * sector + h * sector + s) / size;
    }

    public static int DiskToSequence_Y(int c, int h, int s, int track, int sector, int size) {
        return (c * track * sector + h * sector + s) % size;
    }

    // 序号转磁盘,b: 位图中的序号
    public static int SequenceToDisk_C(int b, int track, int sector) {
        return b / (track * sector);
    }

    public static int SequenceToDisk_H(int b, int track, int sector) {
        return (b / sector) % track;
    }

    public static int SequenceToDisk_S(int b, int sector) {
        return b % sector;
    }
}
