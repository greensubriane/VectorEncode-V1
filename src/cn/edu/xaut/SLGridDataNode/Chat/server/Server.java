/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Chat.server;

/**
 * @author greensubriane
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
public static void main(String args[]) throws IOException {
    ServerSocket server = new ServerSocket(7000);
    Socket client = server.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    PrintWriter out = new PrintWriter(client.getOutputStream());
    while (true) {
        String str = in.readLine();
        System.out.println(str);
        out.println("has received......");
        out.flush();
        if (str.equals("end")) {
            break;
        }
        // client.close();
    }
}
}
