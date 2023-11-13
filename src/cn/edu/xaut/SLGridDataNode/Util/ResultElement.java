package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class ResultElement extends PreElement {

private int DocumentID;
private int ElementPre;
private int ElementPost;
private String TagName;

/** Creates a new instance of ResultElement */
public ResultElement(int DocumentID, int ElementPre, String TagName, int ElementPost) {
    super(DocumentID, ElementPre, TagName);
    this.ElementPost = ElementPost;
}

public int GetResultElementPost() {
    return ElementPost;
}
}
