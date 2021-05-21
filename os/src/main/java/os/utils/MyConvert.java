package os.utils;

import os.enums.MyStatus;
import os.model.entity.MyJCB;
import os.model.entity.MyPCB;

/**
 * 转换类
 * 1. JCB转为PCB，即为一个作业创建一个进程，
 *      1.1 调用前需要检查内存能不能分配
 *      1.2 todo: 后期修改为多个进程共同完成同一作业，即返回的是List
 */
public class MyConvert {

    public static MyPCB convert(MyJCB myJCB){
        MyPCB myPCB = new MyPCB();
        myPCB.setPid(1);
        myPCB.setPriority(myJCB.getPriority());
        myPCB.setStatus(MyStatus.READY);
        return myPCB;
    }
}
