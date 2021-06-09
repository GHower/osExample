package file_system;

import config.OSConfig;
import disk_management.HandelDisk;
import disk_management.iNode;
import users.OSUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;


/***
 *
 * 磁盘; 目录项;
 * 目录项存在内存中
 * 只保存文件名称和指向FCB的指针
 * 仿linux，一切皆是文件
 */
public class Dentry implements Serializable {

	public String path;// 文件路径

	public String dirName;// 文件名

	public boolean isDir;// 文件类型

	public iNode inode;// 指向该文件或目录的指针

	public TreeMap<String, Dentry> dirTree = new TreeMap<>();// 目录下的文件树

	public Dentry(String dirName, boolean isDir, iNode inode) {
		this.dirName = dirName;
		this.isDir = isDir;
		this.inode = inode;
	}

	public void setPath(String path) {
		this.path = path + "/" + dirName;
	}

	/***
	 *
	 * 文件数目统计
	 *
	 */

	// 得到磁盘中所有的文件数量
	public static int getTotalFileNum(Dentry father) {
		Dentry root = father;
		if (root.isDir == false)
			return 1;
		if (root.dirTree.size() == 0 && root.isDir == true)
			return 0;
		int sum = 0;
		for (String fileName : root.dirTree.keySet()) {
			sum += getTotalFileNum(root.dirTree.get(fileName));
		}

		return sum;

	}

	// 得到磁盘中所有的目录数量
	public static int getTotalDirNum(Dentry father) {
		Dentry root = father;
		if (!root.isDir)
			return 0;
		if (root.dirTree.size() == 0 && root.isDir)
			return 1;
		int sum = 0;
		for (String fileName : root.dirTree.keySet()) {
			sum += getTotalDirNum(root.dirTree.get(fileName));
		}

		return sum + 1;// 自己也是目录

	}

	// 得到当前文件夹的详细信息
	public static String getTotalNum(Dentry father) {

		return "Total: " + (getTotalFileNum(father) + getTotalDirNum(father)) + "  Directorys: "
				+ getTotalDirNum(father) + "  Files: " + getTotalFileNum(father);

	}

	/***
	 *
	 * 文件操作
	 *
	 */

	// 检查目录下是否存在重名文件
	public boolean containsFile(String name) {
		return dirTree.containsKey(name);
	}

	// 【查找文件】
	// 从绝对路径中查找到文件
	// 如果输入current就代表从当前路径开始
	public static Dentry searchFile(String path) {
		String[] paths = path.split("/");

		Dentry current = OSConfig.ROOT;
		if(!paths[0].equals("."))
			return null;
		int length = paths.length;
		for (int i = 1; i < length; i++) {
			String tmpPathString = paths[i];
			if (!current.dirTree.containsKey(tmpPathString))
				return null;
			current = current.dirTree.get(tmpPathString);
		}
		return current;
	}


	// 【更改当前路径】
	// 从绝对路径中查找到目录
	// 如果输入current就代表从当前路径开始
	public Dentry cd(String path) {
		Dentry current;
		String[] paths = path.split("/");
		if (paths[0].equals("current")) {
			current = this;
		} else {
			current = OSConfig.ROOT;
		}
		int length = paths.length;
		for (int i = 1; i < length; i++) {
			String tmpPathString = paths[i];
			if (!current.dirTree.containsKey(tmpPathString))
				return null;
			current = current.dirTree.get(tmpPathString);
		}
		if (current.isDir == false)
			return null;

		else
			return current;

	}

	// 【创建文件】
	// 在该目录下创建【文件】【子目录】
	// 返回为0代表创建成功；返回为1代表文件重名；返回为2代表磁盘空间不足
	public String creatFile(String name, String owner, String group, boolean isDir) {
		// 文件已重名
		if (containsFile(name) == true)
			return "<错误> 文件"+name+"已存在!";

		// 创建文件
		iNode newfile = new iNode(name, owner, group, 0, isDir);

		// 为文件分配内存
		if (OSConfig.superBlock.vectorMap.allocateDisk(newfile) == false)
			return "<错误> 磁盘空间不足 !";

		// 在该目录索引节点中加入该文件的索引节点
		dirTree.put(name, newfile.dentry);

		// 为该文件创立路径
		newfile.dentry.setPath(path);

		return "成功创建文件   " + name;
	}

	// 【删除文件】：删除该目录下的文件
	// 返回0代表删除成功
	// 返回1代表文件不存在
	// 返回2代表文件为目录且目录下还有文件
	public String deleteFile(OSUser user, String name) {

		// 该目录下不存在此文件
		if (containsFile(name) == false)
			return "<错误> target File [" + name + "] does not exist!";

		Dentry targetDentry = dirTree.get(name);

		// 文件为目录且目录不为空
		if (targetDentry.isDir == true && targetDentry.dirTree.size() != 0)
			return "<错误> cannot delete Directory (directory is not empty)!";

		if (user.userName.equals(targetDentry.inode.owner) || user.userName.equals("root")) {

			if (targetDentry.inode.write_locked == true || targetDentry.inode.read_locked > 0)
				return "<错误> File is busy !";

			// 从目录项中删除该结点，并释放内存
			iNode target = dirTree.get(name).inode;

			dirTree.remove(name);// 从dentry中删除该索引
			OSConfig.superBlock.vectorMap.releaseAllDisk(target);// 修改位示图
			if (target.diskTable.size() != 0)
				HandelDisk.ClearDisk(target.diskTable);// 抹除磁盘数据

			return "successfully  [Delete]   " + name;
		} else {
			return "<错误> Permission Denied !";
		}
	}

	// 【递归删除整个目录】
	// 递归地删除目录下所有的文件
	public void deleteDirectory(iNode file, Dentry fatherDentry) {

		// 如果该dentry对应的是文件而不是目录，删除它
		if (file.isDir == false) {
			// 父目录项中删除链接
			fatherDentry.dirTree.remove(file.name);

			// 回收磁盘空间
			OSConfig.superBlock.vectorMap.releaseAllDisk(file);

			if (file.diskTable.size() != 0)
				HandelDisk.ClearDisk(file.diskTable);// 抹除磁盘数据
		}

		else {

			// 如果该dentry对应地是目录，递归地删除它
			for (String filename : file.dentry.dirTree.keySet()) {
				Dentry localfatherDentry = file.dentry;
				iNode sonfile = localfatherDentry.dirTree.get(filename).inode;
				deleteDirectory(sonfile, localfatherDentry);
			}
			// 等待该目录全部删空之后......

			// 父目录项中删除链接
			fatherDentry.dirTree.remove(file.name);

			// 回收磁盘空间
			OSConfig.superBlock.vectorMap.releaseAllDisk(file);

			if (file.diskTable.size() != 0)
				HandelDisk.ClearDisk(file.diskTable);// 抹除磁盘数据

		}

	}

	// 【读文件】
	// 读取该文件内容，放入内存
	public String readFile(OSUser user, String FileName) {

		/***
		 * 从该文件对应的所有磁盘块中读取所有的字节，组合成完整的文件 (这个属于磁盘调度) 【接口】
		 * readFromDisk()就是从【磁盘】中读取相应的盘块内容，并整合到fileInfo中
		 *
		 */

		// 文件不存在
		if (dirTree.containsKey(FileName) == false)
			return "<错误> target File [" + FileName + "] does not exist!";

		Dentry File = dirTree.get(FileName);

		// 目标文件是目录
		if (File.isDir == true)
			return "<错误> target File [" + FileName + "] is directory!";

		// 权限不够
		if (FileSystemCommand.getAuthority(user, File, 0) == false)
			return "<错误> Permission Denied !";

		// 文件被锁住
		if (File.inode.write_locked == true)
			return "<错误> File is busy !";

		File.inode.read_locked++;
		String mess = HandelDisk.readFromDisk(File.inode.diskTable);
		File.inode.read_locked--;

		return mess;

	}

	// 【写文件】
	public String writeFile(OSUser user, String FileName, String info) {

		// 文件不存在
		if (dirTree.containsKey(FileName) == false)
			return "<错误> target File [" + FileName + "] does not exist!";

		Dentry File = dirTree.get(FileName);

		// 目标文件是目录
		if (File.isDir == true)
			return "<错误> target File [" + FileName + "] is directory!";

		// 权限不够
		if (FileSystemCommand.getAuthority(user, File, 1) == false)
			return "<错误> Permission Denied !";

		if (File.inode.read_locked > 0 || File.inode.write_locked == true)
			return "<错误> File is busy !";

		/***
		 * 检查完毕，开始写文件操作
		 */
		File.inode.write_locked = true;// 上锁

		// 从目录项中删除该结点，并释放内存
		iNode target = File.inode;

		OSConfig.superBlock.vectorMap.releaseAllDisk(target);// 修改位示图
		if (target.diskTable.size() != 0)
			HandelDisk.ClearDisk(target.diskTable);// 抹除磁盘数据

		// 更新文件大小信息
		target.size = info.length();

		// 为文件分配内存
		if (OSConfig.superBlock.vectorMap.allocateDisk(target) == false) {
			dirTree.remove(FileName);// 从dentry中删除该索引
			File.inode.write_locked = false;// 开锁
			return "<错误> no free disk,original file deleted!";
		}

		/***
		 * 把FileStream中的字符串写会磁盘，组合成完整的文件 (这个属于磁盘调度) 【接口】 writetoDisk()就是更新【磁盘】中相应的盘块内容
		 *
		 */
		ArrayList<String> list = new ArrayList<String>();

		int totalDisk = target.diskTable.size();// 总盘块数
		int length = OSConfig.superBlock.blockSize;// 一个盘块大小
		for (int index = 0; index < totalDisk; index++) {
			String childStr = substring(info, index * length, (index + 1) * length);
			list.add(childStr);
		}

		HandelDisk.WriteToDisk(target.diskTable, list);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		target.lastModifyData = df.format(new Date());// new Date()为获取当前系统时间

		File.inode.write_locked = false;// 开锁

		return "successfully write file";
	}

	public static String substring(String str, int f, int t) {
		if (f > str.length())
			return "";
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}

	@Override
	public String toString() {
		return dirName;
	}
}
