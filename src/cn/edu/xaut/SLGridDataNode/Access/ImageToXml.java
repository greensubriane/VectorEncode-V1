/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author greensubmarine
 */

import org.dom4j.Document;
import org.dom4j.DocumentException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageToXml {
private String ix = null;// image or xml file name

public ImageToXml(String imageOrXml) {
    this.ix = imageOrXml;
}

private String readImage() {
    BufferedInputStream bis = null;
    byte[] bytes = null;
    try {
        try {
            bis = new BufferedInputStream(new FileInputStream(ix));
            bytes = new byte[bis.available()];
            bis.read(bytes);
        } finally {
            bis.close();
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return new BASE64Encoder().encodeBuffer(bytes);
}

public void imageToXml() {
    String xml = "" + "<image>" + "<name>" + ix + "</name>" + "<content>"
                         + readImage() + "</content></image>";
    try {
        XMLHelper.write(XMLHelper.parseText(xml), ix + ".xml");
    } catch (DocumentException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void xmlToImage(String rename) {
    Document d;
    String name = null;
    String content = null;
    try {
        d = XMLHelper.parse(ix);
        name = XMLHelper.getNodeValue(d, "/image/name");
        content = XMLHelper.getNodeValue(d, "/image/content");
        saveImage(rename.equals("") ? name : rename, content);
    } catch (DocumentException e) {
        e.printStackTrace();
    }
}

public void xmlToImage() {
    xmlToImage("");
}

private void saveImage(String filename, String content) {
    try {
        DataOutputStream dos = null;
        try {
            byte[] bs = new BASE64Decoder().decodeBuffer(content);
            dos = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(filename)));
            dos.write(bs);
        } finally {
            dos.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void main(String args[]) {
    ImageToXml itx = new ImageToXml("D:\\3.jpg");
    System.out.println(itx.readImage());
    itx.imageToXml();
    ImageToXml itx1 = new ImageToXml("D:\\3.jpg.xml");
    itx1.xmlToImage("D:\\4.jpg");
}

}
