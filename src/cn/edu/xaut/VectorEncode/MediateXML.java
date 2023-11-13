/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.VectorEncode;

/**
 * @author greensubmarine
 */

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MediateXML {
//EncodeExample encode = new EncodeExample();
private int PreOrder = 1;
private static int count = 1;
private static int totalcounts = 0;
private static int newtotalcounts = 0;
private static String attributevalue;

/*create a new instance of MediateXML*/
public MediateXML() {

}

public String Productcode(String FileName) {
    SAXBuilder builder = new SAXBuilder();
    Document doc = new Document();
    try {
        doc = builder.build(FileName);

    } catch (JDOMException e) {
        System.err.println(e.getMessage());
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
    String OutPutFile = "NewOutput" + FileName;
    Element root = doc.getRootElement();
    ArrayList<Element> ParentList = new ArrayList<Element>();
    ParentList = ProcessList(root);
    Element oroot = new Element(root.getName());
    oroot.setAttribute("PreOrder", String.valueOf(PreOrder));
    Document OrderedDocument = new Document(oroot);
    PreOrder += 1;
    for (int m = 0; m < ParentList.size(); m++) {
        ArrayList<Element> ChildList = new ArrayList<Element>();
        Element t = new Element(ParentList.get(m).getName());
        ChildList = ProcessList(ParentList.get(m));
        Element t1 = MakeChild(t, ChildList);
        Element c = OrderedDocument.getRootElement();
        c.addContent(t1);
    }
    XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
    Format format = output.getFormat();
    format.setEncoding("UTF-8");
    format.setExpandEmptyElements(true);
    output.setFormat(format);
    try {
        output.output(OrderedDocument, new FileOutputStream(OutPutFile));
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    System.out.println("新编码文档已成功生成");
    return OutPutFile;
}

public static int ReadXMLElementCounts(String FileName) {
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = new Document();
        doc = builder.build(FileName);
        Element root = doc.getRootElement();
        totalcounts = ElementcountProcess(root);
        return totalcounts;
    } catch (Exception e) {
        System.err.println(e.getMessage());
        return 0;
    }
}

public void GetElementLevel(Element now) {
    List NowList = now.getContent();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            GetElementLevel(child);
        }
    }
    // return totalcounts;
}

private static ArrayList<Element> ProcessList(Element Now) {
    ArrayList<Element> NArrayList = new ArrayList<Element>();
    List NowList = Now.getContent();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            NArrayList.add((Element) obj);
        } else {
            continue;
        }
    }
    return NArrayList;
}

public static Map<Integer, int[]> GetVectorMap(String FileName, int counts) {
    Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    try {
        SAXBuilder builders = new SAXBuilder();
        Document documents = builders.build(FileName);
        Element root = documents.getRootElement();
        //newtotalcounts = ElementcountProcess(root);
        //System.out.println("The totalcounts is :"+newtotalcounts);
        int[][] code = new int[2][counts];
        code[0][0] = 1;
        code[1][0] = 0;
        code[0][counts - 1] = 0;
        code[1][counts - 1] = 1;
        encode(code, 0, counts - 1);
        result = Vectorresult(code);
        return result;
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return null;
    }
}

private static Map<Integer, int[]> vectormap = GetVectorMap("pub.xml", 21);


public String AttributesProcess(String FileName, Element root, int counts) {
    //Element root = encode.Getroot(FileName);
    Map<Integer, int[]> vectormaps = GetVectorMap(FileName.substring(9), counts);
    List CurrentList = root.getContent();
    attributevalue = root.getAttributeValue("PreOrder");
    int attributevalues = Integer.parseInt(attributevalue);
    int[] newcodes = vectormaps.get(attributevalues);
    System.out.println("The original PreOrder is : " + attributevalues + " The new VectorValue is: " + "(" + newcodes[0] + ", " + newcodes[newcodes.length - 1] + ")");
    root.setAttribute("VectorPreOrder", "(" + newcodes[0] + "," + newcodes[newcodes.length - 1] + ")");
    Iterator iter = CurrentList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            attributevalue = child.getAttributeValue("PreOrder");
            attributevalue = AttributeProcess(child);
        }
    }
    return attributevalue;

}

public static String AttributeProcess(Element root) {

    //Map<Integer, int[]> vectormaps = GetVectorMap();

    List CurrentList = root.getContent();
    attributevalue = root.getAttributeValue("PreOrder");
    int attributevalues = Integer.parseInt(attributevalue);
    int[] newcodes = vectormap.get(attributevalues);
    System.out.println("The original PreOrder is : " + attributevalues + " The new VectorValue is: " + "(" + newcodes[0] + ", " + newcodes[newcodes.length - 1] + ")");
    root.setAttribute("VectorPreOrder", "(" + newcodes[0] + "," + newcodes[newcodes.length - 1] + ")");
    Iterator iter = CurrentList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            attributevalue = child.getAttributeValue("PreOrder");
            attributevalue = AttributeProcess(child);
        }
    }
    return attributevalue;
}

public static int ElementcountProcess(Element root) {
    List NowList = root.getContent();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            Element child = (Element) obj;
            count++;
            totalcounts = count;
            totalcounts = ElementcountProcess(child);
        }
    }
    return totalcounts;
}

private Element MakeChild(Element NowElement, ArrayList<Element> TempList) {
    Element NElement = NowElement;
    NElement = NElement.setAttribute("PreOrder", String.valueOf(PreOrder));
    PreOrder++;
    for (int j = 0; j < TempList.size(); j++) {
        Element CElement = new Element(TempList.get(j).getName());
        Element ChildElement = TempList.get(j);
        ArrayList<Element> TNList = new ArrayList<Element>();
        TNList = ProcessList(ChildElement);
        NElement.addContent(MakeChild(CElement, TNList));
    }
    return NElement;
}

public String MakeVectorEncodeFile(String FileName) {
    String newoutputfile = "VectorNewOutput" + FileName;
    String DietzOutPutFile = Productcode(FileName);
    FileInputStream input = null;
    FileOutputStream output = null;
    try {
        input = new FileInputStream(DietzOutPutFile);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(input);
        Element root = doc.getRootElement();
        String value = AttributeProcess(root);
        XMLOutputter outputs = new XMLOutputter(Format.getPrettyFormat());
        output = new FileOutputStream(newoutputfile);
        outputs.output(doc, output);
        return newoutputfile;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

public String GenerateVectorXMLFile(String newxmlfile, int counts) {
    //this.Productcode(XMLFile);
    String newvectoroutputfile = "Vector" + newxmlfile;
    FileInputStream input = null;
    FileOutputStream output = null;
    try {
        input = new FileInputStream(newxmlfile);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(input);
        Element root = doc.getRootElement();
        String value = this.AttributesProcess(newxmlfile, root, counts);
        System.out.println(value);
        XMLOutputter outputs = new XMLOutputter(Format.getPrettyFormat());
        output = new FileOutputStream(newvectoroutputfile);
        outputs.output(doc, output);
        return newvectoroutputfile;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
      /*finally{
          try{
              input.close();
              output.close();
          }catch(Exception e){
              e.printStackTrace();
          }
      }*/
}

/*public static void main(String args[]) throws Exception{
    MediateXML encode = new MediateXML();
    encode.Productcode("pub.xml");
    //encode.MakeVectorEncodeFile("pub.xml");
    FileInputStream input = null;
    FileOutputStream output = null;
    try{
      input = new FileInputStream("NewOutputPub.xml");
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(input);
      Element root = doc.getRootElement();
      String value = AttributeProcess(root);
      XMLOutputter outputs = new XMLOutputter(Format.getPrettyFormat());
      output = new FileOutputStream("NewVectorOutputPub.xml");
      outputs.output(doc, output);

    }catch(Exception e){
        e.printStackTrace();
    }
    finally{
        try{
           input.close();
           output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}*/
public static Map<Integer, int[]> Vectorresult(int[][] code) {
    Map<Integer, int[]> coderesult = new HashMap<Integer, int[]>();
    int rows = code.length;
    int cols = code[0].length;
    int[][] transcode = new int[cols][rows];
    for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
            transcode[i][j] = code[j][i];
        }
    }
    for (int n = 0; n < transcode.length; n++) {
        int[] codearray = new int[2];
        codearray[0] = transcode[n][0];
        codearray[1] = transcode[n][1];
        coderesult.put(n + 1, codearray);
    }
    return coderesult;
}

public static void encode(int[][] code, int start, int end) {
    //这里的code为二维矩阵，用于存放文档的元素的向量编码
    //执行取整运算
    int m = (int) Math.floor((start + end) / 2);
    if ((m < end) && (m > start)) {
        code[0][m] = code[0][start] + code[0][end];
        code[1][m] = code[1][start] + code[1][end];
        encode(code, start, m);
        encode(code, m, end);
    } else {
        return;
    }
}
}

