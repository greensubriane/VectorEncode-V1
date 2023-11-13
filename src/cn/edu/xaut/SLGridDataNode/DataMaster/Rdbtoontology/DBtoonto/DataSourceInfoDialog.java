package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


class DataSourceInfoDialog extends JDialog implements ActionListener, KeyListener {

private boolean ok = false;

private JButton okButton;

private boolean m_supportsANSI92EntryLevelSQL;
private boolean m_supportsANSI92FullSQL;
private boolean m_supportsANSI92IntermediateSQL;
private boolean m_supportsCoreSQLGrammar;


// Constructor
public DataSourceInfoDialog(java.awt.Frame parent, Connection conn) {
    super(parent, "Data source info", true);


    try {
        DatabaseMetaData dbmd = conn.getMetaData();

        m_supportsANSI92EntryLevelSQL = dbmd.supportsANSI92EntryLevelSQL();
        m_supportsANSI92FullSQL = dbmd.supportsANSI92FullSQL();
        m_supportsANSI92IntermediateSQL = dbmd.supportsANSI92IntermediateSQL();
        m_supportsCoreSQLGrammar = dbmd.supportsCoreSQLGrammar();

    } catch (SQLException e) {
        Global.defaultSQLExceptionHandler(JOptionPane.getFrameForComponent(this), e);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    ok = false;

    setSize(new Dimension(300, 250));

    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    String info = new String();
    JPanel centerPanel = new JPanel(new GridLayout(4, 1));
    JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p1.add(new JLabel("Supports ANSI92 Entry Level SQL"));
    if (m_supportsANSI92EntryLevelSQL)
        info = "YES";
    else
        info = "NO";
    JTextField f1 = new JTextField(info, 5);
    f1.setEnabled(false);
    p1.add(f1);
    centerPanel.add(p1);

    JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p2.add(new JLabel("Supports ANSI92 Full SQL"));
    if (m_supportsANSI92FullSQL)
        info = "YES";
    else
        info = "NO";
    JTextField f2 = new JTextField(info, 5);
    f2.setEnabled(false);
    p2.add(f2);
    centerPanel.add(p2);

    JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p3.add(new JLabel("Supports ANSI92 Intermediate SQL"));
    if (m_supportsANSI92IntermediateSQL)
        info = "YES";
    else
        info = "NO";
    JTextField f3 = new JTextField(info, 5);
    f3.setEnabled(false);
    p3.add(f3);
    centerPanel.add(p3);

    JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p4.add(new JLabel("Supports Core SQL Grammar"));
    if (m_supportsCoreSQLGrammar)
        info = "YES";
    else
        info = "NO";
    JTextField f4 = new JTextField(info, 5);
    f4.setEnabled(false);
    p4.add(f4);
    centerPanel.add(p4);

    contentPane.add(centerPanel, BorderLayout.CENTER);

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    okButton = new JButton("Ok");
    okButton.setMnemonic('K');
    okButton.addActionListener(this);
    okButton.addKeyListener(this);
    southPanel.add(okButton);

    contentPane.add(southPanel, BorderLayout.SOUTH);
}

public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    if (src == okButton) {
        disposeDialog();
    }
}

public void disposeDialog() {
    setVisible(false);
    ok = true;
}

public boolean showDialog() {
    setVisible(true);
    return ok;
}

public void keyPressed(KeyEvent e) {
}

public void keyReleased(KeyEvent e) {
}

public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == KeyEvent.VK_ENTER || e.getKeyChar() == KeyEvent.VK_ESCAPE) {
        disposeDialog();
    }
}

}
