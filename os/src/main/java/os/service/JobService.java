package os.service;

import os.model.entity.MyJCB;

import java.util.LinkedList;
import java.util.List;

/**
 * 作业服务类
 * 1. 提供测试数据，生成随机的几个作业
 */
public interface JobService {
    /**
     * 随机生成测试JCB
     * @return JCB数组
     */
    LinkedList<MyJCB> testJCB();
}
