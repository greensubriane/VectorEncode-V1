package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import edu.stanford.smi.protege.action.CreateClsAction;
import edu.stanford.smi.protege.action.DeleteClsAction;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.resource.ResourceKey;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.ui.ParentChildRoot;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;


public class Tabledemo {
private JFrame frame = null;
private JTable table = null;
private Table_Model model = null;
private JScrollPane s_pan = null;
private JPanel pane = null;
private JFileChooser fc;
private String owlfile1, owlfile2, file1name, file2name, projectpath = null;
private ClassInstPanel panel1 = null, panel2 = null;
private OWLModel owlModelA, owlModelB;
private String onto1pre, onto2pre, onto1prex, onto2prex, linepre1, linepre2, linename1, linename2, linesim;
private int computcount = 0, listitemcount = 0; //表示文件是否修改，是否第一次添加转换项目
public static JFrame parent = null;
private String listnameA = null, listnameB = null;  //数据转换项目

private DefaultListModel listMode = new DefaultListModel();
private JList list = new JList(listMode);
private static final JLabel label = new JLabel("转换项目列表");

private MenuBar mb = new MenuBar();
private Menu m1 = new Menu("文件"), m2 = new Menu("相似度"), m3 = new Menu("数据转换项目"), m4 = new Menu("数据转换");
private MenuItem openA = new MenuItem("打开领域本体文件"), openB = new MenuItem("打开企业本体文件"), exit = new MenuItem("退出");
private MenuItem simcls = new MenuItem("类相似度计算"), sim = new MenuItem("属性和实例相似度计算"), viewsim = new MenuItem("查看相似度"), viewclssim = new MenuItem("查看类相似度"), viewpropertysim = new MenuItem("查看类属性相似度"), viewinstancesim = new MenuItem("查看类实例相似度");
;
private MenuItem addsim = new MenuItem("添加相似度"), deletesim = new MenuItem("删除相似度"), savesim = new MenuItem("保存相似度");
private MenuItem viewconvert = new MenuItem("查看转换项目"), addconvert = new MenuItem("添加转换项目"), delconvert = new MenuItem("删除转换项目"), help = new MenuItem("操作说明");
private MenuItem showconvert = new MenuItem("数据转换");

public Tabledemo() {
    try {
        Init();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void Init() {
    frame = new JFrame("JTableTest");
    pane = new JPanel();

    File convertfile = new File("abc.txt");
    String path = convertfile.getAbsolutePath();
    projectpath = path.substring(0, path.length() - 7);
    projectpath = projectpath.replaceAll("\\\\", "/");

    mb.add(m1);
    m1.add(openA);
    m1.add(openB);
    m1.add(exit);


    openA.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(pane);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                owlfile1 = file.getAbsolutePath();
//                    file1TextArea.setText(owlfile1);
                //在这里添加一些对文件的处理
                try {
                    if (panel1 != null) {
                        pane.remove(panel1);
                    }


                    InputStream is = new FileInputStream(owlfile1);
                    owlModelA = ProtegeOWL.createJenaOWLModelFromInputStream(is);


                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    OWLNamespaceManager nmger = (OWLNamespaceManager) owlModelA.getNamespaceManager();
                    onto1prex = nmger.getDefaultNamespace();

                    //Collection destinationClass =owlModel1.getUserDefinedOWLNamedClasses();
                    //
                    panel1 = new ClassInstPanel(owlModelA, Collections.singleton(destinationClass));

                    //      	  	panel1 = new ClassInstPanel(owlModel1, destinationClass);
                    panel1.setBounds(new Rectangle(10, 0, 400, 590));
                    pane.add(panel1);
                    frame.setVisible(true);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    });

    openB.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(pane);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                owlfile2 = file.getAbsolutePath();
//                    file2TextArea.setText(owlfile2);
                //在这里添加一些对文件的处理
                try {
                    if (panel2 != null) {
                        pane.remove(panel2);
                    }

                    InputStream is = new FileInputStream(owlfile2);
                    owlModelB = ProtegeOWL.createJenaOWLModelFromInputStream(is);

                    OWLNamedClass destinationClass = owlModelB.getOWLThingClass();
                    //OWLNamedClass destinationClass =owlModel1.getUserDefinedOWLNamedClasses();


                    OWLNamespaceManager nmger = (OWLNamespaceManager) owlModelB.getNamespaceManager();
                    onto2prex = nmger.getDefaultNamespace();
                    //getOWLThingClass();
                    //  panel1 = new ClassInstPanel(owlModel1, Collections.singleton(destinationClass));

                    panel2 = new ClassInstPanel(owlModelB, Collections.singleton(destinationClass));

                    //               	  	panel1.setLayout(new BorderLayout());
                    panel2.setBounds(new Rectangle(870, 0, 400, 590));
//	                    onto1_pan.setLayout(new BorderLayout());
                    pane.add(panel2);
                    frame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    });

    exit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            parent.setVisible(true);
            frame.setVisible(false);
        }
    });

    mb.add(m2);
    m2.add(viewsim);
    m2.add(simcls);
    m2.add(sim);
    m2.add(viewclssim);
    m2.add(viewpropertysim);
    m2.add(viewinstancesim);
    m2.add(addsim);
    m2.add(deletesim);
    m2.add(savesim);

    simcls.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);
                String file1 = null, file2 = null;
                file1 = owlfile1.replaceAll("\\\\", "/");
                file2 = owlfile2.replaceAll("\\\\", "/");

                if (file1 != null && file2 != null) {
                    getfilename();

                    ClearData();//清除数据

                    //保存前次的sim=1.0的内容
                    String endfilename = file1name + file2name + "endresult.txt";
                    String resultfilename = file1name + file2name + "result.txt";

                    if (computcount != 0)  //第一次计算不保存结果
                    {
                        readfile("result.txt");
                        saveData(endfilename, true);
                    }

                    computcount = computcount + 1; //计算次数加一

                    File f2 = new File(endfilename);//想命名的原文件

                    simbase sim = new simbase(owlModelA, owlModelB, projectpath, true);
                    sortfile("result.txt");   //删除向同行
                    sortposition("result.txt");
                    File f3 = new File("result.txt");

                    readfile("result.txt");

                } else {
                    JOptionPane.showMessageDialog(null, "请检查是否正确打开本体文件", "", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请检查是否正确打开本体文件", "", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        }
    });


    sim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);
                String file1 = null, file2 = null;
                file1 = owlfile1.replaceAll("\\\\", "/");
                file2 = owlfile2.replaceAll("\\\\", "/");

                if (file1 != null && file2 != null) {
                    getfilename();

                    ClearData();//清除数据

                    //保存前次的sim=1.0的内容
                    String endfilename = file1name + file2name + "endresult.txt";
                    String resultfilename = file1name + file2name + "result.txt";

                    if (computcount != 0)  //第一次计算不保存结果
                    {
                        readfile("result.txt");
                        saveData(endfilename, true);
                    }

                    computcount = computcount + 1; //计算次数加一

                    File f2 = new File(endfilename);//想命名的原文件
                    simbase sim = new simbase(owlModelA, owlModelB, projectpath);

                    sortfile("result.txt");   //删除向同行
                    sortposition("result.txt");
                    File f3 = new File("result.txt");

                    if (computcount != 0)  //第一次计算不保存结果
                    {
                        readfile("result.txt");
                        saveData(resultfilename);
                        saveData(endfilename, true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请检查是否正确打开本体文件", "", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请检查是否正确打开本体文件", "", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        }
    });

    viewsim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);

                ClearData();
                getfilename();
                if (file1name != null && file1name != null) {
                    String fileresultname = file1name + file2name + "result.txt";
                    readfile(fileresultname);
                } else {
                    readfile("result.txt");
                    JOptionPane.showMessageDialog(null, "您没有选择本体文件", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    });


    viewclssim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);

                ClearData();
                getfilename();
                if (file1name != null && file1name != null) {
                    String fileresultname = file1name + file2name + "result.txt";
                    readclsfile(fileresultname);
                } else {
                    readclsfile("result.txt");
                    JOptionPane.showMessageDialog(null, "您没有选择本体文件", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    });

    viewpropertysim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);

                ClearData();
                getfilename();
                if (file1name != null && file1name != null) {
                    String fileresultname = file1name + file2name + "result.txt";
                    readpropertyfile(fileresultname);
                } else {
                    readpropertyfile("result.txt");
                    JOptionPane.showMessageDialog(null, "您没有选择本体文件", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    });

    viewinstancesim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);

                ClearData();
                getfilename();
                if (file1name != null && file1name != null) {
                    String fileresultname = file1name + file2name + "result.txt";
                    readinstfile(fileresultname);
                } else {
                    readinstfile("result.txt");
                    JOptionPane.showMessageDialog(null, "您没有选择本体文件", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    });

    addsim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String onto1 = null, onto2 = null, inst1 = null, inst2 = null;
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);
                onto1 = panel1.getselectclass();
                onto2 = panel2.getselectclass();
                try {
                    inst1 = panel1.getselectInst();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    inst2 = panel2.getselectInst();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (onto1 != null && onto2 != null) {
                    if (inst1 != null && inst2 != null) {
                        model.addRow(inst1, "1.0", inst2);
                        table.updateUI();
                    } else if (inst1 != null) {
                        model.addRow(inst1, "1.0", onto2);
                        table.updateUI();
                    } else if (inst2 != null) {
                        model.addRow(onto1, "1.0", inst2);
                        table.updateUI();
                    } else {
                        model.addRow(onto1, "1.0", onto2);
                        table.updateUI();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请正确填写类或实例", "", JOptionPane.ERROR_MESSAGE);

                    model.addRow("onto1", "1.0", "onto2");
                    table.updateUI();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    });

    deletesim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                removeData(row);
            } else {
                JOptionPane.showMessageDialog(null, "请选择一个相似度数据行", "", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    savesim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            getfilename();
            try {
                s_pan.setVisible(true);
                label.setVisible(false);
                list.setVisible(false);
                frame.setVisible(true);
                if (file1name != null && file1name != null) {
                    String filename = file1name + file2name + "results.txt";
                    String fileprename = file1name + file2name + "prefile.txt";
                    String endfilename = file1name + file2name + "endresult.txt";
                    saveData(filename);
                    sortfile(filename);
                    saveData(fileprename);
                    sortfile(fileprename);
                    saveData(endfilename, true);
                    sortfile(endfilename);
                    saveData("result.txt");
                    sortfile("result.txt");

                    String fileresultname = file1name + file2name + "result.txt";
                    File f = new File(fileresultname);
                    f.delete();

                    File f1 = new File(filename);//想命名的原文件
                    f1.renameTo(new File(fileresultname));
                } else {
                    JOptionPane.showMessageDialog(null, "没有选择相应本体文件,无法保存", "", JOptionPane.ERROR_MESSAGE);

                }
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
    });

    mb.add(m3);
    m3.add(viewconvert);
    m3.add(addconvert);
    m3.add(delconvert);
    m3.add(help);


    label.setText("转换项目列表");
    label.setBounds(new Rectangle(420, 0, 150, 30));
    label.setFont(label.getFont().deriveFont(15f));
    label.setVisible(false);
    pane.add(label);

    list.setBounds(new Rectangle(420, 30, 300, 560));
    list.setBackground(Color.LIGHT_GRAY);
    list.setVisible(false);
    pane.add(list);

    list.addListSelectionListener(new ListSelectionListener() {
                                      public void valueChanged(ListSelectionEvent e) {
                                          JList list = (JList) e.getSource();
                                          String Name = (String) list.getSelectedValue().toString();
                                          String[] item = Name.split("-");
                                          listnameA = item[0];
                                          listnameB = item[1];
                                      }
                                  }
    );

    viewconvert.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                s_pan.setVisible(false);
                label.setVisible(true);
                list.setVisible(true);
                frame.setVisible(true);
                readconvertitem();

            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }
    });

    addconvert.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String onto1 = null, onto2 = null, item = null;
            try {
                readconvertitem();
                onto1 = panel1.getselectclass();
                onto2 = panel2.getselectclass();
                item = onto1 + "#" + onto2 + "#" + owlfile1 + "#" + owlfile2;

                boolean hasitem = addconvertitem(item);  //保存记录
                if (!hasitem) {
                    listMode.addElement(onto1 + "-" + onto2);
                }


                s_pan.setVisible(false);
                label.setVisible(true);
                list.setVisible(true);
                frame.setVisible(false);
                frame.setVisible(true);


            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }
    });

    delconvert.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String onto1 = null, onto2 = null, item = null;
            if (listnameA != null && listnameB != null) {

                onto1 = listnameA;
                onto2 = listnameB;
                item = onto1 + "#" + onto2 + "#" + owlfile1 + "#" + owlfile2;
                deleteconvertitem(item);

                readconvertitem();
                frame.setVisible(false);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "请选择一个转换项目", "", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    help.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            JOptionPane.showMessageDialog(null, "请先选择两边的本体概念+\n+然后点击\"添加项目菜单\"。", "", JOptionPane.ERROR_MESSAGE);

        }
    });

    mb.add(m4);
    m4.add(showconvert);

    showconvert.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Convert convert = new Convert();
            convert.parent = frame;
            frame.setVisible(false);

        }
    });

    //创建一个文件过滤，使用当前目录
    fc = new JFileChooser(".");
    //过滤条件在MyFilter类中定义
    fc.addChoosableFileFilter(new myFilter());


    model = new Table_Model(20);
    table = new JTable(model);


    table.setBackground(Color.white);

    TableColumnModel tcm = table.getColumnModel();
    //       tcm.getColumn(3).setCellEditor(new DefaultCellEditor(com));
    tcm.getColumn(0).setPreferredWidth(20);
    tcm.getColumn(1).setPreferredWidth(180);
    tcm.getColumn(2).setPreferredWidth(60);
    tcm.getColumn(3).setPreferredWidth(180);


    s_pan = new JScrollPane(table);
    table.getTableHeader().addMouseListener(new MouseAdapter() {
                                                public void mouseClicked(MouseEvent me) {
                                                    int columnIndex = table.getTableHeader().columnAtPoint(me.getPoint());
                                                    if (columnIndex != -1) {
                                                        model.sortColumn(columnIndex);
                                                    }
                                                }
                                            }
    );
    s_pan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    s_pan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    s_pan.setBounds(new Rectangle(420, 0, 440, 590));
    s_pan.setVisible(false);


    pane.setLayout(null);

    pane.add(s_pan);


    frame.getContentPane().add(pane);
    frame.setMenuBar(mb);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1280, 660);
    frame.setTitle("异构系统本体集成平台——相似度计算");
    frame.setLocation(0, 150);
    frame.setResizable(false);
    frame.setVisible(true);

}


public void addsim(String simfile) {
    try {
        ArrayList clslist = new ArrayList(), instlist = new ArrayList(), propertylist = new ArrayList();
        ArrayList newclslist = new ArrayList(), newinstlist = new ArrayList(), newobjectpropertylist = new ArrayList(), newdatapropertylist = new ArrayList(), newinstsim = new ArrayList();

        String line, pre1 = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream(simfile);
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        line = reader.readLine(); // 读取第一行

        while (line != null) {
            getlinesim(line);
            if (linesim.equals("1.0"))   //建立sim=1.0的类、属性、实例列表
            {
                if (gettype(linename1).equals("cls")) {
                    clslist.add(linename1);
                }
                if (gettype(linename1).equals("inst")) {
                    instlist.add(linename1);
                }
                if (gettype(linename1).equals("property")) {
                    propertylist.add(linename1);
                }
            }

            //linename1,linename2,linesim;
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }

        reader.close();

        newobjectpropertylist = simobjectproperty(clslist, clslist);
        newdatapropertylist = simdataproperty(clslist);
        newclslist = simcls(clslist, propertylist);
//			newinstlist=siminst(instlist,propertylist); //由属性添加实例相似度
//			newinstsim=siminstall(instlist);    //由实例添加类相似度


        line = "";
        BufferedWriter bw = new BufferedWriter(new FileWriter("addsim.txt"));

        Iterator i1 = newobjectpropertylist.iterator();
        while (i1.hasNext()) {
            line = onto1prex + i1.next().toString();
            if (i1.hasNext()) {
                line = line + ";" + onto2prex + i1.next().toString();

            }
            if (i1.hasNext()) {
                line = line + ";" + i1.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        Iterator i2 = newdatapropertylist.iterator();
        while (i2.hasNext()) {
            line = line + onto1prex + i2.next().toString();
            if (i2.hasNext()) {
                line = line + ";" + onto2prex + i2.next().toString();

            }
            if (i2.hasNext()) {
                line = line + ";" + i2.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        Iterator i3 = newclslist.iterator();
        while (i3.hasNext()) {
            line = line + onto1prex + i3.next().toString();
            if (i3.hasNext()) {
                line = line + ";" + onto2prex + i3.next().toString();

            }
            if (i3.hasNext()) {
                line = line + ";" + i3.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        Iterator i4 = newinstlist.iterator();
        while (i4.hasNext()) {
            line = line + onto1prex + i4.next().toString();
            if (i4.hasNext()) {
                line = line + ";" + onto2prex + i4.next().toString();

            }
            if (i4.hasNext()) {
                line = line + ";" + i4.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        Iterator i5 = newinstsim.iterator();
        while (i5.hasNext()) {
            line = line + onto1prex + i5.next().toString();
            if (i5.hasNext()) {
                line = line + ";" + onto2prex + i5.next().toString();

            }
            if (i5.hasNext()) {
                line = line + ";" + i5.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        bw.close();

    } catch (Exception ex) {

        ex.printStackTrace();
    }
}


public ArrayList siminstall(ArrayList instlist) {
    ArrayList newsimlist = new ArrayList();

    try {
        Iterator ctA = instlist.iterator();
        while (ctA.hasNext()) {
            String instnameA = ctA.next().toString();
            OWLIndividual instA = owlModelA.getOWLIndividual(instnameA);

            String clsnameA = instA.getDirectType().getName();       //得到类名

            String instnameB = findsimonto(instnameA);

            OWLIndividual instB = owlModelB.getOWLIndividual(instnameB);
            String clsnameB = instB.getDirectType().getName();

            newsimlist.add(clsnameA);
            newsimlist.add(clsnameB);
            newsimlist.add("0.99");

        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

public ArrayList simdataproperty(ArrayList clslist) {
    ArrayList newsimlist = new ArrayList();

    try {
        Iterator ctA = clslist.iterator();
        while (ctA.hasNext()) {
            String name1 = ctA.next().toString();
            OWLNamedClass cls1A = owlModelA.getOWLNamedClass(name1);
            String clsname1A = cls1A.getName();

            Collection col = cls1A.getAssociatedProperties();
            Iterator cl = col.iterator();
            while (cl.hasNext()) {
                OWLProperty property = (OWLProperty) cl.next();
                String pnameA = property.getName();

                if (property.isFunctional() && !property.isObjectProperty()) {
                    RDFResource res = property.getRange();
                    String rangenameA = res.getName();

                    String clsname1B = findsimonto(clsname1A);

                    OWLNamedClass cls1B = owlModelB.getOWLNamedClass(clsname1B);


                    Collection col2 = cls1B.getAssociatedProperties();
                    Iterator c2 = col2.iterator();
                    while (c2.hasNext()) {
                        OWLProperty propertyB = (OWLProperty) c2.next();
                        String pnameB = propertyB.getName();
                        if (propertyB.isFunctional() && !propertyB.isObjectProperty()) {
                            RDFResource resB = propertyB.getRange();
                            String rangenameB = resB.getName();
                            if (rangenameA.equals(rangenameB)) {
                                newsimlist.add(pnameA);
                                newsimlist.add(pnameB);
                                newsimlist.add("1.0");
                            }
                        }

                    }

                }

            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

public ArrayList simobjectproperty(ArrayList clslist1, ArrayList clslist2) {
    ArrayList newsimlist = new ArrayList();

    try {
        Iterator ctA = clslist1.iterator();
        while (ctA.hasNext()) {
            String name1 = ctA.next().toString();
            OWLNamedClass cls1A = owlModelA.getOWLNamedClass(name1);
            String clsname1A = cls1A.getName();

            Iterator ctB = clslist2.iterator();
            while (ctB.hasNext()) {
                String name2 = ctB.next().toString();
                OWLNamedClass cls2A = owlModelA.getOWLNamedClass(name2);
                String clsname2A = cls2A.getName();

                Collection col = cls1A.getAssociatedProperties();
                Iterator cl = col.iterator();
                while (cl.hasNext()) {
                    OWLProperty property = (OWLProperty) cl.next();
                    String pnameA = property.getName();
                    if (property.isFunctional() && property.isObjectProperty()) {
                        RDFResource res = property.getRange();
                        String rangename = res.getName();
                        if (clsname2A.equals(rangename)) {
                            String clsname1B = findsimonto(clsname1A);
                            String clsname2B = findsimonto(clsname2A);

                            OWLNamedClass cls1B = owlModelB.getOWLNamedClass(clsname1B);
                            OWLNamedClass cls2B = owlModelB.getOWLNamedClass(clsname2B);

                            Collection col2 = cls1B.getAssociatedProperties();
                            Iterator c2 = col2.iterator();
                            while (c2.hasNext()) {
                                OWLProperty propertyB = (OWLProperty) c2.next();
                                String pnameB = propertyB.getName();
                                if (propertyB.isFunctional()) {
                                    RDFResource resB = propertyB.getRange();
                                    String rangenameB = resB.getName();
                                    if (clsname2B.equals(rangenameB)) {
                                        newsimlist.add(pnameA);
                                        newsimlist.add(pnameB);
                                        newsimlist.add("1.0");
                                    }
                                }

                            }
                        }
                    }

                }

            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

public ArrayList simcls(ArrayList clslist, ArrayList propertylist) {
    ArrayList newsimlist = new ArrayList();

    try {
        Iterator pt = propertylist.iterator();
        while (pt.hasNext()) {
            String pnameA = pt.next().toString();
            OWLProperty propertyA = owlModelA.getOWLProperty(pnameA);
            Iterator ct = clslist.iterator();
            while (ct.hasNext()) {
                String clsnameA = ct.next().toString();
                //   	    	OWLNamedClass cls=owlModelA.getOWLNamedClass(clsnameA);

                if (propertyA.isFunctional() && propertyA.isObjectProperty()) {
                    RDFSClass rdf = propertyA.getDomain(false);
                    RDFResource resA = propertyA.getRange();
                    String rangenameA = resA.getName();

                    String domainnameA = rdf.getName();
                    if (domainnameA.equals(clsnameA)) {
                        String pnameB = findsimonto(pnameA);
                        String clsnameB = findsimonto(clsnameA);

                        OWLProperty propertyB = owlModelB.getOWLProperty(pnameB);
                        if (propertyB.isFunctional() && propertyB.isObjectProperty()) {
                            RDFResource resB = propertyB.getRange();
                            String rangenameB = resB.getName();
                            newsimlist.add(rangenameA);
                            newsimlist.add(rangenameB);
                            newsimlist.add("1.0");
                        }
                    }
                }
            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

public ArrayList siminst(ArrayList instlist, ArrayList propertylist) {
    ArrayList newsimlist = new ArrayList();
    try {
        Iterator pt = propertylist.iterator();
        while (pt.hasNext()) {
            String pnameA = pt.next().toString();
            OWLProperty propertyA = owlModelA.getOWLProperty(pnameA);
            Iterator it = instlist.iterator();
            while (it.hasNext()) {
                String instnameA = it.next().toString();
                OWLIndividual instA = owlModelA.getOWLIndividual(instnameA);

                String clsnameA = instA.getDirectType().getName();       //得到类名
                OWLNamedClass clsA = owlModelA.getOWLNamedClass(clsnameA);
                Collection colA = clsA.getAssociatedProperties();

                Iterator ct = colA.iterator();
                while (ct.hasNext()) {
                    OWLProperty instApropperty = (OWLProperty) ct.next();
                    String instApname = instApropperty.getName();
                    if (instApname.equals(pnameA)) {
                        if (instApropperty.isFunctional() && instApropperty.isObjectProperty()) {
                            OWLIndividual nameAdomain = (OWLIndividual) instA.getPropertyValue((RDFProperty) instApropperty);
                            String instAdomain = nameAdomain.getName();

                            String pnameB = findsimonto(pnameA);
                            String instnameB = findsimonto(instnameA);

                            OWLIndividual instB = owlModelB.getOWLIndividual(instnameB);
                            OWLProperty propertyB = owlModelB.getOWLProperty(pnameB);
                            if (propertyB.isFunctional() && propertyB.isObjectProperty()) {
                                OWLIndividual nameBdomain = (OWLIndividual) instB.getPropertyValue((RDFProperty) propertyB);
                                String instBdomain = nameBdomain.getName();
                                newsimlist.add(instAdomain);
                                newsimlist.add(instBdomain);
                                newsimlist.add("1.0");
                            }

                        }
                    }
                }
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

public String findsimonto(String ontoA) {
    String ontoB = null;
    try {
        getpre();

        String line; // 用来保存每行读取的内容
        InputStream is = new FileInputStream(file1name + file2name + "result.txt");
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行

        while (line != null) {
            String[] str = line.split(";");
            if (onto1prex.equals(onto1pre)) {
                int index = str[0].lastIndexOf("#");
                String NameA = str[0].substring(index + 1, str[0].length());
                if (NameA.equals(ontoA)) {
                    index = str[1].lastIndexOf("#");
                    ontoB = str[1].substring(index + 1, str[1].length());
                    return ontoB;
                }

            } else {
                int index = str[1].lastIndexOf("#");
                String NameA = str[1].substring(index + 1, str[1].length());
                if (NameA.equals(ontoA)) {
                    index = str[0].lastIndexOf("#");
                    ontoB = str[0].substring(index + 1, str[0].length());
                    return ontoB;
                }
            }

            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行

        }

        reader.close();

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return ontoB;
}

public String gettype(String name) {
    try {
        OWLNamedClass cls = owlModelA.getOWLNamedClass(name);
        if (cls != null) {
            return "cls";
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    try {

        OWLIndividual inst = owlModelA.getOWLIndividual(name);
        if (inst != null) {
            return "inst";
        }
    } catch (Exception ex) {

        ex.printStackTrace();
    }

    try {
        OWLProperty property = owlModelA.getOWLProperty(name);
        if (property != null) {
            return "property";
        }
    } catch (Exception ex) {

        ex.printStackTrace();
    }

    return "";
}

public void sortfile(String file) throws IOException {
    ArrayList list = new ArrayList();
    String line; // 用来保存每行读取的内容
    InputStream is = new FileInputStream(file);
    StringBuffer buffer = new StringBuffer();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine(); // 读取第一行
//		boolean isequal=false;
//		String prestr="";
    if (line != null) {
        String[] firstline = line.split(";");
        list.add(firstline[0]);
        list.add(firstline[1]);
        if (firstline[2].length() >= 7) {
            firstline[2] = firstline[2].substring(0, 7);
        }
        list.add(firstline[2]);
    }
    buffer.append(line); // 将读到的内容添加到 buffer 中
    buffer.append("\n"); // 添加换行符
    line = reader.readLine(); // 读取下一行

    while (line != null) {
        if (!hasline(line, list)) {
            String[] newline = line.split(";");
            list.add(newline[0]);
            list.add(newline[1]);
            if (newline[2].length() >= 7) {
                newline[2] = newline[2].substring(0, 7);
            }
            list.add(newline[2]);
        }


        buffer.append(line); // 将读到的内容添加到 buffer 中
        buffer.append("\n"); // 添加换行符
        line = reader.readLine(); // 读取下一行
    }

    reader.close();


    File f = new File(file);   //删除旧文件
    f.delete();

    line = "";

    BufferedWriter bw = new BufferedWriter(new FileWriter(file));

    Iterator it = list.iterator();
    while (it.hasNext()) {
        line = it.next().toString();
        if (it.hasNext()) {
            line = line + ";" + it.next().toString();

        }
        if (it.hasNext()) {
            line = line + ";" + it.next().toString() + "\n";
        }

        if (line != "") {
            bw.write(line);
        }
    }

    bw.close();

}


public void sortposition(String file) throws IOException {
    try {
        ArrayList list = new ArrayList();
        String line, pre1 = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream(file);
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));


        line = reader.readLine(); // 读取第一行


        if (line != null) {
            getlinepre(line);
            if (linepre1.equals(onto1prex)) {
                String[] firstline = line.split(";");
                list.add(firstline[0]);
                list.add(firstline[1]);
                if (firstline[2].length() >= 7) {
                    firstline[2] = firstline[2].substring(0, 7);
                }
                list.add(firstline[2]);
            } else {
                String[] firstline = line.split(";");
                list.add(firstline[1]);
                list.add(firstline[0]);
                if (firstline[2].length() >= 7) {
                    firstline[2] = firstline[2].substring(0, 7);
                }
                list.add(firstline[2]);
            }
        }
        buffer.append(line); // 将读到的内容添加到 buffer 中
        buffer.append("\n"); // 添加换行符
        line = reader.readLine(); // 读取下一行

        while (line != null) {
            getlinepre(line);
            if (linepre1.equals(onto1prex)) {
                String[] newline = line.split(";");
                list.add(newline[0]);
                list.add(newline[1]);
                if (newline[2].length() >= 7) {
                    newline[2] = newline[2].substring(0, 7);
                }
                list.add(newline[2]);
            } else {
                String[] newline = line.split(";");
                list.add(newline[1]);
                list.add(newline[0]);
                if (newline[2].length() >= 7) {
                    newline[2] = newline[2].substring(0, 7);
                }
                list.add(newline[2]);
            }

            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }

        reader.close();


        File f = new File(file);   //删除旧文件
        f.delete();

        line = "";

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        Iterator it = list.iterator();
        while (it.hasNext()) {
            line = it.next().toString();
            if (it.hasNext()) {
                line = line + ";" + it.next().toString();

            }
            if (it.hasNext()) {
                line = line + ";" + it.next().toString() + "\n";
            }

            if (line != "") {
                bw.write(line);
            }
        }

        bw.close();

    } catch (Exception ex) {

        ex.printStackTrace();
    }

}


public boolean hasline(String line, ArrayList list) {
    String[] str = line.split(";");
    int j = 0;
    boolean hasline = false;
    while (j < list.size()) {
        String list0, list1, list2;
        list0 = list.get(j).toString();
        j++;
        list1 = list.get(j).toString();
        j++;
        list2 = list.get(j).toString();
        j++;
        if (str[0].equals(list0) && str[1].equals(list1)) {
            hasline = true;
        }
        if (str[0].equals(list1) && str[1].equals(list0)) {
            hasline = true;
        }
    }
    return hasline;
}


public void IncorporateSim(String file, String endfile) throws IOException {
    //读取原始结果
    ArrayList Simlist = new ArrayList();
    String line; // 用来保存每行读取的内容
    InputStream is = new FileInputStream(endfile);
    StringBuffer buffer = new StringBuffer();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine(); // 读取第一行

    while (line != null) {
        String[] newline = line.split(";");
        Simlist.add(newline[0]);
        Simlist.add(newline[1]);
        if (newline[2].length() >= 7) {
            newline[2] = newline[2].substring(0, 7);
        }
        Simlist.add(newline[2]);


        buffer.append(line); // 将读到的内容添加到 buffer 中
        buffer.append("\n"); // 添加换行符
        line = reader.readLine(); // 读取下一行
    }

    reader.close();

    //读取EndSim文件，合并结果

    //判断Sim文件是否为空
    File f = new File(file);
    if (f.length() != 0L) {
        InputStream in = new FileInputStream(file);
        BufferedReader readerend = new BufferedReader(new InputStreamReader(in));
        line = readerend.readLine(); // 读取第一行

        while (line != null) {
            String[] newline = line.split(";");

            if (Simlist != null) {
                if (!hasline(line, Simlist)) {
                    Simlist.add(newline[0]);
                    Simlist.add(newline[1]);
                    Simlist.add(newline[2]);
                }
            }

            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = readerend.readLine(); // 读取下一行
        }

        readerend.close();
    } else {
        new AccessTextFile(endfile, "prefile.txt");
        new AccessTextFile(endfile, "result.txt");
    }


    //保存结果
    line = "";

    BufferedWriter bw = new BufferedWriter(new FileWriter(file));

    Iterator it = Simlist.iterator();
    while (it.hasNext()) {
        line = it.next().toString();
        if (it.hasNext()) {
            line = line + ";" + it.next().toString();

        }
        if (it.hasNext()) {
            line = line + ";" + it.next().toString() + "\n";
        }

        if (line != "") {
            bw.write(line);
        }
    }

    bw.close();

}

public void getfilename() {
    if (owlfile1 != null) {
        int index = owlfile1.lastIndexOf("\\");
        file1name = owlfile1.substring(index + 1, owlfile1.length());
        file1name = file1name.substring(0, file1name.length() - 4);
        index = owlfile2.lastIndexOf("\\");
        file2name = owlfile2.substring(index + 1, owlfile2.length());
        file2name = file2name.substring(0, file2name.length() - 4);
    }
}


public void readinstfile(String filepath) throws Exception {
    try {
        getpre();

        String clsnameA = panel1.getselectclass();

        AccessTextFile test = new AccessTextFile(filepath);
        ArrayList list = test.simlist;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String onto1 = "";
            String onto2 = "";
            String sim = "";

            onto1 = it.next().toString();
            if (it.hasNext()) {
                onto2 = it.next().toString();

            }
            if (it.hasNext()) {
                sim = (it.next().toString());
            }

            try {
                if (getnametype(onto1).equals("inst") && isshow(clsnameA, onto1, getnametype(onto1))) {
                    model.addRow(onto1, sim, onto2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请选择本体文件", "", JOptionPane.ERROR_MESSAGE);
            }

        }
        table.updateUI();
        s_pan.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void readfile(String filename) throws Exception {
    try {
        getpre();
        AccessTextFile test = new AccessTextFile("result.txt");
        ArrayList list = test.simlist;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String onto1 = "";
            String onto2 = "";
            String sim = "";

            onto1 = it.next().toString();
            if (it.hasNext()) {
                onto2 = it.next().toString();

            }
            if (it.hasNext()) {
                sim = (it.next().toString());
            }

            try {

                model.addRow(onto1, sim, onto2);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请选择本体文件", "", JOptionPane.ERROR_MESSAGE);
            }

        }
        table.updateUI();
        s_pan.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void readclsfile(String filepath) throws Exception {
    try {
        getpre();

        String clsnameA = panel1.getselectclass();

        AccessTextFile test = new AccessTextFile(filepath);
        ArrayList list = test.simlist;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String onto1 = "";
            String onto2 = "";
            String sim = "";

            onto1 = it.next().toString();
            if (it.hasNext()) {
                onto2 = it.next().toString();

            }
            if (it.hasNext()) {
                sim = (it.next().toString());
            }

            try {
                if (getnametype(onto1).equals("cls")) {
                    model.addRow(onto1, sim, onto2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请选择本体文件", "", JOptionPane.ERROR_MESSAGE);
            }

        }
        table.updateUI();
        s_pan.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void readpropertyfile(String filepath) throws Exception {
    try {
        getpre();

        String clsnameA = panel1.getselectclass();

        AccessTextFile test = new AccessTextFile(filepath);
        ArrayList list = test.simlist;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String onto1 = "";
            String onto2 = "";
            String sim = "";

            onto1 = it.next().toString();
            if (it.hasNext()) {
                onto2 = it.next().toString();

            }
            if (it.hasNext()) {
                sim = (it.next().toString());
            }

            try {
                if (getnametype(onto1).equals("prop") && isshow(clsnameA, onto1, getnametype(onto1))) {
                    model.addRow(onto1, sim, onto2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "请选择本体文件", "", JOptionPane.ERROR_MESSAGE);
            }

        }
        table.updateUI();
        s_pan.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private String getnametype(String name) {
    if (name.contains(".")) {
        return "prop";
    }
    if (name.contains("_")) {
        return "inst";
    }
    return "cls";
}

private boolean isshow(String clsname, String name, String type) {
    try {
        if (type.equals("cls")) {
            return true;
        }
        if (type.equals("inst")) {
            if (name.contains(clsname)) {
                return true;
            } else {
                return false;
            }
        }
        if (type.equals("prop")) {
            if (name.contains(clsname)) {
                return true;
            } else {
                return false;
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return false;
}


private void removeData(int row) {
    model.removeRow(row);
    //model.removeRows(0, model.getRowCount());
    table.updateUI();
}

private void ClearData() {
    int row = model.getRowCount();
    for (int i = 0; i < row; i++) {
        model.removeRow(row - i - 1);
    }
    table.updateUI();

    //model.removeRows(0, model.getRowCount());

}

// 保存数据，暂时是将数据从控制台显示出来
private void saveData(String filename) {
    try {
        String line = "";
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        int col = model.getColumnCount();
        int row = model.getRowCount();
        if (onto1prex != null && onto2prex != null) {
            for (int i = 0; i < row; i++) {
                line = line + onto1prex + model.getValueAt(i, 1) + ";";
                line = line + onto2prex + model.getValueAt(i, 3) + ";";
                line = line + model.getValueAt(i, 2) + "\n";
            }
        }

        if (line != "") {
            bw.write(line);
        }
        bw.close();

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


private void saveData(String filename, boolean endsim) {
    try {
        String line = ""; // 用来保存每行读取的内容

        //sim=1的结果合并
        if (endsim == true) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            int col = model.getColumnCount();
            int row = model.getRowCount();
            if (onto1prex != null && onto2prex != null) {
                for (int i = 0; i < row; i++) {
                    if (model.getValueAt(i, 2).equals("1") || model.getValueAt(i, 2).equals("1.0")) {
                        line = line + onto1prex + model.getValueAt(i, 1) + ";";
                        line = line + onto2prex + model.getValueAt(i, 3) + ";";
                        line = line + "1.0" + "\n";
                    }
                }
            }

            if (line != "") {
                bw.write(line);
            }
            bw.close();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private boolean addconvertitem(String str) {
    boolean hasitem = false;
    try {
        File convertfile = new File("Convert.txt");
        if (convertfile.length() == 0L) {
            BufferedWriter f = new BufferedWriter(new FileWriter("Convert.txt"));
            f.close();
        }


        String content = null, line = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream("Convert.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        line = reader.readLine(); // 读取第一行
        content = new String();

        while (line != null) {
            if (line.equals(str)) {
                hasitem = true;
            }
            content = content + line + "\n";
            line = reader.readLine(); // 读取下一行
        }

        if (!hasitem) {
            content = content + str + "\n";
        }

        reader.close();


        BufferedWriter bw = new BufferedWriter(new FileWriter("Convert.txt"));
        bw.write(content);
        bw.close();

    } catch (Exception ex) {

        ex.printStackTrace();
    }
    return hasitem;

}


private void deleteconvertitem(String str) {
    try {
        String content = new String(), line = null; // 用来保存每行读取的内容
        InputStream is = new FileInputStream("Convert.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        line = reader.readLine(); // 读取第一行

        while (line != null) {
            if (!line.equals(str)) {
                content = content + line + "\n";
            }

            line = reader.readLine(); // 读取下一行
        }

        reader.close();

        File f = new File("Convert.txt");   //删除旧文件
        f.delete();

        BufferedWriter bw = new BufferedWriter(new FileWriter("Convert.txt"));
        bw.write(content);
        bw.close();
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
            if (item[2].equals(owlfile1) && item[3].equals(owlfile2)) {
                listMode.addElement(item[0] + "-" + item[1]);
            }
            line = reader.readLine(); // 读取下一行
        }

        reader.close();


    } catch (Exception ex) {

        ex.printStackTrace();
    }

}

public void getpre() throws Exception {

    String filename = "result.txt";
    InputStream is = new FileInputStream(filename);
    String line; // 用来保存每行读取的内容
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine(); // 读取第一行
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    onto1pre = str[0].substring(0, index + 1);
    index = str[1].lastIndexOf("#");
    onto2pre = str[1].substring(0, index + 1);
}


public void getlinesim(String line) throws Exception  //获取该行的类、实例、属性名称
{
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    linename1 = str[0].substring(index + 1, str[0].length());
    index = str[1].lastIndexOf("#");
    linename2 = str[1].substring(index + 1, str[1].length());
    linesim = str[2];
}

public void getlinepre(String line) throws Exception {
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    linepre1 = str[0].substring(0, index + 1);
    index = str[1].lastIndexOf("#");
    linepre2 = str[1].substring(0, index + 1);
}

public static void main(String args[]) throws Exception {
    new Tabledemo();
    System.out.println("相似度计算，浙大现代制造研究所，刘运通\r\n---------------");
}
}


class Table_Model extends AbstractTableModel {

private static final long serialVersionUID = -7495940408592595397L;

private Vector content = null;


private String[] title_name = {"ID", "本体A", "相似度", "本体B"};

public Table_Model() {
    content = new Vector();
}

public Table_Model(int count) {
    content = new Vector(count);
}

public void addRow(String onto1, String simvalue, String onto2) {
    Vector v = new Vector(4);
    v.add(0, new Integer(content.size()));
    v.add(1, onto1);
    v.add(2, simvalue);
    v.add(3, onto2);
    content.add(v);
}

public void removeRow(int row) {
    content.remove(row);
}

public void removeRows(int row, int count) {
    for (int i = 0; i < count; i++) {
        if (content.size() > row) {
            content.remove(row);
        }
    }
}

/**
 * 让表格中某些值可修改，但需要setValueAt(Object value, int row, int col)方法配合才能使修改生效
 */
public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
        return false;
    }
    return true;
}

/**
 * 使修改的内容生效
 */
public void setValueAt(Object value, int row, int col) {
    ((Vector) content.get(row)).remove(col);
    ((Vector) content.get(row)).add(col, value);
    this.fireTableCellUpdated(row, col);
}

public String getColumnName(int col) {
    return title_name[col];
}

public int getColumnCount() {
    return title_name.length;
}

public int getRowCount() {
    return content.size();
}

public Object getValueAt(int row, int col) {
    return ((Vector) content.get(row)).get(col);
}

/**
 * 返回数据类型
 */
public Class getColumnClass(int col) {
    return getValueAt(0, col).getClass();
}


public void sortColumn(int col) {
    int out, in, min;

    for (out = 0; out < content.size() - 1; out++)   // outer loop
    {
        min = out;                     // minimum
        for (in = out + 1; in < content.size(); in++) // inner loop
            if (compareBooks(getValueAt(in, col), getValueAt(min, col), col) < 0)         // if min greater,
                min = in;               // we have a new min
        swap(out, min);                // swap them
    }  // end for(out)
}

private void swap(int out, int min) {
    Vector less = (Vector) content.get(min);
    Vector great = (Vector) content.get(out);
    content.add(out, less);
    content.remove(out + 1);
    content.add(min, great);
    content.remove(min + 1);
}

private int compareBooks(Object b1, Object b2, int col) {
    switch (col) {
        case 0: {
            int i1 = Integer.parseInt(b1.toString());
            int i2 = Integer.parseInt(b2.toString());
            return i1 - i2;
        }
        case 1: {
            return b1.toString().compareTo(b2.toString());
        }
        case 2: {
            return b1.toString().compareTo(b2.toString());
        }
        case 3: {
            return b1.toString().compareTo(b2.toString());
        }
        default: {
            int i1 = Integer.parseInt(b1.toString());
            int i2 = Integer.parseInt(b2.toString());
            return i1 - i2;
        }
    }
}

}


class myFilter extends FileFilter {
//	   private String files;
public boolean accept(File f) {
    if (f.isDirectory()) {
        return true;
    }

    String extension = getExtension(f);
    if (extension != null) {

        if (extension.equals("owl")) {//Java募
            return true;
        } else {
            return false;
        }
    }

    return false;
}

//
public String getDescription() {
    return "owl";
}

public static String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf(".");

    if (i > 0 && i < s.length() - 1) {
        ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
}
}

class AccessTextFile {


public ArrayList simlist = new ArrayList();
public String onto1pre, onto2pre;
public String pre1, pre2;
public String preline;

public void readToBuffer(StringBuffer buffer, InputStream is) throws IOException {
    String line; // 用来保存每行读取的内容
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine(); // 读取第一行
    if (line != null) {
        getpre(line);
    }

    while (line != null) { // 如果 line 为空说明读完了
        getlinepre(line);
        if (readnext(line)) {
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        if (line != null) {
            addsim(line);
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            preline = line;
            line = reader.readLine(); // 读取下一行
        }
    }
}


public boolean readnext(String line)  //判断是否有重复数据例如: A#a1  B#b1 1.0   B#b1 A#a1 1.0
{
    if (preline != null) {
        String[] strpre = preline.split(";");
        String[] str = line.split(";");
        if (strpre[0].equalsIgnoreCase(str[1]) && strpre[1].equalsIgnoreCase(str[0])) {
            return true;
        }
        return false;
    } else {
        return false;
    }
}

public void addsim(String line) {
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    String onto1 = str[0].substring(index + 1);
    index = str[1].lastIndexOf("#");
    String onto2 = str[1].substring(index + 1);

    simlist.add(onto1);
    simlist.add(onto2);
    simlist.add(str[2]);


}

public void getpre(String line)    //读取第一行的两个本体前缀
{
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    onto1pre = str[0].substring(0, index);
    index = str[1].lastIndexOf("#");
    onto2pre = str[1].substring(0, index);
}

public void getlinepre(String line)  //读取其他每行的两个本体前缀
{
    String[] str = line.split(";");
    int index = str[0].lastIndexOf("#");
    pre1 = str[0].substring(0, index);
    index = str[1].lastIndexOf("#");
    String onto2 = str[1].substring(index + 1);
    pre2 = str[1].substring(0, index);
}


public void writeFromBuffer(StringBuffer buffer, OutputStream os) {
    // 用 PrintStream 可以方便的把内容输出到输出流中
    // 其对象的用法和 System.out 一样
    // （System.out 本身就是 PrintStream 对象）
    PrintStream ps = new PrintStream(os);
    ps.print(buffer.toString());
}

public void copyStream(InputStream is, OutputStream os) throws IOException {
    // 这个读过过程可以参阅 readToBuffer 中的注释
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
    line = reader.readLine();
    while (line != null) {
        writer.println(line);
        line = reader.readLine();
    }
    writer.flush(); // 最后确定要把输出流中的东西都写出去了
    // 这里不关闭 writer 是因为 os 是从外面传进来的
    // 既然不是从这里打开的，也就不从这里关闭
    // 如果关闭的 writer，封装在里面的 os 也就被关了
}

public void copyTextFile(String inFilename, String outFilename) throws IOException {
    // 先根据输入/输出文件生成相应的输入/输出流
    InputStream is = new FileInputStream(inFilename);
    OutputStream os = new FileOutputStream(outFilename);
    copyStream(is, os); // 用 copyStream 拷贝内容
    is.close(); // is 是在这里打开的，所以需要关闭
    os.close(); // os 是在这里打开的，所以需要关闭
}


public AccessTextFile(String file) throws IOException {

    InputStream is = new FileInputStream(file);
    StringBuffer buffer = new StringBuffer();
    readToBuffer(buffer, is);
    //System.out.println(buffer); // 将读到 buffer 中的内容写出来
    is.close();
}

public AccessTextFile(String filesource, String filedestination) throws IOException {

    copyTextFile(filesource, filedestination);
}

}

class ClassInstPanel extends SelectableContainer {

private static final long serialVersionUID = 1L;
protected JTree _clsTree;
protected JList _instanceList;
protected JComboBox _directAllInstanceComboBox;

private static String DIRECT_INSTANCES_TEXT = "Direct Instances";
private static String ALL_INSTANCES_TEXT = "All Instances";


// static to remember the state across dialog openings.
private static Object _oldDirectAllInstancesState = DIRECT_INSTANCES_TEXT;

protected Action _viewAction;
protected AllowableAction _createAction;
protected AllowableAction _deleteAction;
protected Action _createInstAction;
protected Action _deleteInstAction;
LabeledComponent clsesComponent;

JSplitPane main;

OWLModel owlModel;

protected ClassInstPanel(OWLModel owlModel, Collection clses) {
    this.owlModel = owlModel;

    _viewAction = getViewClsAction();
    _createInstAction = getCreateInstAction();
    _deleteInstAction = getDeleteInstAction();
    _createAction = getCreateClsAction();
    _deleteAction = getDeleteClsAction();
    setPreferredSize(new Dimension(800, 600));
    if (clses.isEmpty()) {
        KnowledgeBase kb = (KnowledgeBase) owlModel;
        clses = kb.getRootClses();
    }
    createWidgets(owlModel, clses);
    fixRenderer();
}

protected Action getViewClsAction() {
    return new StandardAction(ResourceKey.INSTANCE_VIEW) {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent event) {
            String classname = getselectclass();
            OWLNamedClass parentClass = owlModel.getOWLNamedClass(classname);
            if (parentClass != null) {
                showInstance(parentClass);

            } else {
                System.out.print("can find the class");
            }
        }

        public void onView(Object o) {
            showInstance((Cls) o);
        }
    };
}

protected void showInstance(Instance instance) {
    owlModel.getProject().show(instance);

}

protected AllowableAction getCreateClsAction() {
    return new CreateClsAction() {
        private static final long serialVersionUID = 1L;

        public void onCreate() {
            String name = JOptionPane.showInputDialog("Enter name of new ");
            String classname = getselectclass();
            if (name != null) {
                OWLNamedClass parent = owlModel.getOWLNamedClass(classname);
                OWLNamedClass newclass = owlModel.createOWLNamedClass(name);
                newclass.addSuperclass(parent);
            }
        }
    };
}

protected Action getCreateInstAction() {
    return new StandardAction(ResourceKey.INSTANCE_CREATE) {
        public void actionPerformed(ActionEvent event) {
            String name = JOptionPane.showInputDialog("Enter name of new ");
            String classname = getselectclass();
            OWLNamedClass parentClass = owlModel.getOWLNamedClass(classname);
            if (parentClass != null) {
                OWLIndividual individual = parentClass.createOWLIndividual(name);
                //System.out.print(name);
                loadInstances();
            } else {
                System.out.print("can find the class");
            }
        }
    };
}

protected Action getDeleteInstAction() {
    return new StandardAction(ResourceKey.INSTANCE_DELETE) {
        public void actionPerformed(ActionEvent event) {

            String Instname = getselectInst();
            OWLIndividual individual = owlModel.getOWLIndividual(Instname);
            if (individual != null) {
                individual.delete();
                //System.out.print("hello");
                loadInstances();
            } else {
                System.out.print("can find the Inst");
            }
        }
    };
}

protected AllowableAction getDeleteClsAction() {
    return new DeleteClsAction((Selectable) _clsTree) {
        public void onCreate() {
            String classname = getselectclass();
            if (classname != null) {
                OWLNamedClass cls = owlModel.getOWLNamedClass(classname);
                cls.delete();
            }
            //System.out.print("hello");
        }
    };
}


public String getselectclass() {
    String classname = _clsTree.getSelectionPath().getLastPathComponent().toString();
    int index1 = classname.indexOf("(");
    classname = classname.substring(index1 + 1);
    index1 = classname.indexOf("(");
    classname = classname.substring(index1 + 1);
    index1 = classname.indexOf(",");
    classname = classname.substring(0, index1);
    return classname;
}

public String getselectInst() {
    String classname = _instanceList.getSelectedValue().toString();
    int index1 = classname.indexOf("(");
    classname = classname.substring(index1 + 1);
    index1 = classname.indexOf(" ");
    classname = classname.substring(0, index1);
    return classname;
}


protected LabeledComponent createClsesLabeledComponent(OWLModel owlModel, Collection clses) {
    LabeledComponent clsesComponent = new LabeledComponent("Classes", new JScrollPane(_clsTree));


    //       clsesComponent.addHeaderButton(_viewAction);
    //       clsesComponent.addHeaderButton( _createAction);
    //       clsesComponent.addHeaderButton( _deleteAction);
    //       clsesComponent.setFooterComponent(new ClsTreeFinder(kb, _clsTree));
    return clsesComponent;
}


protected JComponent createClsTree(Collection clses) {
    LazyTreeRoot root = new ParentChildRoot(clses);
    _clsTree = ComponentFactory.createSelectableTree(null, root);
    _clsTree.addTreeSelectionListener(new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent event) {
            loadInstances();
        }
    });
    FrameRenderer renderer = FrameRenderer.createInstance();
    renderer.setDisplayDirectInstanceCount(true);
    _clsTree.setCellRenderer(renderer);
    int rows = _clsTree.getRowCount();
    int diff = rows - clses.size();
    for (int i = rows - 1; i > diff; --i) {
        _clsTree.expandRow(i);
    }
    _clsTree.setSelectionRow(0);
    return _clsTree;
}

private void fixRenderer() {
    boolean displayType = _directAllInstanceComboBox.getSelectedItem().equals(ALL_INSTANCES_TEXT);
    FrameRenderer frameRenderer = (FrameRenderer) _instanceList.getCellRenderer();
    frameRenderer.setDisplayType(displayType);
}

protected LabeledComponent createInstanceLabeledComponent() {
    LabeledComponent c = new LabeledComponent(null, new JScrollPane(_instanceList));
//        c.addHeaderButton( _createInstAction);
//        c.addHeaderButton( _deleteInstAction);
    c.setHeaderComponent(createDirectAllInstanceComboBox(), BorderLayout.WEST);
//        c.setVisible(false);
//       c.setFooterComponent(new ListFinder(_instanceList, "Find Instance"));
    return c;
}

protected JComboBox createDirectAllInstanceComboBox() {
    _directAllInstanceComboBox = ComponentFactory.createComboBox();
    _directAllInstanceComboBox.addItem(DIRECT_INSTANCES_TEXT);
    _directAllInstanceComboBox.addItem(ALL_INSTANCES_TEXT);
    loadState();
    _directAllInstanceComboBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            loadInstances();
            fixRenderer();
            saveState();
        }
    });
    return _directAllInstanceComboBox;
}

private void saveState() {
    _oldDirectAllInstancesState = _directAllInstanceComboBox.getSelectedItem();
}

private void loadState() {
    _directAllInstanceComboBox.setSelectedItem(_oldDirectAllInstancesState);
}

protected JComponent createInstanceList() {
    _instanceList = ComponentFactory.createList(null);
    _instanceList.setCellRenderer(FrameRenderer.createInstance());
    _instanceList.addMouseListener(new ModalDialogCloseDoubleClickAdapter());
    return _instanceList;
}

protected void createWidgets(OWLModel owlModel, Collection clses) {
    createInstanceList();
    LabeledComponent instancesComponent = createInstanceLabeledComponent();
    createClsTree(clses);
    LabeledComponent clsesComponent = createClsesLabeledComponent(owlModel, clses);
    main = ComponentFactory.createLeftRightSplitPane(clsesComponent, instancesComponent);
    main.setDividerLocation(0.5);
    setLayout(new BorderLayout());
    add(main);
}


protected SimpleListModel getInstanceModel() {
    return (SimpleListModel) _instanceList.getModel();
}

public Collection getSelection() {
    return ComponentUtilities.getSelection(_instanceList);
}

protected void loadInstances() {
    ArrayList instances = new ArrayList();
    Iterator i = ComponentUtilities.getSelection(_clsTree).iterator();
    while (i.hasNext()) {
        Cls cls = (Cls) i.next();
        instances.addAll(getInstances(cls));
    }
    Collections.sort(instances, new FrameComparator());
    getInstanceModel().setValues(instances);
    if (!instances.isEmpty()) {
        //  _instanceList.setSelectedIndex(0);
    }
}

protected Collection<Instance> getInstances(Cls cls) {
    boolean direct = _directAllInstanceComboBox.getSelectedItem().equals(DIRECT_INSTANCES_TEXT);
    return (direct) ? cls.getDirectInstances() : cls.getInstances();
}
}

