/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */


public class VectorTextNode extends VectorTextPre {

private int VectorElementPost;

public VectorTextNode(int DocumentID, int TextID, int VectorElementPre, int VectorElementPost, String TextContent) {
    super(DocumentID, TextID, VectorElementPre, TextContent);
    this.VectorElementPost = VectorElementPost;
}

public int GetVecotrElementPost() {
    return VectorElementPost;
}

}
