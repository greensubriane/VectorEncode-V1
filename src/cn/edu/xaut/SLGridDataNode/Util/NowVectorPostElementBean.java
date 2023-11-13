/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */

import java.util.ArrayList;

public class NowVectorPostElementBean {

private static ArrayList<int[]> nowvectorpostelement;

public static ArrayList<int[]> Getnowvectorpostelement() {
    return nowvectorpostelement;
}

public static void Setnowvectorpostelement(ArrayList<int[]> nowvectorpostelement) {
    NowVectorPostElementBean.nowvectorpostelement = nowvectorpostelement;
}

}
