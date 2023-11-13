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
import java.net.URL;
import java.util.Set;

public class AddFavoriteDialog extends javax.swing.JDialog {

private BrowserFrame frame;

public AddFavoriteDialog(java.awt.Frame parent, String title, boolean modal) {
    super(parent, modal);
    frame = (BrowserFrame) parent;
    initComponents();
    nameTextField.setText(title);
    nameTextField.requestFocus();
    nameTextField.selectAll();
    initFavoriteTree();
}

private void initComponents() {
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    nameTextField = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    createForderButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    favoriteTree = new javax.swing.JTree();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("添加到收藏夹");

    jLabel1.setText("将添加当前页面到收藏夹中，请指定添加的名称。");

    jLabel2.setText("名称：");

    jLabel3.setText("创建到：");

    okButton.setText("确定");
    okButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonActionPerformed(evt);
        }
    });

    cancelButton.setText("取消");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelButtonActionPerformed(evt);
        }
    });

    createForderButton.setText("创建文件夹");
    createForderButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            createForderButtonActionPerformed(evt);
        }
    });

    favoriteTree.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jScrollPane1.setViewportView(favoriteTree);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(
                            jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(
                                    jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                                                                           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(nameTextField)).addGroup(layout
                                                                                                                                                                                                                                                                                                                                 .createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                            .addComponent(createForderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                            .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE).addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                            layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nameTextField,
                                            javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3))).addGroup(layout.createSequentialGroup()
                                                                                                                                                                                                                                                                                      .addGap(22, 22, 22).addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(createForderButton)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width - 397) / 2, (screenSize.height - 342) / 2, 397, 342);
}

/**
 * 创建收藏夹的文件夹的业务方法
 */
private void createForderButtonActionPerformed(
        java.awt.event.ActionEvent evt) {
    // 使用对话框接收用户输入新文件夹的名称
    String name = JOptionPane.showInputDialog(this, "请输入新建文件夹名称");
    if (name != null) {                            // 如果输入了文件夹名称
        MyMap newMap = new MyMap(name);            // 使用该名称创建一个集合对象
        // 获取树组件当前选择的路径，路径包括选择的节点和该节点的子节点
        TreePath selPath = favoriteTree.getSelectionPath();
        if (selPath != null) {                    // 如果路径不为空
            // 获取这个路径的最后一个节点。
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
                                                                           .getLastPathComponent();
            Object nodeValue = node.getUserObject();// 获取节点的数据
            if (nodeValue instanceof MyMap) {        // 如果该节点是MyMap类的实例
                MyMap selMap = (MyMap) nodeValue;
                selMap.put(name, newMap);            // 将新建的收藏夹添加到这个节点中
            } else {                                // 否则
                frame.getFavorite().put(name, newMap);// 把新建文件夹添加到收藏夹菜单
            }
        } else {
            frame.getFavorite().put(name, newMap);
        }
        frame.storeFavorite();                        // 执行收藏夹的保存
        frame.reloadFavorite();                        // 重新加载收藏夹
        initFavoriteTree();
    }
}

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
    dispose();
}

/**
 * 确定按钮的业务方法
 */
private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    String name = nameTextField.getText();            // 获取用户输入的名称标识
    URL url = frame.getSelBrowser().getURL();        // 创建网站地址的URL对象
    TreePath selPath = favoriteTree.getSelectionPath();// 获取选择的树节点路径
    if (selPath != null) {
        Object[] paths = selPath.getPath();            // 获取节点路径的数组
        MyMap tempMap = frame.getFavorite();        // 获取收藏夹的集合对象
        for (Object object : paths) {                // 遍历树节点路径的数组
            // 将数组内容转型为节点对象
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            Object nodeValue = node.getUserObject();// 获取节点对象的数据
            if (!(nodeValue instanceof MyMap)) {    // 如果数据不是文件夹
                continue;                            // 跳过本次遍历循环
            }
            MyMap pathMap = (MyMap) nodeValue;        // 将节点的数据转型为MyMap类型
            // 获取树组件中当前选择节点代表的集合对象
            tempMap = (MyMap) tempMap.get(pathMap.getName());
        }
        // 使用名称标识和网址URL做键值对，添加到选择的文件夹中
        tempMap.put(name, url);
    } else {                                        // 如果选择节点路径为空
        frame.getFavorite().put(name, url);            // 将键值对添加到树组件根节点中
    }
    frame.storeFavorite();                            // 调用保存收藏夹数据的方法
    frame.reloadFavorite();                            // 重新装载收藏夹
    dispose();
}

/**
 * 初始化收藏夹列表的方法
 */
private void initFavoriteTree() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(frame
                                                                     .getFavorite().getName());        // 创建树组件的根节点
    dtm = (DefaultTreeModel) favoriteTree.getModel();// 获取树组件的模型
    dtm.setRoot(root);                        // 设置模型的根节点
    loadMenu(frame.getFavorite(), root);    // 调用装载收藏夹内容的方法
    dtm.reload();
}

/**
 * 装载收藏夹内容的方法
 */
private void loadMenu(MyMap map, DefaultMutableTreeNode root) {
    Set<String> items = map.keySet();        // 获取数据Map集合的键名集合
    for (String com : items) {                // 遍历键名的集合
        Object value = map.get(com);        // 获取对应键名的值
        if (value instanceof MyMap) {        // 如果值是MyMap类的实例
            // 使用该值创建树组件的节点对象
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(value);
            root.add(node);                    // 将节点添加到根节点中
            loadMenu((MyMap) value, node);    // 递归执行子菜单的加载
        }
    }
}

private DefaultTreeModel dtm;
// Variables declaration - do not modify
private javax.swing.JButton cancelButton;
private javax.swing.JButton createForderButton;
private javax.swing.JTree favoriteTree;
private javax.swing.JLabel jLabel1;
private javax.swing.JLabel jLabel2;
private javax.swing.JLabel jLabel3;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JTextField nameTextField;
private javax.swing.JButton okButton;
// End of variables declaration
}

