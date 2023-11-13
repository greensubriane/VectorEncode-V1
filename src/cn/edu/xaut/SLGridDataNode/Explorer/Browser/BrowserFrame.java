/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.PlafView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class BrowserFrame extends javax.swing.JFrame {
private TabbedModel tabModel = new TabbedModel(this);
private URL homePage;
private MyMap favorite;
private javax.swing.JMenuItem addFavoriteMenuItem;
private javax.swing.JTextField address;
private javax.swing.JButton backButton;
private javax.swing.JMenuItem backMenuItem;
private javax.swing.JMenuItem bottomTabMenuItem;
private BrowserPanel broserPanel1;
private javax.swing.JTabbedPane browserTabbedPane;
private javax.swing.JMenu favoriteMenu;
private javax.swing.JMenu fileMenu;
private javax.swing.JButton forwardButton;
private javax.swing.JMenuItem forwardMenuItem;
private javax.swing.JButton goButton;
private javax.swing.JMenu goMenu;
private javax.swing.JButton homePageButton;
private javax.swing.JMenuItem homePageMenuItem;
private javax.swing.JLabel jLabel1;
private javax.swing.JMenuItem exitItem;
private javax.swing.JPanel jPanel1;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JToolBar jToolBar1;
private JPanel addressBar;
private javax.swing.JMenuItem leftTabMenuItem;
private javax.swing.JMenuBar menuBar;
private javax.swing.JButton printButton;
private javax.swing.JMenuItem printMenuItem;
private javax.swing.JButton refreshButton;
private javax.swing.JMenuItem refreshMenuItem;
private javax.swing.JMenuItem rightTabMenuItem;
private javax.swing.JMenuItem sortFavoriteMenuItem;
private javax.swing.JLabel statusLabel;
private javax.swing.JButton stopButton;
private javax.swing.JMenuItem stopMenuItem;
private javax.swing.JMenu tabMenu;
private javax.swing.JMenuItem topTabMenuItem;

/** Creates new form BrowserFrame */
public BrowserFrame() {
    try {
        initComponents();
        favorite = new MyMap(favoriteMenu.getText());
        homePage = new URL("http://localhost:8080/DataCenter/");
        reloadFavorite();
    } catch (Exception ex) {
        Logger.getLogger(BrowserFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
}

/**
 * 装载收藏夹内容的方法
 */
public void reloadFavorite() {
    // 创建序列化的文件对象
    File file = new File("data/favorite.data");
    if (file.exists()) { // 如果文件存在
        try {
            // 创建文件的输入流
            FileInputStream fis = new FileInputStream(file);
            // 使用对象输入流封装文件输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            favorite = (MyMap) ois.readObject(); // 读取收藏夹的集合对象
            homePage = (URL) ois.readObject(); // 读取主页URL对象
            ois.close(); // 关闭对象输入流
            fis.close(); // 关闭文件输入流
            addFavoriteItem(); // 添加收藏夹菜单
        } catch (Exception ex) {
            Logger.getLogger(BrowserFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/**
 * 保存收藏夹的方法
 */
public void storeFavorite() {
    try {
        // 创建序列化的文件对象
        File file = new File("data/favorite.data");
        if (!file.exists()) { // 如果文件不存在
            file.getParentFile().mkdirs();// 创建存放文件的文件夹
            file.createNewFile(); // 创建新的文件
        }
        // 获取文件输出流
        FileOutputStream fout = new FileOutputStream(file);
        // 使用对象输出流封装文件输出流
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(favorite);// 将收藏夹的集合对象序列化到文件中
        out.writeObject(homePage);// 将主页的URL对象序列化到文件中
        out.flush();
        out.close();// 关闭对象输出流
        fout.close();// 关闭文件输出流
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// 创建浏览器选项卡的方法
public void createBrowserTab(URL url) {
    // 创建浏览器面板对象
    final BrowserPanel browserPanel = new BrowserPanel(url, this);
    // 将浏览器面板添加到选项卡面板中
    browserTabbedPane.addTab("空", browserPanel);
    // 设置浏览器面板为选项卡面板当前被选择的组件
    browserTabbedPane.setSelectedComponent(browserPanel);
}

// 获取当前浏览器对象的方法
public Browser getSelBrowser() {
    // 获取选项卡面板当前选择的浏览器面板组件
    BrowserPanel selBrowserPanel = (BrowserPanel) browserTabbedPane.getSelectedComponent();
    // 获取浏览器面板组件中的浏览器组件
    Browser browser = selBrowserPanel.getBrowser();
    // 返回浏览器组件实例
    return browser;
}

private void initComponents() {
    jPanel1 = new javax.swing.JPanel();
    jToolBar1 = new javax.swing.JToolBar();
    jToolBar1.setFloatable(false);
    backButton = new javax.swing.JButton();
    backButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/backBtn.png")));
    forwardButton = new javax.swing.JButton();
    forwardButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/forwardBtn.png")));
    stopButton = new javax.swing.JButton();
    stopButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/stopBtn.png")));
    refreshButton = new javax.swing.JButton();
    refreshButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/refreshBtn.png")));
    homePageButton = new javax.swing.JButton();
    homePageButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/homePageBtn.png")));
    printButton = new javax.swing.JButton();
    printButton.setIcon(new ImageIcon(getClass().getResource("/cn/edu/xaut/SLGridDataNode/Explorer/Browser/res/printBtn.png")));
    addressBar = new JPanel();
    addressBar.setLayout(new BorderLayout());
    jLabel1 = new javax.swing.JLabel();
    address = new javax.swing.JTextField();
    address.addKeyListener(new AddressKeyListener());
    goButton = new javax.swing.JButton();
    browserTabbedPane = new javax.swing.JTabbedPane();
    broserPanel1 = new BrowserPanel(homePage, this);
    statusLabel = new javax.swing.JLabel();
    menuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    printMenuItem = new javax.swing.JMenuItem();
    final JMenuItem setHomePageItem = new JMenuItem();
    setHomePageItem.addActionListener(new SetHomePageItemActionListener());
    setHomePageItem.setText("设置主页");
    fileMenu.add(setHomePageItem);
    exitItem = new javax.swing.JMenuItem();
    goMenu = new javax.swing.JMenu();
    homePageMenuItem = new javax.swing.JMenuItem();
    forwardMenuItem = new javax.swing.JMenuItem();
    backMenuItem = new javax.swing.JMenuItem();
    refreshMenuItem = new javax.swing.JMenuItem();
    stopMenuItem = new javax.swing.JMenuItem();
    tabMenu = new javax.swing.JMenu();
    leftTabMenuItem = new javax.swing.JMenuItem();
    rightTabMenuItem = new javax.swing.JMenuItem();
    topTabMenuItem = new javax.swing.JMenuItem();
    bottomTabMenuItem = new javax.swing.JMenuItem();
    if (favoriteMenu == null)
        favoriteMenu = new javax.swing.JMenu();
    favoriteMenu.setText("收藏夹");
    favoriteMenu.getPopupMenu().setLightWeightPopupEnabled(false);
    addFavoriteMenuItem = new javax.swing.JMenuItem();
    sortFavoriteMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();

    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    addWindowListener(new ThisWindowListener());

    jPanel1.setLayout(new java.awt.GridLayout(0, 1));

    jToolBar1.setRollover(true);

    backButton.setText("后退");// 设置按钮文本
    backButton.setFocusable(false);
    backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    // 添加按钮的事件监听器，在单击后退按钮时执行业务方法
    backButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            backButtonActionPerformed(evt);        // 调用后退按钮的业务方法
        }
    });
    jToolBar1.add(backButton);                    // 添加后退按钮到工具栏

    forwardButton.setText("前进");                // 设置按钮文本
    forwardButton.setFocusable(false);
    forwardButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    forwardButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加按钮的事件监听器，当单击前进按钮时执行业务方法
    forwardButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            forwardButtonActionPerformed(evt);    // 调用前进按钮的业务方法
        }
    });
    jToolBar1.add(forwardButton);                // 添加前进按钮到工具栏

    stopButton.setText("停止");                    // 设置按钮文本
    stopButton.setFocusable(false);
    stopButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加按钮的事件监听器，当用户单击停止按钮时执行业务方法
    stopButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            stopButtonActionPerformed(evt);        // 调用按钮的业务方法
        }
    });
    jToolBar1.add(stopButton);                    // 添加停止按钮到工具栏

    refreshButton.setText("刷新");                // 设置按钮的文本
    refreshButton.setFocusable(false);
    refreshButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加按钮的事件监听器，当用户单击刷新按钮时执行业务方法
    refreshButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            refreshButtonActionPerformed(evt);    // 调用刷新按钮的业务方法
        }
    });
    jToolBar1.add(refreshButton);                // 添加刷新按钮到工具栏

    homePageButton.setText("主页");                // 设置按钮文本
    homePageButton.setFocusable(false);
    homePageButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    homePageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加按钮的事件监听器，在单击主页按钮时，执行业务方法
    homePageButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            homePageButtonActionPerformed(evt);    // 调用主页按钮的业务方法
        }
    });
    jToolBar1.add(homePageButton);                // 添加主页按钮到工具栏

    printButton.setText("打印");                    // 设置按钮文本
    printButton.setFocusable(false);
    printButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    printButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加按钮事件监听器，在单击打印按钮时执行业务方法
    printButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            printButtonActionPerformed(evt);    // 调用打印按钮的业务方法
        }
    });
    jToolBar1.add(printButton);                    // 添加打印按钮到工具栏

    jPanel1.add(jToolBar1);                        // 添加工具栏到窗体

    jLabel1.setText("地址：");                            // 地址标签
    addressBar.add(jLabel1, BorderLayout.WEST);            // 添加标签到地址栏
    address.setText("http://");                            // 设置地址文本框内容
    addressBar.add(address, BorderLayout.CENTER);        // 添加地址文本框到地址栏
    goButton.setText("转到");                            // 设置转到按的文本
    goButton.setFocusable(false);
    goButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    goButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    // 添加转到按钮的事件监听器，当单击转到按钮时，执行业务方法
    goButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            goButtonActionPerformed(evt);                // 调用转到按钮的业务方法
        }
    });
    addressBar.add(goButton, BorderLayout.EAST);        // 添加转到按钮到地址栏

    jPanel1.add(addressBar);

    getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

    browserTabbedPane.setModel(tabModel);
    browserTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
    browserTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            browserTabbedPaneMouseClicked(evt);
        }
    });
    browserTabbedPane.addTab("tab1", broserPanel1);
    browserTabbedPane.setSelectedComponent(broserPanel1);
    getContentPane().add(browserTabbedPane, java.awt.BorderLayout.CENTER);

    statusLabel.setText("状态：");
    statusLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    statusLabel.setPreferredSize(new java.awt.Dimension(36, 20));
    getContentPane().add(statusLabel, java.awt.BorderLayout.PAGE_END);

    fileMenu.setText("文件");
    fileMenu.getPopupMenu().setLightWeightPopupEnabled(false);

    printMenuItem.setText("打印");
    printMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            printMenuItemActionPerformed(evt);
        }
    });
    fileMenu.add(printMenuItem);

    exitItem.setText("退出");
    exitItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem2ActionPerformed(evt);
        }
    });
    fileMenu.add(exitItem);

    menuBar.add(fileMenu);

    goMenu.setText("转到");
    goMenu.getPopupMenu().setLightWeightPopupEnabled(false);

    homePageMenuItem.setText("主页");
    homePageMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            homePageMenuItemActionPerformed(evt);
        }
    });
    goMenu.add(homePageMenuItem);

    forwardMenuItem.setText("前进");
    forwardMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            forwardMenuItemActionPerformed(evt);
        }
    });
    goMenu.add(forwardMenuItem);

    backMenuItem.setText("后退");
    backMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            backMenuItemActionPerformed(evt);
        }
    });
    goMenu.add(backMenuItem);

    refreshMenuItem.setText("刷新");
    refreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            refreshMenuItemActionPerformed(evt);
        }
    });
    goMenu.add(refreshMenuItem);

    stopMenuItem.setText("停止");
    stopMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            stopMenuItemActionPerformed(evt);
        }
    });
    goMenu.add(stopMenuItem);

    menuBar.add(goMenu);

    tabMenu.setText("调整");
    tabMenu.getPopupMenu().setLightWeightPopupEnabled(false);

    leftTabMenuItem.setText("左侧选项卡");
    leftTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            leftTabMenuItemActionPerformed(evt);
        }
    });
    tabMenu.add(leftTabMenuItem);

    rightTabMenuItem.setText("右侧选项卡");
    rightTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rightTabMenuItemActionPerformed(evt);
        }
    });
    tabMenu.add(rightTabMenuItem);

    topTabMenuItem.setText("顶部选项卡");
    topTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            topTabMenuItemActionPerformed(evt);
        }
    });
    tabMenu.add(topTabMenuItem);

    bottomTabMenuItem.setText("底部选项卡");
    bottomTabMenuItem
            .addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    bottomTabMenuItemActionPerformed(evt);
                }
            });
    tabMenu.add(bottomTabMenuItem);

    menuBar.add(tabMenu);

    if (favoriteMenu.getMenuComponentCount() < 1) {
        addFavoriteMenuItem.setText("添加到收藏夹");
        addFavoriteMenuItem
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent evt) {
                        addFavoriteMenuItemActionPerformed(evt);
                    }
                });
        favoriteMenu.add(addFavoriteMenuItem);

        sortFavoriteMenuItem.setText("整理收藏夹");
        sortFavoriteMenuItem
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent evt) {
                        sortFavoriteMenuItemActionPerformed(evt);
                    }
                });
        favoriteMenu.add(sortFavoriteMenuItem);
        favoriteMenu.add(jSeparator1);

    }

    menuBar.add(favoriteMenu);

    setJMenuBar(menuBar);

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width - 585) / 2, (screenSize.height - 477) / 2, 585, 477);
}

// 后退按钮的事件处理方法
private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    if (browser.isBackEnabled()) { // 如果后退操作可行
        browser.back(); // 执行后退
    }
}

// 停止按钮的事件处理方法
private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    browser.stop(); // 执行停止操作
}

// 前进按钮的事件处理方法
private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    if (browser.isForwardEnabled()) { // 如果前进操作可行
        browser.forward(); // 执行前进操作
    }
}

// 刷新按钮的事件处理方法
private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    browser.refresh(); // 执行刷新操作
}

// 主页按钮的事件处理方法
private void homePageButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    browser.setURL(homePage); // 设置浏览器当前网址
}

// 打印按钮的事件处理方法
private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
    Browser browser = getSelBrowser();
    browser.print(); // 执行打印操作
}

// 转到按钮的事件处理方法
private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {
    try {
        String text = address.getText();        // 获取地址文本框的内容
        URL url = new URL(text);                // 创建URL对象
        createBrowserTab(url);                    // 调用创建浏览器选项卡的方法
    } catch (MalformedURLException ex) {
        Logger.getLogger(BrowserFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
}

// 主页菜单项的业务方法
private void homePageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    homePageButton.doClick(); // 执行主页按钮的单击方法
}

// 停止菜单项的业务方法
private void stopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    stopButton.doClick(); // 执行停止按钮的单击方法
}

// 刷新菜单项的业务方法
private void refreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    refreshButton.doClick(); // 执行刷新按钮的单击方法
}

// 后退菜单项的业务方法
private void backMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    backButton.doClick(); // 执行后退按钮的单击方法
}

// 前进菜单项的业务方法
private void forwardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    forwardButton.doClick(); // 执行前进按钮的单击方法
}

// 打印菜单项的业务方法
private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    printButton.doClick(); // 执行主页按钮的单击方法
}

// 退出菜单项的业务方法
private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
    System.exit(0);
}

// 处理浏览器选项卡的双击关闭事件
private void browserTabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {
    if (evt.getClickCount() >= 2) {
        final int index = browserTabbedPane.getSelectedIndex();
        if (browserTabbedPane.getTabCount() > 1) {
            browserTabbedPane.remove(index);
        }
    }
}

// 左侧对齐选项卡菜单项的方法
private void leftTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    browserTabbedPane.setTabPlacement(JTabbedPane.LEFT);
}

// 右侧对齐选项卡菜单项的方法
private void rightTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    browserTabbedPane.setTabPlacement(JTabbedPane.RIGHT);
}

// 顶部对齐选项卡菜单项的方法
private void topTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    browserTabbedPane.setTabPlacement(JTabbedPane.TOP);
}

// 底部对齐选项卡菜单项的方法
private void bottomTabMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    browserTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
}

/**
 * 添加到收藏夹菜单项的业务方法
 */
private void addFavoriteMenuItemActionPerformed(
        java.awt.event.ActionEvent evt) {
    // 获取选项卡当前选择的索引
    int index = browserTabbedPane.getSelectedIndex();
    // 获取当前选择的选项卡的标题文本
    String title = browserTabbedPane.getTitleAt(index);
    // 创建添加到收藏夹对话框，由该对话框处理业务
    new AddFavoriteDialog(this, title, true).setVisible(true);
}

private void addFavoriteItem() {
    if (favoriteMenu == null) {
        favoriteMenu = new javax.swing.JMenu();
    }
    int count = favoriteMenu.getMenuComponentCount();
    for (int i = count - 1; i > 2; i--) {
        favoriteMenu.remove(i);
    }
    listFavoriteMap(favorite, favoriteMenu);
}

// 暴露收藏夹集合的方法
public MyMap getFavorite() {
    return favorite;
}

// 遍历Map集合的方法，该方法将集合中的数据添加到收藏夹菜单中
private void listFavoriteMap(MyMap map, JMenu menu) {
    Set<String> keySet = map.keySet();
    final Iterator<String> iterator = keySet.iterator();
    while (iterator.hasNext()) {
        String key = iterator.next();
        final Object obj = map.get(key);
        if (obj instanceof URL) { // 遍历菜单项
            JMenuItem item = new JMenuItem(key);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createBrowserTab((URL) obj);
                }
            });
            menu.add(item);
        } else if (obj instanceof MyMap) { // 遍历子菜单
            JMenu subMenu = new JMenu(key);
            subMenu.getPopupMenu().setLightWeightPopupEnabled(false);
            listFavoriteMap((MyMap) obj, subMenu);
            menu.add(subMenu);
        }
    }
}

/**
 * 整理收藏夹菜单项的业务方法
 */
private void sortFavoriteMenuItemActionPerformed(
        java.awt.event.ActionEvent evt) {
    new ManageFavoriteDialog(this, "", rootPaneCheckingEnabled)
            .setVisible(true);                // 创建并显示整理收藏夹对话框
}

public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {

        @Override
        public void run() {
            try {
                //UIManager.setLookAndFeel(new NimbusLookAndFeel());
                new PlafView().ChangeView();
                BrowserFrame frame = new BrowserFrame();
                frame.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(BrowserFrame.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    });
}

public javax.swing.JTabbedPane getJTabbedPane1() {
    return browserTabbedPane;
}

public javax.swing.JButton getBackButton() {
    return backButton;
}

public javax.swing.JButton getForwardButton() {
    return forwardButton;
}

public javax.swing.JButton getStopButton() {
    return stopButton;
}

public void setStatus(final String info, final Icon icon) {
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            statusLabel.setText(info);
            statusLabel.setIcon(icon);
        }
    };
    if (SwingUtilities.isEventDispatchThread()) {
        runnable.run();
    } else {
        SwingUtilities.invokeLater(runnable);
    }
}

public javax.swing.JTextField getAddress() {
    return address;
}

public javax.swing.JMenu getFavoriteMenu() {
    return favoriteMenu;
}

/**
 * 设置主页菜单项的动作事件监听器
 */
private class SetHomePageItemActionListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent e) {
        String home = JOptionPane.showInputDialog(BrowserFrame.this,
                "请输入主页地址"); // 显示输入对话框
        if (home == null)
            return;
        try {
            homePage = new URL(home); // 创建URL对象
            storeFavorite(); // 序列化URL对象
        } catch (MalformedURLException e1) {
            // 如果抛出异常，提示URL的格式不合法
            JOptionPane.showMessageDialog(BrowserFrame.this, "输入的URL不合法 ");
            e1.printStackTrace();
        }
    }
}

private class ThisWindowListener extends WindowAdapter {
    @Override
    public void windowOpened(final WindowEvent e) {
        broserPanel1.browser.setURL(homePage);
    }
}

// 地址文本框的按键监听器
private class AddressKeyListener extends KeyAdapter {
    @Override
    public void keyPressed(final KeyEvent e) {
        char keyChar = e.getKeyChar();        // 获取按键字符
        if (keyChar == '\n') {                    // 如果是回车字符
            goButton.doClick();                // 执行转到按钮的单击
        }
    }
}
}

