package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * Created by IntelliJ IDEA.
 * User: HeTing
 * Date: 11-10-11
 * Time: 下午1:31
 * To change this template use File | Settings | File Templates.
 */

public class NameSpaceVectorEncodeBean {

int documentid;
String prefix;
String uri;
int namespaceid;
int namespacemark;

public NameSpaceVectorEncodeBean(int documentid, String prefix, String uri, int namespacemark) {
    this.documentid = documentid;
    this.prefix = prefix;
    this.uri = uri;
    this.namespacemark = namespacemark;
}

public int getDocumentID() {
    return documentid;
}

public String getPrefix() {
    return prefix;
}

public String getUri() {
    return uri;
}

public int getNamespaceMark() {
    return namespacemark;
}
}
