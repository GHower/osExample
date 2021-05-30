package os.service.impl;

import os.model.entity.MyPCB;
import os.model.entity.MyProcess;
import os.model.entity.MyRequest;
import os.model.entity.MyResource;
import os.service.BankService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 银行家算法实现类
 */
public class BankServiceImpl implements BankService {
    /**
     * 可用资源，初始未全部可用
     */
//    int[] available = {32,34,34};
    int[] available ;

    /**
     * 最大可用资源
     */
    int[] maxAva  ;
    /**
     * 最大需求矩阵，每个进程各类资源最大需要
     */
//    int[][] max = {{8,7},{5,2},{6,6},{1,1}};
    int[][] max ;
    /**
     * 分配矩阵，每个进程各类资源占用情况
     */
//    int[][] allocation = {{3,2},{2,0},{1,3},{1,0}};
    int[][] allocation ;
    /**
     * 存放数组行号与进程名的关系
     * k-进程名
     * v-数组下标
     */
    HashMap map = new HashMap();

    /**
     * 还需资源，每个进程各类资源还需情况
     */
//    int[][] Need = new int[max.length][available.length];
    int[][] Need ;
    /**
     * 请求资源矩阵，某个进程t0时刻请求进程{{1,0,1}}
     */
//    int[][] Request = new int[3][available.length];
    int[][] Request ;
    /**
     * 工作后释放的总资源
     */
//    int[] Work = new int[available.length];
    int[] Work ;
    /**
     * 设置全局变量进程编号num,需要知道申请资源是对应allocation哪个行下标，1就代表是allocation行下标为1的资源
     */
    int num= 1;
    /**
     * to时刻请求资源
     */
    int[] req= {1,1};

    /**
     * 初始化数据结构
     */
    public BankServiceImpl(){
  /*      setAvailable();
        setMax();
        setNeed();*/
    }

    @Override
    public boolean checkSafe(List<MyPCB> pcbs , List<MyProcess> allocation,int[] ava) {
        /**
         * 获取各进程申请资源的信息
         *      进程最大资源需求量 max
         *      进程已占有资源 allocation（谁来指定）
         *      系统指定可用资源 available (目前只能写三类资源)
         *  根据pcbs里面的每一个进程的pid找到对应的MyProcess，,根据每一个Myprocess获取到进程的占有资源情况，然后统计出每类资源的占有总数
         */
        List<MyProcess> processList = new ArrayList<>();
        for (MyPCB pcb : pcbs) {
            Integer pcbPid = pcb.getPid();
            for (MyProcess process : allocation) {
                if (pcbPid==process.getId()){
                    processList.add(process);
                }
            }
        }

        /**
         * 根据每一个Myprocess获取到进程的占有资源情况
         * 然后统计出每类资源的占有总数，放到数组int[][] allocation = {{3,2,0},{2,0,2},{1,3,2},{1,0,1},{1,1,0}};
         */
        int tmp = 1;
        int j = 0 ;
        for (MyProcess process : processList) {

            List<MyResource> myAllocation = null;
            List<MyResource> myMax = null;
            if (process.getAllocation().size()>0){
                myAllocation = process.getAllocation();
                myMax = process.getMax();
                if (tmp==1){
                    //初始化allocation矩阵
                    setAllocation(processList,myAllocation);
                    tmp=2;
                }
            }

            /**
             * 设置allocation初始值
             */
            int i = 0;
            for (MyResource myResource : myAllocation) {

                //获取占有的资源名称
                String name = myResource.getName();
                //获取占有的资源该名称的资源数量
                Integer number = myResource.getNumber();

                //TODO:根据资源名进行排序，这样做即便开始顺序是乱的，也能完成


                /**
                 * 先做映射再插入
                 * 根据资源名称和资源数量插入到allocation中
                 */
                this.allocation[j][i]=number;
                i++;
            }

            map.put(process.getId(),j);


            /**
             * 设置max初始值
             */
            int t = 0;
            for (MyResource myResource : myMax) {
                //获取占有的资源名称
                String name = myResource.getName();
                //获取占有的资源该名称的资源数量
                Integer number = myResource.getNumber();

                //TODO:根据资源名进行排序，这样做即便开始顺序是乱的，也能完成

                /**
                 * 先做映射再插入
                 * 根据资源名称和资源数量插入到allocation中
                 */
                this.max[j][t]=number;
                t++;
            }
            j++;
        }
        /**
         * 设置available矩阵
         */
        this.setAvailable(ava,false);
        /**
         * 设置work矩阵
         */
        this.setWork();
        /**
         * 设置need矩阵
         */
        this.setNeed();
        System.out.println(map);
        printSystemVariable();
        return SecurityAlgorithm();
        /**
         *
         * 打印allocation数组
         */
/*        for (int i = 0; i < this.max.length; i++) {
            for (int k = 0; k < this.max[0].length; k++) {
                System.out.println(this.max[i][k]);
            }
            System.out.println();
        }*/
    }


    /**
     * TODO:设置Max矩阵
     */
    private void setMax() {
//        int[][] max = {{8,7},{5,2},{6,6},{1,1}};

    }

    /**
     * 设置Need矩阵
     */
    private void  setNeed(){
        System.out.println("setNeed");
        this.Need = new int[max.length][available.length];
        for (int i = 0; i < max.length; i++) {//设置Need矩阵
            for (int j = 0; j < available.length; j++) {
                Need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }
    /**
     * 设置Available矩阵
     */
    private  void setAvailable(int[] ava,boolean flag){
        if (!flag){
            this.available=ava;
            this.maxAva=new int[ava.length];
            for (int i = 0 ; i<ava.length;i++){
                int tmp = ava[i];
                maxAva[i]=tmp;
            }

            System.out.println("setAvailable");
            for (int i = 0; i < available.length; i++) {
                for (int j = 0; j < max.length; j++) {
                    available[i] = available[i] - allocation[j][i];
                }
            }
            for (int i = 0; i < maxAva.length; i++) {
                System.out.println(maxAva[i]+"====================");
            }

        }else {
            for (int i = 0 ; i<ava.length;i++){
                int tmp = ava[i];
                available[i]=tmp;
            }
        }

    }

    /**
     * 初始化allocation
     * TODO:如果processList里的进程是没有申请资源的也会被当成申请资源，可能出现意想不到的问题
     */
    private  void  setAllocation(List<MyProcess> processList,List<MyResource> resource){
        this.allocation=new int[processList.size()][resource.size()];
        this.max=new int[processList.size()][resource.size()];

    }
    /**
     * 打印各进程资源分配表
     */
    public void printSystemVariable(){
        System.out.println("此时资源分配量如下：");
        System.out.println("进程  "+"   Max   "+"   Alloction "+"    Need  "+"     Available ");
        for(int i=0;i< max.length;i++){
            System.out.print("P"+i+"  ");
            for(int j=0;j<available.length;j++){
                System.out.print(max[i][j]+"  ");
            }
            System.out.print("|  ");
            for(int j=0;j< available.length;j++){
                System.out.print(allocation[i][j]+"  ");
            }
            System.out.print("|  ");
            for(int j=0;j<available.length;j++){
                System.out.print(Need[i][j]+"  ");
            }
            System.out.print("|  ");
            if(i==0){
                for(int j=0;j<available.length;j++){
                    System.out.print(available[j]+"  ");
                }
            }
            System.out.println();
        }
    }


    /**
     * 安全算法，打印出安全序列
     */
    public boolean SecurityAlgorithm() {
        Boolean Finish[] = new Boolean[max.length];//初始化Finish
        //设值
        for (int i = 0; i < Finish.length; i++) {
            Finish[i]=false;
        }

        int count = 0;//完成进程数
        int circle=0;//循环圈数
        int[] S=new int[max.length];//安全序列
        for (int i = 0; i < available.length; i++) {//设置工作向量
            Work[i] = available[i];
        }
        boolean flag = true;
        while (count < max.length) {
            if(flag){
                System.out.println("进程  "+"   Work  "+"   Alloction "+"    Need  "+"     Work+Alloction ");
                flag = false;
            }
            for (int i = 0; i < max.length; i++) {
                /**
                 * 动态判断  需要额外写方法实现
                 *
                 * Finish[i]==false&&Need[i][0]<=Work[0]&&Need[i][1]<=Work[1]
                 */
                if (isWork(Finish,i)) {//判断条件
                    System.out.print("P"+i+"  ");
                    for (int k = 0; k < available.length; k++){
                        System.out.print(Work[k]+"  ");
                    }
                    System.out.print("|  ");
                    for (int j = 0; j<available.length;j++){
                        Work[j]+=allocation[i][j];
                    }
                    Finish[i]=true;//当当前进程能满足时
                    S[count]=i;//设置当前序列排号

                    count++;//满足进程数加1
                    for(int j=0;j<available.length;j++){
                        System.out.print(allocation[i][j]+"  ");
                    }
                    System.out.print("|  ");
                    for(int j=0;j<available.length;j++){
                        System.out.print(Need[i][j]+"  ");
                    }
                    System.out.print("|  ");
                    for(int j=0;j<available.length;j++){
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
                setAvailable(this.maxAva,true);
                return true;//跳出循环
            }
            if(count<circle){//判断完成进程数是否小于循环圈数
                count=5;
                System.out.println("当前系统处于不安全状态，故不存在安全序列。");
                /**
                 * TODO:抛出异常
                 */
                return false;//跳出循环
            }
        }
        return false;
    }

    /**
     * 银行家算法
     */
    public boolean BankerAlgorithm() {
        if (Request==null ||Request.length == 0  ){
            return SecurityAlgorithm();
        }
        boolean T=true;
        /**
         * 需要动态判断，写算法
         * TODO:判断
         * Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1]
         * Request[num][0] <= available[0] && Request[num][1] <= available[1]
         *
         * 试分配
         */
        if (isNeed()) {//判断Request是否小于Need
            if (isAvailable()) {//判断Request是否小于Allocation
                for (int i = 0; i < available.length; i++) {
                    available[i] -= Request[num][i];
                    allocation[num][i] += Request[num][i];
                    Need[num][i] -= Request[num][i];
                }
            } else {
                /**
                 * TODO:抛出异常
                 */
                System.out.println("当前没有足够的资源可分配，进程P" + num + "需等待。");
                T=false;
                return false;
            }
        } else {
            /**
             * TODO:抛出异常
             */
            System.out.println("进程P" + num + "请求已经超出最大需求量Need.");
            T=false;
            return false;
        }

        if(T==true){
            printSystemVariable();
            System.out.println("现在进入安全算法：");
            return SecurityAlgorithm();
        }
        return false;
    }


    /**
     * 设置请求资源量Request
     */
    @Override
    public boolean setRequest(MyRequest request,int[] ava) {

        List<MyResource> myResources = request.getRequest();

        //获取通过id获取map映射对应的allocation下标，在试分配使用
        num = (int) map.get(request.getId());

        this.Request = new int[allocation.length][available.length];


        /**
         * 设置Request矩阵
         */
        for (int j = 0; j < available.length; j++) {
                MyResource resource = myResources.get(j);

                 //TODO:根据资源名进行排序，这样做即便开始顺序是乱的，也能完成

                Request[num][j] = resource.getNumber();
        }


        /**
         * 打印需要申请的资源
         */
        System.out.print("进程P" + num + "对各资源请求Request：("  );
        for (int i = 0; i < Request[num].length; i++) {
            System.out.print(Request[num][i]+",");
        }
        System.out.println(").");
        System.out.println();
        return BankerAlgorithm();
    }


    private void  setWork(){
        this.Work = new int[available.length];
    }


    /**
     * Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1]
     *
     *  int[][] Need = new int[max.length][available.length];
     *  int[] work = new int[available.length]
     */
    private boolean isWork(Boolean Finish[] ,int i ){

        if (available.length==1){
            return Finish[i]==false && Need[i][0]<=Work[0] ;
        }
        if (available.length==2){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1];
        }

        if (available.length==3){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1] && Need[i][2]<=Work[2];
        }

        if (available.length==4){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1] && Need[i][2]<=Work[2] && Need[i][3]<=Work[3];
        }
        if (available.length==5){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1] && Need[i][2]<=Work[2] && Need[i][3]<=Work[3] && Need[i][4]<=Work[4] ;
        }
        if (available.length==6){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1] && Need[i][2]<=Work[2] && Need[i][3]<=Work[3] && Need[i][4]<=Work[4] && Need[i][5]<=Work[5];
        }
        if (available.length==7){
            return Finish[i]==false && Need[i][0]<=Work[0] &&  Need[i][1]<=Work[1] && Need[i][2]<=Work[2] && Need[i][3]<=Work[3] && Need[i][4]<=Work[4] && Need[i][5]<=Work[5] && Need[i][6]<=Work[6];
        }

        return false;
    }


    private boolean isNeed(){
        if (Request[num].length==1){
            return Request[num][0] <= Need[num][0] ;
        }
        if (Request[num].length==2){
            return Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] ;
        }
        if (Request[num].length==3){
            return Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2] ;
        }
        if (Request[num].length==4){
            return Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2] && Request[num][4] <= Need[num][4] ;
        }
        if (Request[num].length==5){
            return Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2] && Request[num][4] <= Need[num][4]  && Request[num][5] <= Need[num][5];
        }
        if (Request[num].length==6){
            return Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2] && Request[num][3] <= Need[num][3]  && Request[num][4] <= Need[num][4] && Request[num][5] <= Need[num][5];
        }
        return false;
    }


    private boolean isAvailable(){
        if (Request[num].length==1){
            return Request[num][0] <= available[0] ;
        }
        if (Request[num].length==2){
            return Request[num][0] <= available[0] && Request[num][1] <= available[1] ;
        }
        if (Request[num].length==3){
            return Request[num][0] <= available[0] && Request[num][1] <= available[1] && Request[num][2] <= available[2] ;
        }
        if (Request[num].length==4){
            return Request[num][0] <= available[0] && Request[num][1] <= available[1] && Request[num][2] <= available[2] && Request[num][4] <= available[4] ;
        }
        if (Request[num].length==5){
            return Request[num][0] <= available[0] && Request[num][1] <= available[1] && Request[num][2] <= available[2] && Request[num][4] <= available[4]  && Request[num][5] <= available[5];
        }
        if (Request[num].length==6){
            return Request[num][0] <= available[0] && Request[num][1] <= available[1] && Request[num][2] <= available[2] && Request[num][3] <= available[3]  && Request[num][4] <= available[4] && Request[num][5] <= available[5];
        }
        return false;
    }
}



