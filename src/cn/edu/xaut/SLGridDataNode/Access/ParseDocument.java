/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseDocument {

private ArrayList<VectorElementPre> VectorPreElementList = new ArrayList<VectorElementPre>();
private ArrayList<VectorAttributePre> VectorPreAttributeList = new ArrayList<VectorAttributePre>();
private ArrayList<VectorTextPre> VectorPreTextList = new ArrayList<VectorTextPre>();

private ArrayList<PreElement> PreElementList = new ArrayList<PreElement>();
private ArrayList<PreAttribute> PreAttributeList = new ArrayList<PreAttribute>();
private ArrayList<PreText> PreTextList = new ArrayList<PreText>();
private Document OutDocument;

private int VectorPreOrder = 0;

private int PreOrder = 0;
private int TextID = 1;
private int AttributeID = 1;

private int DocumentID = 0;

/** Creates a new instance of ParseDocument */
public ParseDocument() {
}

/*
public static void main(String[] args) {
}
 */
public void ProcessXML(int DocID, String FileName) {
    SetDocumentID(DocID);
    SAXBuilder builder = new SAXBuilder();
    try {
        Document doc = builder.build(FileName);
        listNodes(doc, 0);
    }
    // indicates a well-formedness error
    catch (JDOMException e) {
        //System.out.println(args[0] + " is not well-formed.");
        System.out.println(e.getMessage());
    } catch (IOException e) {
        System.out.println(e);
    }
    //DisplayParseREsult();
}

public ArrayList<PreElement> GetPreElementList(int DocID, String FileName) {
    ProcessXML(DocID, FileName);
    return PreElementList;
}

public ArrayList<PreAttribute> GetPreAttributeList(int DocID, String FileName) {
    ProcessXML(DocID, FileName);
    return PreAttributeList;
}

public ArrayList<PreText> GetPreTextList(int DocID, String FileName) {
    ProcessXML(DocID, FileName);
    return PreTextList;
}

public void DisplayParseREsult() {
    System.out.println("元素信息");
    for (int i = 0; i < PreElementList.size(); i++) {
        System.out.println(PreElementList.get(i).GetDocumentID() + " " + PreElementList.get(i).GetElementPre() + " " + PreElementList.get(i).GetTagName());
    }
    System.out.println("属性信息");
    for (int j = 0; j < PreAttributeList.size(); j++) {
        System.out.println(PreAttributeList.get(j).GetDocumentID() + " " + PreAttributeList.get(j).GetAttributeID() + " " + PreAttributeList.get(j).GetElementPre() + " " + PreAttributeList.get(j).GetAttributeName() + " " + PreAttributeList.get(j).GetAttributeValue());
    }
    System.out.println("文本信息");
    for (int k = 0; k < PreTextList.size(); k++) {
        System.out.println(PreTextList.get(k).GetDocumentID() + " " + PreTextList.get(k).GetTextID() + " " + PreTextList.get(k).GetElementPre() + " " + PreTextList.get(k).GetTextContent());
    }

}

private void SetDocumentID(int DocID) {
    this.DocumentID = DocID;
}

public void listNodes(Object o, int depth) {

    printSpaces(depth);
    if (o instanceof Element) {

        Element element = (Element) o;
        List<Attribute> AttributeList = element.getAttributes();
        //System.out.println("Element: " + element.getName()+"            ***********元素先序编码："+(PreOrder));
        PreElementList.add(new PreElement(DocumentID, PreOrder, element.getName()));
        String OrderValue = String.valueOf(PreOrder);
        PreOrder++;
        for (int AttIndex = 0; AttIndex < AttributeList.size(); AttIndex++) {
            //Attribute AttributeNode = (Attribute)AttributeList.get(AttIndex);
            Attribute AttributeNode = AttributeList.get(AttIndex);
            //System.out.println("属性编号:" +(AttributeID++)+"       属性名:" +AttributeNode.getName()+"           属性值:" +AttributeNode.getValue()+"            ***********元素先序编码："+(PreOrder-1));
            PreAttributeList.add(new PreAttribute(DocumentID, AttributeID, (PreOrder - 1), AttributeNode.getName(), AttributeNode.getValue()));
            AttributeID++;
        }
        //element = element.setAttribute("Pre_Order",OrderValue);
        List children = element.getContent();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Object child = iterator.next();
            listNodes(child, depth + 1);
        }
    } else if (o instanceof Document) {
        //System.out.println(o.toString()+"          ***********先序编码："+(PreOrder));
        Document doc = (Document) o;
        PreElementList.add(new PreElement(DocumentID, PreOrder, "#DOCUMENT"));
        List children = doc.getContent();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            PreOrder += 1;
            Object child = iterator.next();
            //OutDocument = new Document(new Element(child.toString()));
            listNodes(child, depth + 1);
        }
    } else if (o instanceof Comment) {
        System.out.println(o.toString());
    } else if (o instanceof CDATA) {
        System.out.println(o.toString());
    } else if (o instanceof Text) {
        Text TextNode = (Text) o;
        String TextValue = TextNode.getText().toString();

        if (TextValue.substring(0, 1).equals("\n"))
            System.out.println(" ");

        else {
            //System.out.println("文本内容编号："+(TextID)+"      Text内容:" +TextValue+"           ***********元素先序编码："+(PreOrder-1));
            PreTextList.add(new PreText(DocumentID, TextID, (PreOrder - 1), TextValue));
            TextID += 1;
        }

    } else if (o instanceof EntityRef) {
        System.out.println(o.toString());
    } else if (o instanceof ProcessingInstruction) {
        System.out.println(o.toString());
    } else {
        System.out.println("Unexpected type: " + o.getClass());
    }

}

private void printSpaces(int n) {
    //int ParentPre=PreOrder-1;
    for (int i = 0; i < n; i++) {
        System.out.print(' ');
    }
}
}
