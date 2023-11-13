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
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MapLayerTableDemo {

final JMapFrame mapFrame;

public static void main(String[] args) {
    MapLayerTableDemo demo = new MapLayerTableDemo();
    demo.runDemo();
}

private void runDemo() {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            mapFrame.setSize(800, 600);
            mapFrame.setVisible(true);
        }
    });
}

public MapLayerTableDemo() {
    mapFrame = new JMapFrame(new MapContext(), new StreamingRenderer());
    mapFrame.enableLayerTable(true);
    mapFrame.enableStatusBar(true);
    mapFrame.enableToolBar(true);

    initComponents();
}

private void initComponents() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menu = new JMenu("File");

    JMenuItem item = new JMenuItem("Open shapefile...");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openShapefile();
        }
    });

    menu.add(item);
    menuBar.add(menu);

    mapFrame.setJMenuBar(menuBar);
}

private void openShapefile() {
    File file = JFileDataStoreChooser.showOpenFile("shp", mapFrame);
    if (file != null) {
        try {
            FileDataStore dataStore =
                    FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource source = dataStore.getFeatureSource();
            Style style = SLD.createSimpleStyle(source.getSchema());
            MapContext map = mapFrame.getMapContext();
            Layer layer = new FeatureLayer(source, style);
            map.addLayer(layer);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
}
