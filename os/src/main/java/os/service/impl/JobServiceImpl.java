package os.service.impl;

import os.OSMain;
import os.model.entity.MyJCB;
import os.service.JobService;
import os.utils.Algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 * 作业服务类
 * 1. 提供测试数据，生成随机的几个作业
 */
public class JobServiceImpl implements JobService {
    static int jid=1;
    /**
     * 随机生成测试JCB
     *
     * @return JCB数组
     */
    public LinkedList<MyJCB> testJCB(int n) {
        LinkedList<MyJCB> myJCBS = new LinkedList<>();
        for(int i=1;i<=n;i++){
            MyJCB myJCB = new MyJCB();
            myJCB.setId(jid);
            myJCB.setUsername("ghower");
            myJCB.setName("J"+jid);
            myJCB.setPriority(Algorithm.randomRange(1,5));
            myJCB.setType(1);
            myJCB.setState(1);
            myJCB.setArriveTime(OSMain.time);
            myJCB.setRqTime(Algorithm.randomRange(1L,5L));
            // 生成1M以内的大小
            myJCB.setSize((int) Math.floor(Math.random()*1024*1024+1));
            myJCBS.addLast(myJCB);
            jid++;
        }
        return myJCBS;
    }
}
