/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NodeLook.java
 *
 * Created on 2010-5-5, 0:05:48
 */

package cn.edu.xaut.SLGridDataNode.UI;

/**
 * @author Administrator
 */
public class NodeLook extends javax.swing.JFrame {

/**
 * Creates new form NodeLook
 */
public NodeLook() {
    initComponents();
}

/**
 * This method is called from within the constructor to
 * initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is
 * always regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
private void initComponents() {
    bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

    jPanel1 = new javax.swing.JPanel();
    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane3 = new javax.swing.JScrollPane();
    jTable2 = new javax.swing.JTable();
    jPanel9 = new javax.swing.JPanel();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jPanel10 = new javax.swing.JPanel();
    jPanel7 = new javax.swing.JPanel();
    jSeparator1 = new javax.swing.JSeparator();
    jPanel8 = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jPanel6 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTree1 = new javax.swing.JTree();
    jPanel5 = new javax.swing.JPanel();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();
    jMenuItem3 = new javax.swing.JMenuItem();
    jMenuItem4 = new javax.swing.JMenuItem();
    jMenuItem5 = new javax.swing.JMenuItem();
    jMenuItem6 = new javax.swing.JMenuItem();
    jMenuItem7 = new javax.swing.JMenuItem();
    jMenu3 = new javax.swing.JMenu();
    jMenuItem8 = new javax.swing.JMenuItem();
    jMenuItem9 = new javax.swing.JMenuItem();
    jMenu4 = new javax.swing.JMenu();
    jMenuItem10 = new javax.swing.JMenuItem();
    jMenu5 = new javax.swing.JMenu();
    jMenuItem11 = new javax.swing.JMenuItem();
    jMenuItem12 = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel1.setName("jPanel1"); // NOI18N

    jSplitPane1.setBorder(null);
    jSplitPane1.setResizeWeight(0.5);
    jSplitPane1.setName("jSplitPane1"); // NOI18N

    org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jSplitPane1, org.jdesktop.beansbinding.ObjectProperty.create(), jSplitPane1, org.jdesktop.beansbinding.BeanProperty.create("dividerLocation"));
    bindingGroup.addBinding(binding);

    jPanel2.setName("jPanel2"); // NOI18N

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("数据表数据"));
    jPanel3.setName("jPanel3"); // NOI18N

    jScrollPane3.setName("jScrollPane3"); // NOI18N

    jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
            },
            new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
            }
    ));
    jTable2.setName("jTable2"); // NOI18N
    jScrollPane3.setViewportView(jTable2);

    jPanel9.setName("导航"); // NOI18N

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 923, Short.MAX_VALUE)
    );
    jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                                      .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("数据表字段"));
    jPanel4.setName("jPanel4"); // NOI18N

    jScrollPane2.setName("jScrollPane2"); // NOI18N

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
            },
            new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
            }
    ));
    jTable1.setName("jTable1"); // NOI18N
    jScrollPane2.setViewportView(jTable1);

    jPanel10.setName("jPanel10"); // NOI18N

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 913, Short.MAX_VALUE)
    );
    jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 23, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                                      .addGap(10, 10, 10)
                                      .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                                      .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
    );

    jPanel7.setName("jPanel7"); // NOI18N

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 338, Short.MAX_VALUE)
    );

    jSeparator1.setName("jSeparator1"); // NOI18N

    jPanel8.setName("jPanel8"); // NOI18N

    jButton1.setText("导入到独立本体");
    jButton1.setName("jButton1"); // NOI18N

    jButton2.setLabel("取消操作");
    jButton2.setName("取消操作"); // NOI18N

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                  .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                  .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1)
                                                        .addComponent(jButton2))
                                      .addContainerGap(77, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                                                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addGap(354, 354, 354)
                                      .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addGap(40, 40, 40)
                                      .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addContainerGap(94, Short.MAX_VALUE))
    );

    jSplitPane1.setRightComponent(jPanel2);

    jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("节点数据"));
    jPanel6.setName("jPanel6"); // NOI18N

    jScrollPane1.setName("jScrollPane1"); // NOI18N

    jTree1.setName("jTree1"); // NOI18N
    jScrollPane1.setViewportView(jTree1);

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
    );
    jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
    );

    jSplitPane1.setLeftComponent(jPanel6);

    jPanel5.setName("jPanel5"); // NOI18N
    jPanel5.setLayout(new java.awt.GridBagLayout());

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
                                                                                  .addGap(10, 10, 10))
                    .addComponent(jSplitPane1)
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                  .addComponent(jSplitPane1)
                                                                                  .addGap(18, 18, 18)
                                                                                  .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jMenuBar1.setName("jMenuBar1"); // NOI18N

    jMenu1.setText("描述类数据");
    jMenu1.setName("jMenu1"); // NOI18N

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem1.setText("数据存储");
    jMenuItem1.setName("jMenuItem1"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu1, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu1.add(jMenuItem1);

    jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem2.setText("退出");
    jMenuItem2.setName("jMenuItem2"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu1, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem2, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu1.add(jMenuItem2);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("数据元素查询");
    jMenu2.setName("jMenu2"); // NOI18N

    jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem3.setText("Element");
    jMenuItem3.setName("jMenuItem3"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu2, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem3, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu2.add(jMenuItem3);

    jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem4.setText("Attribute");
    jMenuItem4.setName("jMenuItem4"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu2, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem4, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu2.add(jMenuItem4);

    jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem5.setText("Text");
    jMenuItem5.setName("jMenuItem5"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu2, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem5, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu2.add(jMenuItem5);

    jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem6.setText("视图");
    jMenuItem6.setName("jMenuItem6"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu2, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem6, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu2.add(jMenuItem6);

    jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem7.setText("删除数据");
    jMenuItem7.setName("jMenuItem7"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu2, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem7, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu2.add(jMenuItem7);

    jMenuBar1.add(jMenu2);

    jMenu3.setText("数据导出与查询");
    jMenu3.setName("jMenu3"); // NOI18N

    jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem8.setText("导出数据");
    jMenuItem8.setName("jMenuItem8"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu3, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem8, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu3.add(jMenuItem8);

    jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem9.setText("数据查询");
    jMenuItem9.setName("jMenuItem9"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu3, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem9, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu3.add(jMenuItem9);

    jMenuBar1.add(jMenu3);

    jMenu4.setText("节点文件上传与下载");
    jMenu4.setName("jMenu4"); // NOI18N

    jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
    jMenuItem10.setText("文件上传于下载");
    jMenuItem10.setName("jMenuItem10"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu4, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem10, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu4.add(jMenuItem10);

    jMenuBar1.add(jMenu4);

    jMenu5.setText("关于");
    jMenu5.setName("jMenu5"); // NOI18N

    jMenuItem11.setText("功能介绍");
    jMenuItem11.setName("jMenuItem11"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu5, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem11, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu5.add(jMenuItem11);

    jMenuItem12.setText("返回登录界面");
    jMenuItem12.setName("jMenuItem12"); // NOI18N

    binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jMenu5, org.jdesktop.beansbinding.ObjectProperty.create(), jMenuItem12, org.jdesktop.beansbinding.BeanProperty.create("selected"));
    bindingGroup.addBinding(binding);

    jMenu5.add(jMenuItem12);

    jMenuBar1.add(jMenu5);

    setJMenuBar(jMenuBar1);

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

    bindingGroup.bind();

    pack();
}// </editor-fold>//GEN-END:initComponents

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new NodeLook().setVisible(true);
        }
    });
}

// Variables declaration - do not modify//GEN-BEGIN:variables
private javax.swing.JButton jButton1;
private javax.swing.JButton jButton2;
private javax.swing.JMenu jMenu1;
private javax.swing.JMenu jMenu2;
private javax.swing.JMenu jMenu3;
private javax.swing.JMenu jMenu4;
private javax.swing.JMenu jMenu5;
private javax.swing.JMenuBar jMenuBar1;
private javax.swing.JMenuItem jMenuItem1;
private javax.swing.JMenuItem jMenuItem10;
private javax.swing.JMenuItem jMenuItem11;
private javax.swing.JMenuItem jMenuItem12;
private javax.swing.JMenuItem jMenuItem2;
private javax.swing.JMenuItem jMenuItem3;
private javax.swing.JMenuItem jMenuItem4;
private javax.swing.JMenuItem jMenuItem5;
private javax.swing.JMenuItem jMenuItem6;
private javax.swing.JMenuItem jMenuItem7;
private javax.swing.JMenuItem jMenuItem8;
private javax.swing.JMenuItem jMenuItem9;
private javax.swing.JPanel jPanel1;
private javax.swing.JPanel jPanel10;
private javax.swing.JPanel jPanel2;
private javax.swing.JPanel jPanel3;
private javax.swing.JPanel jPanel4;
private javax.swing.JPanel jPanel5;
private javax.swing.JPanel jPanel6;
private javax.swing.JPanel jPanel7;
private javax.swing.JPanel jPanel8;
private javax.swing.JPanel jPanel9;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JScrollPane jScrollPane2;
private javax.swing.JScrollPane jScrollPane3;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JSplitPane jSplitPane1;
private javax.swing.JTable jTable1;
private javax.swing.JTable jTable2;
private javax.swing.JTree jTree1;
private org.jdesktop.beansbinding.BindingGroup bindingGroup;
// End of variables declaration//GEN-END:variables

}
