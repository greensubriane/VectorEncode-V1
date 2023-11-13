/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding;
/**
 * @author Ting He
 * 2023-09-12
 */

import cn.edu.xaut.SLGridDataNode.Util.DietzEncode;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.ElementVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.NameSpaceVectorEncodeBean;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.FileOutputStream;
import java.util.*;

public class GenerateElementsVectorEncoding {
// VectorEncodingFunctions vectorfunctions = new VectorEncodingFunctions();
private int DietzPreOrder = 1;
private DefaultMutableTreeNode XMLTree;
private String TempElementSpecialName;
private String ParentElementName;
private ArrayList<ElementVectorDietzEncodingBean> elementvectordietzencodebean = new ArrayList<ElementVectorDietzEncodingBean>();
private List<NameSpaceVectorEncodeBean> xmlnamespacebean = new ArrayList<NameSpaceVectorEncodeBean>();
private ArrayList<String> TempElementNameList = new ArrayList<String>();
private ArrayList<String> ParentElementNameList = new ArrayList<String>();
private ArrayList<String> TempElementPreVectorDietzEncodingList = new ArrayList<String>();
private ArrayList<String> TempElementPostVectorDietzEncodingList = new ArrayList<String>();
private ArrayList<String> ParentElementPreVectorDietzEncodingList = new ArrayList<String>();
private ArrayList<String> ParentElementPostVectorDietzEncodingList = new ArrayList<String>();
private ArrayList<String> ElementNameList = new ArrayList<String>();
private ArrayList<String> AttributeNameList = new ArrayList<String>();
private Map<String, String> elementnameandpredietzencoding = new HashMap<String, String>();
private Map<String, String> DietzOrderMap = new HashMap<String, String>();
private Map<String, DietzEncode> DietzParentMap = new HashMap<String, DietzEncode>();
private Map<String, String> ElementnameandprefixMap = new HashMap<String, String>();
private List<Namespace> additionalnamespace = new ArrayList<Namespace>();
private int DietzElementCount = 1;
private int elementcount = 0;

public ArrayList<ElementVectorDietzEncodingBean> getElementBean(int DocID, String FileName) {
    this.GenerateElementsVectorEncodings(DocID, FileName);
    return elementvectordietzencodebean;
}

public String ProductNewXMLContainsDietzPreOrder(String FileName) {

    String OutPutFile = "DietzOutPut" + FileName;
    SAXBuilder builder = new SAXBuilder();
    Document doc = new Document();
    try {
        doc = builder.build(FileName);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    Element root = doc.getRootElement();
    ArrayList<Element> ParentList = new ArrayList<Element>();
    ParentList = DietzFileElementProcessList(root);
    Element oroot;
    oroot = new Element(root.getName(), root.getNamespace());
    oroot = oroot.setAttribute("DietzPreOrder", String.valueOf(DietzPreOrder));
    Document OrderedDocument = new Document(oroot);
    DietzPreOrder += 1;
    for (int m = 0; m < ParentList.size(); m++) {
        ArrayList<Element> ChildList = new ArrayList<Element>();
        Element t;
        t = new Element(ParentList.get(m).getName(), ParentList.get(m).getNamespace());
        ChildList = DietzFileElementProcessList(ParentList.get(m));
        Element t1 = MakeDietzPreXMLChildElement(t, ChildList);
        Element c = OrderedDocument.getRootElement();
        c.addContent(t1);
    }
    //格式化输出的文档
    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
    Format format = output.getFormat();
    //设置文档的语言
    format.setEncoding("UTF-8");
    //设置空元素的输出格式
    format.setExpandEmptyElements(true);
    output.setFormat(format);
    try {
        output.output(OrderedDocument, new FileOutputStream(OutPutFile));
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return OutPutFile;
}

private ArrayList<Element> DietzFileElementProcessList(Element Now) {
    ArrayList<Element> NArrayList = new ArrayList<Element>();
    List NowList = Now.getContent();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            NArrayList.add((Element) obj);
        } else
            continue;
    }
    return NArrayList;
}

private Element MakeDietzPreXMLChildElement(Element NowElement, ArrayList<Element> TempList) {
    Element NElement = NowElement;
    NElement = NElement.setAttribute("DietzPreOrder", String.valueOf(DietzPreOrder));
    DietzPreOrder++;
    Element CElement;
    for (int j = 0; j < TempList.size(); j++) {
        CElement = new Element(TempList.get(j).getName(), TempList.get(j).getNamespace());
        Element ChildElement = TempList.get(j);
        ArrayList<Element> TNList = new ArrayList<Element>();
        TNList = DietzFileElementProcessList(ChildElement);
        NElement.addContent(MakeDietzPreXMLChildElement(CElement, TNList));
    }
    return NElement;
}

private Map<String, String> ReadDietzPreOrderXML(String FileName) {
    SAXBuilder builder = new SAXBuilder();
    try {
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        ArrayList<Element> SecondList = new ArrayList<Element>();
        List TempList = root.getContent();
        DietzElementCount += root.getContentSize();
        Iterator iterator = TempList.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof Element) {
                SecondList.add((Element) obj);
            }
        }
        XMLTree = new DefaultMutableTreeNode(root);
        for (int m = 0; m < SecondList.size(); m++) {
            XMLTree.add(MakeDietzXMLTree(SecondList.get(m)));
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    Enumeration myEnumeration1 = XMLTree.postorderEnumeration();
    Enumeration myEnumeration2 = XMLTree.postorderEnumeration();
    DietzOrderMap = FunDietzOrderMap(myEnumeration1);
    DietzParentMap = FunDietzParentMap(myEnumeration2, DietzOrderMap);
    return DietzOrderMap;
}

public List<NameSpaceVectorEncodeBean> getNameSpaces(int DocID, String FileName) {
    SAXBuilder builder = new SAXBuilder();
    Document doc = new Document();
    try {
        doc = builder.build(FileName);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    Element root = doc.getRootElement();
    Namespace rootnamespace = root.getNamespace();
    additionalnamespace = root.getAdditionalNamespaces();
    xmlnamespacebean.add(new NameSpaceVectorEncodeBean(DocID, rootnamespace.getPrefix(), rootnamespace.getURI(), 1));
    for (int tt = 0; tt < additionalnamespace.size(); tt++) {
        System.out.println(additionalnamespace.get(tt));
        xmlnamespacebean.add(new NameSpaceVectorEncodeBean(DocID, additionalnamespace.get(tt).getPrefix(),
                additionalnamespace.get(tt).getURI(), 0));
    }
    return xmlnamespacebean;
}

public Map<String, String> GetDietzOrderMap(String FileName) {
    ReadDietzPreOrderXML(FileName);
    return DietzOrderMap;
}

public Map<String, DietzEncode> GetDietzParentMap(String FileName) {
    ReadDietzPreOrderXML(FileName);
    return DietzParentMap;
}

private DefaultMutableTreeNode MakeDietzXMLTree(Element TempNode) {
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
        SecondLevel.add(MakeDietzXMLTree(ElemList.get(m)));
    }
    return SecondLevel;
}

private Map<String, String> FunDietzOrderMap(Enumeration Enum) {
    Map<String, String> OMap = new HashMap<String, String>();
    int EPostOrder = 1;
    while (Enum.hasMoreElements()) {
        Object OE = Enum.nextElement();
        DefaultMutableTreeNode TE = (DefaultMutableTreeNode) OE;
        Element ElementNode = ((Element) TE.getUserObject());
        try {
            if ((TE.getParent()) != null) {
                OMap.put(ElementNode.getAttributeValue("DietzPreOrder"), String.valueOf(EPostOrder));
                EPostOrder++;
            } else {
                OMap.put(ElementNode.getAttributeValue("DietzPreOrder"), String.valueOf(EPostOrder));
                DietzElementCount = EPostOrder + 1;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    return OMap;
}

private Map<String, DietzEncode> FunDietzParentMap(Enumeration Enum, Map<String, String> NMap) {
    Map<String, DietzEncode> PMap = new HashMap<String, DietzEncode>();
    int PPre = 0, PPost = 0;
    String CPre;
    DietzEncode ThisDietz;
    while (Enum.hasMoreElements()) {
        Object OE = Enum.nextElement();
        DefaultMutableTreeNode TE = (DefaultMutableTreeNode) OE;
        Element ElementNode = ((Element) TE.getUserObject());
        if ((TE.getParent()) != null) {
            CPre = ElementNode.getAttributeValue("DietzPreOrder");
            PPre = Integer.valueOf(((Element) (((DefaultMutableTreeNode) (TE.getParent())).getUserObject())).getAttributeValue("DietzPreOrder"));
            PPost = Integer.valueOf(NMap.get(String.valueOf(PPre)));
            ThisDietz = new DietzEncode(PPre, PPost);
            PMap.put(CPre, ThisDietz);
        } else {
            CPre = ElementNode.getAttributeValue("DietzPreOrder");
            ThisDietz = new DietzEncode(0, DietzElementCount);
            PMap.put(CPre, ThisDietz);
        }
    }
    return PMap;
}

public void GetAttributeandElementName(Element root) {
    List elementlist = new ArrayList();
    elementlist = root.getContent();
    Attribute attributenode = root.getAttribute("DietzPreOrder");
    String AttributeName = attributenode.getValue();
    AttributeNameList.add(AttributeName);
    String elementname = root.getName();
    ElementNameList.add(elementname);
    ElementnameandprefixMap.put(elementname, root.getNamespacePrefix());
    Iterator iter = elementlist.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            GetAttributeandElementName(child);
        }
    }
}

public int GetElementsCounts(Element root) {
    List elementlist = new ArrayList();
    elementlist = root.getContent();
    Attribute attributenode = root.getAttribute("DietzPreOrder");
    String AttributeName = attributenode.getName();
    AttributeNameList.add(AttributeName);
    String elementname = root.getName();
    ElementNameList.add(elementname);
    elementcount += 1;
    Iterator iter = elementlist.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            elementcount = GetElementsCounts(child);
        }
    }
    return elementcount;
}

public Map<String, String> GetElementNameandPreDietzEncoding(Element root) {
    this.GetAttributeandElementName(root);
    for (int i = 0; i < ElementNameList.size(); i++) {
        elementnameandpredietzencoding.put(AttributeNameList.get(i), ElementNameList.get(i));
    }
    return elementnameandpredietzencoding;
}

public Map<String, String> GetElementNameandPrefix(Element root) {
    this.GetAttributeandElementName(root);
    return ElementnameandprefixMap;
}

public void GenerateElementsVectorEncodings(int DocID, String FileName) {
    String DietzXMLFile = ProductNewXMLContainsDietzPreOrder(FileName);
    Map<String, String> tempordermap = GetDietzOrderMap(DietzXMLFile);
    Map<String, DietzEncode> ParentMap = GetDietzParentMap(DietzXMLFile);
    for (Map.Entry<String, DietzEncode> entrysets : ParentMap.entrySet()) {
        System.out.println(entrysets.getKey() + " , " + entrysets.getValue().GetParentPre() + " , " + entrysets.getValue().GetParentPost());
    }
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(DietzXMLFile);
        Element root = doc.getRootElement();
        TempElementSpecialName = root.getName();
        elementnameandpredietzencoding = GetElementNameandPreDietzEncoding(root);
        ElementnameandprefixMap = GetElementNameandPrefix(root);
        elementcount = GetElementsCounts(root);
        GetAttributeandElementName(root);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    Map<Integer, int[]> prevectordietzmapping = VectorEncodingFunctions.GetDietzVectorMap(elementcount);
    int[] a = {100000, 100000};
    prevectordietzmapping.put(0, a);
    int[] b = {-100000, -100000};
    prevectordietzmapping.put(elementcount + 1, b);
    for (Map.Entry<String, String> entryset5 : elementnameandpredietzencoding.entrySet()) {
        String tempelementprevectordietzencoding = "[" + prevectordietzmapping.get(Integer.parseInt(entryset5.getKey()))[0] + "," + prevectordietzmapping.get(Integer.parseInt(entryset5.getKey()))[1] + "]";
        String tempelementpostvectordietzencoding = "[" + prevectordietzmapping.get(Integer.parseInt(tempordermap.get(entryset5.getKey())))[0] + "," + prevectordietzmapping.get(Integer.parseInt(tempordermap.get(entryset5.getKey())))[1] + "]";
        String parentelementprevectordietzencoding = "[" + prevectordietzmapping.get(ParentMap.get(entryset5.getKey()).GetParentPre())[0] + "," + prevectordietzmapping.get(ParentMap.get(entryset5.getKey()).GetParentPre())[1] + "]";
        String parentelementpostvectordietzencoding = "[" + prevectordietzmapping.get(ParentMap.get(entryset5.getKey()).GetParentPost())[0] + "," + prevectordietzmapping.get(ParentMap.get(entryset5.getKey()).GetParentPost())[1] + "]";

        TempElementNameList.add(entryset5.getValue());
        TempElementPreVectorDietzEncodingList.add(tempelementprevectordietzencoding);
        TempElementPostVectorDietzEncodingList.add(tempelementpostvectordietzencoding);
        ParentElementPreVectorDietzEncodingList.add(parentelementprevectordietzencoding);
        ParentElementPostVectorDietzEncodingList.add(parentelementpostvectordietzencoding);
        if (entryset5.getValue().equals(TempElementSpecialName)) {
            ParentElementName = "#Document";
        } else {
            ParentElementName = elementnameandpredietzencoding.get(Integer.toString(ParentMap.get(entryset5.getKey()).GetParentPre()));
            ParentElementNameList.add(ParentElementName);
        }
        elementvectordietzencodebean.add(new ElementVectorDietzEncodingBean(DocID, tempelementprevectordietzencoding, tempelementpostvectordietzencoding, entryset5.getValue(), parentelementprevectordietzencoding, parentelementpostvectordietzencoding, ElementnameandprefixMap.get(entryset5.getValue())));
    }

}

public static void main(String args[]) {
    GenerateElementsVectorEncoding encode = new GenerateElementsVectorEncoding();
    ArrayList<ElementVectorDietzEncodingBean> elementlist = encode.getElementBean(0, "xinanjiang_gml.gml");
    // List<NameSpaceVectorEncodeBean> namespacelist = encode.getNameSpaces(0,"xinanjiang_gml.xml");
    for (int i = 0; i < elementlist.size(); i++) {
        ElementVectorDietzEncodingBean elementbean = elementlist.get(i);
        System.out.println("The element name is: " + elementbean.getElementsName());
        System.out.println("The element pre vectordietrz encoding is: " + elementbean.GetPreElementVectorDietzEncoding());
        System.out.println("The element post vectordietz encoding is: " + elementbean.GetPostElementVectorDietzEncoding());
        System.out.println("The element parent pre vectordietz encoding is:" + elementbean.GetPreParentElementVectorDietzEncoding());
        System.out.println("The element parent post vectordietz encoding is:" + elementbean.GetPostParentElementVectorDietzEncoding());
        System.out.println("The element prefix is:" + elementbean.getElementPrefix());
    }
}
}
