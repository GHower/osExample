package memory.partition;

import config.OSConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * 自己管理linkedList
 */
public class Mem_LinkedList {
    LinkedList<Mem> linkedList = new LinkedList<>();
    int point = 0;//游标，指向当前链表位置

    public Mem_LinkedList() {
    }


    public Mem_LinkedList(int start, int size) {
        Mem mem = new Mem(start, size);
        addLast(mem);
    }

    /**
     * 以下为原生
     */
    public void addLast(Mem pcb) {
        linkedList.addLast(pcb);
    }

    public void addFirst(Mem pcb) {
        linkedList.addFirst(pcb);
    }

    // todo: 始终按起始地址排序
    public void add(int index, Mem mem) {
        linkedList.add(index, mem);
    }

    public Mem remove(int index) {
        return linkedList.remove(index);
    }

    public boolean remove(Mem mem) {
        return linkedList.remove(mem);
    }

    public Mem removeLast() {
        return linkedList.removeLast();
    }

    public Mem removeFirst() {
        return linkedList.removeFirst();
    }

    public Mem get(int index) {
        return linkedList.get(index);
    }

    public Mem set(int index, Mem pcb) {
        return linkedList.set(index, pcb);
    }

    public int size() {
        return linkedList.size();
    }


    /**
     * 以下是自己的方法
     */
    // 添加到后面
    public void addBehind(int idx, Mem newMem) {
        linkedList.add(idx, newMem);
    }

    // 通过地址查出内存
    public Mem getMemoryByAddr(int start) {
        return linkedList.stream()
                .filter(e -> e.start == start)
                .findFirst()
                .orElse(null);
    }

    // 得到空闲内存区最大的块
    public Mem getMaxBlock(int min) {
        int max = OSConfig.MAX_MEMORY + 100;
        return linkedList.stream()
                .max(Comparator.comparingInt(e -> {
                    if (e.size > min) return e.size;
                    else return -1;
                }))
                .orElse(null);
    }

    // 得到空闲内存区最小的块,但这个块必须大于所需要分配的内存大小
    public Mem getMinBlock(int min) {
        int max = OSConfig.MAX_MEMORY + 100;
        return linkedList.stream()
                .min(Comparator.comparingInt(e -> {
                    if (e.size >= min && e.size < max) return e.size;
                    else return Integer.MAX_VALUE;
                }))
                .orElse(null);
    }

    // 从point位置开始，得到第一个符合要求的内存区域,下次适应算法
    public Mem getFirstBlock(int min) {
        if (size() == 0) {
            return null;
        }
        // 跳过前point个
        return linkedList.stream().filter(e -> {
            return e.size >= min;
        }).findFirst().orElse(null);
    }

    public ArrayList<Mem> toArrayList() {
        return new ArrayList<>(linkedList);
    }

    public int getTotalSize() {
        return linkedList.stream().mapToInt(e -> e.size).sum();
    }

    // 对应内存块切割,返回切出来的内存块
    public Mem cut(Mem firstBlock, int size) {
        Mem newMem = new Mem(firstBlock.start, size);
        // 切完后的块
        firstBlock.start = firstBlock.start + size;
        firstBlock.size = firstBlock.size - size;
        // 切完没了，就删除
        if (firstBlock.start >= firstBlock.end || firstBlock.start <= 0) {
            remove(firstBlock);
        }
        return newMem;
    }

    @Override
    public String toString() {
        return linkedList.toString();
    }

    // 块用完了，需要回到空闲链表中
    public void releaseAndAddBack(Mem mem) {
        // 主要会有 两种 前后特殊情况，四种 中间合并情况,
        // del代表要删除的块，pre是del块前的空闲块，next是del块前后的空闲块
        // head 是链表头，rear是链表尾
        // 1. del在head之前 ， 将del插入链表头， 检查合并
        // 2. rear和del相连， 将del插入链表尾， 检查合并
        // 3. 四种合并
        //      3.1 pre,del,next不相连,插入到next之前，检查合并
        //      3.2 pre,del,next相连，插入到next之前，检查合并
        //      3.3 pre,del相连,插入到pre之后(其实也是next之前),检查合并
        //      3.4 del,next相连,插入到next之前,检查合并
        // 可见四种合并插入位置是一样的,只有检查合并操作不同
        // 队列为空
        if (size() == 0) {
            return;
        }
        // 如果这个块应该放在末尾，直接插入末尾
        if (mem.start >= linkedList.getLast().end) {
            linkedList.addLast(mem);
            mergeMemory(mem);
            return;
        }
        // 如果这个块应该放在头部，直接插入头部
        if (mem.end <= linkedList.getFirst().start) {
            linkedList.addFirst(mem);
            mergeMemory(mem);
            return;
        }
        // 四种合并只需找到next这个合适位置
        for (Mem next : linkedList) {
            if (mem.end <= next.start) {
                linkedList.add(linkedList.indexOf(next), mem);
                mergeMemory(mem);
                return;
            }
        }

    }

    // 检查合并,只对附近两块检查
    void mergeMemory(Mem mem) {
        int cur = linkedList.indexOf(mem);
        int next = Math.min(size()-1, cur + 1);
        int pre = Math.max(0, cur - 1);
        // 三块合并
        if (pre != cur && next != cur
                && linkedList.get(pre).end + 1 >= mem.start
                && linkedList.get(next).start - 1 <= mem.end) {
            linkedList.get(pre).size = linkedList.get(next).size + linkedList.get(pre).size + mem.size;
            linkedList.get(pre).end = linkedList.get(next).end;
            // 移除两块,或者对cur位置移除两次也是一样的
            linkedList.remove(next);
            linkedList.remove(cur);
        }
        // 前中合并
        else if (pre != cur && linkedList.get(pre).start - 1 >= mem.end) {
            linkedList.get(pre).size = linkedList.get(pre).size + mem.size;
            linkedList.get(pre).end = mem.end;
            // 移除
            linkedList.remove(cur);
        }
        // 中后合并
        else if (next!=cur && linkedList.get(next).start - 1 <= mem.end) {
            linkedList.get(cur).size = linkedList.get(next).size + mem.size;
            linkedList.get(cur).end = linkedList.get(next).end;
            linkedList.remove(next);
        }
        // 不合并不处理就行
    }
}
