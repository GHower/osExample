package os.service;

import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;

import java.util.List;

/**
 * 进程相关服务
 */
public interface ProcessService {
    /**
     * 通过pid反向找到jcb
     */
    public MyJCB getJcbByPid(Integer pid);

    /**
     * 生成测试的Process,随机生成
     */
    public List<MyProcess> testProcess();
    // 一个作业生成若干进程,简单一点，这里只做一个
    public MyProcess testProcess(MyJCB myJCB);

    /**
     * 通过pid获取pcb
     * @return
     */
    MyPCB getPcbByPid(Integer pid);
}
