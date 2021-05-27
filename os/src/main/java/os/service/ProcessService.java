package os.service;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;

import java.util.List;

/**
 * 进程调度服务
 */
public interface ProcessService {
    /**
     * 通过pid找到pcb
     */
    public MyPCB getJcbByPid(Integer pid);

    /**
     * 批量生成测试的Process,随机生成
     */
    public List<MyProcess> testProcess(int num);
}
