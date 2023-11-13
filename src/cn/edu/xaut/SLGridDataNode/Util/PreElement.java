package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class PreElement {

private int DocumentID;
private int ElementPre;
private String TagName;

/** Creates a new instance of PreElement */
public PreElement(int DocumentID, int ElementPre, String TagName) {
    this.DocumentID = DocumentID;
    this.ElementPre = ElementPre;
    this.TagName = TagName;
}

public int GetElementPre() {
    return ElementPre;
}

public int GetDocumentID() {
    return DocumentID;
}

public String GetTagName() {
    return TagName;
}
}
