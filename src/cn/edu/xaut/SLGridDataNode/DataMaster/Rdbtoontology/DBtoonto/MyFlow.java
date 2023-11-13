/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.dao.Dao;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import twaver.Node;
import twaver.TDataBox;
import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;
import twaver.tree.ElementNode;
import twaver.tree.TTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class MyFlow {
private TDataBox box = new TDataBox();
private javax.swing.JCheckBox jCheckBox1;
private javax.swing.JCheckBox jCheckBox2;
private javax.swing.JPanel jPanel1;
private javax.swing.JPanel jPanel2;
private javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
private javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
private javax.swing.JScrollPane jScrollPane1, jScrollPane2, jScrollPane3;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JTree jTree1;
private TTable informationtable = new TTable();
private TTable structuretable = new TTable();
JPanel panel2 = new JPanel();
JSplitPane jSplitPane2 = new javax.swing.JSplitPane();
private OWLModel owlModel = null;
private Frame f;
private JFrame frame;
private Button button1, button2, button3;

public static void main(String args[]) {
    MyFlow mflow = new MyFlow();
    mflow.go();
}

public void go() {
    List tables = Dao.getTables(null, null, null);

    for (int i = 0; i < tables.size(); i++) {
        Node rootnode = new Node();
        rootnode.setName(tables.get(i).toString());
        box.addElement(rootnode);
    }
    final TTree jTree1 = new TTree(box);
    int selectmodel = TTree.DEFAULT_SELECTION;
    jTree1.setTTreeSelectionMode(selectmodel);

    jTree1.addTreeNodeClickedActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ElementNode node = (ElementNode) jTree1.getLastSelectedPathComponent();
            if (node == null || !node.isLeaf()) {//如果该节点不是子节点，
                return;
            }
            java.sql.ResultSet rs1 = Dao.getTableVaribles("sl_new", node.getElement().getName().toString(), "127.0.0.1", "SQLSERVER2005");
            ResultSetTableModel model2 = new ResultSetTableModel(rs1);
            final TTableModel informationtablemodel = informationtable.getTableModel();
            informationtablemodel.clearAllColumns();
            informationtablemodel.addColumn(new TTableColumn("字段名"));
            informationtablemodel.addColumn(new TTableColumn("字段类型"));
            informationtablemodel.addColumn(new TTableColumn("字段长度"));
            informationtablemodel.addColumn(new TTableColumn("是否为空"));
            informationtablemodel.addColumn(new TTableColumn("是否为标识"));
            informationtablemodel.addColumn(new TTableColumn("字段描述"));
            informationtablemodel.clearPublishedData();
            //int informationcolumncount = informationtablemodel.getColumnCount();
            int informationtablerowcount = model2.getRowCount();
            for (int l = 0; l < informationtablerowcount; l++) {
                Vector rows = new Vector();
                rows.addElement(model2.getValueAt(l, 0).toString());
                rows.addElement(model2.getValueAt(l, 1).toString());
                rows.addElement(model2.getValueAt(l, 2).toString());
                rows.addElement(model2.getValueAt(l, 3).toString());
                rows.addElement(model2.getValueAt(l, 4).toString());
                rows.addElement(model2.getValueAt(l, 5).toString());
                informationtablemodel.addRow(rows);
            }
            java.util.List structurecolumns = Dao.getTablecolumns("sl_new", node.getElement().getName().toString(), "127.0.0.1", "SQLSERVER2005");
            java.sql.ResultSet rs = Dao.getTableData("sl_new", node.getElement().getName().toString(), "127.0.0.1", "SQLSERVER2005");
            ResultSetTableModel model1 = new ResultSetTableModel(rs);
            int structuretablerowcount = model1.getRowCount();
            final TTableModel structuretablemodel = structuretable.getTableModel();
            structuretablemodel.clearPublishedData();
            structuretablemodel.clearAllColumns();
            for (int t = 0; t < structurecolumns.size(); t++) {
                structuretablemodel.addColumn(new TTableColumn(structurecolumns.get(t).toString()));
            }
            int structuretablecolumncount = structuretablemodel.getColumnCount();
            for (int i = 0; i < structuretablerowcount; i++) {
                Vector row = new Vector();
                for (int m = 0; m < structuretablecolumncount; m++) {
                    row.addElement(model1.getValueAt(i, m));
                }
                structuretablemodel.addRow(row);
            }
        }
    });

    jScrollPane2 = new JScrollPane(informationtable);
    jScrollPane3 = new JScrollPane(structuretable);

    try {
        owlModel = ProtegeOWL.createJenaOWLModel();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        System.err.println(e.getMessage());
    }
    KnowledgeBase kb = (KnowledgeBase) owlModel;
    SuperClsPicker superclspicker = new SuperClsPicker(kb);
    LabeledComponent labelcomponent = new LabeledComponent("本体浏览:", superclspicker, true);

    jPanel1 = new javax.swing.JPanel();
    jSeparator1 = new javax.swing.JSeparator();
    jPanel2 = new javax.swing.JPanel();
    jCheckBox1 = new javax.swing.JCheckBox();
    jCheckBox2 = new javax.swing.JCheckBox();
    jScrollPane1 = new javax.swing.JScrollPane();
    //jTree1 = new javax.swing.JTree();
    jPanel1.setName("jPanel1"); // NOI18N
    jSeparator1.setName("jSeparator1"); // NOI18N
    jPanel2.setBackground(new java.awt.Color(153, 153, 255));
    jPanel2.setName("jPanel2"); // NOI18N
    jCheckBox1.setBackground(new java.awt.Color(153, 153, 255));
    jCheckBox1.setText("导入到当前本体窗口");
    jCheckBox1.setName("jCheckBox1"); // NOI18N
    jCheckBox2.setBackground(new java.awt.Color(153, 153, 255));
    jCheckBox2.setText("导入为独立本体文件");
    jCheckBox2.setName("jCheckBox2"); // NOI18N

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
    );
    jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                                                  .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                  .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jScrollPane1.setName("jScrollPane1"); // NOI18N

    jTree1.setBackground(new java.awt.Color(204, 204, 255));
    jTree1.setName("jTree1"); // NOI18N
    jScrollPane1.setViewportView(jTree1);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    panel2.setName("panel2"); // NOI18N
    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("数据表数据"));
    jPanel3.setName("jPanel3"); // NOI18N
    jScrollPane3 = new javax.swing.JScrollPane(structuretable);
    jScrollPane3.setName("jScrollPane3"); // NOI18N
    int[] options = new int[]{20, 30, 50, 0};
    TTableNavigator navigator = new TTableNavigator(structuretable.getTableModel(), options);
    //jPanel7.add(navigator, new BorderLayout().CENTER);
    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);

    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                                      .addGap(10, 10, 10)
                                      .addComponent(navigator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                                      .addComponent(navigator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("数据表字段"));
    jPanel4.setName("jPanel4"); // NOI18N
    jScrollPane2 = new javax.swing.JScrollPane(informationtable);
    TTableNavigator navigator1 = new TTableNavigator(informationtable.getTableModel(), options);
    jScrollPane2.setName("jScrollPane2"); // NOI18N
    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                                      .addGap(10, 10, 10)
                                      .addComponent(navigator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                                      .addComponent(navigator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
    );
    javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
    panel2.setLayout(panel2Layout);
    panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel2Layout.createSequentialGroup()
                                      .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );


    JPanel panel6 = new JPanel();
    frame = new JFrame("本体提取工具");

    JButton button1 = new JButton("生成本体");

    JSplitPane jSplitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, jPanel1, panel2);
    panel6.add(button1, new BorderLayout().CENTER);
    frame.setLayout(new BorderLayout());
    frame.add(panel6, new BorderLayout().SOUTH);
    frame.add(jSplitPane1, new BorderLayout().CENTER);
    frame.add(labelcomponent, new BorderLayout().WEST);
    frame.setSize(800, 600);
    frame.setVisible(true);

}
}

