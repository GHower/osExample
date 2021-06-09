package disk_management;

import java.util.ArrayList;
import java.util.Vector;
// 磁盘读写处理
public class HandelDisk {

	//从磁盘中读取内容
	public static String readFromDisk(Vector<Integer> disks) {
		
		return DiskSchedulingAlgorithm.diskSchedulingAlgorithm(disks,0,null);
		
	}
	
	//向磁盘中写入内容
	public static String WriteToDisk(Vector<Integer> disks,ArrayList<String> files) {
		
		return DiskSchedulingAlgorithm.diskSchedulingAlgorithm(disks,1,files);
		
	}
	
	//抹除磁盘内容
	public static String ClearDisk(Vector<Integer> disks) {
		
		return DiskSchedulingAlgorithm.diskSchedulingAlgorithm(disks,2,null);
		
	}
	
	
	
	
	
}
