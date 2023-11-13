/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */
public class VectorAttributePre {
private int DocID;
private int AttributeID;
private int VectorElementPre;
private String AttributeName;
private String AttributeValue;

public VectorAttributePre(int DocID, int AttributeID, int VectorElementPre, String AttributeValue, String AttributeName) {
    this.DocID = DocID;
    this.AttributeID = AttributeID;
    this.VectorElementPre = VectorElementPre;
    this.AttributeName = AttributeName;
    this.AttributeValue = AttributeValue;
}

public int GetDocID() {
    return DocID;
}

public int GetAttributeID() {
    return AttributeID;
}

public int GetVectorElementPre() {
    return VectorElementPre;
}

public String GetAttributeValue() {
    return AttributeValue;
}

public String GetAttributeName() {
    return AttributeName;
}
}
