/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorElementPre {

private int DocID;
private String VectorElementPre;
private String TagName;

public VectorElementPre(int DocID, String VectorElementPre, String TagName) {
    this.DocID = DocID;
    this.VectorElementPre = VectorElementPre;
    this.TagName = TagName;
}

public int GetDocID() {
    return DocID;
}

public String GetVectorElementPre() {
    return VectorElementPre;
}

public String GetTagName() {
    return TagName;
}
}
