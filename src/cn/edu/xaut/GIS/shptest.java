/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;


public class shptest {

public static void main(String[] args) {

    String strShpPath = "E:\\SQL Spatial Tools\\weihai.shp";
    File file = new File(strShpPath);
    ShapefileDataStore shpDataStore = null;

    try {
        shpDataStore = new ShapefileDataStore(file.toURL());
        shpDataStore.setStringCharset(Charset.forName("GBK"));
        String typeName = shpDataStore.getTypeNames()[0];
        FeatureSource featureSource = null;
        featureSource = (FeatureSource) shpDataStore.getFeatureSource(typeName);
        FeatureCollection result = featureSource.getFeatures();
        int counts = result.size();
        System.out.println(counts);
        FeatureIterator itertor = result.features();

        while (itertor.hasNext()) {
            SimpleFeature feature1 = (SimpleFeature) itertor.next();
            String id = feature1.getID();
            System.out.println(id);
            System.out.println(feature1.getFeatureType());
            String attribute1 = feature1.getAttribute(1).toString();
            System.out.println(attribute1);
            String attribute2 = feature1.getAttribute(2).toString();
            System.out.println(attribute2);
            String attribute3 = feature1.getAttribute(3).toString();
            System.out.println(attribute3);
            String attribute4 = feature1.getAttribute(4).toString();
            System.out.println(attribute4);
            String attribute5 = feature1.getAttribute(5).toString();
            System.out.println(attribute5);
            String attribute6 = feature1.getAttribute(6).toString();
            System.out.println(attribute6);
            String attribute7 = feature1.getAttribute(7).toString();
            System.out.println(attribute7);
            //Geometry geometry = (Geometry)feature.getDefaultGeometry();
            Object geometry = feature1.getDefaultGeometry();
            System.out.println(geometry.toString());
        }
        itertor.close();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

