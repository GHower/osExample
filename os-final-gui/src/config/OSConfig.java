package config;

import disk_management.SuperBlock;
import file_system.Dentry;
import jobs.JCB;
import jobs.JCBLinkedList;
import kernel.OSConnection;
import memory.partition.Mem;
import memory.partition.Memory;
import process.PCB;
import process.PCBLinkedList;
import users.OSUser;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作系统上的参数
 */
public class OSConfig {
    //在线的用户
    public static OSConnection server;
    public static HashMap<String, PrintStream> onlineUsers = new HashMap<String, PrintStream>();
    public static HashMap<String, OSUser> userList;

    // 时间片大小(*100ms)
    public static int TIMEPIECE = 5;

    // 初始化的文件系统,根目录对象
    // linux中会把文件系统挂载到根目录。
    public static Dentry ROOT;


    public static int Disk_Cylinder = 10; // 10个柱面
    public static int Disk_Track = 5; // 每个柱面 5个磁道
    public static int Disk_Sector = 8; // 每个磁道8个扇区

    // 磁盘超级块,磁盘属于外设硬件,参数在硬件上,
    public static SuperBlock superBlock;
    // 磁盘超级块
    public static String[] MyDisk = new String[20 * 20];

    // 系统用户
    public static OSUser root;

    // 下一个新创建进程的ID
    public static int NEXT_PROCESS_ID = 100;
    // 轮转时间片大小
    public static int TIMEPIECES_SIZE = 4;
    // 处理机的数量
    public static int CPU_num = 1;
    // 内核大小
    public static int Kernel_SIZE = 4*1024;
    // 内存中允许得最多进程数目
    public static int PCB_POOL_SIZE = 20;
    // 内存总大小(单位：Bytes)
    public static int MAX_MEMORY = 64 * 1024;



    /**
     * 以下是标志，标记系统使用的东西
     */
    // 处理机状态位
    // 0－暂停；1－运行；
    public static int CPU_STATE = 1;
    // 处理机调度(进程调度)算法：
    // 0－先来现服务；1－短进程优先；2－高优先级；3－抢占式高优先级；4－高响应比；5－时间片轮转
    //
    public static int CPU_SCHEDULE_FLAG = 3;
    // 作业调度算法：
    //  0－先来现服务；1－短作业优先；2－高优先级；3－高响应比；
    //
    public static int JOB_SCHEDULE_FLAG = 0;

    // 存储方式标记：
    // 0－分区存储；1－页式存储；
    public static int MEMORY_ALLOCATE_FLAG = 0;

    // 分区存储分配算法：
    // 0－首次分配算法；1－循环首次分配算法；2－最佳匹配算法；
    // 3－最坏匹配算法；
    public static int PARTITION_ALGORITHM_FLAG = 0;

    // 页式存储页面置换算法：
    // 0-LRU；1-LFU
    public static int PAGINATION_ALGORITHM_FLAG = 0;

    // 磁盘调度算法：
    // 0-FCFS；1-SCAN；2-CSCAN
    public static int DISK_SCHEDULE_FLAG = 1;

    /**
     * 以下是用到的队列
     */
    // 经常要找PCB 干脆放在全局中通过映射获取
    public static HashMap<Integer, PCB> PCB_Map = new HashMap<Integer, PCB>();// PID--Object
    public static HashMap<Integer, JCB> JCB_MAP_RUNNING= new HashMap<>();// 执行中的作业
    public static HashMap<Integer, JCB> JCB_MAP_FINISHED= new HashMap<>();// 已完成的
    public static HashMap<String, Integer> PCB_Name_Map = new HashMap<String, Integer>();// Name--PID
    // 进程队列：运行队列－－就绪队列－－阻塞队列
    public static PCBLinkedList running_list = new PCBLinkedList(CPU_num);
    public static PCBLinkedList ready_list = new PCBLinkedList();
    public static PCBLinkedList block_list = new PCBLinkedList();

    // 后备队列
    public static JCBLinkedList jobsList = new JCBLinkedList();
    // 页式存储分配时，内存中实际存在的页面
//    public static Pages_LInkList inmemorys = new Pages_LInkList(CPU_num * 3);
//    public static Pages_LInkList outmemorys = new Pages_LInkList();

    public static Memory memory = new Memory(MAX_MEMORY);
    public static Mem point = null;

    public static Map<String,Integer> available=new HashMap<>();
    public static double System_time = 0;
}
