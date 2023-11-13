/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.GeoTool;

import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif_lite.component.UIFSplitPane;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
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

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by JFormDesigner on Sun Sep 04 20:40:08 CEST 2011
 * @author heting
 */
public class StationMapViews extends JFrame {
static List<FeatureSource> gmlfeaturesources = new ArrayList<FeatureSource>();
static List<Style> styles = new ArrayList<Style>();
private MapLayer[] array;

public StationMapViews() throws Exception {
    initComponents();
}

private void initComponents() throws Exception {


    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    panel4 = new JPanel();
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
    array = new MapLayer[DocList.size()];
    for (int l = 0; l < DocList.size(); l++) {
        docs[l] = DocList.get(l);
        InputStream in = StationMapViews.class.getResourceAsStream(DocList.get(l));
        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);
        FeatureCollection features = (FeatureCollection) parser.parse(in);
        gmlfeaturesources.add(DataUtilities.source(features));
        styles.add(createStyle2(gmlfeaturesources.get(l)));
        array[l] = new MapLayer(gmlfeaturesources.get(l), styles.get(l));

    }
    comboBox1.setModel(new javax.swing.DefaultComboBoxModel(docs));

    for (int m = 0; m < gmlfeaturesources.size(); m++) {
        FeatureType type = gmlfeaturesources.get(m).getSchema();
        System.out.println("The featuresource type name is: " + type.getName().toString());
    }
    MapContext map = new MapContext(array);
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
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponent
    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout("default:grow", "fill:default:grow"));
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
    contentPane.add(panel4, CC.xy(1, 1));
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
    } else {
        return JSimpleStyleDialog.showDialog(null, schema);
    }
}

private ArrayList<String> GetDocList() {
    String SQLString = "USE vectorxrsystem SELECT DocumentName FROM Document";
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
}
