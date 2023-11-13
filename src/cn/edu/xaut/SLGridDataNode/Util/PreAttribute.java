package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class PreAttribute {

private int DocumentID;
private int AttributeID;
private int ElementPre;
private String AttributeName;
private String AttributeValue;


/** Creates a new instance of PreAttribute */
public PreAttribute(int DocumentID, int AttributeID, int ElementPre, String AttributeName, String AttributeValue) {
    this.DocumentID = DocumentID;
    this.AttributeID = AttributeID;
    this.ElementPre = ElementPre;
    this.AttributeName = AttributeName;
    this.AttributeValue = AttributeValue;
}

public int GetDocumentID() {
    return DocumentID;
}

public int GetAttributeID() {
    return AttributeID;
}

public int GetElementPre() {
    return ElementPre;
}

public String GetAttributeName() {
    return AttributeName;
}

public String GetAttributeValue() {
    return AttributeValue;
}
}
