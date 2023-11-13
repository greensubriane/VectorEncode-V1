/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import cn.edu.xaut.SLGridDataNode.dao.Dao;
import cn.edu.xaut.SLGridDataNode.dao.ResultSetTableModel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.uif_lite.component.UIFSplitPane;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.*;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
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
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
/*
 * Created by JFormDesigner on Thu Sep 29 20:38:09 CEST 2011
 */


/**
 * @author heting
 */
public class MainPanel extends JFrame {
List table, db, table1, db1, structurecolumns, structurerows, informationcolumns, informationrows;
ResultSet rs, rs1, rs2;
private TDataBox box = new TDataBox();
ArrayList<FeatureCollection> featurecollections = new ArrayList<FeatureCollection>();
ArrayList<FeatureSource> gmlfeaturesources = new ArrayList<FeatureSource>();
ArrayList<Style> styles = new ArrayList<Style>();
private MapLayer[] array;

public MainPanel() throws Exception {
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

private void initComponents() throws Exception {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    tabbedPane3 = new JTabbedPane();
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
    panel5 = new JPanel();
    label1 = compFactory.createLabel("  Features:");
    comboBox1 = new JComboBox();
    separator1 = compFactory.createSeparator("Map");
    uIFSplitPane1 = new UIFSplitPane();
    panel8 = new JPanel();
    uIFSplitPane2 = new UIFSplitPane();
    panel10 = new JPanel();
    panel11 = new JPanel();
    panel9 = new JPanel();
    mapLayerTable1 = new org.geotools.swing.MapLayerTable();
    jToolBar1 = new javax.swing.JToolBar();
    button1 = new javax.swing.JButton();

    ArrayList<String> DocList = new ArrayList<String>();
    DocList = this.GetDocList();
    String[] docs = new String[DocList.size()];
    for (int l = 0; l < DocList.size(); l++) {
        docs[l] = DocList.get(l);
    }
    comboBox1.setModel(new javax.swing.DefaultComboBoxModel(docs));
    Iterator docnames = DocList.iterator();
    GMLConfiguration gml = new GMLConfiguration();
    Parser parser = new Parser(gml);
    parser.setStrict(false);
    while (docnames.hasNext()) {
        String name = (String) docnames.next();
        System.out.println(name);
        featurecollections.add((FeatureCollection) parser.parse(MainPanel.class.getResourceAsStream(name)));
    }
    array = new MapLayer[gmlfeaturesources.size()];
    for (int t = 0; t < featurecollections.size(); t++) {
        gmlfeaturesources.add(DataUtilities.source(featurecollections.get(t)));
        styles.add(createStyle(gmlfeaturesources.get(t)));
    }
    MapContext map = new MapContext();
    array = new MapLayer[gmlfeaturesources.size()];
    for (int n = 0; n < gmlfeaturesources.size(); n++) {
        FeatureType type = gmlfeaturesources.get(n).getSchema();
        System.out.println("The featuresource type name is: " + type.getName().toString());
        array[n] = new MapLayer(gmlfeaturesources.get(n), styles.get(n));
        ReferencedEnvelope envelope = featurecollections.get(n).getBounds();
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        map.setCoordinateReferenceSystem(crs);
    }

    map.addLayers(array);
    for (int j = 0; j < array.length; j++) {
        array[j].setSelected(true);
        array[j].setVisible(true);
    }

    CoordinateReferenceSystem worldCRS = map.getCoordinateReferenceSystem();
    map.setCoordinateReferenceSystem(worldCRS);
    jMapPane1 = new org.geotools.swing.JMapPane();
    jMapPane1.setMapContext(map);
    jMapPane1.setRenderer(new StreamingRenderer());
    jMapPane1.setDisplayArea(map.getLayerBounds());
    jMapPane1.setBackground(Color.WHITE);
    javax.swing.JButton zoominbutton = new javax.swing.JButton(new ZoomInAction(jMapPane1));
    javax.swing.JButton zoomoutbutton = new javax.swing.JButton(new ZoomOutAction(jMapPane1));
    javax.swing.JButton infobutton = new javax.swing.JButton(new InfoAction(jMapPane1));
    javax.swing.JButton pambutton = new javax.swing.JButton(new PanAction(jMapPane1));
    jToolBar1.setOrientation(javax.swing.JToolBar.HORIZONTAL);
    jToolBar1.setFloatable(false);
    jToolBar1.add(zoominbutton);
    jToolBar1.add(zoomoutbutton);
    jToolBar1.add(infobutton);
    jToolBar1.add(pambutton);
    mapLayerTable1.setMapPane(jMapPane1);
    MapLayer[] layerfrommappane = map.getLayers();
    for (int m = 0; m < layerfrommappane.length; m++) {
        mapLayerTable1.onAddLayer(layerfrommappane[m]);
    }
    statusBar1 = new org.geotools.swing.StatusBar(jMapPane1);

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
    panel4 = new JPanel();

    //======== this ========
    setMinimumSize(new Dimension(1024, 768));
    setTitle("Data Platform");
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== tabbedPane3 ========
    {

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
        tabbedPane3.addTab("Data Showing", panel1);


        //======== panel4 ========
        {

            //======== panel5 ========
            {
                GroupLayout panel5Layout = new GroupLayout(panel5);
                panel5.setLayout(panel5Layout);
                panel5Layout.setHorizontalGroup(
                        panel5Layout.createParallelGroup()
                                .addGroup(panel5Layout.createSequentialGroup()
                                                  .addComponent(label1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                                  .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                  .addComponent(comboBox1, 0, 360, Short.MAX_VALUE)
                                                  .addContainerGap())
                );
                panel5Layout.setVerticalGroup(
                        panel5Layout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                                  .addComponent(label1, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                                                                  .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                );
            }

            //======== uIFSplitPane1 ========
            {
                uIFSplitPane1.setOneTouchExpandable(true);
                uIFSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                uIFSplitPane1.setDividerBorderVisible(true);
                //======== panel8 ========
                {
                    //======== uIFSplitPane2 ========
                    {
                        uIFSplitPane2.setDividerBorderVisible(true);
                        uIFSplitPane2.setAutoscrolls(true);
                        uIFSplitPane2.setOneTouchExpandable(true);

                        //======== panel10 ========
                        {
                            panel10.setLayout(new BorderLayout());
                            panel10.add(mapLayerTable1, BorderLayout.CENTER);
                        }
                        uIFSplitPane2.setLeftComponent(panel10);

                        //======== panel11 ========
                        {
                            panel11.setLayout(new BorderLayout());
                            panel11.add(jToolBar1, BorderLayout.NORTH);
                            panel11.add(statusBar1, BorderLayout.SOUTH);
                            panel11.add(jMapPane1, BorderLayout.CENTER);
                        }
                        uIFSplitPane2.setRightComponent(panel11);
                    }

                    GroupLayout panel8Layout = new GroupLayout(panel8);
                    panel8.setLayout(panel8Layout);
                    panel8Layout.setHorizontalGroup(
                            panel8Layout.createParallelGroup()
                                    .addComponent(uIFSplitPane2, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                    );
                    panel8Layout.setVerticalGroup(
                            panel8Layout.createParallelGroup()
                                    .addComponent(uIFSplitPane2, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    );
                }
                uIFSplitPane1.setTopComponent(panel8);

                //======== panel9 ========
                {
                    panel9.setAutoscrolls(true);

                    GroupLayout panel9Layout = new GroupLayout(panel9);
                    panel9.setLayout(panel9Layout);
                    panel9Layout.setHorizontalGroup(
                            panel9Layout.createParallelGroup()
                                    .addGap(0, 460, Short.MAX_VALUE)
                    );
                    panel9Layout.setVerticalGroup(
                            panel9Layout.createParallelGroup()
                                    .addGap(0, 172, Short.MAX_VALUE)
                    );
                }
                uIFSplitPane1.setBottomComponent(panel9);
            }

            GroupLayout panel4Layout = new GroupLayout(panel4);
            panel4.setLayout(panel4Layout);
            panel4Layout.setHorizontalGroup(
                    panel4Layout.createParallelGroup()
                            .addComponent(uIFSplitPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                            .addComponent(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(separator1, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
            );
            panel4Layout.setVerticalGroup(
                    panel4Layout.createParallelGroup()
                            .addGroup(panel4Layout.createSequentialGroup()
                                              .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                              .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                              .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                              .addComponent(uIFSplitPane1, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
            );

        }
        tabbedPane3.addTab("Map Showing", panel4);

    }
    contentPane.add(tabbedPane3, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

private Style createStyle2(FeatureSource featureSource) {
    SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
    Class geomType = schema.getGeometryDescriptor().getType().getBinding();
    if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
        return JSimpleStyleDialog.showDialog(null, schema);
    } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
        return JSimpleStyleDialog.showDialog(null, schema);
    } else if (MultiPoint.class.isAssignableFrom(geomType) || Point.class.isAssignableFrom(geomType)) {
        return JSimpleStyleDialog.showDialog(null, schema);
    } else {
        return JSimpleStyleDialog.showDialog(null, schema);
    }
}

private Style createStyle(FeatureSource featureSource) {
    SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
    Class geomType = schema.getGeometryDescriptor().getType().getBinding();
    System.out.println("The GeoType Name is: " + geomType.getName());
    return JSimpleStyleDialog.showDialog(null, schema);
}

private ArrayList<String> GetDocList() {
    String SQLString = "SELECT documentname FROM document";
    ArrayList<String> DocList = new ArrayList<String>();
    try {
        ResultSet rs1 = new DBConnection().ExecuteSQL(SQLString);
        while (rs1.next()) {
            DocList.add(rs1.getString(1));
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return DocList;
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JTabbedPane tabbedPane3;
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
private JPanel panel4;
private JPanel panel5;
private JLabel label1;
private JComboBox comboBox1;
private JComponent separator1;
private UIFSplitPane uIFSplitPane1;
private JPanel panel8;
private UIFSplitPane uIFSplitPane2;
private JPanel panel10;
private JPanel panel11;
private JPanel panel9;
private org.geotools.swing.MapLayerTable mapLayerTable1;
private org.geotools.swing.JMapPane jMapPane1;
private org.geotools.swing.StatusBar statusBar1;
private javax.swing.JToolBar jToolBar1;
private javax.swing.JButton button1;
// JFormDesigner - End of variables declaration  //GEN-END:variables

public static void main(String args[]) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            try {
                new MainPanel().setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    });

}
}

