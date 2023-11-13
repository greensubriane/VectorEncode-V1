/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author greensubmarine
 */
public class BaseConnBean {
private String name;
private String username;
private String password;
private String standardjdbcurl;
private String jdbcurl;
private String driver;
private boolean defaultAutoCommit;
private int validationQuery;
private boolean testOnBorrow;
private boolean testOnReturn;
private boolean testWhileIdle;
private long timeBetweenEvictionRunsMillis;
private int numTestsPerEvictionRun;
private long minEvictableIdletimeMillis;
private int initialSize;
private int maxActive;
private int maxIdle;
private long maxWait;
private boolean poolPreParedStatements;
private int maxOpenPreparedStatements;
private boolean accessToUnderlyingConnectionAllowed;

public boolean getdefaultAutoCommit() {
    return defaultAutoCommit;
}

public void setdefaultAutocommit(boolean defaultAutoCommit) {
    this.defaultAutoCommit = defaultAutoCommit;
}

public int getvalidationQuery() {
    return validationQuery;
}

public void setvalidationQuery(int validationQuery) {
    this.validationQuery = validationQuery;
}

public boolean gettestOnBorrow() {
    return testOnBorrow;
}

public void settestOnBorrow(boolean testOnBorrow) {
    this.testOnBorrow = testOnBorrow;
}

public boolean gettestOnReturn() {
    return testOnReturn;
}

public void settestOnResturn(boolean testOnReturn) {
    this.testOnReturn = testOnReturn;
}

public boolean gettestWhileIdle() {
    return testWhileIdle;
}

public void settestwhileIdle(boolean testWhileIdle) {
    this.testWhileIdle = testWhileIdle;

}

public long gettimeBetweenEvictionRunsMillis() {
    return timeBetweenEvictionRunsMillis;
}

public void settimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
}

public int getnumTestsPerEvictionRun() {
    return numTestsPerEvictionRun;
}

public void setnumTestsPerEvictionRun(int numTestsPerEviction) {
    this.numTestsPerEvictionRun = numTestsPerEviction;
}

public long getminEvictableIdletimeMillis() {
    return minEvictableIdletimeMillis;
}

public void setminEvictableIdletimeMillis(long minEvictableIdletimeMillis) {
    this.minEvictableIdletimeMillis = minEvictableIdletimeMillis;
}

public int getinitialSize() {
    return initialSize;
}

public void setinitialSize(int initialSize) {
    this.initialSize = initialSize;
}

public int getmaxActive() {
    return maxActive;
}

public void setmaxActive(int maxActive) {
    this.maxActive = maxActive;
}

public int getmaxIdle() {
    return maxIdle;
}

public void setmaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
}

public long getmaxWait() {
    return maxWait;
}

public void setmaxWait(long maxWait) {
    this.maxWait = maxWait;
}

public boolean getpoolPreParedStatements() {
    return poolPreParedStatements;
}

public void setpoolPreParedstatements(boolean poolPreParedStatements) {
    this.poolPreParedStatements = poolPreParedStatements;
}

public int getmaxOpenPreparedStatements() {
    return maxOpenPreparedStatements;
}

public void setmaxOpenPreparedStatements(int maxOpenPreParedStstements) {
    this.maxOpenPreparedStatements = maxOpenPreParedStstements;
}

public boolean getaccessToUnderlyingConnectionAllowed() {
    return accessToUnderlyingConnectionAllowed;
}

public void setaccessToUnderlyingConnectionAllowed(boolean accessToUnderlyingConnectionAllowed) {
    this.accessToUnderlyingConnectionAllowed = accessToUnderlyingConnectionAllowed;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getPassword() {
    return password;
}

public void setStandardjdbcurl(String standardjdbcurl) {
    this.standardjdbcurl = standardjdbcurl;
}

public String getStandardjdbcurl() {
    return standardjdbcurl;
}

public void setPassword(String password) {
    this.password = password;
}

public String getJdbcurl() {
    return jdbcurl;
}

public void setJdbcurl(String jdbcurl) {
    this.jdbcurl = jdbcurl;
}

public String getDriver() {
    return driver;
}

public void setDriver(String driver) {
    this.driver = driver;
}

}
