package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */
public class ElementNode extends PreElement {

private int ElementPost;
private int ParentPre;
private int ParentPost;

/**
 * Creates a new instance of ElementNode
 */
public ElementNode(int DocumentID, int ElementPre, int ElementPost, String TagName, int ParentPre, int ParentPost) {
    super(DocumentID, ElementPre, TagName);
    this.ElementPost = ElementPost;
    this.ParentPre = ParentPre;
    this.ParentPost = ParentPost;
}

public int GetElementPost() {
    return ElementPost;
}

public int GetParentPre() {
    return ParentPre;
}

public int GetParentPost() {
    return ParentPost;
}
}