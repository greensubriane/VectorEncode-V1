/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */

import java.util.ArrayList;

public class ParentVectorPreElementBean {

private static ArrayList<int[]> parentprevectorelement;

public static ArrayList<int[]> Getparentprevectorelement() {
    return parentprevectorelement;
}

public static void Setparentprevectorelement(ArrayList<int[]> parentprevectorelement) {
    ParentVectorPreElementBean.parentprevectorelement = parentprevectorelement;
}

}
