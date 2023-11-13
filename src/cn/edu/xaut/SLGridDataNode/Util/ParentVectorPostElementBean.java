/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */

import java.util.ArrayList;

public class ParentVectorPostElementBean {

private static ArrayList<int[]> parentvectorpostelement;

public static ArrayList<int[]> Getparentvectorpostelement() {
    return parentvectorpostelement;
}

public static void Setparentvectorpostelement(ArrayList<int[]> parentvectorpostelement) {
    ParentVectorPostElementBean.parentvectorpostelement = parentvectorpostelement;
}
}
