package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface DBImporter {

public void init(KnowledgeBase kb, DataMasterConnectionOptions conOptions, DataMasterImportOptions impOptions);

public int importTablecontent(Connection con, String strTableName, Cls cls) throws SQLException;

public String getResourceNameForTable(String tableName);

public Cls CreateClsForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException;

public void CreatePropertyForTable(DatabaseMetaData dbMetaData, String strTableName, TreeNodeInfo nodeInfo) throws SQLException;


}
