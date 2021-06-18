package client.views;

import common.OSStatics;
import config.GlobalConfig;
import config.OSConfig;
import config.PrepareColor;
import jobs.JCB;
import kernel.OSKernel;
import memory.partition.Mem;
import process.PCB;
import schedule.CPUSchedule;
import schedule.CreatTestProcess;
import utils.ConvertTool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

/**
 * 分区存储显示窗口
 */
public class PartitionWindow {
    public JFrame frame;
    public JPanel panelLeft;
    public JPanel panelRight;

    //功能选择
    public JPanel panelChoose;
    //进程展示
    public JPanel panelProcess;
    //内存展示
    public JPanel panelMemory;
    //磁盘展示
    public JPanel panelDisk;
    // PCB详细面板
    public JPanel panelPcbDetail;
    // pid查找框
    public JTextField textField;
    // 表格面板
    public JTabbedPane tabbedPane;

    // 第一行的按钮
    public JButton butStart;// 启动程序
    public JButton butStop;// 暂停程序
    public JButton butRandomCreate; // 随机生成
    public JButton butKernelCreate;// 内核创建
    public JButton butSearch;// 查找
    public JButton butRefresh;// 刷新
    public JButton butToJobsDetail;// 完成作业详情
    public JButton butToFileSystem;// 打开文件系统窗口

    // 接受参数输入输入框
    public JTextField numField;
    public JTextField roundsField;
    public JTextField timePieceField;
    // 文本域
    public JTextArea textArea;
    public JScrollPane scrollPane;
    //初次启动随机产生
    public int num = 2;
    public int rounds = 100;

    public Graphics g;
    public Graphics gDisk;
    public Graphics gChoose;

    //三个PCB队列
    public ArrayList<PCB> arrayListRun;
    public ArrayList<PCB> arrayListReady;
    public ArrayList<PCB> arrayListBlock;
    //后备JCB队列
    public ArrayList<JCB> arrayListPrepare;

    //内存队列
    public ArrayList<Mem> arrayListBlank;
    public ArrayList<Mem> arrayListUsed;
    public int blankTotalSize = 0;
    public int usedTotalSize = 0;

    //绘制列表的数目
    private final static int LIST = 10;

    private boolean butDown = false;
    private boolean on = true;

    private static final int MEM_WIDTH = 320;
    private static final int MEM_X = 220;
    private static final double TOTAL_SIZE = OSConfig.MAX_MEMORY;
    private static final String TAB_MEMORY = "memory";
    private static final String TAB_PCB_DETAIL = "detail";

    public PartitionWindow() {
        OSKernel.getInstance();
        initUI();
        CreatTestProcess.randomCreate(num);
        onButtonClick(butStart);
        onButtonClick(butStop);
        onButtonClick(butRandomCreate);
        onButtonClick(butSearch);
        onButtonClick(butRefresh);
        onButtonClick(butKernelCreate);
        onButtonClick(butToJobsDetail);
        onButtonClick(butToFileSystem);
    }

    /**
     * 通过计算之前的y的值计算当前的y
     */
    public int getY(int index) {
        int y = 20;
        y += (int) (((double) arrayListUsed.get(index).start / TOTAL_SIZE) * 900);
        return y;
    }

    /**
     * 找到Mem,然后获取它的Y
     */
    public int getY(Mem mem) {
        int index = arrayListUsed.indexOf(mem);
        return getY(index);
    }

    public int getHeight(int index) {
        int height = 0;
        height += (int) (((double) arrayListUsed.get(index).size / TOTAL_SIZE) * 900);
        return height;
    }

    public int getHeight(Mem mem) {
        int index = arrayListUsed.indexOf(mem);
        int height = getHeight(index);
        return height;
    }

    private void initUI() {
        frame = new JFrame();
        frame.setSize(new Dimension(1920, 1040));

        panelLeft = new JPanel();
        panelLeft.setPreferredSize(new Dimension(1300, 1050));
        panelLeft.setBackground(new Color(211, 211, 211));
        frame.add(panelLeft, BorderLayout.WEST);

        panelRight = new JPanel();
        panelRight.setBackground(new Color(211, 211, 211));

        frame.add(panelRight, BorderLayout.CENTER);


        /**
         * panelChoose
         */
        panelChoose = new JPanel() {
            Graphics g = this.getGraphics();

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                g.setColor(Color.black);
                g.setFont(GlobalConfig.font(Font.BOLD, 25));
//                g.drawString(arrayListRun.get(0).process.Pname, 20,20);
                g.drawLine(40,130,1200,130);
            }

        };
        panelChoose.setBackground(new Color(238, 238, 238));
        panelChoose.setPreferredSize(new Dimension(1250, 200));
        panelChoose.setBorder(new EmptyBorder(0, 0, 0, 0));

        /**
         * panelProcess
         */
        panelProcess = new JPanel() {
            Graphics g = this.getGraphics();

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (int i = 0; i < OSConfig.CPU_num; i++) {
                    //running
                    g2d.setColor(new Color(234, 67, 53));
                    g2d.fillRoundRect(30 + i * 112, 20, 112, 40, 40, 40);

                    g2d.setColor(Color.white);
                    g2d.setFont(GlobalConfig.font(Font.BOLD, 15));
                    if (arrayListRun != null) {
//                        && arrayListRun.size() > 0
                        for (int j = 0; j < arrayListRun.size(); j++) {
                            g2d.drawString(arrayListRun.get(j).process.name, 42 + j * 112, 45);
                        }
                    }
                }
                for (int i = 0; i < LIST; i++) {
                    //ready
                    g2d.setColor(new Color(52, 168, 83));
                    g2d.fillRoundRect(30 + i * 112, 70, 110, 42, 40, 40);
                    g2d.fillRoundRect(30 + i * 112, 115, 110, 42, 40, 40);
//                    g2d.fillRoundRect(30 + i * 112,160,112,42,40,40);
                    g2d.setColor(Color.white);
                    g2d.setFont(GlobalConfig.font(Font.BOLD, 15));
                    if (arrayListReady != null) {
                        int n = arrayListReady.size();
                        for (int j = 0; j < n; j++) {
                            g2d.drawString(arrayListReady.get(j).process.name, 42 + (j % 10) * 112, (j / 10) * 45 + 95);
                        }
                    }

                    //block
                    g2d.setColor(new Color(120, 120, 120));
                    g2d.fillRoundRect(30 + i * 112, 165, 112, 40, 40, 40);
                    g2d.fillRoundRect(30 + i * 112, 208, 112, 40, 40, 40);
                    //绘制阻塞队列的信息
                    g2d.setColor(new Color(255, 255, 255));
                    if (arrayListBlock != null) {
                        for (int j = 0; j < arrayListBlock.size(); j++) {
                            g2d.drawString(arrayListBlock.get(j).process.name, 42 + j * 112, 190);
                        }
                    }

                    // 后备作业
                    g2d.setColor(new Color(85, 85, 85));
                    g2d.fillRoundRect(30 + i * 112, 260, 112, 40, 40, 40);
                    g2d.fillRoundRect(30 + i * 112, 302, 112, 40, 40, 40);
                    g2d.fillRoundRect(30 + i * 112, 346, 112, 40, 40, 40);

                    /**
                     * 绘制后备队列信息
                     */
                    g2d.setColor(new Color(255, 255, 255));
                    if (arrayListPrepare != null) {
                        int n = arrayListPrepare.size();
                        for (int j = 0; j < n; j++) {
                            g2d.drawString(arrayListPrepare.get(j).pcb.process.name, 42 + (j % 10) * 112, (j / 10) * 45 + 283);
                        }
                    }
                }

                // 队列文字
                g2d.setFont(GlobalConfig.font(Font.BOLD, 18));

                g2d.setColor(new Color(234, 67, 53));
                g2d.drawString("运行", 1180, 50);

                g2d.setColor(new Color(52, 168, 83));
                g2d.drawString("就绪", 1180, 120);

                g2d.setColor(new Color(120, 120, 120));
                g2d.drawString("阻塞", 1180, 218);

                g2d.setColor(new Color(85, 85, 85));
                g2d.drawString("后备", 1180, 325);
            }
        };
        panelProcess.setBackground(new Color(238, 238, 238));
        panelProcess.setPreferredSize(new Dimension(1250, 409));

        /**
         * panelMemory
         */
        panelMemory = new JPanel() {
            Graphics g = this.getGraphics();

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //提示信息
                g2d.setFont(GlobalConfig.font(Font.BOLD, 17));
                g2d.setColor(new Color(66, 133, 224));
                g2d.drawString("已使用内存", 20, 40);
                g2d.fillRect(20, 45, 160, 6);

                g2d.setColor(new Color(120, 120, 120));
                g2d.drawString("空闲内存", 20, 70);
                g2d.setColor(new Color(180, 180, 180));
                g2d.fillRect(20, 75, 160, 6);

                g2d.setColor(new Color(52, 168, 83));
                g2d.drawString("内核内存", 20, 100);
                g2d.fillRect(20, 105, 160, 6);

                g2d.setColor(new Color(255, 154, 4));
                g2d.drawString("选中的内存", 20, 130);
                g2d.fillRect(20, 135, 160, 6);

                g2d.setColor(new Color(66, 133, 224));
                g2d.drawString("内存使用量: ", 20, 170);
                g2d.setColor(Color.black);
                String total = ConvertTool.formatSize(OSConfig.MAX_MEMORY, true);
                String used = ConvertTool.formatSize(OSStatics.getMemoryTotalUsed(), true);
                g2d.drawString(used + "/" + total, 20, 200);

                g2d.setColor(GlobalConfig.color());
                g2d.drawString("内核大小: ", 20, 230);
                g2d.setColor(Color.black);
                g2d.drawString(ConvertTool.formatSize(OSConfig.Kernel_SIZE, true), 20, 260);

                g2d.setColor(GlobalConfig.color());
                g2d.drawString("运行速度: ", 20, 290);
                g2d.setColor(Color.black);
                g2d.drawString("每次/" + OSConfig.TIMEPIECE * 100 + "ms", 20, 320);

                g2d.setColor(GlobalConfig.color());
                g2d.drawString("PCB池: ", 20, 350);
                g2d.setColor(Color.black);
                g2d.drawString(OSStatics.getPCBTotal() + "/" + OSConfig.PCB_POOL_SIZE, 20, 380);

                g2d.setColor(GlobalConfig.color());
                g2d.drawString("运行时间: ", 20, 410);
                g2d.setColor(Color.black);
                g2d.drawString((int) OSConfig.System_time + "s", 20, 440);
                g2d.setColor(GlobalConfig.color());

                g2d.drawString("系统可用资源: ", 20, 470);
                g2d.setColor(Color.black);
                Set<String> keySet = OSConfig.available.keySet();
                int j=0;
                for (String s : keySet) {
                    g2d.drawString(s+":"+OSConfig.available.get(s) , 20, j*30+500);
                    j++;
                }


                //画内存底色
                g2d.setColor(new Color(229, 229, 229));
                g2d.fillRect(220, 20, 320, 900);

                //kernel内存区域
                g2d.setColor(new Color(52, 168, 83));
                g2d.fillRect(MEM_X, 862, MEM_WIDTH, 60);
                //画使用过的
                g2d.setColor(new Color(66, 133, 224));
//                g2d.fillRect(MEM_X,20,MEM_WIDTH,(int)(usedTotalSize/TOTAL_SIZE * 900));
                if (arrayListUsed != null) {
                    for (int i = 0; i < arrayListUsed.size(); i++) {
                        g2d.fillRect(MEM_X,
                                PartitionWindow.this.getY(i),
                                MEM_WIDTH,
                                PartitionWindow.this.getHeight(i) + 1);
                    }
                }
            }
        };
        panelMemory.setBackground(new Color(238, 238, 238));
        panelMemory.setPreferredSize(new Dimension(575, 950));


        /**
         * panelDisk
         */
        panelDisk = new JPanel() {
            Graphics g = this.getGraphics();

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(GlobalConfig.font(Font.PLAIN, 20));
                //画运行队列详细信息
                g2d.setColor(Color.black);
                g2d.drawString("PID", 40, 30);
                g2d.drawString("进程名", 100, 30);
                g2d.drawString("类型", 250, 30);
//                g2d.drawString("所有者", 360, 30);
//                g2d.drawString("所属组", 480, 30);
//                g2d.drawString("优先级", 630, 30);
//                g2d.drawString("所需时间", 760, 30);
//                g2d.drawString("运行时间", 900, 30);
                g2d.drawString("优先级", 360, 30);
                g2d.drawString("所需时间", 480, 30);
                g2d.drawString("运行时间", 630, 30);

                if (arrayListRun != null) {
                    g2d.setColor(new Color(234, 67, 53));
                    for (int i = 0; i < arrayListRun.size(); i++) {
                        PCB pcb = arrayListRun.get(i);
                        g2d.drawString("" + pcb.process.PID, 40, 60 + (i * 25));
                        g2d.drawString("" + pcb.process.name, 100, 60 + (i * 25));
                        g2d.drawString("" + pcb.process.type, 250, 60 + (i * 25));
//                        g2d.drawString("" + pcb.process.operator, 360, 60 + (i * 25));
//                        g2d.drawString("" + pcb.process.ownerGroup, 480, 60 + (i * 25));
//                        g2d.drawString("" + pcb.priority, 630, 60 + (i * 25));
//                        g2d.drawString("" + pcb.time_needed, 760, 60 + (i * 25));
//                        g2d.drawString("" + pcb.process.time_used, 900, 60 + (i * 25))
                        g2d.drawString("" + pcb.priority, 360, 60 + (i * 25));
                        g2d.drawString("" + pcb.time_needed, 480, 60 + (i * 25));
                        g2d.drawString("" + pcb.process.time_used, 630, 60 + (i * 25));
                    }
                }
            }
        };
        panelDisk.setBackground(new Color(238, 238, 238));
        panelDisk.setPreferredSize(new Dimension(1250, 300));

        panelDisk.add(nullLabel(1200, 30));
        textArea = new JTextArea();
        setTextArea(textArea);

        panelLeft.add(nullLabel(1200, 20));
        panelLeft.add(panelChoose, BorderLayout.NORTH);
        panelLeft.add(nullLabel(1200, 10));

        panelLeft.add(panelProcess, BorderLayout.WEST);
        panelLeft.add(nullLabel(1200, 10));

        panelLeft.add(panelDisk, BorderLayout.SOUTH);

        panelRight.add(nullLabel(600, 20));
        panelRight.add(panelMemory);
        //pcb详情
        panelPcbDetail = new JPanel() {
            Graphics g = this.getGraphics();

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(GlobalConfig.font(Font.PLAIN, 20));

                g2d.setColor(Color.black);
                g2d.drawString("PID", 40, 30);
                g2d.drawString("进程名", 100, 30);
                g2d.drawString("类型", 220, 30);
                g2d.drawString("线程数", 360, 30);
                g2d.drawString("所有者", 500, 30);
                g2d.drawString("所属组", 650, 30);
                g2d.drawString("端口", 820, 30);

                while (arrayListReady != null) {
                    g2d.setColor(new Color(66, 133, 224));
                    //TODO
                    for (int i = 0; i < arrayListReady.size(); i++) {
                        //TODO
                    }
                    //TODO
                    for (int i = 0; i < arrayListBlock.size(); i++) {
                        //TODO
                    }
                    //TODO
                    for (int i = 0; i < arrayListPrepare.size(); i++) {
                        //TODO
                    }

                }
            }
        };
        panelPcbDetail.setPreferredSize(new Dimension(500, 900));
        panelPcbDetail.setBackground(new Color(238, 238, 238));


//        UIManager.put("TabbedPane.borderHightlightColor", Color.gray);
//        UIManager.put("TabbedPane.darkShadow", Color.gray);
//        UIManager.put("TabbedPane.light", Color.gray);
//        UIManager.put("TabbedPane.selectHighlight", Color.gray);
//        UIManager.put("TabbedPane.darkShadow", Color.gray);
//        UIManager.put("TabbedPane.focus", Color.gray);
//        tabbedPane = new JTabbedPane() {};
//
////        tabbedPane.setBorder(new EmptyBorder(0,0,0,0));
//        tabbedPane.add(TAB_MEMORY, panelMemory);
//        tabbedPane.add(TAB_PCB_DETAIL, panelPcbDetail);
//        //tabbedPane.setFocusable(false);
//
//
//        panelRight.add(tabbedPane);


//        panelChoose.add(setJLabel(200, 30, "NUMBER", Color.white));
//        numField = new JTextField();
//        setTextField(numField, 200,30);
//        panelChoose.add(nullLabel(900,30));
//        panelChoose.add(setJLabel(200, 30, "ROUNDS", Color.white));
//        roundsField = new JTextField();
//        setTextField(roundsField, 200,30);
//        panelChoose.add(nullLabel(900,30));
        //时间片输入框
//        panelChoose.add(setJLabel(140, 30, "时间片", new Color(66,133,224)));
//        timePieceField = new JTextField();
//        setTextField(timePieceField, 200,28);
//        panelChoose.add(nullLabel(800,30));


//        panelChoose.add(setJLabel(200, 30, "Algorithm", Color.white));
//        panelChoose.add(comboBox);

//        panelChoose.add(nullLabel(50,30));

        panelChoose.add(setJLabel(390, 30, "运行状态",
                GlobalConfig.color(PrepareColor.FONT_GREY)));
        panelChoose.add(setJLabel(390, 30, "测试",
                GlobalConfig.color(PrepareColor.FONT_GREY)));
        panelChoose.add(setJLabel(390, 30, "查找",
                GlobalConfig.color(PrepareColor.FONT_GREY)));

        butStart = new JButton("启动");
        butStart.setPreferredSize(new Dimension(390, 40));
        butStart.setBackground(new Color(66, 133, 244));
        butStart.setForeground(Color.white);
        butStart.setFont(GlobalConfig.font(Font.BOLD, 25));
        butStart.setFocusPainted(false);
        butStart.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butStart);

        butRandomCreate = new JButton("随机测试");
        butRandomCreate.setPreferredSize(new Dimension(390, 40));
        butRandomCreate.setBackground(new Color(66, 133, 244));
        butRandomCreate.setForeground(Color.white);
        butRandomCreate.setFont(GlobalConfig.font(Font.BOLD, 25));
        butRandomCreate.setFocusPainted(false);
        butRandomCreate.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butRandomCreate);

        // 文本框-查找
        textField = new JTextField();
        textField.setText("");
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        setTextField(textField, 390, 40);

        butStop = new JButton("暂停");
        butStop.setPreferredSize(new Dimension(390, 40));
        butStop.setBackground(new Color(66, 133, 244));
        butStop.setForeground(Color.white);
        butStop.setFont(GlobalConfig.font(Font.BOLD, 25));
        butStop.setFocusPainted(false);
        butStop.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butStop);

//        panelChoose.add(nullLabel(856,30));

        butKernelCreate = new JButton("测试内核进程");
        butKernelCreate.setPreferredSize(new Dimension(390, 40));
        butKernelCreate.setBackground(new Color(66, 133, 244));
        butKernelCreate.setForeground(Color.white);
        butKernelCreate.setFont(GlobalConfig.font(Font.BOLD, 25));
        butKernelCreate.setFocusPainted(false);
        butKernelCreate.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butKernelCreate);


//        panelChoose.add(nullLabel(505,30));


        butSearch = new JButton("查找");
        butSearch.setPreferredSize(new Dimension(195, 40));
        butSearch.setBackground(new Color(66, 133, 244));
        butSearch.setForeground(Color.white);
        butSearch.setFont(GlobalConfig.font(Font.BOLD, 25));
        butSearch.setFocusPainted(false);
        butSearch.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butSearch);

        butRefresh = new JButton("刷新");
        butRefresh.setPreferredSize(new Dimension(195, 40));
        butRefresh.setBackground(new Color(66, 133, 244));
        butRefresh.setForeground(Color.white);
        butRefresh.setFont(GlobalConfig.font(Font.BOLD, 25));
        butRefresh.setFocusPainted(false);
        butRefresh.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butRefresh);
//        panelChoose.add(nullLabel(505,30));

        panelChoose.add(nullLabel(1200,10));

        butToJobsDetail = new JButton("已完成作业统计");
        butToJobsDetail.setPreferredSize(new Dimension(390,40));
        butToJobsDetail.setBackground(new Color(66, 133, 244));
        butToJobsDetail.setForeground(Color.white);
        butToJobsDetail.setFont(GlobalConfig.font(Font.BOLD, 25));
        butToJobsDetail.setFocusPainted(false);
        butToJobsDetail.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butToJobsDetail);


        butToFileSystem = new JButton("文件系统");
        butToFileSystem.setPreferredSize(new Dimension(390,40));
        butToFileSystem.setBackground(new Color(66, 133, 244));
        butToFileSystem.setForeground(Color.white);
        butToFileSystem.setFont(GlobalConfig.font(Font.BOLD, 25));
        butToFileSystem.setFocusPainted(false);
        butToFileSystem.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(butToFileSystem);

        panelChoose.add(nullLabel(400,40));


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        g = panelMemory.getGraphics();
        gDisk = panelDisk.getGraphics();
        gChoose = panelChoose.getGraphics();
    }

    private JLabel setJLabel(int width, int height, String lab, Color color) {
        JLabel label = new JLabel(lab);
        label.setPreferredSize(new Dimension(width, height));
        label.setFont(GlobalConfig.font(Font.BOLD, 25));
        label.setForeground(color);
        return label;
    }

    /**
     * 设置textField
     */
    private void setTextField(JTextField textField, int width, int height) {
        textField.setPreferredSize(new Dimension(width, height));
        textField.setBackground(new Color(211, 211, 211));
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setForeground(new Color(66, 133, 224));
        textField.setFont(GlobalConfig.font(Font.BOLD, 20));
        panelChoose.add(textField);
    }

    /**
     * 子线程启动进程调度
     * 获取队列数据
     */
    public Thread getListData() {
//        Init.init();
        Thread thread = new Thread(() -> {
            while (on) {
                CPUSchedule.CPURun();

                arrayListRun = OSConfig.running_list.toArrayList();
                arrayListReady = OSConfig.ready_list.toArrayList();
                arrayListBlock = OSConfig.block_list.toArrayList();
                arrayListPrepare = OSConfig.jobsList.toArrayList();

                arrayListBlank = OSConfig.memory.blankList.toArrayList();
                arrayListUsed = OSConfig.memory.usedList.toArrayList();

                blankTotalSize = OSConfig.memory.blankList.getTotalSize();
                usedTotalSize = OSConfig.memory.usedList.getTotalSize();

                panelProcess.repaint();
                panelMemory.repaint();
                panelDisk.repaint();
                panelChoose.repaint();
            }
        });
        return thread;
    }

    /**
     * 获取PID
     */
    public int getPID() throws Exception {
        String PID_String = "";
        if (textField == null) return -1000;
        PID_String = textField.getText();
        if (PID_String.equals("")) return -1000;
        return Integer.parseInt(PID_String);
    }


    /**
     * 判断
     *
     * @param PID
     * @return
     */
    public boolean checkPIDInUse(int PID) {
        return OSConfig.PCB_Map.containsKey(PID);
    }

    public void changePowerOn() {
        OSConfig.CPU_STATE = 1;
        on = true;
    }

    public void changePowerCLose() {
        on = false;
        OSConfig.CPU_STATE = 0;
    }

    /**
     * 占位
     *
     * @param width
     * @param height
     * @return
     */
    public JLabel nullLabel(int width, int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    /**
     * button点击事件  系统启动
     */
    public void onButtonClick(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("启动")) {
                    System.out.println("点击了启动");
                    //输出当前的调度算法
                    changePowerOn();
                    getListData().start();
                    System.out.println("时间片大小: " + OSConfig.TIMEPIECE);
                } else if (e.getActionCommand().equals("暂停")) {
                    System.out.println("点击了暂停");
                    changePowerCLose();
                } else if (e.getActionCommand().equals("随机测试")) {
                    System.out.println("点击了随机测试");
                    CreatTestProcess.randomCreate(5);
                } else if (e.getActionCommand().equals("查找")) {
                    try {
                        int PID = getPID();
                        if (checkPIDInUse(PID)) {
                            PCB pcb = OSConfig.PCB_Map.get(PID);
                            int pieceStart = pcb.addrStart;
                            Mem mem = OSConfig.memory.usedList.getMemoryByAddr(pieceStart);
                            //绘制
                            g.setColor(new Color(254, 153, 6));
                            g.fillRect(MEM_X, PartitionWindow.this.getY(mem), MEM_WIDTH, PartitionWindow.this.getHeight(mem) + 1);

                            //在panelDisk绘制选中的详细信息
                            gDisk.setFont(GlobalConfig.font(0, 20));
                            gDisk.setColor(new Color(254, 153, 6));
                            gDisk.drawString("" + pcb.process.PID, 40, 170);
                            gDisk.drawString("" + pcb.process.name, 100, 170);
                            gDisk.drawString("" + pcb.process.type, 250, 170);
//                            gDisk.drawString("" + pcb.process.operator, 360, 170);
//                            gDisk.drawString("" + pcb.process.ownerGroup, 480, 170);
//                            gDisk.drawString("" + pcb.priority, 630, 170);
//                            gDisk.drawString("" + pcb.time_needed, 760, 170);
//                            gDisk.drawString("" + pcb.process.time_used, 900, 170);
                            gDisk.drawString("" + pcb.priority, 360, 170);
                            gDisk.drawString("" + pcb.time_needed, 480, 170);
                            gDisk.drawString("" + pcb.process.time_used, 630, 170);
//                            gDisk.drawString("" + pcb.time_needed, 760, 170);
//                            gDisk.drawString("" + pcb.process.time_used, 900, 170);
//                            JOptionPane.showTestModel.outputTest.printALL(pcb);
                        }  else {
                            JOptionPane.showMessageDialog(null, "内存中不存在该PID的进程!", "输入错误", JOptionPane.DEFAULT_OPTION);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "输入整型的PID!", "输入错误！", JOptionPane.DEFAULT_OPTION);
                    }

                } else if (e.getActionCommand().equals("刷新")) {
                    panelMemory.repaint();
                    panelDisk.repaint();
                } else if (e.getActionCommand().equals("测试内核进程")) {
                    CreatTestProcess.createKernelProcess();
                } else if(e.getActionCommand().equals("已完成作业统计")){
                    System.out.println("已完成作业统计");
                    new JobStaticsWindow();
                } else if(e.getActionCommand().equals("文件系统")){
                    System.out.println("文件系统");
                    new DiskAndFileWindow();
                }
            }
        });
    }

    // 文本域设置
    public void setTextArea(JTextArea textArea) {
        textArea.setPreferredSize(new Dimension(1150, 200));
        textArea.setBackground(new Color(220, 220, 220));
        textArea.setFont(GlobalConfig.font(Font.BOLD, 40));
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1200, 210));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(textArea);
        panelDisk.add(scrollPane);
    }

    public static void main(String[] args) {
        new PartitionWindow();
    }
}
