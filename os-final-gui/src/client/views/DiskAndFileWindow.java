package client.views;

import config.GlobalConfig;
import config.OSConfig;
import disk_management.VectorMap;
import disk_management.iNode;
import file_system.Dentry;
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
    // 搜索按钮
    public JButton buttonSearch;
    public JButton btnRefresh;
    // 硬盘位示图 的画笔
    public Graphics gDisk;
    // 其他参数
    int total_num;
    static int cursor = 1;
    int x_current = 50;
    static int y_current = 20;

    public DiskAndFileWindow() {
        OSKernel.getInstance();
        initUI();
//        getData();
        onButtClick(buttonSearch);
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

        // 右侧布局统一配置
        JTree tree = panelRightUI();
        tree.setBackground(new Color(238,238,238));
        tree.setPreferredSize(new Dimension(520,850));
        tree.setFont(GlobalConfig.font(Font.BOLD,20));
        tree.setBorder(new EmptyBorder(20,30,0,0));
        panelRight.add(tree);

        // 左侧大布局加边框
//        panelLeft.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelLeft.add(panelChoose);
        panelLeft.add(panelDisk);

        frame.add(panelRight, BorderLayout.CENTER);// 右侧面板向左对齐
        frame.add(panelLeft, BorderLayout.WEST);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // 画笔取出
        gDisk = panelDisk.getGraphics();
    }

    public JTree panelRightUI() {
        // 文件总数，包括目录
//        total_num = Dentry.getTotalDirNum(OSConfig.ROOT)+Dentry.getTotalFileNum(OSConfig.ROOT);
        DefaultMutableTreeNode treeModel = dirTreeModel(OSConfig.ROOT);
        return new JTree(treeModel);
    }

    // 递归目录树
    public DefaultMutableTreeNode dirTreeModel(Dentry root) {
        DefaultMutableTreeNode cur = new DefaultMutableTreeNode(root);
        if (root.dirTree.size() <= 0)
            return null;
        for (String fileName : root.dirTree.keySet()) {
            Dentry dentry = root.dirTree.get(fileName);
            DefaultMutableTreeNode cur2 = new DefaultMutableTreeNode(dentry);
            if (dentry.isDir) {
                // 是目录就递归
                DefaultMutableTreeNode child = dirTreeModel(dentry);
                if(child!=null){
                    cur2.add(child);
                }
            }
            cur.add(cur2);
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
                    panelDisk.repaint();
                    try {
                        String path = getPath();
                        System.out.println(path);
                        Dentry dentry = Dentry.searchFile(path);
                        if (dentry != null) {
                            if (dentry.isDir) {
                                JOptionPane.showMessageDialog(null, "What you search is a folder.", "Folder", JOptionPane.DEFAULT_OPTION);
                            } else {
                                iNode iNode = dentry.inode;
                                Vector<Integer> vector = iNode.diskTable;
                                int size = OSConfig.superBlock.vectorMap.size;
                                gDisk.setColor(new Color(254, 153, 6));
                                for (int n = 0; n < vector.size(); n++) {
                                    int num = vector.get(n);
                                    int i = num / size;
                                    int j = num % size;
                                    gDisk.fillRect(20 + j * 42, 80 + i * 30, 37, 25);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "请输入正确的路径", "路径错误", JOptionPane.DEFAULT_OPTION);
                        }

                    } catch (Exception ex) {
                        System.out.println("please input an Integer");
                    }
                }
            }
        });
    }

    /**
     * 获取文本框的路径
     */
    public String getPath() throws Exception {
        if (textField == null) return "";
        return textField.getText();
    }

    public static void main(String[] args) {
        new DiskAndFileWindow();
    }


}
