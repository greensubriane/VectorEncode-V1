/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import java.awt.*;
import java.net.URL;

public class BrowserPanel extends javax.swing.JPanel {
Browser browser;                        // 浏览器组件
public static BrowserFrame frame;        // 主窗体对象
private WebBrowserListenerImpl webBrowserListenerImpl;    // 浏览器的监听器
private javax.swing.JPanel jPanel1;

public BrowserPanel() {
    super();
}

/**
 * 浏览器面板的构造方法
 */
public BrowserPanel(URL homePage, BrowserFrame frameArg) {
    BrowserPanel.frame = frameArg;
    browser = new Browser(homePage);    // 创建浏览器组件
    // 创建浏览器组件的事件监听器
    webBrowserListenerImpl = new WebBrowserListenerImpl(this);
    // 为浏览器组件添加事件监听器
    browser.addWebBrowserListener(webBrowserListenerImpl);
    // 调用初始化方法
    initComponents();
}

/**
 * 初始化方法
 */
private void initComponents() {
    jPanel1 = new javax.swing.JPanel();    // 创建面板对象
    setLayout(new java.awt.BorderLayout());// 设置布局管理器
    // 设置背景颜色
    jPanel1.setBackground(new java.awt.Color(255, 255, 255));
    // 设置面板的布局管理器
    jPanel1.setLayout(new java.awt.BorderLayout());
    // 将浏览器组件添加到面板的Center位置
    jPanel1.add(browser, BorderLayout.CENTER);
    // 将面板添加到界面中
    add(jPanel1, java.awt.BorderLayout.CENTER);
}

/**
 * 获取浏览器面板组件中的浏览器组件的方法
 */
public Browser getBrowser() {
    return browser;
}
}

