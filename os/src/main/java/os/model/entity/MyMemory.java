package os.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class MyMemory {
    /**
     * 内存起始地址
     */
    Integer address;
    /**
     * 内存大小
     */
    Integer size;
    /**
     * 最大内存
     */
    Integer max;
}
