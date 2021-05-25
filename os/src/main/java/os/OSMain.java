package os;


import os.enums.MyStatus;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyRequest;
import os.model.entity.MyResource;
import os.service.BankService;
import os.service.impl.BankServiceImpl;
import os.service.impl.ProcessServiceImpl;

import java.util.*;

public class OSMain {
    Map<MyStatus, List<MyPCB>> queue=null;
    public static void main(String[] args) {
/*
        OSMain osMain = new OSMain();
        osMain.init();
        // 进程进入
        osMain.processIn();
        // todo:为进程设置需要的最大资源等信息，后面则分配资源
        osMain.processResource();
        ProcessServiceImpl processService= new ProcessServiceImpl();
        List<MyPCB> fcfs = processService.FCFS(osMain.queue.get(MyStatus.BACK));
*/


        List<MyProcess> myProcesses = new ArrayList<>();
        List<MyPCB> myPCBS = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<MyResource> myResourceList = new ArrayList<>();
            List<MyResource> myResourceList1 = new ArrayList<>();
            MyPCB myPCB = new MyPCB();
            MyProcess myProcess = new MyProcess();
            myPCB.setPid(i);
            myProcess.setId(i);
            for (int j = 0; j < 5; j++) {
                MyResource myResource = new MyResource();
                myResource.setName("p"+j);
                myResource.setNumber(3);
                myResourceList.add(myResource);
            }
            for (int j = 0; j < 5; j++) {
                MyResource myResource = new MyResource();
                myResource.setName("p"+j);
                myResource.setNumber(7);
                myResourceList1.add(myResource);
            }
            myProcess.setAllocation(myResourceList);
            myProcess.setMax(myResourceList1);
            myPCBS.add(myPCB);
            myProcesses.add(myProcess);
        }


        BankService bankService = new BankServiceImpl();

        System.out.println(bankService.checkSafe(myPCBS,myProcesses));
//        bankService.printSystemVariable();
        MyRequest myRequest = new MyRequest();
        myRequest.setId(3);

        List<MyResource> myResourceList = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            MyResource myResource = new MyResource();
            myResource.setName("p"+j);
            myResource.setNumber(2);
            myResourceList.add(myResource);
        }
        myRequest.setRequest(myResourceList);
        System.out.println(bankService.setRequest(myRequest));
//      bankService.BankerAlgorithm();
    }

    /**
     * 为进程设置资源
     */
    private void processResource() {

    }

    /**
     * 初始化 后备、就绪、运行、等待、完成 队列
     */
    public void init(){
        queue = new HashMap<>();
        List<MyPCB> backList = new ArrayList<>();
        List<MyPCB> readyList = new ArrayList<>();
        List<MyPCB> runList = new ArrayList<>();
        List<MyPCB> waitList = new ArrayList<>();
        List<MyPCB> finishList = new ArrayList<>();

        queue.put(MyStatus.BACK,backList);
        queue.put(MyStatus.READY,readyList);
        queue.put(MyStatus.RUN,runList);
        queue.put(MyStatus.WAIT,waitList);
        queue.put(MyStatus.FINISH,finishList);
    }

    /**
     * 进程调度的输入过程，命令行黑框方式
     * fixme: 从文件或数据库读取，更快生成。或者线程方式随机时间按一定规律随机生成
     */
    public void processIn(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入进程数量：");
        int i = scanner.nextInt();

        for(int j=0;j<i;j++){
            System.out.println("输入第"+(j+1)+"个进程的id、到达时间(s)、运行时间(s)：");
            Integer id = scanner.nextInt();
            Double arrive = scanner.nextDouble();
            Double run = scanner.nextDouble();
            MyPCB myPCB = new MyPCB();
            myPCB.setPid(id);
            myPCB.setArriveTime(arrive);
//            myPCB.setArriveTime(LocalDateTime.now());
            myPCB.setRunTime(run);
            queue.get(MyStatus.BACK).add(myPCB);
        }
    }
}
