package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;


/**
 * The DataPreviewPanel class encapsulates a table used to display a preview
 * of a table to the user.
 *
 * @author csnyulas
 */
class DataPreviewPanel extends JPanel implements ItemListener, ActionListener {

// Class data

private JTable tableData;
private JDBCDataTable dataModel;

private JCheckBox previewCheckBox;
private JLabel labelTableName;

private JTextField numRowsField;
private JButton setNumRowsButton;

private long numRows = Global.PREVIEW_DEFAULT_ROW_COUNT;

private Connection conn;
private String queryString;

// Constructor

public int getrows() {

    return Integer.parseInt(numRowsField.getText());
}

public DataPreviewPanel() {
    setLayout(new BorderLayout());
    setAlignmentX(Component.CENTER_ALIGNMENT);
    setAlignmentY(Component.CENTER_ALIGNMENT);

    // add UI components
    JPanel optionsPanel = new JPanel(new GridLayout(1, 2));

    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    previewCheckBox = new JCheckBox("预览");
    previewCheckBox.setSelected(true);
    previewCheckBox.setMnemonic('P');
    previewCheckBox.addItemListener(this);
    previewCheckBox.setEnabled(false);
    p1.add(previewCheckBox);

    labelTableName = new JLabel();
    p1.add(labelTableName);

    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    numRowsField = new JTextField(Long.toString(numRows), 6);
    numRowsField.setEnabled(false);
    p2.add(new JLabel("行数"));
    p2.add(numRowsField);
    p2.add(new JLabel(" ( <0 = all ) "));
    setNumRowsButton = new JButton("设置");
    setNumRowsButton.addActionListener(this);
    setNumRowsButton.setEnabled(false);
    p2.add(setNumRowsButton);

    optionsPanel.add(p1);
    optionsPanel.add(p2);
    add(optionsPanel, BorderLayout.NORTH);

    dataModel = new JDBCDataTable();
    tableData = new JTable(dataModel);
    JScrollPane sp = new JScrollPane(tableData);
    //CHANGED!!!!
    sp.setPreferredSize(new Dimension(400, 150));
    //add(new LabeledComponent("Preview",sp,true),BorderLayout.CENTER);
    add(sp, BorderLayout.CENTER);
}

public static void main(String[] args) {
    try {
        DataPreviewPanel panel = new DataPreviewPanel();
        panel.setSize(800, 600);
        panel.setVisible(true);

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

// Handle state changed events of the Preview check box

public void itemStateChanged(ItemEvent e) {
    Object item = e.getItem();
    int newState = e.getStateChange();
    if (item == previewCheckBox) {
        if (newState == ItemEvent.DESELECTED)
            deleteTable();
        else if (newState == ItemEvent.SELECTED) ;
        updateTable2();
    }
}

// Handle action events

public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    if (src == setNumRowsButton) {
        try {
            numRows = Long.parseLong(numRowsField.getText());
        } catch (NumberFormatException ex) {
            numRows = Global.PREVIEW_DEFAULT_ROW_COUNT;
            numRowsField.setText(Long.toString(numRows));
        }
        updateTable2();
    }
}

// Called by other classes to display a preview of another table

public void updateTable(Connection connection, String strTableName) {
    labelTableName.setText(strTableName);
    this.conn = connection;
    queryString = "SELECT * FROM " + strTableName;
    updateTable2();
}

// Update the table with data from a new table

private void updateTable2() {
    if (previewCheckBox.isSelected() && labelTableName.getText().length() > 0) {
        boolean bOK = dataModel.executeQuery(conn, queryString, numRows);
        if (!bOK) {
            deleteTable();

            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }
}

public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    previewCheckBox.setEnabled(enabled);
    numRowsField.setEnabled(enabled);
    setNumRowsButton.setEnabled(enabled);
    if (!enabled) {
        labelTableName.setText("");
    }
}

// Return the current data model

public JDBCDataTable getTable() {
    return dataModel;
}

// Clear the contents of the table

public void deleteTable() {
    dataModel = new JDBCDataTable();
    tableData.setModel(dataModel);
}
}

