/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StationMapinformation.java
 *
 * Created on 2011-8-29, 14:26:42
 */

package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

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
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StationMapinformations extends javax.swing.JFrame {

static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
static Map PostgisconnInformation = new HashMap();
static JDBCDataStore pgDatastore;
static List<FeatureSource> features = new ArrayList<FeatureSource>();

/** Creates new form StationMapinformation */
public StationMapinformations() {
    initComponents();
}

/** This method is called from within the constructor to
 * initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is
 * always regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">
private void initComponents() {

    jSeparator1 = new javax.swing.JSeparator();
    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jTextField1 = new javax.swing.JTextField();
    jTextField2 = new javax.swing.JTextField();
    jTextField3 = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jTextField4 = new javax.swing.JTextField();
    jTextField5 = new javax.swing.JTextField();
    jPasswordField1 = new javax.swing.JPasswordField();
    jComboBox1 = new javax.swing.JComboBox();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jFrame = new javax.swing.JFrame();
    //jSplitPane1 = new javax.swing.JSplitPane();
    // mapLayerTable1 = new org.geotools.swing.MapLayerTable();
    //jMapPane1 = new org.geotools.swing.JMapPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setName("Form"); // NOI18N

    jSeparator1.setName("jSeparator1"); // NOI18N

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jPanel1.border.title"))); // NOI18N
    jPanel1.setName("jPanel1"); // NOI18N

    jLabel1.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel1.text")); // NOI18N
    jLabel1.setName("jLabel1"); // NOI18N

    jLabel2.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel2.text")); // NOI18N
    jLabel2.setName("jLabel2"); // NOI18N

    jLabel3.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel3.text")); // NOI18N
    jLabel3.setName("jLabel3"); // NOI18N

    jLabel4.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel4.text")); // NOI18N
    jLabel4.setName("jLabel4"); // NOI18N

    jTextField1.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField1.text")); // NOI18N
    jTextField1.setName("jTextField1"); // NOI18N

    jTextField2.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField2.text")); // NOI18N
    jTextField2.setName("jTextField2"); // NOI18N

    jTextField3.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField3.text")); // NOI18N
    jTextField3.setName("jTextField3"); // NOI18N

    jLabel5.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel5.text")); // NOI18N
    jLabel5.setName("jLabel5"); // NOI18N

    jLabel6.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel6.text")); // NOI18N
    jLabel6.setName("jLabel6"); // NOI18N

    jLabel7.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jLabel7.text")); // NOI18N
    jLabel7.setName("jLabel7"); // NOI18N

    jTextField4.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField4.text")); // NOI18N
    jTextField4.setName("jTextField4"); // NOI18N

    jTextField5.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField5.text")); // NOI18N
    jTextField5.setName("jTextField5"); // NOI18N

    jPasswordField1.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jTextField6.text")); // NOI18N
    jPasswordField1.setName("jTextField6"); // NOI18N

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
    jComboBox1.setName("jComboBox1"); // NOI18N

    jButton1.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jButton1.text")); // NOI18N
    jButton1.setName("jButton1"); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton2.setText(org.openide.util.NbBundle.getMessage(StationMapinformation.class, "StationMapinformation.jButton2.text")); // NOI18N
    jButton2.setName("jButton2"); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                          .addComponent(jLabel1)
                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                          .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                          .addComponent(jLabel4)
                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                          .addComponent(jComboBox1, 0, 222, Short.MAX_VALUE))
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                                                                                                       .addComponent(jLabel3)
                                                                                                                                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                       .addComponent(jTextField3))
                                                                          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                                                                                                       .addComponent(jLabel2)
                                                                                                                                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                       .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                      .addGap(63, 63, 63)
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                                                                      .addComponent(jLabel5)
                                                                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                          .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                            .addComponent(jLabel7)
                                                                                            .addGap(6, 6, 6))
                                                                          .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                            .addComponent(jLabel6)
                                                                                            .addGap(18, 18, 18))))
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                                                        .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                      .addGap(36, 36, 36))
    );
    jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jButton1)
                                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                            .addComponent(jLabel2)
                                                                                            .addComponent(jButton2)
                                                                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                            .addComponent(jLabel3)
                                                                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                          .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                            .addComponent(jLabel4)
                                                                                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                          .addComponent(jLabel6)
                                                                          .addGap(18, 18, 18)
                                                                          .addComponent(jLabel7)))
                                      .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                                      .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addContainerGap(308, Short.MAX_VALUE))
    );

    pack();
}// </editor-fold>

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    PostgisconnInformation.put("dbtype", jTextField1.getText().toString());
    PostgisconnInformation.put("host", jTextField2.getText().toString());
    PostgisconnInformation.put("port", Integer.parseInt(jTextField3.getText().toString()));
    PostgisconnInformation.put("database", jTextField4.getText().toString());
    PostgisconnInformation.put("user", jTextField5.getText().toString());
    PostgisconnInformation.put("passwd", jPasswordField1.getText().toString());

    try {
        pgDatastore = (JDBCDataStore) DataStoreFinder.getDataStore(PostgisconnInformation);

        if (pgDatastore != null) {
            System.out.println("连接postgis数据库成功");
        } else {
            System.out.println("连接postgis数据库失败");
        }
        ComboBoxModel cbm = new DefaultComboBoxModel(pgDatastore.getTypeNames());
        jComboBox1.setModel(cbm);

        FeatureSource featureSource = pgDatastore.getFeatureSource("weihai");
        FeatureSource featureSource1 = pgDatastore.getFeatureSource("Precipitation Gauges");
        FeatureSource featureSource2 = pgDatastore.getFeatureSource("river");
        FeatureSource featureSource3 = pgDatastore.getFeatureSource("reservoir");
        FeatureSource featureSource4 = pgDatastore.getFeatureSource("huangleihe");
        // Create a map context and add our shapefile to it

        org.opengis.geometry.Envelope env = featureSource.getBounds();
        org.opengis.geometry.Envelope env1 = featureSource1.getBounds();
        org.opengis.geometry.Envelope env2 = featureSource2.getBounds();
        org.opengis.geometry.Envelope env3 = featureSource3.getBounds();
        org.opengis.geometry.Envelope env4 = featureSource4.getBounds();

        Style style = createStyle2(featureSource);
        Style style1 = createStyle2(featureSource1);
        Style style2 = createStyle2(featureSource2);
        Style style3 = createStyle2(featureSource3);
        Style style4 = createStyle2(featureSource4);
        // Add the features and the associated Style object to
        // the MapContext as a new MapLayer
        Layer layer = new FeatureLayer(featureSource, style);
        Layer layer1 = new FeatureLayer(featureSource1, style1);
        Layer layer2 = new FeatureLayer(featureSource2, style2);
        Layer layer3 = new FeatureLayer(featureSource3, style3);
        Layer layer4 = new FeatureLayer(featureSource4, style4);
        CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
        MapContext map = new DefaultMapContext();
        map.layers().add(layer);
        map.layers().add(layer1);
        map.layers().add(layer2);
        map.layers().add(layer3);
        map.layers().add(layer4);
        map.setCoordinateReferenceSystem(crs);
        org.geotools.swing.JMapPane jMapPanel1 = new org.geotools.swing.JMapPane(new StreamingRenderer(), map);
        jMapPanel1.setDisplayArea(map.getLayerBounds());
        org.geotools.swing.MapLayerTable mapLayerTable1 = new org.geotools.swing.MapLayerTable(jMapPanel1);
        javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, mapLayerTable1, jMapPanel1);
        //this.add(jSplitPane1, BorderLayout.CENTER);
    } catch (Exception ex) {
        System.err.println(ex.getMessage());
    }

}

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new StationMapinformations().setVisible(true);
        }
    });
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

// Variables declaration - do not modify
private javax.swing.JButton jButton1;
private javax.swing.JButton jButton2;
private javax.swing.JComboBox jComboBox1;
private javax.swing.JLabel jLabel1;
private javax.swing.JLabel jLabel2;
private javax.swing.JLabel jLabel3;
private javax.swing.JLabel jLabel4;
private javax.swing.JLabel jLabel5;
private javax.swing.JLabel jLabel6;
private javax.swing.JLabel jLabel7;
private javax.swing.JPanel jPanel1;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JTextField jTextField1;
private javax.swing.JTextField jTextField2;
private javax.swing.JTextField jTextField3;
private javax.swing.JTextField jTextField4;
private javax.swing.JTextField jTextField5;
private javax.swing.JPasswordField jPasswordField1;
private javax.swing.JFrame jFrame;
}
