package kernel;

import config.OSConfig;

/**
 * 假装内核对象,实际上只负责初始化系统和占用一个端口
 */
public class OSKernel {
    private static final OSKernel osKernel=new OSKernel();
    public static OSKernel getInstance(){
        return osKernel;
    }
    private OSKernel() {
        try {
            OSInit.loader();
        } catch (Exception e) {
            System.err.println("初始化失败了!");
            e.printStackTrace();
            System.exit(0);
        }

        OSConfig.server = new OSConnection();
    }
}
