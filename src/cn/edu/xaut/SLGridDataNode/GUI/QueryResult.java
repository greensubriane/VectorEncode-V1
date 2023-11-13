package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Access.ProcessQuery;
import cn.edu.xaut.SLGridDataNode.Tool.ScreenSize;

public class QueryResult extends javax.swing.JFrame {


/** Creates new form QueryResult */
public QueryResult(String DocName, String XPathExpr) {
    this.DocName = DocName;
    this.XPathExpr = XPathExpr;
    initComponents();
    setBounds((ScreenSize.getWidth() - getWidth()) / 2, (ScreenSize.getHeight() - getHeight()) / 2, getWidth(), getHeight());
}

public QueryResult(String XMLResult) {
    this.XMLResult = XMLResult;
    ViewResult();
}

// <editor-fold defaultstate="collapsed" desc="通过将XPath转换为SQL得到查询结果">
private void ViewResult() {
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();

    jMenu1.setText("Menu");
    jMenuBar1.add(jMenu1);

    new cn.edu.xaut.SLGridDataNode.Util.PlafView().ChangeView();
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("显示查询结果");
    setResizable(false);
    setLocationByPlatform(true);
    jTextArea1.setColumns(20);
    jTextArea1.setFont(new java.awt.Font("Times New Roman", 1, 14));
    jTextArea1.setRows(5);
    jTextArea1.setText(this.XMLResult);
    jTextArea1.setEditable(false);
    jScrollPane1.setViewportView(jTextArea1);

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                           .addContainerGap()
                                                                           .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                                                                           .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                           .addContainerGap()
                                                                           .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                                                           .addContainerGap())
    );
    jTabbedPane1.addTab("查询结果", jPanel1);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                                 .addContainerGap()
                                 .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 348, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                                 .addContainerGap()
                                 .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 307, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    pack();
}// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="直接调用XQEngine查询引擎得到查询结果">
private void initComponents() {
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();

    jMenu1.setText("Menu");
    jMenuBar1.add(jMenu1);

    new cn.edu.xaut.SLGridDataNode.Util.PlafView().ChangeView();
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("显示查询结果");
    setResizable(false);
    setLocationByPlatform(true);
    jTextArea1.setColumns(20);
    jTextArea1.setFont(new java.awt.Font("Times New Roman", 1, 14));
    jTextArea1.setRows(5);
    jTextArea1.setText(new ProcessQuery(this.DocName, this.XPathExpr).RunQuery());
    jTextArea1.setEditable(false);
    jScrollPane1.setViewportView(jTextArea1);

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                           .addContainerGap()
                                                                           .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                                                                           .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                           .addContainerGap()
                                                                           .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                                                           .addContainerGap())
    );
    jTabbedPane1.addTab("查询结果", jPanel1);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                                 .addContainerGap()
                                 .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 348, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                                 .addContainerGap()
                                 .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 307, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                 .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    pack();
}// </editor-fold>


public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {

        public void run() {
            new QueryResult("pub.xml", "//author").setVisible(true);
        }
    });
}


private String XPathExpr;
private String DocName;
private String XMLResult;
// 变量声明 - 不进行修改
private javax.swing.JMenu jMenu1;
private javax.swing.JMenuBar jMenuBar1;
private javax.swing.JPanel jPanel1;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JTabbedPane jTabbedPane1;
private javax.swing.JTextArea jTextArea1;
// 变量声明结束

}
