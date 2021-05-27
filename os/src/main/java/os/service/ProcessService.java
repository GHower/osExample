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
     * 根据进程PID找到 allocation   private List<MyResource> allocation;
     */
    public List<MyResource> getAllocation(List<MyProcess> allocation, Integer id);

}
