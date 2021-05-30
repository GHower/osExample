package os.model.entity;

import os.enums.MyStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * pcb池对象，复杂对象，含很多对自身操作的方法
 */
public class MyPCBPool {
    private List<MyPCB> pcbPool;
    private int maxSize = 10;

    public List<MyPCB> getPcbPool() {
        return pcbPool;
    }

    public MyPCBPool(int size) {
        this.maxSize = size;
        this.setPcbPool(new ArrayList<>(this.maxSize));
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

    public boolean hasNext(){
        return this.maxSize != pcbPool.size();
    }

    /**
     * 分配空的pcb
     */
    public MyPCB allocation(MyProcess myProcess,MyJCB myJCB){
        MyPCB myPCB = new MyPCB();
        myPCB.setPid(myProcess.getId());
        myPCB.setStatus(MyStatus.READY);
        myPCB.setPriority(1);
        myPCB.setJid(myJCB.getId());
        myPCB.setPriority(myJCB.getPriority());
        myPCB.setName(myProcess.getName());
        pcbPool.add(myPCB);
        return myPCB;
    }

    public boolean remove(MyPCB myPCB){
        return pcbPool.remove(myPCB);
    }
}
