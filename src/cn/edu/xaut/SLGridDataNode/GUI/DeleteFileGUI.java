/*
 * Created by JFormDesigner on Fri Oct 28 12:06:27 CEST 2011
 */

package cn.edu.xaut.SLGridDataNode.GUI;

import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jgoodies.forms.factories.CC.xy;
import static com.jgoodies.forms.factories.CC.xywh;

/**
 * @author ting
 */
public class DeleteFileGUI extends JFrame {
public DeleteFileGUI() {
    initComponents();
}

private void DeleteActionPerformed(ActionEvent e) {
    // TODO add your code here
    String NowDoc = (String) comboBox1.getSelectedItem();
    String DelDoc =
            "DELETE FROM  namespace WHERE documentid = (SELECT documentid FROM document WHERE documentname='" + NowDoc + "'); " +
                    "DELETE FROM  text WHERE documentid = (SELECT documentid FROM document WHERE documentname='" + NowDoc + "'); " +
                    "DELETE FROM  attribute WHERE documentid = (SELECT documentid FROM document WHERE documentname='" + NowDoc + "'); " +
                    "DELETE FROM  element WHERE documentid = (SELECT documentid FROM document WHERE documentname='" + NowDoc + "'); " +
                    "DELETE FROM  document WHERE documentname='" + NowDoc + "'; ";
    try {
        new DBConnection().ExecuteInsert(DelDoc);
    } catch (Exception e1) {
        e1.printStackTrace();
    }
    ArrayList<String> NowList = GetDocList();
    for (int m = 0; m < NowList.size(); m++)
        comboBox1.addItem(NowList.get(m).toString());
}

private void DeleteAllActionPerformed(ActionEvent e) {
    // TODO add your code here
    String DelDoc = "DELETE FROM namespace ;" +
                            "DELETE FROM text ;" +
                            "DELETE FROM element ;" +
                            "DELETE FROM attribute ;" +
                            "DELETE FROM document ;";
    try {
        new DBConnection().ExecuteInsert(DelDoc);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    label1 = new JLabel();
    comboBox1 = new JComboBox();
    button1 = new JButton();
    button2 = new JButton();
    ArrayList<String> DocList = new ArrayList<>();
    DocList = this.GetDocList();
    for (int m = 0; m < DocList.size(); m++)
        comboBox1.addItem(DocList.get(m).toString());
    //======== this ========
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "63dlu, 65dlu, 67dlu, 60dlu",
            "14dlu, 10dlu, 15dlu"));

    //---- label1 ----
    label1.setText("     File Name:");
    contentPane.add(label1, xy(1, 1));
    contentPane.add(comboBox1, xywh(2, 1, 3, 1));

    //---- button1 ----
    button1.setText("Delete");
    button1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DeleteActionPerformed(e);
        }
    });
    contentPane.add(button1, xywh(1, 3, 2, 1));

    //---- button2 ----
    button2.setText("Delete All");
    button2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DeleteAllActionPerformed(e);
        }
    });
    contentPane.add(button2, xywh(3, 3, 2, 1));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

private ArrayList<String> GetDocList() {
    String SQLString = "SELECT documentname FROM document";
    ArrayList<String> DocList = new ArrayList<>();
    ResultSet rs = null;
    //rs = new DBConnection().ExecuteSQL(SQLString);
    try {
        rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            DocList.add(rs.getString(1));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return DocList;
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JLabel label1;
private JComboBox comboBox1;
private JButton button1;
private JButton button2;
// JFormDesigner - End of variables declaration  //GEN-END:variables
}
