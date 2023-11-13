/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author Administrator
 */
public class RainFallCurveBean {
public static String STCD;
public static String StartDate;
public static String EndDate;

public static void SetSTCD(String stcd) {
    STCD = stcd;
}

public static void SetStartDate(String startdate) {
    StartDate = startdate;
}

public static void SetEndDate(String enddate) {
    EndDate = enddate;
}

public static String GetSTCD() {
    return STCD;
}

public static String GetstartDate() {
    return StartDate;
}

public static String GetEndDate() {
    return EndDate;
}

}
