package os.model.entity;

import lombok.Data;


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

    /**
     * 内存状态
     * true:  空闲内存
     * false: 内存被占用
     */
    boolean state;
}
