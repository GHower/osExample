package os;


import os.enums.MyStatus;
import os.model.entity.*;
import os.service.*;
import os.service.impl.*;

import java.io.IOException;
import java.util.*;

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
        // 生成测试的jcb 更新 后备作业 队列
        outsideQueue.put(MyStatus.BACK, jobService.testJCB());
        // 进行作业调度
        System.out.println("=======作业调度========");
        dispatchService.jobDispatch();
        System.out.println("后备队列：" + OSMain.outsideQueue.get(MyStatus.BACK));
        System.out.println("就绪队列：" + OSMain.innerQueue.get(MyStatus.READY));
        System.out.println("=======进程调度========");
//        dispatchService.jobDispatch();
        dispatchService.proDispatch();
        System.out.println("就绪队列：" + innerQueue.get(MyStatus.READY));
        System.out.println("运行队列：" + innerQueue.get(MyStatus.RUN));
        System.out.println("=======阻塞产生========");
        dispatchService.blockDispatch();
        System.out.println("就绪队列：" + innerQueue.get(MyStatus.READY));
        System.out.println("运行队列：" + innerQueue.get(MyStatus.RUN));
        System.out.println("等待队列：" + innerQueue.get(MyStatus.WAIT));
        System.out.println("=======内存情况========");
        memoryService.display3();
        System.out.println("=======移除内存后========");
        MyPCB first = OSMain.innerQueue.get(MyStatus.WAIT).removeFirst();
        memoryService.remove(first);
        memoryService.display3();

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
        time++;
    }

    /**
     * 模拟执行进程
     */
    void runProcess() {
        LinkedList<MyPCB> runList = innerQueue.get(MyStatus.RUN);
        MyPCB first = runList.getFirst();
        MyProcess process = processService.getProcessByPid(first.getPid());
        List<MyResource> request = process.getRequests();
        if (request != null) {
            // todo: 调用银行家算法
            if (true) {
                // 其他处理....

                // 进入下一时间单位
                timeNext();
            } else {
                dispatchService.blockDispatch();
            }
        }
    }

    /**
     * 进程调度的输入过程，命令行黑框方式
     * fixme: 从文件或数据库读取，更快生成。或者线程方式随机时间按一定规律随机生成
     */
    public void processIn() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("输入进程数量：");
//        int i = scanner.nextInt();
//
//        for(int j=0;j<i;j++){
//            System.out.println("输入第"+(j+1)+"个进程的id、到达时间(s)、运行时间(s)：");
//            Integer id = scanner.nextInt();
//            Double arrive = scanner.nextDouble();
//            Double run = scanner.nextDouble();
//            MyPCB myPCB = new MyPCB();
//            myPCB.setPid(id);
//            myPCB.setArriveTime(arrive);
////            myPCB.setArriveTime(LocalDateTime.now());
//            myPCB.setRunTime(run);
//            outsideQueue.get(MyStatus.BACK).add(myPCB);
//        }
    }
}
