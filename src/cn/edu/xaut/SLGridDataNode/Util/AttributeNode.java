package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class AttributeNode extends PreAttribute {

private int ElementPost;

/** Creates a new instance of AttributeNode */
public AttributeNode(int DocumentID, int AttributeID, int ElementPre, int ElementPost, String AttributeName, String AttributeValue) {
    super(DocumentID, AttributeID, ElementPre, AttributeName, AttributeValue);
    this.ElementPost = ElementPost;
}

public int GetElementPost() {
    return ElementPost;
}

}
