package os.model.entity;

import os.enums.MyStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * pcb池对象，复杂对象，含很多对自身操作的方法
 */
public class MyPCBPool {
    private List<MyPCB> pcbPool;
    private int maxSize = 10;

    public MyPCBPool(int size) {
        this.maxSize = size;
        this.setPcbPool(new ArrayList<>(this.maxSize));
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
        return myPCB;
    }

    /**
     * 移除PCB
     */
    public boolean remove(MyPCB myPCB) {
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
