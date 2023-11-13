/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.VectorEncoding;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributePreVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributeVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextPreVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextVectorDietzEncodingBean;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.*;

import static cn.edu.xaut.SLGridDataNode.VectorEncoding.VectorEncodingFunctions.GetDietzVectorMap;
import static java.lang.Integer.parseInt;
import static java.lang.System.err;
import static java.lang.System.out;

public class GenerateAttributesAndTextVectorEncoding {

GenerateElementsVectorEncoding elementvectorencoding = new GenerateElementsVectorEncoding();
public ArrayList<TextPreVectorDietzEncodingBean> textlists = new ArrayList<>();
public static ArrayList<TextVectorDietzEncodingBean> textvectordietzencodebean = new ArrayList<>();
private Map<String, String> TempElementDietzOrder = new HashMap<>();
private Map<Integer, int[]> Vectormaps = new HashMap<>();
private Map<int[], String> TextNameandVectorDietzPreOrderMap = new HashMap<>();
private Map<int[], String> TextNameandVectorDietzPostOrderMap = new HashMap<>();
private Map<int[], String> AttributeNameandVectorDietzPreOrderMap = new HashMap<>();
private Map<int[], String> AttributeNameandVectorDietzPostOrderMap = new HashMap<>();
VectorEncodingFunctions vectorfunctions = new VectorEncodingFunctions();
private ArrayList<AttributePreVectorDietzEncodingBean> attributelists = new ArrayList<>();
public static ArrayList<AttributeVectorDietzEncodingBean> attributevectordietzencodebean = new ArrayList<>();
private int AttributePreOrder = 0;
private int TextPreOrder = 0;
private int elementcount = 0;

public int GetElementCount(Element root) {

    List elementlist = root.getContent();
    Iterator iter = elementlist.iterator();
    elementcount++;
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            elementcount = GetElementCount(child);
        }
    }
    return elementcount;
}

public ArrayList<AttributeVectorDietzEncodingBean> getAttributeBean(int DocID, String FileName) {

    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        this.GetAttributeName(DocID, root);
    } catch (JDOMException | IOException e) {
        err.println(e.getMessage());
    }

    this.GenerateAttributesAndTextPreandPostVectorEncoding(DocID, FileName);
    return attributevectordietzencodebean;
}

public ArrayList<TextVectorDietzEncodingBean> getTextBean(int DocID, String FileName) {

    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        this.GetTextName(DocID, root);
    } catch (JDOMException | IOException e) {
        err.println(e.getMessage());
    }
    this.GenerateAttributesAndTextPreandPostVectorEncoding(DocID, FileName);
    return textvectordietzencodebean;
}

public void GetAttributeName(int DocumentID, Element root) {
    List elementlist = root.getContent();
    AttributePreOrder++;
    String formattedattributepreorder = Integer.toString(AttributePreOrder);
    List<Attribute> attributelist = root.getAttributes();
    for (int i = 0; i < attributelist.size(); i++) {
        Attribute attributenode = attributelist.get(i);
        String attributename = attributenode.getName();
        String namespaceprefix = attributenode.getNamespacePrefix();
        String attributevalue = attributenode.getValue();
        attributelists.add(new AttributePreVectorDietzEncodingBean(DocumentID, attributename,
                attributevalue, formattedattributepreorder, namespaceprefix));
    }
    Iterator iter = elementlist.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            GetAttributeName(DocumentID, child);
        }
    }
}

public void GetTextName(int DocumentID, Element root) {
    List elementlist = root.getContent();
    TextPreOrder += 1;
    String TextValue = root.getTextTrim();
    String formattedtextpreorder = Integer.toString(TextPreOrder);
    if (!TextValue.equals("")) {
        textlists.add(new TextPreVectorDietzEncodingBean(DocumentID, formattedtextpreorder, TextValue));
    }
    Iterator iter = elementlist.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            GetTextName(DocumentID, child);
        }
    }
}

public void GenerateAttributesAndTextPreandPostVectorEncoding(int DocID, String FileName) {
    String DietzXMLFile = elementvectorencoding.ProductNewXMLContainsDietzPreOrder(FileName);
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        elementcount = this.GetElementCount(root);
        out.println(elementcount);
    } catch (JDOMException | IOException e) {
        err.println(e.getMessage());
    }
    TempElementDietzOrder = elementvectorencoding.GetDietzOrderMap(DietzXMLFile);
    Vectormaps = GetDietzVectorMap(elementcount);
    if (textlists == null) {
        out.println("data transfer failer");
    } else {
        for (TextPreVectorDietzEncodingBean textprebean : textlists) {
            String PreVectorTextEncoding = "[" + Vectormaps.get(parseInt(textprebean.getTextPreVectorDietzEncode()))[0] + "," +
                                                   Vectormaps.get(parseInt(textprebean.getTextPreVectorDietzEncode()))[1] + "]";
            TextNameandVectorDietzPreOrderMap.put(Vectormaps.get(parseInt(textprebean.getTextPreVectorDietzEncode())), textprebean.getTextValue());
            String TempElementPostDietzOrder = TempElementDietzOrder.get(textprebean.getTextPreVectorDietzEncode());
            String PostVectorTextEncoding = "[" + Vectormaps.get(parseInt(TempElementPostDietzOrder))[0] + "," + Vectormaps.get(parseInt(TempElementPostDietzOrder))[1] + "]";
            TextNameandVectorDietzPostOrderMap.put(Vectormaps.get(parseInt(TempElementPostDietzOrder)), textprebean.getTextValue());
            textvectordietzencodebean.add(new TextVectorDietzEncodingBean(DocID, PreVectorTextEncoding,
                    textprebean.getTextValue(), PostVectorTextEncoding));
        }
    }
    if (attributelists == null) {
        out.println("data transfer failer");

    } else {
        for (AttributePreVectorDietzEncodingBean attributeprebean : attributelists) {
            String PreVectorAttributeEncoding = "[" + Vectormaps.get(parseInt(attributeprebean.GetAttributeVectorPreDietzOrder()))[0] + ","
                                                        + Vectormaps.get(parseInt(attributeprebean.GetAttributeVectorPreDietzOrder()))[1] + "]";
            AttributeNameandVectorDietzPreOrderMap.put(Vectormaps.get(parseInt(attributeprebean.GetAttributeVectorPreDietzOrder())), attributeprebean.GetAttributeName());
            String TempElementPostDietzOrder = TempElementDietzOrder.get(attributeprebean.GetAttributeVectorPreDietzOrder());
            String PostVectorAttributeEncoding = "[" + Vectormaps.get(parseInt(TempElementPostDietzOrder))[0] + "," + Vectormaps.get(parseInt(TempElementPostDietzOrder))[1] + "]";
            AttributeNameandVectorDietzPostOrderMap.put(Vectormaps.get(parseInt(TempElementPostDietzOrder)), attributeprebean.GetAttributeName());
            attributevectordietzencodebean.add(new AttributeVectorDietzEncodingBean(DocID,
                    attributeprebean.GetAttributeName(),
                    attributeprebean.GetAttributeValue(),
                    PreVectorAttributeEncoding,
                    attributeprebean.GetAttributePrefix(),
                    PostVectorAttributeEncoding));
        }
    }
}

   /* public static void main(String args[]) {
        GenerateAttributesAndTextVectorEncoding attributeencode = new GenerateAttributesAndTextVectorEncoding();
        //attributeencode.GenerateAttributesAndTextPreandPostVectorEncoding(0,"boudaries.xml");
        ArrayList<AttributeVectorDietzEncodingBean> attributebeans = attributeencode.getAttributeBean(0, "china_administrator.gml");
        ArrayList<TextVectorDietzEncodingBean> textbeans = attributeencode.getTextBean(0, "china_administrator.gml");
        if (attributebeans.size() == 0) {
            out.println("操作失误");
        } else {
            for (int i = 0; i < attributebeans.size(); i++) {
                out.println("The Attribute name is:" + attributebeans.get(i).GetAttributeName());
                out.println("The Attribute value is:" + attributebeans.get(i).GetAttributeValue());
                out.println("The Attribute pre vectordietz encoding is:" + attributebeans.get(i).GetAttributeVectorPreDietzOrder());
                out.println("The Attribute post vectordietz encoding is:" + attributebeans.get(i).GetAttributeVectorPostDietzOrder());
            }
        }
        out.println("-----------------------------------------------------------------------\n");
        for (int j = 0; j < textbeans.size(); j++) {
            out.println("The Text value is:" + textbeans.get(j).getTextValue());
            out.println("The Text pre vectordietz encoding is:" + textbeans.get(j).getTextPreVectorDietzEncode());
            out.println("The Text post vectordietz encoding is:" + textbeans.get(j).getTextPostVectorDietzEncode());
        }
    }*/
}
