/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author He Ting
 */
public class ElementVectorDietzEncodingwithNamespaceBean {
private int documentid;
private String elementprevectorencode;
private String elementpostvectorencode;
private String elementtag;
private String elementparentprevectorencode;
private String elementparentpostvectorencode;
private String elementprefix;
private String uri;

public ElementVectorDietzEncodingwithNamespaceBean(int documentid, String elementprevectorencode, String elementpostvectorencode, String elementtag, String elementparentprevectorencode, String elementparentpostvectorencode, String elementprefix, String uri) {
    this.documentid = documentid;
    this.elementprevectorencode = elementprevectorencode;
    this.elementpostvectorencode = elementpostvectorencode;
    this.elementtag = elementtag;
    this.elementparentprevectorencode = elementparentprevectorencode;
    this.elementparentpostvectorencode = elementparentpostvectorencode;
    this.elementprefix = elementprefix;
    this.uri = uri;
}

public int getDocumentID() {
    return documentid;
}

public String getElementprevectorencode() {
    return elementprevectorencode;
}

public String getElementpostvectorencode() {
    return elementpostvectorencode;
}

public String getElementtag() {
    return elementtag;
}

public String getElementparentprevectorencode() {
    return elementparentprevectorencode;
}

public String getElementparentpostvectorencode() {
    return elementparentpostvectorencode;
}

public String getElementprefix() {
    return elementprefix;
}

public String getUri() {
    return uri;
}
}
