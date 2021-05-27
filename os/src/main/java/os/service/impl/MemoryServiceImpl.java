package os.service.impl;

import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.service.MemoryService;

/**
 * 内存服务
 */
public class MemoryServiceImpl implements MemoryService {

    /**
     * 显示位示图到控制台
     */
    public void showBitMap() {

    }

    /**
     * todo: 判断内存是否可以分配
     *
     * @param myJCB 传入当前需要分配内存的作业
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyJCB myJCB) {
        return true;
    }
    /**
     * todo: 判断内存是否可以分配
     * @param myPCB 传入当前需要分配内存的进程
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyPCB myPCB) {
        return true;
    }

    /**
     * todo: 分配内存的操作
     *
     * @param myPCB 需要分配内存的进程
     * @return 分配是否成功, 成功true, 失败false
     */
    public boolean allocation(MyPCB myPCB) {
        return false;
    }

    /**
     * 回收内存
     */
    public boolean remove(MyPCB myPCB) {
        return false;
    }

    /**
     * todo: 通过pcb回收内存
     * 1. 清空pcb的内容
     * 2. 将内存上的Process删除或移到外存中
     */
    public boolean removeByPcb(MyPCB myPCB) {
        return false;
    }
}
