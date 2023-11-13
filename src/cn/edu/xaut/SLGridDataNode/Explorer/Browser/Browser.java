/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

import org.jdesktop.jdic.browser.WebBrowser;

import java.net.URL;

/**
 * @author greensubmarine
 */
public class Browser extends WebBrowser {
public Browser(URL url) {
    super(url);        // 调用父类构造方法
}

/**
 * 重写父类的willOpenWindow方法
 */
protected boolean willOpenWindow(URL url) {
    if (BrowserPanel.frame != null) {
        // 创建新的选项卡显示网页
        BrowserPanel.frame.createBrowserTab(url);
    }
    return false;
}

public void print() {
    throw new UnsupportedOperationException("Not yet implemented");
}
}
