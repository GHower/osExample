package process;

public class PCB {
    //进程ID
    public int PID;
    //进程类型,0-内核进程 1-代表普通进程
    public int type;
    //进程标记，0代表可运行，1-代表被阻塞
    public int flag = 0;
    //进程初始优先级，默认为10
    public int priority = 10;
    //进程发送的信息
    public String send = null;
    //进程接收到的通信信息
    public String Message = null;
    //进程通信信息--备注部分
    public String info = null;
    // 阻塞类型,当flag为1时 需要判断阻塞类型
    public int blockType = 0;
    // 进程初始化所需内存
    public int memory;
    public int time_needed = 0;    //进程需要CPU时间
    public MyProcess process;    //PCB对应的进程
    // 该进程在内存页中对应的起始地址（分区存储模型）
    public int addrStart = -1;

    // JID，为了看出作业完成而保留
    public int jid;
    // 页表（分页存储模型）
    //    public  Vector<Page> pageTable = new Vector<Page>();

    //一个进程新建立时做出的基本信息初始化
    public PCB(int PID, int type, int priority, int memory, int time_needed) {
        this.PID = PID;
        this.type = type;
        this.priority = priority;
        this.memory = memory;
        this.time_needed = time_needed;

//        this.next = null;
    }
}
