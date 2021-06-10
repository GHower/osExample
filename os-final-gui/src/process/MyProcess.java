package process;

import com.sun.org.apache.xml.internal.security.Init;
import config.OSConfig;
import file_system.FileSystemCommand;
import kernel.OSInit;
import schedule.BlockActivate;
import users.OSUser;

import javax.swing.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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

    public boolean switch_flag = true;// 进程控制器
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
    /**
     * 进程磁盘管理所需信息
     */

    public String Disk_write; // 写入磁盘总量
    public String Disk_read; // 从磁盘读出总量


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
    public String run() {
        if (switch_flag == false || command_type == -1 || command == null) {
            return null;
        }
        // 命令执行后返回的信息
        String returnMessage = "";

        switch (command_type) {

            // 0-创建目录（mkdir dir）
            case 0:
                returnMessage = FileSystemCommand.mkdir(owner, command);
                System.out.println(returnMessage);
                break;

            // 1-读文件（read file）
            case 1:
                returnMessage = ">>" + FileSystemCommand.readFile(owner, command);
                break;

            // 2-写文件（write file info）；
            case 2:
                returnMessage = FileSystemCommand.writeFile(owner, command.split("@#")[0], command.split("@#")[1]);
                break;

            // 3-创建文件（touch file）
            case 3:
                returnMessage = FileSystemCommand.touch(owner, command);
                break;

            // 4-删除文件（rm file）；
            case 4:
                returnMessage = FileSystemCommand.rm(owner, command);
                break;

            // 5-查看路径下的文件（ls）
            case 5:
                returnMessage = FileSystemCommand.ls(owner, Boolean.parseBoolean(command));
                break;

            // 6-修改文件权限（chmod 777 file）
            case 6:
                returnMessage = FileSystemCommand.chmod(owner, command.split("@#")[0], command.split("@#")[1]);
                break;

            // 7-更改当前路径（cd path）
            case 7:
                returnMessage = FileSystemCommand.cd(owner, command);
                break;

            // 8-进程通信
            case 8:
                if(!owner.userName.equals("root"))
                {
                    System.err.println(owner.userName);
                    returnMessage = "<Error>  权限不足 ! ";
                }
                break;

            // 9-删除目录(rmdir file)
            case 9:
                returnMessage = FileSystemCommand.rmdir(owner, command);
                break;

            // 10-查看当前路径（pwd）
            case 10:
                returnMessage = owner.current.path;
                break;

            // 11-查看当前用户（who）
            case 11:
                returnMessage = owner.userName + "     " + owner.groupName;
                break;

            // 12-用户进程通信（mail）
            case 12:
                String targetUserName = command.split("@#")[0];
                String message = command.split("@#")[1];
                if (!OSConfig.onlineUsers.containsKey(targetUserName)) {
                    returnMessage = "<Error>  用户不存在或不在线";
                    break;
                }

                PrintStream ps0 = OSConfig.onlineUsers.get(targetUserName);
                ps0.println("<Mail> User  [" + owner.userName + "]  send you a message:" + "\n\n>>" + message + "\n"
                        + "-------------------END" + "\n");

                returnMessage = "successfully send the Message to " + targetUserName;
                break;

            // 13-查看当前系统时间（time）
            case 13:
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time = df.format(new Date());// new Date()为获取当前系统时间
                returnMessage = time;
                break;

            // 14-查看当前在线用户（onlieuser）
            case 14:
                for(String userName:OSConfig.onlineUsers.keySet())
                {
                    returnMessage += userName+"  ";
                }
                break;

            // 15-更改密码（passwd）
            case 15:
                owner.password = command;
                returnMessage = "successfully change the password to "+owner.password;
                break;

            default:
                break;
        }

        // 阻塞进程
//        if (returnMessage.equals("<Error> 文件繁忙 !")) {
//            PCB target = OSConfig.PCB_Map.get(PID);
//            BlockActivate.block(target);
//            target.blockType = 1;
//            target.info = command.split("@#")[0];
//            return returnMessage;
//        }

        command = null;
        command_type = -1;
        switch_flag = false;
        try {
            OSInit.saver();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "保存失败");
            e.printStackTrace();
        }
         System.out.println(owner.current.path);
         System.out.println(returnMessage);
        // PrintStream ps = OS.server.myList.get(owner.userName).ps;
        // PrintStream ps = owner.ps;
//        PrintStream ps = OSConfig.onlineUsers.get(owner.userName);
//        if (returnMessage.equals(""))
//            returnMessage = "  ";
//        ps.println(returnMessage);
        return returnMessage;

    }

    public void setSleep(boolean no_sleep) {
        // 是否允许睡眠，为true表示不允许睡眠，默认为false
        this.NO_SLEEP = no_sleep;
    }

    // 进程调度信息初始化
    public void init_process_scheduling(double Ppower_cost, boolean NO_SLEEP) {
//        this.power_cost = Ppower_cost; // 进程能耗
        this.NO_SLEEP = NO_SLEEP; // 是否允许睡眠，为true表示不允许睡眠，默认为false
    }

    // 磁盘调度信息初始化
    public void init_process_disk(String Disk_write, String Disk_read) {
        this.Disk_write = Disk_write; // 写入磁盘总量
        this.Disk_read = Disk_read; // 从磁盘读出总量
    }
}
