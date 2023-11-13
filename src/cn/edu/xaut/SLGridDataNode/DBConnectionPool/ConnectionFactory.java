/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author Administrator
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static cn.edu.xaut.SLGridDataNode.DBConnectionPool.C3P0ConfigFileParsing.read;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class ConnectionFactory {

private static String user;
private static String password;
private static String jdbcUrl;
private static String driverClass;
private static int acquireIncrement;
private static int initialPoolSize;
private static int minPoolSize;
private static int maxPoolSize;
private static int maxStatements;
private static int maxStatementsPerConnection;


private ConnectionFactory() {
}

private static ComboPooledDataSource ds = null;

//static {
public static void initpool(String poolname) {
    List<C3P0Bean> pools = read();
    for (C3P0Bean baseconnbean : pools) {
        if (baseconnbean.getName().equals(poolname)) {
            user = baseconnbean.getUser();
            password = baseconnbean.getPassword();
            jdbcUrl = baseconnbean.getjdbcUrl();
            driverClass = baseconnbean.getDriverClass();
            acquireIncrement = baseconnbean.getAcquireIncrement();
            initialPoolSize = baseconnbean.getInitialPoolSize();
            minPoolSize = baseconnbean.getMinPoolSize();
            maxPoolSize = baseconnbean.getMaxPoolsize();
            maxStatements = baseconnbean.getMaxStatements();
            maxStatementsPerConnection = baseconnbean.getMaxStatementsperConnection();

        }
    }
    // Logger log = Logger.getLogger("com.mchange"); // 日志
    // log.setLevel(Level.WARNING);
    ds = new ComboPooledDataSource();
    try {
        // 设置JDBC的Driver类
        //ds.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");  // 参数由 Config 类根据配置文件读取
        ds.setDriverClass(driverClass);
    } catch (PropertyVetoException ex) {
        getLogger(ConnectionFactory.class.getName()).log(SEVERE, null, ex);
    }
    // 设置JDBC的URL
    //ds.setJdbcUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=gmldatabase");
    ds.setJdbcUrl(jdbcUrl);
    //ds.setAcquireIncrement(acquireIncrement);
    //ds.setInitialPoolSize(initialPoolSize);
    //ds.setMaxStatements(maxStatements);
    //ds.setMaxStatementsPerConnection(maxStatementsPerConnection);
    // 设置数据库的登录用户名
    // ds.setUser("sa");
    ds.setUser(user);
    // 设置数据库的登录用户密码
    //ds.setPassword("ikoiko");
    ds.setPassword(password);
    // 设置连接池的最大连接数
    //ds.setMaxPoolSize(200);
    ds.setMinPoolSize(minPoolSize);
    // 设置连接池的最小连接数
    //ds.setMinPoolSize(20);
    ds.setMaxPoolSize(maxPoolSize);
    ds.setTestConnectionOnCheckout(true);

    ds.setTestConnectionOnCheckin(true);

    ds.setAcquireRetryAttempts(30);

    ds.setAcquireRetryDelay(1_000);

    ds.setBreakAfterAcquireFailure(true);
}
//}

public static synchronized Connection getConnection(String poolname) {
    Connection con = null;
    initpool(poolname);
    try {
        con = ds.getConnection();
    } catch (SQLException e1) {
        e1.printStackTrace();
    }
    return con;
}
// C3P0 end
}

