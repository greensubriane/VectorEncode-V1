/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode;

import cn.edu.xaut.SLGridDataNode.GUI.DataLook;
import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * Created by JFormDesigner on Sun Sep 04 19:32:20 CEST 2011
 */

/**
 * @author heting
 * 2023-09-12
 */
public class DataBaseLogin extends JFrame {
public DataBaseLogin() {
    initComponents();
}

private void LoginActionPerformed(ActionEvent e) {
    // TODO add your code here
    login();
}

private void ResetActionPerformed(ActionEvent e) {
    // TODO add your code here
    reset();
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    label1 = compFactory.createLabel("         DBType:");
    textField1 = new JTextField();
    label2 = compFactory.createLabel("        Host-IP:");
    textField2 = new JTextField();
    label3 = compFactory.createLabel("       Port-Num:");
    textField3 = new JTextField();
    label4 = compFactory.createLabel("       UserName:");
    textField4 = new JTextField();
    label5 = compFactory.createLabel("       Password:");
    passwordField1 = new JPasswordField();
    button1 = new JButton();
    button2 = new JButton();
    //======== this ========
    setResizable(false);
    setTitle("DataBaseLogin");
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "left:88dlu:grow, right:88dlu:grow",
            "15dlu:grow, 15dlu:grow, 15dlu:grow, 15dlu:grow, 15dlu:grow, 15dlu:grow"
                    + ""));
    contentPane.add(label1, CC.xy(1, 1, CC.FILL, CC.FILL));
    contentPane.add(textField1, CC.xy(2, 1, CC.FILL, CC.BOTTOM));
    contentPane.add(label2, CC.xy(1, 2, CC.FILL, CC.FILL));
    contentPane.add(textField2, CC.xy(2, 2, CC.FILL, CC.BOTTOM));
    contentPane.add(label3, CC.xy(1, 3, CC.FILL, CC.FILL));
    contentPane.add(textField3, CC.xy(2, 3, CC.FILL, CC.BOTTOM));
    contentPane.add(label4, CC.xy(1, 4, CC.FILL, CC.FILL));
    contentPane.add(textField4, CC.xy(2, 4, CC.FILL, CC.BOTTOM));
    contentPane.add(label5, CC.xy(1, 5, CC.FILL, CC.FILL));
    contentPane.add(passwordField1, CC.xy(2, 5, CC.FILL, CC.BOTTOM));

    //---- button1 ----
    button1.setText("Login");
    button1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                LoginActionPerformed(e);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    });
    contentPane.add(button1, CC.xy(1, 6, CC.FILL, CC.DEFAULT));
    //---- button2 ----
    button2.setText("Reset");
    button2.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ResetActionPerformed(e);
        }
    });
    contentPane.add(button2, CC.xy(2, 6, CC.FILL, CC.DEFAULT));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}


// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JLabel label1;
private JTextField textField1;
private JLabel label2;
private JTextField textField2;
private JLabel label3;
private JTextField textField3;
private JLabel label4;
private JTextField textField4;
private JLabel label5;
private JPasswordField passwordField1;
private JButton button1;
private JButton button2;
// JFormDesigner - End of variables declaration  //GEN-END:variables

public void login() {
    String dbtype = textField1.getText();
    String ip = textField2.getText();
    String port = textField3.getText();
    String username = textField4.getText();
    String password = passwordField1.getText();
    if (ip.length() == 0 || port.length() == 0 || dbtype.length() == 0 || username.length() == 0 || password.length() == 0) {
        JOptionPane.showMessageDialog(null, "please don't input value of null");
    } else {
        LoginNodeBean.setDBType(dbtype);
        LoginNodeBean.setIP(ip);
        LoginNodeBean.setPortNum(port);
        LoginNodeBean.setUsername(username);
        LoginNodeBean.setPassword(password);
        this.setVisible(false);
        try {
            // new MainPanel().setVisible(true);
            new DataLook().setVisible(true);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}

public void reset() {
    textField1.setText("");
    textField2.setText("");
    textField3.setText("");
    textField4.setText("");
    passwordField1.setText("");
}

public static void main(String args[]) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new DataBaseLogin().setVisible(true);
        }
    });

}
}
