/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Chat.server;

/**
 * @author greensubmarine
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class ClientProc implements Runnable {
/*
 * 为某个用户服务的单一线程
 */
Socket s;
BufferedReader in;
PrintWriter out;
private String name = null;
private String sex = null;

public ClientProc(Socket s) throws IOException {
    this.s = s;
    in = new BufferedReader(new InputStreamReader(s.getInputStream()));//得到输入的端口流
    out = new PrintWriter(s.getOutputStream());//从端口得到一个输出流
    this.updateList();
}

public String getName() {
    return name;
}

public String getSex() {
    return sex;
}

public Socket getSocket() {
    return s;
}

private void updateList() {
    //更新用户列表
    Vector cs = ChatServer.getClients();
    if (cs != null) {
        for (Enumeration e = cs.elements(); e.hasMoreElements(); ) {
            ClientProc cp = (ClientProc) e.nextElement();
            String exist_name = cp.getName();
            String exist_sex = cp.getSex();
            System.out.println("old" + "&" + exist_name + "&" + exist_sex);
            out.println("old" + "&" + exist_name + "&" + exist_sex);
            out.flush();
        }
    }
}


public void run() {
    while (name == null) {
        try {
            String inmsg;
            inmsg = in.readLine();
            ChatServer.sendAll("new" + inmsg); //发送信息更新用户列表
            String[] userInfo;
            userInfo = inmsg.split("&");
            name = userInfo[0];
            sex = userInfo[1];
        } catch (IOException ee) {
            ee.printStackTrace();
        }

        while (true) {
            try {
                String line = in.readLine();
                System.out.println(line);
                //处理推出事件(读取信息)
                if (line.equals("quit")) {
                    ChatServer.sendAll("【系统消息】" + this.name + "退出了聊天室");
                    ChatServer.deleteConnection(s, this);
                    return;
                    //处理刷心用户列表的请求
                } else {
                    String[] inmsg = line.split("&");
                    if (inmsg[0].compareTo("cancelsendfile") == 0) {
                        ChatServer.sendOne(inmsg[0], "sendfile" + "&" + this.name + "&" + inmsg[5]);
                    }
                    //接受信息
                    else if (inmsg[0].compareTo("acceptfile") == 0) {
                        ChatServer.sendOne(inmsg[1], inmsg[0] + "&" + this.name);
                    } else if (!line.startsWith("withWho")) {
                        //发送至全体用户
                        ChatServer.sendAll(this.name + ":" + line);
                    } else if (inmsg[1].equals("privateTrue")) {
                        if (ChatServer.sendOne(inmsg[2], "privateTalk" + "&" + name + "&" + inmsg[2] + "&" + inmsg[3])) {
                            //发送至用户自己
                            out.flush();
                        } else {
                            out.println(inmsg[2] + "已经离开聊天室");
                            out.flush();
                        }
                    } else {
                        ChatServer.sendAll("withWho" + "&" + name + "&" + inmsg[2] + "&" + inmsg[3]);
                        //发送给所有用户
                    }
                }
            } catch (IOException e) {
                System.out.println(e.toString());
                try {
                    s.close();
                } catch (IOException e2) {

                }
                return;
            }
        }
    }
}
}
