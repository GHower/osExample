package os.service.impl;

import os.model.entity.MyPCB;
import os.model.entity.MyResource;
import os.service.BankService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 银行家算法实现类
 */
public class BankServiceImpl implements BankService {

    @Override
    public boolean checkSafe(List<MyPCB> pcbs) {

        List<MyResource> myResources = new ArrayList<>();

        return false;
    }
}
