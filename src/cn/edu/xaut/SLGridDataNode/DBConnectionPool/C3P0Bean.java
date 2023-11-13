/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author Administrator
 */
public class C3P0Bean {
String poolname;
String user;
String password;
String jdbcUrl;
String driverClass;
int acquireIncrement;
int initialPoolSize;
int minPoolSize;
int maxPoolSize;
int maxStatements;
int maxStatementsPerConnection;

public void setPoolName(String poolname) {
    this.poolname = poolname;
}

public void setUser(String user) {
    this.user = user;
}

public void setPassword(String password) {
    this.password = password;
}

public void setjdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
}

public void setdriverClass(String driverClass) {
    this.driverClass = driverClass;
}

public void setAcquireIncrement(int acquireIncrement) {
    this.acquireIncrement = acquireIncrement;
}

public void setinitialPoolSize(int initialPoolSize) {
    this.initialPoolSize = initialPoolSize;
}

public void setminPoolSize(int minPoolSize) {
    this.minPoolSize = minPoolSize;
}

public void setmaxPoolSize(int maxPoolsize) {
    this.maxPoolSize = maxPoolsize;
}

public void setmaxStatements(int maxStatements) {
    this.maxStatements = maxStatements;
}

public void setStatementsperConnection(int maxStatementsperConnection) {
    this.maxStatementsPerConnection = maxStatementsperConnection;
}

public String getName() {
    return poolname;
}

public String getUser() {
    return user;
}

public String getPassword() {
    return password;
}

public String getjdbcUrl() {
    return jdbcUrl;
}

public String getDriverClass() {
    return driverClass;
}

public int getAcquireIncrement() {
    return acquireIncrement;
}

public int getInitialPoolSize() {
    return initialPoolSize;
}

public int getMinPoolSize() {
    return minPoolSize;
}

public int getMaxPoolsize() {
    return maxPoolSize;
}

public int getMaxStatements() {
    return maxStatements;
}

public int getMaxStatementsperConnection() {
    return maxStatementsPerConnection;
}
}
