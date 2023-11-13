/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class AttributeVectorDietzEncodingBean extends AttributePreVectorDietzEncodingBean {

String AttributeVectorPostDietzOrder;
//String AttributePrefix;

public AttributeVectorDietzEncodingBean(int DocID, String AttributeName, String AttributeValue, String AttributeVectorPreDietzOrder, String Attributeprefix, String AttributeVectorPostDietzOrder) {
    super(DocID, AttributeName, AttributeValue, AttributeVectorPreDietzOrder, Attributeprefix);
    this.AttributeVectorPostDietzOrder = AttributeVectorPostDietzOrder;
    //this.AttributePrefix = AttributePrefix;
}

public String GetAttributeVectorPostDietzOrder() {
    return AttributeVectorPostDietzOrder;
}
    
   /* public String GetAttributePrefix(){
        return AttributePrefix;
    }*/

}
