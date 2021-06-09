package file_system;


import config.OSConfig;
import users.OSUser;
import users.OSUser;

import java.util.regex.Pattern;


/***
 * 
 * 对文件系统的操作
 *
 */
public class FileSystemCommand {
	// 用户类型判断
	// 输入用户和目标文件，判断用户对应的是什么类型（0-u,1-g,2-o）
	public static int chargeUserType(OSUser user, Dentry file) {

		if(user.userName.equals(file.inode.owner) || user.userName.equals("root"))
			return 0;

		if (user.groupName.equals(file.inode.group))
			return 1;

		return 2;

	}

	// 权限判断
	// 输入用户、文件、命令种类（0-r,1-w,2-x）
	public static boolean getAuthority(OSUser user, Dentry file, int cmd) {

		int who = chargeUserType(user, file);
		int authority = Integer.parseInt(file.inode.authority);
		if (who == 0) {

			int binary = Integer.parseInt(Integer.toBinaryString(authority / 100));
			int r = binary / 100;
			int w = binary / 10 % 10;
			int x = binary % 10;

			switch (cmd) {
			case 0:
				if (r == 1)
					return true;
				else
					return false;
			case 1:
				if (w == 1)
					return true;
				else
					return false;
			case 2:
				if (x == 1)
					return true;
				else
					return false;
			default:
				return false;
			}
		}

		else if (who == 1) {

			int binary = Integer.parseInt(Integer.toBinaryString(authority / 10 % 10));
			int r = binary / 100;
			int w = binary / 10 % 10;
			int x = binary % 10;

			switch (cmd) {
			case 0:
				if (r == 1)
					return true;
				else
					return false;
			case 1:
				if (w == 1)
					return true;
				else
					return false;
			case 2:
				if (x == 1)
					return true;
				else
					return false;
			default:
				return false;
			}
		}

		else {

			int binary = Integer.parseInt(Integer.toBinaryString(authority % 10));
			int r = binary / 100;
			int w = binary / 10 % 10;
			int x = binary % 10;

			switch (cmd) {
			case 0:
				if (r == 1)
					return true;
				else
					return false;
			case 1:
				if (w == 1)
					return true;
				else
					return false;
			case 2:
				if (x == 1)
					return true;
				else
					return false;
			default:
				return false;
			}
		}

	}

	// 更改权限
	public static String chmod(OSUser owner, String authority, String FileName) {

		
		// 文件不存在
				if (owner.current.dirTree.containsKey(FileName) == false)
					return "<Error> target File [" + FileName + "] does not exist!";

				Dentry file = owner.current.dirTree.get(FileName);
		
		
		
		
		if(owner.userName.equals(file.inode.owner) || owner.userName.equals("root")){

			if (Pattern.matches("[0-7]{3}", authority) == false)
				return "<Error> illegal  authority number  [should like '744']!";

			file.inode.authority = authority;
			return "successfully change authority to [" + authority + "]";

		}

		else {
			return "<Error> Permission Denied !";
		}

	}

	// 更换当前工作目录命令
	public static String cd(OSUser user, String targetDir) {

		Dentry current = user.current.cd(targetDir);

		// 先判断是否存在目标目录
		if (current == null)
			return "<Error> target directory [" + targetDir + "] does not exist!";

		user.current = current;

		return "";

	}
	
	
	//查看当前目录下所有文件
	public static String ls(OSUser user,boolean isSimple)
	{
		
		if(user.current.dirTree.size() == 0)
			return "";
		
		int i = 0;
		String returnMessage = "";
		if(isSimple == true) {
			for(String files:user.current.dirTree.keySet())
		{
			returnMessage+=" "+files+"  ";

			if(i%5 == 0&&i!=0)
				returnMessage+="\n";
			
			i++;
		}
		}
		else {
			for(String files:user.current.dirTree.keySet())
			{
				Dentry file = user.current.dirTree.get(files);
				String dir = "";
				if(file.isDir == true)
					dir = "d";
				
				int name_length = files.length();
				int owner_length = file.inode.owner.length();
				int group_length = file.inode.group.length();
				String format = "%-"+(30-name_length)+"s %-"+(30-owner_length)+"s %-"+(30-group_length)+"s";
				String string = String.format(format, files,file.inode.owner,file.inode.group);
			
				
				returnMessage+=dir+"  "+file.inode.authority+"  "+file.inode.size+"   "
						+"       "+file.inode.lastModifyData+"           "+string+"\n";
				
				i++;
			}
		}
		
		return returnMessage;
	}
	
	
	

	// 读文件操作
	public static String readFile(OSUser user, String FileName) {

		

		return user.current.readFile(user, FileName);

	}

	// 写文件操作
	public static String writeFile(OSUser user, String FileName, String info) {

		return user.current.writeFile(user, FileName, info);


	}

	// 创建文件操作
	public static String touch(OSUser user, String FileName) {
		Dentry current = user.current;
		return current.creatFile(FileName, user.userName, user.groupName, false);
	}

	// 创建目录操作
	public static String mkdir(OSUser user, String FileName) {
		Dentry current = user.current;
		return current.creatFile(FileName,user.userName, user.groupName, true);
	}

	// 删除文件操作
	public static String rm(OSUser user, String FileName) {
			
		Dentry current = user.current;
		return current.deleteFile(user, FileName);
		
		
		
	}

	
	//删除目录操作
	// 删除文件操作
	public static String rmdir(OSUser user, String FileName) {
			
		Dentry current = user.current;
		if(current.dirTree.containsKey(FileName) == false)
		{
			return "<Error> target File [" + FileName + "] does not exist!";
		}
		
		if(user!= OSConfig.root || OSConfig.onlineUsers.size() >0)
		{
			return "Permission Deined!";
		}
		
		 current.deleteDirectory(current.dirTree.get(FileName).inode, current);
		
		 return "successfully remove directory "+FileName;
		
		
	}
	
	
	
	
}
