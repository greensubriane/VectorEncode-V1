/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.GeoTool;

/**
 * @author Administrator
 */

import org.geotools.jdbc.JDBCDataStore;

public class PGDataStoreBean {
JDBCDataStore pgDataStore;

public void setPGDataStore(JDBCDataStore pgDataStore) {
    this.pgDataStore = pgDataStore;
}

public JDBCDataStore getPGDataStore() {
    return pgDataStore;
}

}
