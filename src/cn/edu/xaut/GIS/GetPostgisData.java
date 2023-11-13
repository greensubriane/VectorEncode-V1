/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author greensubmarine
 */

import com.vividsolutions.jts.io.ParseException;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.map.MapContext;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetPostgisData {
//private static final Logger LOGGER = Logger.getLogger("org.geotools.postgis");
static JDBCDataStore pgDatastore;
//static PostgisNGDataStoreFactory factory=new PostgisNGDataStoreFactory();
static PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
static FeatureSource fsBC;


@SuppressWarnings("unchecked")
private static void ConnPostGis(String dbtype, String URL, int port, String database, String user, String password) {
    Map params = new HashMap();
    params.put("dbtype", dbtype);
    params.put("host", URL);
    params.put("port", new Integer(port));
    params.put("database", database);
    params.put("user", user);
    params.put("passwd", password);
    try {
        pgDatastore = (JDBCDataStore) DataStoreFinder.getDataStore(params);
        if (pgDatastore != null) {
            System.out.println(" 系统连接到位于： " + URL + " 的空间数据库 " + database + " 成功！ ");
        } else {
            System.out.println(" 系统连接到位于： " + URL + " 的空间数据库 " + database + " 失败！请检查相关参数 ");
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println(" 系统连接到位于： " + URL + " 的空间数据库 " + database + " 失败！请检查相关参数 ");
    }
}

public static void main(String args[]) throws IOException, ParseException {
    ConnPostGis("postgis", "localhost", 5432, "postgis", "postgres", "ikoiko");
    FeatureSource featuresource = pgDatastore.getFeatureSource("weihai");
    FeatureSource featuresource2 = pgDatastore.getFeatureSource("river");
    FeatureSource featuresource4 = pgDatastore.getFeatureSource("huangleihe");
    FeatureSource featuresource5 = pgDatastore.getFeatureSource("guogezhuang");
    FeatureSource featuresource1 = pgDatastore.getFeatureSource("Precipitation Gauges");
    FeatureSource featuresource3 = pgDatastore.getFeatureSource("reservoir");
    //Style shpStyle = SLD.createSimpleStyle(featuresource.getSchema(), Color.black);
    Style shpStyle = SLD.createPolygonStyle(Color.yellow, Color.cyan, 0.5f);
    Style shpStyle2 = SLD.createSimpleStyle(featuresource2.getSchema(), Color.blue);
    Style shpStyle1 = SLD.createSimpleStyle(featuresource1.getSchema(), Color.red);
    Style shpStyle3 = SLD.createSimpleStyle(featuresource4.getSchema(), Color.blue);
    Style shpStyle4 = SLD.createSimpleStyle(featuresource5.getSchema(), Color.blue);
    //Style shpStyle5 = SLD.createSimpleStyle(featuresource3.getSchema(), Color.blue);
    Style shpStyle5 = SLD.createPolygonStyle(Color.blue, Color.blue, 0.5f);

    MapContext map = new MapContext();

    map.setTitle("ImageLab");

    map.addLayer(featuresource, shpStyle);
    map.addLayer(featuresource1, shpStyle1);
    map.addLayer(featuresource2, shpStyle2);
    map.addLayer(featuresource3, shpStyle5);
    map.addLayer(featuresource4, shpStyle3);
    map.addLayer(featuresource5, shpStyle4);

    JMapFrame frame = new JMapFrame();

    frame.setSize(800, 600);
    frame.enableStatusBar(true);
    frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN, JMapFrame.Tool.RESET);
    frame.enableToolBar(true);
    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);
    JMenu menu = new JMenu("Raster");
    menuBar.add(menu);
    frame.repaint();
    frame.setVisible(true);
}
}

