package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * A DataMasterConnectionOptions instance contains all the
 * connection information needed by {@link DataMaster}
 * to connect to a JDBC data source and all the
 * data source information  necessary a complete import.
 *
 * @author Csongor Nyulas
 * @see DataMaster
 */
public class DataMasterConnectionOptions implements Cloneable {

private String jdbcDriverClassName;
private String dsURL;
private String schemaName;
private String userName;
private String userPWD;


protected DataMasterConnectionOptions clone() {
    try {
        return (DataMasterConnectionOptions) super.clone();
    } catch (CloneNotSupportedException e) {
        e.printStackTrace();
        return null;
    }
}


/**
 * Convenience method visible to other classes in this package,
 * to create a DataMasterConnectionOptions instance populated
 * with values provided by a {@link ConnectInfoPanel}.
 *
 * @param importPanel a ConnectInfoPanel
 */
// Do not make this method public or private
DataMasterConnectionOptions(ConnectInfoPanel importPanel) {
    this.jdbcDriverClassName = importPanel.getDriverName();
    this.dsURL = importPanel.getDataSourceURL();
    this.schemaName = importPanel.getSchemaName(dsURL);
    this.userName = importPanel.getUserName();
    this.userPWD = importPanel.getUserPWD();
}


/**
 * Constructor for basic connection options with no user name
 * and password specified.
 *
 * @param jdbcDriverClassName the name of a JDBC Driver class
 * @param dsURL               a valid JDBC URL to the data source that can be solved
 *                            by the <code>jdbcDriverClassName</code> class or one
 *                            of the previously loaded JDBC driver classes.
 * @param schemaName          the schema name referred by the data source URL
 */
public DataMasterConnectionOptions(String jdbcDriverClassName, String dsURL, String schemaName) {
    this.jdbcDriverClassName = jdbcDriverClassName;
    this.dsURL = dsURL;
    this.schemaName = schemaName;

    if (dsURL.toUpperCase().indexOf(schemaName.toUpperCase()) < 0) {
        Global.debug("WARNING: attempt to create an invalid DataMasterConnectionOptions! Schema name is not part of the dsURL!");
    }
}


/**
 * Constructor for complete connection options instance.
 *
 * @param jdbcDriverClassName the name of a JDBC Driver class
 * @param dsURL               a valid JDBC URL to the data source that can be solved
 *                            by the <code>jdbcDriverClassName</code> class or one
 *                            of the previously loaded JDBC driver classes.
 * @param schemaName          the schema name referred by the data source URL
 * @param userName            valid user name to connect to the data source
 * @param password            a valid password belonging to the <code>userName</code>
 *                            to access the data source
 */
public DataMasterConnectionOptions(String jdbcDriverClassName, String dsURL, String schemaName,
                                   String userName, String password) {
    this(jdbcDriverClassName, dsURL, schemaName);
    this.userName = userName;
    this.userPWD = password;
}


/**
 * Sets the JDBC driver class name.
 *
 * @param jdbcDriverClassName the JDBC driver class name to set
 */
public void setJDBCDirverClassName(String jdbcDriverClassName) {
    this.jdbcDriverClassName = jdbcDriverClassName;
}

/**
 * Gets the JDBC driver class name.
 *
 * @return the JDBC Driver class name
 */
public String getJDBCDirverClassName() {
    return jdbcDriverClassName;
}


/**
 * Sets the JDBC URL referring to the data source. The datasource URL
 * must conform to the syntax expected the by the JDBC driver.
 * <p>
 * IMPORTANT NOTE! In order to avoid inconsistencies in the imported instances it is
 * really important that besides calling this method to also call the
 * {@link #setSchemaName(String)} method with the same schema name
 * as the one refered by the argument of this method
 *
 * @param dsURL the data source URL to set
 * @see #setJDBCDirverClassName(String)
 * @see #setSchemaName(String)
 */
public void setDataSourceURL(String dsURL) {
    this.dsURL = dsURL;
}

/**
 * Gets the data source URL.
 *
 * @return the data source URL
 */
public String getDataSourceURL() {
    return dsURL;
}


/**
 * Sets the database schema name option.
 * <p>
 * IMPORTANT NOTE! In order to avoid inconsistencies in the imported instances it is
 * really important that the schema name specified as the argument of this method
 * is the same schema name as the one refered in the data source URL.
 *
 * @param schemaName the schema name to set
 * @see #setDataSourceURL(String)
 */
public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
}

/**
 * Gets the database schema name.
 *
 * @return the schema name
 */
public String getSchemaName() {
    return schemaName;
}


/**
 * Sets the user name.
 *
 * @param userName the user name to set
 */
public void setUserName(String userName) {
    this.userName = userName;
}

/**
 * Gets the user name.
 *
 * @return the user name
 */
public String getUserName() {
    return userName;
}


/**
 * Sets the user password.
 *
 * @param userPWD the user password to set
 */
public void setUserPassword(String userPWD) {
    this.userPWD = userPWD;
}

/**
 * Gets the user password.
 *
 * @return the user password
 */
public String getUserPassword() {
    return userPWD;
}


public String toString() {
    return "DataMasterConnectionOptions(" + "JDBC Driver = " + printString(jdbcDriverClassName) + "; " +
                   "data source URL = " + printString(dsURL) + "; " +
                   "schema name = " + printString(schemaName) + "; " +
                   "user name = " + printString(userName) + "; " +
                   "user password = " + printString(userPWD) + ")";
}

private String printString(String s) {
    return (s == null ? "null" : "'" + s + "'");
}
}
