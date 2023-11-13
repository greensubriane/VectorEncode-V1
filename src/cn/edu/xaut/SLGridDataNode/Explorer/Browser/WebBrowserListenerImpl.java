/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListener;

import javax.swing.*;
import java.net.URL;

class WebBrowserListenerImpl implements WebBrowserListener {
private final BrowserPanel browserPanel;
private ImageIcon icon = null;

/**
 * 监听器的构造方法
 */
public WebBrowserListenerImpl(BrowserPanel browserPanel) {
    this.browserPanel = browserPanel;
}

public void initializationCompleted(WebBrowserEvent arg0) {
}

/**
 * 网页开始下载的事件处理方法
 */
@Override
public void downloadStarted(WebBrowserEvent arg0) {
    BrowserPanel.frame.setStatus(arg0.getData(), icon);
}

/**
 * 下载完毕的事件处理方法
 */
@Override
public void downloadCompleted(WebBrowserEvent arg0) {
    BrowserPanel.frame.setStatus("完毕", icon);    // 改变状态栏的提示文本
    // 设置后退按钮的状态
    BrowserPanel.frame.getBackButton().setEnabled(
            this.browserPanel.browser.isBackEnabled());
    // 设置前进按钮的状态
    BrowserPanel.frame.getForwardButton().setEnabled(
            this.browserPanel.browser.isForwardEnabled());
    // 获取容纳浏览器的选项卡面板组件
    JTabbedPane tabbed = BrowserPanel.frame.getJTabbedPane1();
    URL url = this.browserPanel.browser.getURL();// 获取当前选择的浏览器的URL对象
    if (tabbed.getSelectedComponent() == this.browserPanel && url != null) {
        // 使用当前浏览器组件的URL更新地址文本框的内容
        BrowserPanel.frame.getAddress().setText(url.toString());
    }
}

@Override
public void downloadProgress(WebBrowserEvent arg0) {
}

@Override
public void downloadError(WebBrowserEvent arg0) {
    BrowserPanel.frame.setStatus(arg0.getData(), icon);
}

/**
 * 文档下载完毕的处理方法
 */
@Override
public void documentCompleted(WebBrowserEvent arg0) {
    // 改变状态栏的提示文本
    BrowserPanel.frame.setStatus("完毕", icon);
}

/**
 * 网页标题改变的事件处理方法
 */
@Override
public void titleChange(WebBrowserEvent e) {
    // 获取容纳浏览器组件的选项卡面板组件
    JTabbedPane tabbed = BrowserPanel.frame.getJTabbedPane1();
    // 获浏览器面板组件在取选项卡面板中的索引
    int index = tabbed.indexOfComponent(this.browserPanel);
    String data = e.getData();            // 获取网页的标题
    tabbed.setToolTipTextAt(index, data);    // 设置文本提示
    if (data.length() > 10) {
        data = data.substring(0, 10);        // 取标题的10个字符
    }
    tabbed.setTitleAt(index, data);            // 设置选项卡标签文本为标题内容
}

/**
 * 处理状态改变事件的方法
 */
@Override
public void statusTextChange(WebBrowserEvent arg0) {
    // 改变状态栏文本
    BrowserPanel.frame.setStatus(arg0.getData(), icon);
}

public void windowClose(WebBrowserEvent arg0) {
}
}
