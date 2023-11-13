package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.sql.*;
import java.util.*;


public class DataMaster extends Thread {

public static final String[] TABLE_TYPES_ALL = Global.ALLOWED_TABLE_TYPES.toArray(new String[]{});
public static final String[] TABLE_TYPES_TABLE = new String[]{"TABLE"};
public static final String[] TABLE_TYPES_VIEW = new String[]{"VIEW"};

private KnowledgeBase kb;
private OWLModel owlModel;
private DataMasterConnectionOptions optConn;
private DataMasterImportOptions optImport;
private ConnectInfoPanel connPanel = null;
private String filename = null;
private Connection conn = null;
public static int rowNumber = 0;

private Vector<TreeNodeInfo> tables = new Vector<TreeNodeInfo>();


public DataMaster(KnowledgeBase kb, DataMasterConnectionOptions conOptions) throws DataMasterException {
    this(kb, conOptions, null);
}

public DataMaster(KnowledgeBase kb, DataMasterConnectionOptions conOptions, DataMasterImportOptions impOptions) throws DataMasterException {
    this.kb = kb;

    if (kb instanceof OWLModel) {
        this.owlModel = (OWLModel) kb;
    } else {
        this.owlModel = null;
    }

    setConnectionOptions(conOptions);
    setImportOptions(impOptions);

    Global.init(owlModel);
}

DataMaster(ConnectInfoPanel connPanel, KnowledgeBase kb, DataMasterConnectionOptions conOptions) throws DataMasterException {
    this(kb, conOptions, null);
    this.connPanel = connPanel;
}

public void setConnectionOptions(DataMasterConnectionOptions conOptions) throws DataMasterException {
    if (isConnected()) {
        throw new DataMasterException("Setting connection options for DataMaster is not allowed while an active connection is established to the data source. Disconnect first.");
    }
    if (conOptions == null) {
        throw new DataMasterException("Null value is not allowed as connection options for DataMaster");
    }
    this.optConn = conOptions.clone();
}

public void setImportOptions(DataMasterImportOptions impOptions) {
    this.optImport = (impOptions == null ? null : impOptions.clone());
}

public boolean isConnected() {
    try {
        checkConnection();
        return true;
    } catch (DataMasterException e) {
        return false;
    }
}

public String[] getAllAvailableTableTypes() throws SQLException, DataMasterException {
    checkConnection();

    DatabaseMetaData dbMetaData = conn.getMetaData();


    ResultSet rs = dbMetaData.getTableTypes();
    Vector<String> tableTypesVector = new Vector<String>();
    boolean more = rs.next();
    while (more) {
        String type = rs.getString("TABLE_TYPE");
        if (Global.ALLOWED_TABLE_TYPES.contains(type))
            tableTypesVector.addElement(type);
        more = rs.next();
    }
    rs.close();

    int numTypes = tableTypesVector.size();
    String[] tableTypes = new String[numTypes];
    for (int i = 0; i < numTypes; i++)
        tableTypes[i] = tableTypesVector.get(i);

    return tableTypes;
}


public void disconnect() throws SQLException, DataMasterException {
    checkConnection();

    conn.close();
    conn = null;
}


Connection getConnection() throws DataMasterException {
    checkConnection();

    return conn;
}


Collection<TreeNodeInfo> getTableTreeNodeInfos() {
    return Collections.unmodifiableCollection(tables);
}


void populateTables(String[] tableTypes) throws SQLException {

    DatabaseMetaData dbMetaData = conn.getMetaData();

    tables.removeAllElements();

    ResultSet rsTables = dbMetaData.getTables(null, null, null, tableTypes);
    boolean bMore = rsTables.next();
    while (bMore) {
        String strTableName = rsTables.getString("TABLE_NAME");
        String tableTypeStr = rsTables.getString("TABLE_TYPE");
        int tableType;

        if (tableTypeStr.compareToIgnoreCase("TABLE") == 0)
            tableType = TreeNodeInfo.TABLE;
        else if (tableTypeStr.compareToIgnoreCase("VIEW") == 0)
            tableType = TreeNodeInfo.TABLE_VIEW;
        else
            tableType = TreeNodeInfo.TABLE_OTHER;

        TreeNodeInfo nodeInfo = new TreeNodeInfo(strTableName, tableType);
        tables.add(nodeInfo);

        bMore = rsTables.next();
    }

    rsTables.close();
}


public void connect() throws ClassNotFoundException, DataMasterException, SQLException {
    if (isConnected())
        throw new DataMasterException("DataMaster is already connected. Disconnect first, before trying to make a new connection.");
    conn = null;
    if (optConn.getJDBCDirverClassName() == null) {
        throw new DataMasterException("JDBC driver class name is not specified (null)");
    }
    if (optConn.getJDBCDirverClassName().equals("org.postgresql.Driver")) {
        Class.forName(optConn.getJDBCDirverClassName());
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + optConn.getDataSourceURL(), optConn.getUserName(), optConn.getUserPassword());
    }
                /*if(optConn.getJDBCDirverClassName().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver"))
		{
			Class.forName(optConn.getJDBCDirverClassName());
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName="+optConn.getDataSourceURL(), optConn.getUserName(), optConn.getUserPassword());
			//conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=AdventureWorksDW","sa","lytlyt");

		}*/
    Global.IDENT_QUOTE_STRING = conn.getMetaData().getIdentifierQuoteString();
    Global.IDENT_QUOTE_STRING = (Global.IDENT_QUOTE_STRING == null || Global.IDENT_QUOTE_STRING.equals("") ?
                                         Global.DEFAULT_IDENT_QUOTE_STRING : Global.IDENT_QUOTE_STRING);

    String[] tableTypes = getAllAvailableTableTypes();

    populateTables(tableTypes);
}


private void checkConnection() throws DataMasterException {
    try {
        if (conn == null || conn.isClosed()) {
            throw new DataMasterException("DataMaster is not connected to the database");
        }
    } catch (SQLException ex) {
        throw new DataMasterException("DataMaster is not connected to the database " +
                                              "(SQLException while checking connection status: " + ex.getMessage() + ")");
    }
}


public Collection<String> getTableNamesForTableTypes(final String[] tableTypes) throws DataMasterException {
    checkConnection();

    Collection<Integer> infoTypes = convertTableTypeNamesToNodeInfoTypes(tableTypes);

    Vector<String> tableNames = new Vector<String>();
    for (TreeNodeInfo nodeInfo : tables) {
        if (infoTypes.contains(new Integer(nodeInfo.m_type))) {
            tableNames.add(nodeInfo.m_text);
        }
    }

    return tableNames;
}


protected boolean isPrimaryKey(DatabaseMetaData dbMetaData, String strTableName, String collumName) throws SQLException {
    try {
        ArrayList PrimaryKeysList = new ArrayList();
        ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
        while (rs.next()) {
            PrimaryKeysList.add(rs.getString(4));
        }
        rs.close();
        rs = null;
        if (PrimaryKeysList.contains(collumName) && PrimaryKeysList.size() == 1) {
            return true;
        }

        PrimaryKeysList = null;
        return false;
    } catch (Exception e) {
        return false;
    }
}

public OWLNamedClass CreateSubClsAndInst(OWLNamedClass cls, String FirstVaule, String SecondVaule, String ThirdVaule) throws Exception {
    OWLNamedClass subcls = null;
    try {
        if (!FirstVaule.equals("")) {
            subcls = createSubClasssafe(cls.getName() + "." + FirstVaule, cls);

            if (!SecondVaule.equals("")) {
                subcls = createSubClasssafe(cls.getName() + "." + SecondVaule, subcls);
                if (!ThirdVaule.equals("")) {
                    subcls = createSubClasssafe(cls.getName() + "." + ThirdVaule, subcls);
                }
            }
        }
        return subcls;
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
        return null;
    }
}

public void createSubClsAndIndividualsForTable(Connection con, String strTableName, String Firstkey, String Secondkey, String Thirdkey, int rows) throws SQLException {
    try {
        if (filename == null) {
            filename = JOptionPane.showInputDialog("请输入文件名:\n例如：abc.owl");
        }
        InputStream is = new FileInputStream(filename);
        owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        DatabaseMetaData dbMetaData = con.getMetaData();
        ArrayList PrimaryKeysList = new ArrayList();
        ArrayList ImportedtablesList = new ArrayList();
        ArrayList ImportedKeysList = new ArrayList();
        ArrayList ImportedKeysNList = new ArrayList();

        ArrayList ExportedtablesList = new ArrayList();
        ArrayList ExportedKeysList = new ArrayList();
        ArrayList ExportedKeysNList = new ArrayList();

        System.out.print("\n表名：" + strTableName + "\n");

        if (!strTableName.equals("sysdiagrams")) {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
            while (rs.next()) {
                PrimaryKeysList.add(rs.getString(4));
            }

            rs = dbMetaData.getImportedKeys(null, null, strTableName);
            while (rs.next()) {
                ImportedtablesList.add(rs.getString(3));  //添加外部表名
                ImportedKeysList.add(rs.getString(4));  //添加外部关联键名
                ImportedKeysNList.add(rs.getString(8)); //添加自身关联键名
            }

            rs = dbMetaData.getExportedKeys(null, null, strTableName);

            while (rs.next()) {
                ExportedtablesList.add(rs.getString(7));  //添加外部表名
                ExportedKeysList.add(rs.getString(4));  //添加自身关联键名
                ExportedKeysNList.add(rs.getString(8)); //添加外部关联键名
            }

            String strQuery = "SELECT * FROM " + strTableName;

            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(strQuery);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            final RDFSDatatype xsdDate = owlModel.getXSDdate();
            final RDFSDatatype xsdTime = owlModel.getXSDtime();
            final RDFSDatatype xsdDateTime = owlModel.getXSDdateTime();

            int i2 = 0;
            while (resultSet.next()) {
                System.out.print("行数：" + i2 + "\n");
                OWLIndividual ins;
                String instname = null;
                String primarykeyvaule = "";
                //创建实例
                if (PrimaryKeysList.size() == 1)  //一个主键
                {
                    String primarykey = PrimaryKeysList.get(0).toString();
                    primarykeyvaule = resultSet.getObject(primarykey).toString();
                    instname = cls.getName() + "_" + primarykeyvaule;
                } else if (PrimaryKeysList.size() > 1)  //多个主键
                {
                    String primarykey;

                    for (int j = 0; j < PrimaryKeysList.size(); j++) {
                        primarykey = PrimaryKeysList.get(j).toString();
                        if (j == 0) {
                            primarykeyvaule = resultSet.getObject(primarykey).toString();
                        } else {
                            primarykeyvaule = primarykeyvaule + "_" + resultSet.getObject(primarykey).toString();
                        }
                    }
                    instname = cls.getName() + "_" + primarykeyvaule;
                } else                             //无主键
                {
                    primarykeyvaule = Integer.toString(i2 + 1);
                    instname = cls.getName() + "_" + Integer.toString(i2 + 1);
                }

                OWLIndividual inst = getOWLIndividualSafe(instname);
                if (inst != null) {
                    inst.delete();
                }

                String firstvaule = "", Secondvaule = "", Thirdvaule = "";
                if (!Firstkey.equals("")) {
                    if (resultSet.getObject(Firstkey) != null) {
                        firstvaule = resultSet.getObject(Firstkey).toString();
                    } else {
                        firstvaule = "";
                    }
                }
                if (!Secondkey.equals("")) {
                    if (resultSet.getObject(Secondkey) != null) {
                        Secondvaule = resultSet.getObject(Secondkey).toString();
                    } else {
                        Secondvaule = "";
                    }
                }
                if (!Thirdkey.equals("")) {
                    if (resultSet.getObject(Thirdkey) != null) {
                        Thirdvaule = resultSet.getObject(Thirdkey).toString();
                    } else {
                        Thirdvaule = "";
                    }
                }

                OWLNamedClass subcls = CreateSubClsAndInst(cls, firstvaule, Secondvaule, Thirdvaule);


                instname = cls.getName() + "_" + primarykeyvaule;
                ins = createOWLIndividualSafe(subcls, instname);

                //设置实例各属性值

                for (int j = 0; j < rsmd.getColumnCount(); j++) {
                    String strColumnName = rsmd.getColumnName(j + 1);

                    if (!ImportedKeysNList.contains(strColumnName)) //设置Datatype属性值
                    {
                        Object obj = resultSet.getObject(strColumnName);

                        int sqlType = rsmd.getColumnType(j + 1);
                        String colName = strColumnName;
                        obj = Global.getSQLProtegeOWLObject(sqlType, obj);
                        if (obj != null) {
                            String propName = strTableName + "." + colName;
                            OWLProperty propCol = owlModel.getOWLProperty(propName);
                            if (propCol == null) {
                                Global.debug("WARNING: Property \"" + propName + "\" not found! ");
                            } else {
                                RDFSDatatype rdfType = Global.getSQLProtegeOWLType(owlModel, sqlType);
                                if (rdfType.equals(xsdDate) || rdfType.equals(xsdTime) || rdfType.equals(xsdDateTime)) {
                                    obj = DefaultRDFSLiteral.create(owlModel, (String) obj, rdfType);
                                } else {
                                    obj = DefaultRDFSLiteral.create(owlModel, obj);
                                }
                                ins.setPropertyValue(propCol, obj);

                                //rdfType=null;
                            }
                            //propCol=null;
                            //propName=null;
                        }

                        //obj=null;
                    } else      //设置实例属性值
                    {
                        Object Vaule = resultSet.getObject(strColumnName);
                        if (Vaule != null) {
                            String keyVaule = resultSet.getObject(strColumnName).toString();
                            String propName = "", othertablename = "";
                            String otherColumnName = "";

                            //获取相关表名称	及属性名称
                            if (ImportedKeysNList.size() > 0) {
                                int index = ImportedKeysNList.indexOf(strColumnName);
                                othertablename = ImportedtablesList.get(index).toString();
                                String keyself = ImportedKeysNList.get(index).toString();
                                String key = ImportedKeysList.get(index).toString();
                                otherColumnName = ImportedKeysList.get(index).toString();  //相关表对应列名

                                propName = strTableName + "." + othertablename + "." + strColumnName + "." + key;

                                keyself = null;
                                key = null;
                            }


                            OWLProperty propCol = owlModel.getOWLProperty(propName);


                            OWLNamedClass othercls = owlModel.getOWLNamedClass(othertablename);

                            if (othercls != null && propCol != null) {
                                if (isPrimaryKey(dbMetaData, othertablename, otherColumnName)) {

                                    keyVaule = othertablename + "_" + keyVaule;
                                    OWLIndividual vauleIns = createOWLIndividualSafe(othercls, keyVaule);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                } else {
                                    OWLIndividual vauleIns = createObjectInstance(con, othertablename, keyVaule, otherColumnName);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                }


                            } else {
                                Global.debug("创建实例失败");
                            }

                            //othercls=null;
                            //propCol=null;

                            propName = null;
                            othertablename = null;

                        }
                    }
                }
                i2++;
            }

            stmt.close();
            resultSet.close();
        }

        PrimaryKeysList = null;
        ImportedtablesList = null;
        ImportedKeysList = null;
        ImportedKeysNList = null;

        ExportedtablesList = null;
        ExportedKeysList = null;
        ExportedKeysNList = null;

        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
        Collection errors = new ArrayList();
        jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
        System.out.println("File saved with " + errors.size() + " errors.");


    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
    }
}


public void createSubCls(String strTableName, int rows) {
    try {
        String key1 = null, key2 = null, key3 = null;
        KeyFile keydlg = new KeyFile();
        key1 = keydlg.keyA;
        key2 = keydlg.keyB;
        key3 = keydlg.keyC;
        if (!(key1 == null & key2 == null & key3 == null)) {
            createSubClsAndIndividualsForTable(conn, strTableName, key1, key2, key3, rows);
            String strMsg = "子类创建成功!";
            JOptionPane.showMessageDialog(null, strMsg, strTableName, JOptionPane.INFORMATION_MESSAGE);
        } else {
            String strMsg = "无分类字段信息!";
            JOptionPane.showMessageDialog(null, strMsg, strTableName, JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception e) {
        Global.debug("子类创建错误，第一个键!");
        e.printStackTrace();
    }
}


private OWLNamedClass createSubClasssafe(String SubClassName, OWLNamedClass parentcls) throws SQLException {
    try {
        OWLNamedClass cls = owlModel.getOWLNamedClass(SubClassName);
        if (cls == null) {
            cls = owlModel.createOWLNamedSubclass(SubClassName, parentcls);
        }
        return cls;
    } catch (Exception e) {
        Global.debug("创建子类出错");
        return null;
    }
}

private OWLNamedClass getOWLNamedClasssafe(String className) throws SQLException {
    OWLNamedClass cls = null;
    try {
        cls = owlModel.getOWLNamedClass(className);
    } catch (Exception e) {
        return null;
    }
    return cls;
}

protected OWLIndividual getOWLIndividualSafe(String individualName) {
    OWLIndividual owlIndividual = null;
    try {
        owlIndividual = owlModel.getOWLIndividual(individualName);

    } catch (Exception e) {
        Global.debug("创建实例出错");
        return null;
    }
    return owlIndividual;
}


public String Findbefore(ArrayList ListA, ArrayList ListB, String vaule) {
    String before = null;
    try {
        if (ListA.contains(vaule)) {
            int index = ListA.indexOf(vaule);
            before = ListB.get(index).toString();
        }
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
        return before;
    }
    return before;
}

public OWLNamedClass getSelfSubClsForRow(OWLNamedClass cls, ArrayList ListA, ArrayList ListC, String Secondvaule) throws Exception {
    OWLNamedClass clsA = null;
    try {
        int index1 = ListA.indexOf(Secondvaule);
        if (index1 >= 0) {
            String Vaule = ListC.get(index1).toString();
            String clsname = cls.getName() + "." + Vaule;
            clsA = getOWLNamedClasssafe(clsname);
        } else {
            return cls;
        }
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
        return null;
    }
    return clsA;
}

public void CreateSelfSubClsFortable(OWLNamedClass cls, ArrayList ListA, ArrayList ListB, ArrayList ListC) throws Exception {
    OWLNamedClass subcls = null;
    try {
        cls.getName();
        Iterator ia = ListA.iterator();
        while (ia.hasNext()) {
            ArrayList deslist = new ArrayList();
            String vauleA = ia.next().toString();
            String beforeVaule = Findbefore(ListA, ListB, vauleA);
            String oldvaule = null;
            while (beforeVaule != null & !beforeVaule.equals("") & !deslist.contains(beforeVaule)) {
                oldvaule = beforeVaule;
                deslist.add(beforeVaule);
                beforeVaule = Findbefore(ListA, ListB, beforeVaule);
                if (beforeVaule == null) {
                    beforeVaule = "**";
                }
            }

            if (deslist.size() > 3) {
                String pos = deslist.get(deslist.size() - 3).toString();
                int index = ListA.indexOf(pos);
                String vaule = ListC.get(index).toString();
                createSubClasssafe(cls.getName() + "." + vaule, cls);

                String oldclsname = cls.getName() + "." + vaule;
                for (int i = 0; i < deslist.size() - 3; i++) {
                    if (deslist.size() - 4 - i >= 0) {
                        pos = deslist.get(deslist.size() - 4 - i).toString();
                        index = ListA.indexOf(pos);
                        vaule = ListC.get(index).toString();
                        OWLNamedClass parentcls = getOWLNamedClasssafe(oldclsname);
                        if (parentcls != null) {
                            createSubClasssafe(cls.getName() + "." + vaule, parentcls);
                            oldclsname = cls.getName() + "." + vaule;
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
    }
}


public void createSelfSubClsAndIndividualsForTable(Connection con, String strTableName, String Firstkey, String Secondkey, String Thirdkey, int rows) throws SQLException {
    try {
        if (filename == null) {
            filename = JOptionPane.showInputDialog("请输入文件名:\n例如：abc.owl");
        }
        InputStream is = new FileInputStream(filename);
        owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        DatabaseMetaData dbMetaData = con.getMetaData();
        ArrayList PrimaryKeysList = new ArrayList();
        ArrayList ImportedtablesList = new ArrayList();
        ArrayList ImportedKeysList = new ArrayList();
        ArrayList ImportedKeysNList = new ArrayList();

        ArrayList ExportedtablesList = new ArrayList();
        ArrayList ExportedKeysList = new ArrayList();
        ArrayList ExportedKeysNList = new ArrayList();

        System.out.print("\n表名：" + strTableName + "\n");

        if (!strTableName.equals("sysdiagrams")) {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
            while (rs.next()) {
                PrimaryKeysList.add(rs.getString(4));
            }

            rs = dbMetaData.getImportedKeys(null, null, strTableName);
            while (rs.next()) {
                ImportedtablesList.add(rs.getString(3));  //添加外部表名
                ImportedKeysList.add(rs.getString(4));  //添加外部关联键名
                ImportedKeysNList.add(rs.getString(8)); //添加自身关联键名
            }

            rs = dbMetaData.getExportedKeys(null, null, strTableName);

            while (rs.next()) {
                ExportedtablesList.add(rs.getString(7));  //添加外部表名
                ExportedKeysList.add(rs.getString(4));  //添加自身关联键名
                ExportedKeysNList.add(rs.getString(8)); //添加外部关联键名
            }

            String strQuery = "SELECT * FROM " + strTableName;

            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(strQuery);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            final RDFSDatatype xsdDate = owlModel.getXSDdate();
            final RDFSDatatype xsdTime = owlModel.getXSDtime();
            final RDFSDatatype xsdDateTime = owlModel.getXSDdateTime();

            String Firstvaule = "", Secondvaule = "", Thirdvaule = "";
            ArrayList ListA = new ArrayList();
            ArrayList ListB = new ArrayList();
            ArrayList ListC = new ArrayList();

            while (resultSet.next()) {
                if (!Firstkey.equals("") & !Secondkey.equals("")) {
                    if (resultSet.getObject(Firstkey) != null) {
                        Firstvaule = resultSet.getObject(Firstkey).toString();
                        ListA.add(Firstvaule);
                    } else {
                        ListA.add("");
                    }

                    if (resultSet.getObject(Secondkey) != null) {
                        Secondvaule = resultSet.getObject(Secondkey).toString();
                        ListB.add(Secondvaule);
                    } else {
                        ListB.add("");
                    }
                    if (resultSet.getObject(Thirdkey) != null) {
                        Thirdvaule = resultSet.getObject(Thirdkey).toString();
                        ListC.add(Thirdvaule);
                    } else {
                        ListC.add("");
                    }
                }
            }

            CreateSelfSubClsFortable(cls, ListA, ListB, ListC);


            stmt = con.createStatement();
            resultSet = stmt.executeQuery(strQuery);
            rsmd = resultSet.getMetaData();

            int i2 = 0;
            while (resultSet.next()) {
                System.out.print("行数：" + i2 + "\n");
                OWLIndividual ins;
                String instname = null;
                //创建实例
                if (PrimaryKeysList.size() == 1)  //一个主键
                {
                    String primarykey = PrimaryKeysList.get(0).toString();
                    String primarykeyvaule = resultSet.getObject(primarykey).toString();
                    instname = cls.getName() + "_" + primarykeyvaule;
                }

                OWLIndividual inst = getOWLIndividualSafe(instname);
                if (inst != null) {
                    inst.delete();
                }

                Firstvaule = "";
                Secondvaule = "";
                Thirdvaule = "";
                if (!Firstkey.equals("")) {
                    Firstvaule = resultSet.getObject(Firstkey).toString();
                }
                if (!Secondkey.equals("")) {
                    Secondvaule = resultSet.getObject(Secondkey).toString();
                }
                if (!Thirdkey.equals("")) {
                    Thirdvaule = resultSet.getObject(Thirdkey).toString();
                }

                OWLNamedClass subcls = getSelfSubClsForRow(cls, ListA, ListC, Secondvaule);


                instname = cls.getName() + "_" + Firstvaule;
                ins = createOWLIndividualSafe(subcls, instname);

                //设置实例各属性值

                for (int j = 0; j < rsmd.getColumnCount(); j++) {
                    String strColumnName = rsmd.getColumnName(j + 1);

                    if (!ImportedKeysNList.contains(strColumnName)) //设置Datatype属性值
                    {
                        Object obj = resultSet.getObject(strColumnName);

                        int sqlType = rsmd.getColumnType(j + 1);
                        String colName = strColumnName;
                        obj = Global.getSQLProtegeOWLObject(sqlType, obj);
                        if (obj != null) {
                            String propName = strTableName + "." + colName;
                            OWLProperty propCol = owlModel.getOWLProperty(propName);
                            if (propCol == null) {
                                Global.debug("WARNING: Property \"" + propName + "\" not found! ");
                            } else {
                                RDFSDatatype rdfType = Global.getSQLProtegeOWLType(owlModel, sqlType);
                                if (rdfType.equals(xsdDate) || rdfType.equals(xsdTime) || rdfType.equals(xsdDateTime)) {
                                    obj = DefaultRDFSLiteral.create(owlModel, (String) obj, rdfType);
                                } else {
                                    obj = DefaultRDFSLiteral.create(owlModel, obj);
                                }
                                ins.setPropertyValue(propCol, obj);

                                //rdfType=null;
                            }
                            //propCol=null;
                            //propName=null;
                        }

                        //obj=null;
                    } else      //设置实例属性值
                    {
                        Object Vaule = resultSet.getObject(strColumnName);
                        if (Vaule != null) {
                            String keyVaule = resultSet.getObject(strColumnName).toString();
                            String propName = "", othertablename = "";
                            String otherColumnName = "";

                            //获取相关表名称	及属性名称
                            if (ImportedKeysNList.size() > 0) {
                                int index = ImportedKeysNList.indexOf(strColumnName);
                                othertablename = ImportedtablesList.get(index).toString();
                                String keyself = ImportedKeysNList.get(index).toString();
                                String key = ImportedKeysList.get(index).toString();
                                otherColumnName = ImportedKeysList.get(index).toString();  //相关表对应列名

                                propName = strTableName + "." + othertablename + "." + strColumnName + "." + key;

                                keyself = null;
                                key = null;
                            }


                            OWLProperty propCol = owlModel.getOWLProperty(propName);


                            OWLNamedClass othercls = owlModel.getOWLNamedClass(othertablename);

                            if (othercls != null && propCol != null) {
                                if (isPrimaryKey(dbMetaData, othertablename, otherColumnName)) {

                                    keyVaule = othertablename + "_" + keyVaule;
                                    OWLIndividual vauleIns = createOWLIndividualSafe(othercls, keyVaule);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                } else {
                                    OWLIndividual vauleIns = createObjectInstance(con, othertablename, keyVaule, otherColumnName);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                }


                            } else {
                                Global.debug("创建实例失败");
                            }

                            //othercls=null;
                            //propCol=null;

                            propName = null;
                            othertablename = null;

                        }
                    }
                }
                i2++;
            }

            stmt.close();
            resultSet.close();
        }

        PrimaryKeysList = null;
        ImportedtablesList = null;
        ImportedKeysList = null;
        ImportedKeysNList = null;

        ExportedtablesList = null;
        ExportedKeysList = null;
        ExportedKeysNList = null;

        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
        Collection errors = new ArrayList();
        jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
        System.out.println("File saved with " + errors.size() + " errors.");


    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
    }
}


public void createSelfSubCls(String strTableName, int rows) {
    try {
        String key1 = null, key2 = null, key3 = null;
        KeyFile keydlg = new KeyFile(true);
        key1 = keydlg.keyA;
        key2 = keydlg.keyB;
        key3 = keydlg.keyC;
        if (!(key1 == null & key2 == null & key3 == null)) {
            createSelfSubClsAndIndividualsForTable(conn, strTableName, key1, key2, key3, rows);
            String strMsg = "创建子类成功!";
            JOptionPane.showMessageDialog(null, strMsg, strTableName, JOptionPane.INFORMATION_MESSAGE);

        } else {
            String strMsg = "无分类字段信息!";
            JOptionPane.showMessageDialog(null, strMsg, strTableName, JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception e) {
        Global.debug("子类创建错误，第一个键!");
        e.printStackTrace();
    }
}


protected OWLIndividual createOWLIndividualSafe(OWLNamedClass cls, String individualName) {
    try {
        OWLIndividual owlIndividual = null;

        owlIndividual = owlModel.getOWLIndividual(individualName);


        if (owlIndividual == null) {
            try {
                owlIndividual = cls.createOWLIndividual(individualName);
            } catch (Exception e) {
                Global.debug("创建实例出错");
                return null;
            }
        }
        return owlIndividual;

    } catch (Exception e) {
        Global.debug("创建实例出错");
        return null;
    }
}


protected OWLIndividual createObjectInstance(Connection con, String strTableName, String keyVaule, String collumName) throws SQLException {
    OWLIndividual owlIndividual;
    try {
        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        DatabaseMetaData dbMetaData = con.getMetaData();

        ArrayList PrimaryKeysList = new ArrayList();

        ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
        while (rs.next()) {
            PrimaryKeysList.add(rs.getString(4));
        }

        String strQuery = "SELECT top 2  * FROM " + strTableName + " where " + collumName + " = " + keyVaule + " order by " + collumName + " desc";

        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery(strQuery);

        if (resultSet.next()) {
            //创建实例
            if (PrimaryKeysList.size() == 1)  //一个主键
            {

                String primarykey = PrimaryKeysList.get(0).toString();
                String primarykeyvaule = resultSet.getObject(primarykey).toString();
                primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                owlIndividual = createOWLIndividualSafe(cls, primarykeyvaule);
            } else if (PrimaryKeysList.size() > 1)  //多个主键
            {
                String primarykey;
                String primarykeyvaule = "";
                for (int j = 0; j < PrimaryKeysList.size(); j++) {
                    primarykey = PrimaryKeysList.get(j).toString();
                    if (j == 0) {
                        primarykeyvaule = resultSet.getObject(primarykey).toString();
                    } else {
                        primarykeyvaule = primarykeyvaule + "_" + resultSet.getObject(primarykey).toString();
                    }
                }
                primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                owlIndividual = createOWLIndividualSafe(cls, primarykeyvaule);
            } else                             //无主键
            {
                String keyvaule = resultSet.getObject(collumName).toString();
                keyvaule = cls.getName() + "_" + keyvaule;      //多条记录可能只对应于一个实例
                owlIndividual = createOWLIndividualSafe(cls, keyvaule);
            }
        } else {
            owlIndividual = null;
        }
    } catch (Exception e) {
        return null;
    }

    return owlIndividual;
}


public void createIndividualsForRows(Connection con, String strTableName, int rows) throws SQLException {
    try {
        if (filename == null) {
            filename = JOptionPane.showInputDialog("请输入文件名:\n例如：abc.owl");
            InputStream is = new FileInputStream(filename);
            owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        }

        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        DatabaseMetaData dbMetaData = con.getMetaData();
        ArrayList PrimaryKeysList = new ArrayList();
        ArrayList ImportedtablesList = new ArrayList();
        ArrayList ImportedKeysList = new ArrayList();
        ArrayList ImportedKeysNList = new ArrayList();

        ArrayList ExportedtablesList = new ArrayList();
        ArrayList ExportedKeysList = new ArrayList();
        ArrayList ExportedKeysNList = new ArrayList();

        System.out.print("\n表名：" + strTableName + "\n");

        if (!strTableName.equals("sysdiagrams")) {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
            while (rs.next()) {
                PrimaryKeysList.add(rs.getString(4));
            }

            rs = dbMetaData.getImportedKeys(null, null, strTableName);
            while (rs.next()) {
                ImportedtablesList.add(rs.getString(3));  //添加外部表名
                ImportedKeysList.add(rs.getString(4));  //添加外部关联键名
                ImportedKeysNList.add(rs.getString(8)); //添加自身关联键名
            }

            rs = dbMetaData.getExportedKeys(null, null, strTableName);

            while (rs.next()) {
                ExportedtablesList.add(rs.getString(7));  //添加外部表名
                ExportedKeysList.add(rs.getString(4));  //添加自身关联键名
                ExportedKeysNList.add(rs.getString(8)); //添加外部关联键名
            }

            String strQuery = "SELECT * FROM " + strTableName;

            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(strQuery);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            final RDFSDatatype xsdDate = owlModel.getXSDdate();
            final RDFSDatatype xsdTime = owlModel.getXSDtime();
            final RDFSDatatype xsdDateTime = owlModel.getXSDdateTime();

            int i2 = 0;
            while (resultSet.next() && i2 < rows) {
                System.out.print("行数：" + i2 + "\n");
                OWLIndividual ins;
                //创建实例
                if (PrimaryKeysList.size() == 1)  //一个主键
                {
                    String primarykey = PrimaryKeysList.get(0).toString();
                    String primarykeyvaule = resultSet.getObject(primarykey).toString();
                    primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                    ins = createOWLIndividualSafe(cls, primarykeyvaule);
                } else if (PrimaryKeysList.size() > 1)  //多个主键
                {
                    String primarykey;
                    String primarykeyvaule = "";
                    for (int j = 0; j < PrimaryKeysList.size(); j++) {
                        primarykey = PrimaryKeysList.get(j).toString();
                        if (j == 0) {
                            primarykeyvaule = resultSet.getObject(primarykey).toString();
                        } else {
                            primarykeyvaule = primarykeyvaule + "_" + resultSet.getObject(primarykey).toString();
                        }
                    }
                    primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                    ins = createOWLIndividualSafe(cls, primarykeyvaule);
                } else                             //无主键
                {
                    String strInsName = cls.getName() + "_" + Integer.toString(i2 + 1);

                    ins = createOWLIndividualSafe(cls, strInsName);
                }

                //设置实例各属性值

                for (int j = 0; j < rsmd.getColumnCount(); j++) {
                    String strColumnName = rsmd.getColumnName(j + 1);

                    if (!ImportedKeysNList.contains(strColumnName)) //设置Datatype属性值
                    {
                        Object obj = resultSet.getObject(strColumnName);

                        int sqlType = rsmd.getColumnType(j + 1);
                        String colName = strColumnName;
                        obj = Global.getSQLProtegeOWLObject(sqlType, obj);
                        if (obj != null) {
                            String propName = strTableName + "." + colName;
                            OWLProperty propCol = owlModel.getOWLProperty(propName);
                            if (propCol == null) {
                                Global.debug("WARNING: Property \"" + propName + "\" not found! ");
                            } else {
                                RDFSDatatype rdfType = Global.getSQLProtegeOWLType(owlModel, sqlType);
                                if (rdfType.equals(xsdDate) || rdfType.equals(xsdTime) || rdfType.equals(xsdDateTime)) {
                                    obj = DefaultRDFSLiteral.create(owlModel, (String) obj, rdfType);
                                } else {
                                    obj = DefaultRDFSLiteral.create(owlModel, obj);
                                }
                                ins.setPropertyValue(propCol, obj);

                                //rdfType=null;
                            }
                            //propCol=null;
                            //propName=null;
                        }

                        //obj=null;
                    } else      //设置实例属性值
                    {
                        Object Vaule = resultSet.getObject(strColumnName);
                        if (Vaule != null) {
                            String keyVaule = resultSet.getObject(strColumnName).toString();
                            String propName = "", othertablename = "";
                            String otherColumnName = "";

                            //获取相关表名称	及属性名称
                            if (ImportedKeysNList.size() > 0) {
                                int index = ImportedKeysNList.indexOf(strColumnName);
                                othertablename = ImportedtablesList.get(index).toString();
                                String keyself = ImportedKeysNList.get(index).toString();
                                String key = ImportedKeysList.get(index).toString();
                                otherColumnName = ImportedKeysList.get(index).toString();  //相关表对应列名

                                propName = strTableName + "." + othertablename + "." + strColumnName + "." + key;

                                keyself = null;
                                key = null;
                            }


                            OWLProperty propCol = owlModel.getOWLProperty(propName);


                            OWLNamedClass othercls = owlModel.getOWLNamedClass(othertablename);

                            if (othercls != null && propCol != null) {
                                if (isPrimaryKey(dbMetaData, othertablename, otherColumnName)) {

                                    keyVaule = othertablename + "_" + keyVaule;
                                    OWLIndividual vauleIns = createOWLIndividualSafe(othercls, keyVaule);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                } else {
                                    OWLIndividual vauleIns = createObjectInstance(con, othertablename, keyVaule, otherColumnName);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                }


                            } else {
                                Global.debug("创建实例失败");
                            }

                            //othercls=null;
                            //propCol=null;

                            propName = null;
                            othertablename = null;

                        }
                    }
                }
                i2++;
            }

            stmt.close();
            resultSet.close();
        }

        PrimaryKeysList = null;
        ImportedtablesList = null;
        ImportedKeysList = null;
        ImportedKeysNList = null;

        ExportedtablesList = null;
        ExportedKeysList = null;
        ExportedKeysNList = null;


    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
    }
}


public void createIndividualsForRows(Connection con, String strTableName) throws SQLException {
    try {
        if (filename == null) {
            filename = JOptionPane.showInputDialog("请输入文件名:\n例如：abc.owl");
            InputStream is = new FileInputStream(filename);
            owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        }


        //strTableName="FactResellerSales";
        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        DatabaseMetaData dbMetaData = con.getMetaData();
        ArrayList PrimaryKeysList = new ArrayList();
        ArrayList ImportedtablesList = new ArrayList();
        ArrayList ImportedKeysList = new ArrayList();
        ArrayList ImportedKeysNList = new ArrayList();

        ArrayList ExportedtablesList = new ArrayList();
        ArrayList ExportedKeysList = new ArrayList();
        ArrayList ExportedKeysNList = new ArrayList();

        System.out.print("\n表名：" + strTableName + "\n");

        if (!strTableName.equals("sysdiagrams")) {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
            while (rs.next()) {
                PrimaryKeysList.add(rs.getString(4));
            }

            rs = dbMetaData.getImportedKeys(null, null, strTableName);
            while (rs.next()) {
                ImportedtablesList.add(rs.getString(3));  //添加外部表名
                ImportedKeysList.add(rs.getString(4));  //添加外部关联键名
                ImportedKeysNList.add(rs.getString(8)); //添加自身关联键名
            }

            rs = dbMetaData.getExportedKeys(null, null, strTableName);

            while (rs.next()) {
                ExportedtablesList.add(rs.getString(7));  //添加外部表名
                ExportedKeysList.add(rs.getString(4));  //添加自身关联键名
                ExportedKeysNList.add(rs.getString(8)); //添加外部关联键名
            }

            String strQuery = "SELECT * FROM " + strTableName;

            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(strQuery);
            ResultSetMetaData rsmd = resultSet.getMetaData();

            final RDFSDatatype xsdDate = owlModel.getXSDdate();
            final RDFSDatatype xsdTime = owlModel.getXSDtime();
            final RDFSDatatype xsdDateTime = owlModel.getXSDdateTime();

            int i2 = 0;
            while (resultSet.next()) {
                System.out.print("行数：" + i2 + "\n");
                OWLIndividual ins;
                //创建实例
                if (PrimaryKeysList.size() == 1)  //一个主键
                {
                    String primarykey = PrimaryKeysList.get(0).toString();
                    String primarykeyvaule = resultSet.getObject(primarykey).toString();
                    primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                    ins = createOWLIndividualSafe(cls, primarykeyvaule);
                } else if (PrimaryKeysList.size() > 1)  //多个主键
                {
                    String primarykey;
                    String primarykeyvaule = "";
                    for (int j = 0; j < PrimaryKeysList.size(); j++) {
                        primarykey = PrimaryKeysList.get(j).toString();
                        if (j == 0) {
                            primarykeyvaule = resultSet.getObject(primarykey).toString();
                        } else {
                            primarykeyvaule = primarykeyvaule + "_" + resultSet.getObject(primarykey).toString();
                        }
                    }
                    primarykeyvaule = cls.getName() + "_" + primarykeyvaule;
                    ins = createOWLIndividualSafe(cls, primarykeyvaule);
                } else                             //无主键
                {
                    String strInsName = cls.getName() + "_" + Integer.toString(i2 + 1);

                    ins = createOWLIndividualSafe(cls, strInsName);
                }

                //设置实例各属性值

                for (int j = 0; j < rsmd.getColumnCount(); j++) {
                    String strColumnName = rsmd.getColumnName(j + 1);

                    if (!ImportedKeysNList.contains(strColumnName)) //设置Datatype属性值
                    {
                        Object obj = resultSet.getObject(strColumnName);

                        int sqlType = rsmd.getColumnType(j + 1);
                        String colName = strColumnName;
                        obj = Global.getSQLProtegeOWLObject(sqlType, obj);
                        if (obj != null) {
                            String propName = strTableName + "." + colName;
                            OWLProperty propCol = owlModel.getOWLProperty(propName);
                            if (propCol == null) {
                                Global.debug("WARNING: Property \"" + propName + "\" not found! ");
                            } else {
                                RDFSDatatype rdfType = Global.getSQLProtegeOWLType(owlModel, sqlType);
                                if (rdfType.equals(xsdDate) || rdfType.equals(xsdTime) || rdfType.equals(xsdDateTime)) {
                                    obj = DefaultRDFSLiteral.create(owlModel, (String) obj, rdfType);
                                } else {
                                    obj = DefaultRDFSLiteral.create(owlModel, obj);
                                }
                                ins.setPropertyValue(propCol, obj);

                                //rdfType=null;
                            }
                            //propCol=null;
                            //propName=null;
                        }

                        //obj=null;
                    } else      //设置实例属性值
                    {
                        Object Vaule = resultSet.getObject(strColumnName);
                        if (Vaule != null) {
                            String keyVaule = resultSet.getObject(strColumnName).toString();
                            String propName = "", othertablename = "";
                            String otherColumnName = "";

                            //获取相关表名称	及属性名称
                            if (ImportedKeysNList.size() > 0) {
                                int index = ImportedKeysNList.indexOf(strColumnName);
                                othertablename = ImportedtablesList.get(index).toString();
                                String keyself = ImportedKeysNList.get(index).toString();
                                String key = ImportedKeysList.get(index).toString();
                                otherColumnName = ImportedKeysList.get(index).toString();  //相关表对应列名

                                propName = strTableName + "." + othertablename + "." + strColumnName + "." + key;

                                keyself = null;
                                key = null;
                            }


                            OWLProperty propCol = owlModel.getOWLProperty(propName);


                            OWLNamedClass othercls = owlModel.getOWLNamedClass(othertablename);

                            if (othercls != null && propCol != null) {
                                if (isPrimaryKey(dbMetaData, othertablename, otherColumnName)) {
                                    keyVaule = othertablename + "_" + keyVaule;
                                    OWLIndividual vauleIns = createOWLIndividualSafe(othercls, keyVaule);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                } else {
                                    OWLIndividual vauleIns = createObjectInstance(con, othertablename, keyVaule, otherColumnName);
                                    if (vauleIns != null) {
                                        ins.setPropertyValue(propCol, vauleIns);
                                        vauleIns = null;
                                    } else {
                                        Global.debug("创建实例失败");
                                    }
                                }


                            } else {
                                Global.debug("创建实例失败");
                            }

                            //othercls=null;
                            //propCol=null;

                            propName = null;
                            othertablename = null;

                        }
                    }
                }
                i2++;
            }

            stmt.close();
            resultSet.close();
        }

        PrimaryKeysList = null;
        ImportedtablesList = null;
        ImportedKeysList = null;
        ImportedKeysNList = null;

        ExportedtablesList = null;
        ExportedKeysList = null;
        ExportedKeysNList = null;

        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
        Collection errors = new ArrayList();
        jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
        System.out.println("File saved with " + errors.size() + " errors.");
        jenaowlModel.dispose();
        owlModel.dispose();
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
    }
}

public void CreatSelfSubClsForTable(Collection<String> tableNames, int rows) throws DataMasterException {
    checkConnection();

    Collection<TreeNodeInfo> nodeInfos = getTreeNodeInfosForTableNames(tableNames);

    if (tableNames == null)
        throw new DataMasterException("invalid argument: tableNames is null");

    if (nodeInfos.size() != 0) {
        for (TreeNodeInfo nodeInfo : nodeInfos) {
            String strTableName = nodeInfo.m_text;

            try {

                DatabaseMetaData dbMetaData = conn.getMetaData();

                setKeyInfo(dbMetaData, strTableName, nodeInfo);


                int i2 = 0;
                // Create class instances from the rows of data in the table

                createSelfSubCls(strTableName, rows);  //创建子类
                dbMetaData = null;

            } catch (SQLException ex) {
                Global.debug("导入" + strTableName + "内容出错\n");
                throw new DataMasterException(ex);
            }
            strTableName = null;
        }
    }
    nodeInfos = null;
}

public void CreatSubClsForTable(Collection<String> tableNames, int rows) throws DataMasterException {
    checkConnection();

    Collection<TreeNodeInfo> nodeInfos = getTreeNodeInfosForTableNames(tableNames);

    if (tableNames == null)
        throw new DataMasterException("invalid argument: tableNames is null");

    if (nodeInfos.size() != 0) {
        for (TreeNodeInfo nodeInfo : nodeInfos) {
            String strTableName = nodeInfo.m_text;

            try {

                DatabaseMetaData dbMetaData = conn.getMetaData();

                setKeyInfo(dbMetaData, strTableName, nodeInfo);


                int i2 = 0;
                // Create class instances from the rows of data in the table

                createSubCls(strTableName, rows);  //创建子类
                dbMetaData = null;

            } catch (SQLException ex) {
                Global.debug("导入" + strTableName + "内容出错\n");
                throw new DataMasterException(ex);
            }
            strTableName = null;
        }
    }
    nodeInfos = null;
}

public OWLNamedClass GetSubClssafe(OWLNamedClass cls, String FirstVaule, String SecondVaule, String ThirdVaule) throws Exception {
    OWLNamedClass subcls = null;
    try {
        if (!FirstVaule.equals("")) {
            subcls = createSubClasssafe(cls.getName() + "." + FirstVaule, cls);

            if (!SecondVaule.equals("")) {
                subcls = createSubClasssafe(cls.getName() + "." + SecondVaule, subcls);
                if (!ThirdVaule.equals("")) {
                    subcls = createSubClasssafe(cls.getName() + "." + ThirdVaule, subcls);
                }
            }
        }
        return subcls;
    } catch (Exception e) {
        Global.debug("导入数据出错!");
        e.printStackTrace();
        return null;
    }
}

public OWLIndividual copyinst(OWLNamedClass cls, OWLIndividual sourceinst, OWLIndividual destinst) {
    try {
        Collection P1 = cls.getAssociatedProperties();
        Iterator i1 = P1.iterator();
        while (i1.hasNext()) {
            OWLProperty nameP1 = (OWLProperty) i1.next();
            Object vaule = sourceinst.getPropertyValue(nameP1);
            if (vaule != null & nameP1 != null) {
                if (!nameP1.isObjectProperty()) {
                    destinst.setPropertyValue(nameP1, vaule);
                } else {
                    OWLIndividual inst = owlModel.getOWLIndividual(vaule.toString());
                    destinst.setPropertyValue(nameP1, inst);
                }
            }
        }
        return destinst;
    } catch (Exception e) {
        Global.debug("实例复制出错!");
        e.printStackTrace();
        return null;
    }
}


private Collection<Integer> convertTableTypeNamesToNodeInfoTypes(final String[] tableTypes) throws DataMasterException {
    Set<Integer> result = new HashSet<Integer>();
    for (int i = 0; i < tableTypes.length; i++) {
        String tableTypeStr = tableTypes[i];

        int tableType;
        if (tableTypeStr.toUpperCase().contains("SYSTEM"))
            tableType = TreeNodeInfo.TABLE_SYSTEM;
        else if (tableTypeStr.compareToIgnoreCase("TABLE") == 0)
            tableType = TreeNodeInfo.TABLE;
        else if (tableTypeStr.compareToIgnoreCase("VIEW") == 0)
            tableType = TreeNodeInfo.TABLE_VIEW;
        else if (tableTypeStr.toUpperCase().contains("OTHER"))
            tableType = TreeNodeInfo.TABLE_OTHER;
        else
            throw new DataMasterException("Invalid table type name: " + tableTypeStr);

        result.add(new Integer(tableType));
    }
    return result;
}

public void importTablesToProtegeOWL(Collection<String> tableNames) throws DataMasterException {
    checkConnection();

    Collection<TreeNodeInfo> nodeInfos = getTreeNodeInfosForTableNames(tableNames);

    if (tableNames == null)
        throw new DataMasterException("invalid argument: tableNames is null");

    if (nodeInfos.size() != 0) {
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();

            //If import in separate ontology create a new OWL ontology and make it the default ontology
            if (optImport.importInSepOntology()) {


                filename = JOptionPane.showInputDialog("请输入文件名\n例如abc.owl");
                //String namespace = JOptionPane.showInputDialog("请输入Ontology URI:\n例如：http://www.abc.com/Ontologyabc.owl#" );
                String namespace = "http://www." + optConn.getDataSourceURL() + ".com/" + filename + "#";
                owlModel = ProtegeOWL.createJenaOWLModel();
                owlModel.getNamespaceManager().setDefaultNamespace(namespace);
            }


            DBImporter dbImporter = null;


            dbImporter = new DBImporterForTableClasses();
            DBImporterForTableClasses.conn = this.conn;

            dbImporter.init(owlModel, optConn, optImport);


            for (TreeNodeInfo nodeInfo : nodeInfos)  //创建类
            {
                String strTableName = nodeInfo.m_text;

                try {
                    setKeyInfo(dbMetaData, strTableName, nodeInfo);
                    // Create the Protege class
                    Cls cls = dbImporter.CreateClsForTable(dbMetaData, strTableName, nodeInfo);

                } catch (Exception ex) {
                    throw new DataMasterException(ex);
                }
                strTableName = null;

            }

            for (TreeNodeInfo nodeInfo : nodeInfos)   //创建属性
            {
                String strTableName = nodeInfo.m_text;

                try {
                    setKeyInfo(dbMetaData, strTableName, nodeInfo);
                    // Create the Protege class
                    dbImporter.CreatePropertyForTable(dbMetaData, strTableName, nodeInfo);

                } catch (Exception ex) {
                    throw new DataMasterException(ex);
                }
                strTableName = null;

            }

            //dbImporter.refactorSchema(dbMetaData);
            //dbImporter.refactorData();

            dbMetaData = null;
            dbImporter = null;
        } catch (SQLException ex) {
            throw new DataMasterException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (optImport.importInSepOntology()) {
            try {
                JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
                Collection errors = new ArrayList();
                jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
                System.out.println("File saved with " + errors.size() + " errors.");
                owlModel.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } else {
        if (nodeInfos.size() == 0) {
            String msg = "Please specify (valid) tables names to import.\nThere was no table to import.";
            Global.debug(msg);
        } else {
            String msg = "At least one of the import option \"import tables as classes\" or \"import tables as instance\" must be selected.";
            throw new DataMasterException(msg);
        }
    }
    nodeInfos = null;
}

public boolean savefile() {
    try {
        if (optImport.importInSepOntology()) {

            return true;
        } else {
            JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
            Collection errors = new ArrayList();
            jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
            System.out.println("File saved with " + errors.size() + " errors.");
            owlModel.dispose();
            return true;
        }
    } catch (Exception e) {
        return false;
    }
}

public void importTablesContent(Collection<String> tableNames, int rowNumber) throws DataMasterException {
    checkConnection();

    Collection<TreeNodeInfo> nodeInfos = getTreeNodeInfosForTableNames(tableNames);

    if (tableNames == null)
        throw new DataMasterException("invalid argument: tableNames is null");


    if (nodeInfos.size() != 0) {
        try {
            InputStream is = new FileInputStream(filename);
            owlModel = ProtegeOWL.createJenaOWLModelFromInputStream(is);
        } catch (Exception e) {
            Global.debug("导入数据出错!");
            e.printStackTrace();
        }
        for (TreeNodeInfo nodeInfo : nodeInfos) {
            String strTableName = nodeInfo.m_text;

            try {

                DatabaseMetaData dbMetaData = conn.getMetaData();

                setKeyInfo(dbMetaData, strTableName, nodeInfo);

                int i2 = 0;
                // Create class instances from the rows of data in the table
                if (owlModel != null) {
                    createIndividualsForRows(conn, strTableName, rowNumber);
                }

                dbMetaData = null;

            } catch (SQLException ex) {
                Global.debug("导入" + strTableName + "内容出错\n");
                throw new DataMasterException(ex);
            }
            strTableName = null;
        }

        JenaOWLModel jenaowlModel = (JenaOWLModel) owlModel;
        Collection errors = new ArrayList();
        jenaowlModel.save(new File(filename).toURI(), FileUtils.langXMLAbbrev, errors);
        System.out.println("File saved with " + errors.size() + " errors.");
        owlModel.dispose();
    }
    nodeInfos = null;
}


public void importTablesContent(Collection<String> tableNames) throws DataMasterException {
    checkConnection();

    Collection<TreeNodeInfo> nodeInfos = getTreeNodeInfosForTableNames(tableNames);

    if (tableNames == null)
        throw new DataMasterException("invalid argument: tableNames is null");

    if (nodeInfos.size() != 0) {
        for (TreeNodeInfo nodeInfo : nodeInfos) {
            String strTableName = nodeInfo.m_text;

            try {

                DatabaseMetaData dbMetaData = conn.getMetaData();

                setKeyInfo(dbMetaData, strTableName, nodeInfo);


                int i2 = 0;
                // Create class instances from the rows of data in the table

                createIndividualsForRows(conn, strTableName);
                dbMetaData = null;

            } catch (SQLException ex) {
                Global.debug("导入" + strTableName + "内容出错\n");
                throw new DataMasterException(ex);
            }
            strTableName = null;
        }
    }
    nodeInfos = null;
}


private Collection<TreeNodeInfo> getTreeNodeInfosForTableNames(final Collection<String> tableNames) {
    Set<TreeNodeInfo> result = new HashSet<TreeNodeInfo>();

    for (TreeNodeInfo nodeInfo : tables) {
        if (tableNames.contains(nodeInfo.m_text)) {
            result.add(nodeInfo);
        }
    }

    return Collections.unmodifiableCollection(result);
}


private void selectProjectFileName(Project project, boolean showProject) {
    JComponent _rootPane = ProjectManager.getProjectManager().getMainPanel();
    KnowledgeBaseFactory factory = project.getKnowledgeBaseFactory();

    PropertyList sources = project.getSources();
    URI projectURI = project.getProjectURI();
    String s = projectURI == null ? null : projectURI.toString();
    KnowledgeBaseSourcesEditor editor = factory.createKnowledgeBaseSourcesEditor(s, sources);
    editor.setShowProject(showProject);
    String title = "Save the ontology of the imported database schema as ";// + factory.getDescription() + ")";
    int result = ModalDialog.showDialog(_rootPane, editor, title, ModalDialog.MODE_OK_CANCEL);
    if (result == ModalDialog.OPTION_OK) {
        project.setProjectURI(URIUtilities.createURI(editor.getProjectPath()));
    } else {
        //do nothing. Use the generated default names name
    }
    sources.setString(KnowledgeBaseFactory.FACTORY_CLASS_NAME, factory.getClass().getName());
}


static private void setKeyInfo(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo tableNodeInfo) {
    if (!tableNodeInfo.m_infoComplete) {

        tableNodeInfo.m_infoComplete = true;
    }
}


static private ArrayList<String> getPrimaryKeys(DatabaseMetaData dbMetaData, String strTableName) {
    ArrayList<String> primaryKeys = new ArrayList<String>();

    ResultSet rs;
    try {
        rs = dbMetaData.getPrimaryKeys(null, null, strTableName);

        boolean isPKDefined = rs.isBeforeFirst();
        if (isPKDefined) {
            Vector<String> colNamesInPK = new Vector<String>();

            while (rs.next()) {
                int keySeq = rs.getShort("KEY_SEQ");
                if (keySeq >= colNamesInPK.size()) {
                    for (int i = colNamesInPK.size(); i <= keySeq; i++)
                        colNamesInPK.add(null);
                }
                colNamesInPK.setElementAt(rs.getString("COLUMN_NAME"), keySeq);
                Global.debug(colNamesInPK.get(keySeq) + " " + keySeq);
            }

            //treat special case if we had it
            if (colNamesInPK.get(0) == null) {
                //normal case
                colNamesInPK.remove(0);
            } else {
                Global.debug("PRIMARY KEY COLUMN WITH KEY_SEQ = 0 WAS SPECIFIED. COLUMN = " + colNamesInPK.get(0));
            }

            primaryKeys = new ArrayList<String>(colNamesInPK);
        }
			/*
			more = rs.next();
			while (more) {
				primaryKeys.add(rs.getString("COLUMN_NAME"));
				more = rs.next();
			}*/
        rs.close();
    } catch (SQLException sqlex) {
        Global.debug("QUERYING A TABLE FOR PRIMARY KEYS IS NOT SUPPORTED BY THIS DRIVER (" + sqlex.getMessage() + ")");
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return primaryKeys;
}

}
