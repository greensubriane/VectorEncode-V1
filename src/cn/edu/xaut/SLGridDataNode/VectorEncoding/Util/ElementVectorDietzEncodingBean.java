/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class ElementVectorDietzEncodingBean {

private int DocID;
private String PreElementVectorDietzEncoding;
private String PostElementVectorDietzEncoding;
private String ElementName;
private String ElementPrefix;
private String PreParentElementVectorDietzEncoding;
private String PostParentElementVectorDietzEncoding;

public ElementVectorDietzEncodingBean(int DocID, String PreElementVectorDietzEncoding, String PostElementVectorDietzEncoding, String ElementName, String PreParentElementVectorDietzEncoding, String PostParentElementVectorDietzEncoding, String ElementPrefix) {
    this.DocID = DocID;
    this.ElementName = ElementName;
    this.PreElementVectorDietzEncoding = PreElementVectorDietzEncoding;
    this.PostElementVectorDietzEncoding = PostElementVectorDietzEncoding;
    this.PreParentElementVectorDietzEncoding = PreParentElementVectorDietzEncoding;
    this.PostParentElementVectorDietzEncoding = PostParentElementVectorDietzEncoding;
    this.ElementPrefix = ElementPrefix;
}

public int GetDocID() {
    return DocID;
}

public String getElementsName() {
    return ElementName;
}

public String GetPreElementVectorDietzEncoding() {
    return PreElementVectorDietzEncoding;
}

public String GetPostElementVectorDietzEncoding() {
    return PostElementVectorDietzEncoding;
}

public String GetPreParentElementVectorDietzEncoding() {
    return PreParentElementVectorDietzEncoding;
}

public String GetPostParentElementVectorDietzEncoding() {
    return PostParentElementVectorDietzEncoding;
}

public String getElementPrefix() {
    return ElementPrefix;
}
}
