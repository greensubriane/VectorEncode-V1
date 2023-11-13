/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class TextVectorDietzEncodingBean extends TextPreVectorDietzEncodingBean {
private String TextPostVectorDietzEncode;

public TextVectorDietzEncodingBean(int DocID, String TextPreVectorDietzEncode, String TextValue, String TextPostVectorDietzEncode) {
    super(DocID, TextPreVectorDietzEncode, TextValue);
    this.TextPostVectorDietzEncode = TextPostVectorDietzEncode;
}

public String getTextPostVectorDietzEncode() {
    return TextPostVectorDietzEncode;
}
}
