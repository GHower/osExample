package schedule;

import config.OSConfig;
import memory.pagination.PaginationAllocation;
import memory.partition.PartitionAllocation;
import process.PCB;


/***
 *
 * 通过配置指定的内存分配方式（分区/分页）
 * 决定采用什么方式分配内存
 *
 */
public class MemorySchedule {
    // 调度方式分支
    public static boolean MemoryAllocate(PCB pcb) {
        boolean result;
        switch (OSConfig.MEMORY_ALLOCATE_FLAG) {
            case 0:
                result =  PartitionAllocation.allocate(pcb);
                break;
            default:
                result = PartitionAllocation.allocate(pcb);
                break;
        }
        return result;
    }

    // 调度方式分支
    public static void MemoryRelease(PCB pcb) {

        switch (OSConfig.MEMORY_ALLOCATE_FLAG) {
            case 0:
                PartitionAllocation.release(pcb);
                break;
            default:
                PaginationAllocation.release(pcb);
                break;
        }

    }


}
