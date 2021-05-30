package os.service.impl;

import os.OSMain;
import os.enums.MyStatus;
import os.model.entity.*;
import os.service.MemoryService;
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
 * 进程服务，提供针对进程的操作方法，不过主要是对pcb的操作
 */
public class ProcessServiceImpl implements ProcessService {
    MyPCBPool myPCBPool = OSMain.pcbPool;
    MemoryService memoryService = OSMain.memoryService;
    @Override
    public MyJCB getJcbByPid(Integer pid) {
        return null;
    }

    @Override
    public MyProcess getProcessByPid(Integer pid) {
        return memoryService.getProcessByPid(pid);
    }

    @Override
    public List<MyProcess> testProcess() {
        List<MyProcess> result = new LinkedList<>();
        // 生成n个进程
        for(int i = 1; i<= OSMain.innerQueue.get(MyStatus.READY).size(); i++){
            MyProcess myProcess = new MyProcess();
            myProcess.setName("p"+i);
            myProcess.setId(i);
            //fixme: need不断修改
            if(i%3!=0){
                myProcess.setMax(testResource(3));
                myProcess.setAllocation(testResource(3,myProcess.getMax()));
                // 部分进程某时刻不申请资源
                myProcess.setNeed(testResource(3,myProcess.getAllocation()));
            }
            result.add(myProcess);
        }
        return result;
    }

    @Override
    public MyProcess testProcess(MyJCB myJCB) {
        MyProcess myProcess = new MyProcess();
        myProcess.setName("p"+myJCB.getId());
        myProcess.setId(OSMain.pcbPool.size()+1); //
        myProcess.setMax(testResource(2));
        return myProcess;
    }


    @Override
    public MyPCB getPcbByPid(Integer pid) {
        return null;
    }

    @Override
    public int getRNumByRName(MyProcess process, String name) {
        List<MyResource> requests = process.getRequests();
        if(requests !=null){
            MyResource myResource = requests.stream().filter(e -> e.getName().equals(name)).findFirst().get();
            return myResource.getNumber();
        }
        return 0;
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
