/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorElementNode extends VectorElementPre {

private String VectorElementPost;
private String VectorParentPre;
private String VectorParentPost;

public VectorElementNode(int DocID, String VectorElementPre, String TagName, String VectorElementPost, String VectorParentPre, String VectorParentPost) {
    super(DocID, VectorElementPre, TagName);
    this.VectorElementPost = VectorElementPost;
    this.VectorParentPre = VectorParentPre;
    this.VectorParentPost = VectorParentPost;
}

public String GetVectorElementPost() {
    return VectorElementPost;
}

public String GetVectorParentPre() {
    return VectorParentPre;
}

public String GetVectorParentPost() {
    return VectorParentPost;
}
}
