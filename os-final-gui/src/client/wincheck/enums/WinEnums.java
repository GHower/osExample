package client.wincheck.enums;

/**
 * 窗口枚举
 */
public enum WinEnums {
    SYS_STATUS("系统状态"),
    CPU_SCHEDULE("CPU调度算法"),
    MEMORY_DISK_ALLOCATION("内存/硬盘分配策略"),
    FILE_SYSTEM("文件系统"),
    TERMINAL("模拟终端");
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    WinEnums(String name) {
        this.name = name;
    }
}
