package common;

import config.OSConfig;
import jobs.JCB;

import java.util.ArrayList;

/**
 * 对系统数据做一些统计
 */
public class OSStatics {
    // 统计已使用内存
    public static int getMemoryTotalUsed() {
        return OSConfig.ready_list.memoryStatics() +
                OSConfig.block_list.memoryStatics() +
                OSConfig.running_list.memoryStatics() +
                OSConfig.Kernel_SIZE;
    }

    // 统计已使用的PCB数
    public static int getPCBTotal() {
        return OSConfig.ready_list.size() +
                OSConfig.block_list.size() +
                OSConfig.running_list.size();
    }


    // 统计已完成作业的平均周转时间
    public static double getJobTT(JCB jcb) {
        return jcb.finishTime - jcb.submitTime;
    }

    // 统计已完成作业的平均周转时间
    public static double getJobsATT() {
        ArrayList<JCB> jcbs = new ArrayList<>(OSConfig.JCB_MAP_FINISHED.values());
        double sum = 0;
        for (JCB jcb : jcbs) {
            sum += getJobTT(jcb);
        }
        return sum / jcbs.size();
    }

    // 统计已完成作业的带权周转时间
    public static double getJobsWTT() {
        ArrayList<JCB> jcbs = new ArrayList<>(OSConfig.JCB_MAP_FINISHED.values());
        double sum = 0;
        for (JCB jcb : jcbs) {
            sum += getJobTT(jcb)/jcb.runTime;
        }
        return sum/jcbs.size();
    }
}
