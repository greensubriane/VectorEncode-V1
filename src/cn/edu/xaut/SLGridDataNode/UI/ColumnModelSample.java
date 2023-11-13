/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.UI;

/**
 * @author Administrator
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ColumnModelSample {
public static void main(String args[]) {
    final Object rows[][] = {{"one", "1"}, {"two", "2"}, {"three", "3"}};
    final Object headers[] = {"English", "#"};
    JFrame frame = new JFrame("Scrollless Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JTable table = new JTable(rows, headers);

    TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
        public void columnAdded(TableColumnModelEvent e) {
            System.out.println("Added");
        }

        public void columnMarginChanged(ChangeEvent e) {
            System.out.println("Margin");
        }

        public void columnMoved(TableColumnModelEvent e) {
            System.out.println("Moved");
        }

        public void columnRemoved(TableColumnModelEvent e) {
            System.out.println("Removed");
        }

        public void columnSelectionChanged(ListSelectionEvent e) {
            System.out.println("Selection Changed");
        }
    };
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.addColumnModelListener(tableColumnModelListener);

    columnModel.setColumnMargin(12);

    TableColumn column = new TableColumn(1);
    columnModel.addColumn(column);

    JScrollPane pane = new JScrollPane(table);
    frame.add(pane, BorderLayout.CENTER);
    frame.setSize(300, 150);
    frame.setVisible(true);
}
} 
