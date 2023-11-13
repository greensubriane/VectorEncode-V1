/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author He Ting
 */
public class AttributePrefixBean {
private String AttributePrefix;
private String AttributeURI;

public AttributePrefixBean(String attributeprefix, String attributeuri) {
    this.AttributePrefix = attributeprefix;
    this.AttributeURI = attributeuri;
}

public String GetAttributePrefix() {
    return AttributePrefix;
}

public String GetAttributeURI() {
    return AttributeURI;
}

}
