/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.VectorEncode;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.NowVectorPostElementBean;
import cn.edu.xaut.SLGridDataNode.Util.NowVectorPreElementBean;
import cn.edu.xaut.SLGridDataNode.Util.ParentVectorPostElementBean;
import cn.edu.xaut.SLGridDataNode.Util.ParentVectorPreElementBean;

import java.util.ArrayList;

public class test {
public static void main() {

    ArrayList<int[]> nowprevectorelement = NowVectorPreElementBean.Getnowvectorpreelement();
    ArrayList<int[]> nowpostvectorelement = NowVectorPostElementBean.Getnowvectorpostelement();
    ArrayList<int[]> parentprevectorelement = ParentVectorPreElementBean.Getparentprevectorelement();
    ArrayList<int[]> parentpostvectorelement = ParentVectorPostElementBean.Getparentvectorpostelement();

    for (int i = 0; i < nowprevectorelement.size(); i++) {
        System.out.println("当前节点的前序向量编码：(" + nowprevectorelement.get(i)[0] + "," + nowprevectorelement.get(i)[1] + ") "

                                   + " 当前节点后序向量编码：(" + nowpostvectorelement.get(i)[0] + "," + nowpostvectorelement.get(i)[1] + ") "
                                   + " 父节点前序向量编码：(" + parentprevectorelement.get(i)[0] + "," + parentprevectorelement.get(i)[1] + ")"

                                   + " 父节点后序向量编码：("
                                   + parentpostvectorelement.get(i)[0] + "," + parentpostvectorelement.get(i)[1] + ")");
    }
}

}



