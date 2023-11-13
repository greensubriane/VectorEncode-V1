/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class XMLDocumentBean {
private int DocID;
private String DocName;
private String FilePath;
private String StorageTime;

public XMLDocumentBean(int DocID, String DocName, String FilePath, String StorageTime) {
    this.DocID = DocID;
    this.DocName = DocName;
    this.FilePath = FilePath;
    this.StorageTime = StorageTime;
}

public int getDocID() {
    return DocID;
}

public String getDocName() {
    return DocName;
}

public String getFilePath() {
    return FilePath;
}

public String getStorageTime() {
    return StorageTime;
}

}
