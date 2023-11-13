package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KeyFile extends JDialog {
private JFrame frame = null;
private JPanel pane = null;
private JButton button1 = new JButton(), button2 = new JButton();
private JLabel label1 = new JLabel("字段1"), label2 = new JLabel("字段2"), label3 = new JLabel("字段3");
public JTextArea key1 = new JTextArea(), key2 = new JTextArea(), key3 = new JTextArea();

public String keyA = null, keyB = null, keyC = null;

public KeyFile() {
    try {
        //frame = new JFrame("JTableTest");
        pane = new JPanel();
        pane.setLayout(null);


        label1.setSize(new Dimension(100, 40));
        label1.setBounds(new Rectangle(10, 50, 150, 40));
        label1.setFont(label1.getFont().deriveFont(20f));
        label1.setText("第一分类字段");
        key1.setSize(new Dimension(100, 40));
        key1.setBounds(new Rectangle(150, 50, 300, 40));
        key1.setBackground(new Color(128, 128, 128));
        key1.setFont(key1.getFont().deriveFont(20f));


        label2.setSize(new Dimension(100, 40));
        label2.setBounds(new Rectangle(10, 150, 300, 40));
        label2.setFont(label2.getFont().deriveFont(20f));
        label2.setText("第二分类字段");
        key2.setSize(new Dimension(110, 40));
        key2.setBounds(new Rectangle(150, 150, 300, 40));
        key2.setBackground(new Color(128, 128, 128));
        key2.setFont(key2.getFont().deriveFont(15f));

        label3.setSize(new Dimension(100, 40));
        label3.setBounds(new Rectangle(10, 250, 300, 40));
        label3.setFont(label3.getFont().deriveFont(20f));
        label3.setText("第三分类字段");
        key3.setSize(new Dimension(100, 40));
        key3.setBounds(new Rectangle(150, 250, 300, 40));
        key3.setBackground(new Color(128, 128, 128));
        key3.setFont(key3.getFont().deriveFont(15f));

        button1.setSize(new Dimension(80, 40));
        button1.setBounds(new Rectangle(135, 350, 200, 40));
        button1.setFont(button1.getFont().deriveFont(20f));
        button1.setText("确定");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    keyA = key1.getText();
                    keyB = key2.getText();
                    keyC = key3.getText();
                    if (!keyA.equals("")) {
                        dispose();
                    } else {
                        String strMsg = "请正确输入!";
                        JOptionPane.showMessageDialog(null, strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        pane.add(label1);
        pane.add(label2);
        pane.add(label3);
        pane.add(key1);
        pane.add(key2);
        pane.add(key3);
        pane.add(button1);

        this.getContentPane().add(pane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(470, 470);
        this.setLocation(250, 250);
        this.setTitle("子类生成信息");
        this.setResizable(false);
        this.setModal(true);
        this.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public KeyFile(boolean self) {
    try {
        //frame = new JFrame("JTableTest");
        pane = new JPanel();
        pane.setLayout(null);


        label1.setSize(new Dimension(100, 40));
        label1.setBounds(new Rectangle(10, 50, 150, 40));
        label1.setFont(label1.getFont().deriveFont(20f));
        label1.setText("主键字段");
        key1.setSize(new Dimension(100, 40));
        key1.setBounds(new Rectangle(150, 50, 300, 40));
        key1.setBackground(new Color(128, 128, 128));
        key1.setFont(key1.getFont().deriveFont(20f));

        label2.setSize(new Dimension(100, 40));
        label2.setBounds(new Rectangle(10, 150, 300, 40));
        label2.setFont(label2.getFont().deriveFont(20f));
        label2.setText("相关字段");
        key2.setSize(new Dimension(110, 40));
        key2.setBounds(new Rectangle(150, 150, 300, 40));
        key2.setBackground(new Color(128, 128, 128));
        key2.setFont(key2.getFont().deriveFont(15f));


        label3.setSize(new Dimension(100, 40));
        label3.setBounds(new Rectangle(10, 250, 300, 40));
        label3.setFont(label3.getFont().deriveFont(20f));
        label3.setText("类名字段");
        key3.setSize(new Dimension(100, 40));
        key3.setBounds(new Rectangle(150, 250, 300, 40));
        key3.setBackground(new Color(128, 128, 128));
        key3.setFont(key3.getFont().deriveFont(15f));

        button1.setSize(new Dimension(80, 40));
        button1.setBounds(new Rectangle(130, 350, 200, 40));
        button1.setFont(button1.getFont().deriveFont(20f));
        button1.setText("确定");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    keyA = key1.getText();
                    keyB = key2.getText();
                    keyC = key3.getText();
                    if (!keyA.equals("") & !keyB.equals("") & !keyC.equals("")) {
                        dispose();
                    } else {
                        String strMsg = "请正确输入!";
                        JOptionPane.showMessageDialog(null, strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        pane.add(label1);
        pane.add(key1);
        pane.add(label2);
        pane.add(key2);
        pane.add(label3);
        pane.add(key3);
        pane.add(button1);

        this.getContentPane().add(pane);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(470, 470);
        this.setLocation(250, 250);
        this.setTitle("子类生成信息");
        this.setResizable(false);
        this.setModal(true);
        this.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public static void main(String[] args) {
    new KeyFile(true);
}

}    




