package os;


import os.enums.MyStatus;
import os.model.entity.MyCB;
import os.model.entity.MyJCB;
import os.model.entity.MyPCB;
import os.service.impl.ProcessServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OSMain {
    Map<MyStatus, List<MyPCB>> innerQueue=null;
    Map<MyStatus,List<MyJCB>> outsideQueue=null;
    final ProcessServiceImpl processService= new ProcessServiceImpl();

    public static void main(String[] args) {
        OSMain osMain = new OSMain();
        osMain.init();
        System.out.println("-------正在为作业创建进程-------");
        osMain.processIn();
        // todo:为进程设置需要的最大资源等信息，后面则分配资源
        osMain.processResource();

//        List<MyPCB> fcfs = processService.FCFS(osMain.innerQueue.get(MyStatus.READY));
//        processService.display(fcfs);
//        osMain
    }

    /**
     * 为进程设置资源
     */
    private void processResource() {

    }

    /**
     * 系统初始化
     * innerQueue   存在内存中的 就绪、运行、等待、完成 队列
     * outsideQueue 存在外存中的 后备、挂起 队列
     */
    public void init(){
        innerQueue = new HashMap<>();
        outsideQueue = new HashMap<>();
        List<MyJCB> backList = testJob();
        List<MyPCB> readyList = new ArrayList<>();
        List<MyPCB> runList = new ArrayList<>();
        List<MyPCB> waitList = new ArrayList<>();
        List<MyPCB> finishList = new ArrayList<>();
        // 后备作业队列
        outsideQueue.put(MyStatus.BACK,backList);
        // 操作pcb的队列
        innerQueue.put(MyStatus.READY,readyList);
        innerQueue.put(MyStatus.RUN,runList);
        innerQueue.put(MyStatus.WAIT,waitList);
        innerQueue.put(MyStatus.FINISH,finishList);
    }

    /**
     * 生成几个测试的作业，这是模拟的，实际上是由用户的操作不断提交作业
     * @return 生成的作业集合
     */
    public List<MyJCB> testJob(){
        MyJCB cloudMusic = new MyJCB();
        cloudMusic.setId(1);
        cloudMusic.setName("网易云音乐");
        cloudMusic.setType(1);
        cloudMusic.setUsername("ghower");

        MyJCB QQ = new MyJCB();
        QQ.setId(2);
        QQ.setName("腾讯QQ");
        QQ.setType(1);
        QQ.setUsername("ghower");

        List<MyJCB> jcbs = new ArrayList<>();
        jcbs.add(cloudMusic);
        jcbs.add(QQ);
        for (MyJCB jcb : jcbs) {
            System.out.println("用户{"+jcb.getUsername()+"}提交了作业{"+jcb.getName()+"}");
        }
        return jcbs;
    }

    /**
     * 进程高级调度后自动创建进程
     * fixme: 从文件或数据库读取，更快生成。或者线程方式随机时间按一定规律随机生成
     */
    public void processIn() {
        Scanner scanner = new Scanner(System.in);
        CopyOnWriteArrayList<MyJCB> myJCBS = new CopyOnWriteArrayList<MyJCB>(outsideQueue.get(MyStatus.BACK));
        // 每次都最大尝试能否分配内存
        for (MyJCB myJCB : myJCBS) {
            // todo: if中检查内存是否可以分配
            if (true) {
//                System.out.println(myJCB);
                System.out.println("输入作业{" + myJCB.getName() + "}对应进程的id、到达时间(s)、运行时间(s)：");
                Integer id = scanner.nextInt();
                Double arrive = scanner.nextDouble();
                Double run = scanner.nextDouble();
                MyPCB myPCB = new MyPCB();
                myPCB.setPid(id);
                myPCB.setArriveTime(arrive);
                myPCB.setRunTime(run);
                innerQueue.get(MyStatus.READY).add(myPCB);
                // 进程已经创建，将PCB从后备队列中移除
                myJCBS.remove(myJCB);
            }
            // 更新
            outsideQueue.put(MyStatus.BACK, myJCBS);
//        System.out.println(outsideQueue.get(MyStatus.BACK));
        }
    }
}
