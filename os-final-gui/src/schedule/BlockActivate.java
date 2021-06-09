package schedule;


import config.OSConfig;
import process.PCB;

/**
 * 阻塞和激活进程
 */
public class BlockActivate {

	// 阻塞某个进程，并且输入这个进程阻塞的理由
	public static void block(PCB pcb) {
		OSConfig.running_list.remove(pcb);
		OSConfig.block_list.addFirst(pcb);
		pcb.flag = 1;
	}
	
	
	public static void activate(PCB pcb) {
		OSConfig.block_list.remove(pcb);
		OSConfig.ready_list.addLast(pcb);
		pcb.flag = 0;
		pcb.blockType = -1;
		pcb.Message = null;
		pcb.info = null;		
	}
}
