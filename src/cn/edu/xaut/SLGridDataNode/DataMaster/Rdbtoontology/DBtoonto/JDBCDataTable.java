package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.ValueType;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.Vector;

/**
 * The JDBCDataTable class is a Table model that provides data to a JTable
 *
 * @author csnyulas
 */
class JDBCDataTable extends AbstractTableModel {

private Statement stmt;
private ResultSet resultSet;
private ResultSetMetaData rsMetaData;

private String[] columnNames = {};
private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

/*
 * A default constructor that does nothing at the moment
 */
public JDBCDataTable() {
}

/**
 * Execute a query to obtain data for the model
 *
 * @param conn
 * @param strQuery
 * @param numRows
 * @return
 */
public boolean executeQuery(Connection conn, String strQuery, long numRows) {
    try {
        stmt = conn.createStatement();

        if (numRows < 0) {
            resultSet = stmt.executeQuery(strQuery);
        } else {
            try {
                //try to make special limiting query construct, based on the database or driver name
                resultSet = stmt.executeQuery(convertToLimitedQuery(conn, strQuery, numRows));
            } catch (SQLException ex2) {
                //if doesn't work, select all the rows and limit the displayed rows manually
                resultSet = stmt.executeQuery(strQuery);
            }
        }

        rsMetaData = resultSet.getMetaData();

        int numColumns = rsMetaData.getColumnCount();
        columnNames = new String[numColumns];
        for (int i = 0; i < numColumns; i++) {
            columnNames[i] = rsMetaData.getColumnLabel(i + 1);
        }

        rows.removeAllElements();
        int count = 0;
        while (resultSet.next()) {
            if (count == numRows)
                break;
            Vector<Object> newRow = new Vector<Object>();
            for (int i = 0; i < numColumns; i++) {
                int type = rsMetaData.getColumnType(i + 1);
                Object obj = resultSet.getObject(i + 1);
                newRow.addElement(Global.getSQLProtegeObject(type, obj));
            }
            rows.addElement(newRow);
            count++;
        }
        resultSet.close();
        stmt.close();

        fireTableChanged(null);
        return true;
    } catch (SQLException ex) {
        //Global.defaultSQLExceptionHandler(ex);
        return false;
    }
}


/**
 * Add limitation to strQuery using non-standard keywords based on
 * <a href="http://en.wikipedia.org/wiki/Select_(SQL)#Non-standard_syntax"> http://en.wikipedia.org/wiki/Select_(SQL)</a>
 * in order to limit the length of the result set to numRows
 *
 * @param con      a JDBC connection
 * @param strQuery a simple select query
 * @param numRows  number of rows the result set should be limited to
 * @return the extended query containing the resultset length limitation
 */
private String convertToLimitedQuery(Connection con, String strQuery, long numRows) {
    DatabaseMetaData dbMetaData = null;
    String dbName = "";
    String strLimitedQuery = strQuery;
    try {
        dbMetaData = con.getMetaData();
        dbName = dbMetaData.getDatabaseProductName();
        if (dbName.equals(""))
            dbName = dbMetaData.getDriverName();
    } catch (SQLException e) {
    }

    dbName = dbName.toLowerCase();
    strQuery = strQuery.toUpperCase();
    assert strQuery.contains("SELECT") : "Wrong query! strQuery should be a SELECT query";

    if (dbName.contains("firebird") || dbName.contains("informix")) {
        int posAfterSelect = strQuery.indexOf("SELECT") + "SELECT".length();
        strLimitedQuery = strLimitedQuery.substring(0, posAfterSelect) +
                                  " FIRST " + numRows +
                                  strLimitedQuery.substring(posAfterSelect);
    } else if (dbName.contains("microsoft") || dbName.contains("sql server") || dbName.contains("access")/* other alternatives? */) {
        int posAfterSelect = strQuery.indexOf("SELECT") + "SELECT".length();
        strLimitedQuery = strLimitedQuery.substring(0, posAfterSelect) +
                                  " TOP " + numRows +
                                  strLimitedQuery.substring(posAfterSelect);
    } else if (dbName.contains("oracle") /* other alternatives? */) {
        strLimitedQuery = strLimitedQuery + " WHERE ROWNUM <= " + numRows;
    } else if (dbName.contains("interbase") /* other alternatives? */) {
        strLimitedQuery = strLimitedQuery + " ROWS " + numRows;
    } else if (dbName.contains("mysql") || dbName.contains("sqlite") || dbName.contains("postgresql") /* other alternatives? */) {
        strLimitedQuery = strLimitedQuery + " LIMIT " + numRows;
    } else if (dbName.contains("db2") /* other alternatives? */) {
        strLimitedQuery = strLimitedQuery + " FETCH FIRST " + numRows + " ROWS ONLY";
    } else {
        //default extension
        strLimitedQuery = strLimitedQuery + " LIMIT " + numRows;
    }

    return strLimitedQuery;
}


// Implementation of TableModel interface
// See the Java Doc for additional information on the methods that follow

/**
 * Return the name of a column
 *
 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
 */
public String getColumnName(int columnIndex) {
    if (columnNames[columnIndex] != null)
        return columnNames[columnIndex];
    else
        return "";
}

/**
 * Return the class type of a column
 *
 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
 */
public Class<?> getColumnClass(int columnIndex) {
    int type;
    try {
        type = rsMetaData.getColumnType(columnIndex + 1);
    } catch (SQLException e) {
        return super.getColumnClass(columnIndex);
    }

    ValueType protegeType = Global.getSQLProtegeType(type);
    return protegeType.getJavaType();
}

/**
 * Return the number of rows in this model
 *
 * @see javax.swing.table.TableModel#getRowCount()
 */
public int getRowCount() {
    return rows.size();
}

/**
 * Return the number of columns contained in this model
 *
 * @see javax.swing.table.TableModel#getColumnCount()
 */
public int getColumnCount() {
    return columnNames.length;
}

/**
 * Return the data residing at a row and column
 *
 * @see javax.swing.table.TableModel#getValueAt(int, int)
 */
public Object getValueAt(int rowIndex, int columnIndex) {
    Vector row = (Vector) rows.elementAt(rowIndex);
    return row.elementAt(columnIndex);
}

}

