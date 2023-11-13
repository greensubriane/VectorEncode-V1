/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author Administrator
 */
//数据库连接池  单例模式

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.util.List;

import static cn.edu.xaut.SLGridDataNode.DBConnectionPool.C3P0ConfigFileParsing.read;
import static java.lang.System.err;
import static java.lang.System.out;

public class ConnectionManager {
private ComboPooledDataSource ds;
private Connection connection;
private String poolname;
private String user;
private String password;
private String jdbcUrl;
private String driverClass;
private int acquireIncrement;
private int initialPoolSize;
private int minPoolSize;
private int maxPoolSize;
private int maxStatements;
private int maxStatementsPerConnection;

private ComboPooledDataSource initialPool(String poolname) throws Exception {
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
    ds = new ComboPooledDataSource();
    ds.setDriverClass(driverClass);
    ds.setJdbcUrl(jdbcUrl);
    ds.setUser(user);
    ds.setPassword(password);

    //初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3 initialPoolSize
    ds.setInitialPoolSize(initialPoolSize);
    //连接池中保留的最大连接数。Default: 15 maxPoolSize
    ds.setMaxPoolSize(maxPoolSize);
    //// 连接池中保留的最小连接数。
    ds.setMinPoolSize(minPoolSize);
    //当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 acquireIncrement
    ds.setAcquireIncrement(acquireIncrement);

    //每60秒检查所有连接池中的空闲连接。Default: 0  idleConnectionTestPeriod
    ds.setIdleConnectionTestPeriod(60);
    //最大空闲时间,25000秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0  maxIdleTime
    ds.setMaxIdleTime(2_500);
    //连接关闭时默认将所有未提交的操作回滚。Default: false autoCommitOnClose
    ds.setAutoCommitOnClose(true);

    ds.setPreferredTestQuery("select sysdate from dual");

    ds.setTestConnectionOnCheckout(true);

    ds.setTestConnectionOnCheckin(true);

    ds.setAcquireRetryAttempts(30);

    ds.setAcquireRetryDelay(1_000);

    ds.setBreakAfterAcquireFailure(true);
    ds.setMaxStatements(maxStatements);
    ds.setMaxStatementsPerConnection(maxStatementsPerConnection);
    return ds;

}


public Connection getconnection(String poolname) {

    try {
        ComboPooledDataSource datasource = this.initialPool(poolname);
        connection = datasource.getConnection();
        return connection;
    } catch (Exception e) {
        err.println(e.getMessage());
        return null;
    }

}

public static void main(String args[]) {
    try {
        Connection connection = new ConnectionManager().getconnection("127.0.0.1-gmldatabase");
        out.println("连接成功");
    } catch (Exception e) {
        err.println(e.getMessage());
    }
}
}

