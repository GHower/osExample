package common;

import config.OSConfig;
import process.PCB;

import java.util.*;

//银行家算法
public class BankAlgorithm {
    // 当前发起请求的进程名称
    String pName;
    // 尝试分配
    Map<String, Integer> available;
    // 系统上可用资源数量
    Map<String, Integer> availableMax;
    Map<String, Integer> request;
    // <进程名,资源对象> <资源名,数量>
    Map<String, Map<String, Integer>> allPcbMax;
    Map<String, Map<String, Integer>> allPcbAllocation;
    List<String> safeQueue;

    private BankAlgorithm() {
        allPcbMax = new HashMap<>();
        allPcbAllocation = new HashMap<>();
        safeQueue = new ArrayList<>();
    }

    public static boolean checkSafe(PCB request) {
        // 初始化算法数据
        BankAlgorithm bank = initData(request);
        // 算法操作
        if (bank.checkNeedAndAvailable()) {//need和available必须满足
            // 尝试分配
            bank.tryAllocation();
            // 检查 试分配后系统是否安全,安全性检查子算法
            return bank.checkSubSafe();
        }
        return false;
    }

    private boolean checkSubSafe() {
        // 遍历全部pcb
        for (String pn : allPcbMax.keySet()) {
            // 找满足分配的pcb，这个pcb没有被安全队列记录
            boolean b = true;
            Map<String, Integer> need = getNeed(pn);
            // 看看need能不能满足
            for (String rn : need.keySet()) {
                if (need.get(rn) > available.get(rn)) {
                    b = false;
                    break;
                }
            }

            // 这个进程符合分配且不在安全序列中
            if (b && !safeQueue.contains(pn)) {
                safeQueue.add(pn);
                // 让这个进程完成,资源释放回到available
                for (String key : available.keySet()) {
                    available.put(key, available.get(key) + allPcbAllocation.get(pn).get(key));
                }
                // 一次添加完成
                checkSubSafe();
                return true;
            }
        }
        return false;
    }

    // 试分配,可以缩合
    private void tryAllocation() {
        for (String key : request.keySet()) {
            Integer dec = request.get(key);
            // 当前资源已分配
            Integer curAll = allPcbAllocation.get(pName).get(key);
            // 当前资源总可用
            Integer curMax = available.get(key);
            allPcbAllocation.get(pName).put(key, curAll + dec);
            available.put(key, curMax - dec);
        }
    }

    // 检查need和available
    private boolean checkNeedAndAvailable() {
        Set<String> keys = request.keySet();
        Map<String, Integer> need = getNeed(pName);

        for (String key : keys) {
            Integer num = request.get(key);
            if (num <= need.get(key) && num <= available.get(key)) {
                return true;
            }
        }
        return false;
    }

    // 初始化算法数据
    private static BankAlgorithm initData(PCB request) {
        BankAlgorithm bankAlgorithm = new BankAlgorithm();
        // 记录发起请求的进程名
        bankAlgorithm.pName = request.process.name;
        // 系统可用资源
        bankAlgorithm.available = new HashMap<>(OSConfig.available);
        bankAlgorithm.availableMax = OSConfig.available;
        bankAlgorithm.request = new HashMap<>(request.process.request);

        // 装载涉及到相关资源的pcb
        Set<String> strings = bankAlgorithm.request.keySet();
        List<PCB> runningExist = OSConfig.running_list.getAllExistAllocation(strings);
        List<PCB> readyExist = OSConfig.ready_list.getAllExistAllocation(strings);
        List<PCB> blockExist = OSConfig.block_list.getAllExistAllocation(strings);
        bankAlgorithm.setMaxAndAllocation(runningExist);
        bankAlgorithm.setMaxAndAllocation(readyExist);
        bankAlgorithm.setMaxAndAllocation(blockExist);

        return bankAlgorithm;
    }

    // 设置max和allocation
    private void setMaxAndAllocation(List<PCB> list) {
        for (PCB pcb : new ArrayList<>(list)) {
            this.allPcbMax.put(pcb.process.name, new HashMap<>(pcb.process.max));
            this.allPcbAllocation.put(pcb.process.name, new HashMap<>(pcb.process.allocation));
        }
    }

    // 给出进程名计算得到need
    private Map<String, Integer> getNeed(String pName) {
        Map<String, Integer> need = new HashMap<>();
        Map<String, Integer> pMax = allPcbMax.get(pName);
        Map<String, Integer> pAllocation = allPcbAllocation.get(pName);

        Set<String> rKeySet = pMax.keySet();
        for (String rKey : rKeySet) {
            int i = pMax.get(rKey) - pAllocation.get(rKey);
            need.put(rKey, i);
        }
        return need;
    }
}