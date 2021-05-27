package os.service.impl;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyResource;
import os.service.ProcessService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 进程服务，提供针对进程的操作方法
 */
public class ProcessServiceImpl implements ProcessService {
    @Override
    public MyPCB getJcbByPid(Integer pid) {
        return null;
    }
    @Override
    public List<MyProcess> testProcess(int n) {
        return null;
    }
}
