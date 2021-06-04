package os.service.impl;

import os.OSMain;
import os.enums.MyStatus;
import os.model.entity.*;
import os.service.MemoryService;
import os.service.ProcessService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * 进程服务，提供针对进程的操作方法，不过主要是对pcb的操作
 */
public class ProcessServiceImpl implements ProcessService {

    @Override
    public MyJCB getJcbByPid(Integer pid) {
        return null;
    }

    @Override
    public MyProcess getProcessByPid(Integer pid) {
        return OSMain.memoryService.getProcessByPid(pid);
    }

    @Override
    public List<MyProcess> getAllProcess() {
        return OSMain.memoryService.getAllProcess();
    }

    @Override
    public void putProcessByPid(MyProcess myProcess) {
        OSMain.memoryService.putProcessByPid(myProcess);
    }

    @Override
    public List<MyProcess> testProcess() {
        List<MyProcess> result = new LinkedList<>();
        int n = 3;
        // 生成n个进程
        for (int i = 1; i <= OSMain.innerQueue.get(MyStatus.READY).size(); i++) {
            MyProcess myProcess = new MyProcess();
            myProcess.setName("P" + i);
            myProcess.setId(i);
            //fixme: allocation不断增加，need不断修改
//            if (i % 3 != 0) {
            myProcess.setMax(testResource(n));
//                myProcess.setAllocation(testResource(myProcess.getMax()));
            // 部分进程某时刻不申请资源
//                myProcess.setNeed(testResource(myProcess.getAllocation()));
            myProcess.setAllocation(testIncResource(myProcess.getMax(), initResource(n)));
            myProcess.setRequests(testRequest(myProcess.getMax(), myProcess.getAllocation()));
//            }
            result.add(myProcess);
        }
        return result;
    }

    @Override
    public MyProcess testProcess(MyJCB myJCB) {
        // 申请的资源类数
        int n = 3;
        MyProcess myProcess = new MyProcess();
        myProcess.setName("P" + myJCB.getId());
        myProcess.setId(OSMain.pcbPool.nextPid());
        myProcess.setMax(testResource(testResource(n)));
        // 测试为看出效果 allocation 初值不设置为0
        myProcess.setAllocation(initResource(n));
//        List<MyResource> allocation = testIncResource(myProcess.getMax(), initResource(n));
//        // 这里是没有经过银行家检测的
//        IntStream.range(0, allocation.size()).forEach(i -> {
//            OSMain.available[i] -= allocation.get(i).getNumber();
//        });
//        myProcess.setAllocation(allocation);
        myProcess.setRequests(testRequest(myProcess.getMax(), myProcess.getAllocation()));
        return myProcess;
    }

    @Override
    public List<MyResource> testRequest(List<MyResource> max, List<MyResource> allocation) {
        List<MyResource> request = IntStream.range(0, max.size()).mapToObj(i -> {
            MyResource myResource = new MyResource();
            myResource.setName(max.get(i).getName());
            myResource.setNumber((int) Math.round(Math.random() * (max.get(i).getNumber() - allocation.get(i).getNumber())));
            return myResource;
        }).collect(Collectors.toList());
        int sum = request.stream().mapToInt(MyResource::getNumber).sum();
        return sum > 0 ? request : null;
    }

    @Override
    public List<MyResource> testRequest(MyProcess myProcess) {
        return testRequest(myProcess.getMax(), myProcess.getAllocation());
    }

    @Override
    public MyPCB getPcbByPid(Integer pid) {
        return OSMain.pcbPool.getPcbByPid(pid);
    }

    @Override
    public int getRNumByRName(MyProcess process, String name) {
        List<MyResource> requests = process.getRequests();
        if (requests != null) {
            MyResource myResource = requests.stream().filter(e -> e.getName().equals(name)).findFirst().get();
            return myResource.getNumber();
        }
        return 0;
    }

    // 生成若干个资源,无限制的任意生成
    private List<MyResource> testResource(int n) {
        List<MyResource> result = new ArrayList<>();
        // 生成n个进程
        for (int i = 1; i <= n; i++) {
            MyResource myResource = new MyResource();
            myResource.setName("R" + i);
            myResource.setNumber((int) (Math.random() * 5));
            result.add(myResource);
        }
        return result;
    }

    // 生成若干个资源,含限制，即随机生成的请求量会被参数所限制
    private List<MyResource> testResource(List<MyResource> resources) {
        List<MyResource> result = new ArrayList<>();
        for (MyResource myResource : resources) {
            myResource.setNumber((int) Math.round(Math.random() * myResource.getNumber()));
            result.add(myResource);
        }
        return result;
    }

    // 随机增量生成若干个资源,cur是需要增量的资源数组,ups是上限
    // 这个方法没啥用。。先放着可能用得上
    public List<MyResource> testIncResource(List<MyResource> ups, List<MyResource> cur) {
        List<MyResource> result = new ArrayList<>();
        // 生成n个进程
        for (int i = 0; i < cur.size(); i++) {
            MyResource myResource = cur.get(i);
//            myResource.setName("R" + i+1);
            int min = myResource.getNumber();
            int max = ups.get(i).getNumber();
            myResource.setNumber(min + (int) (Math.random() * (max - min + 1)));
            result.add(myResource);
        }
        return result;
    }

    private List<MyResource> initResource(int n) {
        List<MyResource> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            MyResource myResource = new MyResource();
            myResource.setName("R" + i);
            myResource.setNumber(0);
            result.add(myResource);
        }
        return result;
    }
}
