/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.UI;

/**
 * @author greensubmarine
 */

import twaver.TWaverUtil;
import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class TTableComponent {
public static void main(String args[]) {
    JFrame frame = new JFrame("TWaver Table");
    TTable table = new TTable();
    table.addColumn(new TTableColumn("Name"));
    table.addColumn(new TTableColumn("Description"));
    TTableModel model = table.getTableModel();

    Vector row = new Vector();
    row.addElement("Peter");
    row.addElement("President");
    model.addRow(row);

    row = new Vector();
    row.addElement("Mike");
    row.addElement("Vice President");
    model.addRow(row);
    frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    frame.getContentPane().add(new TTableNavigator(table.getTableModel()), BorderLayout.NORTH);
    frame.setSize(400, 100);
    TWaverUtil.centerWindow(frame);
    frame.show();
}
}
