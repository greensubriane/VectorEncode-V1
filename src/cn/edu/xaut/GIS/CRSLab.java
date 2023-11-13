/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;


/**
 * This is a visual example of changing the coordinate reference system of a feature layer.
 */
public class CRSLab {

private File sourceFile;
private SimpleFeatureSource featureSource;
private MapContext map;

public static void main(String[] args) throws Exception {
    CRSLab lab = new CRSLab();
    lab.displayShapefile();
}


private void displayShapefile() throws Exception {
    sourceFile = JFileDataStoreChooser.showOpenFile("shp", null);
    if (sourceFile == null) {
        return;
    }
    FileDataStore store = FileDataStoreFinder.getDataStore(sourceFile);
    featureSource = store.getFeatureSource();

    // Create a map context and add our shapefile to it
    map = new MapContext();
    Style style = SLD.createSimpleStyle(featureSource.getSchema());
    Layer layer = new FeatureLayer(featureSource, style);
    map.layers().add(layer);

    // Create a JMapFrame with custom toolbar buttons
    JMapFrame mapFrame = new JMapFrame((MapContext) map);
    mapFrame.enableToolBar(true);
    mapFrame.enableStatusBar(true);

    JToolBar toolbar = mapFrame.getToolBar();
    toolbar.addSeparator();
    toolbar.add(new JButton(new ValidateGeometryAction()));
    toolbar.add(new JButton(new ExportShapefileAction()));

    // Display the map frame. When it is closed the application will exit
    mapFrame.setSize(800, 600);
    mapFrame.setVisible(true);
}

class ValidateGeometryAction extends SafeAction {
    ValidateGeometryAction() {
        super("Validate geometry");
        putValue(Action.SHORT_DESCRIPTION, "Check each geometry");
    }

    public void action(ActionEvent e) throws Throwable {
        int numInvalid = validateFeatureGeometry(null);
        String msg;
        if (numInvalid == 0) {
            msg = "All feature geometries are valid";
        } else {
            msg = "Invalid geometries: " + numInvalid;
        }
        JOptionPane.showMessageDialog(null, msg, "Geometry results",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private int validateFeatureGeometry(ProgressListener progress) throws Exception {
        final SimpleFeatureCollection featureCollection = featureSource.getFeatures();

        // Rather than use an iterator, create a FeatureVisitor to check each fature
        class ValidationVisitor implements FeatureVisitor {
            public int numInvalidGeometries = 0;

            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom != null && !geom.isValid()) {
                    numInvalidGeometries++;
                    System.out.println("Invalid Geoemtry: " + feature.getID());
                }
            }
        }

        ValidationVisitor visitor = new ValidationVisitor();

        // Pass visitor and the progress bar to feature collection
        featureCollection.accepts(visitor, progress);
        return visitor.numInvalidGeometries;
    }

}

class ExportShapefileAction extends SafeAction {
    ExportShapefileAction() {
        super("Export...");
        putValue(Action.SHORT_DESCRIPTION, "Export using current crs");
    }

    public void action(ActionEvent e) throws Throwable {
        exportToShapefile();
    }

    private void exportToShapefile() throws Exception {
        SimpleFeatureType schema = featureSource.getSchema();
        JFileDataStoreChooser chooser = new JFileDataStoreChooser("shp");
        chooser.setDialogTitle("Save reprojected shapefile");
        chooser.setSaveFile(sourceFile);
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal != JFileDataStoreChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file.equals(sourceFile)) {
            JOptionPane.showMessageDialog(null, "Cannot replace " + file);
            return;
        }
    }

}
}

