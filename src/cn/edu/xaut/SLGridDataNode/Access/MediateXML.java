/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author Greensubmarine
 */

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediateXML {
int PreOrder = 1;

/*create a new instance of MediateXML*/
public MediateXML() {

}

public String ProductXML(URL FilePath) {
    String[] ThisList = FilePath.getFile().split("/");
    int FileLength = ThisList.length;
    String FileName = ThisList[FileLength - 1].toString();
    String OutPutFile = "OutPut" + FileName;
    SAXBuilder builder = new SAXBuilder();
    Document doc = new Document();
    try {
        doc = builder.build(FilePath);
    } catch (JDOMException e) {
        System.err.println(e.getMessage());
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
    //org.jdom.Document XMLDoc = (org.jdom.Document)doc;
    Element root = doc.getRootElement();
    ArrayList<Element> ParentList = new ArrayList<Element>();
    ParentList = ProcessList(root);
    Element oroot = new Element(root.getName());
    oroot = oroot.setAttribute("PreOrder", String.valueOf(PreOrder));
    Document OrderedDocument = new Document(oroot);
    PreOrder += 1;
    // PreOrder = 1;
    for (int m = 0; m < ParentList.size(); m++) {
        ArrayList<Element> ChildList = new ArrayList<Element>();
        Element t = new Element(ParentList.get(m).getName());
        ChildList = ProcessList(ParentList.get(m));
        Element t1 = MakeChild(t, ChildList);
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
        e.printStackTrace();
    }
    System.out.println("XML文档已经成功生成！");
    return OutPutFile;
}

private ArrayList<Element> ProcessList(Element Now) {
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

private Element MakeChild(Element NowElement, ArrayList<Element> TempList) {
    Element NElement = NowElement;
    NElement = NElement.setAttribute("PreOrder", String.valueOf(PreOrder));
    PreOrder++;
    //ArrayList<Element> SecondList = new ArrayList<Element>();
    for (int j = 0; j < TempList.size(); j++) {
        Element CElement = new Element(TempList.get(j).getName());
        Element ChildElement = TempList.get(j);
        ArrayList<Element> TNList = new ArrayList<Element>();
        TNList = ProcessList(ChildElement);
        NElement.addContent(MakeChild(CElement, TNList));
    }
    return NElement;
}
}
