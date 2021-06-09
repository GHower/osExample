package process;

import config.OSConfig;
import schedule.BlockActivate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 将LinkedList封装 以适应PCB操作
 */
public class PCBLinkedList {
    int maxSize;
    LinkedList<PCB> linkedList;

    public PCBLinkedList() {
        linkedList = new LinkedList<>();
    }

    public PCBLinkedList(int maxSize) {
        this.maxSize = maxSize;
        linkedList = new LinkedList<>();
    }


    /**
     *    以下为原生
     */
    public void addLast(PCB pcb){
        linkedList.addLast(pcb);
    }
    public void addFirst(PCB pcb){
        linkedList.addFirst(pcb);
    }
    public void add(int index,PCB pcb){
        linkedList.add(index,pcb);
    }
    public PCB remove(int index){
        return linkedList.remove(index);
    }
    public boolean remove(PCB pcb){
        return linkedList.remove(pcb);
    }
    public PCB removeLast(){
        return linkedList.removeLast();
    }
    public PCB removeFirst(){
        return linkedList.removeFirst();
    }
    public PCB get(int index){
        return linkedList.get(index);
    }
    public PCB getByPid(int pid){
        return linkedList.stream().filter(e->e.PID==pid).findFirst().orElse(null);
    }
    public PCB set(int index, PCB pcb){
        return linkedList.set(index,pcb);
    }
    public int size(){
        return linkedList.size();
    }

    /**
     * 自己多加一点功能
     */
    public boolean isEmpty() {
        return linkedList.size() == 0;
    }
    public boolean isFull() {
        return linkedList.size() == maxSize;
    }
    // 移除已完成的进程，每次只返回一个，也可能是null
    public PCB removeFinished(){
        for (PCB pcb : linkedList) {
            if(pcb.time_needed == pcb.process.time_used){
                return pcb;
            }
        }
        return null;
    }
    // 返回可以运行的PCB，即阻塞标记是0的
    public PCB getActivePCB(){
        return linkedList.stream()
                .filter(e->e.flag==0)
                .findFirst()
                .orElse(null);
    }


    // 内存统计,将链表中所有内存对象内存大小加起来
    public int memoryStatics(){
        if(size()!=0){
            return linkedList.stream().mapToInt(e -> e.memory).sum();
        }
        return 0;
    }
    // todo:文件系统受限
    // 将阻塞的进程激活,这是给阻塞队列调用的
    public void block2ready() {

    }
    // 将阻塞的进程激活,这是给阻塞队列调用的
    public List<PCB> resetBlockByResource(){
        return linkedList.stream().filter(e -> {
                        if (e.flag == 1
                                && checkResource(e.process.request)) {
                            return true;
                        }
                        return false;
                }).collect(Collectors.toList());
    }
    // 资源检查
    private boolean checkResource(HashMap<String,Integer> request){
        boolean result=true;
        // 不申请资源 直接返回
        if(request==null)return true;
        for (String key : request.keySet()) {
            // 有一个资源不满足，就返回false
            if (OSConfig.available.get(key) < request.get(key)) {
                result = false;
            }
        }
        return result;
    }
    // 运行队列调用
    public void timeUsedPlus() {
        for (PCB pcb : linkedList) {
            // 先运行进程
            pcb.process.run();
            // 时间+1
            pcb.process.time_used++;
        }
    }
    // 阻塞队列调用
    public void timeWaitedPlus() {
        for (PCB pcb : linkedList) {
            // 时间+1
            pcb.process.time_waited++;
        }
    }

    public void checkBlockProcess() {
        List<PCB> collect = linkedList.stream()
                .filter(e -> e.flag == 1)
                .collect(Collectors.toList());
        for (PCB pcb : collect) {
            BlockActivate.block(pcb);
        }
    }

    public ArrayList<PCB> toArrayList(){
        return new ArrayList<>(linkedList);
    }
    // 返回链表中优先级最低的
    public PCB getMinPriority() {
        return linkedList.stream()
                .min(Comparator.comparingInt(value -> value.priority))
                .orElse(null);
    }
    // 返回链表中优先级最高的
    public PCB getMaxPriority() {
        return linkedList.stream()
                .max(Comparator.comparingInt(value -> value.priority))
                .orElse(null);
    }
    // 返回存在request请求的PCB
    public PCB getExistRequest(){
        return linkedList.stream()
                .filter(e->e.process.request!=null)
                .findFirst()
                .orElse(null);
    }
    // 返回已分配某组资源的pcb集合
    public List<PCB> getAllExistAllocation(Set<String> strings){
        List<PCB> result = new ArrayList<>();
        for (PCB pcb : linkedList) {
            for (String string : strings) {
                if (checkExistResource(pcb,string)) {
                    result.add(pcb);
                    break;
                }
            }
        }
        return result;
    }
    // 进程已分配某个资源
    private boolean checkExistResource(PCB pcb,String rName){
        for (String s : pcb.process.allocation.keySet()) {
            if (rName.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
