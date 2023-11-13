package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.TabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;


class ConnectInfoPanel extends JPanel
        implements ActionListener, TreeSelectionListener, TreeExpansionListener, ItemListener {

private static final int IMPORT_ENTIRE_DB = 1;
private static final int IMPORT_SELECTED = 2;
private static final int IMPORT_HEURISTIC = 3;

private static final int ODBC = 1;
private static final int JDBC = 2;

private static int nDSType = JDBC;

private static int columnType = Global.COLUMN_TYPE_INSTANCE;

private static boolean bPrefixedColumnNames = false;

private static ImageIcon helpIcon = ComponentUtilities.loadImageIcon(ConnectInfoPanel.class, "resource/help.gif");

private JRadioButton optJDBC;
private JRadioButton optODBC;

private JRadioButton optCurrOnt;
private JRadioButton optSepOnt;

private JCheckBox chbxSelectAll;
private JCheckBox tableCheckBox, viewCheckBox;

private Vector<TreeNodeInfo> tables;

private String strDefaultODBCDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
private String strDefaultMySQLDriver = "com.mysql.jdbc.Driver";
private String strDefaultPostGreSQLDriver = "org.postgresql.Driver";


private String LABEL_ODBC_DRIVER = "ODBC 驱动";
private String LABEL_JDBC_DRIVER = "JDBC 驱动";
private String LABEL_ODBC_DS = "数据库名称";
private String LABEL_JDBC_DS = "数据库名称";
private String LABEL_ODBC_DS_PREFIX = "jdbc:odbc:";
private String LABEL_JDBC_DS_PREFIX = "";
private JLabel driverLabel = new JLabel(LABEL_JDBC_DRIVER, JLabel.LEFT);
private JLabel dsLabel = new JLabel(LABEL_JDBC_DS, JLabel.LEFT);
private JLabel dsPrefixLabel = new JLabel(LABEL_JDBC_DS_PREFIX, JLabel.LEFT);
//private JButton btnDsHelp = new JButton(helpIcon);
private JComboBox cmbDSN;
private JComboBox cmbDriver;
private Collection<String> loadedDrivers = new ArrayList<String>();
private JTextField txtUserID = new JTextField(20);
private JPasswordField txtPW = new JPasswordField(20);
private JButton btnConnect = new JButton("连接");
private JButton btnInfo = new JButton("数据库信息");
private SuperClsPicker superClsPicker;
private JTextField txtNamePrefix = new JTextField(20);


private boolean connected = false;

private Connection conn = null;

private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TreeNodeInfo("Data Source", TreeNodeInfo.OTHER));
private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
private JTree treeTables = new JTree(treeModel);

private JButton btnImportCls = new JButton("生成本体类及属性");
private JButton btnImportInstance = new JButton("导入数据表内容");
private JButton btnCreateSubClsSelfKey = new JButton("设定自相关字段");
private JButton btnCreateSubClsKey = new JButton("设定分类字段");
private JButton btnSavefile = new JButton("保存文件");
private DataPreviewPanel previewPanel;
private KnowledgeBase kb;
private OWLModel owlModel = null;

private DataMaster dm;


/**
 * Constructor
 */
public ConnectInfoPanel(DataPreviewPanel previewPanel, KnowledgeBase kb) {

    // retrieve the list of stored data source names
    Vector<String> driverList = new Vector<String>();
    Vector<String> dsnList = new Vector<String>();
    boolean readingDriverList = true;
    try {
        File file = new File(Global.tabName);
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while (line != null) {
                if (line.equals(Global.SEPARATOR_DRIVER_DSN))
                    readingDriverList = false;
                else if (readingDriverList)
                    driverList.addElement(line);
                else
                    dsnList.addElement(line);
                line = reader.readLine();
            }

            reader.close();
            fileReader.close();
        } else {
            Global.debug(Global.tabName + " file does not exist.");
        }
    } catch (NullPointerException ex) {
        ex.printStackTrace();
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    }

    if (!driverList.contains(strDefaultODBCDriver)) {
        driverList.add(strDefaultODBCDriver);
    }
    if (!driverList.contains(strDefaultMySQLDriver)) {
        driverList.add(strDefaultMySQLDriver);
    }
    if (!driverList.contains(strDefaultPostGreSQLDriver)) {
        driverList.add(strDefaultPostGreSQLDriver);
    }

    // initialize
    this.previewPanel = previewPanel;
    this.kb = kb;
    if (kb instanceof OWLModel)
        this.owlModel = (OWLModel) kb;

    Global.init(owlModel);

    tables = new Vector<TreeNodeInfo>();

    // add UI components
    this.setLayout(new BorderLayout());

    //JSplitPane verticalSplitPane = ComponentFactory.createTopBottomSplitPane(true);
    //JSplitPane verticalSplitPane = ComponentFactory.createTopBottomSplitPane(false);
    JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
    verticalSplitPane.setBorder(null);

    // NORTH
    //JSplitPane pNorth = ComponentFactory.createLeftRightSplitPane(true);
    //JSplitPane pNorthEast = ComponentFactory.createLeftRightSplitPane(true);
    //JSplitPane pNorth = ComponentFactory.createLeftRightSplitPane(false);
    //JSplitPane pNorthEast = ComponentFactory.createLeftRightSplitPane(false);
    JSplitPane pNorth = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    JSplitPane pNorthEast = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    pNorth.setBorder(null);
    pNorthEast.setBorder(null);

    GridBagLayout layConnect = new GridBagLayout();
    GridBagConstraints lcConnLabel = new GridBagConstraints();
    GridBagConstraints lcConnComp = new GridBagConstraints();
    JPanel pConnect = new JPanel(layConnect);
    JPanel pClasses = new JPanel(new BorderLayout());
    GridBagLayout layOptions = new GridBagLayout();
    GridBagConstraints lcOpt = new GridBagConstraints();
    JPanel pOptions = new JPanel(layOptions);

    //vertical panels for labels and input components respectively
    lcConnLabel.anchor = GridBagConstraints.NORTHWEST;
    lcConnLabel.fill = GridBagConstraints.VERTICAL;
    lcConnLabel.insets = new Insets(Global.VERTICAL_INSET, 0, 0, Global.HORIZ_INSET / 2);
    lcConnComp.anchor = GridBagConstraints.NORTHWEST;
    lcConnComp.fill = GridBagConstraints.HORIZONTAL;
    lcConnComp.insets = new Insets(Global.VERTICAL_INSET, 0, 0, 0);
    lcConnComp.gridwidth = GridBagConstraints.REMAINDER;

    FlowLayout flc = new FlowLayout(FlowLayout.CENTER);
    flc.setHgap(2 * Global.HORIZ_INSET);
    JPanel p0 = new JPanel(flc);
    JLabel labelTemp = ComponentFactory.createLabel("数据源类型", JLabel.LEFT);
    ComponentUtilities.setSmallLabelFont(labelTemp);
    layConnect.setConstraints(labelTemp, lcConnLabel);
    pConnect.add(labelTemp);
    optODBC = new JRadioButton("ODBC", false);
    optODBC.setMnemonic('O');
    optODBC.setActionCommand("optODBC");
    optODBC.addActionListener(this);
    optJDBC = new JRadioButton("JDBC", true);
    optJDBC.setMnemonic('J');
    optJDBC.setActionCommand("optJDBC");
    optJDBC.addActionListener(this);
    ButtonGroup dataGroup = new ButtonGroup();
    dataGroup.add(optODBC);
    dataGroup.add(optJDBC);
    p0.add(optODBC);
    p0.add(optJDBC);
    layConnect.setConstraints(p0, lcConnComp);
    pConnect.add(p0);

    //vertical panels for labels having help button and help buttons respectively
    GridBagConstraints lcConnLabelWithHelp = new GridBagConstraints();
    lcConnLabelWithHelp.anchor = GridBagConstraints.NORTHWEST;
    lcConnLabelWithHelp.fill = GridBagConstraints.BOTH;
    lcConnLabelWithHelp.weightx = 1;
    GridBagConstraints lcConnLabelHelp = new GridBagConstraints();
    lcConnLabelHelp.anchor = GridBagConstraints.NORTHEAST;
    lcConnLabelHelp.fill = GridBagConstraints.NONE;
    lcConnLabelHelp.weightx = 0;
    lcConnLabelHelp.gridwidth = GridBagConstraints.REMAINDER;

    ComponentUtilities.setSmallLabelFont(driverLabel);
//        btnDriverHelp.setBorderPainted(false);
//        bindKeyStrokeToActionForButton("ENTER", "driverHelp_pressed", btnDriverHelp);
//	  btnDriverHelp.addActionListener(this);
    JPanel pDriverLabelWithHelp = new JPanel(new GridBagLayout());
    pDriverLabelWithHelp.setPreferredSize(labelTemp.getPreferredSize());
    pDriverLabelWithHelp.add(driverLabel, lcConnLabelWithHelp);
    //      pDriverLabelWithHelp.add(btnDriverHelp, lcConnLabelHelp);
    layConnect.setConstraints(pDriverLabelWithHelp, lcConnLabel);
    pConnect.add(pDriverLabelWithHelp);
    cmbDriver = new JComboBox(driverList);
    cmbDriver.setEditable(true);
    cmbDriver.setRenderer(new DriverListRenderer());
    layConnect.setConstraints(cmbDriver, lcConnComp);
    pConnect.add(cmbDriver);


    ComponentUtilities.setSmallLabelFont(dsLabel);
    JPanel pDsLabelWithHelp = new JPanel(new GridBagLayout());
    pDsLabelWithHelp.setPreferredSize(labelTemp.getPreferredSize());
    pDsLabelWithHelp.add(dsLabel, lcConnLabelWithHelp);
    layConnect.setConstraints(pDsLabelWithHelp, lcConnLabel);
    pConnect.add(pDsLabelWithHelp);
    cmbDSN = new JComboBox(dsnList);
    cmbDSN.setEditable(true);
    lcConnComp.gridwidth = GridBagConstraints.RELATIVE;
    GridBagConstraints lcDSPrefixLabel = (GridBagConstraints) lcConnComp.clone();
    lcDSPrefixLabel.insets.top += 3;
    layConnect.setConstraints(dsPrefixLabel, lcDSPrefixLabel);
    pConnect.add(dsPrefixLabel);
    lcConnComp.gridwidth = GridBagConstraints.REMAINDER;
    layConnect.setConstraints(cmbDSN, lcConnComp);
    pConnect.add(cmbDSN);

    updateSelectedComboListItem();

    labelTemp = ComponentFactory.createLabel("用户名", JLabel.LEFT);
    ComponentUtilities.setSmallLabelFont(labelTemp);
    layConnect.setConstraints(labelTemp, lcConnLabel);
    pConnect.add(labelTemp);
    layConnect.setConstraints(txtUserID, lcConnComp);
    pConnect.add(txtUserID);

    labelTemp = ComponentFactory.createLabel("密码", JLabel.LEFT);
    ComponentUtilities.setSmallLabelFont(labelTemp);
    layConnect.setConstraints(labelTemp, lcConnLabel);
    pConnect.add(labelTemp);
    layConnect.setConstraints(txtPW, lcConnComp);
    pConnect.add(txtPW);

    JPanel p5 = new JPanel(flc);
    btnConnect.setMnemonic('n');
    bindKeyStrokeToActionForButton("ENTER", "connect_pressed", btnConnect);
    btnConnect.addActionListener(this);
    p5.add(btnConnect);
    btnInfo.setMnemonic('f');
    bindKeyStrokeToActionForButton("ENTER", "info_pressed", btnInfo);
    btnInfo.addActionListener(this);
    p5.add(btnInfo);
    //dummy label. Introduced in order to layout the connect and info buttons under the textfield and combobox components
    pConnect.add(new JLabel(""));
    lcConnComp.insets = new Insets(Global.VERTICAL_INSET, 0 * Global.HORIZ_INSET, Global.VERTICAL_INSET, 0 * Global.HORIZ_INSET);
    layConnect.setConstraints(p5, lcConnComp);
    pConnect.add(p5);

    Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
    Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
    newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    optODBC.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
    optJDBC.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
    cmbDriver.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
    cmbDSN.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
    txtUserID.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
    txtPW.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);

//      **********************************************

    addRefreshedSuperClsPickerTo(pClasses);

//      **********************************************

    JPanel p21 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p21.add(new JLabel("Name prefix for the table classes:", JLabel.LEFT));
    p21.add(txtNamePrefix);
    //TODO Prefix and suffix options can be added here
    //pOptions.add(p21);


    GridBagLayout layP31 = new GridBagLayout();
    GridBagConstraints lcP31 = new GridBagConstraints();
    JPanel p31 = new JPanel(layP31);
    JLabel labelDestination = ComponentFactory.createLabel("导入类型: ");
    ComponentUtilities.setSmallLabelFont(labelDestination);
    optCurrOnt = new JRadioButton("导入到当前本体", false);
    optCurrOnt.setMnemonic('u');
    optCurrOnt.setActionCommand("optCurrOnt");
    optCurrOnt.addActionListener(this);
    optSepOnt = new JRadioButton("导入到独立本体", true);
    optSepOnt.setMnemonic('s');
    optSepOnt.setActionCommand("optSepOnt");
    optSepOnt.addActionListener(this);
    ButtonGroup ontoGroup = new ButtonGroup();
    ontoGroup.add(optCurrOnt);
    ontoGroup.add(optSepOnt);
    lcP31.anchor = GridBagConstraints.NORTHWEST;
    lcP31.fill = GridBagConstraints.NONE;
    lcP31.gridwidth = GridBagConstraints.REMAINDER;
    layP31.setConstraints(labelDestination, lcP31);
    p31.add(labelDestination);
    lcP31.insets = new Insets(0, Global.HORIZ_INSET, 0, 0);
    layP31.setConstraints(optCurrOnt, lcP31);
    p31.add(optCurrOnt);
    lcP31.insets = new Insets(0, 2 * Global.HORIZ_INSET, 0, 0);
    lcP31.insets = new Insets(0, Global.HORIZ_INSET, 0, 0);
    layP31.setConstraints(optSepOnt, lcP31);
    p31.add(optSepOnt);

    //disable the options CURRENTLY not available for Protege Frames
    if (owlModel == null) {
        optSepOnt.setEnabled(false);
    }

    lcOpt.anchor = GridBagConstraints.NORTHWEST;
    lcOpt.fill = GridBagConstraints.NONE;
    lcOpt.gridwidth = GridBagConstraints.REMAINDER;
    lcOpt.gridheight = 4;
    lcOpt.weighty = 1.0;
    layOptions.setConstraints(p31, lcOpt);
    pOptions.add(p31);
    lcOpt.weighty = 0.0;    //reset to default
    lcOpt.insets = new Insets(Global.VERTICAL_INSET, 0, 0, 0);
    //disable the options not available for Protege Frames
    if (owlModel != null) {
        lcOpt.gridheight = 1;
        lcOpt.gridwidth = GridBagConstraints.REMAINDER;

    }


    lcOpt.gridheight = 1;
    lcOpt.gridwidth = GridBagConstraints.REMAINDER;

    GridBagLayout layP34 = new GridBagLayout();
    GridBagConstraints lcP34 = new GridBagConstraints();
    JPanel p34 = new JPanel(layP34);
    //TODO change this hack: simulate a label of a LabeledComponent


    ButtonGroup colTypeGroup = new ButtonGroup();

    lcP34.anchor = GridBagConstraints.NORTHWEST;
    lcP34.fill = GridBagConstraints.NONE;
    lcP34.gridwidth = GridBagConstraints.REMAINDER;
    lcP34.insets = new Insets(0, Global.HORIZ_INSET, 0, 0);

    //disable the options not available for Protege Frames
    if (owlModel == null) {
        p34.setVisible(false);
    }

    lcOpt.anchor = GridBagConstraints.NORTHWEST;
    lcOpt.fill = GridBagConstraints.NONE;
    lcOpt.gridwidth = GridBagConstraints.REMAINDER;
    lcOpt.gridheight = 4;
    lcOpt.weighty = 1.0;
    layOptions.setConstraints(p34, lcOpt);
    pOptions.add(p34);


    lcOpt.gridheight = 1;
    lcOpt.gridwidth = GridBagConstraints.REMAINDER;
    lcOpt.gridheight = GridBagConstraints.REMAINDER;


    JScrollPane pScrollConn = new JScrollPane(pConnect);
    pScrollConn.setBorder(null);
    //pScrollConn.setPreferredSize(new Dimension(350, 300));
    JScrollPane pScrollOpt = new JScrollPane(pOptions);
    pScrollOpt.setBorder(null);
    int prefHeight = 0;
    for (Component c : pOptions.getComponents()) {
        prefHeight += c.getPreferredSize().getHeight();
    }
    pScrollOpt.setPreferredSize(new Dimension(pScrollOpt.getPreferredSize().width, prefHeight));
    //pScrollOpt.setPreferredSize(new Dimension(350, 300));

//		**********************************************

    pNorth.setLeftComponent(pScrollConn);
    pNorthEast.setLeftComponent(pClasses);
    pNorthEast.setRightComponent(pScrollOpt);
    pNorth.setRightComponent(pNorthEast);

    verticalSplitPane.setTopComponent(pNorth);

    JPanel pBottom = new JPanel(new BorderLayout());

    // CENTER
    JPanel p6 = new JPanel(new BorderLayout());
    JPanel tableTypesPanel = new JPanel(new FlowLayout());
    tableCheckBox = new JCheckBox("表格                  ");
    tableCheckBox.setMnemonic('t');
    tableCheckBox.addItemListener(this);
    tableTypesPanel.add(tableCheckBox);
    viewCheckBox = new JCheckBox("视图                    ");
    viewCheckBox.setMnemonic('V');
    viewCheckBox.addItemListener(this);
    tableTypesPanel.add(viewCheckBox);
    p6.add(new LabeledComponent("数据表", tableTypesPanel, false), BorderLayout.NORTH);
    treeTables.setCellRenderer(new MyTreeCellRenderer());
    treeTables.setEditable(false);
    treeTables.setRootVisible(false);
    treeTables.setShowsRootHandles(true);
    treeTables.putClientProperty("JTree.lineStyle", "Angled");
    treeTables.addTreeSelectionListener(this);
    treeTables.addTreeExpansionListener(this);
    JScrollPane scrollPane = new JScrollPane(treeTables);
    //scrollPane.setPreferredSize(new Dimension(350, 300));
    p6.add(scrollPane, BorderLayout.CENTER);

    //pBottom.add(p6,BorderLayout.CENTER);

    JScrollPane pScrollPreview = new JScrollPane(previewPanel);

    JSplitPane pSouthSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, p6, pScrollPreview);
    pSouthSplit.setBorder(null);

    pBottom.add(pSouthSplit, BorderLayout.CENTER);

    // SOUTH
    JPanel pSouth = new JPanel(new BorderLayout());


    chbxSelectAll = new JCheckBox("选择所有表格");
    chbxSelectAll.setMnemonic('a');
    chbxSelectAll.addItemListener(this);
    p6.add(chbxSelectAll, BorderLayout.SOUTH);

    JPanel p7 = new JPanel();
    btnImportCls.setMnemonic('m');
    bindKeyStrokeToActionForButton("ENTER", "import_pressed", btnImportCls);
    btnImportCls.setEnabled(false);
    btnImportCls.addActionListener(this);
    Font currFont = btnImportCls.getFont();
    btnImportCls.setFont(new Font(currFont.getFontName(), Font.BOLD, currFont.getSize()));
    p7.add(btnImportCls);

    //btnImportInstance.setEnabled(false);
    p7.add(btnImportInstance);
    bindKeyStrokeToActionForButton("ENTER", "importinstance_pressed", btnImportInstance);
    btnImportInstance.addActionListener(this);
    currFont = btnImportInstance.getFont();
    btnImportInstance.setFont(new Font(currFont.getFontName(), Font.BOLD, currFont.getSize()));


    //btnCreateSubClsSelfKey.setEnabled(false);
    p7.add(btnCreateSubClsSelfKey);
    bindKeyStrokeToActionForButton("ENTER", "CreateSubClsSelf_pressed", btnCreateSubClsSelfKey);
    btnCreateSubClsSelfKey.addActionListener(this);
    currFont = btnCreateSubClsSelfKey.getFont();
    btnCreateSubClsSelfKey.setFont(new Font(currFont.getFontName(), Font.BOLD, currFont.getSize()));


    //btnCreateSubClsKey.setEnabled(false);
    p7.add(btnCreateSubClsKey);
    bindKeyStrokeToActionForButton("ENTER", "CreateSubCls_pressed", btnCreateSubClsKey);
    btnCreateSubClsKey.addActionListener(this);
    currFont = btnCreateSubClsKey.getFont();
    btnCreateSubClsKey.setFont(new Font(currFont.getFontName(), Font.BOLD, currFont.getSize()));

    btnSavefile.setEnabled(false);
    p7.add(btnSavefile);
    bindKeyStrokeToActionForButton("ENTER", "Savefile_pressed", btnSavefile);
    btnSavefile.addActionListener(this);
    currFont = btnSavefile.getFont();
    btnSavefile.setFont(new Font(currFont.getFontName(), Font.BOLD, currFont.getSize()));

    pSouth.add(p7, BorderLayout.SOUTH);

    pBottom.add(pSouth, BorderLayout.SOUTH);

    verticalSplitPane.setBottomComponent(pBottom);

    assureMinimumSize(pScrollConn, 300, 200);
    assureMinimumSize(pClasses, 100, 200);
    assureMinimumSize(pScrollOpt, 250, 200);
    pBottom.setMinimumSize(new Dimension(200, 200));

    assureMinimumSizeForSplitPanes(pNorthEast);
    assureMinimumSizeForSplitPanes(pNorth);
    assureMinimumSizeForSplitPanes(verticalSplitPane);

    assureMinimumPreferredSize(pScrollConn, 440, 250);
    assureMinimumPreferredSize(pClasses, 300, 300);
    assureMinimumPreferredSize(pScrollOpt, 400, 300);
    pBottom.setPreferredSize(new Dimension(800, 300));


    pNorthEast.setResizeWeight(0.5);
    pNorth.setResizeWeight(0.33);
    verticalSplitPane.setResizeWeight(0.0);

    verticalSplitPane.setDividerLocation(0.5);
    verticalSplitPane.revalidate();
    verticalSplitPane.resetToPreferredSizes();
    pNorth.resetToPreferredSizes();
    pNorthEast.resetToPreferredSizes();

    this.add(verticalSplitPane);
    this.revalidate();

    connected = false;
    setConnectedGUI(false);

    //if the last selected driver in the driverList seems to be an ODBC driver select the ODBC driver radio button
    String selDriver = ((String) cmbDriver.getSelectedItem());
    for (String s : selDriver.split("\\.")) {
        if (s.toLowerCase().startsWith("odbc")) {
            optODBC.doClick();
            break;
        }
    }

    updateLoadedDriverList();
}

private void addRefreshedSuperClsPickerTo(Container c) {
    c.removeAll();
    superClsPicker = new SuperClsPicker(kb);
    LabeledComponent p22 = new LabeledComponent("本体浏览:", superClsPicker, true);
    c.add(p22, BorderLayout.CENTER);
}

private void bindKeyStrokeToActionForButton(String keyName, String actionName, final JButton button) {
    button.getInputMap().put(KeyStroke.getKeyStroke(keyName), actionName);
    button.getActionMap().put(actionName, new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            button.doClick();
        }
    });
}


private int max(int a, int b) {
    return (a > b ? a : b);
}

private void assureMinimumSizeForSplitPanes(JSplitPane splitPane) {
    Dimension minDim1 = splitPane.getLeftComponent().getMinimumSize();
    Dimension minDim2 = splitPane.getRightComponent().getMinimumSize();
    int divWidth = splitPane.getDividerSize();

    Dimension newMinDim;
    if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
        newMinDim = new Dimension(minDim1.width + divWidth + minDim2.width, max(minDim1.height, minDim2.height));
    } else {
        newMinDim = new Dimension(max(minDim1.width, minDim2.width), minDim1.height + divWidth + minDim2.height);
    }

    splitPane.setMinimumSize(newMinDim);
}


private void assureMinimumSize(JComponent comp, int width, int height) {
    Dimension currDim = comp.getMinimumSize();
    comp.setMinimumSize(new Dimension(max(currDim.width, width), max(currDim.height, height)));
}

private void assureMinimumPreferredSize(JComponent comp, int width, int height) {
    Dimension currDim = comp.getPreferredSize();
    comp.setPreferredSize(new Dimension(max(currDim.width, width), max(currDim.height, height)));
}


public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == btnConnect && btnConnect.getText().equalsIgnoreCase("连接")) {
        // reset data and GUI components
        previewPanel.deleteTable();
        tables.removeAllElements();
        rootNode = new DefaultMutableTreeNode(new TreeNodeInfo("Data Source", TreeNodeInfo.OTHER));
        treeModel = new DefaultTreeModel(rootNode);
        treeTables.setModel(treeModel);
        setConnectedGUI(false);

        try {
            conn = null;

            String s = (String) cmbDriver.getSelectedItem();
            if (s.trim() != s) {
                s = s.trim();
                cmbDriver.setSelectedItem(s);
            }

            s = (String) cmbDSN.getSelectedItem();
            if (s.trim() != s) {
                s = s.trim();
                cmbDSN.setSelectedItem(s);
            }
            if (s.endsWith("/") || s.endsWith("\\")) {
                cmbDSN.setSelectedItem(s.substring(0, s.length() - 1));
            }

            dm = new DataMaster(this, kb, new DataMasterConnectionOptions(this));

            try {
                dm.connect();
                conn = dm.getConnection();
            } catch (DataMasterException ex) {
                Global.defaultDataMasterExceptionHandler(JOptionPane.getFrameForComponent(this), ex);
            }

            String[] tableTypes = dm.getAllAvailableTableTypes();
            populateTableTree(tableTypes);

            // Save this data source to the data source list
            updateComboListItems(cmbDSN);
            updateComboListItems(cmbDriver);

            int i = 0;

            // Set the table type check boxes according to the types
            // available in the data source
            if (tables.size() == 0)
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), "No table found in this data source.", Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            else {
                setConnectedGUI(true);
                connected = false;

                tableCheckBox.setEnabled(false);
                viewCheckBox.setEnabled(false);

                for (i = 0; i < tableTypes.length; i++) {
                    if (tableTypes[i].compareToIgnoreCase("SYSTEM TABLE") == 0) {

                    } else if (tableTypes[i].compareToIgnoreCase("TABLE") == 0) {
                        tableCheckBox.setEnabled(true);
                        tableCheckBox.setSelected(true);
                    } else if (tableTypes[i].compareToIgnoreCase("VIEW") == 0) {
                        viewCheckBox.setEnabled(true);
                        viewCheckBox.setSelected(true);
                    }
                }

                previewPanel.setEnabled(true);

                if (chbxSelectAll.isSelected()) {
                    selectAllDBTables();
                }

                connected = true;

                updateConnectionPanelGUI();
            }
        } catch (SQLException ex) {
            //if access denied keep the Driver and DSN info in the combo box,
            //because they are valid. Only the user and/or password are wrong
            if (ex.getSQLState() != null &&
                        (ex.getSQLState().equals("S0001") ||
                                 ex.getSQLState().equals("28000") || ex.getSQLState().equals("42000"))) {
                updateComboListItems(cmbDSN);
                updateComboListItems(cmbDriver);
            } else {
                if (!(ex.getMessage() != null && ex.getMessage().equalsIgnoreCase("no suitable driver"))) {
                    cmbDSN.removeItem(cmbDSN.getSelectedItem());
                }
            }
            Global.defaultSQLExceptionHandler(JOptionPane.getFrameForComponent(this), ex);
        } catch (ClassNotFoundException cnfe) {
            cmbDriver.removeItem(cmbDriver.getSelectedItem());
            Global.defaultClassNotFoundExceptionHandler(JOptionPane.getFrameForComponent(this), cnfe);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            updateLoadedDriverList();
        }
    } else if (source == btnConnect && btnConnect.getText().equalsIgnoreCase("断开")) {
        try {
            dm.disconnect();
            conn = null;
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (DataMasterException e2) {
            e2.printStackTrace();
        }
        connected = false;
        setConnectedGUI(false);
        updateConnectionPanelGUI();
        previewPanel.deleteTable();
        previewPanel.setEnabled(false);
        tables.removeAllElements();
        rootNode = new DefaultMutableTreeNode(new TreeNodeInfo("Data Source", TreeNodeInfo.OTHER));
        treeModel = new DefaultTreeModel(rootNode);
        treeTables.setModel(treeModel);
    } else if (source == btnInfo) {
        DataSourceInfoDialog dlg = new DataSourceInfoDialog(
                JOptionPane.getFrameForComponent(this), conn);
        dlg.setLocationRelativeTo(JOptionPane.getFrameForComponent(this));
        dlg.showDialog();
    } else if (source == btnImportCls) {
        dm.setImportOptions(new DataMasterImportOptions(this));
        TreePath[] selection = treeTables.getSelectionPaths();
        if (getSelectedTableCount(selection) != 0) {
            Collection<String> tableNames = getSelectedTableNames(selection);
            String strMsg = "";
//					boolean eventsEnabled = kb.setDispatchEventsEnabled(false);
            try {
                dm.importTablesToProtegeOWL(tableNames);
                strMsg = "本体生成成功";
                btnImportInstance.setEnabled(true);
                btnCreateSubClsSelfKey.setEnabled(true);
                btnCreateSubClsKey.setEnabled(true);
                btnSavefile.setEnabled(true);
            } catch (DataMasterException ex1) {
                Global.defaultDataMasterExceptionHandler(JOptionPane.getFrameForComponent(this), ex1);
                strMsg = "Problems occured during the import!\nPlease check the console for more information.";
            } finally {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            String msg = "Please select the tables to import.\nNo table is selected.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        }
    } else if (source == btnImportInstance) {
        TreePath[] selection = treeTables.getSelectionPaths();
        if (getSelectedTableCount(selection) != 0) {
            Collection<String> tableNames = getSelectedTableNames(selection);
            String strMsg = "";
            try {
                int rows = previewPanel.getrows();
                if (rows < 0) {
                    dm.importTablesContent(tableNames);
                    strMsg = "生成实例成功!";
                }
                if (rows == 0) {
                    dm.importTablesContent(tableNames, 50);
                    strMsg = "生成实例成功!";
                }

                if (rows <= 300 && rows > 0) {
                    dm.importTablesContent(tableNames, rows);
                    strMsg = "生成实例成功!";
                }
                if (rows > 300) {
                    dm.importTablesContent(tableNames, 300);
                    strMsg = "生成实例成功!";
                }
            } catch (DataMasterException ex1) {
                Global.defaultDataMasterExceptionHandler(JOptionPane.getFrameForComponent(this), ex1);
                strMsg = "Problems occured during the import!\nPlease check the console for more information.";
            } finally {
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            String msg = "Please select the tables to import.\nNo table is selected.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        }
    } else if (source == btnCreateSubClsSelfKey) {
        TreePath[] selection = treeTables.getSelectionPaths();
        if (getSelectedTableCount(selection) == 1) {
            Collection<String> tableNames = getSelectedTableNames(selection);
            try {
                int rows = previewPanel.getrows();

                if (rows < 0) {
                    dm.CreatSelfSubClsForTable(tableNames, 500);
                }
                if (rows == 0) {
                    dm.CreatSelfSubClsForTable(tableNames, 50);
                }

                if (rows <= 300 && rows > 0) {
                    dm.CreatSelfSubClsForTable(tableNames, rows);
                }
                if (rows > 300) {
                    dm.CreatSelfSubClsForTable(tableNames, 300);
                }
            } catch (DataMasterException ex1) {
                Global.defaultDataMasterExceptionHandler(JOptionPane.getFrameForComponent(this), ex1);
                String strMsg = "子类生成错误";
            }
        } else if ((getSelectedTableCount(selection) == 0)) {
            String msg = "请选择一个表格\n没有选择表格.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "只能选择一个表格\n您选择了多个表格.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        }
    } else if (source == btnCreateSubClsKey) {
        TreePath[] selection = treeTables.getSelectionPaths();
        if (getSelectedTableCount(selection) != 0) {
            Collection<String> tableNames = getSelectedTableNames(selection);
            try {
                int rows = previewPanel.getrows();
                if (rows < 0) {
                    dm.CreatSubClsForTable(tableNames, 500);
                }
                if (rows == 0) {
                    dm.CreatSubClsForTable(tableNames, 50);
                }

                if (rows <= 300 && rows > 0) {
                    dm.CreatSubClsForTable(tableNames, rows);
                }
                if (rows > 300) {
                    dm.CreatSubClsForTable(tableNames, 300);
                }
            } catch (DataMasterException ex1) {
                Global.defaultDataMasterExceptionHandler(JOptionPane.getFrameForComponent(this), ex1);
                String strMsg = "子类生成错误";
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            }
        } else if ((getSelectedTableCount(selection) == 0)) {
            String msg = "请选择一个表格\n没有选择表格.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        } else {
            String msg = "只能选择一个表格\n您选择了多个表格.";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), msg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        }
    } else if (source == btnSavefile) {
        try {
            boolean save = dm.savefile();
            if (save) {
                String strMsg = "文件保存成功";
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            } else {
                String strMsg = "文件保存出错";
                JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex1) {
            String strMsg = "文件保存出错";
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), strMsg, Global.tabName, JOptionPane.INFORMATION_MESSAGE);

        }
    }


    String strActionCmd = e.getActionCommand();
    if (strActionCmd == "optEntireDB") {
        selectAllDBTables();
    } else if (strActionCmd == "optHeuristic") {
        selectAllDBTables();
    } else if (strActionCmd == "optODBC") {
        nDSType = ODBC;
        driverLabel.setText(LABEL_ODBC_DRIVER);
        dsLabel.setText(LABEL_ODBC_DS);
        Dimension d1 = dsPrefixLabel.getPreferredSize();
        dsPrefixLabel.setText(LABEL_ODBC_DS_PREFIX);
        Dimension d2 = dsPrefixLabel.getPreferredSize();
        Dimension dOld = cmbDSN.getPreferredSize();
        Dimension dNew = substractWidth(dOld, substractWidth(d2, d1));
        cmbDSN.setPreferredSize(dNew);
        updateSelectedComboListItem();
        //cmbDriver.setEditable(false);
        txtPW.setText("");
    } else if (strActionCmd == "optJDBC") {
        nDSType = JDBC;
        driverLabel.setText(LABEL_JDBC_DRIVER);
        dsLabel.setText(LABEL_JDBC_DS);
        dsPrefixLabel.setText(LABEL_JDBC_DS_PREFIX);
        cmbDSN.setPreferredSize(null);
        updateSelectedComboListItem();
        txtPW.setText("");
    } else if (strActionCmd == "optPropRange") {
        //case txtDummy = "1";
        columnType = Global.COLUMN_TYPE_RANGE;
    } else if (strActionCmd == "optPropHasXSDType") {
        //case txtDummy = "3";
        columnType = Global.COLUMN_TYPE_XSD;
    } else if (strActionCmd == "optPropHasColumnType") {
        //case txtDummy = "4";
        columnType = Global.COLUMN_TYPE_INSTANCE;
    }
}

private Dimension substractWidth(Dimension d1, Dimension d2) {
    return new Dimension(d1.width - d2.width, d1.height);
}


Collection<String> getSelectedTableNames(TreePath[] selection) {
    Vector<String> result = new Vector<String>();

    for (int i = 0; i < selection.length; i++) {
        if (!isSelectedPathATable(selection[i]))
            continue;

        // Get the table to insert into the Protege project
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selection[i].getLastPathComponent();
        TreeNodeInfo nodeInfo = (TreeNodeInfo) node.getUserObject();
        String strTableName = nodeInfo.m_text;

        result.add(strTableName);
    }

    return result;
}

private void populateTableTree(String[] tableTypes) throws SQLException {
    dm.populateTables(tableTypes);
    for (TreeNodeInfo nodeInfo : dm.getTableTreeNodeInfos()) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeInfo);
        treeModel.insertNodeInto(childNode, rootNode, rootNode.getChildCount());
        treeTables.scrollPathToVisible(new TreePath(childNode.getPath()));

        DefaultMutableTreeNode dummyChildNode = new DefaultMutableTreeNode(new TreeNodeInfo("DUMMY", TreeNodeInfo.OTHER));
        treeModel.insertNodeInto(dummyChildNode, childNode, childNode.getChildCount());
//			populateTableColumns(dbMetaData, childNode);

        tables.add(nodeInfo);
    }

    treeTables.scrollRectToVisible(new Rectangle(new Point(0, 0)));
}

private void populateTableColumns(DatabaseMetaData dbMetaData, MutableTreeNode tableTreeNode) throws SQLException {
    TreeNodeInfo tableNodeInfo = (TreeNodeInfo) ((DefaultMutableTreeNode) tableTreeNode).getUserObject();
    if (tableNodeInfo.m_columnsCreated)
        return;

    String strTableName = tableNodeInfo.m_text;

    setKeyInfo(dbMetaData, strTableName, tableNodeInfo);


    //delete dummy node
    if (tableTreeNode.getChildCount() > 0)
        treeModel.removeNodeFromParent((MutableTreeNode) tableTreeNode.getChildAt(0));

    //add column nodes
    ResultSet rsColumns = dbMetaData.getColumns(null, null, strTableName, null);
    boolean bMore2 = rsColumns.next();
    while (bMore2) {
        String strColumnName = rsColumns.getString("COLUMN_NAME");
        String strTypeName = rsColumns.getString("TYPE_NAME");
        int sqlType = rsColumns.getShort("DATA_TYPE");
        //ValueType protegeType = Global.getSQLProtegeType(sqlType);
        String protegeTypeText;
        if (owlModel == null)
            protegeTypeText = Global.getSQLProtegeType(sqlType).toString();
        else
            protegeTypeText = Global.getSQLProtegeOWLType(owlModel, sqlType).getName();
        String nodeText = strColumnName + " (" + strTypeName + " --> " + protegeTypeText + ")";

        int colType = TreeNodeInfo.COLUMN;
        // Determine whether this column is a foreign key
    }

    bMore2 = rsColumns.next();

}

private void setKeyInfo(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo tableNodeInfo) {
    if (!tableNodeInfo.m_infoComplete) {

        tableNodeInfo.m_infoComplete = true;
    }
}

// A new table has been selected from the tree and we want to display
// a preview of it in the preview table
public void valueChanged(TreeSelectionEvent e) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeTables.getLastSelectedPathComponent();
    if (node == null)
        return;

    /* */
    TreeNodeInfo nodeInfo = (TreeNodeInfo) node.getUserObject();
    String strTableName = Global.getDBMSSpecificNamingStyle(nodeInfo.m_text);

    previewPanel.updateTable(conn, strTableName);
    /* */

    //actualize the radio buttons
    int selTableCount = getSelectedTableCount(treeTables.getSelectionPaths());

    //if (selTableCount == treeModel.getChildCount(treeModel.getRoot())) {
    if (selTableCount == tables.size()) {
        chbxSelectAll.setSelected(true);
    } else {
        chbxSelectAll.setSelected(false);
    }
}

private void updateSelectedComboListItem() {
    if (nDSType == ODBC) {
        String selItem = null;
        //SET SELECTED ITEM FOR cmbDriver
        //if selected item looks to be a valid Driver name do nothing
        if (cmbDriver.getSelectedItem() != null
                    &&
                    ((String) cmbDriver.getSelectedItem()).toLowerCase().contains("odbc")) {
        } else {
            //otherwise search for a valid Driver name
            int n = cmbDriver.getItemCount();
            String currItem = "";
            int i;
            //first search for "odbc" separated by "."
            for (i = 0; i < n; i++) {
                currItem = (String) cmbDriver.getItemAt(i);
                for (String s : currItem.split("\\.")) {
                    if (s.equalsIgnoreCase("odbc")) {
                        selItem = currItem;
                        break;
                    }
                }
                if (selItem != null) {
                    break;
                }
            }
            //if no "odbc" separated by "." were found
            if (selItem == null) {
                //search for "odbc" in any context
                for (i = 0; i < n; i++) {
                    currItem = (String) cmbDriver.getItemAt(i);
                    if (currItem.toLowerCase().contains("odbc")) {
                        selItem = currItem;
                        break;
                    }
                }
            }
            //if no "odbc" found, set the item to be selected to "" - to be edited by the user
            if (selItem == null) {
                selItem = "";

            }

            cmbDriver.setSelectedItem(selItem);
        }

        //SET SELECTED ITEM FOR cmbDSN
        //if selected item looks to be a valid Data Source Name do nothing
        if (cmbDSN.getSelectedItem() != null
                    &&
                    (!((String) cmbDSN.getSelectedItem()).equals(""))
                    &&
                    (!((String) cmbDSN.getSelectedItem()).contains(":"))) {
        } else {
            //otherwise search for a valid DSN
            selItem = null;
            int n = cmbDSN.getItemCount();
            String currItem = "";
            int i;
            for (i = 0; i < n; i++) {
                currItem = (String) cmbDSN.getItemAt(i);
                if ((!currItem.equals(""))
                            &&
                            (!currItem.contains(":"))) {
                    selItem = currItem;
                    break;
                }
            }

            //if no valid DSN found, set the item to be selected to "" - to be edited by the user
            if (selItem == null) {
                selItem = "";

            }

            cmbDSN.setSelectedItem(selItem);
        }
    } else if (nDSType == JDBC) {
        String selItem = null;
        //SET SELECTED ITEM FOR cmbDriver
        //if selected item looks to be a valid Driver name do nothing
        if (cmbDriver.getSelectedItem() != null
                    &&
                    (((String) cmbDriver.getSelectedItem()).toLowerCase().contains("jdbc"))
                    &&
                    (!((String) cmbDriver.getSelectedItem()).toLowerCase().contains("odbc"))) {
        } else {
            //otherwise search for a valid Driver name
            int n = cmbDriver.getItemCount();
            String currItem = "";
            int i;

            //search for item not containing "odbc" in any context
            for (i = 0; i < n; i++) {
                currItem = (String) cmbDriver.getItemAt(i);
                if ((currItem.toLowerCase().contains("jdbc"))
                            &&
                            (!currItem.toLowerCase().contains("odbc"))) {
                    selItem = currItem;
                    break;
                }
            }

            //if no item not containing "odbc" found, set the item to be selected to "" - to be edited by the user
            if (selItem == null) {
                selItem = "";

            }

            cmbDriver.setSelectedItem(selItem);
        }

        //SET SELECTED ITEM FOR cmbDSN
        //if selected item looks to be a valid Data Source Name do nothing
        if (cmbDSN.getSelectedItem() != null
                    &&
                    ((String) cmbDSN.getSelectedItem()).contains(":")) {
        } else {
            //otherwise search for a valid DSN
            selItem = null;
            int n = cmbDSN.getItemCount();
            String currItem = "";
            int i;
            for (i = 0; i < n; i++) {
                currItem = (String) cmbDSN.getItemAt(i);
                if (currItem.contains(":")) {
                    selItem = currItem;
                    break;
                }
            }

            //if no valid DSN found, set the item to be selected to "" - to be edited by the user
            if (selItem == null) {
                selItem = "";

            }

            cmbDSN.setSelectedItem(selItem);
        }
    } else
        assert false : "Invalid value for nDSType";
}

/**
 * Save edited comboitem to combolist
 */
private void updateComboListItems(JComboBox cmb) {
    String selectedItemText = (String) cmb.getSelectedItem();
    int nCount = cmb.getItemCount();
    ArrayList<String> items = new ArrayList<String>(nCount + 1);
    //add selected item text as first
    items.add(selectedItemText);
    int i = 0;
    while (i < nCount && items.size() < Global.COMBO_HISTORY_LIMIT) {
        String str = (String) cmb.getItemAt(i);
        if (!str.equalsIgnoreCase(selectedItemText)) {
            items.add(str);
        }
        i++;
    }

    cmb.removeAllItems();
    for (String str : items) {
        cmb.addItem(str);
    }

}

public void itemStateChanged(ItemEvent e) {
    Object item = e.getItem();
    if (item == chbxSelectAll) {
        if (chbxSelectAll.isSelected()) {
            selectAllDBTables();
        }
        //return, because we don't want to reload the table tree
        return;
    }

    if (connected) {
        Vector<String> selectTypes = new Vector<String>();
        if (tableCheckBox.isSelected())
            selectTypes.addElement("TABLE");
        if (viewCheckBox.isSelected())
            selectTypes.addElement("VIEW");

        int numTypes = selectTypes.size();
        String[] selectTableTypes = new String[numTypes];
        for (int i = 0; i < numTypes; i++)
            selectTableTypes[i] = selectTypes.get(i);

        selectTableTypesForView(selectTableTypes);
        if (chbxSelectAll.isSelected()) {
            selectAllDBTables();
        }
    }
}

// Take a list of table types and modify the tree to show all the
// tables of those types.

private void selectTableTypesForView(String[] tableTypes) {
    // reset data and GUI components
    previewPanel.deleteTable();
    tables.removeAllElements();
    rootNode = new DefaultMutableTreeNode(new TreeNodeInfo("Data Source", TreeNodeInfo.OTHER));
    treeModel = new DefaultTreeModel(rootNode);
    treeTables.setModel(treeModel);
    if (tableTypes.length == 0) {
        return;
    }

    try {
        populateTableTree(tableTypes);

        if (tables.size() == 0) {
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), "No table of the selected types above found.", Global.tabName, JOptionPane.INFORMATION_MESSAGE);
        }
				/*
			else
				setConnectedGUI(true);
				*/
    } catch (SQLException ex) {
        Global.defaultSQLExceptionHandler(JOptionPane.getFrameForComponent(this), ex);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


private ArrayList<String> getPrimaryKeys(DatabaseMetaData dbMetaData, String strTableName) {
    ArrayList<String> primaryKeys = new ArrayList<String>();

    ResultSet rs;
    try {
        rs = dbMetaData.getPrimaryKeys(null, null, strTableName);

        boolean isPKDefined = rs.isBeforeFirst();
        if (isPKDefined) {
            Vector<String> colNamesInPK = new Vector<String>();
            while (rs.next()) {
                int keySeq = rs.getShort("KEY_SEQ");
                if (keySeq >= colNamesInPK.size()) {
                    for (int i = colNamesInPK.size(); i <= keySeq; i++)
                        colNamesInPK.add(null);
                }
                colNamesInPK.setElementAt(rs.getString("COLUMN_NAME"), keySeq);
                Global.debug(colNamesInPK.get(keySeq) + " " + keySeq);
            }

            //treat special case if we had it
            if (colNamesInPK.get(0) == null) {
                //normal case
                colNamesInPK.remove(0);
            } else {
                Global.debug("PRIMARY KEY COLUMN WITH KEY_SEQ = 0 WAS SPECIFIED. COLUMN = " + colNamesInPK.get(0));
            }

            primaryKeys = new ArrayList<String>(colNamesInPK);
        }
			/*
			more = rs.next();
			while (more) {
				primaryKeys.add(rs.getString("COLUMN_NAME"));
				more = rs.next();
			}*/
        rs.close();
    } catch (SQLException sqlex) {
        Global.debug("QUERYING A TABLE FOR PRIMARY KEYS IS NOT SUPPORTED BY THIS DRIVER (" + sqlex.getMessage() + ")");
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return primaryKeys;
}


// This method is used to enable/disable the GUI components based
// on the current state of the tab widget

private void setConnectedGUI(boolean isConnected) {
    connected = isConnected;
    tableCheckBox.setEnabled(isConnected);
    viewCheckBox.setEnabled(isConnected);
    if (!isConnected) {
        tableCheckBox.setSelected(isConnected);
        viewCheckBox.setSelected(isConnected);
    }

    btnInfo.setEnabled(isConnected);
    chbxSelectAll.setEnabled(isConnected);
    btnImportCls.setEnabled(isConnected);
}

private void updateConnectionPanelGUI() {
    if (connected)
        btnConnect.setText("断开");
    else
        btnConnect.setText("连接");

    optODBC.setEnabled(!connected);
    optJDBC.setEnabled(!connected);

    cmbDriver.setEnabled(!connected);
    cmbDSN.setEnabled(!connected);
    //if connected make the combo boxes ineditable and without popup menu if you can (nicer GUI, nicer screendump)
//        cmbDSN.setEditable( ! connected);
//        cmbDSN.setComponentPopupMenu(null);

    txtUserID.setEnabled(!connected);
    txtPW.setEnabled(!connected);
//        txtUserID.setEditable( ! connected);
//        txtPW.setEditable( ! connected);

}


// Select all the tables in the tree.  These are the top-level
// items in the tree.

private void selectAllDBTables() {
    TreePath paths[] = new TreePath[rootNode.getChildCount()];
    Enumeration enumrtn = rootNode.children();
    int i = 0;
    while (enumrtn.hasMoreElements()) {
        TreeNode path[] = treeModel.getPathToRoot((TreeNode) enumrtn.nextElement());
        paths[i] = new TreePath(path);
        i++;
    }
    treeTables.setSelectionPaths(paths);
}


private int getSelectedTableCount(TreePath[] selection) {
    if (selection == null)
        return 0;

    int selTableCount = 0;
    for (TreePath p : selection) {
        if (isSelectedPathATable(p))
            selTableCount++;
    }

    return selTableCount;
}

private boolean isSelectedPathATable(final TreePath p) {
    return (p.getPathCount() == 2);
}


public void treeCollapsed(TreeExpansionEvent arg0) {
}


public void treeExpanded(TreeExpansionEvent event) {
    TreePath expPath = event.getPath();
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) expPath.getLastPathComponent();
    if (node == null)
        return;

    if (!isSelectedPathATable(new TreePath(node.getPath()))) {
        return;
    }

    try {
        DatabaseMetaData dbMetaData = conn.getMetaData();

        populateTableColumns(dbMetaData, node);

        treeTables.expandPath(expPath);
    } catch (SQLException ex1) {
        //do we want to do here something?
    }
}


// Return the list of data source names
public Vector<String> getDSNList() {
    Vector<String> dsnList = new Vector<String>();
    int count = cmbDSN.getItemCount();
    for (int i = 0; i < count; i++)
        dsnList.addElement((String) cmbDSN.getItemAt(i));

    return dsnList;
}

// Return the list of driver names
public Vector<String> getDriversList() {
    Vector<String> driverList = new Vector<String>();
    int count = cmbDriver.getItemCount();
    for (int i = 0; i < count; i++)
        driverList.addElement((String) cmbDriver.getItemAt(i));

    return driverList;
}

// Class data
KnowledgeBase getKB() {
    return this.kb;
}


public String getDriverName() {
    return (String) cmbDriver.getSelectedItem();
}

public String getDataSourceURL() {
    return dsPrefixLabel.getText() + ((String) cmbDSN.getSelectedItem());
}

//do not make it public or private
String getUserName() {
    return txtUserID.getText();
}

//do not make it public or private
String getUserPWD() {
    return new String(txtPW.getPassword());
}


public Collection<Cls> getSuperClasses() {
    return superClsPicker.getSelection();
}


public boolean importInCurrOntology() {
    return optCurrOnt.isSelected();
}

public boolean importInSepOntology() {
    return optSepOnt.isSelected();
}

public String getTableClassNamePrefix() {
    return txtNamePrefix.getText();
}

public String getTableClassNameSuffix() {
    return "";
}

public String getColumnPropertyNamePrefix() {
    return "";
}

public String getColumnPropertyNameSuffix() {
    return "";
}


public int getColumnType() {
    return columnType;
}


public String getSchemaName(String dsn) {
    if (nDSType == ODBC) {
        assert dsn.toLowerCase().startsWith(LABEL_ODBC_DS_PREFIX) : "nDSType ODBC does not match the expected DSN syntax: " + dsn;
    }

    String strSchemaName = "";

    //split dsn in 3, by the first 2 ":"
    String[] dsnTokens = dsn.split(":", 3);
    if (dsnTokens.length > 1) {
        //set dsn
        dsn = dsnTokens[dsnTokens.length - 1];
    }

    if (nDSType == ODBC) {
        dsnTokens = dsn.split(";");
        //strSchema is the part of the subname
        strSchemaName = dsnTokens[0];
    } else if (nDSType == JDBC) {
        strSchemaName = dsn;
        int pos = strSchemaName.indexOf("//");
        if (pos > -1) {
            //set to the substring after "//"
            strSchemaName = strSchemaName.substring(pos + "//".length());
        }
        pos = strSchemaName.indexOf("/");
        if (pos > -1) {
            //set to the substring after "/"
            strSchemaName = strSchemaName.substring(pos + "/".length());
        }
    } else {
        assert false : "Invalid value for nDSType";
    }

    return strSchemaName;
}


public void shutdown() {
    if (dm != null && dm.isConnected()) {
        try {
            dm.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataMasterException e) {
            e.printStackTrace();
        }
    }
}

private class DriverListRenderer extends JLabel implements ListCellRenderer {
    private Font loadedFont, unloadedFont;

    public DriverListRenderer() {
        super();
        loadedFont = getFont().deriveFont(Font.BOLD);
        unloadedFont = getFont().deriveFont(Font.PLAIN);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        this.setText(value.toString());
        if (loadedDrivers.contains(value)) {
            this.setFont(loadedFont);
        } else {
            this.setFont(unloadedFont);
        }

        this.setOpaque(true);
        this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

        return this;
    }
}


private void updateLoadedDriverList() {
    Set<String> actuallyLoadedDriverNames = new HashSet<String>();
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
        actuallyLoadedDriverNames.add(drivers.nextElement().getClass().getName());
    }

    //recreate the loadedDriver list
    loadedDrivers.clear();
    for (int i = 0; i < cmbDriver.getItemCount(); i++) {
        String driverName = (String) cmbDriver.getItemAt(i);
        if (actuallyLoadedDriverNames.contains(driverName)) {
            loadedDrivers.add(driverName);
        }
    }

    //if there are actually loaded drivers which were not present in the ComboList cmbDriver
    //add them to the combo list, so that the user will be informed about all the loaded Drivers
    if (!loadedDrivers.containsAll(actuallyLoadedDriverNames)) {
        Vector<String> missingLoadedDriverNames = new Vector<String>(actuallyLoadedDriverNames);
        missingLoadedDriverNames.removeAll(loadedDrivers);
        for (String missingDriver : missingLoadedDriverNames) {
            cmbDriver.addItem(missingDriver);
            loadedDrivers.add(missingDriver);
        }
    }
}

void selectiveReload() {
    ProjectView prjView = ProjectManager.getProjectManager().getCurrentProjectView();
    assert this.getParent() instanceof TabWidget;
    TabWidget dataMasterTab = (TabWidget) this.getParent();
    prjView.reloadAllTabsExcept(dataMasterTab);
    prjView.reloadAll();
    addRefreshedSuperClsPickerTo(superClsPicker.getParent().getParent());
}
}


