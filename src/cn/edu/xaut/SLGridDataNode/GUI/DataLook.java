/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author He Ting
 * @author heting
 */
/*
 * Created by JFormDesigner on Mon Sep 26 21:53:56 CEST 2011
 */
/**
 * @author heting
 */

import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import cn.edu.xaut.SLGridDataNode.dao.Dao;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;
import com.jgoodies.uif_lite.component.UIFSplitPane;
import twaver.Node;
import twaver.TDataBox;
import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;
import twaver.tree.ElementNode;
import twaver.tree.TTree;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;
/*
 * Created by JFormDesigner on Mon Sep 26 21:53:56 CEST 2011
 */


/**
 * @author heting
 */
public class DataLook extends JFrame {
List table, db, table1, db1, structurecolumns, structurerows, informationcolumns, informationrows;
ResultSet rs, rs1, rs2;
private TDataBox box = new TDataBox();

public DataLook() {
    initComponents();
}

private void StorageActionPerformed(ActionEvent e) {
    // TODO add your code here
    new XMLFileStorageGUI().setVisible(true);
}

private void ExitActionPerformed(ActionEvent e) {
    // TODO add your code here
}

private void ElementActionPerformed(ActionEvent e) {
    // TODO add your code here
    new ElementInformation().setVisible(true);
}

private void AttributeActionPerformed(ActionEvent e) {
    // TODO add your code here
    new AttributeInformation().setVisible(true);
}

private void TextActionPerformed(ActionEvent e) {
    // TODO add your code here
    new TextInformation().setVisible(true);
}

private void ViewActionPerformed(ActionEvent e) {
    // TODO add your code here
}

private void ExtractActionPerformed(ActionEvent e) {
    //TODO add your code here
    new ExportData().setVisible(true);

}

private void DeleteActionPerformed(ActionEvent e) {
    // TODO add your code here
    new DelData().setVisible(true);
}

private void AboutActionPerformed(ActionEvent e) {
    // TODO add your code here
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    panel1 = new JPanel();
    DataMenuBar = new JMenuBar();
    DataStorageMenu = new JMenu();
    StorageMenuItem = new JMenuItem();
    ExitMenuItem = new JMenuItem();
    DataInformationMenu = new JMenu();
    ElementMenuItem = new JMenuItem();
    AttributeMenuItem = new JMenuItem();
    TextMenuItem = new JMenuItem();
    ViewMenuItem6 = new JMenuItem();
    DataExtractionMenu = new JMenu();
    ExtractMenuItem = new JMenuItem();
    DeleteMenuItem = new JMenuItem();
    AboutMenu = new JMenu();
    AboutDataPlatformMenuItem = new JMenuItem();
    DataLookuIFSplitPane = new UIFSplitPane();
    DataBasePanel = new JPanel();
    scrollPane2 = new JScrollPane();
    DataInformationPanel = new JPanel();
    DataInformationuIFSplitPane = new UIFSplitPane();
    TableFieldsPanel = new JPanel();
    panel14 = new JPanel();
    scrollPane3 = new JScrollPane();
    TableRecordsPanel = new JPanel();
    panel15 = new JPanel();
    scrollPane4 = new JScrollPane();
    db = Dao.getDBs(LoginNodeBean.getIP(), LoginNodeBean.getDBType(), LoginNodeBean.getUsername(), LoginNodeBean.getPassword());

    for (int i = 0; i < db.size(); i++) {
        Node rootnode = new Node();
        rootnode.setName(db.get(i).toString());
        table = Dao.getTables(db.get(i).toString(), LoginNodeBean.getIP(), LoginNodeBean.getDBType());
        for (int j = 0; j < table.size(); j++) {
            Node subrootnode = new Node();
            subrootnode.setName(table.get(j).toString());
            subrootnode.setParent(rootnode);
            box.addElement(subrootnode);
        }
        box.addElement(rootnode);
    }
    int selectmodel = TTree.DEFAULT_SELECTION;
    DataBaseTree = new TTree(box);
    DataBaseTree.setTTreeSelectionMode(selectmodel);
    DataBaseTree.addTreeNodeClickedActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            ElementNode node = (ElementNode) DataBaseTree.getLastSelectedPathComponent();
            if (node == null || !node.isLeaf()) {//如果该节点不是子节点，
                return;
            }
            rs1 = Dao.getTableVaribles(node.getParent().toString().substring(15), node.getElement().getName().toString(), LoginNodeBean.getIP(), LoginNodeBean.getDBType());
            ResultSetTableModel model2 = new ResultSetTableModel(rs1);
            final TTableModel informationtablemodel = informationtable.getTableModel();
            informationtablemodel.clearAllColumns();
            String type = LoginNodeBean.getDBType().toString();
            if (type.equals("SQLSERVER2008")) {
                informationtablemodel.addColumn(new TTableColumn("Fields Name"));
                informationtablemodel.addColumn(new TTableColumn("Fields Type"));
                informationtablemodel.addColumn(new TTableColumn("Fields Length"));
                informationtablemodel.addColumn(new TTableColumn("If NUll"));
                informationtablemodel.addColumn(new TTableColumn("Identity"));
                informationtablemodel.addColumn(new TTableColumn("Description"));
            } else if (type.equals("PostgreSQL")) {
                informationtablemodel.addColumn(new TTableColumn("name"));
                informationtablemodel.addColumn(new TTableColumn("type"));
                informationtablemodel.addColumn(new TTableColumn("notnull"));
                informationtablemodel.addColumn(new TTableColumn("comment"));
            }
            informationtablemodel.clearPublishedData();
            //int informationcolumncount = informationtablemodel.getColumnCount();
            int informationtablerowcount = model2.getRowCount();
            for (int l = 0; l < informationtablerowcount; l++) {
                Vector rows = new Vector();
                if (type.equals("SQLSERVER2008")) {
                    rows.addElement(model2.getValueAt(l, 0).toString());
                    rows.addElement(model2.getValueAt(l, 1).toString());
                    rows.addElement(model2.getValueAt(l, 2).toString());
                    rows.addElement(model2.getValueAt(l, 3).toString());
                    rows.addElement(model2.getValueAt(l, 4).toString());
                    rows.addElement(model2.getValueAt(l, 5).toString());
                }
                if (type.equals("PostgreSQL")) {
                    rows.addElement(model2.getValueAt(l, 0).toString());
                    rows.addElement(model2.getValueAt(l, 1).toString());
                    rows.addElement(model2.getValueAt(l, 2).toString());
                    rows.addElement(model2.getValueAt(l, 3).toString());
                }
                informationtablemodel.addRow(rows);
            }
            structurecolumns = Dao.getTablecolumns(node.getParent().toString().substring(15), node.getElement().getName().toString(), LoginNodeBean.getIP(), LoginNodeBean.getDBType());
            rs = Dao.getTableData(node.getParent().toString().substring(15), node.getElement().getName().toString(), LoginNodeBean.getIP(), LoginNodeBean.getDBType());
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
    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== panel1 ========
    {
        panel1.setLayout(new BorderLayout());

        //======== DataMenuBar ========
        {

            //======== DataStorageMenu ========
            {
                DataStorageMenu.setText("DataStorage");

                //---- StorageMenuItem ----
                StorageMenuItem.setText("Storage");
                StorageMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        StorageActionPerformed(e);
                    }
                });
                DataStorageMenu.add(StorageMenuItem);
                DataStorageMenu.addSeparator();

                //---- ExitMenuItem ----
                ExitMenuItem.setText("Exit");
                ExitMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ExitActionPerformed(e);
                    }
                });
                DataStorageMenu.add(ExitMenuItem);
            }
            DataMenuBar.add(DataStorageMenu);

            //======== DataInformationMenu ========
            {
                DataInformationMenu.setText("DataInformation");

                //---- ElementMenuItem ----
                ElementMenuItem.setText("Element");
                ElementMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ElementActionPerformed(e);
                    }
                });
                DataInformationMenu.add(ElementMenuItem);
                DataInformationMenu.addSeparator();

                //---- AttributeMenuItem ----
                AttributeMenuItem.setText("Attribute");
                AttributeMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        AttributeActionPerformed(e);
                    }
                });
                DataInformationMenu.add(AttributeMenuItem);
                DataInformationMenu.addSeparator();

                //---- TextMenuItem ----
                TextMenuItem.setText("Text");
                TextMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        TextActionPerformed(e);
                    }
                });
                DataInformationMenu.add(TextMenuItem);
                DataInformationMenu.addSeparator();

                //---- ViewMenuItem6 ----
                ViewMenuItem6.setText("View");
                ViewMenuItem6.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ViewActionPerformed(e);
                    }
                });
                DataInformationMenu.add(ViewMenuItem6);
            }
            DataMenuBar.add(DataInformationMenu);

            //======== DataExtractionMenu ========
            {
                DataExtractionMenu.setText("DataExtraction");

                //---- ExtractMenuItem ----
                ExtractMenuItem.setText("Extract");
                ExtractMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ExtractActionPerformed(e);
                    }
                });
                DataExtractionMenu.add(ExtractMenuItem);
                DataExtractionMenu.addSeparator();

                //---- DeleteMenuItem ----
                DeleteMenuItem.setText("Delete");
                DeleteMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        DeleteActionPerformed(e);
                    }
                });
                DataExtractionMenu.add(DeleteMenuItem);
            }
            DataMenuBar.add(DataExtractionMenu);

            //======== AboutMenu ========
            {
                AboutMenu.setText("About");

                //---- AboutDataPlatformMenuItem ----
                AboutDataPlatformMenuItem.setText("About Data Platform");
                AboutDataPlatformMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        AboutActionPerformed(e);
                    }
                });
                AboutMenu.add(AboutDataPlatformMenuItem);
            }
            DataMenuBar.add(AboutMenu);
        }
        panel1.add(DataMenuBar, BorderLayout.NORTH);

        //======== DataLookuIFSplitPane ========
        {
            DataLookuIFSplitPane.setOneTouchExpandable(true);
            DataLookuIFSplitPane.setDividerBorderVisible(true);

            //======== DataBasePanel ========
            {

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(DataBaseTree);
                }

                GroupLayout DataBasePanelLayout = new GroupLayout(DataBasePanel);
                DataBasePanel.setLayout(DataBasePanelLayout);
                DataBasePanelLayout.setHorizontalGroup(
                        DataBasePanelLayout.createParallelGroup()
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                );
                DataBasePanelLayout.setVerticalGroup(
                        DataBasePanelLayout.createParallelGroup()
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                );
            }
            DataLookuIFSplitPane.setLeftComponent(DataBasePanel);

            //======== DataInformationPanel ========
            {

                //======== DataInformationuIFSplitPane ========
                {
                    DataInformationuIFSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    DataInformationuIFSplitPane.setDividerBorderVisible(true);
                    DataInformationuIFSplitPane.setOneTouchExpandable(true);

                    //======== TableFieldsPanel ========
                    {
                        TableFieldsPanel.setBorder(new TitledBorder(null, "TableFileds", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, new Color(122, 150, 223)));

                        //======== panel14 ========
                        {
                            int[] options = new int[]{20, 30, 50, 0};
                            TTableNavigator navigator1 = new TTableNavigator(informationtable.getTableModel(), options);
								/*GroupLayout panel14Layout = new GroupLayout(panel14);
								panel14.setLayout(panel14Layout);
								panel14Layout.setHorizontalGroup(
									panel14Layout.createParallelGroup()
										.addGap(0, 753, Short.MAX_VALUE)
								);
								panel14Layout.setVerticalGroup(
									panel14Layout.createParallelGroup()
										.addGap(0, 26, Short.MAX_VALUE)
								);
								panel14.add(navigator1, BorderLayout.CENTER);*/
                            panel14.setLayout(new BorderLayout());
                            panel14.add(navigator1, BorderLayout.CENTER);
                        }

                        //======== scrollPane3 ========
                        {
                            scrollPane3.setViewportView(informationtable);
                        }

                        GroupLayout TableFieldsPanelLayout = new GroupLayout(TableFieldsPanel);
                        TableFieldsPanel.setLayout(TableFieldsPanelLayout);
                        TableFieldsPanelLayout.setHorizontalGroup(
                                TableFieldsPanelLayout.createParallelGroup()
                                        .addComponent(panel14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                        );
                        TableFieldsPanelLayout.setVerticalGroup(
                                TableFieldsPanelLayout.createParallelGroup()
                                        .addGroup(TableFieldsPanelLayout.createSequentialGroup()
                                                          .addComponent(panel14, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                          .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                        );
                    }
                    DataInformationuIFSplitPane.setTopComponent(TableFieldsPanel);

                    //======== TableRecordsPanel ========
                    {
                        TableRecordsPanel.setBorder(new TitledBorder(null, "TableRecords", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, new Color(122, 150, 223)));

                        //======== panel15 ========
                        {
                            int[] options1 = new int[]{20, 30, 50, 0};
                            TTableNavigator navigator2 = new TTableNavigator(structuretable.getTableModel(), options1);
								/*GroupLayout panel15Layout = new GroupLayout(panel15);
								panel15.setLayout(panel15Layout);
								panel15Layout.setHorizontalGroup(
									panel15Layout.createParallelGroup()
										.addGap(0, 753, Short.MAX_VALUE)
								);
								panel15Layout.setVerticalGroup(
									panel15Layout.createParallelGroup()
										.addGap(0, 26, Short.MAX_VALUE)
								);*/
                            panel15.setLayout(new BorderLayout());
                            panel15.add(navigator2, BorderLayout.CENTER);

                        }

                        //======== scrollPane4 ========
                        {
                            scrollPane4.setViewportView(structuretable);
                        }

                        GroupLayout TableRecordsPanelLayout = new GroupLayout(TableRecordsPanel);
                        TableRecordsPanel.setLayout(TableRecordsPanelLayout);
                        TableRecordsPanelLayout.setHorizontalGroup(
                                TableRecordsPanelLayout.createParallelGroup()
                                        .addComponent(panel15, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(scrollPane4, GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                        );
                        TableRecordsPanelLayout.setVerticalGroup(
                                TableRecordsPanelLayout.createParallelGroup()
                                        .addGroup(TableRecordsPanelLayout.createSequentialGroup()
                                                          .addComponent(panel15, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                          .addComponent(scrollPane4, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
                        );
                    }
                    DataInformationuIFSplitPane.setBottomComponent(TableRecordsPanel);
                }

                GroupLayout DataInformationPanelLayout = new GroupLayout(DataInformationPanel);
                DataInformationPanel.setLayout(DataInformationPanelLayout);
                DataInformationPanelLayout.setHorizontalGroup(
                        DataInformationPanelLayout.createParallelGroup()
                                .addComponent(DataInformationuIFSplitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
                );
                DataInformationPanelLayout.setVerticalGroup(
                        DataInformationPanelLayout.createParallelGroup()
                                .addComponent(DataInformationuIFSplitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                );
            }
            DataLookuIFSplitPane.setRightComponent(DataInformationPanel);
        }
        panel1.add(DataLookuIFSplitPane, BorderLayout.CENTER);
    }
    contentPane.add(panel1, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JPanel panel1;
private JMenuBar DataMenuBar;
private JMenu DataStorageMenu;
private JMenuItem StorageMenuItem;
private JMenuItem ExitMenuItem;
private JMenu DataInformationMenu;
private JMenuItem ElementMenuItem;
private JMenuItem AttributeMenuItem;
private JMenuItem TextMenuItem;
private JMenuItem ViewMenuItem6;
private JMenu DataExtractionMenu;
private JMenuItem ExtractMenuItem;
private JMenuItem DeleteMenuItem;
private JMenu AboutMenu;
private JMenuItem AboutDataPlatformMenuItem;
private UIFSplitPane DataLookuIFSplitPane;
private JPanel DataBasePanel;
private JScrollPane scrollPane2;
private JPanel DataInformationPanel;
private UIFSplitPane DataInformationuIFSplitPane;
private JPanel TableFieldsPanel;
private JPanel panel14;
private JScrollPane scrollPane3;
private JPanel TableRecordsPanel;
private JPanel panel15;
private JScrollPane scrollPane4;
private twaver.tree.TTree DataBaseTree;
TTable structuretable = new TTable();
TTable informationtable = new TTable();
// JFormDesigner - End of variables declaration  //GEN-END:variables
}
