/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author He Ting
 */

import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import cn.edu.xaut.SLGridDataNode.dao.Dao;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
/*
 * Created by JFormDesigner on Mon Sep 05 10:35:04 CEST 2011
 */

/**
 * @author heting
 */
public class TextInformation extends JFrame {
public TextInformation() {
    initComponents();
}

final TTable texttable = new TTable();

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    label1 = compFactory.createLabel("   FileName:");
    comboBox1 = new JComboBox();
    button1 = new JButton();
    panel5 = new JPanel();
    scrollPane1 = new JScrollPane();

    ArrayList<String> DocList = new ArrayList<String>();
    //======== this ========
    setTitle("Element Information");
    setMinimumSize(new Dimension(800, 600));
    setVisible(true);
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "61dlu, center:211dlu, default:grow",
            "16dlu, 14dlu, fill:default:grow"));
    DocList = this.GetDocList();
    String[] docs = new String[DocList.size()];
    for (int l = 0; l < DocList.size(); l++) {
        docs[l] = DocList.get(l);
    }
    comboBox1.setModel(new javax.swing.DefaultComboBoxModel(docs));


    contentPane.add(label1, CC.xy(1, 1, CC.FILL, CC.CENTER));
    contentPane.add(comboBox1, CC.xy(2, 1, CC.FILL, CC.CENTER));

    //---- button1 ----
    button1.setText("Choose");
    button1.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
    button1.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ChooseActionPerformed(e);
        }
    });
    contentPane.add(button1, CC.xy(3, 1, CC.DEFAULT, CC.CENTER));
    //======== panel5 ========
    {
        panel5.setLayout(new BorderLayout());
        int[] options = new int[]{20, 30, 50, 0};
        TTableNavigator navigator = new TTableNavigator(texttable.getTableModel(), options);
        panel5.add(navigator, BorderLayout.CENTER);
    }
    contentPane.add(panel5, CC.xywh(1, 2, 3, 1));
    //======== scrollPane1 ========
    {
        scrollPane1.setViewportView(texttable);
    }
    contentPane.add(scrollPane1, CC.xywh(1, 3, 3, 1));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

private ArrayList<String> GetDocList() {
    String SQLString = "SELECT documentname FROM document";
    ArrayList<String> DocList = new ArrayList<String>();
    try {
        ResultSet rs1 = new DBConnection().ExecuteSQL(SQLString);
        while (rs1.next()) {
            DocList.add(rs1.getString(1));
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return DocList;
}

private void ChooseActionPerformed(ActionEvent e) {
    // TODO add your code here
    String DocName = (String) comboBox1.getSelectedItem();
    String SQLString = "USE vectorxrsystem SELECT * FROM text WHERE documentid = (SELECT documentid FROM document WHERE documentname = " + "'" + DocName + "')";
    String SQLStrings = "SELECT * FROM Text WHERE documentid = (SELECT documentid FROM document WHERE documentname = " + "'" + DocName + "')";
    rs = new DBConnection().ExecuteSQL(SQLStrings);
    model = new ResultSetTableModel(rs);
    int texttablecolumns = model.getColumnCount();
    java.util.List tablecolumns = Dao.getTablecolumns("gmldatabase", "text", LoginNodeBean.getIP().toString(), LoginNodeBean.getDBType().toString());
    int rowcount = model.getRowCount();
    TTableModel textmodel = texttable.getTableModel();
    textmodel.clearPublishedData();
    textmodel.clearAllColumns();
    for (int m = 0; m < texttablecolumns; m++) {
        textmodel.addColumn(new TTableColumn(tablecolumns.get(m).toString()));
    }
    int columncount = textmodel.getColumnCount();
    for (int i = 0; i < rowcount; i++) {
        Vector row = new Vector();
        for (int j = 0; j < columncount; j++) {
            row.addElement(model.getValueAt(i, j));
        }
        textmodel.addRow(row);
    }

}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JLabel label1;
private JComboBox comboBox1;
private JButton button1;
private JPanel panel5;
private JScrollPane scrollPane1;
private ResultSet rs;
private ResultSetTableModel model;
// JFormDesigner - End of variables declaration  //GEN-END:variables
}

