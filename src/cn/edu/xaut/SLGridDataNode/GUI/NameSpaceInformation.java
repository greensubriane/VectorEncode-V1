/*
 * Created by JFormDesigner on Wed Oct 12 12:17:20 CEST 2011
 */

package cn.edu.xaut.SLGridDataNode.GUI;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author ting
 */
public class NameSpaceInformation extends JFrame {
public NameSpaceInformation() {
    initComponents();
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    label1 = new JLabel();
    comboBox1 = new JComboBox();
    button1 = new JButton();
    scrollPane1 = new JScrollPane();
    table1 = new JTable();

    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new FormLayout(
            "113dlu, 134dlu, 136dlu",
            "default, 204dlu"));

    //---- label1 ----
    label1.setText("                    FileName:");
    contentPane.add(label1, CC.xy(1, 1, CC.FILL, CC.FILL));
    contentPane.add(comboBox1, CC.xy(2, 1));

    //---- button1 ----
    button1.setText("Choose");
    contentPane.add(button1, CC.xy(3, 1));

    //======== scrollPane1 ========
    {
        scrollPane1.setViewportView(table1);
    }
    contentPane.add(scrollPane1, CC.xywh(1, 2, 3, 1));
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JLabel label1;
private JComboBox comboBox1;
private JButton button1;
private JScrollPane scrollPane1;
private JTable table1;
// JFormDesigner - End of variables declaration  //GEN-END:variables
}
