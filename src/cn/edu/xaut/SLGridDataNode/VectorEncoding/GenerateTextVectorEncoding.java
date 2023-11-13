/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.VectorEncoding;

/**
 * @author He Ting
 */

import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextPreVectorDietzEncodingBean;
import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.TextVectorDietzEncodingBean;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.util.*;

public class GenerateTextVectorEncoding {
private ArrayList<TextPreVectorDietzEncodingBean> textlists = new ArrayList<>();
private ArrayList<TextVectorDietzEncodingBean> textvectordietzencodebean = new ArrayList<TextVectorDietzEncodingBean>();
GenerateElementsVectorEncoding elementvectorencoding = new GenerateElementsVectorEncoding();
private Map<String, String> TempElementDietzOrder = new HashMap<String, String>();
private Map<Integer, int[]> Vectormaps = new HashMap<Integer, int[]>();
private Map<int[], String> TextNameandVectorDietzPreOrderMap = new HashMap<int[], String>();
private Map<int[], String> TextNameandVectorDietzPostOrderMap = new HashMap<int[], String>();

private int TextPreOrder = 0;
private int elementcount = 0;


public int GetElementCount(Element root) {

    List elementlist = new ArrayList();
    elementlist = root.getContent();
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

public ArrayList<TextVectorDietzEncodingBean> getTextBean(int DocID, String FileName) {
    this.GenerateTextPreandPostVectorEncoding(DocID, FileName);
    return textvectordietzencodebean;
}

public void GetTextName(int DocumentID, Element root) {
    List elementlist = new ArrayList();
    elementlist = root.getContent();
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


public void GenerateTextPreandPostVectorEncoding(int DocID, String FileName) {
    String DietzXMLFile = elementvectorencoding.ProductNewXMLContainsDietzPreOrder(FileName);
    try {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(FileName);
        Element root = doc.getRootElement();
        elementcount = this.GetElementCount(root);
        System.out.println("The Element count is :" + elementcount);
        this.GetTextName(DocID, root);
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    TempElementDietzOrder = elementvectorencoding.GetDietzOrderMap(DietzXMLFile);
    Vectormaps = VectorEncodingFunctions.GetDietzVectorMap(elementcount);
    if (textlists == null) {
        System.out.println("data transfer failer");
    } else {
        Iterator textlistiter = textlists.iterator();
        while (textlistiter.hasNext()) {
            TextPreVectorDietzEncodingBean textprebean = (TextPreVectorDietzEncodingBean) textlistiter.next();
            String PreVectorTextEncoding = "[" + Vectormaps.get(Integer.parseInt(textprebean.getTextPreVectorDietzEncode()))[0] + "," + Vectormaps.get(Integer.parseInt(textprebean.getTextPreVectorDietzEncode()))[1] + "]";
            TextNameandVectorDietzPreOrderMap.put(Vectormaps.get(Integer.parseInt(textprebean.getTextPreVectorDietzEncode())), textprebean.getTextValue());
            String TempElementPostDietzOrder = TempElementDietzOrder.get(textprebean.getTextPreVectorDietzEncode());
            String PostVectorTextEncoding = "[" + Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder))[0] + "," + Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder))[1] + "]";
            TextNameandVectorDietzPostOrderMap.put(Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder)), textprebean.getTextValue());
            textvectordietzencodebean.add(new TextVectorDietzEncodingBean(DocID, PreVectorTextEncoding, textprebean.getTextValue(), PostVectorTextEncoding));
        }
    }
}


public static void main(String args[]) {
    GenerateTextVectorEncoding textencode = new GenerateTextVectorEncoding();
    ArrayList<TextVectorDietzEncodingBean> textvectordietzencodebeans = textencode.getTextBean(0, "xinanjiang_gml.gml");
    for (int j = 0; j < textvectordietzencodebeans.size(); j++) {
        System.out.println("The Text value is:" + textvectordietzencodebeans.get(j).getTextValue());
        System.out.println("The Text pre vectordietz encoding is:" + textvectordietzencodebeans.get(j).getTextPreVectorDietzEncode());
        System.out.println("The Text post vectordietz encoding is:" + textvectordietzencodebeans.get(j).getTextPostVectorDietzEncode());
    }
}

}
