/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.Set;

public class FavoriteForderDialog extends javax.swing.JDialog {

private BrowserFrame frame;
private DefaultTreeModel dtm;
private MyMap selmap;

public FavoriteForderDialog() {
    super();
}

public FavoriteForderDialog(ManageFavoriteDialog parent, boolean modal) {
    super(parent, modal);
    frame = parent.getFrame();
    initComponents();
    initFavoriteTree();
}

private void initComponents() {
    jScrollPane1 = new javax.swing.JScrollPane();
    favoriteTree = new javax.swing.JTree();
    jLabel1 = new javax.swing.JLabel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    favoriteTree.setBorder(javax.swing.BorderFactory
                                   .createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jScrollPane1.setViewportView(favoriteTree);
    jLabel1.setText("单击目标文件夹");
    okButton.setText("确定");
    okButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonActionPerformed(evt);
        }
    });
    cancelButton.setText("取消");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
            getContentPane());
    layout.setHorizontalGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.TRAILING)
                                      .addGroup(
                                              layout.createSequentialGroup().addContainerGap(253,
                                                              Short.MAX_VALUE).addComponent(okButton)
                                                      .addPreferredGap(
                                                              LayoutStyle.ComponentPlacement.RELATED)
                                                      .addComponent(cancelButton).addGap(22, 22, 22))
                                      .addGroup(
                                              layout.createSequentialGroup().addGap(10, 10, 10)
                                                      .addComponent(jScrollPane1,
                                                              GroupLayout.DEFAULT_SIZE, 379,
                                                              Short.MAX_VALUE).addContainerGap())
                                      .addGroup(
                                              layout.createSequentialGroup().addContainerGap()
                                                      .addComponent(jLabel1).addContainerGap(298,
                                                              Short.MAX_VALUE)));
    layout.setVerticalGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.TRAILING)
                                    .addGroup(
                                            layout.createSequentialGroup().addComponent(jLabel1,
                                                            GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                                    .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jScrollPane1,
                                                            GroupLayout.PREFERRED_SIZE, 175,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            layout.createParallelGroup(
                                                                            GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(cancelButton)
                                                                    .addComponent(okButton))
                                                    .addContainerGap()));
    getContentPane().setLayout(layout);
    pack();

}

/* 取消按钮的事件处理方法 */
private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
    dispose();
}

/** 确定按钮的事件处理方法 */
private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    TreePath selPath = favoriteTree.getSelectionPath();
    if (selPath != null) {
        Object[] paths = selPath.getPath();
        MyMap tempMap = frame.getFavorite();
        for (Object object : paths) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            Object nodeValue = node.getUserObject();
            if (!(nodeValue instanceof MyMap)) {
                continue;
            }
            MyMap pathMap = (MyMap) nodeValue;
            tempMap = (MyMap) tempMap.get(pathMap.getName());
        }
        selmap = tempMap;
        dispose();
    }
}

/* 初始化收藏夹列表的方法 */
private void initFavoriteTree() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(frame
                                                                     .getFavorite().getName());
    dtm = (DefaultTreeModel) favoriteTree.getModel();
    dtm.setRoot(root);
    loadMenu(frame.getFavorite(), root);
    dtm.reload();
}

/* 遍历收藏夹的方法 */
private void loadMenu(MyMap map, DefaultMutableTreeNode root) {
    Set<String> items = map.keySet();
    for (String com : items) {
        Object value = map.get(com);
        if (value instanceof MyMap) {
            MyMap nodeMap = (MyMap) value;
            nodeMap.setName(com);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                    nodeMap);
            root.add(node);
            loadMenu((MyMap) value, node);
        }
    }
}

// Variables declaration - do not modify
private javax.swing.JButton cancelButton;
private javax.swing.JTree favoriteTree;
private javax.swing.JLabel jLabel1;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JButton okButton;

// End of variables declaration

public MyMap getSelmap() {
    return selmap;
}
}

