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



}
