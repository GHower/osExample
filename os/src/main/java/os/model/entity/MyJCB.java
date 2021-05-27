package os.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 作业控制块
 * 用户提交的是作业，而进程是系统自动创建的
 * JCB通常包含作业标识，用户名称，用户账号，作业类型，作业状态，调度信息，资源需求，资源使用情况
 * 1. 作业的运行时间应该是所有所属进程的运行时间总和
 * 2. JCB的结构和PCB比较接近
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyJCB extends MyCB{
    /**
     * 作业的标识，
     */
    private Integer id;

    /**
     * 进程pid,这可能不止一个进程,暂时不做多个的情况
     */
    private Integer pid;

    /**
     * 作业名称
     */
    private String name;

    /**
     * 优先级标识，可能用得上
     */
    private Integer priority;
    /**
     * 发起作业的用户的用户名
     */
    private String username;

    /**
     * todo:作业的类型,目前统一为非交互式的普通类型，一个作业对应一个进程,且可以直接估计或输入运行时间
     */
    private Integer type;

    /**
     * 作业需要的资源集合
     */
    private List<MyResource> resources;
    // 下面是作业的运行情况,最后在做这部分
//    /**
//     * 到达时间
//     */
//    private Double arriveTime = 0D;
//    /**
//     * 开始时间
//     */
//    private Double startTime = 0D;
//    /**
//     * 等待时间
//     */
//    private Double waitTime = 0D;
//    /**
//     * 完成时间
//     */
//    private Double finishTime = 0D;
//    /**
//     * 运行时间，这是一个时间长度，时间单位为毫秒
//     */
//    private Double runTime = 0D;
//    /**
//     * 周转时间
//     */
//    private Double turnTime = 0D;
//    /**
//     * 带权周转时间
//     */
//    private Double weightTime = 0D;

}
