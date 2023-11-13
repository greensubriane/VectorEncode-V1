/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorPreText {

private int TextID;
private int DocID;
private String VectorPreElement;
private String TextContent;

public VectorPreText(int TextID, int DocID, String VectorPreElement, String TextContent) {
    this.TextID = TextID;
    this.DocID = DocID;
    this.VectorPreElement = VectorPreElement;
    this.TextContent = TextContent;
}

public int GetTextID() {
    return TextID;
}

public int GetDocID() {
    return DocID;
}

public String GetVectorPreElement() {
    return VectorPreElement;
}

public String GetTextContent() {
    return TextContent;
}
}
