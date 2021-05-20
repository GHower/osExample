package os.model.entity;

import lombok.Data;

/**
 * 资源类,指的是最大申请资源
 */
@Data
public class MyResource {
    /**
     * 资源的名称,R1,R2,R3
     */
    private String name;

    /**
     * 使用的数量
     */
    private Integer number;
}
