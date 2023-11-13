/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;

import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;


public class shapepropertyfile {

public static void main(String args[]) {
    try {
        FileChannel in = new FileInputStream("E:\\SQL Spatial Tools\\shandong.dbf").getChannel();
        DbaseFileReader dr = new DbaseFileReader(in, true, Charset.forName("GBK"));
        DbaseFileHeader dh = dr.getHeader();

        int fields = dh.getNumFields();
        int columns = dh.getNumRecords();
        System.out.println(fields);
        System.out.println(columns);

        DbaseFileReader.Row row = dr.readRow();
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < fields; i++) {
                Object data = row.read(i);
                System.out.println(data);
            }
        }
        System.out.println();
        dr.close();


    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
