package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class TextNode extends PreText {

private int ElementPost;

/** Creates a new instance of TextNode */
public TextNode(int DocumentID, int TextID, int ElementPre, int ElementPost, String TextContent) {
    super(DocumentID, TextID, ElementPre, TextContent);
    this.ElementPost = ElementPost;
}

public int GetElementPost() {
    return ElementPost;
}

}
