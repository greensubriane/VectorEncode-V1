package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */
public class PreText {
private int DocumentID;
private int TextID;
private int ElementPre;
private String TextContent;

/**
 * Creates a new instance of PreText
 */
public PreText(int DocumentID, int TextID, int ElementPre, String TextContent) {
    this.DocumentID = DocumentID;
    this.TextID = TextID;
    this.ElementPre = ElementPre;
    this.TextContent = TextContent;
}

public int GetDocumentID() {
    return DocumentID;
}

public int GetTextID() {
    return TextID;
}

public int GetElementPre() {
    return ElementPre;
}

public String GetTextContent() {
    return TextContent;
}
}

