/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.GIS;

/**
 * @author Administrator
 */

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Test {

public static List<Element> childelements = new ArrayList<Element>();

//获取document对象
public Document getDocument(String xml) throws Exception {

    FileInputStream input = new FileInputStream(xml);

    //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
    //创建一个新的SAXBuilder
    SAXReader sb = new SAXReader();
    //通过输入源构造一个Document
    //Document doc = builder.build(input);
    Document doc = sb.read(input);
    //取的根元素
    return doc;
}

public void jie() {
    String a = "example.xml";
    ArrayList<Element> ParentList = new ArrayList<Element>();

    try {
        Document doc = this.getDocument("weihai.xml");

        Element root = doc.getRootElement();
        showallelements(root);
   /*ParentList = DietzFileElementProcessList(root);
   List<Element> childlist = new ArrayList<Element>();
  for(int i= 0; i < ParentList.size(); i++){  
     childlist = DietzFileElementProcessList(ParentList.get(i));
   }*/
        //System.out.println(root.getName());
        //Element el=this.getDestElement(doc);

        // System.out.println("*********"+el.getText());


    } catch (Exception e) {
        // TODO 自动生成 catch 块
        e.printStackTrace();
    }
}

private void showallelements(Element Now) {
    //ArrayList<Element> templist = new ArrayList<Element>();
    System.out.println(Now.getNamespacePrefix() + ":" + Now.getName());
    List NowList = Now.elements();
    Iterator iter = NowList.iterator();
    while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Element) {
            showallelements((Element) obj);
        }
    }
}


private ArrayList<Element> DietzFileElementProcessList(Element Now) {
    ArrayList<Element> NArrayList = new ArrayList<Element>();
    System.out.println(Now.getNamespacePrefix() + ":" + Now.getName());
    List NowList = Now.content();
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


public static void main(String[] args) {

    Test t = new Test();
    t.jie();
}

}



