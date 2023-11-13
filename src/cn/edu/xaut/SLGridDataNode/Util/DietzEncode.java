package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �Greensubmarine��
 */
public class DietzEncode {

private int ParentPre;
private int ParentPost;

/**
 * Creates a new instance of DietzEncode
 */
public DietzEncode(int ParentPre, int ParentPost) {
    this.ParentPre = ParentPre;
    this.ParentPost = ParentPost;
}

public int GetParentPre() {
    return ParentPre;
}

public int GetParentPost() {
    return ParentPost;
}
}