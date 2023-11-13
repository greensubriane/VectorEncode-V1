/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode;

/**
 * @author greensubmarine
 * 类功能说明：登录界面
 */

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Scanner;

public class Login {
public static void main(String[] args) {
    // TODO 自动生成方法存根
    try {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null || !splash.isVisible()) {
            JOptionPane.showMessageDialog(null, "程序缺少必要的资源文件，导致闪屏界面失败，程序将终止执行。", "资源不全", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new Thread() {
            private FileInputStream fis;
            private Scanner scanner;
            final Graphics2D g2 = splash.createGraphics();
            final Dimension size = splash.getSize();

            //private NodeMapView nodemapview = new NodeMapView();
            @Override
            public void run() {
                try {
                    drawInfo("程序启动中……");
                    //initAndRecLog();
                    // 初始化并记录日志
                    System.out.println("系统正在启动中......");
                    System.out.println("系统启动正常......");
                    System.out.println("进入数据节点登陆窗体......");
                    System.out.println("成功进入数据节点登陆窗体......");
                    fis = new FileInputStream("StartUp.log");
                    scanner = new Scanner(fis);
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        str = "启动信息：" + str.toString();
                        drawInfo(str);
                        Thread.sleep(500);
                    }
                    scanner.close();
                    fis.close();
                    //nodemapview.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 在闪屏界面绘制文本信息
            private void drawInfo(String info) {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, size.height - 20, size.width, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("方正姚体", Font.BOLD, 15));
                g2.drawString(info, 10, size.height - 5);
                splash.update();
            }
        }.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
