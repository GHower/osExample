package os.service.impl;

import os.model.entity.MyJCB;
import os.model.entity.MyMemory;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.service.MemoryService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内存服务
 */
public class MemoryServiceImpl implements MemoryService {


    List<MyProcess> myProcesses = new ArrayList<>();
    LinkedList<MyMemory> idleLinked = new LinkedList<>();

    /**
     * todo: 判断内存是否可以分配给作业
     *
     * @param myJCB 传入当前需要分配内存的作业
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyJCB myJCB) {
        return true;
    }

    /**
     * todo: 判断内存是否可以分配
     *
     * @param myPCB 传入当前需要分配内存的进程
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyPCB myPCB) {
        return true;
    }

    /**
     * 分配内存的操作
     *
     * @param myProcess 需要分配内存的进程PCB
     * @return 分配是否成功, 成功true, 失败false
     */
    public boolean allocation(MyProcess myProcess) {
        // 记录该进程
        myProcesses.add(myProcess);
        // todo: 调整内存对象中的空闲链表
        return true;
    }

    @Override
    public MyProcess getProcessByPid(Integer pid) {
        List<MyProcess> collect = myProcesses.stream()
                .filter(e -> e.getId().equals(pid))
                .collect(Collectors.toList());
        return collect.size() > 0 ? collect.get(0) : null;
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
