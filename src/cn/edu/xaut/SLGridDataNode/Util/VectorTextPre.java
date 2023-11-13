/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorTextPre {
private int DocID;
private int TextID;
private int VectorElementPre;
private String TextContent;

public VectorTextPre(int DocID, int TextID, int VectorElementPre, String TextContent) {
    this.DocID = DocID;
    this.TextID = TextID;
    this.VectorElementPre = VectorElementPre;
    this.TextContent = TextContent;
}

public int GetDocID() {
    return DocID;
}

public int GetTextID() {
    return TextID;
}

public int GetVectorElementPre() {
    return VectorElementPre;
}

public String GetTextContent() {
    return TextContent;
}
}
