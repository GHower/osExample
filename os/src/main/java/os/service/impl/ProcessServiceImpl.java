package os.service.impl;

import os.model.entity.MyPCB;
import os.service.ProcessService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 进程调度服务
 */
public class ProcessServiceImpl implements ProcessService {
    /**
     * TODO:先来先服务，计算各个时间信息
     *
     * @param pcbs 需要计算的pcb数组,PCB中按到达时间优先
     * @return 先来先服务结果
     */
    public List<MyPCB> FCFS(List<MyPCB> pcbs) {
        // 先对输入数组排序
        List<MyPCB> result = pcbs.stream().sorted(Comparator.comparingDouble(MyPCB::getArriveTime)).collect(Collectors.toList());

        for (int i = 0; i < pcbs.size(); i++) {
            MyPCB myPCB = result.get(i);
            // 第一项，直接计算
            if (i == 0) {
                myPCB.setStartTime(myPCB.getArriveTime());
                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
            } else if (myPCB.getArriveTime()>=pcbs.get(i-1).getFinishTime()){
                // 到达时间在前一个进程执行完成之后，则该进程同第一个进程操作
                myPCB.setStartTime(myPCB.getArriveTime());
                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
            }else{
                // 开始时间将是上一个进程完成的时间
                myPCB.setStartTime(pcbs.get(i-1).getFinishTime());
                // 有等待时间
                myPCB.setWaitTime(myPCB.getStartTime()-myPCB.getArriveTime());
                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
            }
//            result.set(i,myPCB);
        }
//        System.out.println(result);
        this.display(result);
        return null;
    }

    /**
     * TODO:短作业优先，计算各个时间信息
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 短作业优先调整后的pcb数组
     */
    public List<MyPCB> SJF(List<MyPCB> pcbs) {
        return null;
    }

    /**
     * TODO:高响应比优先，计算各个时间信息
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public List<MyPCB> FPF(List<MyPCB> pcbs) {
        return null;
    }

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(List<MyPCB> pcbs) {
        System.out.println("执行次序\t进程pid\t提交时间\t开始时间\t等待时间\t完成时间\t运行时间\t周转时间\t带权周转时间");
        for (int i=0;i<pcbs.size();i++) {
            MyPCB myPCB = pcbs.get(i);
            System.out.println(String.format(
                    "%-8d %-8d %-8.2f %-8.2f %-8.2f %-8.2f %-8.2f %-8.2f %-8.2f",
                    i, myPCB.getPid(),
                    myPCB.getArriveTime(),
                    myPCB.getStartTime(),
                    myPCB.getWaitTime(),
                    myPCB.getFinishTime(),
                    myPCB.getRunTime(),
                    myPCB.getTurnTime(),
                    myPCB.getWeightTime()));
        }
    }

}
