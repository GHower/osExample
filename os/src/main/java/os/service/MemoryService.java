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
     *
     * @param myPCB@return 分配是否成功, 成功true, 失败false
     */
    public MyPCB allocation(MyPCB myPCB,MyProcess myProcess);
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
     * 内存紧凑
     */
    public void compact();

    public void display();
    public void display2();
    public void display3();
}
