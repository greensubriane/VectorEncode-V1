/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class XMLFileBean {

private static String XMLFileName;
private static String OriginalXMLFileName;
private static String newxmlfilename;

public static String Getnewxmlfile() {
    return newxmlfilename;
}

public static void Setnewxmlfile(String newxmlfilename) {
    XMLFileBean.newxmlfilename = newxmlfilename;
}

public static String GetXMLFile() {
    return XMLFileName;
}

public static void SetXMLFile(String XMLFileName) {
    XMLFileBean.XMLFileName = XMLFileName;
}

public static String GetOriginalXMLFileName() {
    return OriginalXMLFileName;
}

public static void SetOriginalXMLFileName(String OriginalXMLFileName) {
    XMLFileBean.OriginalXMLFileName = OriginalXMLFileName;
}

}
