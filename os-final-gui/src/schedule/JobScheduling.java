package schedule;

import config.OSConfig;
import jobs.JCB;
import process.PCB;

/***
 * 作业调度算法 在后备队列中调取相应的作业到内存，创建进程 （FCFS、SPN、HPF、HRRN）
 * 
 * 
 * 维持一个队列 利用队列完成各种算法
 */

public class JobScheduling {

	// 作业调度
	public static void jobScheduling() {

		switch (OSConfig.JOB_SCHEDULE_FLAG) {
		case 0:
			FCFS();
			break;
		case 1:
			SJF();
			break;
		case 2:
			HPF();
			break;
		case 3:
			HRRN();
			break;

		default:
			FCFS();
			break;
		}

	}

	// 判断内存中进程数目是否超标 或 内存已满
	public static boolean isMemoryFull() {

		if (OSConfig.PCB_POOL_SIZE <=
				OSConfig.running_list.size() +
						OSConfig.ready_list.size() +
						OSConfig.block_list.size())
			return true;
		// 所有内存加起来
		if (OSConfig.running_list.memoryStatics() +
				OSConfig.ready_list.memoryStatics() +
				OSConfig.block_list.memoryStatics()
				+ OSConfig.Kernel_SIZE >= OSConfig.MAX_MEMORY)
			return true;
		return false;

	}

	// 先来先服务(First Come First Server)，哪个先到先给哪个服务
	public static void FCFS() {
		// 运行队列有空闲，且后备队列中有作业在等待
		while (!isMemoryFull() && !OSConfig.jobsList.isEmpty()) {
			// 进了内存就移走JCB
			JCB jcb = OSConfig.jobsList.removeFirst();
			PCB pcb = jcb.pcb;
			OSConfig.ready_list.addLast(pcb);
			MemorySchedule.MemoryAllocate(pcb);
			// 设置开始时间,只需要一次设置
			if(jcb.startTime==-1){
				jcb.startTime = OSConfig.System_time;
			}
			// 记录这个作业
			OSConfig.JCB_MAP_RUNNING.put(jcb.JID, jcb);

			pcb.process.time_waited = 0;
		}

	}

	// 短作业优先(Short Job First)，用时最短的作业先被服务
	public static void SJF() {
	}

	// 高优先权调度(Highest Priority First)，找到最高优先权的进程
	public static void HPF() {
	}

	// 最高响应比优先调度(Highest Response Ratio Next)，找到最高响应比的进程
	// 响应比R定义如下： R =(W+T)/T = 1+W/T
	public static void HRRN() {
	}

}
