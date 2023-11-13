/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorPreElement {

private int DocID;
private String VectorPreElement;
private String ElementTag;

public VectorPreElement(int DocID, String VectorPreElement, String TagName) {
    this.DocID = DocID;
    this.VectorPreElement = VectorPreElement;
    this.ElementTag = ElementTag;
}

public int GetDocID() {
    return DocID;
}

public String GetVectorPreElement() {
    return VectorPreElement;
}

public String GetElementTag() {
    return ElementTag;
}


}
