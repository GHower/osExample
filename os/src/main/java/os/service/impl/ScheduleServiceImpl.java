package os.service.impl;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;
import os.service.ScheduleService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调度服务，这里写所有的调度操作，包括 进程调度到不同队列等等
 * fixme: 时间数据应该由CPU回响过来，而不是手动输入
 * fixme: 步骤执行，每个时间单位执行一个。
 */
public class ScheduleServiceImpl implements ScheduleService {
    /**
     * 先来先服务
     * @param pcbs 需要计算的pcb数组,PCB中按到达时间优先
     * @return 先来先服务结果
     */
    public List<MyPCB> FCFS(List<MyPCB> pcbs) {
        // 先对输入数组排序
        List<MyPCB> result = pcbs.stream()
                .sorted(Comparator.comparingDouble(MyPCB::getArriveTime))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * TODO:短作业优先
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 短作业优先调整后的pcb数组
     */
    public List<MyPCB> SJF(List<MyPCB> pcbs) {
        /*List<MyPCB> list = null;
        if (pcbs != null || pcbs.size() > 0){
            list = pcbs.stream().sorted((e1,e2)->{
                        return (int) (e1.get()-e2.getRunTime());
                    }
            ).collect(Collectors.toList());
        }*/

//        return list;
        return null;
    }

    /**
     * TODO:高响应比优先
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public List<MyPCB> FPF(List<MyPCB> pcbs) {

        return null;
    }

    /**
     * TODO:时间片轮转算法
     *
     * @param pcbs 需要计算的pcb数组
     * @return 时间片轮转算法调整后的pcb数组
     */
    public List<MyPCB> RR(List<MyPCB> pcbs) {

        return null;
    }

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(List<MyPCB> pcbs) {
        System.out.println("进程pid\t提交时间\t开始时间\t等待时间\t完成时间\t运行时间\t周转时间\t带权周转时间");
        displayRunInfo(pcbs);
    }



    /**
     * 输出某个进程的运行信息
     */
    public void displayRunInfo(MyPCB myPCB){
        System.out.println(String.format(
                "%-7d %-7.2f %-7.2f %-7.2f %-7.2f %-7.2f %-7.2f %-7.2f",
                myPCB.getPid(),
                myPCB.getArriveTime(),
                myPCB.getStartTime(),
                myPCB.getWaitTime(),
                myPCB.getFinishTime(),
                myPCB.getRunTime(),
                myPCB.getTurnTime(),
                myPCB.getWeightTime()));
    }

    /**
     * 输出整个进程数组中所有进程的运行信息
     */
    public void displayRunInfo(List<MyPCB>  pcbs){
        for (MyPCB myPCB : pcbs) {
            displayRunInfo(myPCB);
        }
    }

    // 计算进程运行过程中的各种时间
    private void calculate(MyPCB pcbs){
//
//        for (int i = 0; i < pcbs.size(); i++) {
//            MyPCB myPCB = result.get(i);
//            // 第一项，直接计算
//            if (i == 0) {
//                myPCB.setStartTime(myPCB.getArriveTime());
//                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
//                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
//                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
//            } else if (myPCB.getArriveTime()>=pcbs.get(i-1).getFinishTime()){
//                // 到达时间在前一个进程执行完成之后，则该进程同第一个进程操作
//                myPCB.setStartTime(myPCB.getArriveTime());
//                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
//                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
//                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
//            }else{
//                // 开始时间将是上一个进程完成的时间
//                myPCB.setStartTime(pcbs.get(i-1).getFinishTime());
//                // 有等待时间
//                myPCB.setWaitTime(myPCB.getStartTime()-myPCB.getArriveTime());
//                myPCB.setFinishTime(myPCB.getStartTime()+myPCB.getRunTime());
//                myPCB.setTurnTime(myPCB.getFinishTime()-myPCB.getArriveTime());
//                myPCB.setWeightTime(myPCB.getTurnTime()/myPCB.getRunTime());
//            }
////            result.set(i,myPCB);
//        }
//        System.out.println(result);
    }


    /**
     * 根据进程PID找到 allocation   private List<MyResource> allocation;
     */
    @Override
    public List<MyResource> getAllocation(List<MyProcess> allocation, Integer id) {
        return  null;
    }

}
