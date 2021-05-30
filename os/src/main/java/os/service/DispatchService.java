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
    public LinkedList<MyPCB> SJF(LinkedList<MyJCB> pcbs);
    public LinkedList<MyPCB> SPF(LinkedList<MyPCB> pcbs);

    /**
     * TODO:高响应比优先，计算各个时间信息
     * @param pcbs 需要计算的pcb数组,PCB中按运行时间优先
     * @return 高响应比优先调整后的pcb数组
     */
    public LinkedList<MyPCB> FPF(LinkedList<MyPCB> pcbs);

    /**
     * 作业调度，执行一次进行以下步骤
     * 1. 按调度算法取出队首作业,这个作业是wait状态
     * 2. 为该作业
     *      2.1 创建进程
     *      2.2 分配PCB
     * 3. 修改作业的pid指向、状态，进程和pcb自动产生联系
     */
    public boolean jobDispatch();

    /**
     * 进程调度，放入cpu中
     * 1. 通过 优先级 调度算法
     * @return
     */
    public boolean proDispatch();

    /**
     * 运行时出现阻塞
     * @return
     */
    public boolean blockDispatch();

    /**
     * 阻塞进程回到就绪
     * @return
     */
    public boolean reReadyDispatch();

    /**
     * 打印输出传入的pcbs数组
     * todo: 重载多种输出格式
     */
    public void display(LinkedList<MyPCB> pcbs);

}
