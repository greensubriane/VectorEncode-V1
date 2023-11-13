package cn.edu.xaut.SLGridDataNode.Access;

import cn.edu.xaut.SLGridDataNode.DBConnectionPool.DBPool;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.GenerateAttributesVectorEncoding;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.GenerateElementsVectorEncoding;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.GenerateTextVectorEncoding;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributeVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.ElementVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.NameSpaceVectorEncodeBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Greensubmarine
 */
public class StartProcess {

String FileName;

public StartProcess() {

}

public boolean StartProcess(URL FilePath) {
    String[] ThisList = FilePath.getFile().split("/");
    int iList = ThisList.length;
    FileName = ThisList[iList - 1].toString();
    boolean flags;
    java.util.Date date = new java.util.Date();
    String TimeString = new Timestamp(date.getTime()).toString();
    String InsertDocuments = "INSERT INTO document(documentname, stortedtime, storagedpath) VALUES ('" + FileName + "'" + ", " + "'" + TimeString + "'" + ", " + "'" + FilePath.toString() + "'" + ")";
    try {
        new DBConnection().ExecuteInsert(InsertDocuments);
        System.out.println(InsertDocuments);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    int DocumentID = GetDocumentID();
    System.out.println(DocumentID);
    flags = InsertVectordata(DocumentID, FileName);
    if (flags == true) {
        System.out.println("true");
    } else {
        System.out.println("false");
    }
    DBPool.ShutdownPool();
    return flags;
}

public int GetDocumentID() {
    ResultSet rs = null;
    int DocID = 0;
    String SQLStrings = "SELECT documentid FROM document WHERE documentname= '" + FileName + "'";
    try {
        rs = new DBConnection().ExecuteSQL(SQLStrings);
        while (rs.next()) {
            DocID = rs.getInt(1);
        }
        return DocID;
    } catch (Exception e) {
        System.err.println(e.getMessage());
        return -1;
    }
}

private ArrayList<ElementVectorDietzEncodingBean> MakeElementMapInfo(int DocID, String FName) {
    ArrayList<ElementVectorDietzEncodingBean> elementvectorencodelist;
    elementvectorencodelist = new GenerateElementsVectorEncoding().getElementBean(DocID, FName);
    return elementvectorencodelist;
}

private ArrayList<AttributeVectorDietzEncodingBean> MakeAttributeMapInfo(int DocID, String FName) {
    ArrayList<AttributeVectorDietzEncodingBean> attributevectorencodelist;
    attributevectorencodelist = new GenerateAttributesVectorEncoding().getAttributeBean(DocID, FName);
    return attributevectorencodelist;
}

private ArrayList<TextVectorDietzEncodingBean> MakeTextMapInfo(int DocID, String FName) {
    ArrayList<TextVectorDietzEncodingBean> textvectorencodelist;
    textvectorencodelist = new GenerateTextVectorEncoding().getTextBean(DocID, FName);
    return textvectorencodelist;
}

private List<NameSpaceVectorEncodeBean> MakeNameSpacelist(int DocID, String FName) {
    List<NameSpaceVectorEncodeBean> xmlnamespacelist;
    xmlnamespacelist = new GenerateElementsVectorEncoding().getNameSpaces(DocID, FName);
    return xmlnamespacelist;
}

private boolean InsertVectordata(int DocID, String FName) {
    boolean flag;
    String InsertVectorelements, InsertVectorAttributes, InsertVectorTexts, InsertNameSpace;
    ArrayList<ElementVectorDietzEncodingBean> elementvectorencodelist = this.MakeElementMapInfo(DocID, FName);
    ArrayList<AttributeVectorDietzEncodingBean> attributevectorencodelist = this.MakeAttributeMapInfo(DocID, FName);
    ArrayList<TextVectorDietzEncodingBean> textvectorencodelist = this.MakeTextMapInfo(DocID, FName);
    List<NameSpaceVectorEncodeBean> xmlnamespacelist = this.MakeNameSpacelist(DocID, FName);
    for (int i = 0; i < xmlnamespacelist.size(); i++) {
        NameSpaceVectorEncodeBean namespacebean = xmlnamespacelist.get(i);
        InsertNameSpace = "INSERT INTO namespace VALUES (" + namespacebean.getDocumentID()
                                  + ", '" + namespacebean.getPrefix() + "', '" + namespacebean.getUri() + "', '" + namespacebean.getNamespaceMark() + "')";
        try {
            new DBConnection().ExecuteInsert(InsertNameSpace);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(InsertNameSpace);
    }
    for (int m = 0; m < elementvectorencodelist.size(); m++) {
        ElementVectorDietzEncodingBean elementbean = elementvectorencodelist.get(m);
        InsertVectorelements = "INSERT INTO element VALUES (" + elementbean.GetDocID()
                                       + ", '" + elementbean.GetPreElementVectorDietzEncoding()
                                       + "', '" + elementbean.GetPostElementVectorDietzEncoding()
                                       + "', '" + elementbean.getElementsName()
                                       + "', '" + elementbean.GetPreParentElementVectorDietzEncoding()
                                       + "', '" + elementbean.GetPostParentElementVectorDietzEncoding()
                                       + "', '" + elementbean.getElementPrefix() + "')";
        try {
            new DBConnection().ExecuteInsert(InsertVectorelements);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(InsertVectorelements);
    }
    for (int n = 0; n < attributevectorencodelist.size(); n++) {
        AttributeVectorDietzEncodingBean attributebean = attributevectorencodelist.get(n);
        InsertVectorAttributes = "INSERT INTO attribute VALUES (" + attributebean.GetDocID()
                                         + ", '" + attributebean.GetAttributeVectorPreDietzOrder()
                                         + "', '" + attributebean.GetAttributeVectorPostDietzOrder()
                                         + "', '" + attributebean.GetAttributeName()
                                         + "', '" + attributebean.GetAttributeValue()
                                         + "', '" + attributebean.GetAttributePrefix() + "')";
        try {
            new DBConnection().ExecuteInsert(InsertVectorAttributes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(InsertVectorAttributes);
    }
    for (int p = 0; p < textvectorencodelist.size(); p++) {
        TextVectorDietzEncodingBean textbean = textvectorencodelist.get(p);
        InsertVectorTexts = "INSERT INTO text VALUES (" + textbean.GetDocID()
                                    + ", '" + textbean.getTextPreVectorDietzEncode()
                                    + "', '" + textbean.getTextPostVectorDietzEncode()
                                    + "', '" + textbean.getTextValue() + "')";
        try {
            new DBConnection().ExecuteInsert(InsertVectorTexts);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println(InsertVectorTexts);
    }
    flag = true;
    return flag;
}

}


