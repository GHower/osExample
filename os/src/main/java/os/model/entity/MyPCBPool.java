package os.model.entity;

import os.enums.MyStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * pcb池对象，复杂对象，含很多对自身操作的方法
 */
public class MyPCBPool {
    private List<MyPCB> pcbPool;
    private int maxSize;
    public int pidPosition = 0;
    public boolean[] pidVisited;

    public MyPCBPool(int size) {
        this.maxSize = size;
        this.setPcbPool(new ArrayList<>());
        this.pidVisited = new boolean[this.maxSize];
    }
   
    public List<MyPCB> getPcbPool() {
        return pcbPool;
    }


    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setPcbPool(List<MyPCB> pcbPool) {
        this.pcbPool = pcbPool;
    }

    public int size() {
        return pcbPool.size();
    }

    public boolean hasNext() {
        return this.maxSize != pcbPool.size();
    }

    /**
     * 分配空的pcb
     */
    public MyPCB allocation(MyPCB myPCB) {
        pcbPool.add(myPCB);
        pidVisited[myPCB.getPid()-100] = true;
        return myPCB;
    }
    public int nextPid() {
        // 已占有就跳过,遇到第一个false就结束,前提是有空闲
//        System.out.println(Arrays.toString(pidVisited));
        do{
            pidPosition = 100+((pidPosition+1) % this.maxSize);
        }while(hasNext() && pidVisited[pidPosition-100]);
        return pidPosition;
    }
    
    /**
     * 移除PCB
     */
    public boolean remove(MyPCB myPCB) {
        pidVisited[myPCB.getPid()-100] = false;
        return pcbPool.remove(myPCB);
    }
    /**
     *
     */
    public MyPCB getPcbByPid(Integer pid){
        Optional<MyPCB> optional = pcbPool.stream().filter(e -> e.getPid().equals(pid)).findFirst();
        return optional.orElse(null);
    }

    /**
     * 空闲pcb数量
     * @return
     */
    public int getRemain(){
        return maxSize-pcbPool.size();
    }
    @Override
    public String toString() {
        return "MyPCBPool{" +
                "pcbPool=" + pcbPool +
                ", maxSize=" + maxSize +
                '}';
    }
}
