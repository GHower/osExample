package os.service.impl;

import os.model.entity.MyPCB;
import os.service.BankService;

import java.util.List;

/**
 * 银行家算法实现类
 */
public class BankServiceImpl implements BankService {

    @Override
    public boolean checkSafe(List<MyPCB> pcbs) {
        return false;
    }
}
