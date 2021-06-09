package memory.partition;

import config.OSConfig;
import process.PCB;

public class PartitionAllocation {
    // 分区调度算法分支
    public static boolean allocate(PCB pcb) {
        boolean result = false;
        switch (OSConfig.PARTITION_ALGORITHM_FLAG) {
            case 0:
                result = FirstFit(pcb);
                break;
            case 1:
                result = NextFit(pcb);
                break;
            case 2:
                result = BestFit(pcb);
                break;
            case 3:
                result = WorstFit(pcb);
                break;

            default:
                result = FirstFit(pcb);
                break;
        }
        return result;
    }

    private static boolean WorstFit(PCB pcb) {
        return false;
    }

    private static boolean BestFit(PCB pcb) {
        return false;
    }

    private static boolean NextFit(PCB pcb) {
        return false;
    }
    // 首次适应算法
    // 从内存区内找到一个合适的块，为进程分配内存
    // 输入当前申请内存的PCB
    private static boolean FirstFit(PCB pcb) {
        int size = pcb.memory;
        // 对空闲链表进行首次适应
        Mem firstBlock = OSConfig.memory.blankList.getFirstBlock(size);
        if (firstBlock==null) {
            return false;
        }
        // 切出来的目标内存
        Mem target = OSConfig.memory.blankList.cut(firstBlock, size);
        OSConfig.memory.usedList.addLast(target);
        // 内存绑定
        pcb.addrStart = target.start;
        return true;
    }
    // 释放内存
    public static boolean release(PCB pcb) {
        Mem mem = OSConfig.memory.usedList.getMemoryByAddr(pcb.addrStart);
        if(mem == null) {
            //如果内存中不存在这个进程
            return false;
        }
        OSConfig.memory.usedList.remove(mem);
        OSConfig.memory.blankList.releaseAndAddBack(mem);
        return true;
    }
}
