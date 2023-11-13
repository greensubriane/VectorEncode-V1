/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/*
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.AttributeNode;
import cn.edu.xaut.SLGridDataNode.Util.ElementNode;
import cn.edu.xaut.SLGridDataNode.Util.TextNode;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GenerateXMLs {

private String DocumentName;
private int DocumentID;
private String XMLString;

/**
 * Creates a new instance of GenerateXML
 */
public GenerateXMLs(String DocumentName) {
    this.DocumentName = DocumentName;
}

public GenerateXMLs() {

}

public String GetDocumentName() {
    return DocumentName;
}

private void GetDocumentID() {
    String SQLString = "USE gmldatabase SELECT documentid FROM document WHERE documentname = " + "'" + this.GetDocumentName() + "'";
    ResultSet IDSet = null;
    //int DocumentID;
    try {
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

private ArrayList<ElementNode> ProcessData(String SQL) {
    ResultSet rs = null;
    ArrayList<ElementNode> ElementList = new ArrayList<ElementNode>();
    String SQLString = SQL;
    try {
        rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            ElementNode TempNode = new ElementNode(DocumentID, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
            ElementList.add(TempNode);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return ElementList;
}

private ArrayList<TextNode> ProcessText(int TEPre) {
    ArrayList<TextNode> TextList = new ArrayList<TextNode>();
    String TextSQL = "USE gmldatabase SELECT textid, elementpre, elementpost, textcontent FROM text WHERE elementpre=" + TEPre + " AND documentid=" + DocumentID;
    ResultSet rsT = null;
    try {
        rsT = new DBConnection().ExecuteSQL(TextSQL);
        while (rsT.next()) {
            TextNode TempText = new TextNode(DocumentID, rsT.getInt(1), rsT.getInt(2), rsT.getInt(3), rsT.getString(4));
            TextList.add(TempText);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return TextList;
}

private ArrayList<AttributeNode> ProcessAttribute(int AEPre) {
    ArrayList<AttributeNode> AttributeList = new ArrayList<AttributeNode>();
    String AttributeSQL = "USE gmldatabase SELECT attributeid, elementpre, elementpost, attributename, attributevalue FROM attribute WHERE elementpre=" + AEPre + " AND documentid=" + DocumentID;
    ResultSet rsA = null;
    try {
        rsA = new DBConnection().ExecuteSQL(AttributeSQL);
        while (rsA.next()) {
            AttributeNode TempAttribute = new AttributeNode(DocumentID, rsA.getInt(1), rsA.getInt(2), rsA.getInt(3), rsA.getString(4), rsA.getString(5));
            AttributeList.add(TempAttribute);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return AttributeList;
}

public String ConstructXML() {
    this.GetDocumentID();
    String RootSQL = "USE gmldatabase SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE elementpre=1 AND documentid=" + DocumentID;
    ArrayList<ElementNode> TopList = new ArrayList<ElementNode>();
    TopList = ProcessData(RootSQL);
    String RootName = TopList.get(0).GetTagName();
    Element RootElement = new Element(RootName);
    int RootPre = TopList.get(0).GetElementPre();
    Document XMLDocument = new Document(RootElement);
    ArrayList<AttributeNode> AList1 = new ArrayList<AttributeNode>();
    ArrayList<TextNode> TList1 = new ArrayList<TextNode>();
    AList1 = ProcessAttribute(RootPre);
    TList1 = ProcessText(RootPre);
    for (int A1 = 0; A1 < AList1.size(); A1++) {
        RootElement.setAttribute(AList1.get(A1).GetAttributeName(), AList1.get(A1).GetAttributeValue());
    }
    for (int T1 = 0; T1 < TList1.size(); T1++) {
        RootElement.setText(TList1.get(T1).GetTextContent());
    }
    String SecondSQL = "USE gmldatabase SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE parentpre=" + RootPre + " AND documentid=" + DocumentID;
    ArrayList<ElementNode> SecondList = new ArrayList<ElementNode>();
    SecondList = ProcessData(SecondSQL);
    for (int x = 0; x < SecondList.size(); x++) {
        ArrayList<AttributeNode> AList3 = new ArrayList<AttributeNode>();
        ArrayList<TextNode> TList3 = new ArrayList<TextNode>();
        Element ParentElement = new Element(SecondList.get(x).GetTagName());
        AList3 = ProcessAttribute(SecondList.get(x).GetElementPre());
        TList3 = ProcessText(SecondList.get(x).GetElementPre());
        for (int A3 = 0; A3 < AList3.size(); A3++) {
            ParentElement.setAttribute(AList3.get(A3).GetAttributeName(), AList3.get(A3).GetAttributeValue());
        }
        for (int T3 = 0; T3 < TList3.size(); T3++) {
            String TValue = TList3.get(T3).GetTextContent();
            ParentElement.setText(TValue);
        }
        String LowSQL = "USE gmldatabase SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE parentpre=" + SecondList.get(x).GetElementPre() + " AND documentid=" + DocumentID;
        ArrayList<ElementNode> ChildList = new ArrayList<ElementNode>();
        ChildList = ProcessData(LowSQL);
        Element SecondLevel = RecursiveConstruct(ParentElement, ChildList);
        XMLDocument.getRootElement().addContent(SecondLevel);
    }
    XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()); //格式化输出，产生缩进和换行

    Format format = outp.getFormat();
    format.setEncoding("UTF-8"); //设置语言
    format.setExpandEmptyElements(true); //设置输出空元素格式
    outp.setFormat(format);
    try {
        //outp.output(XMLDocument, new FileOutputStream("GenerateXML.XML"));
        //System.out.println(outp.outputString(XMLDocument));
        XMLString = outp.outputString(XMLDocument);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    } //输出XML文档
    //System.out.print(XMLString);
    return XMLString;
}

private Element RecursiveConstruct(Element TempElem, ArrayList<ElementNode> TempList) {
    Element ThisElem = TempElem;
    ArrayList<AttributeNode> AList2 = new ArrayList<AttributeNode>();
    ArrayList<TextNode> TList2 = new ArrayList<TextNode>();
    for (int m = 0; m < TempList.size(); m++) {
        Element ChildElement = new Element(TempList.get(m).GetTagName());
        AList2 = ProcessAttribute(TempList.get(m).GetElementPre());
        TList2 = ProcessText(TempList.get(m).GetElementPre());
        for (int A2 = 0; A2 < AList2.size(); A2++) {
            ChildElement.setAttribute(AList2.get(A2).GetAttributeName(), AList2.get(A2).GetAttributeValue());
        }
        for (int T2 = 0; T2 < TList2.size(); T2++) {
            String TValue = TList2.get(T2).GetTextContent();
            ChildElement.setText(TValue);
        }
        String ChildSQL = "USE gmldatabase SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE parentpre=" + TempList.get(m).GetElementPre() + " AND documentid=" + DocumentID;
        ArrayList<ElementNode> TNList = new ArrayList<ElementNode>();
        TNList = ProcessData(ChildSQL);
        ThisElem.addContent(RecursiveConstruct(ChildElement, TNList));
    }
    return ThisElem;
}

   /* public static void main(String[] args) {
        GenerateXML generate = new GenerateXML("Issue.xml");
        generate.ConstructXML();
    }*/
}
