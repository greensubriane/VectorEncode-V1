package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * Created by IntelliJ IDEA.
 * User: HeTing
 * Date: 11-10-10
 * Time: 下午6:26
 * To change this template use File | Settings | File Templates.
 */

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.GMLSchema;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * GML3 Parsing examples.
 *
 * @author He Ting
 */
public class GMLParsing {
private static org.geotools.swing.StatusBar statusBar1;
private static javax.swing.JToolBar jToolBar1 = new javax.swing.JToolBar();
private static javax.swing.JPanel panel = new javax.swing.JPanel();
GMLSchema schemas = new GMLSchema();
static ArrayList<String> GMLName = new ArrayList<String>();
static ArrayList<FeatureSource> featuresources = new ArrayList<FeatureSource>();
static ArrayList<FeatureCollection> featurecollections = new ArrayList<FeatureCollection>();
static ArrayList<Style> styles = new ArrayList<Style>();


public static void main(String[] args) throws Exception {
    parseGML3();
}

/**
 * Parses GML3 without specifying a schema location.
 */
public static void parseGML3() throws Exception {

    Class.forName("org.postgresql.Driver");
    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gmldatabase", "postgres", "ikoiko");
    Statement stat = conn.createStatement();
    ResultSet rs = stat.executeQuery("select storagedpath from document");
    while (rs.next()) {
        GMLName.add(rs.getString(1));
        System.out.println(rs.getString(1));
    }
    System.out.println("The GML FILE NAME is:" + GMLName);
    GMLConfiguration gml = new GMLConfiguration();
    Parser parser = new Parser(gml);
    parser.setStrict(false);

    for (int i = 0; i < GMLName.size(); i++) {
        featurecollections.add((FeatureCollection) parser.parse(GMLParsing.class.getResourceAsStream(GMLName.get(i))));
        //file:/C:/sciproject/DataPlatFormV1.0/basemanp.gml
    }
    MapContext map = new MapContext();
    map.setTitle("Generating Shapefile by GML");
    for (int j = 0; j < featurecollections.size(); j++) {
        featuresources.add(DataUtilities.source(featurecollections.get(j)));
        styles.add(createStyle2(featuresources.get(j)));
        map.addLayer(featuresources.get(j), styles.get(j));


    }
    JMapPane pane = new JMapPane();
    pane.setMapContext(map);
    pane.setRenderer(new StreamingRenderer());
    javax.swing.JButton zoominbutton = new javax.swing.JButton(new ZoomInAction(pane));
    javax.swing.JButton zoomoutbutton = new javax.swing.JButton(new ZoomOutAction(pane));
    javax.swing.JButton infobutton = new javax.swing.JButton(new InfoAction(pane));
    javax.swing.JButton pambutton = new javax.swing.JButton(new PanAction(pane));
    jToolBar1.setOrientation(javax.swing.JToolBar.HORIZONTAL);
    jToolBar1.setFloatable(false);
    jToolBar1.add(zoominbutton);
    jToolBar1.add(zoomoutbutton);
    jToolBar1.add(infobutton);
    jToolBar1.add(pambutton);
    statusBar1 = new org.geotools.swing.StatusBar(pane);
    panel.setLayout(new BorderLayout());
    panel.add(jToolBar1, BorderLayout.NORTH);
    panel.add(pane, BorderLayout.CENTER);
    panel.add(statusBar1, BorderLayout.SOUTH);
    JFrame frame = new JFrame();
    frame.add(panel, BorderLayout.CENTER);
    frame.setSize(800, 600);
    frame.setVisible(true);
}

private static Style createStyle2(FeatureSource featureSource) {
    SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
    Class geomType = schema.getGeometryDescriptor().getType().getBinding();
    System.out.println("The geomType name is :" + geomType.getName());
    return JSimpleStyleDialog.showDialog(null, schema);
}
}
