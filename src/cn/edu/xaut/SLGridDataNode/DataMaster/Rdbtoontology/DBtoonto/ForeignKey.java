/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * @author greensubmarine
 */
class ForeignKey {

public String name;
public String localField;
public String referenceField;
public String referenceTable;


public ForeignKey(String n, String l, String f, String t) {
    name = n;
    localField = l;
    referenceField = f;
    referenceTable = t;
}

public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("Name: " + name);
    buf.append("\nLocal Field: " + localField);
    buf.append("\nRef Field: " + referenceField);
    buf.append("\nRef Table: " + referenceTable);
    return buf.toString();
}
}

