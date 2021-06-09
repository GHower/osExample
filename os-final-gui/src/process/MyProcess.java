package process;

import config.OSConfig;
import users.OSUser;

import java.util.HashMap;
import java.util.List;

/**
 * 进程实体,包括进程实际执行的操作
 */
public class MyProcess {
    /**
     * 进程基本信息
     */
    public int PID; // 进程ID
    public String name; // 进程名
    // 进程类型 0-内核进程 1-用户进程
    public int type;
    public int thread; // 进程中线程数目
    // 所需资源 资源名称-数量
    public HashMap<String,Integer> request;
    public HashMap<String,Integer> allocation;
    public HashMap<String,Integer> max;

    public String operator; // 进程操作人
    public int port; // 进程端口号

    public OSUser owner;// 进程所有者
    public String ownerGroup; // 进程用户组
    /**
     * 进程调度所需信息
     */
    public int time_used = 0; // 进程已用CPU时间
    public int time_waited = 0; // 进程等待时间
    // 是否允许睡眠，为true表示不允许睡眠，默认为false
    public boolean NO_SLEEP = false;
    public String command = null;// 进程执行内容
    /**
     * 进程执行内容的类型：
     * 0-创建目录（mkdir）；1-读文件（read）；2-写文件（write）；
     * 3-创建文件（touch）；4-删除文件（rm）；5-查看路径下的文件（ls）;
     * 6-修改文件权限（chmod）；7-更改当前路径（cd）；8-进程通信；9-删除目录(rmdir)
     * 10-查看当前路径（pwd）；11-查看当前用户（who）；12-用户通信（mail）
     * 13-当前系统时间（time）；14-在线用户（who） 15-更改密码（passwd）
     */
    public int command_type = -1;

    public MyProcess(int PID, String name, OSUser owner,int type,int thread,int port) {
        this.PID = PID;
        this.name = name;
        this.type = type;
        this.thread = thread;
        this.request = null;
        this.port = port;
        this.owner = owner;
        this.operator = owner.userName;
        this.ownerGroup = owner.userName;
        this.allocation = new HashMap<>();
        this.max = new HashMap<>();
        OSConfig.PCB_Name_Map.put(name,PID);
    }

    // 进程的运行方法，进程有自己的执行流程，所以绑定在进程实体上
    public void run() {
        System.out.println("进程(pid:"+PID+")"+name+": 运行了!");
    }

    public void setSleep(boolean no_sleep) {
        // 是否允许睡眠，为true表示不允许睡眠，默认为false
        this.NO_SLEEP = no_sleep;
    }

    public void init_process_disk(String disk_write, String disk_read) {
//        this.Disk_Write = disk_write; // 写入磁盘总量
//        this.Disk_Read = disk_read; // 从磁盘读出总量
    }
}
