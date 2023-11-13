/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorElementResult extends VectorElementPre {
private String VectorElementPost;

public VectorElementResult(int DocID, String VectorElementPre, String TagName, String VectorElementPost) {
    super(DocID, VectorElementPre, TagName);
    this.VectorElementPost = VectorElementPost;
}

public String GetVectorElementPost() {
    return VectorElementPost;
}

}
