/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.UI;

/**
 * @author Administrator
 */

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;

public class TableModelListenerDemo {
public static void main(String args[]) {

    final Object rowData[][] = {{"1", "one", "I"}, {"2", "two", "II"}, {"3", "three", "III"}};
    final String columnNames[] = {"#", "English", "Roman"};

    final JTable table = new JTable(rowData, columnNames);
    JScrollPane scrollPane = new JScrollPane(table);
    table.getModel().addTableModelListener(new TableModelListener() {

        public void tableChanged(TableModelEvent e) {
            System.out.println(e);
        }
    });

    table.setValueAt("", 0, 0);
    JFrame frame = new JFrame("Resizing Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(scrollPane, BorderLayout.CENTER);

    frame.setSize(300, 150);
    frame.setVisible(true);

}
}
