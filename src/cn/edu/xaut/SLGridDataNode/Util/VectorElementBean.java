/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorElementBean extends VectorPreElement {
private String VectorParentPreElement;
private String VectorParentPostElement;
private String VectorPostElement;

public VectorElementBean(int DocID, String VectorPreElement, String ElementTag, String VectorParentPreElement, String VectorParentPostElement, String VectorPostElement) {
    super(DocID, VectorPreElement, ElementTag);
    this.VectorParentPostElement = VectorParentPostElement;
    this.VectorParentPreElement = VectorParentPreElement;
    this.VectorPostElement = VectorPostElement;
}

public String VectorParentPostElement() {
    return VectorParentPostElement;
}

public String vectorParentPreElement() {
    return VectorParentPreElement;
}

public String VectorPostElement() {
    return VectorPostElement;
}
}
