/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Tool.ScreenSize;
import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import cn.edu.xaut.SLGridDataNode.dao.Dao;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;
import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TextTable extends javax.swing.JFrame {

/** Creates new form TextTable */
public TextTable() {
    initComponents();
}

/** This method is called from within the constructor to
 * initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is
 * always regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">
private void initComponents() {
    setBounds((ScreenSize.getWidth() - getWidth()) / 2, (ScreenSize.getHeight() - getHeight()) / 2, getWidth(), getHeight());
    buttonGroup1 = new javax.swing.ButtonGroup();
    jPanel1 = new javax.swing.JPanel();
    jSeparator1 = new javax.swing.JSeparator();
    jLabel1 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox();
    jButton1 = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jSeparator2 = new javax.swing.JSeparator();
    jPanel2 = new javax.swing.JPanel();
    final TTable texttable = new TTable();
    //final TTable texttable = new TTable();
    ArrayList<String> DocList = new ArrayList<String>();
    new cn.edu.xaut.SLGridDataNode.Util.PlafView().ChangeView();
    setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

    jPanel1.setName("jPanel1"); // NOI18N

    jSeparator1.setName("jSeparator1"); // NOI18N

    jLabel1.setText("     选择数据：");
    jLabel1.setName("jLabel1"); // NOI18N
    DocList = this.GetDocList();
    String[] docs = new String[DocList.size()];
    for (int l = 0; l < DocList.size(); l++) {
        docs[l] = DocList.get(l);
    }
    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(docs));
    jComboBox1.setName("jComboBox1"); // NOI18N

    jButton1.setText("选择数据");
    jButton1.setName("jButton1"); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            //jButton1ActionPerformed(evt);
            String DocName = (String) jComboBox1.getSelectedItem();
            String SQLString = "USE vectorxrsystem SELECT * FROM Text WHERE DocumentID = (SELECT DocumentID FROM Document WHERE DocumentName = " + "'" + DocName + "')";
            rs = new DBConnection().ExecuteSQL(SQLString);
            model = new ResultSetTableModel(rs);
            int texttablecolumns = model.getColumnCount();
            List tablecolumns = Dao.getTablecolumns("vectorxrsystem", "Text", LoginNodeBean.getIP().toString(), LoginNodeBean.getDBType().toString());
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
    });

    jScrollPane1.setName("jScrollPane1"); // NOI18N
    jScrollPane1.setViewportView(texttable);

    jSeparator2.setName("jSeparator2"); // NOI18N

    jPanel2.setName("导航栏"); // NOI18N
    int[] options = new int[]{20, 30, 50, 0};
    TTableNavigator navigator = new TTableNavigator(texttable.getTableModel(), options);
    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 518, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 19, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                      .addComponent(jComboBox1, 0, 221, Short.MAX_VALUE)
                                      .addGap(18, 18, 18)
                                      .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addContainerGap())
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addComponent(navigator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                                      .addGap(15, 15, 15)
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton1))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(navigator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    this.setResizable(false);
    pack();
    //this.setDefaultLookAndFeelDecorated(rootPaneCheckingEnabled);
}// </editor-fold>

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new TextTable().setVisible(true);
        }
    });
}

private ArrayList<String> GetDocList() {
    String SQLString = "USE vectorxrsystem SELECT DocumentName FROM Document";
    ArrayList<String> DocList = new ArrayList<String>();
    //ResultSet resultset = null;
    //rs = new DBConnection().ExecuteSQL(SQLString);
    try {
        ResultSet resultset = new DBConnection().ExecuteSQL(SQLString);
        while (resultset.next()) {
            DocList.add(resultset.getString(1));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return DocList;
}

// Variables declaration - do not modify
private javax.swing.ButtonGroup buttonGroup1;
private javax.swing.JButton jButton1;
private javax.swing.JComboBox jComboBox1;
private javax.swing.JLabel jLabel1;
private javax.swing.JPanel jPanel1;
private javax.swing.JPanel jPanel2;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JSeparator jSeparator2;
private javax.swing.JTable jTable1;
// End of variables declaration
private ResultSetTableModel model;
private ResultSet rs;
}

