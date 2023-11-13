/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/*
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.*;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class GenerateXML {
private String DocumentName;
private int DocumentID;
private String XMLString;
private String NamespaceSQL;
private Namespace AttributeNamespace;

/**
 * Creates a new instance of GenerateXML
 */
public GenerateXML(String DocumentName) {
    this.DocumentName = DocumentName;
}

public GenerateXML() {
}

public String GetDocumentName() {
    return DocumentName;
}

private void GetDocumentID() {
    String SQLString = "SELECT documentid FROM document WHERE documentname = '" + this.GetDocumentName() + "'";
    ResultSet IDSet = null;
    try {
        //IDSet = new DBConnection().ExecutetestSQL(SQLString);
        IDSet = new DBConnection().ExecuteSQL(SQLString);
        while (IDSet.next()) {
            DocumentID = IDSet.getInt(1);
            System.out.println(DocumentID);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

private void SetAttributeandTextintoelement(Element tempelement, String elementprevectorencode, Namespace namespace) {
    String AttributeSQL = "SELECT elementprevectorencode, elementpostvectorencode, attributename, attributevalue, attributeprefix FROM attribute WHERE elementprevectorencode='" + elementprevectorencode + "' AND documentid=" + DocumentID;
    String TextSQL = "SELECT elementprevectorencode, elementpostvectorencode, textvalue FROM text WHERE elementprevectorencode='" + elementprevectorencode + "' AND documentid=" + DocumentID;


    try {
        //ResultSet rsT= new DBConnection().ExecutetestSQL(TextSQL);
        //ResultSet rsA = new DBConnection().ExecutetestSQL(AttributeSQL);
        ResultSet rsA = new DBConnection().ExecuteSQL(AttributeSQL);
        ResultSet rsT = new DBConnection().ExecuteSQL(TextSQL);
        while (rsA.next()) {
            AttributeVectorDietzEncodingBean AttributeBean = new AttributeVectorDietzEncodingBean(DocumentID, rsA.getString(3), rsA.getString(4), rsA.getString(1), rsA.getString(5), rsA.getString(2));
            Attribute attribute = new Attribute(AttributeBean.GetAttributeName(), AttributeBean.GetAttributeValue(), namespace);
            tempelement.setAttribute(attribute);
        }
        while (rsT.next()) {
            TextVectorDietzEncodingBean TempText = new TextVectorDietzEncodingBean(DocumentID, rsT.getString(1), rsT.getString(3), rsT.getString(2));
            tempelement.setText(TempText.getTextValue());
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

private Namespace GetAttributeNamespace(Element element) {
    if (element.isRootElement()) {
        NamespaceSQL = "SELECT DISTINCT(attribute.attributeprefix), namespace.uri FROM attribute, namespace WHERE attribute.documentid = namespace.documentid AND attribute.attributeprefix = namespace.prefix AND attribute.attributeprefix LIKE 'xsi' AND attribute.documentid = '" + DocumentID + "'";
    } else {
        NamespaceSQL = "SELECT DISTINCT(attribute.attributeprefix), namespace.uri FROM attribute, namespace WHERE attribute.documentid = namespace.documentid AND attribute.attributeprefix = namespace.prefix AND attribute.attributeprefix LIKE 'gml' AND attribute.documentid = '" + DocumentID + "'";
    }
    try {
        //String  SQL = "USE gmldatabase SELECT DISTINCT(attribute.attributeprefix), namespace.uri FROM attribute, namespace WHERE attribute.documentid = namespace.documentid AND attribute.attributeprefix = namespace.prefix AND attribute.attributeprefix LIKE 'xsi' AND attribute.documentid = '"+DocumentID+"'";
        //ResultSet rs = new DBConnection().ExecutetestSQL(NamespaceSQL);
        ResultSet rs = new DBConnection().ExecuteSQL(NamespaceSQL);
        while (rs.next()) {
            AttributePrefixBean attributeprefixbeans = new AttributePrefixBean(rs.getString(1), rs.getString(2));
            AttributeNamespace = Namespace.getNamespace(attributeprefixbeans.GetAttributePrefix(), attributeprefixbeans.GetAttributeURI());
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return AttributeNamespace;
}

private void ProcessVectorNameSpacedata(Element element) {
    String SQL = "SELECT prefix, uri, namespacemark from namespace where namespacemark = 0 AND documentid= " + DocumentID;
    ArrayList<NameSpaceVectorEncodeBean> namespacelist = new ArrayList<NameSpaceVectorEncodeBean>();
    try {
        //ResultSet rs = new DBConnection().ExecutetestSQL(SQL);
        ResultSet rs = new DBConnection().ExecuteSQL(SQL);
        while (rs.next()) {
            NameSpaceVectorEncodeBean additionalnamespace = new NameSpaceVectorEncodeBean(DocumentID, rs.getString(1), rs.getString(2), rs.getInt(3));
            namespacelist.add(additionalnamespace);
            for (int nt = 0; nt < namespacelist.size(); nt++) {
                element.addNamespaceDeclaration(Namespace.getNamespace(namespacelist.get(nt).getPrefix(), namespacelist.get(nt).getUri()));
            }
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

private ArrayList<ElementVectorDietzEncodingwithNamespaceBean> ProcessVectorElementData(String SQL) {
    String SQLString = SQL;
    ArrayList<ElementVectorDietzEncodingwithNamespaceBean> ElementList = new ArrayList<ElementVectorDietzEncodingwithNamespaceBean>();
    try {
        //ResultSet rs = new DBConnection().ExecutetestSQL(SQLString);
        ResultSet rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            ElementVectorDietzEncodingwithNamespaceBean TempNode = new ElementVectorDietzEncodingwithNamespaceBean(DocumentID, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            ElementList.add(TempNode);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return ElementList;
}

public String ConstructXML() {
    this.GetDocumentID();
    String RootSQL = "SELECT element.elementprevectorencode, element.elementpostvectorencode, element.elementtag, element.elementparentprevectorencode, element.elementparentpostvectorencode, namespace.prefix, namespace.uri FROM element, namespace WHERE element.elementprevectorencode= '[1,0]' AND element.documentid = namespace.documentid AND namespace.namespacemark = 1 AND element.documentid= " + DocumentID;
    ArrayList<ElementVectorDietzEncodingwithNamespaceBean> TopList = new ArrayList<ElementVectorDietzEncodingwithNamespaceBean>();
    TopList = ProcessVectorElementData(RootSQL);
    String RootName = TopList.get(0).getElementtag();
    Element root = new Element(RootName, TopList.get(0).getElementprefix(), TopList.get(0).getUri());
    AttributeNamespace = this.GetAttributeNamespace(root);
    this.ProcessVectorNameSpacedata(root);
    Document XMLDocument = new Document(root);
    String RootPre = TopList.get(0).getElementprevectorencode();
    System.out.println(RootPre);
    this.SetAttributeandTextintoelement(root, RootPre, AttributeNamespace);
    this.RecursiveConstruct(root, RootPre);
    XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()); //格式化输出，产生缩进和换行
    Format format = outp.getFormat();
    format.setEncoding("UTF-8"); //设置语言
    format.setExpandEmptyElements(true); //设置输出空元素格式
    outp.setFormat(format);
    try {
        XMLString = outp.outputString(XMLDocument);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    } //输出XML文档
    return XMLString;
}

private void RecursiveConstruct(Element TempElem, String elementprevectorencode) {
    String ChildSQL = "SELECT element.elementprevectorencode, element.elementpostvectorencode, element.elementtag, element.elementparentprevectorencode, element.elementparentpostvectorencode, namespace.prefix, namespace.uri FROM namespace INNER JOIN element ON (element.elementparentprevectorencode='" + elementprevectorencode + "' AND element.documentid = namespace.documentid AND element.elementprefix = namespace.prefix AND namespace.documentid=" + DocumentID + ")";
    ArrayList<ElementVectorDietzEncodingwithNamespaceBean> TNList = ProcessVectorElementData(ChildSQL);
    Iterator TempListIterator = TNList.iterator();
    while (TempListIterator.hasNext()) {
        ElementVectorDietzEncodingwithNamespaceBean childelementvectorbean = (ElementVectorDietzEncodingwithNamespaceBean) TempListIterator.next();
        Element ChildElement = new Element(childelementvectorbean.getElementtag(), childelementvectorbean.getElementprefix(), childelementvectorbean.getUri());
        this.SetAttributeandTextintoelement(ChildElement, childelementvectorbean.getElementprevectorencode(), AttributeNamespace);
        TempElem.addContent(ChildElement);
        String tempelementprevectorencode = childelementvectorbean.getElementprevectorencode();
        RecursiveConstruct(ChildElement, tempelementprevectorencode);
    }
}

//public static void main(String args[]){
//      String XMLString = new GenerateXML("example.xml").ConstructXML();
//      System.out.println(XMLString);
// }
}
