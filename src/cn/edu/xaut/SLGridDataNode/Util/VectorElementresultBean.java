/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorElementresultBean extends VectorPreElement {
private String VectorPostElement;

public VectorElementresultBean(int DocID, String VectorPreElement, String ElementTag, String VectorPostElement) {
    super(DocID, VectorPreElement, ElementTag);
    this.VectorPostElement = VectorPostElement;
}

public String GetVectorPostElement() {
    return VectorPostElement;
}

}
