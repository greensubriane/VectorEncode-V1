package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;


import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;


public class Query {


public Query() {
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("query.xml"));

        Node node = document.selectSingleNode("/root/sysname");
        String sysname = node.getText();
        String filename = null;


        InputStream is = new FileInputStream("ontoclient.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = reader.readLine(); // 读取第一行


        while (line != null) {
            String owlfilename = null;
            String[] str = line.split("#");
            if (sysname.equals(str[0])) {
                filename = str[1];
            }
            line = reader.readLine(); // 读取下一行


        }

        is = new FileInputStream(filename);
        OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);

        node = document.selectSingleNode("/root/clsname");
        String clsname = node.getText();

        node = document.selectSingleNode("/root/propertyname");
        String propertyname = node.getText();

        node = document.selectSingleNode("/root/vaule");
        String vaule = node.getText();

        node = document.selectSingleNode("/root/relation");
        String relation = node.getText();

        OWLNamedClass cls = owlModel.getOWLNamedClass(clsname);
        Collection instCol = cls.getInstances();

        Iterator it = instCol.iterator();    //针对类实例的循环
        while (it.hasNext()) {
            OWLIndividual inst = (OWLIndividual) it.next();

            node = document.selectSingleNode("/root/result");

            OWLProperty property = null;

            if (propertyname != null && !propertyname.equals("novaule")) {
                property = owlModel.getOWLProperty(propertyname);
            }
            if (property != null) //属性非空
            {
                Object instvaule = inst.getPropertyValue(property);
                System.out.print(property.getName());
                String strvaule = null;
                if (instvaule != null) {
                    if (!property.isObjectProperty()) {
                        strvaule = instvaule.toString();
                    } else {
                        strvaule = instvaule.toString();
                    }
                    if (strvaule.contains(vaule)) {
                        Element elem2 = ((Element) node).addElement("resultdata");
                        Element elem3 = elem2.addElement("name");
                        elem3.setText(inst.getName());

                        Collection ObjectB = cls.getAssociatedProperties();
                        Iterator pt = ObjectB.iterator();
                        while (pt.hasNext()) {
                            OWLProperty prop = (OWLProperty) pt.next();
                            elem3 = elem2.addElement("property");
                            Element elem4 = elem3.addElement("propertyname");
                            elem4.setText(prop.getName());
                            elem4 = elem3.addElement("propertyvaule");
                            System.out.print("--------------" + strvaule + "\n");
                            Object propvaule = inst.getPropertyValue(prop);
                            if (!prop.isObjectProperty()) {
                                if (propvaule != null) {
                                    elem4.setText(propvaule.toString());
                                }
                            } else {
                                if (propvaule != null) {
                                    OWLIndividual propobjectvaule = (OWLIndividual) propvaule;
                                    elem4.setText(propobjectvaule.getName());
                                }
                            }
                        }
                    }
                }
            } else {
                boolean isshow = false;
                if (propertyname.equals("novaule")) {
                    isshow = true;
                }
                Collection ObjectB = cls.getAssociatedProperties();
                Iterator pt = ObjectB.iterator();
                while (pt.hasNext()) {
                    OWLProperty prop = (OWLProperty) pt.next();
                    Object propvaule = inst.getPropertyValue(prop);
                    if (!prop.isObjectProperty()) {
                        if (propvaule != null) {
                            if (propvaule.toString().contains(vaule)) {
                                isshow = true;
                            }
                        }
                    } else {
                        if (propvaule != null) {
                            OWLIndividual propobjectvaule = (OWLIndividual) propvaule;

                            if (propobjectvaule.getName().contains(vaule)) {
                                isshow = true;
                            }
                        }
                    }
                }

                if (isshow) {
                    Element elem2 = ((Element) node).addElement("resultdata");
                    Element elem3 = elem2.addElement("name");
                    elem3.setText(inst.getName());

                    Collection Object = cls.getAssociatedProperties();
                    Iterator jt = Object.iterator();
                    while (jt.hasNext()) {
                        OWLProperty prop = (OWLProperty) jt.next();
                        elem3 = elem2.addElement("property");
                        Element elem4 = elem3.addElement("propertyname");
                        elem4.setText(prop.getName());
                        elem4 = elem3.addElement("propertyvaule");
                        Object propvaule = inst.getPropertyValue(prop);
                        if (!prop.isObjectProperty()) {
                            if (propvaule != null) {
                                elem4.setText(propvaule.toString());
                            }
                        } else {
                            if (propvaule != null) {
                                OWLIndividual propobjectvaule = (OWLIndividual) propvaule;
                                elem4.setText(propobjectvaule.getName());
                            }
                        }
                    }
                }
            }
        }

        try {
            /** 将document中的内容写入文件中 */

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");

            XMLWriter writer = new XMLWriter(new FileWriter(new File("queryresult.xml")), format);//用FileOutputStream更好。
            writer.write(document);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


public static void main(String[] args) {
    new Query();
}
}