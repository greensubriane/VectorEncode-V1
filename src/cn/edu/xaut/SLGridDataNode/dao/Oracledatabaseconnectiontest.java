/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.dao;

/**
 * @author heting
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Oracledatabaseconnectiontest {
static String className = "oracle.jdbc.driver.OracleDriver";
static String sql = "select * from tab";

public static void main(String args[]) {
    try {
        Class.forName(className);
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@10.1.60.28:1521:gmldatabase", "sysman", "ikoiko");
        System.out.println("Connection successful!");

        Statement stat = con.createStatement();
        ResultSet result = stat.executeQuery(sql);
        while (result.next()) {
            System.out.println(result.getString("TNAME"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
