package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.resource.Colors;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.resource.LocalizedText;
import edu.stanford.smi.protege.resource.ResourceKey;
import edu.stanford.smi.protege.server.framestore.RemoteClientFrameStore;
import edu.stanford.smi.protege.server.metaproject.impl.OperationImpl;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.ui.ParentChildRoot;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.properties.*;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;

public class test {

private JFrame frame = null;
private JPanel pane = null;
private JTextArea file1TextArea = new JTextArea();
private JFileChooser fc;
private SelectInstancesPanel panel1 = null;
private OWLPropertyHierarchiesPanel pane2 = null;
private OWLModel owlModelA;
public static String filename = null, namespace = null;
private String projectpath = null;
private boolean hasfile = false;

private MenuBar mb = new MenuBar();
private Menu m1 = new Menu("Ontology File"), m2 = new Menu("Similarity Caculation"), m3 = new Menu("Data Transformation"), m4 = new Menu("Service");
;
private MenuItem newfile = new MenuItem("New Ontology File"), open = new MenuItem("Open Ontology File"), save = new MenuItem("Save"), exit = new MenuItem("Quit");
private MenuItem sim = new MenuItem("Simulation Caculation");
private MenuItem start = new MenuItem("Start Service");
private MenuItem convert = new MenuItem("Data Transformation"), config = new MenuItem("Data Transformation Configuration");


public test() {

    frame = new JFrame("Ontology Business Interaction Integration System");
    pane = new JPanel();


    File convertfile = new File("abc.txt");
    String path = convertfile.getAbsolutePath();
    projectpath = path.substring(0, path.length() - 7);
    projectpath = projectpath.replaceAll("\\\\", "/");

    //创建一个文件过滤，使用当前目录
    fc = new JFileChooser(".");
    //过滤条件在MyFilter类中定义
    fc.addChoosableFileFilter(new myFilter());

    mb.add(m1);
    m1.add(newfile);
    m1.add(open);
    m1.add(save);
    m1.add(exit);

    m2.add(sim);
    m3.add(config);
    m3.add(convert);

    m4.add(start);

    mb.add(m2);
    mb.add(m4);
    mb.add(m3);

    newfile.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                if (panel1 != null) {
                    pane.remove(panel1);
                }
                if (pane2 != null) {
                    pane.remove(pane2);
                }

                filename = JOptionPane.showInputDialog("Please Input FileName:\n例如：abc.owl");
                namespace = JOptionPane.showInputDialog("Please Input Ontology URI:\n例如：http://www.abc.com/Ontologyabc.owl#"); //http://www.owl-ontologies.com/Ontology1223192566.owl
                if (filename != null && namespace != null) {
                    owlModelA = ProtegeOWL.createJenaOWLModel();
                    owlModelA.getNamespaceManager().setDefaultNamespace(namespace);

                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    panel1 = new SelectInstancesPanel(owlModelA, Collections.singleton(destinationClass));

                    panel1.setBounds(new Rectangle(5, 0, 600, 600));
                    pane.add(panel1);


                    OWLPropertyHierarchiesPanel pane2 = new OWLPropertyHierarchiesPanel(owlModelA);
                    pane2.setBounds(new Rectangle(610, 0, 600, 600));
                    pane.add(pane2);
                    frame.setVisible(true);
                } else if (filename != null) {
                    namespace = "http://www.owl-ontologies.com/unnamed.owl#";
                    owlModelA = ProtegeOWL.createJenaOWLModel();
                    owlModelA.getNamespaceManager().setDefaultNamespace(namespace);

                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    panel1 = new SelectInstancesPanel(owlModelA, Collections.singleton(destinationClass));

                    panel1.setBounds(new Rectangle(5, 0, 600, 600));
                    pane.add(panel1);


                    OWLPropertyHierarchiesPanel pane2 = new OWLPropertyHierarchiesPanel(owlModelA);
                    pane2.setBounds(new Rectangle(610, 0, 600, 600));
                    pane.add(pane2);
                    frame.setVisible(true);
                } else if (namespace != null) {
                    filename = "unname.owl";
                    owlModelA = ProtegeOWL.createJenaOWLModel();
                    owlModelA.getNamespaceManager().setDefaultNamespace(namespace);

                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    panel1 = new SelectInstancesPanel(owlModelA, Collections.singleton(destinationClass));

                    panel1.setBounds(new Rectangle(5, 0, 600, 600));
                    pane.add(panel1);


                    OWLPropertyHierarchiesPanel pane2 = new OWLPropertyHierarchiesPanel(owlModelA);
                    pane2.setBounds(new Rectangle(610, 0, 600, 600));
                    pane.add(pane2);
                    frame.setVisible(true);
                } else {
                    filename = "unname.owl";
                    namespace = "http://www.owl-ontologies.com/unnamed.owl#";
                    owlModelA = ProtegeOWL.createJenaOWLModel();
                    owlModelA.getNamespaceManager().setDefaultNamespace(namespace);

                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    panel1 = new SelectInstancesPanel(owlModelA, Collections.singleton(destinationClass));

                    panel1.setBounds(new Rectangle(5, 0, 600, 600));
                    pane.add(panel1);


                    OWLPropertyHierarchiesPanel pane2 = new OWLPropertyHierarchiesPanel(owlModelA);
                    pane2.setBounds(new Rectangle(610, 0, 600, 600));
                    pane.add(pane2);
                    frame.setVisible(true);
                }
                hasfile = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    });

    open.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(pane);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                filename = file.getAbsolutePath();

                //在这里添加一些对文件的处理
                try {
                    if (panel1 != null) {
                        pane.remove(panel1);
                    }
                    if (pane2 != null) {
                        pane.remove(pane2);
                    }


                    InputStream is = new FileInputStream(filename);
                    owlModelA = ProtegeOWL.createJenaOWLModelFromInputStream(is);


                    OWLNamedClass destinationClass = owlModelA.getOWLThingClass();

                    panel1 = new SelectInstancesPanel(owlModelA, Collections.singleton(destinationClass));

                    panel1.setBounds(new Rectangle(5, 0, 600, 600));
                    pane.add(panel1);


                    OWLPropertyHierarchiesPanel pane2 = new OWLPropertyHierarchiesPanel(owlModelA);
                    pane2.setBounds(new Rectangle(610, 0, 600, 600));
                    pane.add(pane2);
                    frame.setVisible(true);
                    hasfile = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    });

    save.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (hasfile)  //是否存在文件
            {
                if (filename.equals("unname.owl") && namespace.equals("http://www.owl-ontologies.com/unnamed.owl#")) {
                    filename = JOptionPane.showInputDialog("Please Input File Name:\n例如：abc.owl");
                    namespace = JOptionPane.showInputDialog("Please Input Ontology URI:\n例如：http://www.abc.com/Ontologyabc.owl#"); //http://www.owl-ontologies.com/Ontology1223192566.owl
                    if (filename != null && namespace != null) {
                        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModelA;
                        String fileName = filename;
                        Collection errors = new ArrayList();
                        jenaowlModel.save(new File(fileName).toURI(), FileUtils.langXMLAbbrev, errors);
                        System.out.println("File saved with " + errors.size() + " errors.");

                    } else {
                        namespace = "http://www.owl-ontologies.com/unnamed.owl#"; //恢复默认值
                        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModelA;
                        String fileName = filename;
                        Collection errors = new ArrayList();
                        jenaowlModel.save(new File("unname.owl").toURI(), FileUtils.langXMLAbbrev, errors);
                        System.out.println("File saved with " + errors.size() + " errors.");
                        JOptionPane.showMessageDialog(null, "文件被保存为：unname.owl", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JenaOWLModel jenaowlModel = (JenaOWLModel) owlModelA;
                    String fileName = filename;
                    Collection errors = new ArrayList();
                    jenaowlModel.save(new File(fileName).toURI(), FileUtils.langXMLAbbrev, errors);
                    System.out.println("File saved with " + errors.size() + " errors.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "文件不存在", "", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });

    exit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    });

    sim.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Tabledemo sim = new Tabledemo();
            sim.parent = frame;
            frame.setVisible(false);
        }
    });

    convert.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Convert Convert = new Convert();
            Convert.parent = frame;
            frame.setVisible(false);
        }
    });

    start.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            SendFileSocket socket = new SendFileSocket();
            socket.parent = frame;
            frame.setVisible(false);
        }
    });


    pane.setLayout(null);

    pane.add(file1TextArea);


    frame.getContentPane().add(pane);
    frame.setMenuBar(mb);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1220, 670);
    frame.setLocation(50, 150);
    frame.setTitle("异构系统本体集成平台——本体编辑");
    frame.setResizable(false);
    frame.setVisible(true);
}

public static void main(String[] args) {
    new test();
}
}


class SelectInstancesPanel extends SelectableContainer {

private static final long serialVersionUID = 1L;
protected JTree _clsTree;
protected JList _instanceList;
protected JComboBox _directAllInstanceComboBox;

private static String DIRECT_INSTANCES_TEXT = "Direct Instances";
private static String ALL_INSTANCES_TEXT = "All Instances";

// static to remember the state across dialog openings.
private static Object _oldDirectAllInstancesState = DIRECT_INSTANCES_TEXT;

protected Action _viewClsAction;
protected Action _viewinstAction;
protected Action _createAction;
protected Action _deleteAction;
protected Action _createInstAction;
protected Action _deleteInstAction;
LabeledComponent clsesComponent;

JSplitPane main;

OWLModel owlModel;

protected SelectInstancesPanel(OWLModel owlModel, Collection clses) {
    this.owlModel = owlModel;
    _viewClsAction = getViewClsAction();
    _viewinstAction = getViewInstAction();

    _createAction = getCreateClsAction();
    _createInstAction = getCreateInstAction();

    _deleteInstAction = getDeleteInstAction();

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
    return new StandardAction(ResourceKey.CLASS_VIEW) {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent event) {
            String classname = getselectclass();
            OWLNamedClass cls = owlModel.getOWLNamedClass(classname);
            if (cls != null) {
                showInstance(cls);

            } else {
                System.out.print("can find the class");
            }
        }
            /*
			public void onView(Object o) {
				showInstance((Cls) o);
            }
            */
    };
}

protected Action getViewInstAction() {
    return new StandardAction(ResourceKey.INSTANCE_VIEW) {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent event) {
            String Instname = getselectInst();
            OWLIndividual Inst = owlModel.getOWLIndividual(Instname);

            if (Inst != null) {
                showInstance(Inst);

            } else {
                System.out.print("can find the class");
            }
        }
    };
}

protected void showInstance(Instance instance) {
    owlModel.getProject().show(instance);

}

protected Action getCreateClsAction() {
    return new StandardAction(ResourceKey.CLASS_CREATE) {
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent event) {
            String name = JOptionPane.showInputDialog("Enter name of new ");
            String classname = getselectclass();
            if (name != null) {
                OWLNamedClass parent = owlModel.getOWLNamedClass(classname);
                owlModel.createOWLNamedSubclass(name, parent);
                //   newclass.addSuperclass(parent);


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
                System.out.print(name);
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
                System.out.print("hello");
                loadInstances();
            } else {
                System.out.print("can find the Inst");
            }
        }
    };
}

protected Action getDeleteClsAction() {
    return new StandardAction(ResourceKey.CLASS_DELETE) {
        public void actionPerformed(ActionEvent event) {
            String classname = getselectclass();
            OWLNamedClass cls = owlModel.getOWLNamedClass(classname);
            if (cls != null) {
                cls.delete();
            }
            System.out.print("hello");
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
    LabeledComponent clsesComponent = new LabeledComponent("Allowed Classes", new JScrollPane(_clsTree));
    clsesComponent.addHeaderButton(_viewClsAction);
    clsesComponent.addHeaderButton(_createAction);
    clsesComponent.addHeaderButton(_deleteAction);
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
    c.addHeaderButton(_viewinstAction);
    c.addHeaderButton(_createInstAction);
    c.addHeaderButton(_deleteInstAction);
    c.setHeaderComponent(createDirectAllInstanceComboBox(), BorderLayout.WEST);

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
    main.setDividerLocation(WIDTH / 2);
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
        _instanceList.setSelectedIndex(0);
    }
}

protected Collection<Instance> getInstances(Cls cls) {
    boolean direct = _directAllInstanceComboBox.getSelectedItem().equals(DIRECT_INSTANCES_TEXT);
    return (direct) ? cls.getDirectInstances() : cls.getInstances();
}
}

class OWLPropertyHierarchiesPanel extends JPanel implements Selectable, HostResourceDisplay {


private static final long serialVersionUID = 1L;

private OWLModel owlModel;

private JTabbedPane tabbedPane;

private OWLPropertyHierarchyPanel objectPropertyHierarchy;

private OWLPropertyHierarchyPanel datatypePropertyHierarchy;

private OWLPropertyHierarchyPanel annotationPropertyHierarchy;

private OWLPropertyHierarchyPanel allPropertiesHierarchy;

ArrayList listeners;

public OWLPropertyHierarchiesPanel(OWLModel owlModel) {
    this.owlModel = owlModel;
    listeners = new ArrayList();
    createUI();
}

private void createUI() {
    setLayout(new BorderLayout());
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.add("All", allPropertiesHierarchy = createPanel(new OWLSubpropertyPane(owlModel), "OWLObjectProperty"));
    tabbedPane.add("Object", objectPropertyHierarchy = createPanel(new OWLObjectPropertySubpropertyPane(owlModel), "OWLObjectProperty"));
    tabbedPane.add("Datatype", datatypePropertyHierarchy = createPanel(new OWLDatatypePropertySubpropertyPane(owlModel), "OWLDatatypeProperty"));
    tabbedPane.add("Annotation", annotationPropertyHierarchy = createPanel(new OWLAnnotationPropertySubpropertyPane(owlModel), "OWLDatatypeProperty"));
    add(tabbedPane);
    tabbedPane.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (isShowing()) {
                notifySelectionListeners();
            }
        }
    });
    JLabel label = ComponentFactory.createLabel(Icons.getProjectIcon());
    label.setText(test.filename);
    //	label.setText(owlModel.getProject().getName());
    String forProjectLabel = LocalizedText.getText(ResourceKey.CLASS_BROWSER_FOR_PROJECT_LABEL);
    HeaderComponent header = new HeaderComponent("属性编辑：", forProjectLabel, label);
    header.setColor(Colors.getSlotColor());
    add(header, BorderLayout.NORTH);
}

private OWLPropertyHierarchyPanel createPanel(OWLSubpropertyPane subpropertyPane, String iconBase) {
    subpropertyPane.addSelectionListener(new SelectionListener() {
        public void selectionChanged(SelectionEvent event) {
            notifySelectionListeners();
        }
    });
    OWLSuperpropertiesPanel superpropertiesPanel = new OWLSuperpropertiesPanel(subpropertyPane, owlModel);
    superpropertiesPanel.setAddActionIconBase(iconBase);
    superpropertiesPanel.setRemoveActionIconBase(iconBase);
    return new OWLPropertyHierarchyPanel(subpropertyPane, superpropertiesPanel);
}


public Collection getSelection() {
    OWLPropertyHierarchyPanel panel = ((OWLPropertyHierarchyPanel) tabbedPane.getSelectedComponent());
    if (panel != null) {
        return panel.getSubpropertyPane().getSelection();
    } else {
        return Collections.EMPTY_LIST;
    }
}


public void addSelectionListener(SelectionListener selectionListener) {
    listeners.add(selectionListener);
}


public void clearSelection() {
}


public void notifySelectionListeners() {
    for (Iterator it = new ArrayList(listeners).iterator(); it.hasNext(); ) {
        SelectionListener curListener = (SelectionListener) it.next();
        final SelectionEvent event = new SelectionEvent(this, SelectionEvent.SELECTION_CHANGED);
        curListener.selectionChanged(event);
    }
}


public void removeSelectionListener(SelectionListener selectionListener) {
    listeners.remove(selectionListener);
}


public static void main(String[] args) {
    try {
        OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
        owlModel.createOWLObjectProperty("A");
        OWLPropertyHierarchiesPanel panel = new OWLPropertyHierarchiesPanel(owlModel);
        JFrame f = new JFrame();
        f.setSize(300, 700);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(panel);
        f.show();
    } catch (Exception e) {
        Log.getLogger().log(Level.SEVERE, "Exception caught", e);
    }
}

public boolean displayHostResource(RDFResource resource) {
    boolean result = false;
    if (resource instanceof RDFProperty) {
        RDFProperty property = (RDFProperty) resource;
        OWLPropertyHierarchyPanel panel = allPropertiesHierarchy;

        if (property.isAnnotationProperty()) {
            panel = annotationPropertyHierarchy;
        } else if (property instanceof OWLObjectProperty) {
            panel = objectPropertyHierarchy;
        } else {
            panel = datatypePropertyHierarchy;
        }

        tabbedPane.setSelectedComponent(panel);
        result = panel.getSubpropertyPane().displayHostResource(property);
    }
    return result;
}

public void setEnabled(boolean enabled) {
    enabled = enabled && RemoteClientFrameStore.isOperationAllowed(owlModel, OperationImpl.PROPERTY_TAB_WRITE);
    objectPropertyHierarchy.setEnabled(enabled);
    datatypePropertyHierarchy.setEnabled(enabled);
    annotationPropertyHierarchy.setEnabled(enabled);
    allPropertiesHierarchy.setEnabled(enabled);
    super.setEnabled(enabled);
}

;

public void setHierarchyTreeRenderer(FrameRenderer renderer) {
    objectPropertyHierarchy.setRenderer(renderer);
    datatypePropertyHierarchy.setRenderer(renderer);
    annotationPropertyHierarchy.setRenderer(renderer);
    allPropertiesHierarchy.setRenderer(renderer);
}
}

class OWLPropertyHierarchyPanel extends JPanel {

private OWLSubpropertyPane subpropertyPane;

private OWLSuperpropertiesPanel superpropertiesPanel;

public OWLPropertyHierarchyPanel(OWLSubpropertyPane subpropertyPane,
                                 OWLSuperpropertiesPanel superpropertiesPanel) {
    this.subpropertyPane = subpropertyPane;
    this.superpropertiesPanel = superpropertiesPanel;
    createUI();
}

private void createUI() {
    setLayout(new BorderLayout(7, 7));
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
    splitPane.setTopComponent(subpropertyPane);
    splitPane.setBottomComponent(superpropertiesPanel);
    splitPane.setBorder(null);
    add(splitPane);
    splitPane.setDividerLocation(600);
    subpropertyPane.addSelectionListener(new SelectionListener() {
        public void selectionChanged(SelectionEvent event) {
            RDFProperty property = (RDFProperty) CollectionUtilities.getFirstItem(subpropertyPane.getSelection());
            superpropertiesPanel.setProperty(property, null);
        }
    });
}


public OWLSubpropertyPane getSubpropertyPane() {
    return subpropertyPane;
}


public OWLSuperpropertiesPanel getSuperpropertiesPanel() {
    return superpropertiesPanel;
}

public void setEnabled(boolean enabled) {
    enabled = enabled && RemoteClientFrameStore.isOperationAllowed(subpropertyPane.getOWLModel(), OperationImpl.PROPERTY_TAB_WRITE);
    subpropertyPane.setEnabled(enabled);
    superpropertiesPanel.setEnabled(enabled);
    super.setEnabled(enabled);
}

;

public void setRenderer(FrameRenderer renderer) {
    subpropertyPane.setRenderer(renderer);
}
}






