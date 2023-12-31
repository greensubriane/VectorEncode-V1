/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

import java.io.File;


/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 * <p>
 * This is the GeoTools Quickstart application used in documentationa and tutorials. *
 */
public class Quickstart {

/**
 * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
 * contents on the screen in a map frame
 */
public static void main(String[] args) throws Exception {
    // display a data store file chooser dialog for shapefiles
    File file = JFileDataStoreChooser.showOpenFile("shp", null);
    if (file == null) {
        return;
    }

    FileDataStore store = FileDataStoreFinder.getDataStore(file);
    SimpleFeatureSource featureSource = store.getFeatureSource();

    // Create a map content and add our shapefile to it
    MapContext map = new MapContext();
    map.setTitle("Quickstart");

    Style style = SLD.createSimpleStyle(featureSource.getSchema());
    Layer layer = new FeatureLayer(featureSource, style);
    map.addLayer(layer);

    // Now display the map
    JMapFrame.showMap(map);
}

}
