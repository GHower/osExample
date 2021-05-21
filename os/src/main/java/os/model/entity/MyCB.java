package os.model.entity;

import lombok.Data;

/**
 * 控制块的父类,
 * 1. JCB:作业控制块
 * 2. PCB:进程控制块
 * 3. FCB:文件控制块
 */
@Data
public class MyCB {
    private Integer id;
}
