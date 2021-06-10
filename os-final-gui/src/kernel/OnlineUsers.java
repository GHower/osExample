package kernel;

import users.OSUser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 当前在线用户
 */
public class OnlineUsers implements Runnable{
    public OSUser who = null;
    public Socket socket = null;
    public OSConnection server = null;
    // BufferedReader bf = null;
    public PrintStream ps = null;
    public ObjectInputStream ois = null;
    // ObjectOutputStream oos = null;
    public boolean flag = true;

    public OnlineUsers(Socket socket, OSConnection server) throws IOException {
        this.server = server;
        this.socket = socket;
        ps = new PrintStream(this.socket.getOutputStream());
        ois = new ObjectInputStream(this.socket.getInputStream());
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(flag) {

//            try {
//                Message message = (Message) ois.readObject();
//                String type = message.type;
//                if (type.equals(Mtype.LOGIN)) {
//
//                    Login(message);
//
//                } else if (type.equals(Mtype.REGISTER)) {
//
//                    Register(message);
//
//                } else if (type.equals(Mtype.COMMAND)){
//
//                    runProcess(message);
//
//                }
//                else{
//                    ErrorCommand();
//                }
//            } catch (Exception e) {
//                if(this.who!=null)
//                    Logout();
//            }
        }
    }

//    private void Login(Message message) {
//
//    }

//    private void Logout() {
//
//    }
}
