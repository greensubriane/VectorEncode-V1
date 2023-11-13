package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class DBImporterForTableClasses extends AbstractDBImporter {
private Cls metaClass;
public static Connection conn = null;


public void init(KnowledgeBase kb, DataMasterConnectionOptions conOptions, DataMasterImportOptions impOptions) {
    super.init(kb, conOptions, impOptions);
    if (owlModel == null) {
        nsPrefix = "";
    } else {
        nsPrefix = getPrefixForNamespace_TableClasses();
    }
}

private void prepareKB() {
    // Create the metaclass for all tables
    this.metaClass = this.kb.getCls("Table Metaclass");
    Collection<Cls> tempholder;
    if (this.metaClass == null) {
        tempholder = Collections.singleton(this.kb.getCls(":STANDARD-CLASS"));
        this.metaClass = this.kb.createCls("Table Metaclass", tempholder);
    }

    // Create the class Foreign Key
    tempholder = Collections.singleton(this.kb.getCls(":SYSTEM-CLASS"));
    Cls fkClass = this.kb.getCls("Foreign Key");
    if (fkClass == null) {
        fkClass = this.kb.createCls("Foreign Key", tempholder);

        // Add a slot for name
        Slot fkname = this.kb.getSlot("FK Name");
        if (fkname == null) {
            fkname = this.kb.createSlot("FK Name");
            fkname.setValueType(ValueType.STRING);
            fkname.setMaximumCardinality(1);
        }
        fkClass.addDirectTemplateSlot(fkname);

        // Add a slot for local field
        Slot local = this.kb.getSlot("Local Field");
        if (local == null) {
            local = this.kb.createSlot("Local Field");
            local.setValueType(ValueType.STRING);
            local.setMaximumCardinality(1);
        }
        fkClass.addDirectTemplateSlot(local);

        // Add a slot for reference field
        Slot reference = this.kb.getSlot("Reference Field");
        if (reference == null) {
            reference = this.kb.createSlot("Reference Field");
            reference.setValueType(ValueType.STRING);
            reference.setMaximumCardinality(1);
        }
        fkClass.addDirectTemplateSlot(reference);

        // Add a slot for reference table
        Slot refTable = this.kb.getSlot("Reference Table");
        if (refTable == null) {
            refTable = this.kb.createSlot("Reference Table");
            refTable.setValueType(ValueType.STRING);
            refTable.setMaximumCardinality(1);
        }
        fkClass.addDirectTemplateSlot(refTable);

        // Add primary key field(s) to the metaclass
        Slot pks = this.kb.getSlot("Primary Key Fields");
        if (pks == null) {
            pks = this.kb.createSlot("Primary Key Fields");
            pks.setValueType(ValueType.STRING);
            pks.setAllowsMultipleValues(true);
        }
        metaClass.addDirectTemplateSlot(pks);

        // Add foreign key relationships to the metaclass
        Slot fks = this.kb.getSlot("Foreign Keys");
        if (fks == null) {
            fks = this.kb.createSlot("Foreign Keys");
            fks.setValueType(ValueType.INSTANCE);
            Collection<Cls> classholder = Collections.singleton(this.kb.getCls("Foreign Key"));
            fks.setAllowedClses(classholder);
            fks.setAllowsMultipleValues(true);
        }
        metaClass.addDirectTemplateSlot(fks);

        // Add a checkbox to the metaclass to denote whether this might be a bridge table
        Slot bridge = this.kb.getSlot("Is Bridge Table");
        if (bridge == null) {
            bridge = this.kb.createSlot("Is Bridge Table");
            bridge.setValueType(ValueType.BOOLEAN);
        }
        metaClass.addDirectTemplateSlot(bridge);
    }
}


private void createOWLClassPropertyForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException {
    try {
        //strTableName="FactResellerSales";
        ArrayList PrimaryKeysList = new ArrayList();

        ArrayList ImportedtablesList = new ArrayList();
        ArrayList ImportedKeysList = new ArrayList();
        ArrayList ImportedKeysNList = new ArrayList();

        ArrayList ExportedtablesList = new ArrayList();
        ArrayList ExportedKeysList = new ArrayList();
        ArrayList ExportedKeysNList = new ArrayList();


        if (!strTableName.equals("sysdiagrams")) {
            String beforePrimarykey = "";
            ResultSet rs = dbMetaData.getPrimaryKeys(null, null, strTableName);
            while (rs.next()) {
                if (!rs.getString(6).equals(beforePrimarykey)) {
                    PrimaryKeysList.add(rs.getString(6));
                }
                beforePrimarykey = rs.getString(6);
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
        }
        OWLNamedClass cls = owlModel.getOWLNamedClass(strTableName);

        //创建属性
        ResultSet rsColumns = dbMetaData.getColumns(null, null, strTableName, null);
        boolean bMore = rsColumns.next();
        while (bMore) {
            String strColumnName = rsColumns.getString("COLUMN_NAME");

            if (!ImportedKeysNList.contains(strColumnName)) {
                String strPropertyColumnName = strTableName + "." + strColumnName;

                int sqlType = rsColumns.getShort("DATA_TYPE");
                //ValueType protegeType = Global.getSQLProtegeType(sqlType);
                RDFSDatatype owlType = Global.getSQLProtegeOWLType(owlModel, sqlType);
                //set visible true for uncommon rdfs datatypes, like xsd:short, xsd:byte, etc.
                owlType.setVisible(true);

                OWLDatatypeProperty slot = owlModel.createOWLDatatypeProperty(strPropertyColumnName, owlType);
                slot.setDomain(cls);
                slot.setFunctional(true);

                slot = null;
                owlType = null;
                strPropertyColumnName = null;
            } else {    //获取相关表名称
                if (ImportedKeysNList.size() > 0) {
                    int index = ImportedKeysNList.indexOf(strColumnName);
                    String othertablename = ImportedtablesList.get(index).toString();
                    String keyself = ImportedKeysNList.get(index).toString();
                    String key = ImportedKeysList.get(index).toString();

                    if (strColumnName.equals(keyself))   //是该Key则添加Object属性
                    {
                        OWLObjectProperty slot = owlModel.createOWLObjectProperty(strTableName + "." + othertablename + "." + strColumnName + "." + key);
                        RDFSClass rangecls = (RDFSClass) owlModel.getOWLNamedClass(othertablename);

                        slot.setDomain(cls);
                        slot.setRange(rangecls);
                        slot.setFunctional(true);

                        rangecls = null;
                        slot = null;

                    }

                    othertablename = null;
                    keyself = null;
                    key = null;
                }
            }

            strColumnName = null;
            bMore = rsColumns.next();
        }

        rsColumns.close();

        PrimaryKeysList = null;
        ImportedtablesList = null;
        ImportedKeysList = null;
        ImportedKeysNList = null;

        ExportedtablesList = null;
        ExportedKeysList = null;
        ExportedKeysNList = null;

        cls = null;
    } catch (Exception e) {
        Global.debug("创建属性错误！");
    }
}

// Create the Protege class and slots for each table
private OWLClass createOWLClassForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException {
    Global.debug("Importing table '" + strTableName + "' to class '" + strTableName + "'.");

    OWLClass cls;
    try {
        cls = owlModel.createOWLNamedClass(strTableName);
    } catch (Exception e) {
        Global.debug("Exception: class " + strTableName + " already exists");
    } finally {
        cls = owlModel.getOWLNamedClass(strTableName);
        boolean hasOnlyDefaultSuperclass = cls.getSuperclassCount() == 1 && cls.getNamedSuperclasses().contains(owlModel.getOWLThingClass());
        for (Cls superCls : superClses) {
            cls.addSuperclass((RDFSClass) superCls);
            if (hasOnlyDefaultSuperclass) {
                if (cls.getSuperclassCount() > 1) {
                    cls.removeSuperclass(owlModel.getOWLThingClass());
                }
                hasOnlyDefaultSuperclass = false;
            }
        }
    }

    return cls;
}

// Create class instances from the rows of data in the table
private int createInstancesForRows(Connection con, String strTableName, Cls cls) throws SQLException {
    String strColumnNamePrefix = getColumnNamePrefix(strTableName);
    strTableName = Global.getDBMSSpecificNamingStyle(strTableName);

    String strQuery = "SELECT * FROM " + strTableName;

    Statement stmt = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(strQuery);
    ResultSetMetaData rsmd = resultSet.getMetaData();

    int i2 = 0;
    while (resultSet.next()) {
        String strInsName = cls.getName() + "_Instance_" + Integer.toString(i2 + 1);
        Instance ins = createInstanceSafe(cls, strInsName);

        for (int j = 0; j < rsmd.getColumnCount(); j++) {
            Object obj = resultSet.getObject(j + 1);
            int sqlType = rsmd.getColumnType(j + 1);
            String colName = strColumnNamePrefix + rsmd.getColumnName(j + 1);
            obj = Global.getSQLProtegeObject(sqlType, obj);
            if (obj != null)
                ins.setOwnSlotValue(this.kb.getSlot(colName), obj);
        }

        i2++;
    }

    resultSet.close();
    stmt.close();

    return i2;
}

private void refactorImportedSchemaProtegeFrame() {
    //TODO This method is to be implemented only if we intend to change the original
    //Protege frames import, by adding additional slots to the "Foreign Keys"
    //metaclass (similar to what we implemented in the Proteg OWL import)
}


private void refactorImportedDataProtegeFrame() {
    Iterator clsesIter = this.kb.getClses().iterator();
    Slot pkSlot = this.kb.getSlot("Primary Key Fields");
    Slot fkSlot = this.kb.getSlot("Foreign Keys");
    Slot bridgeSlot = this.kb.getSlot("Is Bridge Table");
    Slot fkLocalFieldSlot = this.kb.getSlot("Local Field");
    Slot fkReferenceField = this.kb.getSlot("Reference Field");
    Slot fkReferenceTable = this.kb.getSlot("Reference Table");

    while (clsesIter.hasNext()) {
        try {
            Cls cls = (Cls) clsesIter.next();
            if (cls.getDirectType().getName().equals("Table Metaclass")) {
                if (((Boolean) cls.getOwnSlotValue(bridgeSlot)).booleanValue()) // It is a bridge table (many-to-many)
                {
                    // Look at the foreign keys to determine the bridge structure
                    Collection<Instance> fks = cls.getOwnSlotValues(fkSlot);  // These will be instances of Foreign Key
                    Iterator<Instance> fkIter = fks.iterator();
                    Instance fk1 = fkIter.next();
                    Instance fk2 = fkIter.next();
                    String tableName1 = (String) fk1.getOwnSlotValue(fkReferenceTable);  // One side of the bridge...
                    String tableName2 = (String) fk2.getOwnSlotValue(fkReferenceTable);    // ...and the other
                    String localField1 = (String) fk1.getOwnSlotValue(fkLocalFieldSlot);
                    String localField2 = (String) fk2.getOwnSlotValue(fkLocalFieldSlot);
                    String referenceField1 = (String) fk1.getOwnSlotValue(fkReferenceField);
                    String referenceField2 = (String) fk2.getOwnSlotValue(fkReferenceField);

                    assert tableName1 != null : "refference table name not set for " + fk1;
                    assert tableName2 != null : "refference table name not set for " + fk2;
                    assert localField1 != null : "Local field value not set for " + fk1;
                    assert localField2 != null : "Local field value not set for " + fk2;

                    Cls tableCls1 = this.kb.getCls(tableName1);
                    Cls tableCls2 = this.kb.getCls(tableName2);
                    if (tableCls1 == null || tableCls2 == null) {
                        continue;
                    }
                    // Create new slots in each of the bridged tables, and make them inverses of each other
                    Slot newSlot1 = generateSlot(tableCls1, cls.getName() + " " + tableName2 + " Instances", tableCls2, true);
                    Slot newSlot2 = generateSlot(tableCls2, cls.getName() + " " + tableName1 + " Instances", tableCls1, true);
                    newSlot1.setInverseSlot(newSlot2);

                    // Iterate through the instances of this class, and assign values to the slots that were just created
                    Collection instances = cls.getDirectInstances();
                    Collection<Instance> referenceInstances1 = tableCls1.getDirectInstances();
                    Collection<Instance> referenceInstances2 = tableCls2.getDirectInstances();
                    Iterator instIter = instances.iterator();
                    while (instIter.hasNext()) {
                        Instance localInstance = (Instance) instIter.next();
                        ////int idToMatch1 = ((Integer)localInstance.getOwnSlotValue(this.kb.getSlot(localField1))).intValue();  // value to match in table1
                        ////int idToMatch2 = ((Integer)localInstance.getOwnSlotValue(this.kb.getSlot(localField2))).intValue();  // value to match in table2
                        Object idToMatch1 = localInstance.getOwnSlotValue(this.kb.getSlot(localField1));  // value to match in table1
                        Object idToMatch2 = localInstance.getOwnSlotValue(this.kb.getSlot(localField2));  // value to match in table2

                        Instance referenceInstance1 = getMatchingInstance(referenceInstances1, referenceField1, idToMatch1);
                        Instance referenceInstance2 = getMatchingInstance(referenceInstances2, referenceField2, idToMatch2);
                        if (referenceInstance1 == null) {
                            Global.debug("ERROR: Could not find matching instance for " + referenceField1 + " = " + idToMatch1);
                        } else if (referenceInstance2 == null) {
                            Global.debug("ERROR: Could not find matching instance for " + referenceField2 + " = " + idToMatch2);
                        } else {
                            referenceInstance1.setOwnSlotValue(newSlot1, referenceInstance2);
                        }
                    }

                } else // Treat foreign keys as a many-to-one relationship
                {
                    // Process the list of foreign keys, making the appropriate substitutions
                    Collection<String> pks = cls.getOwnSlotValues(pkSlot);  // These will be Strings
                    Collection<Instance> fks = cls.getOwnSlotValues(fkSlot);  // These will be instances of Foreign Key
                    Collection instances = cls.getDirectInstances();

                    // Iterate through the foreign keys, checking to see if they are part of the primary key
                    Iterator<Instance> iter = fks.iterator();
                    while (iter.hasNext()) {
                        Instance fkInst = iter.next();
                        String localField = (String) fkInst.getOwnSlotValue(fkLocalFieldSlot);
                        String referenceField = (String) fkInst.getOwnSlotValue(fkReferenceField);
                        String referenceTable = (String) fkInst.getOwnSlotValue(fkReferenceTable);

                        // If it is not part of the PK, create a new slot to hold instances of the foreign table
                        if (!contains(pks, localField)) {
                            Cls refTableCls = this.kb.getCls(referenceTable);
                            if (refTableCls != null) {
                                Slot newSlot = generateSlot(cls, localField + "_INSTANCE", refTableCls, false);

                                // Iterate through the instances of this class, and for each one, fill in the new slot with
                                // instances of the foreign table.
                                Iterator localInstanceIterator = instances.iterator();
                                Collection<Instance> referenceInstances = this.kb.getCls(referenceTable).getDirectInstances();
                                while (localInstanceIterator.hasNext()) {
                                    Instance localInstance = (Instance) localInstanceIterator.next();
                                    ////int idToMatch = ((Integer)localInstance.getOwnSlotValue(this.kb.getSlot(localField))).intValue();
                                    Object idToMatch = localInstance.getOwnSlotValue(this.kb.getSlot(localField));
                                    Instance referenceInstance = getMatchingInstance(referenceInstances, referenceField, idToMatch);
                                    if (referenceInstance == null)
                                        Global.debug("ERROR: Could not find matching instance for " + referenceField + " = " + idToMatch);
                                    else
                                        localInstance.setOwnSlotValue(newSlot, referenceInstance);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


// Create owl class instances from the rows of data in the table
private int createIndividualsForRows(Connection con, String strTableName, OWLClass cls) throws SQLException {
    String strColumnNamePrefix = getColumnNamePrefix(strTableName);
    strTableName = Global.getDBMSSpecificNamingStyle(strTableName);

    String strQuery = "SELECT * FROM " + strTableName;

    Statement stmt = con.createStatement();
    ResultSet resultSet = stmt.executeQuery(strQuery);
    ResultSetMetaData rsmd = resultSet.getMetaData();

    final RDFSDatatype xsdDate = owlModel.getXSDdate();
    final RDFSDatatype xsdTime = owlModel.getXSDtime();
    final RDFSDatatype xsdDateTime = owlModel.getXSDdateTime();

    int i2 = 0;
    while (resultSet.next()) {
        String strInsName = cls.getName() + "_Instance_" + Integer.toString(i2 + 1);
        //Instance ins = createInstanceSafe(strInsName, cls);
        RDFResource ins = createOWLIndividualSafe(cls, strInsName);

        for (int j = 0; j < rsmd.getColumnCount(); j++) {
            Object obj = resultSet.getObject(j + 1);
            int sqlType = rsmd.getColumnType(j + 1);
            String colName = rsmd.getColumnName(j + 1);
            obj = Global.getSQLProtegeOWLObject(sqlType, obj);
            if (obj != null) {
                //ins.setOwnSlotValue(this.kb.getSlot(colName), obj);
                String propName = nsPrefix + strColumnNamePrefix + colName;
                RDFProperty propCol = owlModel.getOWLProperty(propName);
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
                }
            }
        }

        i2++;
    }
    resultSet.close();
    stmt.close();

    return i2;
}


public void CreatePropertyForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException {
    createOWLClassPropertyForTable(dbMetaData, strTableName, nodeInfo);
}

public int importTablecontent(Connection con, String strTableName, Cls cls) throws SQLException {
    if (owlModel == null) {
        return createInstancesForRows(con, strTableName, cls);
    } else {
        assert cls instanceof OWLClass : "Table class " + cls + " must be an OWLClass";
        return createIndividualsForRows(con, strTableName, (OWLClass) cls);
    }
}

public Cls CreateClsForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException {
    return createOWLClassForTable(dbMetaData, strTableName, nodeInfo);
}

}
