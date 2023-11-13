/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorTextBean extends VectorPreText {
private String VectorPostElement;

public VectorTextBean(int DocID, int TextID, String VectorPreElement, String TextContent, String VectorPostelement) {
    super(TextID, DocID, VectorPreElement, TextContent);
    this.VectorPostElement = VectorPostelement;
}

public String GetvectorPostElement() {
    return VectorPostElement;
}


}
