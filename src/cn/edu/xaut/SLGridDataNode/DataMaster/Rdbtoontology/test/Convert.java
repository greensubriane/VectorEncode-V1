package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Convert {
private JFrame frame = null;
private JPanel pane = null, paneA = null, paneB = null;
private JScrollPane s_panA = null, s_panB = null;
private JButton button_1 = null, button_2 = null;
private JComboBox[] onto1combox = new JComboBox[40];
private JLabel[] onto1name = new JLabel[40];
private JLabel[] onto2name = new JLabel[40];
private JTextArea[] onto1text = new JTextArea[40], onto2text = new JTextArea[40];
private JLabel label1 = new JLabel("本体数据转换"), label2 = new JLabel("概念A"), label3 = new JLabel("概念B");
private String projectpath = null;

private DefaultListModel listMode = new DefaultListModel();
private JList list = new JList(listMode);
private String owlfile1, owlfile2, file1name, file2name;

public OWLModel owlModelA, owlModelB;
private OWLNamedClass ClassNameA = null, ClassNameB = null;
private String listnameA = null, listnameB = null;
private String ontoApre = null, ontoBpre = null;  //result.txt文件前缀
private String ontoAprex = null, ontoBprex = null;   //本体模型前缀
private int propertyNo = 0;
public static JFrame parent = null;  //用于返回父窗体

private MenuBar mb = new MenuBar();
private Menu m1 = new Menu("数据转换");
private MenuItem selectitem = new MenuItem("选定转换项目"), dateinput = new MenuItem("填写原始数据"), convertmenu = new MenuItem("转换"), exit = new MenuItem("退出");

public Convert() {
    try {
        frame = new JFrame("JTableTest");
        pane = new JPanel();
        paneA = new JPanel();
        paneA.setLayout(null);
        paneB = new JPanel();
        paneB.setLayout(null);

        File convertfile = new File("abc.txt");
        String path = convertfile.getAbsolutePath();
        projectpath = path.substring(0, path.length() - 7);
        projectpath = projectpath.replaceAll("\\\\", "/");

        mb.add(m1);
        m1.add(selectitem);
        m1.add(dateinput);
        m1.add(convertmenu);
        m1.add(exit);

        convertmenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    convertonto();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        dateinput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (listnameA != null && listnameB != null) {
                        getowlfilename();
                        getfilename();
                        owlModelA = loadonto(owlfile1);
                        owlModelB = loadonto(owlfile2);
                        OWLNamespaceManager nmgerA = (OWLNamespaceManager) owlModelA.getNamespaceManager();
                        ontoAprex = nmgerA.getDefaultNamespace();
                        OWLNamespaceManager nmgerB = (OWLNamespaceManager) owlModelB.getNamespaceManager();
                        ontoBprex = nmgerB.getDefaultNamespace();

                        //	ClassNameA =owlModelA.getOWLNamedClass(nameA);
                        //	ClassNameB =owlModelB.getOWLNamedClass(nameB);
                        ClassNameA = owlModelA.getOWLNamedClass(listnameA);
                        ClassNameB = owlModelB.getOWLNamedClass(listnameB);
                        if (ClassNameA != null && ClassNameA != null) {
                            displayonttoA();
                        } else {
                            JOptionPane.showMessageDialog(null, "未找到该类", "", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "请选择要转换的项目", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        selectitem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Name = (String) list.getSelectedValue().toString();
                String[] item = Name.split("-");
                listnameA = item[0];
                listnameB = item[1];
                getowlfilename();
                getfilename();
                label2.setText("概念A：" + listnameA);
                label3.setText("概念B：" + listnameB);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                frame.dispose();
            }
        });

        label1.setSize(new Dimension(100, 100));
        label1.setBounds(new Rectangle(480, 0, 300, 80));
        label1.setFont(label1.getFont().deriveFont(30f));

        label2.setSize(new Dimension(100, 100));
        label2.setBounds(new Rectangle(320, 40, 200, 60));
        label2.setFont(label2.getFont().deriveFont(25f));

        label3.setSize(new Dimension(100, 100));
        label3.setBounds(new Rectangle(820, 40, 200, 60));
        label3.setFont(label3.getFont().deriveFont(25f));


        list.setBounds(new Rectangle(10, 100, 150, 520));
        list.setBackground(Color.lightGray);
        readconvertitem();

        list.addListSelectionListener(new ListSelectionListener() {
                                          public void valueChanged(ListSelectionEvent e) {
                                              JList list = (JList) e.getSource();
                                              String Name = (String) list.getSelectedValue().toString();
                                              String[] item = Name.split("-");
                                              listnameA = item[0];
                                              listnameB = item[1];
                                              getfilename();
                                              label2.setText("概念A：" + listnameA);
                                              label3.setText("概念B：" + listnameB);
                                          }
                                      }
        );


        s_panA = new JScrollPane(paneA);
        s_panA.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s_panA.setBounds(new Rectangle(170, 100, 500, 520));

        s_panB = new JScrollPane(paneB);
        s_panB.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        s_panB.setBounds(new Rectangle(680, 100, 500, 520));

        pane.setLayout(null);
        pane.add(list);
        pane.add(label1);
        pane.add(label2);
        pane.add(label3);
        pane.add(s_panA);
        pane.add(s_panB);

        frame.getContentPane().add(pane);
        frame.setMenuBar(mb);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocation(50, 150);
        frame.setTitle("异构系统本体集成平台——概念转换");
        frame.setResizable(false);
        frame.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


public static void main(String[] args) {
    Convert f = new Convert();
}

public Convert(boolean isinit) {

}

public void loadOWLModelA(String filename) {
    try {
        file1name = filename.substring(0, filename.length() - 4);
        InputStream is = new FileInputStream(filename);
        owlModelA = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        OWLNamespaceManager nmgerA = (OWLNamespaceManager) owlModelA.getNamespaceManager();
        ontoAprex = nmgerA.getDefaultNamespace();
    } catch (Exception ex) {
        ex.printStackTrace();
    }

}

public void loadOWLModelB(String filename) {
    try {
        file2name = filename.substring(0, filename.length() - 4);
        InputStream is = new FileInputStream(filename);
        owlModelB = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        OWLNamespaceManager nmgerB = (OWLNamespaceManager) owlModelB.getNamespaceManager();
        ontoBprex = nmgerB.getDefaultNamespace();
    } catch (Exception ex) {
        ex.printStackTrace();
    }

}

public OWLModel loadonto(String filepath) throws Exception {
    InputStream is = new FileInputStream(filepath);
    OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
    return owlModel;
}

public void getfilename() {
    try {
        int index = owlfile1.lastIndexOf("\\");
        file1name = owlfile1.substring(index + 1, owlfile1.length());
        file1name = file1name.substring(0, file1name.length() - 4);
        index = owlfile2.lastIndexOf("\\");
        file2name = owlfile2.substring(index + 1, owlfile2.length());
        file2name = file2name.substring(0, file2name.length() - 4);
    } catch (Exception ex) {

        ex.printStackTrace();
    }
}

private void getowlfilename() {
    try {
        String line = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream("Convert.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行


        while (line != null) {
            String[] item = line.split("#");
            if (listnameA.equals(item[0]) && listnameB.equals(item[1])) {
                owlfile1 = item[2];
                owlfile2 = item[3];
            }
            line = reader.readLine(); // 读取下一行
        }

        reader.close();


    } catch (Exception ex) {

        ex.printStackTrace();
    }

}

private void readconvertitem() {
    try {
        String line = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream("Convert.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行

        try {
            listMode.removeAllElements();
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        while (line != null) {
            String[] item = line.split("#");
            listMode.addElement(item[0] + "-" + item[1]);
            line = reader.readLine(); // 读取下一行
        }

        reader.close();


    } catch (Exception ex) {

        ex.printStackTrace();
    }

}


public void creatclsxml(String nameA, String nameB) {
    try {
        String filenameA = null, filenameB = null;
        InputStream is = new FileInputStream("ontoclient.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = reader.readLine(); // 读取第一行

        while (line != null) {
            String[] str = line.split("#");
            if (nameA.equals(str[0])) {
                filenameA = str[1];
            }
            if (nameB.equals(str[0])) {
                filenameB = str[1];
            }

            line = reader.readLine(); // 读取下一行
        }
        if (filenameA != null && filenameB != null) {
            loadOWLModelB(filenameB);

            Document document = DocumentHelper.createDocument();  //创建类名xml文件
            Element rootElement = document.addElement("root");
            Element FirstElement = rootElement.addElement("filenameA");
            FirstElement.setText(filenameA);
            FirstElement = rootElement.addElement("filenameB");
            FirstElement.setText(filenameB);


            Collection ObjectB = owlModelB.getUserDefinedOWLNamedClasses();
            Iterator it = ObjectB.iterator();
            while (it.hasNext()) {
                OWLNamedClass cls = (OWLNamedClass) it.next();
                String clsname = cls.getName();

                if (!clsname.contains(".")) {
                    FirstElement = rootElement.addElement("class");

                    Element SecondElement = FirstElement.addElement("classname");
                    if (clsname != null) {
                        SecondElement.setText(clsname);
                    }

                    getsubcls(cls, FirstElement);
                }
            }
            try {
                /** 将document中的内容写入文件中 */

                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("GBK");

                XMLWriter writer = new XMLWriter(new FileWriter(new File("objectclass.xml")), format);
                writer.write(document);
                writer.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void getsubcls(OWLNamedClass cls, Element parenrelem) {
    for (Iterator it = cls.getSubclasses(false).iterator(); it.hasNext(); ) {
        OWLNamedClass subclass = (OWLNamedClass) it.next();

        String clsname = subclass.getName();

        Element FirstElement = parenrelem.addElement("class");

        Element SecondElement = FirstElement.addElement("classname");
        if (clsname != null) {
            SecondElement.setText(clsname);
        }

        getsubcls(subclass, FirstElement);
    }
}


public void creatclspropertyxml(String nameA, String nameB, String nameC) {
    try {
        String filenameA = null, filenameB = null;
        InputStream is = new FileInputStream("ontoclient.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = reader.readLine(); // 读取第一行

        while (line != null) {
            String[] str = line.split("#");
            if (nameA.equals(str[0])) {
                filenameA = str[1];
            }
            if (nameB.equals(str[0])) {
                filenameB = str[1];
            }

            line = reader.readLine(); // 读取下一行
        }
        if (filenameA != null && filenameB != null) {
            loadOWLModelA(filenameA);
            loadOWLModelB(filenameB);
        }

        Document document = DocumentHelper.createDocument();  //创建xml文件
        Element rootElement = document.addElement("root");

        Element FirstElement = rootElement.addElement("filenameA");
        FirstElement.setText(filenameA);
        FirstElement = rootElement.addElement("filenameB");
        FirstElement.setText(filenameB);

        //rootElement.addComment("本体概念转换数据");

        FirstElement = rootElement.addElement("clsName");
        FirstElement.setText(nameC);
        FirstElement = rootElement.addElement("clsconvertName");
        FirstElement.setText(nameC);

        OWLNamedClass cls = owlModelB.getOWLNamedClass(nameC);

        Collection ObjectB = cls.getAssociatedProperties();
        int i = 1;
        Iterator it = ObjectB.iterator();
        while (it.hasNext()) {
            OWLProperty property = (OWLProperty) it.next();
            boolean isobject = property.isObjectProperty();
            if (isobject) {
                if (property.isFunctional()) {
                    //动态添加属性标签
                    String name = property.getName().toString();

                    FirstElement = rootElement.addElement("property");
                    Element SecondElement = FirstElement.addElement("propertyname");
                    SecondElement.setText(name);
                    SecondElement = FirstElement.addElement("Convertpropertyname");
                    SecondElement = FirstElement.addElement("propertyVaule");
                    SecondElement = FirstElement.addElement("ConvertPropertyVaule");
                    SecondElement = FirstElement.addElement("propertyType");
                    SecondElement.setText("object");
                    SecondElement = FirstElement.addElement("VauleType");

                    i = i + 1;
                } else   //非函数属性
                {
                    //动态添加属性标签
                    String name = property.getName().toString();

                    FirstElement = rootElement.addElement("property");
                    Element SecondElement = FirstElement.addElement("propertyname");
                    SecondElement.setText(name);
                    SecondElement = FirstElement.addElement("Convertpropertyname");
                    SecondElement = FirstElement.addElement("propertyVaule");
                    SecondElement = FirstElement.addElement("ConvertPropertyVaule");
                    SecondElement = FirstElement.addElement("propertyType");
                    SecondElement.setText("object");
                    SecondElement = FirstElement.addElement("VauleType");

                    i = i + 1;
                }
            } else {
                if (!property.isFunctional()) {
                    //动态添加属性标签
                    String name = property.getName().toString();

                    FirstElement = rootElement.addElement("property");
                    Element SecondElement = FirstElement.addElement("propertyname");
                    SecondElement.setText(name);
                    SecondElement = FirstElement.addElement("Convertpropertyname");
                    SecondElement = FirstElement.addElement("propertyVaule");
                    SecondElement = FirstElement.addElement("ConvertPropertyVaule");
                    SecondElement = FirstElement.addElement("propertyType");
                    SecondElement.setText("data");

                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname.equals("xsd:int")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("整数");
                        } else if (clsname.equals("xsd:float")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("实数");
                        } else if (clsname.equals("xsd:boolean")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("逻辑");
                        } else if (clsname.equals("xsd:string")) {
                            Element ThirdElement = FirstElement.addElement("propertyname");
                            ThirdElement.setText("字符串");
                        } else if (clsname.equals("xsd:date")) {
                            SecondElement = FirstElement.addElement("propertyType");
                            SecondElement.setText("日期");
                        } else if (clsname.equals("xsd:time")) {
                            SecondElement = FirstElement.addElement("propertyType");
                            SecondElement.setText("时间");
                        } else if (clsname.equals("xsd:dateTime")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("日期时间");
                        } else {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                        }

                    }
                    i = i + 1;
                } else {   //非函数属性
                    //动态添加属性标签
                    String name = property.getName().toString();

                    FirstElement = rootElement.addElement("property");
                    Element SecondElement = FirstElement.addElement("propertyname");
                    SecondElement.setText(name);
                    SecondElement = FirstElement.addElement("Convertpropertyname");
                    SecondElement = FirstElement.addElement("propertyVaule");
                    SecondElement = FirstElement.addElement("ConvertPropertyVaule");
                    SecondElement = FirstElement.addElement("propertyType");
                    SecondElement.setText("data");


                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname.equals("xsd:int")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("整数");
                        } else if (clsname.equals("xsd:float")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("实数");

                        } else if (clsname.equals("xsd:boolean")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("逻辑");
                        } else if (clsname.equals("xsd:string")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("字符串");
                        } else if (clsname.equals("xsd:date")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("日期");

                        } else if (clsname.equals("xsd:time")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("时间");

                        } else if (clsname.equals("xsd:dateTime")) {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                            ThirdElement.setText("日期时间");
                        } else {
                            Element ThirdElement = FirstElement.addElement("VauleType");
                        }

                    }

                    i = i + 1;

                }
            }
        }

        propertyNo = i;

        try {
            /** 将document中的内容写入文件中 */

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");

            XMLWriter writer = new XMLWriter(new FileWriter(new File("convertname.xml")), format);//用FileOutputStream更好。
            writer.write(document);
            writer.close();
            writer = null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File("convertname.xml"));
            XMLWriter writer = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
            writer = new XMLWriter(new FileWriter(new File("convertname.xml")), format);
            writer.write(document);
            writer.close();
            writer = null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


public void creatallnameXML(String sysname) {
    try {
        Document document = DocumentHelper.createDocument();  //创建xml文件
        Element rootElement = document.addElement("root");

        InputStream is = new FileInputStream("ontoclient.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = reader.readLine(); // 读取第一行

        while (line != null) {
            String owlfilename = null;
            String[] str = line.split("#");
            if (sysname.equals(str[0])) {
                owlfilename = str[1];
                Element element = rootElement.addElement("filenameA");
                element.setText(owlfilename);
            }
            line = reader.readLine(); // 读取下一行

            //rootElement.addComment("本体概念转换数据");
            Element FirstElement = rootElement.addElement("object");
            Element SecondElement = FirstElement.addElement("name");
            SecondElement.setText(str[0]);

            SecondElement = FirstElement.addElement("owlfilename");
            SecondElement.setText(str[1]);
        }
        try {
            /** 将document中的内容写入文件中 */

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");

            XMLWriter writer = new XMLWriter(new FileWriter(new File("allname.xml")), format);//用FileOutputStream更好。
            writer.write(document);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File("allname.xml"));
            XMLWriter writer = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
            writer = new XMLWriter(new FileWriter(new File("allname.xml")), format);
            writer.write(document);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void convertontoXML() {
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("convert.xml"));
        /** 修改内容之一: 如果book节点中show属性的内容为yes,则修改成no */
        /** 先用xpath查找对象 */

        Node node = document.selectSingleNode("/root/filenameA");
        String filenameA = node.getText();


        node = document.selectSingleNode("/root/filenameB");
        String filenameB = node.getText();


        node = document.selectSingleNode("/root/clsName");
        String oldvaule = node.getText();

        file1name = filenameA.substring(0, filenameA.length() - 4);
        file2name = filenameB.substring(0, filenameB.length() - 4);

        String convertvaule = findsimonto(oldvaule);


        if (convertvaule != null && filenameA != null && filenameB != null) {
            //loadOWLModelA(filenameA);
            //loadOWLModelB(filenameB);
            file1name = filenameA.substring(0, filenameA.length() - 4);
            file2name = filenameB.substring(0, filenameB.length() - 4);

            node = document.selectSingleNode("/root/clsconvertName");
            node.setText(convertvaule);

            List list = document.selectNodes("/root/property");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element ownerElement = (Element) it.next();

                node = ownerElement.selectSingleNode("propertyname");
                oldvaule = node.getText();
                convertvaule = findsimonto(oldvaule);
                if (convertvaule != null)     //具有对应属性
                {
                    node = ownerElement.selectSingleNode("Convertpropertyname");
                    node.setText(convertvaule);

                    node = ownerElement.selectSingleNode("propertyVaule");
                    oldvaule = node.getText();
                    if (!oldvaule.equals("")) {
                        node = ownerElement.selectSingleNode("propertyType");
                        String proptype = node.getText();
                        node = ownerElement.selectSingleNode("VauleType");
                        String vauletype = node.getText();
                        if (proptype.equals("object")) {
                            convertvaule = findsimonto(oldvaule);
                            node = ownerElement.selectSingleNode("ConvertPropertyVaule");
                            node.setText(convertvaule);
                        }

                        if (proptype.equals("data")) {
                            if (vauletype.equals("整数")) {
                                node = ownerElement.selectSingleNode("ConvertPropertyVaule");
                                node.setText(oldvaule);
                            }
                            //其他的省略
                            node = ownerElement.selectSingleNode("ConvertPropertyVaule");
                            node.setText(oldvaule);
                        }
                    } else {
                        node = ownerElement.selectSingleNode("ConvertPropertyVaule");
                        node.setText("No input vaule!");
                    }

                } else {
                    node = ownerElement.selectSingleNode("Convertpropertyname");
                    node.setText("Cann't find property");
                    node = ownerElement.selectSingleNode("ConvertPropertyVaule");
                    node.setText("Cann't convert");
                }
            }
        }

        try {
            /** 将document中的内容写入文件中 */

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");

            XMLWriter writer = new XMLWriter(new FileWriter(new File("convertresult.xml")), format);//用FileOutputStream更好。
            writer.write(document);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            saxReader = new SAXReader();
            document = saxReader.read(new File("convertresult.xml"));
            XMLWriter writer = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
            writer = new XMLWriter(new FileWriter(new File("convertresult.xml")), format);
            writer.write(document);
            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } catch (Exception ex) {

        ex.printStackTrace();
    }
}

public void convertonto() {
    try {
        paneB.removeAll();
        paneB.repaint();

        for (int i = 1; i <= propertyNo; i++) {
            String propertyA = null, propertyB = null, VauleA = null, VauleB = null;


            try {
                propertyA = onto1name[i].getText();
            } catch (Exception ex) {

            }

            if (propertyA != null && propertyA.contains("：")) {
                propertyA = propertyA.substring(0, propertyA.lastIndexOf("："));
            }

            if (propertyA != null) {
                propertyB = findsimonto(propertyA);
            }

            if (propertyB != null) {
                onto2name[i] = new JLabel();
                onto2text[i] = new JTextArea();

                OWLProperty property = owlModelB.getOWLProperty(propertyB);
                if (property != null) {
                    boolean isobject = property.isObjectProperty();
                    if (isobject) {
                        if (property.isFunctional()) {
                            //动态添加属性标签
                            int x = getposx(i);
                            int y = getposy(i);
                            VauleA = onto1combox[i].getSelectedItem().toString();

                            String name = property.getName().toString();
                            onto2name[i].setBounds(new Rectangle(x, y, 120, 40));
                            onto2name[i].setText(name + "：");
                            onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                            paneB.add(onto2name[i]);

                            //找相似的概念
                            VauleB = findsimonto(VauleA);
                            if (VauleB != null) {
                                OWLIndividual instanB = owlModelB.getOWLIndividual(VauleB);
                                OWLNamedClass clsB = owlModelB.getOWLNamedClass(VauleB);


                                if (clsB != null) {
                                    onto2text[i] = new JTextArea();
                                    onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                    onto2text[i].setEditable(false);
                                    onto2text[i].setBackground(Color.lightGray);
                                    onto2text[i].setText(clsB.getName());
                                    paneB.add(onto2text[i]);
                                } else if (instanB != null) {
                                    onto2text[i] = new JTextArea();
                                    onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                    onto2text[i].setEditable(false);
                                    onto2text[i].setBackground(Color.lightGray);
                                    onto2text[i].setText(instanB.getName());
                                    paneB.add(onto2text[i]);
                                } else {
                                    onto2text[i] = new JTextArea();
                                    onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                    onto2text[i].setEditable(false);
                                    onto2text[i].setBackground(Color.lightGray);
                                    onto2text[i].setText("未找到相似的内容");
                                    paneB.add(onto2text[i]);
                                }
                            } else {
                                onto2text[i] = new JTextArea();
                                onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                onto2text[i].setEditable(false);
                                onto2text[i].setBackground(Color.lightGray);
                                onto2text[i].setText("未找到相似的内容");
                                paneB.add(onto2text[i]);
                            }
                        } else {    //非函数属性
                            //动态添加属性标签
                            int x = getposx(i);
                            int y = getposy(i);
                            String name = property.getName().toString();
                            onto2name[i].setBounds(new Rectangle(x, y, 120, 40));
                            onto2name[i].setText(name + "：");
                            onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                            paneB.add(onto2name[i]);

                            onto2text[i] = new JTextArea();
                            onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                            onto2text[i].setEditable(false);
                            onto2text[i].setBackground(Color.lightGray);
                            paneB.add(onto2text[i]);

                            VauleA = onto1text[i].getText();
                            String content = "";
                            String[] vaule = VauleA.split(";");
                            for (int m = 0; m < vaule.length; m++) {
                                //找相似的概念

                                VauleB = findsimonto(vaule[m]);


                                if (VauleB != null) {
                                    OWLNamedClass clsB = owlModelB.getOWLNamedClass(VauleB);
                                    OWLIndividual instanB = owlModelB.getOWLIndividual(VauleB);

                                    if (clsB != null) {
                                        if (content.equals(""))   //填写内容
                                        {
                                            content = clsB.getName();
                                        } else {
                                            content = content + ";" + clsB.getName();
                                        }
                                    } else if (instanB != null) {
                                        if (content.equals(""))   //填写内容
                                        {
                                            content = instanB.getName();
                                        } else {
                                            content = content + ";" + instanB.getName();
                                        }
                                    } else {
                                        if (content.equals(""))   //填写内容
                                        {
                                            content = "No";
                                        } else {
                                            content = content + ";" + "No";
                                        }
                                    }

                                    onto2text[i].setText(content);
                                } else {
                                    onto2text[i] = new JTextArea();
                                    onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                    onto2text[i].setEditable(false);
                                    onto2text[i].setBackground(Color.lightGray);
                                    onto2text[i].setText("No");
                                    paneB.add(onto2text[i]);
                                }
                            }

                            onto2text[i].setText(content);

                        }

                    } else {
                        if (property.isFunctional())  //是否函数属性
                        {
                            //动态添加属性标签
                            int x = getposx(i);
                            int y = getposy(i);

                            try {
                                VauleA = onto1combox[i].getSelectedItem().toString();
                            } catch (Exception ex) {

                            }

                            if (VauleA != null)   //数值属性未填写内容的处理
                            {
                                String name = property.getName().toString();
                                onto2name[i].setBounds(new Rectangle(x, y - 10, 120, 40));
                                onto2name[i].setText(propertyB);
                                onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                                paneB.add(onto2name[i]);


                                onto2text[i] = new JTextArea();
                                onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                onto2text[i].setEditable(false);
                                onto2text[i].setBackground(Color.lightGray);
                                onto2text[i].setText(VauleA);
                                paneB.add(onto2text[i]);
                            } else {
                                String name = property.getName().toString();
                                onto2name[i].setBounds(new Rectangle(x, y - 10, 120, 40));
                                onto2name[i].setText(propertyB);
                                onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                                paneB.add(onto2name[i]);


                                onto2text[i] = new JTextArea();
                                onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                                onto2text[i].setEditable(false);
                                onto2text[i].setBackground(Color.lightGray);
                                onto2text[i].setText("没有输入");
                                paneB.add(onto2text[i]);
                            }
                        } else {
                            //动态添加属性标签
                            int x = getposx(i);
                            int y = getposy(i);
                            String name = property.getName().toString();
                            onto2name[i].setBounds(new Rectangle(x, y - 10, 120, 40));
                            onto2name[i].setText(propertyB);
                            onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                            paneB.add(onto2name[i]);


                            onto2text[i] = new JTextArea();
                            onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                            onto2text[i].setEditable(false);
                            onto2text[i].setBackground(Color.lightGray);

                            paneB.add(onto2text[i]);

                            VauleA = onto1text[i].getText();

                            String[] vaule = VauleA.split(";");
                            for (int m = 0; m < vaule.length; m++) {
                                //找相似的概念
                                String content = "";

                                if (content.equals(""))   //填写内容
                                {
                                    content = vaule[m];
                                } else {
                                    content = content + ";" + vaule[m];
                                }


                                onto2text[i].setText(content);
                            }


                        }

                    }
                } else {


                    onto2name[i] = new JLabel();
                    onto2text[i] = new JTextArea();

                    int x = getposx(i);
                    int y = getposy(i);

                    onto2name[i].setBounds(new Rectangle(x, y, 120, 40));
                    onto2name[i].setText(propertyB);
                    onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                    paneB.add(onto2name[i]);


                    onto2text[i] = new JTextArea();
                    onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                    onto2text[i].setEditable(false);
                    onto2text[i].setBackground(Color.lightGray);
                    onto2text[i].setText("未找到相似的内容");
                    paneB.add(onto2text[i]);

                }
            } else {
                onto2name[i] = new JLabel();
                onto2text[i] = new JTextArea();

                int x = getposx(i);
                int y = getposy(i);

                onto2name[i].setBounds(new Rectangle(x, y, 120, 40));
                onto2name[i].setText("未找到相似关系");
                onto2name[i].setFont(onto2name[i].getFont().deriveFont(15f));
                paneB.add(onto2name[i]);


                onto2text[i] = new JTextArea();
                onto2text[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                onto2text[i].setEditable(false);
                onto2text[i].setBackground(Color.lightGray);
                onto2text[i].setText("未找到相似的内容");
                paneB.add(onto2text[i]);

                JOptionPane.showMessageDialog(null, "未找到与" + propertyA + "相似的关系", "", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    pane.repaint();
}

public void getpre(String filename) throws Exception {
    try {
        File f = new File(filename);

        if (f.length() != 0L) {
            InputStream is = new FileInputStream(filename);
            String line; // 用来保存每行读取的内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行
            String[] str = line.split(";");
            int index = str[0].lastIndexOf("#");
            ontoApre = str[0].substring(0, index + 1);
            index = str[1].lastIndexOf("#");
            ontoBpre = str[1].substring(0, index + 1);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public String findsimvaule(String ontoA) {
    String ontoB = null;
    try {
        File f = new File(file1name + file2name + "result.txt");

        if (f.length() != 0L) {
            getpre(file1name + file2name + "result.txt");

            String line = null; // 用来保存每行读取的内容
            InputStream is = new FileInputStream(file1name + file2name + "result.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                line = reader.readLine(); // 读取第一行
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            while (line != null) {
                String[] str = line.split(";");

                int index = str[0].lastIndexOf("#");
                index = str[0].lastIndexOf("#");
                String NameA = str[0].substring(index + 1, str[0].length());

                index = str[1].lastIndexOf("#");
                index = str[1].lastIndexOf("#");
                String NameB = str[1].substring(index + 1, str[1].length());


                if (ontoA.equals(NameA)) {
                    return str[2];
                }

                if (ontoA.equals(NameB)) {
                    return str[2];
                }

                line = reader.readLine(); // 读取下一行

            }

            reader.close();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return ontoB;
}


public String findsimonto(String ontoA) {
    String ontoB = null;
    try {
        File f = new File(file1name + file2name + "result.txt");

        if (f.length() != 0L) {
            getpre(file1name + file2name + "result.txt");

            String line = null; // 用来保存每行读取的内容
            InputStream is = new FileInputStream(file1name + file2name + "result.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            line = reader.readLine(); // 读取第一行

            while (line != null) {
                String[] str = line.split(";");

                int index = str[0].lastIndexOf("#");
                index = str[0].lastIndexOf("#");
                String NameA = str[0].substring(index + 1, str[0].length());

                index = str[1].lastIndexOf("#");
                index = str[1].lastIndexOf("#");
                String NameB = str[1].substring(index + 1, str[1].length());


                if (ontoA.equals(NameA)) {
                    return NameB;
                }

                if (ontoA.equals(NameB)) {
                    return NameA;
                }
                line = reader.readLine(); // 读取下一行
            }

            reader.close();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}

public void displayonttoA() {
    try {

        paneA.removeAll();
        paneA.repaint();


        Collection ObjectA = ClassNameA.getAssociatedProperties();
        int i = 1;
        Iterator it = ObjectA.iterator();
        while (it.hasNext()) {
            OWLProperty property = (OWLProperty) it.next();
            onto1name[i] = new JLabel();
            onto1combox[i] = new JComboBox();
            boolean isobject = property.isObjectProperty();
            if (isobject) {
                if (property.isFunctional()) {
                    //动态添加属性标签
                    int x = getposx(i);
                    int y = getposy(i);
                    String name = property.getName().toString();
                    onto1name[i].setBounds(new Rectangle(x, y, 120, 40));
                    onto1name[i].setText(name + "：");
                    onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                    paneA.add(onto1name[i]);

                    //动态添加属性标签

                    DefaultComboBoxModel commodel = new DefaultComboBoxModel();
                    onto1combox[i] = new JComboBox(commodel);
                    onto1combox[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                    onto1combox[i].setEditable(true);
                    paneA.add(onto1combox[i]);
                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname != null) {
                            OWLNamedClass cls = owlModelA.getOWLNamedClass(clsname);
                            Collection instances = cls.getInstances(true);
                            Iterator kt = instances.iterator();
                            while (kt.hasNext()) {
                                OWLIndividual individual = (OWLIndividual) kt.next();
                                if (individual != null) {
                                    commodel.addElement(individual.getName());
                                }
                            }
                        }
                    }
                    if (commodel.getSize() == 0) {
                        onto1combox[i].setEditable(true);
                        commodel.addElement("没有实例");
                    }
                    i = i + 1;
                } else   //非函数属性
                {
                    //动态添加属性标签
                    int x = getposx(i);
                    int y = getposy(i);
                    final int k = i;
                    String name = property.getName().toString();
                    onto1name[i].setBounds(new Rectangle(x, y, 120, 40));
                    onto1name[i].setText(name + "：");
                    onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                    paneA.add(onto1name[i]);


                    //动态添加属性标签

                    DefaultComboBoxModel commodel = new DefaultComboBoxModel();
                    onto1combox[i] = new JComboBox(commodel);
                    onto1combox[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                    onto1combox[i].setEditable(true);
                    JLabel lable = new JLabel("多值:");  //添加操作说明
                    //lable.setFont(lable.getFont().deriveFont(15f));
                    lable.setBounds(new Rectangle(x, y + 25, 120, 40));
                    paneA.add(lable);

                    onto1text[i] = new JTextArea();
                    onto1text[i].setBounds(new Rectangle(x + 125, y + 32, 100, 25));
                    onto1text[i].setBackground(Color.lightGray);


                    onto1combox[i].addItemListener(new ItemListener() {
                                                       public void itemStateChanged(ItemEvent e) {

                                                           if (e.getStateChange() == ItemEvent.SELECTED) {
                                                               String content = onto1text[k].getText();
                                                               JComboBox com = (JComboBox) e.getSource();
                                                               String selectname = com.getSelectedItem().toString();
                                                               if (content.equals("")) {
                                                                   content = selectname;
                                                               } else {
                                                                   content = content + ";" + selectname;
                                                               }
                                                               onto1text[k].setText(content);
                                                           }
                                                       }
                                                   }
                    );

                    paneA.add(onto1text[i]);
                    paneA.add(onto1combox[i]);
                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname != null) {
                            OWLNamedClass cls = owlModelA.getOWLNamedClass(clsname);
                            Collection instances = cls.getInstances(true);
                            Iterator kt = instances.iterator();
                            while (kt.hasNext()) {
                                OWLIndividual individual = (OWLIndividual) kt.next();
                                if (individual != null) {
                                    commodel.addElement(individual.getName());
                                }
                            }
                        }
                    }
                    if (commodel.getSize() == 0) {
                        onto1combox[i].setEditable(true);
                        commodel.addElement("没有实例");
                    }
                    i = i + 1;
                }
            } else {
                if (!property.isFunctional()) {
                    //动态添加属性标签
                    int x = getposx(i);
                    int y = getposy(i);
                    final int k = i;
                    String name = property.getName().toString();
                    onto1name[i].setBounds(new Rectangle(x, y - 10, 120, 40));

                    JLabel explain = new JLabel();
                    explain.setBounds(new Rectangle(x, y + 10, 120, 40));
                    paneA.add(onto1name[i]);
                    paneA.add(explain);

                    //动态添加属性标签

                    DefaultComboBoxModel commodel = new DefaultComboBoxModel();
                    onto1combox[i] = new JComboBox(commodel);
                    onto1combox[i].setBounds(new Rectangle(x + 125, y, 100, 30));


                    JLabel lable = new JLabel("多值:");  //添加操作说明
                    //lable.setFont(lable.getFont().deriveFont(15f));
                    lable.setBounds(new Rectangle(x + 50, y + 25, 120, 40));
                    paneA.add(lable);

                    onto1text[i] = new JTextArea();
                    onto1text[i].setBounds(new Rectangle(x + 125, y + 32, 100, 25));
                    onto1text[i].setBackground(Color.lightGray);

                    onto1combox[i].addItemListener(new ItemListener() {
                                                       public void itemStateChanged(ItemEvent e) {

                                                           if (e.getStateChange() == ItemEvent.SELECTED) {
                                                               String content = onto1text[k].getText();
                                                               JComboBox com = (JComboBox) e.getSource();
                                                               String selectname = com.getSelectedItem().toString();
                                                               if (content.equals("")) {
                                                                   content = selectname;
                                                               } else {
                                                                   content = content + ";" + selectname;
                                                               }
                                                               onto1text[k].setText(content);
                                                           }
                                                       }
                                                   }
                    );

                    paneA.add(onto1text[i]);
                    paneA.add(onto1combox[i]);
                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname.equals("xsd:int")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(整数)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:float")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(实数)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:boolean")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(逻辑)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:string")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(字符串)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:date")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(日期)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:time")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(时间)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:dateTime")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(日期时间)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else {
                            onto1name[i].setText(name + "：");
                            explain.setText(clsname);
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        }
                    }
                    if (commodel.getSize() == 0) {
                        onto1combox[i].setEditable(true);
                        //	commodel.addElement("没有实例");
                    }
                    i = i + 1;
                } else {   //非函数属性
                    //动态添加属性标签
                    int x = getposx(i);
                    int y = getposy(i);
                    String name = property.getName().toString();
                    onto1name[i].setBounds(new Rectangle(x, y - 10, 120, 40));

                    JLabel explain = new JLabel();
                    explain.setBounds(new Rectangle(x, y + 10, 120, 40));
                    paneA.add(onto1name[i]);
                    paneA.add(explain);


                    //动态添加属性标签

                    DefaultComboBoxModel commodel = new DefaultComboBoxModel();
                    onto1combox[i] = new JComboBox(commodel);
                    onto1combox[i].setBounds(new Rectangle(x + 125, y, 100, 30));
                    paneA.add(onto1combox[i]);
                    Collection dom = property.getRanges(true);
                    Iterator jt = dom.iterator();
                    while (jt.hasNext()) {
                        RDFResource rdfcls = (RDFResource) jt.next();
                        String clsname = rdfcls.getName();
                        if (clsname.equals("xsd:int")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(整数)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:float")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(实数)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:boolean")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(逻辑)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);
                        } else if (clsname.equals("xsd:string")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(字符串)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);
                        } else if (clsname.equals("xsd:date")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(日期)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);

                        } else if (clsname.equals("xsd:time")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(时间)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);
                        } else if (clsname.equals("xsd:dateTime")) {
                            onto1name[i].setText(name + "：");
                            explain.setText("(日期时间)");
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);
                        } else {
                            onto1name[i].setText(name + "：");
                            explain.setText(clsname);
                            onto1name[i].setFont(onto1name[i].getFont().deriveFont(15f));
                            onto1combox[i].setEditable(true);
                        }
                    }
                    if (commodel.getSize() == 0) {
                        onto1combox[i].setEditable(true);
                        //	commodel.addElement("没有实例");
                    }
                    i = i + 1;

                }
            }
        }

        pane.repaint();
        propertyNo = i;

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void update() {
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

public void updatesim() {
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("update.xml"));

        Node node = document.selectSingleNode("/root/filenameA");
        String filenameA = node.getText();

        node = document.selectSingleNode("/root/filenameB");
        String filenameB = node.getText();

        node = document.selectSingleNode("/root/nameA");
        String nameA = node.getText();

        node = document.selectSingleNode("/root/nameB");
        String nameB = node.getText();

        String simfilename = filenameA + filenameB + "result.txt";
        String simendfilename = filenameA + filenameB + "endresult.txt";
        String line, s = ""; // 用来保存每行读取的内容
        String[] str;
        InputStream is = new FileInputStream(simfilename);
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        int index1 = 0, index2 = 0;
        String pre1 = null, pre2 = null;

        if (line != null) {
            str = line.split(";");
            index1 = str[0].lastIndexOf("#");
            index2 = str[0].lastIndexOf("#");
            pre1 = str[0].substring(0, index1);
            pre2 = str[0].substring(0, index1);
        }
        while (line != null) {

            str = line.split(";");
            String name1 = str[0].substring(index1 + 1);
            String name2 = str[1].substring(index1 + 1);
            String sim = str[2];

            if (name1.equals(nameA)) {
                line = pre1 + name1 + ";" + pre1 + name1 + ";" + sim;
                s = s + line + "\n";
            } else {
                s = s + line + "\n";
            }

            line = reader.readLine(); // 读取第一行
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(simfilename));
        bw.write(line);
        bw.close();
        bw = new BufferedWriter(new FileWriter(simendfilename));
        bw.write(line);
        bw.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public int getposx(int index) {

    int x = 10 + (int) ((index - 1) / 8) * 250;
    return x;
}

public int getposy(int index) {
    int y = (index % 9) * 65 - 60;
    return y;
}
}

class xmlfile {
public int createXMLFile(String filename) {
    int returnValue = 0;
    Document document = DocumentHelper.createDocument();
    Element booksElement = document.addElement("books");

    booksElement.addComment("本体概念转换数据");

    Element bookElement = booksElement.addElement("book");
    /** 加入show属性内容 */
    bookElement.addAttribute("show", "yes");
    /** 加入title节点 */
    Element titleElement = bookElement.addElement("title");
    /** 为title设置内容 */
    titleElement.setText("Dom4j Tutorials");
    /** 类似的完成后两个book */
    bookElement = booksElement.addElement("book");
    bookElement.addAttribute("show", "yes");
    titleElement = bookElement.addElement("title");
    titleElement.setText("Lucene Studing");
    bookElement = booksElement.addElement("book");
    bookElement.addAttribute("show", "no");
    titleElement = bookElement.addElement("title");
    titleElement.setText("Lucene in Action");
    /** 加入owner节点 */
    Element ownerElement = booksElement.addElement("owner");
    ownerElement.setText("O'Reilly");
    try {
        /** 将document中的内容写入文件中 */
        XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)));
        writer.write(document);
        writer.close();
        /** 执行成功,需返回1 */
        returnValue = 1;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return returnValue;
}

public int ModiXMLFile(String filename, String newfilename) {
    int returnValue = 0;
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(filename));
        /** 修改内容之一: 如果book节点中show属性的内容为yes,则修改成no */
        /** 先用xpath查找对象 */
        List list = document.selectNodes("/books/book/@show");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Attribute attribute = (Attribute) iter.next();
            if (attribute.getValue().equals("yes")) {
                attribute.setValue("no");
            }
        }
        /**
         * 修改内容之二: 把owner项内容改为Tshinghua
         * 并在owner节点中加入date节点,date节点的内容为2004-09-11,还为date节点添加一个属性type
         */
        list = document.selectNodes("/books/owner");
        iter = list.iterator();
        if (iter.hasNext()) {
            Element ownerElement = (Element) iter.next();
            ownerElement.setText("Tshinghua");
            Element dateElement = ownerElement.addElement("date");
            dateElement.setText("2004-09-11");
            dateElement.addAttribute("type", "Gregorian calendar");
        }
        /** 修改内容之三: 若title内容为Dom4j Tutorials,则删除该节点 */
        list = document.selectNodes("/books/book");
        iter = list.iterator();
        while (iter.hasNext()) {
            Element bookElement = (Element) iter.next();
            Iterator iterator = bookElement.elementIterator("title");
            while (iterator.hasNext()) {
                Element titleElement = (Element) iterator.next();
                if (titleElement.getText().equals("Dom4j Tutorials")) {
                    bookElement.remove(titleElement);
                }
            }
        }
        try {
            /** 将document中的内容写入文件中 */
            XMLWriter writer = new XMLWriter(new FileWriter(new File(newfilename)));
            writer.write(document);
            writer.close();
            /** 执行成功,需返回1 */
            returnValue = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return returnValue;
}

public int formatXMLFile(String filename) {
    int returnValue = 0;
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(filename));
        XMLWriter writer = null;
        /** 格式化输出,类型IE浏览一样 */
        OutputFormat format = OutputFormat.createPrettyPrint();
        /** 指定XML编码 */
        format.setEncoding("UTF-8");
        writer = new XMLWriter(new FileWriter(new File(filename)), format);
        writer.write(document);
        writer.close();
        /** 执行成功,需返回1 */
        returnValue = 1;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return returnValue;
}
}





/*
 * 　　book.xml
　　<?xml version="1.0" encoding="UTF-8"?>
　　<books>
　　<!--This is a test for dom4j-->
　　<book show="yes">
　　<title>Dom4j Tutorials</title>
　　</book>
　　<book show="yes">
　　<title>Lucene Studing</title>
　　</book>
　　<book show="no">
　　<title>Lucene in Action</title>
　　</book>
　　<owner>O'Reilly</owner>
　　</books>

 */
