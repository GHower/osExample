package os.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 进程类
 */
@Data
public class MyProcess {
    /**
     * 进程标识，PID
     */
    private Integer id;
    /**
     * 进程名,方便我们使用，如P1,P2,P3
     */
    private String name;
    /**
     * 进程所需的最大资源数组
     */
    private List<MyResource> max;
    /**
     * 进程已分配的资源数组
     */
    private List<MyResource> allocation;
    /**
     * 进程剩余所需的资源数组
     */
    private List<MyResource> need;

    /**
     * 进程某时刻请求的资源，本次资源请求被分配后这个值置为null,阻塞不改变这个值
     * 1. 作为阻塞条件,用于银行家
     * 2. 用于解除阻塞
     */
    private List<MyResource> requests;

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyProcess myProcess = (MyProcess) o;
        return id.equals(myProcess.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }*/
}
