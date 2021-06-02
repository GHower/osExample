package os;


import os.enums.MyStatus;
import os.model.entity.*;
import os.service.*;
import os.service.impl.*;
import os.utils.Algorithm;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 服务和队列都置为静态，实际上这是内核的东西，这里做模拟就不管了
 * 先写个屎山出来
 */
public class OSMain {
    public static Map<MyStatus, LinkedList<MyPCB>> innerQueue = null; // 模拟内存
    public static Map<MyStatus, LinkedList<MyJCB>> outsideQueue = null; // 模拟外存
    public static MyPCBPool pcbPool = null;// pcb池
    public static long time = 0;//当前系统时刻,用时间戳表示
    public static Integer MEMORY_MAX_SIZE = 1024 * 1024 * 1024; // 最大内存
    //    public static List<MyResource> available = null;// 系统可用资源
    public static int[] available = {10, 10, 9};// 系统可用资源

    public static JobService jobService = new JobServiceImpl();// 与作业相关的服务
    public static ProcessService processService = new ProcessServiceImpl();// 进程相关的服务
        public static BankService bankService = new BankServiceImpl();// 银行家算法
    public static DispatchService dispatchService = new DispatchServiceImpl();// 调度相关的服务
    public static MemoryService memoryService = new MemoryServiceImpl();// 内存相关的服务

    public static void main(String[] args) {
        OSMain osMain = new OSMain();
        osMain.init();// 初始化、系统参数
        osMain.start();// 运行函数
    }

    /**
     * 在这里写主程序代码，懒得弄静态了
     */
    public void start() {
//        testBank();
        // 生成测试的jcb 更新 后备作业 队列
        outsideQueue.put(MyStatus.BACK, jobService.testJCB(6));
        do {
            timeNext();
            System.out.println("按回车键继续!");
            new Scanner(System.in).nextLine();
        } while (true);
    }
    /**
     * 阿全的银行家测试
     */
    void testBank(){
        //        System.out.println("=======阻塞产生========");
//        dispatchService.blockDispatch();
//        System.out.println("就绪队列：" + innerQueue.get(MyStatus.READY));
//        System.out.println("运行队列：" + innerQueue.get(MyStatus.RUN));
//        System.out.println("等待队列：" + innerQueue.get(MyStatus.WAIT));

//        System.out.println("=======内存情况========");
//        memoryService.display3();
//        System.out.println("=======移除内存后========");
//        // 从阻塞队列中移除一个做测试
//        MyPCB first = OSMain.innerQueue.get(MyStatus.WAIT).removeFirst();
//        memoryService.remove(first);
//        memoryService.display3();

//        List<MyProcess> myProcesses = new ArrayList<>();
//        List<MyPCB> myPCBS = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            List<MyResource> myResourceList = new ArrayList<>();
//            List<MyResource> myResourceList1 = new ArrayList<>();
//            MyPCB myPCB = new MyPCB();
//            MyProcess myProcess = new MyProcess();
//            myPCB.setPid(i);
//            myProcess.setId(i);
//            for (int j = 0; j < 3; j++) {
//                MyResource myResource = new MyResource();
//                myResource.setName("r" + j);
//                myResource.setNumber(4);
//                myResourceList.add(myResource);
//            }
//            for (int j = 0; j < 3; j++) {
//                MyResource myResource = new MyResource();
//                myResource.setName("r" + j);
//                myResource.setNumber(6);
//                myResourceList1.add(myResource);
//            }
//            myProcess.setAllocation(myResourceList);
//            myProcess.setMax(myResourceList1);
//            myPCBS.add(myPCB);
//            myProcesses.add(myProcess);
//        }
//
//        System.out.println(bankService.checkSafe(myPCBS, myProcesses));
//        for (int i = 0; i < available.length; i++) {
//            System.out.println(available[i]);
//        }
//        MyRequest myRequest = new MyRequest();
//        myRequest.setId(0);
//
//        List<MyResource> myResourceList = new ArrayList<>();
//        for (int j = 0; j < 3; j++) {
//            MyResource myResource = new MyResource();
//            myResource.setName("p" + j);
//            myResource.setNumber(3);
//            myResourceList.add(myResource);
//        }
//        myRequest.setRequest(myResourceList);
//        System.out.println(bankService.setRequest(myRequest));

/*        MyRequest myRequest1 = new MyRequest();
        myRequest1.setId(3);
        List<MyResource> myResourceList1 = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            MyResource myResource = new MyResource();
            myResource.setName("p"+j);
            myResource.setNumber(3);
            myResourceList1.add(myResource);
        }
        myRequest1.setRequest(myResourceList1);
        System.out.println(bankService.setRequest(myRequest1));*/


//        for (int i = 0; i < available.length; i++) {
//            System.out.println(available[i]);
//        }
    }
    /**
     * 初始化
     * 内存中的 就绪、运行、等待、完成 队列
     * 外存中的 后备、挂起 队列
     * 由于不做磁盘的东西，所以通过hashmap做模拟就行
     */
    public void init() {
        innerQueue = new HashMap<>(); // 内存相关队列
        outsideQueue = new HashMap<>(); // 外存相关队列 map
        pcbPool = new MyPCBPool(10); // pcb池 todo: 大小未定

        LinkedList<MyJCB> backList = new LinkedList<>();

        LinkedList<MyPCB> readyList = new LinkedList<>();
        LinkedList<MyPCB> runList = new LinkedList<>();
        LinkedList<MyPCB> waitList = new LinkedList<>();
        LinkedList<MyPCB> finishList = new LinkedList<>();

        outsideQueue.put(MyStatus.BACK, backList);
        innerQueue.put(MyStatus.READY, readyList);
        innerQueue.put(MyStatus.RUN, runList);
        innerQueue.put(MyStatus.WAIT, waitList);
        innerQueue.put(MyStatus.FINISH, finishList);
    }

    /**
     * todo： 进入下一时间单位
     */
    void timeNext() {
        // 每个时间都尝试将等待的作业放入内存
        while (dispatchService.jobDispatch()) ;
        dispatchService.proDispatch();
        runProcess();
        System.out.println("=======当前时刻:[time=" + time + "]=========");
        System.out.println("后备队列：" + OSMain.outsideQueue.get(MyStatus.BACK));
        System.out.println("就绪队列：" + OSMain.innerQueue.get(MyStatus.READY));
        System.out.println("运行队列：" + innerQueue.get(MyStatus.RUN));
        System.out.println("阻塞队列：" + innerQueue.get(MyStatus.WAIT));
        time++;
    }

    /**
     * todo: 强行中断运行中的进程
     * 1. 释放占有的资源
     * 2. 进程标记为异常，而不是完成，但也放在完成队列中
     */
    void interruptRun() {
        innerQueue.get(MyStatus.RUN).removeFirst();
    }

    /**
     * 进程运行完成调用,会将当前运行进程移走，当下一次进程调度时放入新的进程运行
     */
    void finishRun() {
        // 对应的PCB
        MyPCB first = innerQueue.get(MyStatus.RUN).removeFirst();
        // 对应的进程对象
        MyProcess process = processService.getProcessByPid(first.getPid());
        // 已分配资源释放
        freeResourceOfProcess(process);
        // pcb池移除
        pcbPool.remove(first);
        // 内存回收
        memoryService.remove(first);
        // 标记为完成
        first.setStatus(MyStatus.FINISH);
        innerQueue.get(MyStatus.FINISH).addLast(first);
    }

    boolean canBeDone(MyProcess myProcess) {
        List<MyResource> max = myProcess.getMax();
        List<MyResource> allocation = myProcess.getAllocation();
        for (int i = 0; i < max.size(); i++) {
            if (!max.get(i).getNumber().equals(allocation.get(i).getNumber())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 资源释放操作
     */
    void freeResourceOfProcess(MyProcess myProcess) {
        List<MyResource> allocation = myProcess.getAllocation();
        IntStream.range(0, allocation.size()).forEach(i -> {
            available[i] += allocation.get(i).getNumber();
        });
    }

    /**
     * 模拟执行进程
     */
    void runProcess() {
        LinkedList<MyPCB> runList = innerQueue.get(MyStatus.RUN);
//        List<MyProcess> allProcess = memoryService.getAllProcess();

        MyPCB first = runList.size() > 0 ? runList.getFirst() : null;
        if (first == null) {
            System.out.println("没有进程需要运行");
            return;
        }
        MyProcess process = processService.getProcessByPid(first.getPid());
        List<MyResource> request = process.getRequests();
        // 这一时刻执行的进程请求量不是null，有申请资源行为
        if (request != null) {
            // todo: 调用银行家算法
            boolean b = bankService.checkSafe(pcbPool.getPcbPool(), processService.getAllProcess());
            System.out.println("银行家算法结果:"+b);
            if (b) {
                // 银行家通过,分配资源，修改allocation,及系统可用资源available
                List<MyResource> allocation = process.getAllocation();
                IntStream.range(0, request.size()).forEach(i -> {
                    available[i] = available[i] - request.get(i).getNumber();
                    allocation.get(i).setNumber(allocation.get(i).getNumber() + request.get(i).getNumber());
                });
                System.out.println("系统资源:" + Arrays.toString(available));
                System.out.println("进程 " + process.getName() + " 申请资源:" + request);
                System.out.println("进程 " + process.getName() + " 最大资源:" + request);
                System.out.println("进程 " + process.getName() + " 已分配:" + allocation);
                // 分配结束 设置20%概率为null不申请,80%可能继续申请
                process.setRequests(Algorithm.randomBool(0.8) ? processService.testRequest(process) : null);
                // 修改进程池中的记录
                processService.putProcessByPid(process);
            } else {
                dispatchService.blockDispatch();
            }
        } else {
            System.out.println("进程 " + process.getName() + " 本次运行不申请资源");
            // 80%可能继续申请
            process.setRequests(Algorithm.randomBool(0.8) ? processService.testRequest(process) : null);
//             修改进程池中的记录
            processService.putProcessByPid(process);
        }
        // todo: 进程如果已分配资源等于最大申请资源，有80%概率会结束
        if (canBeDone(process) && Algorithm.randomBool(0.8)) {
            finishRun();
        }
        // 进入下一时间单位
//        timeNext();
    }

}
