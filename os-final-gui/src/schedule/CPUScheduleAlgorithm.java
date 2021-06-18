package schedule;


import config.OSConfig;
import jobs.JCB;
import process.PCB;

/***
 * 处理机调度算法
 * 也就是说在某个时刻可允许多个进程并行处理 （FCFS、SPN、HPF、SHPF、HRRN）
 *
 */

public class CPUScheduleAlgorithm {
    //作业调度
    public static void CPUSchedule() {
        switch (OSConfig.CPU_SCHEDULE_FLAG) {
            case 0:
                FCFS();
                break;
            case 1:
                SPF();
                break;
            case 2:
                HPF();
                break;
            case 3:
                SHPF();
                break;
            case 4:
                HRRN();
                break;
            case 5:
                RR();
                break;
            default:
                FCFS();
                break;
        }


    }


    // 先来先服务，哪个先到先给哪个服务
    public static void FCFS() {
        // 运行队列有空闲，且就绪队列中有进程在等待
        while (!OSConfig.running_list.isFull() && !OSConfig.ready_list.isEmpty()) {
            PCB pcb = OSConfig.ready_list.removeFirst();
            JCB jcb = OSConfig.JCB_MAP_RUNNING.get(pcb.jid);
            OSConfig.running_list.addLast(pcb);

            pcb.process.time_waited = 0;
        }
    }

    // 短进程优先(Short Process First)，用时最短的进程先被服务
    public static void SPF() {

    }

    // 非抢占式－高优先权调度(Highest Priority First)，找到最高优先级的进程
    public static void HPF() {


    }

    // 抢占式－高优先级调度(Seize-Highest Priority First)，找到最高优先级的进程
    public static void SHPF() {
        // 就绪队列中有进程在等待
        while (!OSConfig.ready_list.isEmpty()) {
            // 找到优先级最高的进程
            PCB ready = OSConfig.ready_list.getMaxPriority();
            if (ready == null)
                break;
            if (!OSConfig.running_list.isFull()) {// 运行队列有空闲
                OSConfig.ready_list.remove(ready);
                OSConfig.running_list.addLast(ready);
                ready.process.time_waited = 0;
            } else {// 运行队列无空闲，则找到运行队列中优先级低的进程
                PCB running = OSConfig.running_list.getMinPriority();
                if (running == null)
                    break;
                if (running.priority < ready.priority) {
                    OSConfig.running_list.remove(running);
                    OSConfig.ready_list.remove(ready);
                    OSConfig.running_list.addLast(ready);
                    OSConfig.ready_list.addLast(running);
                } else {
                    break;
                }
            }
        }
    }

    // 最高响应比优先调度(Highest Response Ratio Next)，找到最高响应比的进程
    // 响应比R定义如下： R =(W+T)/T = 1+W/T
    public static void HRRN() {

    }

    // 时间片轮转调度(Round-Robin)
    public static void RR() {

    }

}
