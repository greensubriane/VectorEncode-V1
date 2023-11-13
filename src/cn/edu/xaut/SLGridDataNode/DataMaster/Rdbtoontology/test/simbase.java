package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class simbase {
private static OWLModel owlmodelA, owlmodelB;
private static String path = null;
private static String onto1prex = null, onto2prex = null;
ArrayList clslistA = new ArrayList(), clslistB = new ArrayList(), clssimlist = new ArrayList(), subclssimlist = new ArrayList();
ArrayList propertylistA = new ArrayList(), propertylistB = new ArrayList(), propertysimlist = new ArrayList(), datapropertysimlist = new ArrayList();
ArrayList instsimlist = new ArrayList();
String linename1, linename2, linesim;

private static final float CUTOFF = 0.5f, CUTOFFInst = 0.15f;  //0.25;0.31;0.35(0.7);0.9(0.95)


public simbase(OWLModel owlmodel1, OWLModel owlmodel2, String projectpath) {
    try {
        owlmodelA = owlmodel1;
        owlmodelB = owlmodel2;
        path = projectpath;

        if (owlmodelA != null && owlmodelB != null && projectpath != null) {
            OWLNamespaceManager nmger = (OWLNamespaceManager) owlmodelA.getNamespaceManager();
            onto1prex = nmger.getDefaultNamespace();
            nmger = (OWLNamespaceManager) owlmodelB.getNamespaceManager();
            onto2prex = nmger.getDefaultNamespace();
            simcompute("result.txt", true);
        } else {
            JOptionPane.showMessageDialog(null, "本体文件不能为空", "相似度计算错误", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public simbase(OWLModel owlmodel1, OWLModel owlmodel2, String projectpath, boolean iscls) {
    try {
        owlmodelA = owlmodel1;
        owlmodelB = owlmodel2;
        path = projectpath;

        if (owlmodelA != null && owlmodelB != null && projectpath != null) {
            OWLNamespaceManager nmger = (OWLNamespaceManager) owlmodelA.getNamespaceManager();
            onto1prex = nmger.getDefaultNamespace();
            nmger = (OWLNamespaceManager) owlmodelB.getNamespaceManager();
            onto2prex = nmger.getDefaultNamespace();
            if (iscls) {
                simcompute("result.txt");
            }
        } else {
            JOptionPane.showMessageDialog(null, "本体文件不能为空", "相似度计算错误", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


private ArrayList Subclssim() {
    ArrayList newsimlist = new ArrayList();

    try {
        String clsnameA = null, clsnameB = null, maxsimnameB = null, nameA = null, nameB = null;
        float maxsim = 0f;

        Collection Col1 = owlmodelA.getUserDefinedOWLNamedClasses();
        Iterator it = Col1.iterator();
        while (it.hasNext()) {
            OWLNamedClass clsA = (OWLNamedClass) it.next();
            clsnameA = clsA.getName();


            maxsim = 0f;
            maxsimnameB = null;

            if (clsnameA.contains(".")) {
                int index = clsnameA.indexOf(".");
                String praentnameA = clsnameA.substring(0, index);
                nameA = clsnameA.substring(index + 1);

                String praentnameB = findsimcls(praentnameA);
                OWLNamedClass praentclsB = null;
                if (praentnameB != null) {
                    praentclsB = owlmodelB.getOWLNamedClass(praentnameB);
                }
                if (praentclsB != null) {
                    Collection col = praentclsB.getSubclasses(true);
                    Iterator jt = col.iterator();
                    while (jt.hasNext()) {
                        OWLNamedClass cls = (OWLNamedClass) jt.next();
                        clsnameB = cls.getName();

                        if (clsnameA.contains(".")) {
                            index = clsnameB.indexOf(".");
                            nameB = clsnameB.substring(index + 1);

                            int EditDistance = getEditDistance(nameA, nameB);
                            String s2 = String.valueOf(EditDistance);
                            float dis = Float.parseFloat(s2);
                            float sim = dis / Float.parseFloat(String.valueOf(maxlength(nameA, nameB)));
                            sim = 1.0f - sim;
                            if (sim > maxsim) {
                                maxsim = sim;
                                maxsimnameB = clsnameB;
                            }
                        }
                    }
                }

                if (maxsim > CUTOFF) {
                    String simvaule;
                    float simclsvaule = findsimvaule(praentnameA, praentnameB);
                    simclsvaule = simclsvaule * maxsim;

                    newsimlist.add(clsnameA);
                    newsimlist.add(maxsimnameB);
                    if (haspresim(clsnameA, maxsimnameB)) {
                        simvaule = getpresim(clsnameA, maxsimnameB);
                    } else {
                        simvaule = String.valueOf(simclsvaule);
                    }

                    newsimlist.add(simvaule);
                }
            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
    return newsimlist;
}

private float simvauleclsprop(OWLNamedClass clsA, OWLNamedClass clsB) {
    float sim = 0f, simnum = 0f, simpnum = 0f, simpname = 0f;
    int pnum1 = 0, pnum2 = 0;
    Collection colA = clsA.getAssociatedProperties();
    Collection colB = clsB.getAssociatedProperties();

    pnum1 = colA.size();
    pnum2 = colB.size();

    String s = null;         //属性数量最小值
    if (pnum1 >= pnum2) {
        s = String.valueOf(pnum1);
    } else {
        s = String.valueOf(pnum2);
    }

    simnum = clsobjectpropertysim(pnum1, pnum2);   //计算属性数量相似度

    int pobjectnum1 = 0, pobjectnum2 = 0;
    pobjectnum1 = getobjectpropertynum(clsA);
    pobjectnum2 = getobjectpropertynum(clsB);

    simpnum = clspropertysim(pobjectnum1, pobjectnum2);   //计算object属性数量相似度


    int index = -1;
    String pnameA = null, pnameB = null;

    float maxsim = 0f;
    Iterator iA = colA.iterator();
    while (iA.hasNext()) {
        OWLProperty pA = (OWLProperty) iA.next();
        pnameA = pA.getName();

        String maxsimnameB = null;

        Iterator iB = colB.iterator();
        while (iB.hasNext()) {
            float simtemp = 0f;

            OWLProperty pB = (OWLProperty) iB.next();
            pnameB = pB.getName();
            if (pA.isObjectProperty() & pB.isObjectProperty()) {
                index = pnameA.indexOf(".") + 1;
                pnameA = pnameA.substring(index);
                index = pnameA.indexOf(".") + 1;
                pnameA = pnameA.substring(index);

                index = pnameB.indexOf(".") + 1;
                pnameB = pnameB.substring(index);
                index = pnameB.indexOf(".") + 1;
                pnameB = pnameB.substring(index);

                int EditDistance = getEditDistance(pnameA, pnameB);
                String s2 = String.valueOf(EditDistance);
                float dis = Float.parseFloat(s2);
                simtemp = dis / Float.parseFloat(String.valueOf(maxlength(pnameA, pnameB)));
                simtemp = 1.0f - simtemp;
                if (simtemp > maxsim) {
                    maxsim = simtemp;
                    //maxsimnameB=pB.getName();
                }
            } else if (!pA.isObjectProperty() & !pB.isObjectProperty()) {
                index = pnameA.indexOf(".") + 1;
                pnameA = pnameA.substring(index);
                index = pnameB.indexOf(".") + 1;
                pnameB = pnameB.substring(index);
                int EditDistance = getEditDistance(pnameA, pnameB);
                String s2 = String.valueOf(EditDistance);
                float dis = Float.parseFloat(s2);
                simtemp = dis / Float.parseFloat(String.valueOf(maxlength(pnameA, pnameB)));
                simtemp = 1.0f - simtemp;
                if (simtemp > maxsim) {
                    maxsim = simtemp;
                    //maxsimnameB=pB.getName();
                }
            }
        }
        index = index + 1;
        float minP = Float.parseFloat(s);
        simpname = simpname + maxsim / minP;
    }

    //System.out.print(nameA+"********\n");

    sim = 0.1f * simnum + 0.1f * simpnum + 0.8f * simpname;

    return sim;
}

protected OWLNamedClass getOWLNamedClsSafe(OWLModel owlModel, String ClsName) {

    try {
        OWLNamedClass clsA = owlModel.getOWLNamedClass(ClsName);
        return clsA;
    } catch (Exception e) {
        return null;
    }
}

protected OWLProperty getOWLPropertySafe(OWLModel owlModel, String propName) {

    try {
        OWLProperty property = owlModel.getOWLProperty(propName);
        return property;
    } catch (Exception e) {
        return null;
    }
}

private String getpropertynameA(String pname) //获得属性名前一部分
{
    String prop = null;
    try {
        int index = pname.indexOf(".");
        prop = pname.substring(index + 1);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return prop;
}

private String findsimdataProp(String propname) {
    try {
        if (datapropertysimlist.contains(propname)) {
            int index = datapropertysimlist.indexOf(propname);

            return datapropertysimlist.get(index + 1).toString();

        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

private float findsimvaule(String clsnameA, String clsnameB) {
    try {
        if (clssimlist.contains(clsnameA)) {
            int index = clssimlist.indexOf(clsnameA);
            String nameB = clssimlist.get(index + 1).toString();
            if (nameB.equals(clsnameB)) {
                String sim = clssimlist.get(index + 2).toString();
                float simvaule = Float.parseFloat(sim);
                if (simvaule > CUTOFF) {
                    return simvaule;
                }
            }
        }
        return 0f;
    } catch (Exception ex) {
        ex.printStackTrace();
        return 0f;
    }
}

private String findsimProp(String propname) {
    try {
        if (propertysimlist.contains(propname)) {
            int index = propertysimlist.indexOf(propname);
            String sim = propertysimlist.get(index + 2).toString();
            float simvaule = Float.parseFloat(sim);
            if (simvaule > CUTOFF) {
                return propertysimlist.get(index + 1).toString();
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
    return null;
}


private String findsimcls(String clsname) {
    try {
        if (clssimlist.contains(clsname)) {
            int index = clssimlist.indexOf(clsname);
            String sim = clssimlist.get(index + 2).toString();
            float simvaule = Float.parseFloat(sim);
            if (simvaule > CUTOFF) {
                sim = null;
                return clssimlist.get(index + 1).toString();
            } else {
                sim = null;
                return null;
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

private float finddatapropsimvaule(String clsnameA, String clsnameB) {
    try {
        if (datapropertysimlist.contains(clsnameA)) {
            int index = datapropertysimlist.indexOf(clsnameA);
            String nameB = datapropertysimlist.get(index + 1).toString();
            if (nameB.equals(clsnameB)) {
                String sim = datapropertysimlist.get(index + 2).toString();
                float simvaule = Float.parseFloat(sim);
                if (simvaule > CUTOFF) {
                    return simvaule;
                }
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return 0f;
}


public ArrayList simobjectproperty(ArrayList clslist1) {
    ArrayList newsimlist = new ArrayList();

    try {
        Iterator ctA = clslist1.iterator();
        while (ctA.hasNext()) {
            String name1 = ctA.next().toString();
            OWLNamedClass cls1A = owlmodelA.getOWLNamedClass(name1);
            String clsname1A = cls1A.getName();

            String clsname2A = findsimcls(clsname1A);

            if (clsname2A != null) {
                Collection col = cls1A.getAssociatedProperties();
                Iterator cl = col.iterator();
                while (cl.hasNext()) {
                    OWLProperty property = (OWLProperty) cl.next();
                    String pnameA = property.getName();
                    if (property.isFunctional() && property.isObjectProperty()) {
                        OWLNamedClass res = (OWLNamedClass) property.getRange();
                        String clsname1B = res.getName();


                        String clsname2B = findsimcls(clsname1B);
                        if (clsname1B != null && clsname2B != null) {
                            OWLNamedClass cls2A = owlmodelB.getOWLNamedClass(clsname2A);
                            OWLNamedClass cls2B = owlmodelB.getOWLNamedClass(clsname2B);

                            Collection col2 = cls2A.getAssociatedProperties();
                            Iterator c2 = col2.iterator();
                            while (c2.hasNext()) {
                                OWLProperty propertyB = (OWLProperty) c2.next();
                                String pnameB = propertyB.getName();

                                if (propertyB.isFunctional() && propertyB.isObjectProperty()) {
                                    RDFResource resB = propertyB.getRange();
                                    String rangenameB = resB.getName();
                                    if (clsname2B.equals(rangenameB) && !newsimlist.contains(pnameA)) {
                                        newsimlist.add(pnameA);
                                        newsimlist.add(pnameB);
                                        float simvaule1 = findsimvaule(clsname1A, clsname2A);
                                        float simvaule2 = findsimvaule(clsname1B, clsname2B);
                                        float simvaule = (simvaule1 + simvaule2) / 2.0f;
                                        String vaule = String.valueOf(simvaule);
                                        newsimlist.add(vaule);
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

public ArrayList simdataproperty(ArrayList clslist) {
    ArrayList newsimlist = new ArrayList();
    float sim = 0f, simmax = 0f;

    OWLProperty mixpropertyB = null;
    String pmaxnameB = null;

    try {
        Iterator ctA = clslist.iterator();
        while (ctA.hasNext()) {
            String name = ctA.next().toString();
            OWLNamedClass clsA = getOWLNamedClsSafe(owlmodelA, name);
            String clsnameA = clsA.getName();
            String clsnameB = findsimcls(clsnameA);
            OWLNamedClass clsB = null;
            if (clsnameB != null) {
                clsB = getOWLNamedClsSafe(owlmodelB, clsnameB);
            }
            if (clsB != null) {
                Collection col = clsA.getAssociatedProperties();
                Iterator cl = col.iterator();
                while (cl.hasNext()) {
                    OWLProperty propertyA = (OWLProperty) cl.next();
                    String pnameA = propertyA.getName();

                    if (propertyA.isFunctional() && !propertyA.isObjectProperty()) {
                        mixpropertyB = null;
                        simmax = 0f;

                        Collection col2 = clsB.getAssociatedProperties();
                        Iterator c2 = col2.iterator();
                        while (c2.hasNext()) {
                            OWLProperty propertyB = (OWLProperty) c2.next();
                            if (propertyB.isFunctional() && !propertyB.isObjectProperty()) {
                                String pnameB = propertyB.getName();

                                String p1 = getpropertynameA(pnameA);  //前端编辑距离
                                String p2 = getpropertynameA(pnameB);

                                //int dis=getEditDistance(p1,p2);
                                int dis = getEditDistance(p1, p2);
                                String s2 = String.valueOf(dis);
                                float disf = Float.parseFloat(s2);
                                sim = disf / Float.parseFloat(String.valueOf(maxlength(p1, p2)));
                                sim = 1.0f - sim;

                                if (sim > simmax) {
                                    simmax = sim;
                                    pmaxnameB = pnameB;
                                    mixpropertyB = propertyB;

                                }
                            }
                        }

                        if (simmax > 0.2 & mixpropertyB != null) {
                            String vaule;

                            float simvaule = findsimvaule(clsA.getName(), clsB.getName());
                            simvaule = simvaule * simmax;

                            int index = mixpropertyB.getName().indexOf(".") + 1;
                            String nameB = mixpropertyB.getName().substring(0, index);

                            newsimlist.add(propertyA.getName());
                            newsimlist.add(mixpropertyB.getName());
                            if (haspresim(propertyA.getName(), mixpropertyB.getName())) {
                                vaule = getpresim(propertyA.getName(), mixpropertyB.getName());
                            } else {
                                vaule = String.valueOf(simvaule);
                            }

                            newsimlist.add(vaule);
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

public void simcompute(String simfile, boolean hasinst) {
    String line; // 用来保存每行读取的内容

    try {    //初始化参数
        Collection allclsA = owlmodelA.getUserDefinedOWLNamedClasses();
        Iterator clsItA = allclsA.iterator();
        while (clsItA.hasNext()) {
            OWLNamedClass cls = (OWLNamedClass) clsItA.next();
            if (!cls.getName().contains(".")) {
                clslistA.add(cls.getName());
            }

        }

        Collection allclsB = owlmodelB.getUserDefinedOWLNamedClasses();
        Iterator clsItB = allclsB.iterator();
        while (clsItB.hasNext()) {
            OWLNamedClass cls = (OWLNamedClass) clsItB.next();
            if (!cls.getName().contains(".")) {
                clslistB.add(cls.getName());
            }
        }


        Collection allpropertyA = owlmodelA.getUserDefinedOWLObjectProperties();
        Iterator propertyItA = allpropertyA.iterator();
        while (propertyItA.hasNext()) {
            OWLProperty property = (OWLProperty) propertyItA.next();
            propertylistA.add(property.getName());
        }

        Collection allpropertyB = owlmodelB.getUserDefinedOWLObjectProperties();
        Iterator propertyItB = allpropertyB.iterator();
        while (propertyItB.hasNext()) {
            OWLProperty property = (OWLProperty) propertyItB.next();
            propertylistB.add(property.getName());
        }

        //计算相似度
        clssimlist = Clssim();   //根据名称编辑距离计算类相似度
        propertysimlist = simobjectproperty(clslistA);
        datapropertysimlist = simdataproperty(clslistA);
        subclssimlist = Subclssim();

        //保存相似度
        line = "";
        BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"));

        Iterator i1 = clssimlist.iterator();
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


        Iterator i2 = propertysimlist.iterator();
        while (i2.hasNext()) {
            line = onto1prex + i2.next().toString();
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


        Iterator i3 = datapropertysimlist.iterator();
        while (i3.hasNext()) {
            line = onto1prex + i3.next().toString();
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


        Iterator i4 = subclssimlist.iterator();
        while (i4.hasNext()) {
            line = onto1prex + i4.next().toString();
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

        bw.close();

        subclssimlist = null;

        Iterator i6 = clslistA.iterator();
        int i = 0;

        if (hasinst) {
            while (i6.hasNext()) {
                String clsname = i6.next().toString();

                if (!clsname.contains(".")) {
                    i = i + 1;
                    if (i < 10) {
                        try {
                            ArrayList Instancesimlist = instsim(clsname);
                            Iterator i5 = Instancesimlist.iterator();
                            while (i5.hasNext()) {
                                //RandomAccessFile rf=new RandomAccessFile("result.txt","rw");
                                line = onto1prex + i5.next().toString();
                                if (i5.hasNext()) {
                                    line = line + ";" + onto2prex + i5.next().toString();

                                }
                                if (i5.hasNext()) {
                                    line = line + ";" + i5.next().toString() + "\n";
                                }

                                if (line != "") {
                                    appendToFile(line, "result.txt");
                                }
                            }
                            Instancesimlist = null;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }


    } catch (Exception ex) {

        ex.printStackTrace();
    }

}

public void simcompute(String simfile) {
    String line; // 用来保存每行读取的内容

    try {    //初始化参数
        Collection allclsA = owlmodelA.getUserDefinedOWLNamedClasses();
        Iterator clsItA = allclsA.iterator();
        while (clsItA.hasNext()) {
            OWLNamedClass cls = (OWLNamedClass) clsItA.next();
            if (!cls.getName().contains(".")) {
                clslistA.add(cls.getName());
            }

        }

        Collection allclsB = owlmodelB.getUserDefinedOWLNamedClasses();
        Iterator clsItB = allclsB.iterator();
        while (clsItB.hasNext()) {
            OWLNamedClass cls = (OWLNamedClass) clsItB.next();
            if (!cls.getName().contains(".")) {
                clslistB.add(cls.getName());
            }
        }

        //计算相似度
        clssimlist = Clssim();   //根据名称编辑距离计算类相似度
        subclssimlist = Subclssim();


        //保存相似度
        line = "";
        BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"));

        Iterator i1 = clssimlist.iterator();
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

        Iterator i4 = subclssimlist.iterator();
        while (i4.hasNext()) {
            line = onto1prex + i4.next().toString();
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

        bw.close();

    } catch (Exception ex) {

        ex.printStackTrace();
    }

}

private ArrayList instsim(String clsname) {
    ArrayList newsimlist = new ArrayList();
    try {
        OWLNamedClass clsA = getOWLNamedClsSafe(owlmodelA, clsname);
        String clsnameA = clsA.getName();
        String clsnameB = findsimcls(clsnameA);

        OWLNamedClass clsB = null;
        if (clsnameB != null) {
            clsB = getOWLNamedClsSafe(owlmodelB, clsnameB);
        }
        if (clsA != null && clsB != null) {
            Collection colA = clsA.getInstances();   //获得A实例集合
            Iterator cl = colA.iterator();
            while (cl.hasNext())                    //实例A的循环
            {
                OWLIndividual InstA = (OWLIndividual) cl.next();
                String InstnameA = InstA.getName();
                float maxendsim = 0f;
                String maxInstnameB = null;  //相似度最大的的实例

                Collection colB = clsB.getInstances();   //获得B实例集合
                Iterator c2 = colB.iterator();
                while (c2.hasNext())                 //实例B的循环
                {
                    OWLIndividual InstB = (OWLIndividual) c2.next();
                    String InstnameB = InstB.getName();

                    Collection propcollection = clsA.getAssociatedProperties();
                    int propnum = propcollection.size();
                    Iterator ip = propcollection.iterator();
                    float endsim = 0f, datasim = 0f, objectsim = 0f;
                    int i = 0, j = 0;
                    while (ip.hasNext())               //属性的循环
                    {
                        OWLProperty propertyA = (OWLProperty) ip.next();
                        String propertyAName = propertyA.getName();
                        String InatAPvaule = null, propertyBName = null, InatBPvaule = null, s2 = null;
                        if (propertyA.isFunctional() && !propertyA.isObjectProperty()) {
                            i = i + 1;
                            Object o1 = InstA.getPropertyValue(propertyA);
                            if (o1 != null) {
                                InatAPvaule = InstA.getPropertyValue(propertyA).toString();
                                propertyBName = findsimdataProp(propertyAName); //找到对应属性值
                                OWLProperty propertyB = getOWLPropertySafe(owlmodelB, propertyBName);
                                if (propertyB != null) {
                                    Object o2 = InstB.getPropertyValue(propertyB);
                                    if (o2 != null) {
                                        InatBPvaule = InstB.getPropertyValue(propertyB).toString();

                                        //int dis=getEditDistance(p1,p2);
                                        int dis = getEditDistance(InatAPvaule, InatBPvaule);
                                        s2 = String.valueOf(dis);
                                        float disf = Float.parseFloat(s2);
                                        float sim = disf / Float.parseFloat(String.valueOf(maxlength(InatAPvaule, InatBPvaule)));
                                        sim = 1.0f - sim;
                                        datasim = datasim + sim;
                                    }
                                    o2 = null;
                                }
                                propertyB = null;
                            }
                            o1 = null;
                        } else {
                            j = j + 1;

                            OWLProperty propertyB = null;
                            propertyBName = findsimProp(propertyAName); //找到对应属性值

                            if (propertyBName != null) {
                                propertyB = getOWLPropertySafe(owlmodelB, propertyBName);
                            }
                            if (propertyB != null)    //找到相关属性值
                            {

                                OWLIndividual insttempA = (OWLIndividual) InstA.getPropertyValue(propertyA);
                                if (insttempA != null) {
                                    InatAPvaule = insttempA.getName();
                                }


                                OWLIndividual insttempB = (OWLIndividual) InstB.getPropertyValue(propertyB);

                                int dis = 0;
                                if (insttempB != null & insttempA != null) {
                                    InatBPvaule = insttempB.getName();

                                    int index = -1;
                                    index = InatAPvaule.indexOf("_");
                                    InatAPvaule = InatAPvaule.substring(index + 1);
                                    index = InatBPvaule.indexOf("_");
                                    InatBPvaule = InatBPvaule.substring(index + 1);

                                    dis = getEditDistance(InatAPvaule, InatBPvaule);
                                    s2 = String.valueOf(dis);
                                    float disf = Float.parseFloat(s2);
                                    float sim = disf / Float.parseFloat(String.valueOf(maxlength(InatAPvaule, InatBPvaule)));
                                    sim = 1.0f - sim;
                                    objectsim = objectsim + sim;
                                }
                                insttempA = null;
                                insttempB = null;
                            }
                            propertyB = null;
                        }
                        propertyAName = null;
                        InatAPvaule = null;
                        propertyBName = null;
                        InatBPvaule = null;
                        s2 = null;
                        propertyA = null;
                    }

                    propcollection = null;
                    ip = null;

                    endsim = (datasim + objectsim) / Float.parseFloat(String.valueOf(i + j));//属性个数对应的浮点数

                    if (endsim > maxendsim) {
                        maxendsim = endsim;
                        maxInstnameB = InstnameB;
                    }
                    propcollection = null;
                    InstnameB = null;
                }

                //所有属性都计算过后
                if (maxInstnameB != null && InstnameA != null && maxendsim > CUTOFFInst) {
                    newsimlist.add(InstnameA);
                    newsimlist.add(maxInstnameB);
                    newsimlist.add(String.valueOf(maxendsim));
                    System.out.print(InstnameA + "\n");
                    System.out.print(maxInstnameB + "\n");
                    System.out.print(maxendsim + "\n");
                }
                colB = null;
                InstA = null;
                InstnameA = null;
                maxInstnameB = null;
                c2 = null;
            }
            colA = null;
            cl = null;
        }
        clsA = null;
        clsB = null;
        clsnameA = null;
        clsnameB = null;
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return newsimlist;
}

private boolean haspresim(String nameA, String nameB) {
    try {
        String line = null;
        File f = new File("endresults.txt");
        if (f.length() != 0L) {
            InputStream is = new FileInputStream("endresults.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行

            while (line != null) {
                getlinesim(line);
                if (linename1.equals(nameA) && linename2.equals(nameB)) {
                    return true;
                }
                line = reader.readLine(); // 读取下一行
            }
            reader.close();
        }
    } catch (Exception ex) {

        ex.printStackTrace();
    }

    return false;
}

private String getpresim(String nameA, String nameB) {
    try {
        String line = null;
        File f = new File("endresults.txt");
        if (f.length() != 0L) {
            InputStream is = new FileInputStream("endresults.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行

            while (line != null) {
                getlinesim(line);
                String[] s = line.split(";");
                if (linename1.equals(nameA) && linename2.equals(nameB)) {
                    return s[2];
                }
                line = reader.readLine(); // 读取下一行
            }
            reader.close();
        }
        return null;
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}


private int getobjectpropertynum(OWLNamedClass cls) {
    try {
        Collection colA = cls.getAssociatedProperties();
        Iterator it = colA.iterator();
        int i = 0;
        while (it.hasNext()) {
            OWLProperty property = (OWLProperty) it.next();
            if (property.isObjectProperty()) {
                i++;
            }
        }
        return i;
    } catch (Exception ex) {
        return -1;
    }

}


private float clsobjectpropertysim(int numA, int numB) {
    float sim = 0f;
    try {
        String s1 = String.valueOf(numA);
        float numpropertyA = Float.parseFloat(s1);
        String s2 = String.valueOf(numB);
        float numpropertyB = Float.parseFloat(s2);
        float sub = Math.abs(numpropertyA - numpropertyB);
        if (numA == numB) {
            sim = 0.4f;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return sim;
    }
    return sim;
}

private ArrayList Clssim() {
    ArrayList newsimlist = new ArrayList();
    try {
        String nameA = null, nameB = null, maxsimnameB = null;
        float maxsim = 0f;


        Iterator it = clslistA.iterator();
        while (it.hasNext()) {
            nameA = (String) it.next();
            OWLNamedClass clsA = owlmodelA.getOWLNamedClass(nameA);

            //clsA=owlmodelA.getOWLNamedClass("材质类别");
            //nameA="材质类别";


            maxsim = 0f;
            maxsimnameB = null;
            Iterator jt = clslistB.iterator();
            while (jt.hasNext()) {
                nameB = (String) jt.next();
                OWLNamedClass clsB = owlmodelB.getOWLNamedClass(nameB);

                //clsB=owlmodelB.getOWLNamedClass("MATERIALSORT");
                //nameA="MATERIALSORT";


                if (!nameA.contains(".") & !nameB.contains(".")) {
                    float simproperty = simvauleclsprop(clsA, clsB);
                    int EditDistance = getEditDistance(nameA, nameB);
                    String s2 = String.valueOf(EditDistance);
                    float dis = Float.parseFloat(s2);
                    float sim = dis / Float.parseFloat(String.valueOf(maxlength(nameA, nameB)));
                    sim = 1.0f - sim;
                    sim = 0.02f * sim + 0.98f * simproperty;

                    if (sim > maxsim) {
                        maxsim = sim;
                        maxsimnameB = nameB;
                    }
                }
            }

            if (maxsim > 0.1) {
                String simvaule;
                newsimlist.add(nameA);
                newsimlist.add(maxsimnameB);
                if (haspresim(nameA, maxsimnameB)) {
                    simvaule = getpresim(nameA, maxsimnameB);
                } else {
                    simvaule = String.valueOf(maxsim);
                }

                System.out.print(maxsimnameB + "---------------------\n");
                System.out.print(simvaule + "---------------------\n");
                newsimlist.add(simvaule);
            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
    return newsimlist;
}

private float clspropertysim(int numA, int numB) {
    float sim = 0f;
    try {
        String s1 = String.valueOf(numA);
        float numpropertyA = Float.parseFloat(s1);
        String s2 = String.valueOf(numB);
        float numpropertyB = Float.parseFloat(s2);
        float sub = Math.abs(numpropertyA - numpropertyB);
        if (numpropertyA <= 4f && sub <= 1f) {
            sim = Math.abs(numpropertyA - sub) / 4.0f;
        } else if (numpropertyA <= 4f && sub > 1f) {
            if (sub > 3f) {
                sim = 0f;
            } else {
                sim = Math.abs(numpropertyA - sub) / 4.0f;
            }
        } else if (numpropertyA > 4f && sub <= 1f) {
            sim = Math.abs(numpropertyA - 1f) / numpropertyA;
        } else {
            if (sub > 3f) {
                sim = 0f;
            } else {
                sim = Math.abs(numpropertyA - sub) / numpropertyA;
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return sim;
    }
    return sim;
}

public void appendToFile(String str, String filename) throws Exception {

    FileOutputStream stream;//provides file Access
    OutputStreamWriter writer;//writes to the file
    try {
        stream = new FileOutputStream(filename, true);
        writer = new OutputStreamWriter(stream);
        writer.write(str);
        writer.close();
        stream.close();
    } catch (Exception ex) {
        stream = null;
        writer = null;
        ex.printStackTrace();
    }
    stream = null;
    writer = null;

}

private void addinstsim(ArrayList simlist, String instA, String instB, String sim) {
    try {
        if (simlist.contains(instA) && sim != null) {
            int index = simlist.indexOf(instA);
            String inst2 = (String) simlist.get(index + 1);

            float oldsim = Float.parseFloat(simlist.get(index + 2).toString());
            if (inst2.equals(instB) && Float.parseFloat(sim) > oldsim) {
                simlist.set(index + 2, sim);
            }
            if (!inst2.equals(instB) && Float.parseFloat(sim) > oldsim) {
                simlist.set(index + 1, instB);
                simlist.set(index + 2, sim);
            }
        } else if (sim != null) {
            simlist.add(instA);
            simlist.add(instB);
            simlist.add(sim);
        }
    } catch (Exception ex) {

        ex.printStackTrace();
    }

}

private static int maxlength(String name1, String name2) {
    int l = name1.length();
    if (l > name2.length()) {
        return l;
    } else {
        l = name2.length();
        return l;
    }
}

private static int Minimum(int a, int b, int c) {
    int mi;

    mi = a;
    if (b < mi) {
        mi = b;
    }
    if (c < mi) {
        mi = c;
    }
    return mi;
}

public int getEditDistance(String s, String t) {
    try {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1
        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);
            // Step 4
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);
                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost);
            }
        }
        return d[n][m];
    } catch (Exception ex) {
        ex.printStackTrace();
        return -1;
    }
}

public void getlinesim(String line) throws Exception  //获取该行的类、实例、属性名称
{
    try {
        String[] str = line.split(";");
        int index = str[0].lastIndexOf("#");
        linename1 = str[0].substring(index + 1, str[0].length());
        index = str[1].lastIndexOf("#");
        linename2 = str[1].substring(index + 1, str[1].length());
        linesim = str[2];
    } catch (Exception ex) {

        ex.printStackTrace();
    }
}

}