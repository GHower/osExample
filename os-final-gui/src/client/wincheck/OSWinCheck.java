package client.wincheck;

import client.views.PartitionWindow;
import client.wincheck.enums.WinEnums;
import config.OSConfig;

/**
 * 窗口系统检查,
 */
public class OSWinCheck {
    public OSWinCheck(WinEnums winEnums){
        switch (winEnums){
            case SYS_STATUS:
                if(OSConfig.MEMORY_ALLOCATE_FLAG==0){
                    // 分区存储
                    new PartitionWindow();
                }else {
                    // 分页存储
                }
                break;
            case CPU_SCHEDULE:
                break;
            case MEMORY_DISK_ALLOCATION:
                break;
            case FILE_SYSTEM:
                break;
            case TERMINAL:
                break;
        }
    }
}
