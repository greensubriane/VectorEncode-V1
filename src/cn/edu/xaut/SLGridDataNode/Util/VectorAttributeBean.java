/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorAttributeBean extends VectorPreAttribute {
private String VectorPostElement;

public VectorAttributeBean(int AttributeID, int DocID, String VectorPreElement, String AttributeName, String AttributeValue, String VectorPostelement) {
    super(AttributeID, DocID, VectorPreElement, AttributeName, AttributeValue);
    this.VectorPostElement = VectorPostElement;
}

public String GetVectorPostElement() {
    return VectorPostElement;
}
}
