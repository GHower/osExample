package jobs;

import process.PCB;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 将LinkedList封装 以适应JCB操作
 * 保留原生操作
 */
public class JCBLinkedList {
    public LinkedList<JCB> linkedList;

    public JCBLinkedList() {
        this.linkedList = new LinkedList<JCB>();
    }

    public boolean isEmpty() {
        return linkedList.size() == 0;
    }

    public ArrayList<JCB> toArrayList(){
        return new ArrayList<>(linkedList);
    }

    public void timeWaitedPlus() {
        for (JCB jcb : linkedList) {
            // 时间+1
            jcb.pcb.process.time_waited++;
        }
    }
    /**
     *    以下为原生,其实继承就好...
     */
    public void addLast(JCB jcb){
        linkedList.addLast(jcb);
    }
    public void addFirst(JCB jcb){
        linkedList.addFirst(jcb);
    }
    public void add(int index,JCB jcb){
        linkedList.add(index,jcb);
    }
    public void remove(int index){
        linkedList.remove(index);
    }
    public void remove(JCB jcb){
        linkedList.remove(jcb);
    }
    public JCB removeLast(){
        return linkedList.removeLast();
    }
    public JCB removeFirst(){
        return linkedList.removeFirst();
    }
    public int size(){
        return linkedList.size();
    }
}
