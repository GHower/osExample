package os.service.impl;

import os.OSMain;
import os.enums.MyStatus;
import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;
import os.service.DispatchService;
import os.service.ProcessService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调度服务，这里写所有的调度操作，包括 进程切换到不同队列 等等
 * 就是说这里写的都是队列之间切换的方法
 * fixme: 时间数据应该由CPU回响过来，而不是手动输入
 * fixme: 步骤执行，每个时间单位执行一个。
 */
public class DispatchServiceImpl implements DispatchService {
    ProcessService processService = OSMain.processService;
    /**
     * 先来先服务 作业和进程都有
     *
     * @param pcbs 需要计算的pcb数组,PCB中按到达时间优先
     * @return 先来先服务结果
     */
    public LinkedList<MyJCB> FCFS(LinkedList<MyJCB> pcbs) {
        // 先对输入数组排序
        return pcbs.stream()
                .sorted(Comparator.comparingInt(MyJCB::getState)
                        .thenComparingDouble(MyJCB::getArriveTime))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * TODO:短作业优先，计算各个时间信息
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 短作业优先调整后的pcb数组
     */
    public LinkedList<MyPCB> SJF(LinkedList<MyJCB> pcbs) {
        return null;
    }

    public LinkedList<MyPCB> SPF(LinkedList<MyPCB> pcbs) {
        return pcbs.stream()
                .sorted(Comparator.comparingInt(MyPCB::getPriority).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * TODO: 进程高响应比优先，计算各个时间信息
     *
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public LinkedList<MyPCB> FPF(LinkedList<MyPCB> pcbs) {
        return null;
    }

    @Override
    public boolean jobDispatch() {
        // 先执行调度算法调整顺序
        LinkedList<MyJCB> fcfs = FCFS(OSMain.outsideQueue.get(MyStatus.BACK));
//        System.out.println("原后备队列：" + fcfs);
        // 取出队首
        MyJCB myJCB = fcfs.getFirst();
        // 按到达时间调度。
        if (OSMain.time >= myJCB.getArriveTime()) {
            if (OSMain.memoryService.hasAllocation(myJCB)
                    && OSMain.pcbPool.hasNext()
                    && myJCB.getState().equals(1)) {
                // 创建进程
                MyProcess myProcess = OSMain.processService.testProcess(myJCB);
//                System.out.println(myProcess);

                // 分配PCB
                MyPCB myPCB = OSMain.pcbPool.allocation(myProcess, myJCB);
                // 修改队首为2 running状态
                myJCB.setState(2);
                myJCB.setPid(myProcess.getId());

                // 将pcb放入就绪队列
                OSMain.innerQueue.get(MyStatus.READY).addFirst(myPCB);
                OSMain.outsideQueue.put(MyStatus.BACK, fcfs);
                // todo: 修改内存
                OSMain.memoryService.allocation(myProcess);

                return true;
            } else {
                if (!OSMain.memoryService.hasAllocation(myJCB)) {
                    System.out.println("内存不足");
                }
                if (!OSMain.pcbPool.hasNext()) {
                    System.out.println("PCB池中无可用PCB");
                }
                if (!myJCB.getState().equals(1)) {
                    System.out.println("没有作业等待中");
                }
            }
        }
        return false;
    }

    @Override
    public boolean proDispatch() {
        boolean result = false;
        LinkedList<MyPCB> spf = SPF(OSMain.innerQueue.get(MyStatus.READY));
        LinkedList<MyPCB> runList = OSMain.innerQueue.get(MyStatus.RUN);
        MyPCB running = runList.size() != 0 ? runList.getFirst() : null;
        MyPCB first = spf.size() != 0 ? spf.getFirst() : null;
        // cpu有执行中的进程
        if (running != null && first != null) {
            // 队首进程优先级大于运行中的进程
            if (first.getPriority() > running.getPriority()) {
                first.setStatus(MyStatus.RUN);
                // 将当前运行的进程放回就绪
                spf.addLast(running);
                // 将队首放入运行
                runList.removeFirst();
                runList.addLast(first);
                spf.removeFirst();
            } else {
                System.out.println("cpu繁忙，正在执行进程:" + running.getName());
            }
            result = true;
        } else if (runList.size() == 0 && first != null) {
            first.setStatus(MyStatus.RUN);
            runList.addLast(first);
            spf.removeFirst();
            result = true;
        } else {
            System.out.println("就绪队列为空无需调度");
        }
        OSMain.innerQueue.put(MyStatus.READY, spf);
        OSMain.innerQueue.put(MyStatus.RUN, runList);
        return result;
    }

    @Override
    public boolean blockDispatch() {
        LinkedList<MyPCB> runList = OSMain.innerQueue.get(MyStatus.RUN);
        MyPCB running = runList.getFirst();
        running.setStatus(MyStatus.WAIT);
        OSMain.innerQueue.get(MyStatus.WAIT).addLast(running);
        runList.removeFirst();
        // 阻塞后立即尝试调度
        return proDispatch();
    }

    @Override
    public boolean reReadyDispatch() {
        // 阻塞被消除的进程全部进入就绪，再次得到执行权时，重新申请资源，执行银行家
        LinkedList<MyPCB> waitList = OSMain.innerQueue.get(MyStatus.WAIT);
        List<MyPCB> collect = waitList.stream().filter(e -> {
            MyProcess process = processService.getProcessByPid(e.getPid());
            List<MyResource> need = process.getNeed();
            for (int i = 0; i < need.size(); i++) {
                // 有一个资源无法满足，当前进程继续等待
                if (need.get(i).getNumber() > OSMain.available[i]) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        System.out.println(collect);
        return collect.size()>0;
    }

    /**
     * TODO:时间片轮转算法
     *
     * @param pcbs 需要计算的pcb数组
     * @return 时间片轮转算法调整后的pcb数组
     */
    public LinkedList<MyPCB> RR(LinkedList<MyPCB> pcbs) {

        return null;
    }

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(LinkedList<MyPCB> pcbs) {
        System.out.println("进程pid\t提交时间\t开始时间\t等待时间\t完成时间\t运行时间\t周转时间\t带权周转时间");
        displayRunInfo(pcbs);
    }

    /**
     * 输出某个进程的运行信息
     */
    public void displayRunInfo(MyPCB myPCB) {
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
    public void displayRunInfo(List<MyPCB> pcbs) {
        for (MyPCB myPCB : pcbs) {
            displayRunInfo(myPCB);
        }
    }

    // 计算进程运行过程中的各种时间
    private void calculate(MyPCB pcbs) {
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


}
