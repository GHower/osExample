package os.service.impl;

import os.OSMain;
import os.enums.MyStatus;
import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;
import os.service.ProcessService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 进程服务，提供针对进程的操作方法
 */
public class ProcessServiceImpl implements ProcessService {
    @Override
    public MyJCB getJcbByPid(Integer pid) {
        return null;
    }

    @Override
    public List<MyProcess> testProcess() {
        List<MyProcess> result = new LinkedList<>();
        // 生成n个进程
        for(int i = 1; i<= OSMain.innerQueue.get(MyStatus.READY).size(); i++){
            MyProcess myProcess = new MyProcess();
            myProcess.setName("p"+i);
            myProcess.setId(i);
            myProcess.setMax(testResource(2));
            myProcess.setAllocation(testResource(2,myProcess.getMax()));
//            myProcess.setNeed(testResource(2,myProcess.getAllocation()));
            result.add(myProcess);
        }
        return result;
    }

    @Override
    public MyProcess testProcess(MyJCB myJCB) {

        return null;
    }

    // 生成若干个资源
    private List<MyResource> testResource(int n){
        List<MyResource> result = new ArrayList<>();
        // 生成n个进程
        for(int i=1;i<=n;i++){
            MyResource myResource = new MyResource();
            myResource.setName("r"+i);
            myResource.setNumber((int) (Math.random()*5));
            result.add(myResource);
        }
        return result;
    }
    // 生成若干个资源,含限制
    private List<MyResource> testResource(int n,List<MyResource> resources){
        List<MyResource> result = new ArrayList<>();
        // 生成n个进程
        for(int i=1;i<=n;i++){
            MyResource myResource = new MyResource();
            myResource.setName("r"+i);

            myResource.setNumber((int) (Math.random()*5));
            result.add(myResource);
        }
        return result;
    }
}
