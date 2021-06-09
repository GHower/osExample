package memory.partition;

/***
 * 分区存储［连续的内存］
 */

public class Memory {
    int size;// 指定大小
//    int activeProcessNum = 0;        //内存中内活跃进程（内存）数量
//	int totalProcessNum = 0;            //页面内所包含进程的数量
	// 空闲链表
    public Mem_LinkedList blankList;
    // 使用中的
    public Mem_LinkedList usedList = new Mem_LinkedList();


    public Memory(int size) {
        blankList = new Mem_LinkedList(0, size);
        this.size = size;
    }
}

