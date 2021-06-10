package kernel;

import javax.swing.*;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 表示一个连接，多用户则会有多个实例
 */
public class OSConnection implements Runnable, Serializable {
    public ServerSocket serverSocket  = null;
    public Socket socket = null;
    public OSConnection() {
        try{
            serverSocket = new ServerSocket(6153);
            System.out.print("ok");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "操作系统连接出错");
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                socket = serverSocket.accept();
                new OnlineUsers(socket,this);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "操作系统连接出错");
                try{
                    serverSocket.close();
                }catch (Exception ex) {
                    // TODO: handle exception
                }


            }
        }
    }
}
