package os.service.impl;

import os.OSMain;
import os.model.entity.MyJCB;
import os.model.entity.MyMemory;
import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.service.MemoryService;
import os.service.ProcessService;
import os.utils.MyConvert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 内存服务
 */
public class MemoryServiceImpl implements MemoryService {
    // 整块的内存
    List<MyMemory> allMemory = new ArrayList<>();
    // 所有进程
    List<MyProcess> myProcesses = new ArrayList<>();
    // 空闲内存链表
    LinkedList<MyMemory> idleLinked = new LinkedList<>();

    public MemoryServiceImpl() {
        MyMemory initMemory = new MyMemory(0, OSMain.MEMORY_MAX_SIZE, false);
        idleLinked.add(initMemory);
        allMemory.add(initMemory);
    }

    /**
     * todo: 判断内存是否可以分配给作业
     *
     * @param myJCB 传入当前需要分配内存的作业
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyJCB myJCB) {
        for (MyMemory myMemory : idleLinked) {
            if (myMemory.getSize() >= myJCB.getSize()) return true;
        }
        return false;
    }

    /**
     * todo: 判断内存是否可以分配
     *
     * @param myPCB 传入当前需要分配内存的进程
     * @return 是否可以给这个进程分配内存
     */
    public boolean hasAllocation(MyPCB myPCB) {
        for (MyMemory myMemory : idleLinked) {
            if (myMemory.getSize() > myPCB.getSize()) return true;
        }
        return false;
    }

    /**
     * 分配内存的操作
     *
     * @param myPCB .
     */
    public MyPCB allocation(MyPCB myPCB, MyProcess myProcess) {
        // 记录该进程
        myProcesses.add(myProcess);
        // 调整内存对象中的空闲链表
        idleLinked = idleLinked.stream()
                .sorted(Comparator.comparingInt(MyMemory::getAddress))
                .collect(Collectors.toCollection(LinkedList::new));
        // 改变该位置内存的起始地址和大小
        int index = firstFit(myPCB.getSize());
//        int i = firstFit(allMemory, myPCB.getSize());
        MyMemory oldMemory = idleLinked.get(index);
        MyMemory newMemory = new MyMemory();
        newMemory.setAddress(oldMemory.getAddress() + myPCB.getSize());
        newMemory.setSize(oldMemory.getSize() - myPCB.getSize());
        newMemory.setState(false);
        // 进程放的地方
        myPCB.setAddr(oldMemory.getAddress());

        // 修改空闲链表和全部内存中的的记录
        idleLinked.set(index, newMemory);
//        allMemory.set(i, newMemory);
//        allMemory.stream()
//                .filter(e -> e.getAddress().equals(oldMemory.getAddress()))
//                .findFirst();
        // 给pcb记录使用的起始地址
        // 放进全部内存中
//        allMemory.add(new MyMemory(myPCB.getAddr(), myPCB.getSize(), true));
//        allMemory.sort(Comparator.comparingInt(MyMemory::getAddress));
//        display();
        if (needCompact()) {
            System.out.println("分配后内存使用已超过80%!!");
            System.out.print("是否需要进行内存紧凑(开销大)？y/n:");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if(s.toUpperCase().equals("Y"))
                compact();
        }
        return myPCB;
    }

    @Override
    public MyProcess getProcessByPid(Integer pid) {
        Optional<MyProcess> Optional = myProcesses.stream()
                .filter(e -> e.getId().equals(pid))
                .findFirst();
        return Optional.orElse(null);
    }

    @Override
    public void putProcessByPid(MyProcess myProcess) {
        for (int i=0;i< myProcesses.size();i++) {
            if(myProcesses.get(i).getId().equals(myProcess.getId())){
                myProcesses.set(i,myProcess);
                return;
            }
        }
    }

    /**
     * todo: 回收内存
     */
    public boolean remove(MyPCB myPCB) {
        Integer pid = myPCB.getPid();
        // 移除这个pid的进程记录
        myProcesses = myProcesses.stream()
                .filter(e -> !e.getId().equals(pid))
                .collect(Collectors.toList());
        // pcb池处理
        List<MyPCB> pcbPool = OSMain.pcbPool.getPcbPool();
        pcbPool = pcbPool.stream()
                .filter(e-> !e.getPid().equals(myPCB.getPid()))
                .collect(Collectors.toList());
        OSMain.pcbPool.setPcbPool(pcbPool);

        // 移除这个pid的内存使用,所有内存的方式
//        int i;
//        for (i = 0; i < allMemory.size(); i++) {
//            if (allMemory.get(i).getAddress().equals(myPCB.getAddr())) {
//                MyMemory cur = allMemory.get(i);
//                MyMemory pre = allMemory.get(Math.max(i - 1, 0));
//                pre = pre != cur ? pre : null;
//                MyMemory next = allMemory.get(Math.min(i + 1, allMemory.size()));
//                next = next != cur ? next : null;
//                // 前后都是空闲
//                if ((pre != null && !pre.isState()) && (next != null && !next.isState())) {
//                    int newAddr = pre.getAddress();
//                    int size = pre.getSize() + cur.getSize() + next.getSize();
//                    allMemory.set(i - 1, new MyMemory(newAddr, size, false));
//                    // 移除cur和next
//                    allMemory.remove(i);
//                    allMemory.remove(Math.min(i + 1, allMemory.size()));
//                } else if (pre != null && !pre.isState()) {
//                    // 前一个为空闲
//                    int newAddr = pre.getAddress();
//                    int size = pre.getSize() + cur.getSize();
//                    allMemory.set(i - 1, new MyMemory(newAddr, size, false));
//                    //移除cur
//                    allMemory.remove(i);
//                } else if (next != null && !next.isState()) {
//                    // 后一个为空闲
//                    int newAddr = cur.getAddress();
//                    int size = cur.getSize() + next.getSize();
//                    allMemory.set(i, new MyMemory(newAddr, size, false));
//                    // 移除next
//                    allMemory.remove(Math.min(i + 1, allMemory.size()));
//                } else {
//                    cur.setState(false);
//                    allMemory.set(i, cur);
//                }
//                // 重新收集空闲链表
//                idleLinked = allMemory.stream()
//                        .filter(e -> !e.isState())
//                        .sorted(Comparator.comparingInt(MyMemory::getAddress))
//                        .collect(Collectors.toCollection(LinkedList::new));
//                // PCB池回收PCB
//                OSMain.pcbPool.remove(myPCB);
//                break;
//            }
//        }
        // 移除内存使用,空闲链表的方式
        // fixme: 合并有问题，待改
        int idleLen = idleLinked.size();
        int i = 0;
        int delStart = myPCB.getAddr();
        int delEnd = myPCB.getAddr() + myPCB.getSize();
        for (i = 0; i < idleLen; i++) {
            MyMemory cur = idleLinked.get(i);
            // 找到第一个空闲起始位不小于删除点的
            if (myPCB.getAddr() <= cur.getAddress()) {
                //看看有没有左边的一个
                MyMemory pre = i-1 >=  0 ? idleLinked.get(i - 1) : null;
                // 找到了空闲块，但是前面没有空闲块
                if (pre == null) {
                    // 如果是连着的，就只修改当前空闲块
                    if (cur.getAddress().equals(myPCB.getAddr() + myPCB.getSize())) {
                        cur.setAddress(myPCB.getAddr());
                        cur.setSize(cur.getSize() + myPCB.getSize());
                        idleLinked.set(i, cur);
                    } else { //不是连着的
                        idleLinked.addFirst(new MyMemory(myPCB.getAddr(), myPCB.getSize(), false));
                    }
                } else {
                    // 前面存在空闲块,有4种情况
                    // 1. 删除点只和pre相连
                    // 2. 删除点只和cur相连
                    // 3. 三点相连
                    // 4. 三点不相连
                    int preEnd = pre.getAddress() + pre.getSize();
                    int curAddr = cur.getAddress();
                    if (preEnd != delStart && curAddr != delEnd ) {
                        // 4. 三点不相连
                        idleLinked.add(i, new MyMemory(myPCB.getAddr(), myPCB.getSize(), false));
                    } else if (delStart == preEnd  && delEnd  == curAddr) {
                        // 3. 三点相连
                        pre.setSize(pre.getSize() + cur.getSize() + myPCB.getSize());
//                        cur.setAddress(pre.getAddress());
                        idleLinked.set(i - 1, pre);
                        idleLinked.remove(i);
                    } else if (delEnd  == curAddr) {
                        // 2. 删除点只和cur相连,合并
                        cur.setAddress(delStart);
                        cur.setSize(myPCB.getSize() + cur.getSize());
                        idleLinked.set(i, cur);
                    } else {
                        // 1. 删除点只和pre相连
                        pre.setSize(pre.getSize() + myPCB.getSize());
                        idleLinked.set(i - 1, pre);
                    }
                }
                return true;
            }
            // 删除点在最后
            else if(i==idleLen-1){
//                MyMemory last = idleLinked.getLast();
                int end = cur.getSize() + cur.getAddress();
                if(myPCB.getAddr().equals(end)){
//                    idleLinked.getLast().setSize(cur.getSize()+cur.getSize());
                    idleLinked.getLast().setSize(cur.getSize()+cur.getSize());
                }else{
                    idleLinked.addLast(new MyMemory(myPCB.getAddr(), myPCB.getSize(), false));
                }
            }
        }
        return false;
    }

    @Override
    public void compact() {// 内存紧凑
        System.out.println("正在进行内存紧凑");
        int addr = 0;
        List<MyPCB> pcbPool = OSMain.pcbPool.getPcbPool();
        List<MyPCB> result = new ArrayList<>();
        for (MyPCB next : pcbPool) {
            next.setAddr(addr);
            addr += next.getSize() + 1;
            result.add(next);
        }
        idleLinked = new LinkedList<>();
        idleLinked.addLast(new MyMemory(addr,OSMain.MEMORY_MAX_SIZE-addr,false));
        // 重新设置PCB
        OSMain.pcbPool.setPcbPool(result);
    }
    // 检查需不需要紧凑  todo 待检查！
    private boolean needCompact(){
        double sum = idleLinked.stream().mapToDouble(MyMemory::getSize).sum();
        return OSMain.MEMORY_MAX_SIZE / sum < 0.8;
    }
    /**
     * firstFit 首次适应算法
     *
     * @return 符合条件的空闲内存块下标
     */
    private int firstFit(Integer size) {
        for (int i = 0; i < idleLinked.size(); i++) {
            if (idleLinked.get(i).getSize() > size)
                return i;
        }
        return -1;
    }

    private int firstFit(List<MyMemory> list, Integer size) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSize() > size)
                return i;
        }
        return -1;
    }

    /**
     * 删除时，查找空闲链表的下标
     */
    private int findIdxOfRm(MyPCB myPCB) {
        return 0;
    }

    /**
     * 输出内存情况
     */
    public void display() {
        System.out.println("起始地址\t占用大小\t使用情况");
        allMemory.sort(Comparator.comparingInt(MyMemory::getAddress));
        for (MyMemory myMemory : allMemory) {
            System.out.println(MyConvert.formatSize(myMemory.getAddress()) +
                    "\t" + MyConvert.formatSize(myMemory.getSize()) +
                    "\t" + (myMemory.isState() ? "占用" : "空闲"));
        }
    }

    public void display2() {
        System.out.println("起始地址\t占用大小\t使用情况");
        allMemory.sort(Comparator.comparingInt(MyMemory::getAddress));
        for (MyMemory myMemory : allMemory) {
            System.out.println(myMemory.getAddress() +
                    "\t" + myMemory.getSize() +
                    "\t" + (myMemory.isState() ? "占用" : "空闲"));
        }
    }
    // 针对空闲链表的输出方式
    public void display3() {
        System.out.println("起始地址\t空间大小\t使用情况");
        int start=0;
        for (MyMemory myMemory : idleLinked) {
            if (myMemory.getAddress()!=start) {
                System.out.println(start+
                        "\t" + myMemory.getAddress() +
                        "\t" + "占用");
            }
            System.out.println(myMemory.getAddress()+
                    "\t" + myMemory.getSize() +
                    "\t" + "空闲");
            start = myMemory.getAddress()+myMemory.getSize();
        }
        if(start!=OSMain.MEMORY_MAX_SIZE){
            System.out.println((start)+
                    "\t" + OSMain.MEMORY_MAX_SIZE +
                    "\t" + "占用");
        }
    }
    // 只显示空闲链表
    @Override
    public void display4() {
        System.out.println("起始地址\t空间大小\t使用情况");
        for (MyMemory myMemory : idleLinked) {
            System.out.println(myMemory.getAddress()+
                    "\t" + myMemory.getSize() +
                    "\t" + "空闲");
        }
    }

    @Override
    public List<MyProcess> getAllProcess() {
        return this.myProcesses;
    }
}
