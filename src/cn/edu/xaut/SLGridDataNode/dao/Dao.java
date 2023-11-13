package cn.edu.xaut.SLGridDataNode.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author He Ting
 */

import cn.edu.xaut.SLGridDataNode.DBConnectionPool.DBPool;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Dao {
public static String sql, sql11;
public static String queryparameters;
public static Connection conn, conns;

public static void closeConn() {
    try {
        if (conn != null) {
            conn.close();
        }
    } catch (Exception ex) {
        System.err.println(ex.getMessage());
    }
}

public ResultSet ExecuteQuery(String IP, String DBType, String dbname, String sql) {
    ResultSet result = null;
    try {
        Connection connection = Dao.getConntection(dbname, IP, DBType);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        result = statement.executeQuery(sql);
        return result;
    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "查询过程出现错误请检查查询语句");
        return null;
    }

}

public static List getDBs(String IP, String DBType, String username, String password) {
    List dbs = new ArrayList();

    try {
        if (DBType.equals("SQLSERVER2008")) {
            conns = DBPool.getDBConnection(IP + "-master");
            sql = "select [name] from master.dbo.sysdatabases where DBId>4 Order By [Name]";
            queryparameters = "name";
        } else if (DBType.equals("PostgreSQL")) {
            conns = DBPool.getDBConnection(IP + "-postgres");
            sql = "SELECT datname FROM pg_database";
            queryparameters = "datname";
        }
        Statement stat = conns.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = stat.executeQuery(sql);
        while (result.next()) {
            dbs.add(result.getString(queryparameters));
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    closeConn();
    return dbs;
}

public static Connection getxr_systemconnection(String IP) {
    Connection connection = null;
    try {
        connection = DBPool.getDBConnection(IP + "-gmldatabase");
    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "在连接gmldatabase数据库过程中出现错误!");

    }
    closeConn();
    return connection;

}

public static Connection getConntection(String dbname, String IP, String DBType) {
    Connection connection = null;
    try {
        if (DBType.equals("SQLSERVER2008") || DBType.equals("PostgreSQL")) {
            connection = DBPool.getDBConnection(IP + "-" + dbname);
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "在连接节点数据表过程中出现错误!");

    }
    closeConn();
    return connection;
}

public static ArrayList getTables(String dbname, String IP, String DBType) {
    ArrayList tables = new ArrayList();
    Connection connect2 = null;
    ResultSet resultsetss = null;
    try {
        connect2 = Dao.getConntection(dbname, IP, DBType);
        if (DBType.equals("SQLSERVER2008")) {
            Statement stat = connect2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultsetss = stat.executeQuery("USE " + dbname + " SELECT name as TABLE_NAME FROM sysobjects where type='U'");
        } else if (DBType.equals("PostgreSQL")) {
            DatabaseMetaData metadata1 = connect2.getMetaData();
            resultsetss = metadata1.getTables(connect2.getCatalog(), null, null, new String[]{"TABLE"});
        }
        while (resultsetss.next()) {
            tables.add(resultsetss.getString("TABLE_NAME"));
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "在连接节点数据表过程中出现错误!");
    }
    closeConn();
    return tables;
}

public static ResultSet getTableVaribles(String dbname, String tablename, String IP, String DBType) {
    ResultSet resultset = null;
    Connection connection = null;
    String sql2 = "Use " + dbname + " declare @table_name as varchar(max)" +
                          "set @table_name = '" + tablename + "'" +
                          "select cast(sys.columns.name as varchar(100)) as '字段名', cast(sys.types.name as varchar(100)) as '字段类型', cast(sys.columns.max_length as varchar(100)) as '字段长度', cast(sys.columns.is_nullable as varchar(100)) as '是否为空'," +
                          "cast((select count(*) from sys.identity_columns where sys.identity_columns.object_id = sys.columns.object_id and sys.columns.column_id = sys.identity_columns.column_id) as varchar(100)) as '是否为标识' ," +
                          "cast((select value from sys.extended_properties where sys.extended_properties.major_id = sys.columns.object_id and sys.extended_properties.minor_id = sys.columns.column_id) as varchar(200)) as '字段描述'" +
                          "from sys.columns, sys.tables, sys.types where sys.columns.object_id = sys.tables.object_id and sys.columns.system_type_id=sys.types.system_type_id and sys.tables.name=@table_name order by sys.columns.column_id";
    String sql10 = "SELECT a.attname as name, format_type(a.atttypid,a.atttypmod) as type, a.attnotnull as notnull, col_description(a.attrelid,a.attnum) as comment"
                           + " FROM pg_class as c,pg_attribute as a"
                           + " where c.relname = '" + tablename + "' and a.attrelid = c.oid and a.attnum>0";
    try {
        if (dbname.equals("gmldatabase")) {
            connection = Dao.getxr_systemconnection(IP);
        } else {
            connection = Dao.getConntection(dbname, IP, DBType);
        }
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (DBType.equals("SQLSERVER2008")) {
            resultset = statement.executeQuery(sql2);
            while (resultset.next()) {
                System.out.println(resultset.getString("字段名"));
                System.out.println(resultset.getString("字段类型"));
                System.out.println(resultset.getString("字段长度"));
                System.out.println(resultset.getString("是否为空"));
                System.out.println(resultset.getString("是否为标识"));
                System.out.println(resultset.getString("字段描述"));
            }
        } else if (DBType.equals("PostgreSQL")) {
            resultset = statement.executeQuery(sql10);
            while (resultset.next()) {
                System.out.println(resultset.getString("name"));
                System.out.println(resultset.getString("type"));
                System.out.println(resultset.getString("notnull"));
                System.out.println(resultset.getString("comment"));
            }
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "该数据库不包含数据表，不返回任何字段描述信息!");
    }
    closeConn();
    return resultset;
}

public static ArrayList getTablecolumns(String dbname, String tablename, String IP, String DBType) {
    ArrayList tablecolumns = new ArrayList();
    Connection connection = null;
    ResultSet result = null;
    try {
        if (dbname.equals("gmldatabase")/*dbname.equals("vectorxrsystem")*/) {
            connection = Dao.getxr_systemconnection(IP);
        }
        connection = Dao.getConntection(dbname, IP, DBType);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql12 = "SELECT col_description(a.attrelid,a.attnum) as comment,format_type(a.atttypid,a.atttypmod) as type,a.attname as name, a.attnotnull as notnull"
                               + " FROM pg_class as c,pg_attribute as a"
                               + " where c.relname = '" + tablename + "' and a.attrelid = c.oid and a.attnum>0 ";
        String sql9 = "use " + dbname + " declare @objid int,"
                              + "@objname char(40)"
                              + "set @objname = '" + tablename + "'"
                              + "select @objid = id from sysobjects"
                              + " where id = object_id(@objname)"
                              + "select 'Column_name' = name from syscolumns "
                              + "where id = @objid order by colid ";
        if (DBType.equals("SQLSERVER2008")) {
            result = statement.executeQuery(sql9);
            while (result.next()) {
                tablecolumns.add(result.getString("Column_name"));
            }
        } else if (DBType.equals("PostgreSQL")) {
            result = statement.executeQuery(sql12);
            while (result.next()) {
                tablecolumns.add(result.getString("name"));
            }
        }

    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    closeConn();
    return tablecolumns;
}

public static ResultSet getTableData(String dbname, String tablename, String IP, String DBType) {
    ResultSet result = null;
    Connection connection = null;
    try {
        if (dbname.equals("gmldatabase")/*dbname.equals("vectorxrsystem")*/) {
            connection = Dao.getxr_systemconnection(IP);
        } else {
            connection = Dao.getConntection(dbname, IP, DBType);
        }
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (DBType.equals("SQLSERVER2008")) {
            sql11 = "Use " + dbname + " select * from " + tablename;
        } else if (DBType.equals("PostgreSQL")) {
            sql11 = "select * from " + tablename;
        }
        result = statement.executeQuery(sql11);
    } catch (Exception e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "This Database doesn't contain anytables, return!");
    }
    closeConn();
    return result;
}
}
