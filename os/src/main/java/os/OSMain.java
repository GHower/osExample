package os;


import os.enums.MyStatus;
import os.model.entity.*;
import os.service.*;
import os.service.impl.*;

import java.util.*;

public class OSMain {
    public static Map<MyStatus, LinkedList<MyPCB>> innerQueue=null;
    public static Map<MyStatus, LinkedList<MyJCB>> outsideQueue=null;
    JobService jobService= new JobServiceImpl();
    ProcessService processService= new ProcessServiceImpl();
    BankService bankService = new BankServiceImpl();
    DispatchService scheduleService = new DispatchServiceImpl();
    MemoryService memoryService = new MemoryServiceImpl();

    public static void main(String[] args) {

        OSMain osMain = new OSMain();
        osMain.init();
        osMain.run();


    }
    /**
     * 在这里写主程序代码，懒得弄静态了
     */
    public void run(){
        // 生成测试的jcb
        LinkedList<MyJCB> myJCBS = jobService.testJCB();
        // 更新 后备作业 队列，按FCFS放入
        outsideQueue.put(MyStatus.BACK,scheduleService.FCFS(myJCBS));

        // 取出队首,同时移除
        MyJCB myJCB = outsideQueue.get(MyStatus.BACK).getFirst();
        outsideQueue.get(MyStatus.BACK).removeFirst();

        // 内存可以分配
        if (memoryService.hasAllocation(myJCB)) {
            // 创建进程
            System.out.println(processService.testProcess(myJCB));
            // todo：分配PCB

            System.out.println(outsideQueue.get(MyStatus.BACK));
        }

//        List<MyProcess> myProcesses = new ArrayList<>();
//        List<MyPCB> myPCBS = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            List<MyResource> myResourceList = new ArrayList<>();
//            List<MyResource> myResourceList1 = new ArrayList<>();
//            MyPCB myPCB = new MyPCB();
//            MyProcess myProcess = new MyProcess();
//            myPCB.setPid(i);
//            myProcess.setId(i);
//            for (int j = 0; j < 5; j++) {
//                MyResource myResource = new MyResource();
//                myResource.setName("r"+j);
//                myResource.setNumber(3);
//                myResourceList.add(myResource);
//            }
//            for (int j = 0; j < 5; j++) {
//                MyResource myResource = new MyResource();
//                myResource.setName("r"+j);
//                myResource.setNumber(7);
//                myResourceList1.add(myResource);
//            }
//            myProcess.setAllocation(myResourceList);
//            myProcess.setMax(myResourceList1);
//            myPCBS.add(myPCB);
//            myProcesses.add(myProcess);
//        }
//
//        System.out.println(bankService.checkSafe(myPCBS,myProcesses));
////        bankService.printSystemVariable();
//        MyRequest myRequest = new MyRequest();
//        myRequest.setId(3);
//
//        List<MyResource> myResourceList = new ArrayList<>();
//        for (int j = 0; j < 5; j++) {
//            MyResource myResource = new MyResource();
//            myResource.setName("p"+j);
//            myResource.setNumber(2);
//            myResourceList.add(myResource);
//        }
//        myRequest.setRequest(myResourceList);
//        System.out.println(bankService.setRequest(myRequest));
//      bankService.BankerAlgorithm();
    }
    /**
     * 为进程设置资源
     */
    private void processResource() {

    }

    /**
     * 初始化
     * 内存中的 就绪、运行、等待、完成 队列
     * 外存中的 后备、挂起 队列
     */
    public void init(){
        innerQueue = new HashMap<>();
        outsideQueue = new HashMap<>();

        LinkedList<MyJCB> backList = new LinkedList<>();

        LinkedList<MyPCB> readyList = new LinkedList<>();
        LinkedList<MyPCB> runList = new LinkedList<>();
        LinkedList<MyPCB> waitList = new LinkedList<>();
        LinkedList<MyPCB> finishList = new LinkedList<>();

        outsideQueue.put(MyStatus.BACK,backList);
        innerQueue.put(MyStatus.READY,readyList);
        innerQueue.put(MyStatus.RUN,runList);
        innerQueue.put(MyStatus.WAIT,waitList);
        innerQueue.put(MyStatus.FINISH,finishList);
    }
    /**
     * 进程调度的输入过程，命令行黑框方式
     * fixme: 从文件或数据库读取，更快生成。或者线程方式随机时间按一定规律随机生成
     */
    public void processIn(){
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
