package kernel;

import config.OSConfig;
import disk_management.SuperBlock;
import disk_management.iNode;
import file_system.Dentry;
import users.OSUser;

import java.io.*;
import java.util.HashMap;

/**
 * 完成整个系统的初始化
 */
public class OSInit {
    // 加载磁盘信息
    public static void loadDisk() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("MyDisk"));
            int lineNum = 0;
            String line = br.readLine();
            while (line != null) {
                OSConfig.MyDisk[lineNum] = line;
                line = br.readLine();

                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 保存磁盘信息
    public static void saveDisk() {
        PrintStream ps;
        try {
            ps = new PrintStream(new FileOutputStream("MyDisk", false));

            for (int i = 0; i < 400; i++) {
                // 400块的磁盘块 全部写入
                ps.println(OSConfig.MyDisk[i]);
            }
            ps.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void initDisk() {
        PrintStream ps;
        try {
            ps = new PrintStream(new FileOutputStream("MyDisk", false));
            for (int i = 0; i < 400; i++) {
                ps.println("$");
            }
            ps.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void saver() throws Exception {

        File system = new File("System");

        if (!system.exists())
            system.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(system));
        oos.writeObject(OSConfig.userList);
        oos.writeObject(OSConfig.ROOT);
        oos.writeObject(OSConfig.superBlock);
        oos.close();
        saveDisk();
    }
    public static void loader() throws Exception {

        File system = new File("System");

        loadDisk();
        initResource();

        // 用户已经是存在的
        if (system.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(system));
            OSConfig.userList = (HashMap<String, OSUser>) ois.readObject();
            OSConfig.ROOT = (Dentry) ois.readObject();
            OSConfig.superBlock = (SuperBlock) ois.readObject();
            OSConfig.root = OSConfig.userList.get("root");
        } else {
            // 用户不存在  系统初始化
            OSConfig.superBlock = new SuperBlock();
            //初始化目录项
            initDentry();
            System.out.println("用户初始化");
            initUser();
        }

    }

    private static void initUser() {
        OSConfig.userList = new HashMap<String, OSUser>();
        // 一开始建一个root
        OSUser root = new OSUser("root", ".");
        OSConfig.root = root;
        // 追加一个普通用户
        OSConfig.userList.put("ghower",new OSUser("ghower","ghower"));
    }

    private static void initDentry() {
        iNode node_root = new iNode(".", "root", ".", 0, true);
        OSConfig.ROOT = node_root.dentry;
        // 按linux的结构创建
        OSConfig.ROOT.path = ".";
        OSConfig.ROOT.creatFile("tmp", "root", ".", true);
        OSConfig.ROOT.creatFile("home", "root", ".", true);
        OSConfig.ROOT.creatFile("usr", "root", ".", true);
        OSConfig.ROOT.creatFile("dev", "root", ".", true);
        OSConfig.ROOT.creatFile("bin", "root", ".", true);
        OSConfig.ROOT.creatFile("etc", "root", ".", true);
        OSConfig.ROOT.creatFile("lib", "root", ".", true);
    }
    private static void initResource(){
        OSConfig.available.put("R1",10);
        OSConfig.available.put("R2",10);
        OSConfig.available.put("R3",9);
    }
}
