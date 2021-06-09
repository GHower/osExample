package disk_management;

import config.OSConfig;
import file_system.Dentry;

import java.io.Serializable;

public class SuperBlock implements Serializable{
	//每个盘块大小(单位:Bytes)
	public int blockSize = 64;
	//位示图大小（长/宽）
	public int row_num = 20;
	public VectorMap vectorMap;
	
	public SuperBlock() {
		vectorMap = new VectorMap(row_num, blockSize);
	}
	
	// 得到磁盘中空闲盘块信息
	public String getFreeDisk() {
		return vectorMap.freeDiskNum+"*"+blockSize+" Bytes";
	}
	
	// 得到inode结点总数
	public int getTotalFiles()
	{
		return Dentry.getTotalDirNum(OSConfig.ROOT)+Dentry.getTotalFileNum(OSConfig.ROOT);
	}
	
	
	
	
}
