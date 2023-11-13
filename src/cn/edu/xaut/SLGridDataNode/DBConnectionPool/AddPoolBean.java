/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author greensubmarine
 */
public class AddPoolBean {
private String IP;
private String PortNum;
private String DbType;
private String Username;
private String Password;

public String getIP() {
    return IP;
}

public void setIP(String IP) {
    this.IP = IP;
}

public String getPortNum() {
    return PortNum;
}

public void setPortNum(String PortNum) {
    this.PortNum = PortNum;
}

public String getDbType() {
    return DbType;
}

public void setDbType(String DbType) {
    this.DbType = DbType;
}

public String getUsername() {
    return Username;
}

public void setUsername(String Username) {
    this.Username = Username;
}

public String getPassword() {
    return Password;
}

public void setPassword(String Password) {
    this.Password = Password;
}
}
