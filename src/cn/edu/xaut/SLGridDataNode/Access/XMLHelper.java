/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLHelper {
/**
 * 从文件中获得xml的内存镜像
 *
 * @param fileName
 * @return Document对象代表一个xml文档
 * @throws DocumentException
 */
public static Document parse(String fileName) throws DocumentException {
    //Document document = null;
    SAXReader reader = new SAXReader();
    Document document = reader.read(fileName);
    return document;
}

/**
 * 把字符串转换成document对象
 *
 * @param text
 * @return
 * @throws DocumentException
 */
public static Document parseText(String text) throws DocumentException {
    return DocumentHelper.parseText(text);
}

/**
 * 把document对象转换成字符串
 *
 * @param text
 * @return
 * @throws DocumentException
 */
public static String parseDocument(Document document)
        throws DocumentException {

    return document.asXML();
}

/**
 * 修改指定节点中某个属性的值
 *
 * @param document
 * @param xPath
 * @param attributeName
 * @param newValue
 * @throws DocumentException
 */
public static void modifyAttribute(Document document, String xPath,
                                   String attributeName, String newValue) throws DocumentException {

    XPath xpathSelector = DocumentHelper.createXPath(xPath);

    Element element = (Element) xpathSelector.selectSingleNode(document);

    element.setAttributeValue(attributeName, newValue);
}

/**
 * 删除xPath指定的所有节点，包括子节点。
 *
 * @param document
 * @param xPath
 */
public static void deleteNode(Document document, String xPath) {
    XPath xpathSelector = DocumentHelper.createXPath(xPath);
    List list = xpathSelector.selectNodes(document);
    for (int i = 0; i < list.size(); i++) {
        Node node = (Node) list.get(i);
        node.detach();

    }
}

/**
 * 修改指定节点的值。注意：此节点必须是原子节点，即此节点不能有子节点。
 *
 * @param document
 * @param xPath
 * @param newValue
 */
public static void modifyNodeValue(Document document, String xPath,
                                   String newValue) {
    XPath xpathSelector = DocumentHelper.createXPath(xPath);

    Element element = (Element) xpathSelector.selectSingleNode(document);
    if (element.isTextOnly()) {
        element.setText(newValue);
    } else {
        throw new InvalidXPathException(xPath, "此XPath未选取到原子节点!");
    }

}

/**
 * 获得指定节点的值。 当此节点包含有子节点时获得整个节点的值 当此节点没有子节点时获得节点内的数据
 *
 * @param document
 * @param xPath
 * @return
 */
public static String getNodeValue(Document document, String xPath) {
    XPath xpathSelector = DocumentHelper.createXPath(xPath);
    Element element = (Element) xpathSelector.selectSingleNode(document);
    String result = "";
    if (element.isTextOnly()) {// 判断是否是原子节点
        result = element.getText();
    } else {
        result = element.asXML();

    }

    return result;

}

/**
 * 保存修改结果
 *
 * @param document
 * @param fileName
 * @throws IOException
 */
public static void write(Document document, String fileName)
        throws IOException {
    //这里使用的format后，xml的content节点的内容被格式化，从xml读取图片时会出现问题
    //OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = new XMLWriter(new FileWriter(fileName));
    writer.write(document);
    writer.close();

}

/**
 * 保存修改结果,指定编码
 *
 * @param document
 * @param fileName
 * @throws IOException
 */
public static void writeWithEncoding(Document document, String fileName, String encoding) throws IOException {
    OutputFormat format = OutputFormat.createPrettyPrint();
    format.setEncoding(encoding);
    XMLWriter writer = new XMLWriter(new FileWriter(fileName), format);
    writer.write(document);
    writer.close();
}
}
