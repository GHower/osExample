package os.service.impl;

import os.model.entity.MyPCB;
import os.model.entity.MyResource;
import os.service.BankService;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行家算法实现类
 */
public class BankServiceImpl implements BankService {


    public BankServiceImpl(){
        setAvailable();
        setMax();
        setNeed();
    }
    /**
     * 可用资源，初始未全部可用
     */
    int[] available = {11, 9, 8};
    /**
     * 最大需求矩阵，每个进程各类资源最大需要
     */
    int[][] max = {{8,7,5},{5,2,5},{6,6,2},{1,1,1},{1,1,1}};
    /**
     * 分配矩阵，每个进程各类资源占用情况
     */
    int[][] allocation = {{3,2,0},{2,0,2},{1,3,2},{1,0,1},{1,1,0}};
    /**
     * 还需资源，每个进程各类资源还需情况
     */
    int[][] Need = new int[max.length][3];
    /**
     * 请求资源矩阵，某个进程t0时刻请求进程{{1,0,1}}
     */
    int[][] Request = new int[3][3];
    /**
     * 正在工作的总资源
     */
    int[] Work = new int[3];
    /**
     * 设置全局变量进程编号num
     */
    int num= 1;
    /**
     * to时刻请求资源
     */
    int[] req= {1,1,0};

    @Override
    public boolean checkSafe(List<MyPCB> pcbs) {
        for (MyPCB pcb : pcbs) {
            for (int i = 0; i < pcbs.size(); i++) {
                for ( int j = 0 ; j < available.length ; j++){
                    allocation[i][j]=pcb.getRequest()[i];
                }
            }
        }

        for (int[] ints : allocation) {
            System.out.println(ints);
        }
        return false;
    }


    /**
     * TODO:设置Max矩阵
     */
    private void setMax() {

    }

    /**
     * 设置Need矩阵
     */
    private void  setNeed(){
        for (int i = 0; i < max.length; i++) {//设置Need矩阵

            for (int j = 0; j < 3; j++) {
                Need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }
    /**
     * 设置Available矩阵
     */
    private  void setAvailable(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < max.length; j++) {
                available[i] = available[i] - allocation[j][i];
            }
        }

    }
    /**
     * 打印各进程资源分配表
     */
    public void printSystemVariable(){
        System.out.println("此时资源分配量如下：");
        System.out.println("进程  "+"   Max   "+"   Alloction "+"    Need  "+"     Available ");
        for(int i=0;i< max.length;i++){
            System.out.print("P"+i+"  ");
            for(int j=0;j<3;j++){
                System.out.print(max[i][j]+"  ");
            }
            System.out.print("|  ");
            for(int j=0;j<3;j++){
                System.out.print(allocation[i][j]+"  ");
            }
            System.out.print("|  ");
            for(int j=0;j<3;j++){
                System.out.print(Need[i][j]+"  ");
            }
            System.out.print("|  ");
            if(i==0){
                for(int j=0;j<3;j++){
                    System.out.print(available[j]+"  ");
                }
            }
            System.out.println();
        }
    }


    /**
     * 安全算法，打印出安全序列
     */
    public void SecurityAlgorithm() {
        Boolean Finish[] = new Boolean[max.length];//初始化Finish
        //设值
        for (int i = 0; i < Finish.length; i++) {
            Finish[i]=false;
        }

        int count = 0;//完成进程数
        int circle=0;//循环圈数
        int[] S=new int[max.length];//安全序列
        for (int i = 0; i < 3; i++) {//设置工作向量
            Work[i] = available[i];
        }
        boolean flag = true;
        while (count < max.length) {
            if(flag){
                System.out.println("进程  "+"   Work  "+"   Alloction "+"    Need  "+"     Work+Alloction ");
                flag = false;
            }
            for (int i = 0; i < max.length; i++) {

                if (Finish[i]==false&&Need[i][0]<=Work[0]&&Need[i][1]<=Work[1]&&Need[i][2]<=Work[2]) {//判断条件
                    System.out.print("P"+i+"  ");
                    for (int k = 0; k < 3; k++){
                        System.out.print(Work[k]+"  ");
                    }
                    System.out.print("|  ");
                    for (int j = 0; j<3;j++){
                        Work[j]+=allocation[i][j];
                    }
                    Finish[i]=true;//当当前进程能满足时
                    S[count]=i;//设置当前序列排号

                    count++;//满足进程数加1
                    for(int j=0;j<3;j++){
                        System.out.print(allocation[i][j]+"  ");
                    }
                    System.out.print("|  ");
                    for(int j=0;j<3;j++){
                        System.out.print(Need[i][j]+"  ");
                    }
                    System.out.print("|  ");
                    for(int j=0;j<3;j++){
                        System.out.print(Work[j]+"  ");
                    }
                    System.out.println(Finish[i]==true?"完成":"未完成");
                    System.out.println();
                }

            }
            circle++;//循环圈数加1

            if(count== max.length){//判断是否满足所有进程需要
                System.out.print("此时存在一个安全序列：");
                for (int i = 0; i< max.length;i++){//输出安全序列
                    System.out.print("P"+S[i]+" ");
                }
                System.out.println("故当前可分配！");
                break;//跳出循环
            }
            if(count<circle){//判断完成进程数是否小于循环圈数
                count=5;
                System.out.println("当前系统处于不安全状态，故不存在安全序列。");
                break;//跳出循环
            }
        }
    }

    /**
     * 银行家算法
     */
    public void BankerAlgorithm() {
        if (Request==null ||Request.length == 0  ){
            SecurityAlgorithm();
            return;
        }
        boolean T=true;
        if (Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2]) {//判断Request是否小于Need
            if (Request[num][0] <= available[0] && Request[num][1] <= available[1] && Request[num][2] <= available[2]) {//判断Request是否小于Allocation
                for (int i = 0; i < 3; i++) {
                    available[i] -= Request[num][i];
                    allocation[num][i] += Request[num][i];
                    Need[num][i] -= Request[num][i];
                }
            } else {
                System.out.println("当前没有足够的资源可分配，进程P" + num + "需等待。");
                T=false;
            }
        } else {
            System.out.println("进程P" + num + "请求已经超出最大需求量Need.");
            T=false;
        }

        if(T==true){
            printSystemVariable();
            System.out.println("现在进入安全算法：");
            SecurityAlgorithm();
        }
    }


    /**
     * 设置请求资源量Request
     */
    public void setRequest() {

//        System.out.println("请输入请求资源的进程编号：");


//        System.out.println("请输入请求各资源的数量：");

            for (int j = 0; j < 3; j++) {
                Request[num][j] = req[j];
            }
            System.out.println("进程P" + num + "对各资源请求Request：(" + Request[num][0] + "," + Request[num][1] + "," + Request[num][2] + ").");



        BankerAlgorithm();
    }



}



