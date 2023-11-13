/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Chat.server;

/**
 * @author greensubmarine
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
static Socket server;

public static void main(String args[]) throws Exception {
    server = new Socket(InetAddress.getLocalHost(), 7000);
    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    PrintWriter out = new PrintWriter(server.getOutputStream());
    BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
        String str = wt.readLine();
        out.println(str);
        out.flush();
        if (str.equals("end")) {
            break;
        }
        System.out.println(in.readLine());
    }
    server.close();
}
}
