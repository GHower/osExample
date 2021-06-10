package client.views;

import config.GlobalConfig;
import config.OSConfig;
import disk_management.VectorMap;
import disk_management.iNode;
import file_system.BinCommands;
import file_system.Dentry;
import kernel.CommendInterpreter;
import kernel.OSKernel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * 文件系统和磁盘一起的窗口
 */
public class DiskAndFileWindow {
    public JFrame frame;
    // 左侧布局面板
    public JPanel panelLeft;
    // 右侧布局面板
    public JPanel panelRight;

    // 功能面板
    public JPanel panelChoose;
    // 磁盘显示面板
    public JPanel panelDisk;

    // 路径查找文件的输入框
    public JTextField textField;
    // 命令输入框
    public JTextField commendTextField;
    // 搜索按钮
    public JButton buttonSearch;
    // 刷新按钮
    public JButton buttonRefresh;

    // 命令运行按钮
    public JButton buttonCommend;

    // 硬盘位示图 的画笔
    public Graphics gDisk;

    public JTree tree =null;
    // 其他参数

    public DiskAndFileWindow() {
        OSKernel.getInstance();
        initUI();
        onButtClick(buttonSearch);
        onButtClick(buttonCommend);
        onButtClick(buttonRefresh);

    }


    public void initUI() {
        frame = new JFrame();
        frame.setSize(new Dimension(1440, 900));
        // 左侧布局,基础设置
        panelLeft = new JPanel();
        panelLeft.setBackground(new Color(210, 210, 210));
        panelLeft.setPreferredSize(new Dimension(900, 900));
        // 右侧布局
        panelRight = new JPanel();
        panelRight.setBackground(new Color(210, 210, 210));
        panelRight.setPreferredSize(new Dimension(540, 900));
        // panelChoose,功能面板设置
        panelChoose = new JPanel();
        panelChoose.setPreferredSize(new Dimension(880, 120));
        panelChoose.setBackground(new Color(238, 238, 238));

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(406, 30));
        textField.setBackground(new Color(211, 211, 211));
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setForeground(new Color(66, 133, 224));
        textField.setFont(GlobalConfig.font(Font.BOLD, 22));
        panelChoose.add(nullLabel(880, 10));
        panelChoose.add(textField);
        // 搜索按钮
        buttonSearch = new JButton("搜索");
        buttonSearch.setPreferredSize(new Dimension(210, 30));
        buttonSearch.setBackground(new Color(66, 133, 244));
        buttonSearch.setForeground(Color.white);
        buttonSearch.setFont(GlobalConfig.font(Font.BOLD, 25));
        buttonSearch.setFocusPainted(false);
        buttonSearch.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(buttonSearch);
        panelChoose.add(nullLabel(210, 30));

        commendTextField = new JTextField();
        commendTextField.setPreferredSize(new Dimension(406, 30));
        commendTextField.setBackground(new Color(61, 61, 61));
        commendTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        commendTextField.setForeground(new Color(19, 189, 37));
        commendTextField.setFont(GlobalConfig.font(Font.BOLD, 22));
        panelChoose.add(nullLabel(880, 5));
        panelChoose.add(commendTextField);

        buttonCommend = new JButton("运行");
        buttonCommend.setPreferredSize(new Dimension(210, 30));
        buttonCommend.setBackground(new Color(66, 133, 244));
        buttonCommend.setForeground(Color.white);
        buttonCommend.setFont(GlobalConfig.font(Font.BOLD, 25));
        buttonCommend.setFocusPainted(false);
        buttonCommend.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(buttonCommend);

        buttonRefresh = new JButton("刷新");
        buttonRefresh.setPreferredSize(new Dimension(210, 30));
        buttonRefresh.setBackground(new Color(66, 133, 244));
        buttonRefresh.setForeground(Color.white);
        buttonRefresh.setFont(GlobalConfig.font(Font.BOLD, 25));
        buttonRefresh.setFocusPainted(false);
        buttonRefresh.setBorder(new EmptyBorder(0, 0, 0, 0));
        panelChoose.add(buttonRefresh);

        // 磁盘情况,绘制
        panelDisk = new JPanel() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                //画20*20的盘块图
                g.setColor(new Color(217, 217, 217));
                for (int i = 0; i < 20; i++) {
                    for (int j = 1; j < 20; j++) {
                        g.fillRect(20 + i * (42), 10 + (j * 30), 37, 25);
                    }
                }
                // 第一排作为系统文件
                g.setColor(new Color(0, 0, 0));
                for (int j = 0; j < 20; j++) {
                    g.fillRect(20 + j * (42), 10, 37, 25);
                }
                //绘制实际使用情况,根据超级块上位图的情况画
                g.setColor(new Color(100, 100, 100));
                VectorMap vectorMap = OSConfig.superBlock.vectorMap;
                boolean[][] map = vectorMap.map;
                for (int i = 1; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j]) {
                            g.fillRect(20 + j * 42, 10 + i * 30, 37, 25);
                        }
                    }
                }
            }
        };
        panelDisk.setPreferredSize(new Dimension(880,830));
        panelDisk.setBackground(new Color(238,238,238));


        panelRight.add(nullLabel(540, 5));
        panelLeft.add(nullLabel(900, 5));

        // 右侧布局 文件树
        tree = panelRightUI();
        panelRight.add(tree,1);


        // 左侧大布局加边框
        panelLeft.add(panelChoose);
        panelLeft.add(panelDisk);

        frame.add(panelLeft, BorderLayout.WEST);
        frame.add(panelRight, BorderLayout.CENTER);// 右侧面板向左对齐
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("文件系统与磁盘简况");
        // 画笔取出
        gDisk = panelDisk.getGraphics();
    }

    public JTree panelRightUI() {
        // 文件总数，包括目录
//        total_num = Dentry.getTotalDirNum(OSConfig.ROOT)+Dentry.getTotalFileNum(OSConfig.ROOT);
        DefaultMutableTreeNode treeModel = dirTreeModel(OSConfig.ROOT);
        JTree tree = new JTree(treeModel);
        tree.setBackground(new Color(238,238,238));
        tree.setPreferredSize(new Dimension(450,850));
        tree.setFont(GlobalConfig.font(Font.BOLD,20));
        tree.setBorder(new EmptyBorder(20,30,0,0));
        return tree;
    }

    // 递归目录树
    public DefaultMutableTreeNode dirTreeModel(Dentry root) {
        if (root.dirTree.size() <= 0)
            return null;
        DefaultMutableTreeNode cur = new DefaultMutableTreeNode(root);

        for (String fileName : root.dirTree.keySet()) {
            Dentry dentry = root.dirTree.get(fileName);
            if (dentry.isDir) {
                // 是目录就递归
                DefaultMutableTreeNode child = dirTreeModel(dentry);
                if(child!=null){
                    cur.add(child);
                }else{
                    DefaultMutableTreeNode cur2 = new DefaultMutableTreeNode(dentry){
                        public boolean isLeaf(){
                            return false;
                        }
                    };
                    cur.add(cur2);
                }
            }else{
                DefaultMutableTreeNode cur2 = new DefaultMutableTreeNode(dentry);
                cur.add(cur2);
            }
        }
        return cur;
    }

    /**
     * 占位
     */
    public JLabel nullLabel(int width, int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    // 通用按钮事件绑定
    private void onButtClick(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("搜索")) {
                    //repaint延迟重绘
//                    panelDisk.repaint();
                    // 立即重绘
                    panelDisk.paintImmediately(0,0,panelDisk.getWidth(),panelDisk.getHeight());
                    try {
                        String path = getPath();
                        System.out.println(path);
                        Dentry dentry = Dentry.searchFile(path);
                        if (dentry != null) {
                            if (dentry.isDir) {
                                JOptionPane.showMessageDialog(null, "查找的是一个目录", "目录", JOptionPane.DEFAULT_OPTION);
                            } else {
                                iNode iNode = dentry.inode;
                                Vector<Integer> vector = iNode.diskTable;
                                int size = OSConfig.superBlock.vectorMap.size;
                                gDisk.setColor(new Color(254, 153, 6));
                                for (int n = 0; n < vector.size(); n++) {
                                    int num = vector.get(n);
                                    int i = num / size;
                                    int j = num % size;
                                    gDisk.fillRect(20 + j * 42, 10 + i * 30, 37, 25);
                                }

                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "请输入正确的路径", "路径错误", JOptionPane.DEFAULT_OPTION);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else if (e.getActionCommand().equals("运行")){
                    String commend = getCommend();
                    CommendInterpreter.CommendToProcess(commend);
                    // 刷新界面
//                    refreshPanel();
                }else if (e.getActionCommand().equals("刷新")){
                    refreshPanel();
                }
            }
        });
    }
    /* 刷新面板 */
    public void refreshPanel(){
        // 刷新文件树
        if(tree!=null){
            panelRight.remove(tree);
        }
        tree = panelRightUI();
        panelRight.add(tree,1);
        panelRight.updateUI();
        //刷新磁盘区域
        panelDisk.repaint();
    }

    /**
     * 获取文本框的路径
     */
    public String getPath() throws Exception {
        if (textField == null) return "";
        return textField.getText();
    }

    /**
     * 获取命令
     */
    public String getCommend(){
        if (commendTextField == null) return "";
        return commendTextField.getText();
    }

    public static void main(String[] args) {
        new DiskAndFileWindow();
    }

}
