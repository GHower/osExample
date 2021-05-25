package os.service;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyRequest;
import os.model.entity.MyResource;

import java.util.List;

/**
 * 银行家算法服务接口
 */
public interface BankService {

    /**
     * @param pcbs 某时刻的进程数组，对资源请求
     * @return 逻辑值，存在一个安全序列就返回<code> true </code>
     */
    public boolean checkSafe(List<MyPCB> pcbs, List<MyProcess> allocation);

    /**
     *
     * @param request 某一时刻的进程申请资源
     * @return 安全返回true
     */
    public boolean setRequest(MyRequest request);
}
