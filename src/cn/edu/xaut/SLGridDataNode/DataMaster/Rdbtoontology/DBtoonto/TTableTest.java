/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * @author greensubmarine
 */

import twaver.table.TTable;
import twaver.table.TTableColumn;
import twaver.table.TTableModel;
import twaver.table.TTableNavigator;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class TTableTest {
public static void main(String args[]) {
    JFrame frame = new JFrame();
    TTable table = new TTable();
    table.addColumn(new TTableColumn("Name"));
    table.addColumn(new TTableColumn("Description"));
    table.addColumn(new TTableColumn("Additional"));
    Vector row = new Vector();
    TTableModel model = table.getTableModel();
    for (int i = 0; i < 100; i++) {
        row.add("heting");
        row.add("president");
        model.addRow(row);
    }
    int[] options = new int[]{20, 30, 50, 0};
    TTableNavigator navigator = new TTableNavigator(table.getTableModel(), options);

    frame.getContentPane().add(new JScrollPane(table), new BorderLayout().CENTER);
    frame.getContentPane().add(navigator, new BorderLayout().NORTH);
    frame.setVisible(true);
    frame.setResizable(true);


}
}
