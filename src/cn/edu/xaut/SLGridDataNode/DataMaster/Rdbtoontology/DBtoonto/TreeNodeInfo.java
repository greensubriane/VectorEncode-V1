package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import java.util.ArrayList;

class TreeNodeInfo {

public String m_text;
public int m_type;
public int m_colSQLType;

public boolean m_infoComplete = true;
public boolean m_columnsCreated = true;

public ArrayList<String> primaryKeyFields = new ArrayList<String>();
public ArrayList<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();


public static final int TABLE_SYSTEM = 1;
public static final int TABLE = 2;
public static final int TABLE_VIEW = 3;
public static final int TABLE_OTHER = 10;

public static final int COLUMN = 100;
public static final int COLUMN_PK = 101;
public static final int COLUMN_FK = 102;

public static final int OTHER = -1;


public TreeNodeInfo(String text, int type) {
    m_text = text;
    m_type = type;
}


public TreeNodeInfo(String text, int type, int sqlType) {
    m_text = text;
    m_type = type;
    m_colSQLType = sqlType;
}


public String toString() {
    return m_text;
}
}
