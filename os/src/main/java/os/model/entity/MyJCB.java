package os.model.entity;

import lombok.Data;

@Data
public class MyJCB {

    /**
     * 进程pid
     */
    private Integer pid;

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

}
