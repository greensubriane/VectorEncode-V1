/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.dao.Db;

public class LoginNodeBean {

private static Db dbtest;

private static String Username, Password, IP, PortNum, DBType, DBName;

public static Db getDB() {
    return dbtest;
}

public static void setDB(Db dbtest) {
    LoginNodeBean.dbtest = dbtest;
}

public static String getUsername() {
    return Username;
}

public static void setUsername(String Usernames) {
    Username = Usernames;
}

public static String getPassword() {
    return Password;
}

public static void setPassword(String Passwords) {
    Password = Passwords;
}

public static String getIP() {
    return IP;
}

public static void setIP(String IPs) {
    IP = IPs;
}

public static String getPortNum() {
    return PortNum;
}

public static void setPortNum(String PortNums) {
    PortNum = PortNums;
}

public static String getDBType() {
    return DBType;
}

public static void setDBType(String DBTypes) {
    DBType = DBTypes;
}

public static String getDBName() {
    return DBName;
}

public static void setDBName(String dbname) {
    DBName = dbname;
}
}
