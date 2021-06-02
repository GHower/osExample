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
     * 通过pid从内存中找到进程
     */
    public MyProcess getProcessByPid(Integer pid);

    /**
     * 通过pid修改进程
     */
    public void putProcessByPid(MyProcess myProcess);

    /**
     * 生成测试的Process,随机生成
     */
    public List<MyProcess> testProcess();

    // 一个作业生成若干进程,简单一点，这里只做一个
    public MyProcess testProcess(MyJCB myJCB);
    /**
     *
     */
    public List<MyResource> testIncResource(List<MyResource> ups, List<MyResource> cur);
    /**
     * 通过pid获取pcb
     * @return
     */
    MyPCB getPcbByPid(Integer pid);

    /**
     * 生成资源请求
     */
    List<MyResource> testRequest(List<MyResource> ups, List<MyResource> cur);
    List<MyResource> testRequest(MyProcess myProcess);
    /**
     * 通过资源名称获取对应数量
     *
     * @return
     */
    int getRNumByRName(MyProcess process, String name);


}
