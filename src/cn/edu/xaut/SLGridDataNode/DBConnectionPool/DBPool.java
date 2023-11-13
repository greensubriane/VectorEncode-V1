/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author He Ting
 */

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBPool {
public static List<Connection> connections = new ArrayList<Connection>();

private static String dbJdbc;

private static String dbUser;

private static String dbPwd;

private static int initialsize;

private static int maxactive;

private static int maxidle;

private static long maxwait;

private static boolean testonborrow;

private static boolean testonreturn;

private static boolean testwhileidle;

private static long timebetweenevictionrunsmillis;

private static int numtestsperevictionrun;

private static long minevictableidletimemillis;

private static String driver;

private static Class driverClass;

private static ObjectPool connectionPool;

public static Map<String, ObjectPool> map;

private static final Logger logger = Logger.getLogger(DBPool.class);


public DBPool() {

}

/*
 * 初始化数据源
 */
private static synchronized void initDataSource() {
    //驱动数据源
    if (driverClass == null) {
        try {
            driverClass = Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}

/*
 * 连接池启动
 */

public static void StartPool(String poolname, String dbJdbc, String dbUser, String dbPwd, int initialsize, int maxactive, int maxidle, long maxwait, boolean testonborrow, boolean testonreturn, long timebetweenevictionrunsmillis, boolean testwhileidle, int numtestsperevictionrun, long minevictableidletimemillis) {
    initDataSource();
    try {
        connectionPool = new GenericObjectPool(null, maxactive, (byte) 1, maxwait, maxidle, 30, testonborrow, testonreturn, timebetweenevictionrunsmillis, numtestsperevictionrun, minevictableidletimemillis, testwhileidle);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbJdbc, dbUser, dbPwd);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        Class.forName("org.apache.commons.dbcp.PoolingDriver");
        PoolingDriver drivers = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        drivers.registerPool(poolname, connectionPool);
        map.put(poolname, connectionPool);
        System.out.println("Create " + poolname + " for Database Connection Success.");
    } catch (Exception e) {
        logger.error("创建数据库连接池的过程中出现错误:" + e.getMessage(), e);
    }
}

/*
 * 释放连接池
 */
public static void ShutdownPool() {
    try {
        PoolingDriver drivers1 = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        drivers1.closePool("dbcp");
        System.out.println("连接池已关闭");
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
}
/*
 * 打印连接池的状态
 */

public static void GetPoolStates(String poolname) {

    try {
        PoolingDriver poolingdriver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        ObjectPool objectPools = poolingdriver.getConnectionPool(poolname);
        System.out.println("活动的连接: " + objectPools.getNumActive());
        System.out.println("空闲的连接: " + objectPools.getNumIdle());
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }

}

/*
 * 初始化数据
 */
public static void init(String poolname) {
    List<BaseConnBean> pools = ParseConfig.read();
    for (BaseConnBean baseconnbean : pools) {
        if (baseconnbean.getName().equals(poolname)) {
            dbJdbc = baseconnbean.getJdbcurl();
            dbUser = baseconnbean.getUsername();
            dbPwd = baseconnbean.getPassword();
            maxactive = baseconnbean.getmaxActive();
            driver = baseconnbean.getDriver();
            initialsize = baseconnbean.getinitialSize();
            testonborrow = baseconnbean.gettestOnBorrow();
            testonreturn = baseconnbean.gettestOnReturn();
            testwhileidle = baseconnbean.gettestWhileIdle();
            timebetweenevictionrunsmillis = baseconnbean.gettimeBetweenEvictionRunsMillis();
            numtestsperevictionrun = baseconnbean.getnumTestsPerEvictionRun();
            minevictableidletimemillis = baseconnbean.getminEvictableIdletimeMillis();
        }
    }

}

/*
 * 从连接池中取得连接
 */
public synchronized static Connection getDBConnection(String poolname) {
    Connection conn = null;
    if (map == null) {
        System.out.println("map null,can create dbpool");
        map = new HashMap<String, ObjectPool>();
    }
    if (map.get(poolname) == null) {
        init(poolname);//初始化基本信息
        StartPool(poolname, dbJdbc, dbUser, dbPwd, initialsize, maxactive, maxidle, maxwait, testonborrow, testonreturn, timebetweenevictionrunsmillis, testwhileidle, numtestsperevictionrun, minevictableidletimemillis);
    }
    try {
        conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + poolname);
        GetPoolStates(poolname);
    } catch (Exception e) {
        logger.error("链接过程中出现错误:" + e.getMessage(), e);
    }
    return conn;
}

/*
 * 关闭数据库连接
 */

public static void close(Connection c) {
    try {
        if (c != null) {
            c.close();
        }
    } catch (Exception e) {
        System.err.println(e.getMessage());
    }
}
}
