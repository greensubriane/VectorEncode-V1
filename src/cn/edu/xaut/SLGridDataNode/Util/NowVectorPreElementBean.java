/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */

import java.util.ArrayList;

public class NowVectorPreElementBean {
private static ArrayList<int[]> nowvectorpreelement;

public static ArrayList<int[]> Getnowvectorpreelement() {
    return nowvectorpreelement;
}

public static void Setnowvectorpreelement(ArrayList<int[]> nowvectorpreelement) {
    NowVectorPreElementBean.nowvectorpreelement = nowvectorpreelement;
}

}
