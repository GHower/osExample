package os.model.entity;

import lombok.Data;
import os.enums.MyStatus;

import java.time.LocalDateTime;

/**
 * PCB，其中时间单位全部用秒来模拟
 */
@Data
public class MyPCB {
    /**
     * 对应进程的PID,进程标识符,即 MyPCB.pid => MyProcess.id
     * 相当于指针，指向process,用来找到真正的进程实体
     */
    private Integer pid;
    /**
     * 状态，对应枚举的状态,同状态的放在同一队列，如就绪队列
     */
    private MyStatus status;

    /**
     * 优先级标识，可能用得上
     */
    private Integer priority;


    /**
     * 到达时间
     */
    private Double arriveTime;
    /**
     * 开始时间
     */
    private Double startTime;
    /**
     * 等待时间
     */
    private Double waitTime=0D;
    /**
     * 完成时间
     */
    private Double finishTime;
    /**
     * 运行时间，这是一个时间长度，时间单位为毫秒
     */
    private Double runTime;
    /**
     * 周转时间
     */
    private Double turnTime;
    /**
     * 带权周转时间
     */
    private Double weightTime;
    /**
     * 请求资源  目前只能三类资源{1,3,2}
     */
    private int[] request;



}
