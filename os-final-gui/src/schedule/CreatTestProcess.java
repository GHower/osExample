package schedule;

import common.OSStatics;
import config.OSConfig;
import jobs.JCB;
import process.MyProcess;
import process.PCB;
import users.OSUser;
import utils.RandomTool;

import java.util.HashMap;
import java.util.Set;

public class CreatTestProcess {
    public static PCB createKernelProcess() {
// 进程类型是1-用户进程
        int PID = OSConfig.NEXT_PROCESS_ID;
        int PType = 0;
        String name = "内核进程_" + OSConfig.NEXT_PROCESS_ID;
        int priority = 100;
        int time_needed = 10;
        int memory = 75;
        boolean NO_SLEEP = true;
        int thread = RandomTool.random(10, 100);
        int port = RandomTool.random(10000, 60000);

        // 磁盘信息初始化
//        String Disk_write = RandomTool.random(23,123) + "M";
//        String Disk_read = RandomTool.random(100) + "M";

        // 初始化,用户进程类型为1，初始状态默认为阻塞状态
        PCB pcb = new PCB(PID, PType, priority, memory, time_needed);
        pcb.process = new MyProcess(PID, name, OSConfig.root, PType, thread, port);
        // 用户进程优先级默认为0；可以被休眠,创建进程
        pcb.process.setSleep(NO_SLEEP);
//        pcb.process.init_process_disk(Disk_write, Disk_read);

        // 直接分配内存
        if (OSStatics.getPCBTotal() < OSConfig.PCB_POOL_SIZE
                && MemorySchedule.MemoryAllocate(pcb)) {
            // 记录到映射关系
            OSConfig.PCB_Map.put(PID, pcb);
            // 进程加入就绪队列
            OSConfig.ready_list.addLast(pcb);
            OSConfig.NEXT_PROCESS_ID++;
        }
        return pcb;
    }

    public static void randomCreate(int num) {
        for (int i = 0; i < num; i++) {
            int priority = RandomTool.random(1, 10);
            int memory = RandomTool.random(100, 1024 * 2);
            int time_needed = RandomTool.random(3, 10);
            createUserProcess(priority, memory, "", time_needed, OSConfig.root, 0);
        }
    }
    // 命令的用户进程创建，type为作业类型
    public static PCB createUserProcess(OSUser users,HashMap<String, String> parameters,int type){
        int id = Integer.parseInt(parameters.get("ID"));
        int priority = Integer.parseInt(parameters.get("priority"));
        int memory = Integer.parseInt(parameters.get("memory"));
        int time_needed = Integer.parseInt(parameters.get("time_needed"));
        String name = parameters.get("name");
        int pType = 1;

        int thread = (int)(10+100*Math.random());
        int port = (int)(10+100*Math.random());
        PCB pcb = new PCB(id, type, priority, memory, time_needed);
        pcb.process = new MyProcess(id, name, users,type, thread, port);
        pcb.type = pType;

        // 磁盘信息初始化
        String Disk_write = (int) (23 + 100 * Math.random()) +"M";
        String Disk_read = (int) (100 * Math.random()) +"M";

        // 用户进程优先级默认为0；可以被休眠
        pcb.process.init_process_scheduling(1, false);
        pcb.process.init_process_disk(Disk_write, Disk_read);

        JCB jcb = new JCB(pcb, type);
        OSConfig.PCB_Map.put(id, pcb);
        pcb.jid = jcb.JID;

        // 在后备队列中加入该进程的信息
        jcb.submitTime = OSConfig.System_time;
        OSConfig.jobsList.addLast(jcb);
        OSConfig.NEXT_PROCESS_ID++;
        return pcb;
    }

    private static PCB createUserProcess(
            int priority, int memory, String name,
            int time_needed, OSUser root, int type) {
        // 进程类型是1-用户进程
        int PID = OSConfig.NEXT_PROCESS_ID;
        name = name.isEmpty() ? "进程_" + OSConfig.NEXT_PROCESS_ID : name;
        int PType = 1;

        // 进程变量初始化
        int thread = RandomTool.random(10, 100);
        int port = RandomTool.random(10000, 60000);
        boolean NO_SLEEP = false;

        // 磁盘信息初始化
//        String Disk_write = RandomTool.random(23,123) + "M";
//        String Disk_read = RandomTool.random(100) + "M";

        // 初始化,用户进程类型为1，初始状态默认为阻塞状态
        PCB pcb = new PCB(PID, PType, priority, memory, time_needed);
        pcb.process = new MyProcess(PID, name, root, PType, thread, port);
        // 用户进程优先级默认为0；可以被休眠,创建进程
//        pcb.process.init_process_scheduling(NO_SLEEP);
//        pcb.process.init_process_disk(Disk_write, Disk_read);
        // 生成max和allocation
        createMaxAndAllocation(pcb);
        pcb.process.request = RandomTool.randomBool() ? createRequest(pcb) : null;

        JCB jcb = new JCB(pcb, type);
        pcb.jid = jcb.JID;
        // 记录到映射关系
        OSConfig.PCB_Map.put(PID, pcb);
        // 在后备队列中加入该作业的信息
        jcb.submitTime = OSConfig.System_time;

        OSConfig.jobsList.addLast(jcb);
        OSConfig.NEXT_PROCESS_ID++;
        return pcb;
    }

    public static HashMap<String, Integer> createRequest(PCB pcb) {
        HashMap<String, Integer> request = new HashMap<>();
        int time = pcb.time_needed - pcb.process.time_used;
        // 没时间的，全部申请
        Set<String> keys = pcb.process.max.keySet();
        for (String key : keys) {
            Integer max = pcb.process.max.get(key);
            Integer allocation = pcb.process.allocation.get(key);
            request.put(key, time > 1 ? RandomTool.random(max - allocation) : max - allocation);
        }
        return request;
    }

    private static void createMaxAndAllocation(PCB pcb) {
        Set<String> keys = OSConfig.available.keySet();
        // 现在先做全部资源申请
        for (String key : keys) {
            pcb.process.max.put(key, RandomTool.random(OSConfig.available.get(key)));
            pcb.process.allocation.put(key,0);
        }
    }
}
