/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.VectorEncode;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.*;
//import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class EncodeExample {

/** Creates a new instance of EncodeXML */
static NowVectorPreElementBean nowvectorelementbean = new NowVectorPreElementBean();
static NowVectorPostElementBean nowvectorelementpostbean = new NowVectorPostElementBean();
static ParentVectorPreElementBean parentvectorpreelement = new ParentVectorPreElementBean();
static ParentVectorPostElementBean parentvectorpostelement = new ParentVectorPostElementBean();
public static Map<String, String> vectornamemaps = new HashMap<String, String>();
private MediateXML mediate = new MediateXML();
private static int count = 1;
private DefaultMutableTreeNode XMLTree;
private Map<String, String> OrderMap = new HashMap<String, String>();
private Map<int[], int[]> VectorOrderMap = new HashMap<int[], int[]>();
private static ArrayList<int[]> parentprevector = new ArrayList<int[]>();
private static ArrayList<int[]> parentpostvector = new ArrayList<int[]>();
private static ArrayList<int[]> nowelementprevectorencode = new ArrayList<int[]>();
private static ArrayList<int[]> nowelementpostvectorencode = new ArrayList<int[]>();
private static Map<int[], int[]> nowvectorencode = new HashMap<int[], int[]>();
private Map<String, DietzEncode> ParentMap = new HashMap<String, DietzEncode>();
private Map<int[], VectorDietzEncode> VectorParentMap = new HashMap<int[], VectorDietzEncode>();
//private Map<String, VectorDietzEncode> ParentMaps = new HashMap<String, VectorDietzEncode>();
private static Map<Integer, String> maps = new HashMap<Integer, String>();
public static Map<int[], String> Vectormaps = new HashMap<int[], String>();
private static Map<String, String> testmap = new HashMap<String, String>();
private static int elementcounts = 0;
private int ElementCount = 1;

private Map<String, String> ReadXML(String FileName) {
    SAXBuilder builder = new SAXBuilder();
    try {
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        ArrayList<Element> SecondList = new ArrayList<Element>();
        List TempList = root.getContent();
        ElementCount += root.getContentSize();
        Iterator iterator = TempList.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Element) {
                SecondList.add((Element) o);
            }
        }
        XMLTree = new DefaultMutableTreeNode(root);
        for (int m = 0; m < SecondList.size(); m++) {
            XMLTree.add(MakeXMLTree(SecondList.get(m)));
        }
    }
    // indicates a well-formedness error
    catch (JDOMException e) {
        System.out.println(e.getMessage());
    } catch (IOException e) {
        System.out.println(e);
    }
    Enumeration myEnumeration1 = XMLTree.postorderEnumeration();
    Enumeration myEnumeration2 = XMLTree.preorderEnumeration();
    OrderMap = FunOrderMap(myEnumeration1);
    //ViewMapInfo(OrderMap);
    ParentMap = FunParentMap(myEnumeration2, OrderMap);
    //System.out.println("_______________________________________");
    return Collections.unmodifiableMap(OrderMap);
}


public static int ElementcountProcess(Element root) {
    List NowList = root.getContent();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            count++;
            elementcounts = count;
            elementcounts = ElementcountProcess(child);
        }
    }
    return elementcounts;
}

/*对DIETZ编码进行向量赋值，返回一个数组HashMap*/
public Map<int[], int[]> GetVectorMap(String FileName) {
    //elementcounts = MediateXML.ReadXMLElementCounts(FileName);
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        elementcounts = ElementcountProcess(root);
        System.out.println("the counts is:" + elementcounts);
        Map<Integer, int[]> result = new HashMap<Integer, int[]>();
        int[][] code = new int[2][elementcounts];
        code[0][0] = 1;
        code[1][0] = 0;
        code[0][elementcounts - 1] = 0;
        code[1][elementcounts - 1] = 1;
        MediateXML.encode(code, 0, elementcounts - 1);
        result = MediateXML.Vectorresult(code);
        Map<String, String> TempMap = this.GetOrderMap(FileName);
        for (Map.Entry<String, String> ThisEntry : TempMap.entrySet()) {
            VectorOrderMap.put(result.get(Integer.parseInt(ThisEntry.getKey())), result.get(Integer.parseInt(ThisEntry.getValue())));
        }
        return Collections.unmodifiableMap(VectorOrderMap);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


public Element Getroot(String FileName) {
    Element root;
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        root = doc.getRootElement();
        return root;
    } catch (Exception e) {
        return null;
    }
}

public Map<String, String> GetVectorElementNames(Element root) {
    //Map<String, String> Namemap = null;
    String Elementname;
    Elementname = root.getName().toString();
    String attributevalue = root.getAttributeValue("VectorPreOrder");
    vectornamemaps.put(attributevalue, Elementname);
    List elements = root.getChildren();
    Iterator iter = elements.iterator();
    while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Element) {
            Element child = (Element) o;
            vectornamemaps = GetVectorElementNames(child);
        }
    }
    return vectornamemaps;
}

public Map<int[], String> GetVectorElements(Element root) {
    String Elementname;
    Elementname = root.getName().toString();
    String attributevalue = root.getAttributeValue("VectorPreOrder");
    int a = Integer.parseInt(attributevalue.substring(1, 2));
    int b = Integer.parseInt(attributevalue.substring(3, 4));
    int[] vector = {a, b};
    Vectormaps.put(vector, Elementname);
    List elements = root.getChildren();
    Iterator iter = elements.iterator();
    while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Element) {
            Element child = (Element) o;
            Vectormaps = GetVectorElements(child);
        }
    }
    return Vectormaps;
}


public Map<int[], String> GetVectorElementName(Element root) {
    String Elementname;
    Elementname = root.getName().toString();
    String attributevalue = root.getAttributeValue("VectorPreOrder");
    testmap.put(attributevalue, Elementname);
    int a = Integer.parseInt(attributevalue.substring(1, 2));
    System.out.println("the first element is:" + a);
    int b = Integer.parseInt(attributevalue.substring(3, 4));
    System.out.println("the second element is:" + b);
    int[] vector = {a, b};
    Vectormaps.put(vector, Elementname);
    List elements = root.getChildren();
    //List attributes = root.getAttributes();
    Iterator iter = elements.iterator();
    while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Element) {
            Element child = (Element) o;
            Vectormaps = GetVectorElementName(child);
        }
    }
    return Vectormaps;
}

public String GetElementName(Element root) {
    String elementname;
    elementname = root.getName().toString();
    String attributevalue = root.getAttributeValue("PreOrder");
    maps.put(Integer.parseInt(attributevalue), elementname);
    System.out.println(elementname);
    List elements = root.getChildren();
    //List attributes = root.getAttributes();
    Iterator iter = elements.iterator();
    while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Element) {
            Element child = (Element) o;
            elementname = GetElementName(child);
        }
    }
    return elementname;
}

public Map<Integer, int[]> Getnowvectorencode(String FileName) {
    Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    int[][] code = new int[2][elementcounts];
    code[0][0] = 1;
    code[1][0] = 0;
    code[0][elementcounts - 1] = 0;
    code[1][elementcounts - 1] = 1;
    MediateXML.encode(code, 0, elementcounts - 1);
    result = MediateXML.Vectorresult(code);
    return result;
}

public void Adddocumentlabel(Map<String, DietzEncode> parentmap, Map<Integer, int[]> result) {
    for (Map.Entry<String, DietzEncode> entry : parentmap.entrySet()) {
        if ((entry.getValue().GetParentPre() == 0) || (entry.getValue().GetParentPost() == elementcounts + 1)) {
            int[] rootparentprecode = {10000, 10000};
            int[] rootparentpostcode = {-10000, -10000};
            result.put(0, rootparentprecode);
            result.put(elementcounts + 1, rootparentpostcode);
        }
    }
}

/*获取父节点的向量编码*/
public void GetParentVectorMap(String FileName) {
    //System.out.println("the counts is:"+elementcounts);
    Map<Integer, int[]> result = new HashMap<Integer, int[]>();

    int[][] code = new int[2][elementcounts];
    code[0][0] = 1;
    code[1][0] = 0;
    code[0][elementcounts - 1] = 0;
    code[1][elementcounts - 1] = 1;
    MediateXML.encode(code, 0, elementcounts - 1);
    result = MediateXML.Vectorresult(code);
    Map<String, String> NowTempMap = this.GetOrderMap(FileName);
    for (Map.Entry<String, String> ThisEntry : NowTempMap.entrySet()) {
        nowvectorencode.put(result.get(Integer.parseInt(ThisEntry.getKey())), result.get(Integer.parseInt(ThisEntry.getValue())));
    }
    Map<String, DietzEncode> parenttempmap = this.GetParentMap(FileName);

    for (Map.Entry<String, DietzEncode> entry : parenttempmap.entrySet()) {
        if ((entry.getValue().GetParentPre() == 0) || (entry.getValue().GetParentPost() == elementcounts + 1)) {
            int[] rootparentprecode = {10000, 10000};
            int[] rootparentpostcode = {-10000, -10000};
            result.put(0, rootparentprecode);
            result.put(elementcounts + 1, rootparentpostcode);
        }
        nowelementprevectorencode.add(result.get(Integer.parseInt(entry.getKey())));
        NowVectorPreElementBean.Setnowvectorpreelement(nowelementprevectorencode);
        nowelementpostvectorencode.add(nowvectorencode.get((result.get(Integer.parseInt(entry.getKey())))));
        NowVectorPostElementBean.Setnowvectorpostelement(nowelementpostvectorencode);
        parentprevector.add(result.get(entry.getValue().GetParentPre()));
        ParentVectorPreElementBean.Setparentprevectorelement(parentprevector);
        parentpostvector.add(result.get(entry.getValue().GetParentPost()));
        ParentVectorPostElementBean.Setparentvectorpostelement(parentpostvector);
        System.out.println("当前节点的先序编码是：" + entry.getKey() + " 父节点先序编码是： " + entry.getValue().GetParentPre() + " 父节点后序编码是：" + entry.getValue().GetParentPost());
    }
    testmap.put("(10000,10000)", "Document");
    for (int i = 0; i < nowelementprevectorencode.size(); i++) {
        String vectorvalues = "(" + nowelementprevectorencode.get(i)[0] + "," + nowelementprevectorencode.get(i)[1] + ")";
        String parentvectorvalues = "(" + parentprevector.get(i)[0] + "," + parentprevector.get(i)[1] + ")";
        System.out.println("当前节点的前序向量编码：(" + nowelementprevectorencode.get(i)[0] + "," + nowelementprevectorencode.get(i)[1] + ") "
                                   + "当前元素名称：" + testmap.get(vectorvalues)
                                   + " 当前节点后序向量编码：(" + nowelementpostvectorencode.get(i)[0] + "," + nowelementpostvectorencode.get(i)[1] + ") "
                                   + " 父节点前序向量编码：(" + parentprevector.get(i)[0] + "," + parentprevector.get(i)[1] + ")"
                                   + "父节点元素名称: " + testmap.get(parentvectorvalues)
                                   + " 父节点后序向量编码：("
                                   + parentpostvector.get(i)[0] + "," + parentpostvector.get(i)[1] + ")");
    }
}

public void GetParentVectorMaps(String FileName, int counts) {
    Map<Integer, int[]> result = new HashMap<Integer, int[]>();

    int[][] code = new int[2][counts];
    code[0][0] = 1;
    code[1][0] = 0;
    code[0][counts - 1] = 0;
    code[1][counts - 1] = 1;
    MediateXML.encode(code, 0, counts - 1);
    result = MediateXML.Vectorresult(code);
    Map<String, String> NowTempMap = this.GetOrderMap(FileName);
    for (Map.Entry<String, String> ThisEntry : NowTempMap.entrySet()) {
        nowvectorencode.put(result.get(Integer.parseInt(ThisEntry.getKey())), result.get(Integer.parseInt(ThisEntry.getValue())));
    }
    Map<String, DietzEncode> parenttempmap = this.GetParentMap(FileName);

    for (Map.Entry<String, DietzEncode> entry : parenttempmap.entrySet()) {
        if ((entry.getValue().GetParentPre() == 0) || (entry.getValue().GetParentPost() == counts + 1)) {
            int[] rootparentprecode = {10000, 10000};
            int[] rootparentpostcode = {-10000, -10000};
            result.put(0, rootparentprecode);
            result.put(counts + 1, rootparentpostcode);
        }
        nowelementprevectorencode.add(result.get(Integer.parseInt(entry.getKey())));
        NowVectorPreElementBean.Setnowvectorpreelement(nowelementprevectorencode);
        nowelementpostvectorencode.add(nowvectorencode.get((result.get(Integer.parseInt(entry.getKey())))));
        NowVectorPostElementBean.Setnowvectorpostelement(nowelementpostvectorencode);
        parentprevector.add(result.get(entry.getValue().GetParentPre()));
        ParentVectorPreElementBean.Setparentprevectorelement(parentprevector);
        parentpostvector.add(result.get(entry.getValue().GetParentPost()));
        ParentVectorPostElementBean.Setparentvectorpostelement(parentpostvector);
        //System.out.println("当前节点的先序编码是："+entry.getKey()+" 父节点先序编码是： "+entry.getValue().GetParentPre()+" 父节点后序编码是："+entry.getValue().GetParentPost());
    }
    testmap.put("(10000,10000)", "Document");
        /*for(int i = 0; i< nowelementprevectorencode.size(); i++){
            String vectorvalues = "("+nowelementprevectorencode.get(i)[0]+","+nowelementprevectorencode.get(i)[1]+")";
            String parentvectorvalues = "("+parentprevector.get(i)[0]+","+parentprevector.get(i)[1]+")";
            System.out.println("当前节点的前序向量编码：("+nowelementprevectorencode.get(i)[0]+","+nowelementprevectorencode.get(i)[1]+") "
            + "当前元素名称："+testmap.get(vectorvalues)
            + " 当前节点后序向量编码：("+nowelementpostvectorencode.get(i)[0]+","+nowelementpostvectorencode.get(i)[1]+") "
            + " 父节点前序向量编码：("+parentprevector.get(i)[0]+","+parentprevector.get(i)[1]+")"
            + "父节点元素名称: "+ testmap.get(parentvectorvalues)
            + " 父节点后序向量编码：("
            +parentpostvector.get(i)[0]+","+parentpostvector.get(i)[1]+")");
        }*/
}


public Map<int[], int[]> getVectorOrderMap(String FileName) {
    ReadXML(FileName);
    return Collections.unmodifiableMap(VectorOrderMap);
}

public Map<String, String> GetOrderMap(String FileName) {
    ReadXML(FileName);
    return Collections.unmodifiableMap(OrderMap);
}

public Map<int[], VectorDietzEncode> GetVectorParentMap(String FileName) {
    ReadXML(FileName);
    return Collections.unmodifiableMap(VectorParentMap);
}

public Map<String, DietzEncode> GetParentMap(String FileName) {
    ReadXML(FileName);
    return Collections.unmodifiableMap(ParentMap);
}

private void ViewMapInfo(Map<int[], int[]> TempMap, Map<String, String> TempMap1) {
    for (Map.Entry<String, String> ThisEntry1 : TempMap1.entrySet()) {
        System.out.println("先序DIETZ编码是：" + ThisEntry1.getKey() + " 元素名称是：" + maps.get(Integer.parseInt(ThisEntry1.getKey())) + "   后序DIETZ编码是：" + ThisEntry1.getValue());
    }
    for (Map.Entry<int[], int[]> ThisEntry : TempMap.entrySet()) {

        System.out.println("前序向量编码是：(" + ThisEntry.getKey()[0] + "," + ThisEntry.getKey()[ThisEntry.getKey().length - 1] + ")" + " 后序向量编码是：(" + ThisEntry.getValue()[0] + "," + ThisEntry.getValue()[ThisEntry.getValue().length - 1] + ")");
    }
}

private DefaultMutableTreeNode MakeXMLTree(Element TempNode) {
    DefaultMutableTreeNode SecondLevel = new DefaultMutableTreeNode(TempNode);
    List ChildList = TempNode.getContent();
    ArrayList<Element> ElemList = new ArrayList<Element>();
    Iterator iterator = ChildList.iterator();
    while (iterator.hasNext()) {
        Object o = iterator.next();
        if (o instanceof Element) {
            ElemList.add((Element) o);
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
                //ParentPre=Integer.valueOf(((Element)(((DefaultMutableTreeNode)(TE.getParent())).getUserObject())).getAttributeValue("PreOrder"));
                OMap.put(ElementNode.getAttributeValue("PreOrder"), String.valueOf(EPostOrder));
                EPostOrder++;
            } else {
                OMap.put(ElementNode.getAttributeValue("PreOrder"), String.valueOf(EPostOrder));
                ElementCount = EPostOrder + 1;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
        }
    }
    return PMap;
}

public static void main(String args[]) {
    EncodeExample example = new EncodeExample();
    Element roots = example.Getroot("NewOutputpub.xml");
    Element vectorroots = example.Getroot("NewVectorOutputpub.xml");
    String names = example.GetElementName(roots);
    //String vectornames = example.GetVectorElementName(vectorroots);
    Map<int[], String> vectormaps = example.GetVectorElementName(vectorroots);
    for (Map.Entry<int[], String> temvectormap : vectormaps.entrySet()) {
        System.out.println("请注意1：[" + temvectormap.getKey()[0] + "," + temvectormap.getKey()[1] + "] , " + temvectormap.getValue());
    }

    for (Map.Entry<Integer, String> temmap : maps.entrySet()) {
        System.out.println("请注意：" + temmap.getKey() + ", " + temmap.getValue());
    }
    example.ViewMapInfo(example.GetVectorMap("NewOutputpub.xml"), example.GetOrderMap("NewOutputpub.xml"));
    example.GetParentVectorMap("NewOutputpub.xml");
    System.out.println("该数组的长度为：" + nowelementprevectorencode.size());
    Element originalroot = example.Getroot("pub.xml");
    //String elementnamess = GetAttribiteName(originalroot);

}

/**
 * @return the mediate
 */
public MediateXML getMediate() {
    return mediate;
}

/**
 * @param mediate the mediate to set
 */
public void setMediate(MediateXML mediate) {
    this.mediate = mediate;
}
}


