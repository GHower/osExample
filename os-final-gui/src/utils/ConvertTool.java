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
    public static String formatDecimal(double num,int suffix){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(suffix);
        return df.format(num);
    }

    // 磁盘三维地址转换
    // 磁盘转序号,track:磁道数 sector: 扇区数
    public static int DiskToSequence_X(int c,int h,int s,int track,int sector,int size){
        return (c*track*sector+h*sector+s)/size;
    }
    public static int DiskToSequence_Y(int c,int h,int s,int track,int sector,int size){
        return (c*track*sector+h*sector+s)%size;
    }

}
