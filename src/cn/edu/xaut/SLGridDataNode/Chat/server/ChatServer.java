/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Chat.server;

/**
 * @author greensubmarine
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class ChatServer {

static final int DEFAULT_PORT = 6000;
static ServerSocket serverSocket;
static Vector<Socket> connections;//连接
static Vector<ClientProc> clients;

/*
 * 在全局范围内分发消息
 */
public static void sendAll(String s) {
    if (connections != null) {
        for (Enumeration e = connections.elements(); e.hasMoreElements(); ) {
            try {
                PrintWriter pw = new PrintWriter(((Socket) e.nextElement()).getOutputStream());
                pw.println(s);
                pw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    System.out.println(s);
}

/*
 * 点对点发送消息
 */
public static boolean sendOne(String name, String msg) {
    if (clients != null) {
        for (Enumeration e = clients.elements(); e.hasMoreElements(); ) {
            ClientProc cp = (ClientProc) e.nextElement();
            if ((cp.getName()).equals(name)) {
                try {
                    PrintWriter pw = new PrintWriter((cp.getSocket()).getOutputStream());
                    pw.println(msg);
                    pw.flush();
                    return true;//返回值为真，说明该用户被发现可以建立聊天
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    return false;//没有找到该用户，无法建立聊天
}

public static void addConnection(Socket s, ClientProc cp) {
    if (connections == null) {
        connections = new Vector<Socket>();
    }
    clients.addElement(cp);
}

public static void deleteConnection(Socket s, ClientProc cp) throws IOException {
    if (connections != null) {
        connections.removeElement(s);
        s.close();
    }
    if (clients != null) {
        clients.removeElement(cp);
    }
}

public static Vector getClients() {
    return clients;
}

/*
 * 服务端在此启动
 */
public static void main(String args[]) {
    int port = DEFAULT_PORT;

    try {
        serverSocket = new ServerSocket(port);
        System.out.println("服务器已经启动.......正在监听......");
    } catch (IOException e) {
        System.out.println("服务器在启动过程中发生异常");
        System.err.println(e);
        System.exit(1);
    }
    while (true) {//死循环
        try {
            Socket cs = serverSocket.accept();
            ClientProc cp = new ClientProc(cs);//启动一个用户线程
            Thread ct = new Thread(cp);
            ct.start();
            addConnection(cs, cp);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

}
