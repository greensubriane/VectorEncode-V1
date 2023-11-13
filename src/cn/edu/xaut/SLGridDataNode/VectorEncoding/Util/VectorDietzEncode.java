/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubriane
 */
public class VectorDietzEncode {
private String VectorPreOrder;
private String VectorPostOrder;

public VectorDietzEncode(String VectorPreOrder, String VectorPostOrder) {
    this.VectorPostOrder = VectorPostOrder;
    this.VectorPreOrder = VectorPreOrder;
}

public String GetVectorPredOrder() {
    return VectorPreOrder;
}

public String GetVectorPostOrder() {
    return VectorPostOrder;
}
}
