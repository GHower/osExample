package os.service.impl;

import os.OSMain;
import os.model.entity.MyJCB;
import os.service.JobService;

import java.util.LinkedList;
import java.util.List;

/**
 * 作业服务类
 * 1. 提供测试数据，生成随机的几个作业
 */
public class JobServiceImpl implements JobService {
    public int[] available;
    /**
     * 随机生成测试JCB
     *
     * @return JCB数组
     */
    public LinkedList<MyJCB> testJCB(int n) {
        LinkedList<MyJCB> myJCBS = new LinkedList<>();
        for(int i=1;i<=n;i++){
            MyJCB myJCB = new MyJCB();
            myJCB.setId(i);
            myJCB.setUsername("ghower");
            myJCB.setName("J"+i);
            myJCB.setPriority((int) Math.floor(Math.random()*5+1));
            myJCB.setType(1);
//            myJCB.setState((int) Math.floor(Math.random()*3)+1);
            myJCB.setState(1);
            myJCB.setArriveTime(0L);
//            myJCB.setArriveTime((long) Math.floor(Math.random()*5));
            // 生成1M以内的大小
            myJCB.setSize((int) Math.floor(Math.random()*1024*1024+1));
            myJCBS.addLast(myJCB);
        }
        available = OSMain.available;
        System.out.println(available);
        return OSMain.dispatchService.FCFS(myJCBS);
    }
}
