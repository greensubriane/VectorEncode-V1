/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author Administrator
 */

import cn.edu.xaut.SLGridDataNode.Tool.ScreenSize;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ElementTable extends JFrame {

/** Creates new form ElementTable */
public ElementTable() {
    initComponents();
    setBounds((ScreenSize.getWidth() - getWidth()) / 2, (ScreenSize.getHeight() - getHeight()) / 2, getWidth(), getHeight());
    this.setResizable(false);
    this.setTitle("数据元素信息查看");
}

/** This method is called from within the constructor to
 * initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is
 * always regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">
private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jSeparator1 = new javax.swing.JSeparator();
    jLabel1 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox();
    jButton1 = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    ArrayList<String> DocList = new ArrayList<String>();

    new cn.edu.xaut.SLGridDataNode.Util.PlafView().ChangeView();
    setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

    jPanel1.setName("jPanel1"); // NOI18N

    jSeparator1.setName("jSeparator1"); // NOI18N

    jLabel1.setText("     选择数据：");
    jLabel1.setName("jLabel1"); // NOI18N

    //jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jComboBox1.setName("jComboBox1"); // NOI18N

    DocList = this.GetDocList();
    for (int m = 0; m < DocList.size(); m++) {
        jComboBox1.addItem(DocList.get(m).toString());
    }

    jButton1.setText("选择");
    jButton1.setName("jButton1"); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {


        public void actionPerformed(java.awt.event.ActionEvent evt) {
            //jButton1ActionPerformed(evt);
            String DocName = (String) jComboBox1.getSelectedItem();
            String SQLString = "USE vectorxrsystem SELECT VectorElementPre AS 元素先序编码, VectorElementPost AS 元素后序编码, ElementTag AS 元素标记, VectorParentElementPre AS 父元素先序编码, "
                                       + "VectorParentElementPost AS 父元素后序编码 FROM Element WHERE DocumentID = (SELECT DocumentID FROM Document WHERE DocumentName = " + "'" + DocName + "')";
            rs = new DBConnection().ExecuteSQL(SQLString);
            if (rs != null) {
                model = new ResultSetTableModel(rs);
                jTable1 = new javax.swing.JTable(model);
                jTable1.setFont(new java.awt.Font("宋体", 0, 14));
                jScrollPane1.setViewportView(jTable1);
            }
            jScrollPane1.setViewportView(jTable1);
        }
    });

    jScrollPane1.setName("jScrollPane1"); // NOI18N


    //Table1.setName("jTable1"); // NOI18N
    // jScrollPane1.setViewportView(jTable1);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE));
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1)).addGap(21, 21, 21).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
    pack();
    //pack();
}// </editor-fold>

private ArrayList<String> GetDocList() {
    String SQLString = "USE vectorxrsystem SELECT DocumentName FROM Document";
    ArrayList<String> DocList = new ArrayList<String>();
    try {
        ResultSet rs1 = new DBConnection().ExecuteSQL(SQLString);
        while (rs1.next()) {
            DocList.add(rs1.getString(1));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return DocList;
}

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {


        public void run() {
            new ElementTable().setVisible(true);
        }
    });
}

// Variables declaration - do not modify
private javax.swing.JButton jButton1;
private javax.swing.JComboBox jComboBox1;
private javax.swing.JLabel jLabel1;
private javax.swing.JPanel jPanel1;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JTable jTable1;
private ResultSetTableModel model;
private ResultSet rs;
// End of variables declaration
}
