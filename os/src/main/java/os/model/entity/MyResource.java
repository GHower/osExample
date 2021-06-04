package os.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源类,指的是最大申请资源
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyResource {
    /**
     * 资源的名称,R1,R2,R3
     */
    private String name;

    /**
     * 使用的数量
     */
    private Integer number;

    @Override
    public String toString() {
        return name+"( "+number+" )";
    }
}
