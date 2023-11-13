package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;


class ConnectInfo extends Object {

// Public accessible class data
public String m_driver;

public String m_DSN;

public String m_UID;

public String m_PWD;


/**
 * Default constructor
 */
public ConnectInfo() {
    set("", "", "", "");
}

/**
 * Constructor
 */
public ConnectInfo(String driver, String DSN, String UID, String PWD) {
    set(driver, DSN, UID, PWD);
}

/**
 * Set the class data
 */
public void set(String driver, String DSN, String UID, String PWD) {
    m_driver = driver;
    m_DSN = DSN;
    m_UID = UID;
    m_PWD = PWD;
}

/**
 * Returns the ODBC data source URL
 */
public String toString() {
    return "DSN=" + m_DSN + ";UID=" + m_UID + ";PWD=" + m_PWD;
}

}


