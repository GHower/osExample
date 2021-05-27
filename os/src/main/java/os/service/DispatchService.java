package os.service;

import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;

import java.util.LinkedList;
import java.util.List;

/**
 * 调度服务
 */
public interface DispatchService {
    /**
     * TODO:先来先服务，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按到达时间优先
     * @return 先来先服务结果
     */
    public LinkedList<MyJCB> FCFS(LinkedList<MyJCB> pcbs);

    /**
     * TODO:短作业优先，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 短作业优先调整后的pcb数组
     */
    public LinkedList<MyPCB> SJF(LinkedList<MyPCB> pcbs);

    /**
     * TODO:高响应比优先，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public LinkedList<MyPCB> FPF(LinkedList<MyPCB> pcbs);

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(LinkedList<MyPCB> pcbs);



}
