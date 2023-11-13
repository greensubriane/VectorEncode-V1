/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.net.URL;

/**
 * 扩展选项卡面板的模型类
 */
public class TabbedModel extends DefaultSingleSelectionModel {
private BrowserFrame frame;

/**
 * 选项卡模型类的构造方法
 */
public TabbedModel(BrowserFrame frameAge) {
    this.frame = frameAge;
    // 添加匿名的变更事件监听器
    addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            // 获取当前选择的浏览器面板中的浏览器组件
            Browser browser = frame.getSelBrowser();
            // 设置和当前浏览器组件对应的后退按钮的状态
            frame.getBackButton().setEnabled(browser.isBackEnabled());
            // 设置和当前浏览器组件对应的前进按钮的状态
            frame.getForwardButton().setEnabled(browser.isForwardEnabled());
            // 获取浏览器组件的URL对象
            URL url = browser.getURL();
            if (url != null) {
                // 是地址文本框的内容与当前浏览器URL同步
                frame.getAddress().setText(url.toString());
            }
        }
    });
}

public TabbedModel() {
}
}

