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

public class ManageFavoriteDialog extends javax.swing.JDialog {

private BrowserFrame frame;

public BrowserFrame getFrame() {
    return frame;
}

/**
 * 构造方法
 */
public ManageFavoriteDialog(java.awt.Frame parent, String title,
                            boolean modal) {
    super(parent, modal);
    frame = (BrowserFrame) parent;
    initComponents(); // 调用初始化界面的方法
    initFavoriteTree(); // 调用初始化树组件的方法
}

private void initComponents() {

    favoriteForderDialog1 = new FavoriteForderDialog(this, true);
    jLabel1 = new javax.swing.JLabel();
    jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel1.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/logo.jpg")));
    moveToButton = new javax.swing.JButton();
    renameButton = new javax.swing.JButton();
    createForderButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    favoriteTree = new javax.swing.JTree();
    delButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
    setTitle("整理收藏夹");

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setOpaque(true);

    moveToButton.setText("移动到...");
    moveToButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            moveToButtonActionPerformed(evt);
        }
    });

    renameButton.setText("重命名");
    renameButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            renameButtonActionPerformed(evt);
        }
    });

    createForderButton.setText("创建文件夹");
    createForderButton
            .addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    createForderButtonActionPerformed(evt);
                }
            });

    favoriteTree.setBorder(javax.swing.BorderFactory
                                   .createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jScrollPane1.setViewportView(favoriteTree);

    delButton.setText("删除");
    delButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            delButtonActionPerformed(evt);
        }
    });
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(createForderButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addComponent(renameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97,
                            Short.MAX_VALUE).addComponent(moveToButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addComponent(delButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)).addContainerGap()).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                    layout
                                            .createSequentialGroup()
                                            .addComponent(
                                                    createForderButton)
                                            .addPreferredGap(
                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(
                                                    renameButton)
                                            .addPreferredGap(
                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(
                                                    moveToButton)
                                            .addPreferredGap(
                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(
                                                    delButton))
                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)).addContainerGap()));

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width - 396) / 2, (screenSize.height - 352) / 2, 396, 352);
}

/**
 * 创建文件夹按钮的事件处理方法
 */
private void createForderButtonActionPerformed(
        java.awt.event.ActionEvent evt) {
    // 使用输入对话框接收用户输入的文件夹名称
    String name = JOptionPane.showInputDialog(this, "请输入新建文件夹名称");
    if (name != null) {
        MyMap newMap = new MyMap(name); // 创建新的集合对象
        // 获取树组件的选择路径
        TreePath selPath = favoriteTree.getSelectionPath();
        if (selPath != null) {
            Object[] paths = selPath.getPath();
            MyMap tempMap = frame.getFavorite();
            for (int i = 0; i < paths.length; i++) {
                Object object = paths[i];
                // 获取路径中的每一个节点
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
                // 获取节点对象中的数据
                Object nodeValue = node.getUserObject();
                // 获取最后的文件夹节点，然后在这个节点中创建新的文件夹节点
                if (nodeValue instanceof MyMap) {
                    MyMap pathMap = (MyMap) nodeValue;
                    tempMap = (MyMap) tempMap.get(pathMap.getName());
                    if (i == paths.length - 1) {
                        tempMap.put(name, newMap);
                    }
                } else if (nodeValue instanceof String) {
                    tempMap.put(name, newMap);
                }
            }
        } else {
            frame.getFavorite().put(name, newMap);
        }
        frame.storeFavorite(); // 保存收藏夹
        frame.reloadFavorite(); // 装载收藏夹
        initFavoriteTree(); // 初始化树组件
    }
}

/**
 * 重命名按钮的事件处理方法 *
 */
private void renameButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // 获取树组件的当前选择路径
    TreePath selPath = favoriteTree.getSelectionPath();
    // 获取选择路径的最后一个树节点
    DefaultMutableTreeNode last = (DefaultMutableTreeNode) selPath
                                                                   .getLastPathComponent();
    // 获取节点的数据
    Object userObject = last.getUserObject();
    // 接收用户输入的新名称
    String name = JOptionPane.showInputDialog(this, "请输入新名称", userObject);
    if (name == null)
        return;
    if (selPath != null) {
        Object[] paths = selPath.getPath();
        MyMap tempMap = frame.getFavorite();
        for (int i = 0; i < paths.length; i++) { // 遍历选择路径的所有节点
            Object object = paths[i];
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            Object nodeValue = node.getUserObject();
            if (nodeValue instanceof MyMap) { // 重命名文件夹
                if (i == paths.length - 1) {
                    String key = ((MyMap) nodeValue).getName();
                    Object value = tempMap.get(key);
                    tempMap.remove(key);
                    tempMap.put(name, value);
                } else {
                    MyMap pathMap = (MyMap) nodeValue;
                    tempMap = (MyMap) tempMap.get(pathMap.getName());
                }
            } else if (nodeValue instanceof String) { // 或者重命名收藏的信息
                if (nodeValue.equals(frame.getFavorite().getName())) {
                    continue;
                }
                Object value = tempMap.get(nodeValue);
                tempMap.remove(nodeValue);
                tempMap.put(name, value);
            }
        }
        frame.storeFavorite(); // 保存收藏夹数据
        frame.reloadFavorite(); // 重新装载收藏夹
        initFavoriteTree(); // 初始化树组件
    }
}

/**
 * 删除按钮的事件处理方法
 */
private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {
    TreePath selPath = favoriteTree.getSelectionPath(); // 获取树节点的选择路径
    if (selPath != null) {
        Object[] paths = selPath.getPath();
        MyMap tempMap = frame.getFavorite(); // 获取并遍历收藏夹数据模型
        for (int i = 0; i < paths.length; i++) {
            Object object = paths[i];
            // 获取选择路径的每个节点
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            Object nodeValue = node.getUserObject();
            if (nodeValue instanceof MyMap) { // 删除文件夹
                if (i == paths.length - 1) {
                    String key = ((MyMap) nodeValue).getName();
                    tempMap.remove(key);
                } else {
                    MyMap pathMap = (MyMap) nodeValue;
                    tempMap = (MyMap) tempMap.get(pathMap.getName());
                }
            } else if (nodeValue instanceof String) { // 删除收藏信息
                if (nodeValue.equals(frame.getFavorite().getName())) {
                    continue;
                }
                tempMap.remove(nodeValue);
            }
        }
        frame.storeFavorite(); // 保存收藏夹数据
        frame.reloadFavorite(); // 重新装载收藏夹
        initFavoriteTree(); // 初始化树组件
    }
}

/**
 * 移动按钮的事件处理方法
 */
private void moveToButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // 获取树节点的选择路径
    TreePath selPath = favoriteTree.getSelectionPath();
    if (selPath != null) {
        Object[] paths = selPath.getPath();
        // 获取收藏夹数据集合
        MyMap parentMap = frame.getFavorite();
        // 遍历收藏夹数据集合,获取要移动项的父节点
        for (int i = 0; i < paths.length - 1; i++) {
            Object object = paths[i];
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            Object nodeValue = node.getUserObject();
            if (nodeValue instanceof MyMap) {
                MyMap pathMap = (MyMap) nodeValue;
                parentMap = (MyMap) parentMap.get(pathMap.getName());
            } else if (nodeValue instanceof String) {
                if (nodeValue.equals(frame.getFavorite().getName())) {
                    continue;
                }
            }
        }
        // 实现节点的移动
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[paths.length - 1];
        Object item = node.getUserObject();
        if (item instanceof MyMap) {
            item = ((MyMap) item).getName();
        }
        Object value = parentMap.get(item);
        MyMap targetMap = showDialog(this, true);
        if (targetMap == null)
            return;
        targetMap.put(item, value);
        parentMap.remove(item);
        frame.storeFavorite();
        frame.reloadFavorite();
        dispose();
    }
}

/**
 * 显示文件夹列表，并返回选择的节点
 */
private MyMap showDialog(ManageFavoriteDialog parent, boolean modal) {
    FavoriteForderDialog dialog = new FavoriteForderDialog(parent, modal);
    dialog.setVisible(true);
    return dialog.getSelmap();
}

/**
 * 初始化收藏夹列表的方法 *
 */
private void initFavoriteTree() {
    // 创建树组件的根节点
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(frame
                                                                     .getFavorite().getName());
    // 获取树节点的数据模型对象
    dtm = (DefaultTreeModel) favoriteTree.getModel();
    dtm.setRoot(root); // 设置树组件的根节点
    loadFavoriteNode(frame.getFavorite(), root);// 递归添加树组件的节点
    dtm.reload(); // 重载树组件内容
}

/**
 * 加载树组件的收藏夹信息的方法 *
 */
private void loadFavoriteNode(MyMap map, DefaultMutableTreeNode root) {
    // 获取集合类的键名集合
    Set<String> items = map.keySet();
    for (String com : items) { // 遍历集合类
        Object value = map.get(com); // 获取键值
        if (value instanceof MyMap) { // 如果键值是集合对象
            MyMap nodeMap = (MyMap) value;
            nodeMap.setName(com);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeMap);
            root.add(node);
            loadFavoriteNode((MyMap) value, node); // 递归调用本方法装载子节点的内容
        } else if (value instanceof URL) { // 如果是收藏夹的网址信息
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(com);
            root.add(node); // 添加到树节点中
        }
    }
}

private DefaultTreeModel dtm;
// Variables declaration - do not modify
private javax.swing.JButton createForderButton;
private javax.swing.JButton delButton;
private FavoriteForderDialog favoriteForderDialog1;
private javax.swing.JTree favoriteTree;
private javax.swing.JLabel jLabel1;
private javax.swing.JScrollPane jScrollPane1;
private javax.swing.JButton moveToButton;
private javax.swing.JButton renameButton;
// End of variables declaration
}

