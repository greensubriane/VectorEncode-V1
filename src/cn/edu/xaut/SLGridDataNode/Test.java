/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode;

/**
 * @author greensubmarine
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {

public static ResultSet getTableData(String dbname, String tablename, String IP, String DBType, String Username, String Password) {
    ResultSet result = null;
    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //Connection connection = Dao.getConntection(dbname, IP, DBType, Username, Password);
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://" + IP + ":1433;DatabaseName=" + dbname + "", Username, Password);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "Use " + dbname + " select * from " + tablename;
        result = statement.executeQuery(sql);
    } catch (Exception e) {
        e.printStackTrace();

    }
    return result;
}

public static void main(String args[]) {

    try {
        ResultSet rs = Test.getTableData("WHALLDB", "ST_PPTN_R", "127.0.0.1", "SQLSERVER2008", "sa", "ikoiko");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

}
