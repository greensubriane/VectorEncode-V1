package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class update {
public update() {
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("update.xml"));

        Node node = document.selectSingleNode("/root/filename");
        String filename = node.getText();
        InputStream is = new FileInputStream(filename);
        OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);

        node = document.selectSingleNode("/root/clsname");
        String clsname = node.getText();
        OWLNamedClass cls = owlModel.getOWLNamedClass(clsname);

        if (cls != null) {
            node = document.selectSingleNode("/root/instname");
            String instname = node.getText();

            OWLIndividual inst = owlModel.getOWLIndividual(instname);
            if (inst == null) {
                inst = (OWLIndividual) cls.createInstance(instname);
            }

            List list = document.selectNodes("/root/property");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element ownerElement = (Element) it.next();

                node = ownerElement.selectSingleNode("propertyname");
                String propname = node.getText();

                node = ownerElement.selectSingleNode("propertyvaule");
                String propvaule = node.getText();
                if (propname != null && propvaule != null) {
                    OWLProperty prop = owlModel.getOWLProperty(propname);

                    if (prop != null) {
                        if (!prop.isObjectProperty())//设置data属性值
                        {
                            try {
                                inst.setPropertyValue(prop, propvaule);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            OWLIndividual instvaule = owlModel.getOWLIndividual(propvaule);//设置实例属性值
                            if (instvaule != null) {
                                try {
                                    inst.setPropertyValue(prop, instvaule);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
            Collection errors = new ArrayList();
            jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
            System.out.println("File saved with " + errors.size() + " errors.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
}