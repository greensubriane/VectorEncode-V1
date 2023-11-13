package cn.edu.xaut.SLGridDataNode.dao;

/*
 * @author greensubmarine
 * DBConnection类用来实现对xr_system数据库的各种操作
 */

import cn.edu.xaut.SLGridDataNode.Util.LoginNodeBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
//String stat;
public void DBConnection() {
}

private java.sql.Connection con;

private java.sql.Connection conns;

public void ExecuteInsert(String InsertSQL) throws Exception {
    int flag;
    try {
        con = Dao.getxr_systemconnection(LoginNodeBean.getIP());
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement(InsertSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            flag = pstmt.executeUpdate();
            if (flag != 0) {
                System.out.println("数据已写入数据库！");
            }
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

public ResultSet ExecuteSQL(String SQLString) {
    ResultSet rs1 = null;
    try {
        con = Dao.getxr_systemconnection(LoginNodeBean.getIP());
        if (con != null) {
            Statement pstmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs1 = pstmt.executeQuery(SQLString);
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
    return rs1;
}
}
