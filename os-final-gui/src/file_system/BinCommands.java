package file_system;


import config.OSConfig;
import process.PCB;
import schedule.CreatTestProcess;
import users.OSUser;

import java.util.HashMap;

/***
 *  包装一些用户的命令：
 *  	0.mkdir 【目录名】
 *  	1.read 【文件名】 todo
 *  	2.write 【文件名】【内容】
 *  	3.touch 【文件名】【大小】
 *  	4.rm 【文件名】
 */
public class BinCommands {
	//mkdir
	public static void Command_mkdir(OSUser users, String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(300));
		parameters.put("name", "mkdir_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 0;
		pcb.process.command = command;
		
	}
	
	
	//read
	public static void Command_read(OSUser users,String command) {
		
		int m = 500;
		if(users.current.dirTree.containsKey(command) == true)
			m += users.current.dirTree.get(command).inode.size;
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(m));
		parameters.put("name", "read_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(3));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 1;
		pcb.process.command = command;
		
		
	}
	
	
	//write
	public static void Command_write(OSUser users,String command) {
		int m = 500;
		if(users.current.dirTree.containsKey(command))
			m += users.current.dirTree.get(command).inode.size+command.split("@#")[1].length();
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(m));
		parameters.put("name", "write_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(3));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 2;
		pcb.process.command = command;
	}
	
	
	//touch
	public static void Command_touch(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(800));
		parameters.put("name", "touch_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(2));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 3;
		pcb.process.command = command;
		
		
	}
	
	
	//rm
	public static void Command_rm(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(18));
		parameters.put("memory", String.valueOf(500));
		parameters.put("name", "rm_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 4;
		pcb.process.command = command;
		
		
	}
	
	//ls
	public static void Command_ls(OSUser users,boolean isSimple) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(15));
		parameters.put("memory", String.valueOf(100));
		parameters.put("name", "ls_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 5;
		pcb.process.command = String.valueOf(isSimple);
		
		
	}
	
	//chmod
	public static void Command_chmod(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(20));
		parameters.put("memory", String.valueOf(200));
		parameters.put("name", "chmod_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 6;
		pcb.process.command = command;
		
		
	}
	
	
	//cd
	//输入的string是：  目标directory
	public static void Command_cd(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(12));
		parameters.put("memory", String.valueOf(80));
		parameters.put("name", "cd_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 7;
		pcb.process.command = command;
		
		
	}
	
	//进程通信：cmd@#receiverName@#message
	public static void Command_communicate(OSUser users,String command) {
		
//		PCB pcb = CreatTestProcess.createCurrentProcess(users,"communicate_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
//
//		pcb.process.command_type = 8;
//		pcb.process.command = command;
		
		
	}
	
	
	//rmdir
	public static void Command_rmdir(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(12));
		parameters.put("memory", String.valueOf(200));
		parameters.put("name", "rmdir_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 9;
		pcb.process.command = command;
		
		
	}
	
	
	//pwd
	public static void Command_pwd(OSUser users) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(20));
		parameters.put("memory", String.valueOf(100));
		parameters.put("name", "pwd_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 10;
		pcb.process.command = "pwd";

		
		
	}
	
	//who
	public static void Command_who(OSUser users) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(100));
		parameters.put("name", "who_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 11;
		pcb.process.command = "who";
	
	
	
}
	
	
	//mail
	public static void Command_mail(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(command.length()));
		parameters.put("name", "mail_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 12;
		pcb.process.command = command;
	
	
	
}
	
	//time
	public static void Command_time(OSUser users) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(100));
		parameters.put("name", "time_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 13;
		pcb.process.command = "time";
	
	
	
}
	
	//onlineuser
	public static void Command_online(OSUser users) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(150));
		parameters.put("name", "online_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 14;
		pcb.process.command = "onlineuser";
	
	
	
}
	
	//passwd
	public static void Command_passwd(OSUser users,String command) {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ID", String.valueOf(OSConfig.NEXT_PROCESS_ID));
		parameters.put("priority",String.valueOf(10));
		parameters.put("memory", String.valueOf(50));
		parameters.put("name", "passwd_"+users.userName+"_"+OSConfig.NEXT_PROCESS_ID);
		parameters.put("time_needed", String.valueOf(1));
		PCB pcb = CreatTestProcess.createUserProcess(users, parameters, 0);
		pcb.process.command_type = 15;
		pcb.process.command =command;
	
	
	
}
	
	
}
