/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StyleLab {

static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
static Map PostgisconnInformation = new HashMap();
static JDBCDataStore pgDatastore;

public static void main(String[] args) throws Exception {
    StyleLab me = new StyleLab();
    me.displayShapefile();
}

private void displayShapefile() throws Exception {

    PostgisconnInformation.put("dbtype", "postgis");
    PostgisconnInformation.put("host", "localhost");
    PostgisconnInformation.put("port", 5432);
    PostgisconnInformation.put("database", "postgis");
    PostgisconnInformation.put("user", "postgres");
    PostgisconnInformation.put("passwd", "ikoiko");

    pgDatastore = (JDBCDataStore) DataStoreFinder.getDataStore(PostgisconnInformation);
    if (pgDatastore != null) {
        System.out.println("连接postgis数据库成功");
    } else {
        System.out.println("连接postgis数据库失败");
    }
    FeatureSource featureSource = pgDatastore.getFeatureSource("weihai");
    FeatureSource featureSource1 = pgDatastore.getFeatureSource("Precipitation Gauges");
    FeatureSource featureSource2 = pgDatastore.getFeatureSource("river");
    FeatureSource featureSource3 = pgDatastore.getFeatureSource("reservoir");
    FeatureSource featureSource4 = pgDatastore.getFeatureSource("huangleihe");
    // Create a map context and add our shapefile to it
    MapContext map = new DefaultMapContext();
    map.setTitle("Station's Map View");
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
    CoordinateReferenceSystem worldCRS = map.getCoordinateReferenceSystem();

    map.layers().add(layer);
    map.layers().add(layer1);
    map.layers().add(layer2);
    map.layers().add(layer3);
    map.layers().add(layer4);
    map.setCoordinateReferenceSystem(worldCRS);
    JMapPane mappane = new JMapPane(new StreamingRenderer(), map);
    mappane.setDisplayArea(map.getLayerBounds());
    MapLayerTable mapLayerTable = new MapLayerTable(mappane);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, mapLayerTable, mappane);

    JFrame frame = new JFrame();
    frame.setLayout(new BorderLayout());
    frame.add(splitPane, BorderLayout.CENTER);
    frame.setVisible(true);


}

private Style createStyle2(FeatureSource featureSource) {
    SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
    Class geomType = schema.getGeometryDescriptor().getType().getBinding();
    if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
        //return createPolygonStyle();
        return JSimpleStyleDialog.showDialog(null, schema);
    } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
        //return createLineStyle();
        return JSimpleStyleDialog.showDialog(null, schema);
    } else {
        //return createPointStyle();
        return JSimpleStyleDialog.showDialog(null, schema);
    }
}
}

