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
import cn.edu.xaut.SLGridDataNode.Util.ResultElement;
import cn.edu.xaut.SLGridDataNode.Util.TextNode;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisplayResult {
private ArrayList<ResultElement> ResultList = new ArrayList<ResultElement>();
private String XMLString;

/*create a new instance of DisplayResult*/
public DisplayResult(ArrayList<ResultElement> ResultList) {
    this.ResultList = ResultList;
}

private int GetDocID() {
    int DocID = ResultList.get(0).GetDocumentID();
    return DocID;
}

private ArrayList ProcessData(String SQL) {
    ResultSet rs = null;
    ArrayList ElementList = new ArrayList();
    String SQLString = SQL;
    try {
        rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            ElementNode TempNode = new ElementNode(this.GetDocID(), rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
            ElementList.add(TempNode);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ElementList;
}

private ArrayList ProcessText(int TEPre) {
    ArrayList TextList = new ArrayList();
    String TextSQL = "USE xr_system SELECT textid, elementpre, elementpost, textcontent FROM text " +
                             "WHERE elementpre=" + TEPre + " AND documentid = " + this.GetDocID();
    ResultSet rsT = null;
    try {
        rsT = new DBConnection().ExecuteSQL(TextSQL);
        while (rsT.next()) {
            TextNode TempText = new TextNode(this.GetDocID(), rsT.getInt(1), rsT.getInt(2), rsT.getInt(3), rsT.getString(4));
            TextList.add(TempText);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return TextList;
}

private ArrayList ProcessAttribute(int AEPre) {
    ArrayList AttributeList = new ArrayList();
    String AttributeSQL = "USE xr_system SELECT attributeid, elementpre, elementpost, attributename, attributevalue FROM attribute WHERE elementpre=" + AEPre + " AND documentid = " + this.GetDocID();
    ResultSet rsA = null;
    try {
        rsA = new DBConnection().ExecuteSQL(AttributeSQL);
        while (rsA.next()) {
            AttributeNode TempAttribute = new AttributeNode(this.GetDocID(), rsA.getInt(1), rsA.getInt(2), rsA.getInt(3), rsA.getString(4), rsA.getString(5));
            AttributeList.add(TempAttribute);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return AttributeList;
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
        String ChildSQL = "USE xr_system SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE parentpre=" + TempList.get(m).GetElementPre() + " AND documentid=" + this.GetDocID();
        ArrayList TNList = new ArrayList();
        TNList = ProcessData(ChildSQL);
        ThisElem.addContent(RecursiveConstruct(ChildElement, TNList));
    }
    return ThisElem;
}

private void PrintResult(Document XMLDocument) {
    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
    try {
        XMLString = output.outputString(XMLDocument);
    } catch (Exception e) {
        e.printStackTrace();
    }
    XMLString = XMLString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n", "");
    XMLString = XMLString.replace("<结果XML文档片段>\r\n", "");
    XMLString = XMLString.replace("</结果XML文档片段>\r\n\r\n", "");
    System.out.println(XMLString);
    new cn.edu.xaut.SLGridDataNode.GUI.QueryResult(XMLString).setVisible(true);
}

private Document ConstructFrag(ArrayList<ResultElement> TList) {
    Document ThisDocument;
    Element RootElement = new Element("结果XML文档片段");
    ThisDocument = new Document(RootElement);

    for (int i = 0; i < TList.size(); i++) {
        Element ParentElement = new Element(TList.get(i).GetTagName());
        ArrayList<AttributeNode> AList1 = new ArrayList<AttributeNode>();
        ArrayList<TextNode> TList1 = new ArrayList<TextNode>();
        AList1 = ProcessAttribute(TList.get(i).GetElementPre());
        TList1 = ProcessText(TList.get(i).GetElementPre());
        for (int A1 = 0; A1 < AList1.size(); A1++) {
            ParentElement.setAttribute(AList1.get(A1).GetAttributeName(), AList1.get(A1).GetAttributeValue());
        }
        for (int T1 = 0; T1 < TList1.size(); T1++) {
            ParentElement.setText(TList1.get(T1).GetTextContent());
        }
        String LowSQL = "USE xr_system SELECT elementpre, elementpost, elementtag, parentpre, parentpost FROM element WHERE parentpre=" + TList.get(i).GetElementPre() + " AND documentid=" + this.GetDocID();
        ArrayList<ElementNode> ChildList = new ArrayList<ElementNode>();
        ChildList = ProcessData(LowSQL);
        Element SecondLevel = RecursiveConstruct(ParentElement, ChildList);
        ThisDocument.getRootElement().addContent(SecondLevel);
    }
    return ThisDocument;
}

public void BuildResult() {

}
}
