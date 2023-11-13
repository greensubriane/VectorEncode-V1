/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode;

/**
 * @author He Ting
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Testexample {
int flag;

public void test() {
    try {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/xr_system", "postgres", "ikoiko");
        PreparedStatement pstmt = conn.prepareStatement("insert into example values(2,'nihao')", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        flag = pstmt.executeUpdate();
        if (flag != 0) {
            System.out.print("数据已写入数据库！");
        }
        //stat = DBPool.GetPoolStates(LoginNodeBean.getIP());
        // System.out.println("连接状态:"+stat);

    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}

public static void main(String args[]) {
    Testexample example = new Testexample();
    example.test();
}
}

