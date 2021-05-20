package os.service;

import os.model.entity.MyPCB;

import java.util.List;

/**
 * 银行家算法服务接口
 */
public interface BankService {

    /**
     * @param pcbs 某时刻的进程数组，对资源请求
     * @return 逻辑值，存在一个安全序列就返回<code> true </code>
     */
    public boolean checkSafe(List<MyPCB> pcbs);
}
