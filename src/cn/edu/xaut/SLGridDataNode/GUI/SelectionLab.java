/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author HeTing
 */

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.FileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.styling.Stroke;
import org.geotools.styling.*;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.awt.Color.*;
import static java.lang.System.out;
import static org.geotools.data.FileDataStoreFinder.getDataStore;
import static org.geotools.factory.CommonFactoryFinder.getFilterFactory2;
import static org.geotools.factory.CommonFactoryFinder.getStyleFactory;
import static org.geotools.swing.data.JFileDataStoreChooser.showOpenFile;

/**
 * In this example we create a map tool to select a feature clicked
 * with the mouse. The selected feature will be painted yellow.
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.7-M3/demo/example/src/main/java/org/geotools/demo/SelectionLab.java $
 */
public class SelectionLab {

/*
 * Factories that we will use to create style and filter objects
 */
private StyleFactory sf = getStyleFactory(null);
private FilterFactory2 ff = getFilterFactory2(null);

/*
 * Convenient constants for the type of feature geometry in the shapefile
 */
private SelectionLab.GeomType POLYGON;
private SelectionLab.GeomType LINE;
private SelectionLab.GeomType POINT;

private enum GeomType {POINT, LINE, POLYGON}

;

/*
 * Some default style variables
 */
private static final Color LINE_COLOUR = BLUE;
private static final Color FILL_COLOUR = CYAN;
private static final Color SELECTED_COLOUR = YELLOW;
private static final float OPACITY = 1.0f;
private static final float LINE_WIDTH = 1.0f;
private static final float POINT_SIZE = 10.0f;

private JMapFrame mapFrame;
private SimpleFeatureSource featureSource;

private String geometryAttributeName;
private GeomType geometryType;

/*
 * The application method
 */
public static void main(String[] args) throws Exception {
    SelectionLab me = new SelectionLab();

    File file = showOpenFile("shp", null);
    if (file == null) {
        return;
    }

    me.displayShapefile(file);
}
// docs end main

// docs start display shapefile

/**
 * This method connects to the shapefile; retrieves information about
 * its features; creates a map frame to display the shapefile and adds
 * a custom feature selection tool to the toolbar of the map frame.
 */
public void displayShapefile(File file) throws Exception {
    FileDataStore store = getDataStore(file);
    featureSource = store.getFeatureSource();
    setGeometry();

    /*
     * Create the JMapFrame and set it to display the shapefile's features
     * with a default line and colour style
     */
    MapContext map = new DefaultMapContext();
    map.setTitle("Feature selection tool example");
    Style style = createDefaultStyle();
    map.addLayer(featureSource, style);
    mapFrame = new JMapFrame(map);
    mapFrame.enableToolBar(true);
    mapFrame.enableStatusBar(true);

    /*
     * Before making the map frame visible we add a new button to its
     * toolbar for our custom feature selection tool
     */
    JToolBar toolBar = mapFrame.getToolBar();
    JButton btn = new JButton("Select");
    toolBar.addSeparator();
    toolBar.add(btn);

    /*
     * When the user clicks the button we want to enable
     * our custom feature selection tool. Since the only
     * mouse action we are intersted in is 'clicked', and
     * we are not creating control icons or cursors here,
     * we can just create our tool as an anonymous sub-class
     * of CursorTool.
     */
    btn.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            mapFrame.getMapPane().setCursorTool(
                    new CursorTool() {

                        @Override
                        public void onMouseClicked(MapMouseEvent ev) {
                            selectFeatures(ev);
                        }
                    });
        }
    });

    /**
     * Finally, we display the map frame. When it is closed
     * this application will exit.
     */
    mapFrame.setSize(600, 600);
    mapFrame.setVisible(true);
}
// docs end display shapefile

// docs start select features

/**
 * This method is called by our feature selection tool when
 * the user has clicked on the map.
 *
 * @param pos map (world) coordinates of the mouse cursor
 */
void selectFeatures(MapMouseEvent ev) {

    out.println("Mouse click at: " + ev.getMapPosition());

    /*
     * Construct a 5x5 pixel rectangle centred on the mouse click position
     */
    Point screenPos = ev.getPoint();
    Rectangle screenRect = new Rectangle(screenPos.x - 2, screenPos.y - 2, 5, 5);


    AffineTransform screenToWorld = mapFrame.getMapPane().getScreenToWorldTransform();
    Rectangle2D worldRect = screenToWorld.createTransformedShape(screenRect).getBounds2D();
    ReferencedEnvelope bbox = new ReferencedEnvelope(
            worldRect,
            mapFrame.getMapContext().getCoordinateReferenceSystem());

    /*
     * Create a Filter to select features that intersect with
     * the bounding box
     */
    Filter filter = ff.intersects(ff.property(geometryAttributeName), ff.literal(bbox));

    /*
     * Use the filter to identify the selected features
     */
    try {
        SimpleFeatureCollection selectedFeatures =
                featureSource.getFeatures(filter);

        SimpleFeatureIterator iter = selectedFeatures.features();
        Set<FeatureId> IDs = new HashSet<>();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                IDs.add(feature.getIdentifier());


                out.println("   " + feature.getIdentifier());
            }

        } finally {
            iter.close();
        }

        if (IDs.isEmpty()) {
            out.println("   no feature selected");
        }

        displaySelectedFeatures(IDs);

    } catch (IOException | NoSuchElementException ex) {
        ex.printStackTrace();
        return;
    }
}

public void displaySelectedFeatures(Set<FeatureId> IDs) {
    Style style;

    if (IDs.isEmpty()) {
        style = createDefaultStyle();

    } else {
        style = createSelectedStyle(IDs);
    }

    mapFrame.getMapContext().getLayer(0).setStyle(style);
    mapFrame.getMapPane().repaint();
}
// docs end display selected

// docs start default style

/**
 * Create a default Style for feature display
 */
private Style createDefaultStyle() {
    Rule rule = createRule(LINE_COLOUR, FILL_COLOUR);

    FeatureTypeStyle fts = sf.createFeatureTypeStyle();
    fts.rules().add(rule);

    Style style = sf.createStyle();
    style.featureTypeStyles().add(fts);
    return style;
}
// docs end default style

// docs start selected style

/**
 * Create a Style where features with given IDs are painted
 * yellow, while others are painted with the default colors.
 */
private Style createSelectedStyle(Set<FeatureId> IDs) {
    Rule selectedRule = createRule(SELECTED_COLOUR, SELECTED_COLOUR);
    selectedRule.setFilter(ff.id(IDs));

    Rule otherRule = createRule(LINE_COLOUR, FILL_COLOUR);
    otherRule.setElseFilter(true);

    FeatureTypeStyle fts = sf.createFeatureTypeStyle();
    fts.rules().add(selectedRule);
    fts.rules().add(otherRule);

    Style style = sf.createStyle();
    style.featureTypeStyles().add(fts);
    return style;
}
// docs end selected style

// docs start create rule

/**
 * Helper for createXXXStyle methods. Creates a new Rule containing
 * a Symbolizer tailored to the geometry type of the features that
 * we are displaying.
 */
private Rule createRule(Color outlineColor, Color fillColor) {
    Symbolizer symbolizer = null;
    Fill fill = null;
    Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(LINE_WIDTH));

    switch (geometryType) {
        case POLYGON:
            fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));
            symbolizer = sf.createPolygonSymbolizer(stroke, fill, geometryAttributeName);
            break;

        case LINE:
            symbolizer = sf.createLineSymbolizer(stroke, geometryAttributeName);
            break;

        case POINT:
            fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));

            Mark mark = sf.getCircleMark();
            mark.setFill(fill);
            mark.setStroke(stroke);

            Graphic graphic = sf.createDefaultGraphic();
            graphic.graphicalSymbols().clear();
            graphic.graphicalSymbols().add(mark);
            graphic.setSize(ff.literal(POINT_SIZE));

            symbolizer = sf.createPointSymbolizer(graphic, geometryAttributeName);
    }

    Rule rule = sf.createRule();
    rule.symbolizers().add(symbolizer);
    return rule;
}
// docs end create rule

// docs start set geometry

/**
 * Retrieve information about the feature geometry
 */
private void setGeometry() {
    GeometryDescriptor geomDesc = featureSource.getSchema().getGeometryDescriptor();
    geometryAttributeName = geomDesc.getLocalName();

    Class<?> clazz = geomDesc.getType().getBinding();

    if (Polygon.class.isAssignableFrom(clazz) ||
                MultiPolygon.class.isAssignableFrom(clazz)) {
        geometryType = POLYGON;

    } else if (LineString.class.isAssignableFrom(clazz) ||
                       MultiLineString.class.isAssignableFrom(clazz)) {

        geometryType = LINE;

    } else {
        geometryType = POINT;
    }

}
// docs end set geometry

}


