/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.dao;

/**
 * @author greensubmarine
 */

import java.io.UnsupportedEncodingException;

public class StringTrans {

public static String tranA(String chi) {
    String result = null;
    byte temp[];
    try {
        temp = chi.getBytes("GBK");
        result = new String(temp);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return result;
}

public static String transB(String chB) {

    String result = null;
    byte temp[];
    try {
        temp = chB.getBytes("GBK");
        result = new String(temp, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return result;
}

public static String tranC(String chB) {
    String result = null;
    byte temp[];
    try {
        temp = chB.getBytes("ISO8859-1");
        result = new String(temp, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
}
