package os.service;

import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;

/**
 * 内存服务
 */
public interface MemoryService {

    /**
     * todo: 判断内存是否可以分配
     *
     * @param myJCB 传入当前需要分配内存的进程
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyJCB myJCB);
    public boolean hasAllocation(MyPCB myPCB);
    /**
     * todo: 分配内存的操作
     *
     * @param myProcess 需要分配内存的进程
     * @return 分配是否成功, 成功true, 失败false
     */
    public boolean allocation(MyProcess myProcess);
    /**
     * 查找内存空间中的进程
     *
     * @param pid 进程pid
     * @return 分配是否成功, 成功true, 失败false
     */
    public MyProcess getProcessByPid(Integer pid);
    /**
     * 回收内存
     */
    public boolean remove(MyPCB myPCB);

    /**
     * todo: 通过pcb回收内存
     * 1. 清空pcb的内容
     * 2. 将内存上的Process删除或移到外存中
     */
    public boolean removeByPcb(MyPCB myPCB);
}
