/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.GeoTool.PGDataStoreBean;
import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StationMappingView {

private javax.swing.JButton jButton1 = new javax.swing.JButton();
private javax.swing.JButton jButton2 = new javax.swing.JButton();
private javax.swing.JComboBox jComboBox1 = new javax.swing.JComboBox();
private javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
private javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
private javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
private javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
private javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
private javax.swing.JTextField jTextField1 = new javax.swing.JTextField();
private javax.swing.JTextField jTextField2 = new javax.swing.JTextField();
private javax.swing.JTextField jTextField3 = new javax.swing.JTextField();
private javax.swing.JTextField jTextField4 = new javax.swing.JTextField();
private javax.swing.JTextField jTextField5 = new javax.swing.JTextField();
private javax.swing.JPasswordField jPasswordField1 = new javax.swing.JPasswordField();
private javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane();
private javax.swing.JToolBar jToolBar1 = new javax.swing.JToolBar();
private javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
MapContext map = new DefaultMapContext();
JFrame frame = new JFrame("Station Mapping View");

public static boolean RIGHT_TO_LEFT = false;

static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
static Map PostgisconnInformation = new HashMap();
static JDBCDataStore pgDatastore;
static List<FeatureSource> features = new ArrayList<FeatureSource>();


public void addComponentsToPane(Container contentPane) {
    //Use BorderLayout. Default empty constructor with no horizontal and vertical
    //gaps

    jSplitPane1.add(jPanel3, JSplitPane.LEFT);
    jSplitPane1.add(jPanel4, JSplitPane.RIGHT);
    contentPane.setLayout(new BorderLayout(5, 5));
    if (!(contentPane.getLayout() instanceof BorderLayout)) {
        contentPane.add(new JLabel("Container doesn't use BorderLayout!"));
        return;
    }

    if (RIGHT_TO_LEFT) {
        contentPane.setComponentOrientation(
                java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    }
    jPanel2.setName("mapcontrolpanel1"); // NOI18N
    jLabel2.setText("Type:");
    jLabel2.setName("jLabel2"); // NOI18N
    jLabel3.setText("Host:");
    jLabel3.setName("jLabel3"); // NOI18N
    jLabel4.setText("Port:");
    jLabel4.setName("jLabel4"); // NOI18N
    jTextField1.setName("jTextField1"); // NOI18N
    jTextField2.setName("jTextField2"); // NOI18N
    jTextField3.setName("jTextField3"); // NOI18N
    jLabel5.setText("DBName:");
    jLabel5.setName("jLabel5"); // NOI18N
    jLabel6.setText("UserName:");
    jLabel6.setName("jLabel6"); // NOI18N
    jLabel7.setText("Password:");
    jLabel7.setName("jLabel7"); // NOI18N
    jTextField4.setName("jTextField4"); // NOI18N
    jTextField5.setName("jTextField5"); // NOI18N
    jButton1.setText("connect");
    jButton1.setName("jButton1"); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jButton2.setText("reset");
    jButton2.setName("jButton2"); // NOI18N
    jPasswordField1.setName("jPasswordField1"); // NOI18N
    jComboBox1.setName("jComboBox1"); // NOI18N
    jLabel8.setText("Feature:");
    jLabel8.setName("jLabel8"); // NOI18N

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jComboBox1, 0, 273, Short.MAX_VALUE)
                                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
                                                        .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                                        .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                                      .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                                                  .addContainerGap()
                                                                                  .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jLabel2)
                                                                                                    .addComponent(jLabel5)
                                                                                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jButton1))
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                  .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                    .addComponent(jLabel3)
                                                                                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jLabel6)
                                                                                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jButton2))
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                  .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                    .addComponent(jLabel4)
                                                                                                    .addComponent(jLabel7)
                                                                                                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                  .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(jLabel8))
                                                                                  .addContainerGap())
    );

    PostgisconnInformation.put("dbtype", "postgis");
    PostgisconnInformation.put("host", LoginNodeBean.getIP().toString());
    PostgisconnInformation.put("port", Integer.parseInt(LoginNodeBean.getPortNum().toString()));
    PostgisconnInformation.put("database", "postgis");
    PostgisconnInformation.put("user", LoginNodeBean.getUsername().toString());
    PostgisconnInformation.put("passwd", LoginNodeBean.getPassword().toString());

    try {
        pgDatastore = (JDBCDataStore) DataStoreFinder.getDataStore(PostgisconnInformation);
        new PGDataStoreBean().setPGDataStore(pgDatastore);
        if (pgDatastore != null) {
            System.out.println("连接postgis数据库成功");
        } else {
            System.out.println("连接postgis数据库失败");
        }
        ComboBoxModel cbm = new DefaultComboBoxModel(pgDatastore.getTypeNames());
        jComboBox1.setModel(cbm);
        FeatureSource basefeatureSource = pgDatastore.getFeatureSource("weihai");

        Style basestyle = createStyle2(basefeatureSource);
        FeatureLayer baselayer = new FeatureLayer(basefeatureSource, basestyle);
        CoordinateReferenceSystem basecrs = basefeatureSource.getSchema().getCoordinateReferenceSystem();
        map.addLayer(baselayer);
        map.setCoordinateReferenceSystem(basecrs);
        org.geotools.swing.JMapPane jMapPanel1 = new org.geotools.swing.JMapPane(new StreamingRenderer(), map);
        org.geotools.swing.MapLayerTable mapLayerTable1 = new org.geotools.swing.MapLayerTable(jMapPanel1);

        jMapPanel1.setDisplayArea(map.getLayerBounds());

        jMapPanel1.setMapContext(map);
        mapLayerTable1.setVisible(true);
        mapLayerTable1.setMapPane(jMapPanel1);
        jMapPanel1.setMapLayerTable(mapLayerTable1);
        javax.swing.JButton zoominbutton = new javax.swing.JButton(new ZoomInAction(jMapPanel1));
        javax.swing.JButton zoomoutbutton = new javax.swing.JButton(new ZoomOutAction(jMapPanel1));
        javax.swing.JButton pambutton = new javax.swing.JButton(new PanAction(jMapPanel1));
        jToolBar1.setOrientation(javax.swing.JToolBar.HORIZONTAL);
        jToolBar1.setFloatable(false);
        jToolBar1.add(zoominbutton);
        jToolBar1.add(zoomoutbutton);
        jToolBar1.add(pambutton);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mapLayerTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mapLayerTable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                        .addComponent(jMapPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                          .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                          .addComponent(jMapPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    } catch (Exception ex) {
        System.err.println(ex.getMessage());
    }

    //contentPane.add(jPanel2, BorderLayout.PAGE_START);
    contentPane.add(jSplitPane1, BorderLayout.CENTER);
    jComboBox1.addItemListener(new ItemListener() {

        public void itemStateChanged(ItemEvent evt) {
            ShowLayers(evt);
        }
    });
}

private void ShowLayers(ItemEvent evt) {
    try {
        FeatureSource featuresource = pgDatastore.getFeatureSource(jComboBox1.getSelectedItem().toString());
        Style style = createStyle2(featuresource);
        FeatureLayer layer = new FeatureLayer(featuresource, style);
        CoordinateReferenceSystem crs = featuresource.getSchema().getCoordinateReferenceSystem();
    } catch (IOException ex) {
        Logger.getLogger(StationMappingView.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void jButton1ActionPerformed(ActionEvent evt) {

}


private Style createStyle2(FeatureSource featureSource) {
    SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
    Class geomType = schema.getGeometryDescriptor().getType().getBinding();
    if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
        return JSimpleStyleDialog.showDialog(null, schema);
    } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
        return JSimpleStyleDialog.showDialog(null, schema);
    } else {
        return JSimpleStyleDialog.showDialog(null, schema);
    }
}

private void createAndShowGUI() {
    JFrame.setDefaultLookAndFeelDecorated(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Set up the content pane and add swing components to it
    addComponentsToPane(frame.getContentPane());
    frame.setSize(800, 800);
    frame.pack();
    frame.setVisible(true);
}

public static void main(String[] args) {

    javax.swing.SwingUtilities.invokeLater(new Runnable() {

        public void run() {
            new StationMappingView().createAndShowGUI();

        }
    });
}
}

