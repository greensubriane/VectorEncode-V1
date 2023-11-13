/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author Administrator
 */
public class AddDataNode {

public String poolname;
public String username;
public String password;
public String port;
public String driver;
public String wait;
public String maxconn;

public String getPoolname() {
    return poolname;
}

public void setPoolname(String poolname) {
    this.poolname = poolname;
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

public void setPassword(String password) {
    this.password = password;
}

public String getPort() {
    return port;
}

public void setPort(String port) {
    this.port = port;
}

public String getDriver() {
    return driver;
}

public void setDriver(String driver) {
    this.driver = driver;
}

public String getWait() {
    return wait;
}

public void setWait(String wait) {
    this.wait = wait;
}

public String getMaxconn() {
    return maxconn;
}

public void setMaxconn(String maxconn) {
    this.maxconn = maxconn;
}
}
