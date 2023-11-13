/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import javax.swing.*;
import java.awt.*;

/**
 * @author Administrator
 */
public class TranslucentWindow extends JFrame {
public TranslucentWindow() {
    super("透明窗体");
    this.setLayout(new FlowLayout());
    this.add(new JButton("按钮"));
    this.add(new JCheckBox("复选按钮"));
    this.add(new JRadioButton("单选按钮"));
    this.add(new JProgressBar(20, 100));
    this.setSize(new Dimension(400, 300));
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

public static void main(String[] args) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            Window w = new TranslucentWindow();
            w.setVisible(true);
            com.sun.awt.AWTUtilities.setWindowOpacity(w, 0.6f);
        }
    });
}

}

