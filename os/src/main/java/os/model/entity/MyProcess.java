package os.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
     * 进程所需的资源数组
     */
    private List<MyResource> resources;
}
