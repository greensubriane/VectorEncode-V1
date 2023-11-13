/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author He Ting
 */

import cn.edu.xaut.SLGridDataNode.Access.StartProcess;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class XMLFileStorageGUI extends JFrame {
public XMLFileStorageGUI() {
    initComponents();
}

private void OpenActionPerformed(ActionEvent e) {
    // TODO add your code here
    openFile();
}

private void SaveActionPerformed(ActionEvent e) {
    // TODO add your code here
    URL FileURL;
    boolean flag;
    try {
        FileURL = new URL("file:\\" + FilePath);
        System.out.println(FileURL.toString());
        flag = storageprocess.StartProcess(FileURL);
        if (flag)
            JOptionPane.showMessageDialog(this, "数据已存入数据库");
        System.out.println("Success!");
    } catch (Exception ex) {
        //JOptionPane.showMessageDialog(this, "数据在存储过程中出现错误");
        System.err.println(ex.getMessage());
        JOptionPane.showMessageDialog(this, "数据在存储过程中出现错误");
    }
    this.dispose();
}


private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    scrollPane1 = new JScrollPane();
    textArea1 = new JTextArea();
    button1 = new JButton();
    button2 = new JButton();

    //======== this ========
    setResizable(false);
    setMinimumSize(new Dimension(800, 600));
    setTitle("Data Storage");
    setAutoRequestFocus(false);
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "left:149dlu:grow, right:149dlu:grow",
            "fill:250dlu:grow, 9dlu, bottom:10dlu"));

    //======== scrollPane1 ========
    {

        //---- textArea1 ----
        textArea1.setBorder(new TitledBorder("Datashowing"));
        scrollPane1.setViewportView(textArea1);
    }
    contentPane.add(scrollPane1, CC.xywh(1, 1, 2, 1, CC.FILL, CC.FILL));

    //---- button1 ----
    button1.setAutoscrolls(true);
    button1.setText("Open");
    button1.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
    button1.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            OpenActionPerformed(e);
        }
    });
    contentPane.add(button1, CC.xywh(1, 2, 1, 2, CC.FILL, CC.FILL));

    //---- button2 ----
    button2.setText("Storage");
    button2.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
    button2.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            SaveActionPerformed(e);
        }
    });
    contentPane.add(button2, CC.xywh(2, 2, 1, 2, CC.FILL, CC.FILL));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JScrollPane scrollPane1;
private JTextArea textArea1;
private JButton button1;
private JButton button2;
private String FilePath;
private StartProcess storageprocess = new StartProcess();

// JFormDesigner - End of variables declaration  //GEN-END:variables
private void openFile() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));

    chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
        }

        @Override
        public String getDescription() {
            return "XML files";
        }
    });
    int r = chooser.showOpenDialog(this);
    if (r != JFileChooser.APPROVE_OPTION) return;
    File f = chooser.getSelectedFile();
    FilePath = f.getAbsolutePath();
    System.out.println(FilePath);
    try {
        byte[] bytes = new byte[(int) f.length()];
        new FileInputStream(f).read(bytes);
        textArea1.setText(new String(bytes));
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

}

