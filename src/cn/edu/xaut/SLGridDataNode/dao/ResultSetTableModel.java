package cn.edu.xaut.SLGridDataNode.dao;

/*
 * Author: greensubmarine
 * ResultSetTableModel类用来对查询结果进行各种处理
 */

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class ResultSetTableModel extends AbstractTableModel {

ResultSet rs;
ResultSetMetaData rsmt;

public ResultSetTableModel(ResultSet rs) {
    this.rs = rs;
    try {
        rsmt = rs.getMetaData();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public int getColumnCount() {
    try {
        return rsmt.getColumnCount();
    } catch (Exception e) {
        System.err.println(e.getMessage());
        return 0;
    }
}

@Override
public int getRowCount() {
    try {
        rs.last();
        return rs.getRow();
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}

@Override
public String getColumnName(int c) {
    try {
        return rsmt.getColumnName(c + 1);
    } catch (Exception e) {
        //e.printStackTrace();
        e.printStackTrace();
        return null;
    }
}

@Override
public Object getValueAt(int r, int c) {
    try {
        rs.absolute(r + 1);
        return rs.getObject(c + 1) + "";
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}