package schedule;


import common.BankAlgorithm;
import config.OSConfig;
import jobs.JCB;
import process.PCB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 处理机调度执行的其他操作 除了处理机调度算法之外，还需要执行一些其他的处理 比如：
 *
 * 1.检查就绪队列中是否存在内核进程，且内核进程准备就绪，这时应先把内核进程放入处理机
 *
 * 2.检查处理机中是否存在已经完成的进程，如果有，那么移除这些进程
 *
 * 3.查看阻塞队列中是否有进程已被激活，如果有的话，把它们调入就绪队列
 *
 * 4.将后备队列中的作业调入内存，创建进程
 *
 * 5.假设CPU执行一个时间片（就绪队列/后备队列 等待时间＋1、运行的进程运行时间＋1、执行相应功能模块）
 *
 * 维持一个队列 利用队列完成各种算法
 */
public class CPUSchedule {

    // 在调度之前的一些预处理
    public static void CPURun() {
        if (OSConfig.CPU_STATE == 1) {
            CPUSchedule.removeFinishTask(); // 移除已经结束的进程
            CPUSchedule.KernelTaskMoving(); // 检查是否有实时进程调入处理机
            CPUSchedule.block2ready(); // 阻塞队列内被激活的进程调入就绪队列
            // 作业调度，分配内存创建进程
            JobScheduling.jobScheduling();
            // 处理机(进程)调度算法
            CPUScheduleAlgorithm.CPUSchedule();
            Running(); // 模拟处理机运行一个时间片段
            // 系统时间调整
            OSConfig.System_time += (100 * OSConfig.TIMEPIECE) / 1000.0;
            try {
                new Thread().sleep(100 * OSConfig.TIMEPIECE); // 假设处理机一个时间片大小为TIMEPIECE
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 当就绪队列中存在内核进程时,需要把内核进程调入处理机
    public static void KernelTaskMoving() {
        // 当就绪队列中存在内核进程时且内核进程active
        if (OSConfig.ready_list.getByPid(0) != null) {
            PCB kernel = OSConfig.ready_list.getByPid(0);
            OSConfig.ready_list.remove(kernel);
            if (OSConfig.running_list.isFull()) {
                // 如果运行队列满了，需要腾出位置给内核进程
                // 删除最先进入的那个进程，放到就绪队列头部
                PCB tem = OSConfig.running_list.removeFirst();
                OSConfig.running_list.addFirst(kernel);
                OSConfig.ready_list.addFirst(tem);
            } else {// 如果运行队列没满，直接调入
                OSConfig.running_list.addFirst(kernel);
            }
            System.out.println("内核进程" + kernel.process.name + " 进入 CPU");
        }
    }

    // 移除处理机内已经运行完毕的进程
    public static void removeFinishTask() {
        // 检测有没有进程运行完毕
        while (true) {
            PCB pcb = OSConfig.running_list.removeFinished();
            if (pcb == null)
                return;
            // 从运行队列删除该进程
            OSConfig.running_list.remove(pcb);
            // 释放该进程占用的内存
            MemorySchedule.MemoryRelease(pcb);
            // 释放该进程占用的资源
            releaseResource(pcb);
            // 通知作业完成
            if (pcb.type != 0) {
                finishJob(pcb);
            }
            System.out.println("进程(" + pcb.PID + "): --" + pcb.process.name + " 完成了!");
        }
    }

    // 回响通知作业完成
    public static void finishJob(PCB pcb) {
        JCB finish = OSConfig.JCB_MAP_RUNNING.remove(pcb.jid);
        finish.finishTime = OSConfig.System_time;
        finish.runTime = finish.finishTime - finish.startTime;
        OSConfig.JCB_MAP_FINISHED.put(finish.JID, finish);
    }

    // 阻塞回就绪,todo: 阻塞类型
    public static void block2ready() {
        // 阻塞队列中有进程在等待,并且阻塞状态被激活
        if (!OSConfig.block_list.isEmpty()) {
            //阻塞进程中有进程可以被激活
            List<PCB> pcbs = OSConfig.block_list.resetBlockByResource();
            // todo: 因I/O导致阻塞的数组,然后合并去重
            // 遍历并激活这些
            pcbs.forEach(BlockActivate::activate);
        }
    }

    //    // 重置因资源导致阻塞的进程标记
    // 过了一个时间片
    public static void Running() {
        PCB existRequest = OSConfig.running_list.getExistRequest();
        if (existRequest != null) {
            //检查这个请求
            if (checkRequest(existRequest)) {
//                System.out.println("请求通过");
                HashMap<String, Integer> request = existRequest.process.request;
                for (String key : request.keySet()) {
                    Integer sys = OSConfig.available.get(key);
                    Integer cur = existRequest.process.allocation.get(key);
                    Integer inc = request.get(key);
                    // 系统资源减少
                    OSConfig.available.put(key, sys - inc);
                    // 进程占有资源上升
                    existRequest.process.allocation.put(key, cur + inc);
                }
                // 时间小于3,让请求结束
                if (existRequest.time_needed - existRequest.process.time_used < 2) {
                    existRequest.process.request = null;
                } else {
                    existRequest.process.request = CreatTestProcess.createRequest(existRequest);
                }
            } else {
                System.out.println("请求不通过，要阻塞它");
                Map<String, Integer> available = OSConfig.available;
                BlockActivate.block(existRequest);
            }
            // 不是测试进程，可能要执行命名
            if (existRequest.type == 1 && existRequest.process.command_type != -1) {
                existRequest.process.run();
            }
        }

        // 首先CPU内所有进程运行一个时间片
        OSConfig.running_list.timeUsedPlus();
        // 然后就绪队列中所有进程等待时间增加一个时间
        OSConfig.ready_list.timeWaitedPlus();
        // 然后后备队列中所有作业等待时间增加一个时间
        OSConfig.jobsList.timeWaitedPlus();
    }

    // 检查运行队列中进程资源请求
    public static boolean checkRequest(PCB existRequest) {
        int b = 2;// 是否使用银行家的触发阈值,倍数
        // 资源请求
        HashMap<String, Integer> request = existRequest.process.request;

        for (String key : request.keySet()) {
            // 有一个资源不能远大于本次请求
            if (OSConfig.available.get(key) < request.get(key) * b) {
                return BankAlgorithm.checkSafe(existRequest);
            }
        }
        return true;
    }


    public static void releaseResource(PCB pcb) {
        Set<String> keySet = pcb.process.allocation.keySet();
        for (String key : keySet) {
            Integer inc = pcb.process.allocation.get(key);
            OSConfig.available.put(key, inc + OSConfig.available.get(key));
        }
    }

}
