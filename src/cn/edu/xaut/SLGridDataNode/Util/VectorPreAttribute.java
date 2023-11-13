/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmariane
 */
public class VectorPreAttribute {
private int AttributeID;
private int DocID;
private String VectorPreElement;
private String AttributeName;
private String AttributeValue;

public VectorPreAttribute(int AttributeID, int DocID, String VectorPreElement, String AttributeName, String AttributeValue) {
    this.AttributeID = AttributeID;
    this.DocID = DocID;
    this.VectorPreElement = VectorPreElement;
    this.AttributeName = AttributeName;
    this.AttributeValue = AttributeValue;
}

public int GetAttributeID() {
    return AttributeID;
}

public int GetDocID() {
    return DocID;
}

public String GetVectorPreElement() {
    return VectorPreElement;
}

public String GetAttributeName() {
    return AttributeName;
}

public String GetAttibuteValue() {
    return AttributeValue;
}

}
