package os.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyPCB {
    /**
     * 对应进程的PID,即 MyPCB.pid => MyProcess.id
     */
    private Integer pid;
    /**
     * 状态
     */
    private String status;

    /**
     * 到达时间
     */
    private LocalDateTime arriveTime;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 等待时间
     */
    private LocalDateTime waitTime;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 运行时间
     */
    private LocalDateTime runTime;
    /**
     * 周转时间
     */
    private LocalDateTime turnTime;
    /**
     * 带权周转时间
     */
    private LocalDateTime weightTime;

}
