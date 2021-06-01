package os.utils;

import os.enums.MyStatus;
import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;

import java.text.DecimalFormat;

/**
 * 转换类
 * 1. JCB转为PCB，即为一个作业创建一个进程，
 * 1.1 调用前需要检查内存能不能分配
 * 1.2 todo: 后期修改为多个进程共同完成同一作业，即返回的是List
 *
 */
public class MyConvert {

    public static MyPCB convert(MyJCB myJCB) {
        MyPCB myPCB = new MyPCB();
        myPCB.setPid(1);
        myPCB.setPriority(myJCB.getPriority());
        myPCB.setStatus(MyStatus.READY);
        return myPCB;
    }

    public static MyPCB convert(MyJCB myJCB, MyProcess myProcess) {
        MyPCB myPCB = new MyPCB();
        myPCB.setPid(myProcess.getId());
        myPCB.setStatus(MyStatus.READY);
        myPCB.setPriority(1);
        myPCB.setJid(myJCB.getId());
        myPCB.setPriority(myJCB.getPriority());
        myPCB.setName(myProcess.getName());
        myPCB.setSize(myJCB.getSize());
        return myPCB;
    }
    // 转换显示方式
    public static String formatSize(Integer num){
        String result = "";
        double i = Double.valueOf(num);
        double kb = 1024;
        double mb = kb * 1024;
        double gb = mb * 1024;
        /*实现保留小数点两位*/
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(2);
        if (i >= gb){
            result =  df.format( i / gb) + "GB";
        }else if(i >= mb){
            result =  df.format( i / mb) + "MB";
        }else if(i >= kb){
            result = String.format("%.2f",  num / kb) + "KB";
        }else {
            result =  num + "B";
        }
        return result;
    }
}
