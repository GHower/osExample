package os.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import os.enums.MyStatus;


import java.time.LocalDateTime;

/**
 * PCB，其中时间单位全部用秒来模拟
 * TODO: 将时间单位改用时间戳进行模拟，更加真实，功能完成后再做这个修改
 */
@Data
public class MyPCB {
    /**
     * 对应进程的PID,进程标识符,即 MyPCB.pid => MyProcess.id
     * 相当于指针，指向process,用来找到真正的进程实体
     */
    private Integer pid;
    /**
     * 进程名字
     */
    private String name;
    /**
     * 用来标识这个进程属于哪个作业。job id
     */
    private Integer jid;
    /**
     * 状态，对应枚举的状态,同状态的放在同一队列，如就绪队列
     */
    private MyStatus status;
    /**
     * 当前进程在内存中的起始地址
     */
    private Integer Mstart;
    /**
     * 当前进程在内存中的地址，也可以是虚拟地址
     */
    private Integer addr;
    /**
     * 当前进程大小
     */
    private Integer size;
    /**
     * 优先级标识，可能用得上
     */
    private Integer priority;

    // 下面是进程运行状态的一些记录
    /**
     * 到达时间
     */
    private Long arriveTime = 0L;
    /**
     * 开始时间
     */
    private Long startTime = 0L;
    /**
     * 等待时间
     */
    private Long waitTime = 0L;
    /**
     * 完成时间
     */
    private Long finishTime = 0L;
    /**
     * 运行时间，这是一个时间长度，时间单位为毫秒
     */
    private Long runTime = 0L;
    /**
     * 周转时间
     */
    private Long turnTime = 0L;
    /**
     * 带权周转时间
     */
    private Long weightTime = 0L;


}
