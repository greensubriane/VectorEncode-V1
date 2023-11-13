/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * 类说明： 对XML文档进行Dietz编码
 *
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.DietzEncode;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.*;

public class EncodeXML {
/*create a new instance of EncodeXML*/
private DefaultMutableTreeNode XMLTree;
private Map<String, String> OrderMap = new HashMap<String, String>();
private Map<String, DietzEncode> ParentMap = new HashMap<String, DietzEncode>();
private int ElementCount = 1;

private Map<String, String> ReadXML(String FileName) {
    SAXBuilder builder = new SAXBuilder();
    int PostOrder = 1;
    try {
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        ArrayList<Element> SecondList = new ArrayList<Element>();
        List TempList = root.getContent();
        ElementCount += root.getContentSize();
        Iterator iterator = TempList.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof Element) {
                SecondList.add((Element) obj);
            }
        }
        XMLTree = new DefaultMutableTreeNode(root);
        for (int m = 0; m < SecondList.size(); m++) {
            XMLTree.add(MakeXMLTree(SecondList.get(m)));
        }
    } catch (JDOMException e) {
        System.err.println(e.getMessage());
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
    Enumeration myEnumeration1 = XMLTree.postorderEnumeration();
    Enumeration myEnumeration2 = XMLTree.postorderEnumeration();
    OrderMap = FunOrderMap(myEnumeration1);
    ParentMap = FunParentMap(myEnumeration2, OrderMap);
    return OrderMap;
}

public Map<String, String> GetOrderMap(String FileName) {
    ReadXML(FileName);
    return OrderMap;
}

public Map<String, DietzEncode> GetParentMap(String FileName) {
    ReadXML(FileName);
    return ParentMap;
}

private void ViewMapInfo(Map<String, String> TempMap) {
    for (Map.Entry<String, String> ThisEntry : TempMap.entrySet()) {
        System.out.println("前序编码：" + ThisEntry.getKey() + " 后续编码: " + ThisEntry.getValue());
    }
}

private DefaultMutableTreeNode MakeXMLTree(Element TempNode) {
    DefaultMutableTreeNode SecondLevel = new DefaultMutableTreeNode(TempNode);
    List ChildList = TempNode.getContent();
    ArrayList<Element> ElemList = new ArrayList<Element>();
    Iterator iterator = ChildList.iterator();
    while (iterator.hasNext()) {
        Object obj1 = iterator.next();
        if (obj1 instanceof Element) {
            ElemList.add((Element) obj1);
        }
    }
    for (int m = 0; m < ElemList.size(); m++) {
        SecondLevel.add(MakeXMLTree(ElemList.get(m)));
    }
    return SecondLevel;
}

private Map<String, String> FunOrderMap(Enumeration Enum) {
    Map<String, String> OMap = new HashMap<String, String>();
    int EPostOrder = 1;
    while (Enum.hasMoreElements()) {
        Object OE = Enum.nextElement();
        String ChildElement = OE.toString();
        DefaultMutableTreeNode TE = (DefaultMutableTreeNode) OE;
        Element ElementNode = ((Element) TE.getUserObject());
        try {
            if ((TE.getParent()) != null) {
                OMap.put(ElementNode.getAttributeValue("PreOrder"), String.valueOf(EPostOrder));
                EPostOrder++;
            } else {
                OMap.put(ElementNode.getAttributeValue("PreOrder"), String.valueOf(EPostOrder));
                ElementCount = EPostOrder + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return OMap;
}

private Map<String, DietzEncode> FunParentMap(Enumeration Enum, Map<String, String> NMap) {
    Map<String, DietzEncode> PMap = new HashMap<String, DietzEncode>();
    int PPre = 0, PPost = 0;
    String CPre;
    DietzEncode ThisDietz;
    while (Enum.hasMoreElements()) {
        Object OE = Enum.nextElement();
        String ChildElement = OE.toString();
        DefaultMutableTreeNode TE = (DefaultMutableTreeNode) OE;
        Element ElementNode = ((Element) TE.getUserObject());
        try {
            if ((TE.getParent()) != null) {
                CPre = ElementNode.getAttributeValue("PreOrder");
                PPre = Integer.valueOf(((Element) (((DefaultMutableTreeNode) (TE.getParent())).getUserObject())).getAttributeValue("PreOrder"));
                PPost = Integer.valueOf(NMap.get(String.valueOf(PPre)));
                ThisDietz = new DietzEncode(PPre, PPost);
                PMap.put(CPre, ThisDietz);
            } else {
                CPre = ElementNode.getAttributeValue("PreOrder");
                ThisDietz = new DietzEncode(0, ElementCount);
                PMap.put(CPre, ThisDietz);
            }
        } catch (Exception e) {
        }
    }
    return PMap;
}
}
