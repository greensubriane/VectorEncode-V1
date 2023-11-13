/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode;
/*
 * @author He Ting
 */

import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributeVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.ElementVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.NameSpaceVectorEncodeBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Constructxml {

private String DocumentName;
private int DocumentID;
private Namespace rootnamespace;
private String XMLString;

ArrayList<ElementVectorDietzEncodingBean> ElementList = new ArrayList<ElementVectorDietzEncodingBean>();
ArrayList<AttributeVectorDietzEncodingBean> AttributeList = new ArrayList<AttributeVectorDietzEncodingBean>();
ArrayList<TextVectorDietzEncodingBean> TextList = new ArrayList<TextVectorDietzEncodingBean>();
ArrayList<NameSpaceVectorEncodeBean> namespacelist = new ArrayList<NameSpaceVectorEncodeBean>();


private ArrayList<ElementVectorDietzEncodingBean> ProcessVectorElementData(String SQL) {

    String SQLString = SQL;
    try {
        ResultSet rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            ElementVectorDietzEncodingBean TempNode = new ElementVectorDietzEncodingBean(22, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
            ElementList.add(TempNode);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return ElementList;
}

private ArrayList<TextVectorDietzEncodingBean> ProcessVectorText(String VectorElementPre) {

    String TextSQL = "SELECT elementprevectorencode, elementpostvectorencode, textvalue FROM text WHERE elementprevectorencode=" + VectorElementPre + " AND documentid= 22";
    try {
        ResultSet rsT = new DBConnection().ExecuteSQL(TextSQL);
        while (rsT.next()) {
            TextVectorDietzEncodingBean TempText = new TextVectorDietzEncodingBean(22, rsT.getString(1), rsT.getString(2), rsT.getString(3));
            TextList.add(TempText);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return TextList;
}

private ArrayList<AttributeVectorDietzEncodingBean> ProcessVectorAttribute(String VectorElementPre) {

    String AttributeSQL = "SELECT elementprevectorencode, elementpostvectorencode, attributename, attributevalue, attributeid, attributeprefix FROM attribute WHERE elementprevectorencode=" + VectorElementPre + " AND documentid= 22";
    try {
        ResultSet rsA = new DBConnection().ExecuteSQL(AttributeSQL);
        while (rsA.next()) {
            AttributeVectorDietzEncodingBean TempAttribute = new AttributeVectorDietzEncodingBean(22, rsA.getString(1), rsA.getString(2), rsA.getString(3), rsA.getString(6), rsA.getString(4));
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


    String RootSQL = "SELECT elementprevectorencode, elementpostvectorencode, elementtag, elementparentprevectorencode, elementparentpostvectorencode, elementprefix FROM element WHERE elementprevectorencode='[1,0]' AND documentid= 22";

    ArrayList<ElementVectorDietzEncodingBean> TopList = ProcessVectorElementData(RootSQL);

    String RootName = TopList.get(0).getElementsName();
    System.out.println(RootName);
    Element root = new Element("pub");
    Document XMLDocument = new Document(root);

    String RootPre = TopList.get(0).GetPreElementVectorDietzEncoding();
    ArrayList<AttributeVectorDietzEncodingBean> AList1 = ProcessVectorAttribute(RootPre);
    ArrayList<TextVectorDietzEncodingBean> TList1 = ProcessVectorText(RootPre);
    for (int A1 = 0; A1 < AList1.size(); A1++) {
        root.setAttribute(AList1.get(A1).GetAttributeName(), AList1.get(A1).GetAttributeValue());
        System.out.println(AList1.get(A1).GetAttributeName() + " , " + AList1.get(A1).GetAttributeValue());
    }
    for (int T1 = 0; T1 < TList1.size(); T1++) {
        root.setText(TList1.get(T1).getTextValue());
        System.out.println(TList1.get(T1).getTextValue());
    }
    String SecondSQL = "SELECT elementprevectorencode, elementpostvectorencode, elementtag, elementparentprevectorencode, elementparentpostvectorencode, elementprefix FROM element WHERE elementparentprevectorencode=" + RootPre + " AND documentid= 22";
    ArrayList<ElementVectorDietzEncodingBean> SecondList = new ArrayList<ElementVectorDietzEncodingBean>();
    SecondList = ProcessVectorElementData(SecondSQL);
    for (int x = 0; x < SecondList.size(); x++) {
        ArrayList<AttributeVectorDietzEncodingBean> AList3 = ProcessVectorAttribute(SecondList.get(x).GetPreElementVectorDietzEncoding());
        ArrayList<TextVectorDietzEncodingBean> TList3 = ProcessVectorText(SecondList.get(x).GetPreElementVectorDietzEncoding());
        Element ParentElement = new Element(SecondList.get(x).getElementsName());
        for (int A3 = 0; A3 < AList3.size(); A3++) {
            ParentElement.setAttribute(AList3.get(A3).GetAttributeName(), AList3.get(A3).GetAttributeValue());
        }
        for (int T3 = 0; T3 < TList3.size(); T3++) {
            String TValue = TList3.get(T3).getTextValue();
            ParentElement.setText(TValue);
        }
        String LowSQL = "SELECT elementprevectorencode, elementpostvectorencode, elementtag, elementparentprevectorencode, elementparentpostvectorencode, elementprefix FROM element WHERE elementparentprevectorencode=" + SecondList.get(x).GetPreElementVectorDietzEncoding() + " AND documentid= 22";
        ArrayList<ElementVectorDietzEncodingBean> ChildList = ProcessVectorElementData(LowSQL);
        Element SecondLevel = RecursiveConstruct(ParentElement, ChildList);
        XMLDocument.getRootElement().addContent(SecondLevel);
    }
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

private Element RecursiveConstruct(Element TempElem, ArrayList<ElementVectorDietzEncodingBean> TempList) {
    Element ThisElem = TempElem;
    for (int m = 0; m < TempList.size(); m++) {
        Element ChildElement = new Element(TempList.get(m).getElementsName());
        ArrayList<AttributeVectorDietzEncodingBean> AList2 = ProcessVectorAttribute(TempList.get(m).GetPreElementVectorDietzEncoding());
        ArrayList<TextVectorDietzEncodingBean> TList2 = ProcessVectorText(TempList.get(m).GetPreElementVectorDietzEncoding());
        for (int A2 = 0; A2 < AList2.size(); A2++) {
            ChildElement.setAttribute(AList2.get(A2).GetAttributeName(), AList2.get(A2).GetAttributeValue());
        }
        for (int T2 = 0; T2 < TList2.size(); T2++) {
            String TValue = TList2.get(T2).getTextValue();
            ChildElement.setText(TValue);
        }
        String ChildSQL = "SELECT elementprevectorencode, elementpostvectorencode, elementtag, elementparentprevectorencode, elementparentpostvectorencode, elementprefix FROM element WHERE elementparentprevectorencode=" + TempList.get(m).GetPreElementVectorDietzEncoding() + " AND documentid= 22";
        ArrayList<ElementVectorDietzEncodingBean> TNList = ProcessVectorElementData(ChildSQL);
        ThisElem.addContent(RecursiveConstruct(ChildElement, TNList));
    }
    return ThisElem;
}

public void constructnewxml() {
    String RootSQL = "SELECT elementprevectorencode, elementpostvectorencode, elementtag, elementparentprevectorencode, elementparentpostvectorencode, elementprefix FROM element WHERE elementprevectorencode='[1,0]' AND documentid= 22";
    ArrayList<ElementVectorDietzEncodingBean> TopList = ProcessVectorElementData(RootSQL);
    String rootname = "pub";
    String RootName = TopList.get(0).getElementsName();
    Element root = new Element(RootName);
    Document doc = new Document(root);
    Element child = new Element("book");
    child.setText("hello");
    root.setContent(child);
    XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat()); //格式化输出，产生缩进和换行
    Format format = outp.getFormat();
    format.setEncoding("UTF-8"); //设置语言
    format.setExpandEmptyElements(true); //设置输出空元素格式
    outp.setFormat(format);
    try {
        String XMLStrings = outp.outputString(doc);
        System.out.println(XMLStrings);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    } //输出XML文档
}

public static void main(String args[]) {
    Constructxml test = new Constructxml();
    String xmlstring = test.ConstructXML();
    System.out.println(xmlstring);
    //test.constructnewxml();
}
}
