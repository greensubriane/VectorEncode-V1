/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorAttributeNode extends VectorAttributePre {
private int VectorElementPost;

public VectorAttributeNode(int DocID, int AttributeID, int VectorElementPre, String AttributeValue, String AttributeName) {
    super(DocID, AttributeID, VectorElementPre, AttributeValue, AttributeName);
    this.VectorElementPost = VectorElementPost;
}

public int GetVectorElementPost() {
    return VectorElementPost;
}
}
