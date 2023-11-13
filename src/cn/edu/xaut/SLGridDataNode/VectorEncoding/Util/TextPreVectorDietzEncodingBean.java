/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class TextPreVectorDietzEncodingBean {

private int DocID;
private String TextPreVectorDietzEncode;
private String TextValue;

public TextPreVectorDietzEncodingBean(int DocID, String TextPreVectorDietzEncode, String TextValue) {
    this.TextPreVectorDietzEncode = TextPreVectorDietzEncode;
    //this.TextName = TextName;
    this.TextValue = TextValue;
    this.DocID = DocID;
}

public int GetDocID() {
    return DocID;
}

public String getTextPreVectorDietzEncode() {
    return TextPreVectorDietzEncode;
}

public String getTextValue() {
    return TextValue;
}

}
