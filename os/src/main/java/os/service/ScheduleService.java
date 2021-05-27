package os.service;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;

import java.util.List;

/**
 * 调度服务
 */
public interface ScheduleService {
    /**
     * TODO:先来先服务，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按到达时间优先
     * @return 先来先服务结果
     */
    public List<MyPCB> FCFS(List<MyPCB> pcbs);

    /**
     * TODO:短作业优先，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 短作业优先调整后的pcb数组
     */
    public List<MyPCB> SJF(List<MyPCB> pcbs);

    /**
     * TODO:高响应比优先，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public List<MyPCB> FPF(List<MyPCB> pcbs);

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(List<MyPCB> pcbs);



}
