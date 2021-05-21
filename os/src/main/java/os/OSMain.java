package os;


import os.enums.MyStatus;
import os.model.entity.MyPCB;
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
        BankServiceImpl bankService = new BankServiceImpl();
        bankService.printSystemVariable();
        bankService.setRequest();

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
