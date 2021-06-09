package jobs;

import process.PCB;

public class JCB {
    public int JID;
    // 作业类型：0为CPU型、1为I/O繁忙型、2为终端型
    public int JType;
    public PCB pcb;//对应的pcb
    // 计算周转时间和带权周转时间
    public double submitTime;// 提交时间
    public double startTime=-1;// 开始时间
    public double runTime;// 运行时间
    public double finishTime;// 完成时间

    public JCB(PCB pcb,int type) {
        this.pcb = pcb;
        JID = pcb.PID;
        JType = type;
    }
}
