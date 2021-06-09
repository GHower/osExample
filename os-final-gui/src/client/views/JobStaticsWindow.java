package client.views;

import common.OSStatics;
import config.GlobalConfig;
import config.OSConfig;
import jobs.JCB;
import kernel.OSKernel;
import schedule.CPUSchedule;
import schedule.CreatTestProcess;
import utils.ConvertTool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 已完成作业统计信息的子窗口
 */
public class JobStaticsWindow {
    public JFrame frame;
    public JPanel center;

    public JTextArea textArea;
    public JScrollPane scrollPane;
    // 画笔
    public Graphics g;
    public ArrayList<JCB> jcbArrayList;
    int size=0;
    public static Thread thread;
    public static void main(String[] args) {
        new JobStaticsWindow();
    }

    public JobStaticsWindow() {
        OSKernel.getInstance();
        initUI();
        // 测试
//        CreatTestProcess.randomCreate(21);
//        OSConfig.CPU_STATE = 1;
        // 立即运行
        if(thread==null){
            thread = getJobsData();
            thread.start();
            System.out.println("首次创建job窗口进程");
        }else{
            thread.interrupt();
            thread = getJobsData();
            thread.start();
            System.out.println("重新创建job窗口进程");
        }
    }

    private void initUI() {
        frame = new JFrame();
        frame.setSize(new Dimension(900, 500));
        // 内容面板
        center = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(GlobalConfig.font(Font.PLAIN, 20));

                g2d.setColor(Color.black);
                g2d.drawString("JID", 40, 30);
                g2d.drawString("作业名", 110, 30);
                g2d.drawString("提交时间(s)", 200, 30);
                g2d.drawString("开始时刻(s)", 320, 30);
                g2d.drawString("运行时间(s)", 440, 30);
                g2d.drawString("完成时刻(s)", 560, 30);
                g2d.drawString("周转时间(s)", 680, 30);

                jcbArrayList = new ArrayList<>(OSConfig.JCB_MAP_FINISHED.values());

//                g2d.setColor(new Color(234, 67, 53));
                int newSize = jcbArrayList.size();
                if(size<newSize){// 有变化才更新文本框
                    jcbArrayList.sort(Comparator.comparingDouble(e->e.finishTime));
                    textArea.setText("");
                    for (int i = 0; i < jcbArrayList.size(); i++) {
                        JCB jcb = jcbArrayList.get(i);
//                    g2d.drawString("" + jcb.JID, 30, (40 * i) + 70);
//                    g2d.drawString("作业_" + jcb.JID, 100, (40 * i) + 70);
//                    g2d.drawString("" + jcb.submitTime, 200, (40 * i) + 70);
//                    g2d.drawString("" + jcb.startTime,  320, (40 * i) + 70);
//                    g2d.drawString("" + jcb.runTime,  440, (40 * i) + 70);
//                    g2d.drawString("" + jcb.finishTime, 560, (40 * i) + 70);
//                    g2d.drawString("" + OSStatics.getJobTT(jcb), 680, (40 * i) + 70);
                        String line = jcb.JID + "    " +
                                "作业_" + jcb.JID +
                                "     " + jcb.submitTime +
                                "        " + jcb.startTime +
                                "         " + jcb.runTime +
                                "         " + jcb.finishTime +
                                "         " + OSStatics.getJobTT(jcb) + "\n";
                        textArea.append(line);
                    }
                    size = newSize;
                }
                String att = ConvertTool.formatDecimal(OSStatics.getJobsATT(), 2);
                String awtt = ConvertTool.formatDecimal(OSStatics.getJobsWTT(), 2);
                g2d.drawString("平均周转时间(s):" + att, 120, 400);
                g2d.drawString("带权周转时间(s):" + awtt, 500, 400);
            }
        };
        center.setBackground(new Color(239, 239, 239));
        center.setPreferredSize(new Dimension(900, 480));
        center.add(nullLabel(800, 40));
        center.setFocusable(false);
        // 文本域设置
        textArea = new JTextArea();
        setTextArea(textArea);


        frame.add(center, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // 画笔取出
        g = center.getGraphics();
    }

    // 文本域设置
    public void setTextArea(JTextArea textArea) {
//        textArea.setPreferredSize(new Dimension(800, 300));
        textArea.setBackground(new Color(220, 220, 220));
//        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setFont(GlobalConfig.font(Font.PLAIN, 20));
        //字体
        textArea.setForeground(new Color(234, 67, 53));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(810, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setViewportView(textArea);
        center.add(scrollPane);
    }

    public JLabel nullLabel(int width, int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    public Thread getJobsData() {
        Thread thread = new Thread(() -> {
            while (OSConfig.CPU_STATE == 1) {
                center.repaint();
            }
        });
        return thread;
    }
}
