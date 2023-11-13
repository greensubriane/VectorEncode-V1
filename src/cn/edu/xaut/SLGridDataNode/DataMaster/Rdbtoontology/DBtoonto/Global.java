package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


/**
 * The Global class contains common services used by other objects
 *
 * @author csnyulas
 */
class Global extends Object {

// Information about the tab and the current version

static public final String tabName = "DataMaster";

static public final String tabVersion = "v1.3.1";

// Separator between the Driver list and DSN list in the configuration file
static public final String SEPARATOR_DRIVER_DSN = "------------******------------";


static public final String NAMESPACE_TABLE_CLASSES = "http://biostorm.stanford.edu/db_table_classes";
static public final String NAMESPACE_TABLE_INSTANCES = "http://biostorm.stanford.edu/db_table_instances";
static public final String NAMESPACE_TABLE_CLASSES_AND_INSTANCES = "http://biostorm.stanford.edu/db_table_classes_and_instances";
static public final String NAMESPACE_BIND_DSN = "/DSN_";
static public final String NAMESPACE_RELATIONAL_OWL = "http://www.dbs.cs.uni-duesseldorf.de/RDF/relational.owl#";

public static enum URL_Conversion {CreateSingleShema, EliminateAllColons}

;

public static final int COLUMN_TYPE_INSTANCE = 1;
public static final int COLUMN_TYPE_XSD = 2;
public static final int COLUMN_TYPE_RANGE = 3;

public static RDFSLiteral[] ALL_XSD_TYPES = null;

public static final String DEFAULT_IDENT_QUOTE_STRING = "\"";
public static String IDENT_QUOTE_STRING = DEFAULT_IDENT_QUOTE_STRING;

public static final int VERTICAL_INSET = 10;
public static final int HORIZ_INSET = 20;

public static final int PREVIEW_DEFAULT_ROW_COUNT = 50;

public static final int COMBO_HISTORY_LIMIT = 10;

public static final List<String> ALLOWED_TABLE_TYPES = Arrays.asList(new String[]{"TABLE", "VIEW"});

private static final SimpleDateFormat DBDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
private static final SimpleDateFormat DBTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
private static final SimpleDateFormat DBDateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

// return a Protege equivalent value type of a SQL data type.
// This method also safeguards against triggering the new strong
// type checking in the current version 1.6 build 787 of Protege

static public ValueType getSQLProtegeType(int sqlType) {
    switch (sqlType) {
        case Types.BIT:
            return ValueType.BOOLEAN;
        // return ValueType.CLS;
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.REAL:
        case Types.NUMERIC:
        case Types.DECIMAL:
            return ValueType.FLOAT;
        // return ValueType.INSTANCE;
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return ValueType.INTEGER;
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            return ValueType.STRING;
        // return ValueType.SYMBOL;
        case Types.ARRAY:
        case Types.BINARY:
        case Types.BLOB:
        case Types.CLOB:
            // Types.DISTINT:
        case Types.JAVA_OBJECT:
        case Types.LONGVARBINARY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.STRUCT:
        case Types.VARBINARY:
        default:
            return ValueType.ANY;
    }
}

// Similar to the above method, but converts a SQL object to
// a Protege object.

// See "8. Mapping SQL data types into Java, Table 2: Standard mapping
// from SQL types to Java types" (D:\Program Files\jdk1.3\docs\guide\jdbc\spec\jdbc-spec.frame8.html)
// for the standard mapping from SQL tyes to Java types.

static public Object getSQLProtegeObject(int sqlType, Object obj) {
    if (obj == null)
        return obj;

    switch (sqlType) {
        case Types.BIT:
            return (Boolean) obj;

        case Types.FLOAT:
            return new Float(((Double) obj).floatValue());
        case Types.DOUBLE:
            // because Protege only has type FLOAT for floating point number
            // and not a type DOUBLE for double (Double.class), we need to convert to Float
            // will get an exception otherwise
            return new Float(((Double) obj).floatValue());
        case Types.REAL:
            return (Float) obj;
        case Types.NUMERIC:
            return new Float(((BigDecimal) obj).floatValue());
        case Types.DECIMAL:
            return new Float(((BigDecimal) obj).floatValue());

        case Types.TINYINT:
            if (obj instanceof Byte) {
                return new Integer(((Byte) obj).intValue());
            } else if (obj instanceof Short) {
                return new Integer(((Short) obj).intValue());
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Integer(((Number) obj).intValue());
            }
        case Types.SMALLINT:
            if (obj instanceof Short) {
                return new Integer(((Short) obj).intValue());
            } else if (obj instanceof Integer) {
                return (Integer) obj;
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Integer(((Number) obj).intValue());
            }
        case Types.INTEGER:
            if (obj instanceof Integer) {
                return (Integer) obj;
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Integer(((Number) obj).intValue());
            }
        case Types.BIGINT:
            //return new Integer(((BigInteger)obj).intValue());
            return new Integer(((Long) obj).intValue());

        case Types.CHAR:
            return (String) obj;
        case Types.VARCHAR:
            return (String) obj;
        case Types.LONGVARCHAR:
            return (String) obj;
        case Types.DATE:
            return obj.toString();
        case Types.TIME:
            return obj.toString();
        case Types.TIMESTAMP:
            return obj.toString();

        case Types.ARRAY:
        case Types.BINARY:
        case Types.BLOB:
        case Types.CLOB:
            // Types.DISTINT:
        case Types.JAVA_OBJECT:
        case Types.LONGVARBINARY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.STRUCT:
        case Types.VARBINARY:
        default:
            return obj;
    }
}

/**
 * Return a Protege-OWL equivalent value type of a SQL data type.
 */
static public RDFSDatatype getSQLProtegeOWLType(OWLModel owlModel, int sqlType) {
    switch (sqlType) {
        case Types.BIT:
            return owlModel.getXSDboolean();
        case Types.DOUBLE:
            return owlModel.getXSDdouble();
        case Types.REAL:
        case Types.FLOAT:
            return owlModel.getXSDfloat();
        case Types.NUMERIC:
        case Types.DECIMAL:
            return owlModel.getXSDdecimal();
        case Types.TINYINT:            //??????
            //return owlModel.getXSDunsignedByte();
            return owlModel.getXSDbyte();
        case Types.SMALLINT:
            return owlModel.getXSDshort();
        case Types.INTEGER:
            return owlModel.getXSDint();
        case Types.BIGINT:
            return owlModel.getXSDlong();
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:        //??????
            return owlModel.getXSDstring();
        case Types.DATE:
            return owlModel.getXSDdate();
        case Types.TIME:
            return owlModel.getXSDtime();
        case Types.TIMESTAMP:
            return owlModel.getXSDdateTime();
        case Types.BINARY:
        case Types.BLOB:
            //return owlModel.getXSDhexBinary();
            return owlModel.getXSDbase64Binary();
        case Types.CLOB:            //??????
            // Types.DISTINT:
        case Types.JAVA_OBJECT:        //??????
            return owlModel.getXSDstring();
        case Types.LONGVARBINARY:
        case Types.VARBINARY:
            return owlModel.getXSDbase64Binary();
        case Types.ARRAY:            //??????
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.STRUCT:
        default:
            return owlModel.getXSDanyURI();
    }
}

// Similar to the above method, but converts a SQL object to
// a Protege OWL object.

// See "8. Mapping SQL data types into Java, Table 2: Standard mapping
// from SQL types to Java types" (D:\Program Files\jdk1.3\docs\guide\jdbc\spec\jdbc-spec.frame8.html)
// for the standard mapping from SQL tyes to Java types.

static public Object getSQLProtegeOWLObject(int sqlType, Object obj) {
    if (obj == null)
        return obj;

    switch (sqlType) {
        case Types.BIT:
            return (Boolean) obj;

        case Types.FLOAT:
            return new Float(((Double) obj).floatValue());
        case Types.DOUBLE:
            // because Protege only has type FLOAT for floating point number
            // and not a type DOUBLE for double (Double.class), we need to convert to Float
            // will get an exception otherwise
            //return new Float(((Double)obj).floatValue());
            return (Double) obj;
        case Types.REAL:
            return (Float) obj;
        case Types.NUMERIC:        //TODO check the next lines again
            return new Float(((BigDecimal) obj).floatValue());
        case Types.DECIMAL:
            return new Float(((BigDecimal) obj).floatValue());

        case Types.TINYINT:
            if (obj instanceof Byte) {
                return (Byte) obj;
            } else if (obj instanceof Short) {
                return new Byte(((Short) obj).byteValue());
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Byte(((Number) obj).byteValue());
            }
        case Types.SMALLINT:
            if (obj instanceof Short) {
                return (Short) obj;
            } else if (obj instanceof Integer) {
                return new Short(((Integer) obj).shortValue());
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Short(((Number) obj).shortValue());
            }
        case Types.INTEGER:
            if (obj instanceof Integer) {
                return (Integer) obj;
            } else {
                //this case should never occur, but in practice sometimes can happen because
                //of inaccurate type mapping in the JDBCDriver
                return new Integer(((Number) obj).intValue());
            }
        case Types.BIGINT:
            //return new Integer(((BigInteger)obj).intValue());
            //return new Integer(((Long)obj).intValue());
            return (Long) obj;

        case Types.CHAR:
            return (String) obj;
        case Types.VARCHAR:
            return (String) obj;
        case Types.LONGVARCHAR:
            return (String) obj;

        case Types.DATE:
            String dt = ((Date) obj).toString();
            try {
                return XMLSchemaDatatypes.getDateString(DBDateFormat.parse(dt));
            } catch (ParseException e) {
                Log.getLogger().info(e.toString());
                //default value is now
                return XMLSchemaDatatypes.getDateString(new java.util.Date());
            }
        case Types.TIME:
            String tm = ((Time) obj).toString();
            try {
                return XMLSchemaDatatypes.getTimeString(DBTimeFormat.parse(tm));
            } catch (ParseException e) {
                Log.getLogger().info(e.toString());
                //default value is now
                return XMLSchemaDatatypes.getTimeString(new java.util.Date());
            }
        case Types.TIMESTAMP:
            String ts = ((Timestamp) obj).toString();
            try {
                return XMLSchemaDatatypes.getDateTimeString(DBDateTimeFormat.parse(ts));
            } catch (ParseException e) {
                Log.getLogger().info(e.toString());
                //default value is now
                return XMLSchemaDatatypes.getDateTimeString(new java.util.Date());
            }

        case Types.ARRAY:
        case Types.BINARY:
        case Types.BLOB:
        case Types.CLOB:
            // Types.DISTINT:
        case Types.JAVA_OBJECT:
        case Types.LONGVARBINARY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.STRUCT:
        case Types.VARBINARY:
        default:
            return obj;
    }
}

// The default handler for DataMaster exceptions in this project

static public void defaultDataMasterExceptionHandler(java.awt.Frame parent, DataMasterException ex) {
    // A DataMasterException was generated.  Catch it and display the error
    // information.  Note that there could be multiple error objects
    // chained together
    debug("*** DataMasterException caught ***");

    String exceptionString = "DataMasterException: " + ex.getMessage();
    if (ex.getCause() instanceof SQLException) {
        SQLException cex = (SQLException) ex.getCause();
        while (ex != null) {
            String sqlExceptionString = exceptionString + "\nSQLState: " + cex.getSQLState()
                                                + "\nMessage:  " + cex.getMessage()
                                                + "\nVendor:   " + cex.getErrorCode();
            debug(exceptionString);
            JOptionPane.showMessageDialog(parent, sqlExceptionString, "DataMaster Exception", JOptionPane.ERROR_MESSAGE);
            cex = cex.getNextException();
        }
    } else {
        debug(exceptionString);
        JOptionPane.showMessageDialog(parent, exceptionString, "DataMaster Exception", JOptionPane.ERROR_MESSAGE);
    }
}

// The default handler for SQL exceptions in this project

static public void defaultSQLExceptionHandler(java.awt.Frame parent, SQLException ex) {
    // A SQLException was generated.  Catch it and display the error
    // information.  Note that there could be multiple error objects
    // chained together
    debug("*** SQLException caught ***");

    while (ex != null) {
        String exceptionString = "SQLState: " + ex.getSQLState()
                                         + "\nMessage:  " + ex.getMessage()
                                         + "\nVendor:   " + ex.getErrorCode();
        debug(exceptionString);
        JOptionPane.showMessageDialog(parent, exceptionString, "SQL Exception", JOptionPane.ERROR_MESSAGE);
        ex = ex.getNextException();
    }
}

// The default handler for ClassNotFound exceptions in this project

static public void defaultClassNotFoundExceptionHandler(java.awt.Frame parent, ClassNotFoundException ex) {
    // A SQLException was generated.  Catch it and display the error
    // information.  Note that there could be multiple error objects
    // chained together
    debug("*** ClassNotFoundException caught ***");

    String exceptionString = "Message:  " + ex.getMessage();
    debug(exceptionString);
    JOptionPane.showMessageDialog(parent, exceptionString, "Class Not Found Exception", JOptionPane.ERROR_MESSAGE);
}

// Provide a structured output mechanism

static public void debug(String debugMsg) {
    //System.out.println("\n" + Global.tabName + "> " + debugMsg + "\n");
    Log.getLogger().info(Global.tabName + "> " + debugMsg);
}

public static void init(OWLModel owlModel) {
    if (owlModel == null) {
        return;
    }
    ALL_XSD_TYPES = new RDFSLiteral[]{
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDanyURI().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDbase64Binary().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDboolean().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDbyte().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDdate().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDdateTime().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDdecimal().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDdouble().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDfloat().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDint().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDlong().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDshort().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDstring().getName()),
            DefaultRDFSLiteral.create(owlModel, owlModel.getXSDtime().getName())};
    //*/
}


// Different DBMS handle table names in queries differently.
// For instance, MySQL does not allow characters such as spaces,
// $, #, ', and other in table names while they are valid in DBMS such as
// MS Access and Excel.  Therefore, this method is intended to modify the
// table names accordingly to work with these different DBMS.
public static String getDBMSSpecificNamingStyle(String tableName) {
    if (tableName.indexOf(' ') != -1
                || tableName.indexOf('.') != -1            //at least if (dbMetaData.getDatabaseProductName().toUpperCase().contains("XLSQL"))
                || tableName.indexOf('$') != -1
                || tableName.indexOf('#') != -1
                || tableName.indexOf('\'') != -1)
        return IDENT_QUOTE_STRING + tableName + IDENT_QUOTE_STRING;
    else
        return tableName;
}

public static String replaceInvalidProtegeCharacters(String str) {
    String new_str = str;
    new_str = new_str.replaceAll("\\$", "_-DOLL-_");
    new_str = new_str.replaceAll(":", "_-COLON-_");
    new_str = new_str.replaceAll("/", "_-SLASH-_");
    new_str = new_str.replaceAll("-", "_-HYPH-_");
    new_str = new_str.replaceAll("#", "_-NUM_SGN-_");
    new_str = new_str.replaceAll("&", "_-AMP-_");
    new_str = new_str.replaceAll("%", "_-PERC-_");
    new_str = new_str.replaceAll("@", "_-AT-_");
    new_str = new_str.replaceAll(" ", "_-SPACE-_");
    //other replacements may be done
    return new_str;
}

public static String convertJdbcUrlToOwlNamespaceUrl(String dsURL, URL_Conversion convRule) {
    String new_str = dsURL;

    //general replaces
    new_str = new_str.replaceAll(" ", "_");
    new_str = new_str.replaceAll("\\\\", "/");

    //eliminate all ":"-s except the schema separator
    int pos_first_slash = new_str.indexOf("/");
    //special treatment for Oracle URLs
    int tmp_idx = new_str.indexOf("@");
    if (tmp_idx > 0 && (pos_first_slash < 0 || tmp_idx < pos_first_slash)) pos_first_slash = tmp_idx;

    int pos_schema_sep = -1;
    if (pos_first_slash > 0) {
        //find the last ":" before the first slash that does not follow a (one character) drive name
        pos_schema_sep = new_str.substring(0, pos_first_slash).lastIndexOf(":");
        if (pos_schema_sep > 1 && new_str.charAt(pos_schema_sep - 2) == ':') {
            pos_schema_sep -= 2;
        }
    } else {
        pos_schema_sep = new_str.lastIndexOf(":");
    }

    //replace all ":" with "-", but revert the one which should be used as schema separator (in case it is desired)
    new_str = new_str.replaceAll(":", ".");
    if (pos_schema_sep >= 0 && convRule == URL_Conversion.CreateSingleShema) {
        new_str = new_str.substring(0, pos_schema_sep) + ":" + new_str.substring(pos_schema_sep + 1);
    }

    return new_str;
}
}
