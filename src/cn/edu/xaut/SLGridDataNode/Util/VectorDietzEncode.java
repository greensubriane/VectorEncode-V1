/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorDietzEncode {
private String VectorParentPre;
private String VectorParentPost;

public VectorDietzEncode(String VectorParentPre, String VectorParentPost) {
    this.VectorParentPre = VectorParentPre;
    this.VectorParentPost = VectorParentPost;
}

public String GetVectorParentPre() {
    return VectorParentPre;
}

public String GetVectorParentPost() {
    return VectorParentPost;
}
}
