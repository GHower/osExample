package os.service.impl;

import os.model.entity.MyJCB;
import os.service.JobService;

import java.util.LinkedList;
import java.util.List;

/**
 * 作业服务类
 * 1. 提供测试数据，生成随机的几个作业
 */
public class JobServiceImpl implements JobService {
    /**
     * 随机生成测试JCB
     *
     * @return JCB数组
     */
    public LinkedList<MyJCB> testJCB() {
        LinkedList<MyJCB> myJCBS = new LinkedList<>();
        for(int i=1;i<=3;i++){
            MyJCB myJCB = new MyJCB();
            myJCB.setId(i);
            myJCB.setUsername("ghower");
            myJCB.setName("J"+i);
            myJCB.setPriority(1);
            myJCB.setType(1);
            myJCB.setArriveTime(Math.floor(Math.random()*3));
            myJCBS.addLast(myJCB);
        }
        return myJCBS;
    }
}
