/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class AttributePreVectorDietzEncodingBean {

private int DocID;
//private int AttributeID;
private String AttributeName;
private String AttributeValue;
private String AttributeVectorPreDietzOrder;
private String AttributePrefix;

public AttributePreVectorDietzEncodingBean(int DocID, String AttributeName, String AttributeValue, String AttributeVectorPreDietzOrder, String Atrributeprefix) {
    this.DocID = DocID;
    //this.AttributeID = AttributeID;
    this.AttributeName = AttributeName;
    this.AttributeValue = AttributeValue;
    this.AttributeVectorPreDietzOrder = AttributeVectorPreDietzOrder;
    this.AttributePrefix = Atrributeprefix;
}

/*public AttributeVectorDietzEncodingBean(String attributename, String attributevalue, int PreOrder) {
    throw new UnsupportedOperationException("Not yet implemented");
}*/
public int GetDocID() {
    return DocID;
}
     
     /*public int GetAttributeID(){
         return AttributeID;
     }*/

public String GetAttributeName() {
    return AttributeName;
}

public String GetAttributeValue() {
    return AttributeValue;
}

public String GetAttributeVectorPreDietzOrder() {
    return AttributeVectorPreDietzOrder;
}

public String GetAttributePrefix() {
    return AttributePrefix;
}
}
