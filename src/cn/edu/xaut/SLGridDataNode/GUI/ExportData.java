/*
 * Created by JFormDesigner on Tue Oct 11 23:24:17 CEST 2011
 */

package cn.edu.xaut.SLGridDataNode.GUI;

import cn.edu.xaut.SLGridDataNode.Access.GenerateXML;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author ting
 */
public class ExportData extends JFrame {

private File file = null;
private JFileChooser filechooser = new JFileChooser();
ArrayList<String> DocList = new ArrayList<String>();

public ExportData() {
    initComponents();
}

private void DataChooseActionPerformed(ActionEvent e) {
    // TODO add your code here
    System.out.println(comboBox1.getSelectedItem().toString());
    final String XMLString = new GenerateXML(comboBox1.getSelectedItem().toString()).ConstructXML();
    textArea1.setText(XMLString);

}

private void SaveasActionPerformed(ActionEvent e) {
    // TODO add your code here
    if (file != null) {
        filechooser.setSelectedFile(file);
    }
    int returnVal = filechooser.showSaveDialog(ExportData.this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = filechooser.getSelectedFile();
        saveFile();
    }
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    label1 = new JLabel();
    comboBox1 = new JComboBox();
    button1 = new JButton();
    scrollPane1 = new JScrollPane();
    textArea1 = new JTextArea();
    button3 = new JButton();
    DocList = this.GetDocList();
    for (int m = 0; m < DocList.size(); m++)
        comboBox1.addItem(DocList.get(m).toString());
    //======== this ========
    setResizable(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "71dlu, pref, center:150dlu, pref, 65dlu",
            "top:default, pref, fill:269dlu, pref, 17dlu"));

    //---- label1 ----
    label1.setText("            FileName:");
    contentPane.add(label1, CC.xy(1, 1, CC.FILL, CC.FILL));
    contentPane.add(comboBox1, CC.xywh(2, 1, 3, 1, CC.FILL, CC.FILL));

    //---- button1 ----
    button1.setText("choose");
    button1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DataChooseActionPerformed(e);
        }
    });
    contentPane.add(button1, CC.xy(5, 1));

    //======== scrollPane1 ========
    {
        scrollPane1.setViewportView(textArea1);
    }
    contentPane.add(scrollPane1, CC.xywh(1, 2, 5, 3));

    //---- button3 ----
    button3.setText("Save as");
    button3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            SaveasActionPerformed(e);
        }
    });
    contentPane.add(button3, CC.xywh(2, 5, 3, 1));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JLabel label1;
private JComboBox comboBox1;
private JButton button1;
private JScrollPane scrollPane1;
private JTextArea textArea1;
private JButton button3;

// JFormDesigner - End of variables declaration  //GEN-END:variables
private ArrayList<String> GetDocList() {
    String SQLString = "SELECT documentname FROM document";
    ArrayList<String> DocList = new ArrayList<String>();
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

private void saveFile() {
    try {
        FileWriter fw = new FileWriter(file);
        fw.write(textArea1.getText());
        fw.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e);
    }
}
}
