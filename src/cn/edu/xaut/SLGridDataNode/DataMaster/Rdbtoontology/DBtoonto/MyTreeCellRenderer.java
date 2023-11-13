package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.util.ComponentUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;


class MyTreeCellRenderer extends DefaultTreeCellRenderer {
ImageIcon sysTableIcon;
ImageIcon tableIcon;
ImageIcon viewIcon;
ImageIcon middleIcon;
ImageIcon pkIcon;
ImageIcon fkIcon;

public MyTreeCellRenderer() {

    sysTableIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/table_system.gif");
    tableIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/table.gif");
    viewIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/view.gif");
    middleIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/middle.gif");
    pkIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/pk.gif");
    fkIcon = ComponentUtilities.loadImageIcon(MyTreeCellRenderer.class, "resource/fk.gif");

		/*int status = tableIcon.getImageLoadStatus();
		switch(status) {
			case MediaTracker.ABORTED:
				Global.debug("ABORTED");
				break;
			case MediaTracker.ERRORED:
				Global.debug("ERRORED");
				break;
			case MediaTracker.COMPLETE:
				Global.debug("COMPLETE");
				break;
		}*/
}

// Override
public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {

    super.getTreeCellRendererComponent(
            tree, value, sel,
            expanded, leaf, row,
            hasFocus);

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    if (node != null) {
        TreeNodeInfo info = (TreeNodeInfo) node.getUserObject();
        Font font = getFont();
        switch (info.m_type) {
            case TreeNodeInfo.TABLE_SYSTEM:
                setFont(font.deriveFont(Font.ITALIC));
                setIcon(sysTableIcon);
                break;
            case TreeNodeInfo.TABLE:
                setFont(font.deriveFont(Font.BOLD));
                setIcon(tableIcon);
                break;
            case TreeNodeInfo.TABLE_VIEW:
                setFont(font.deriveFont(Font.PLAIN));
                setIcon(viewIcon);
                break;
            case TreeNodeInfo.COLUMN:
                setFont(font.deriveFont(Font.PLAIN));
                break;
            case TreeNodeInfo.COLUMN_PK:
                setFont(font.deriveFont(Font.BOLD));
                setIcon(pkIcon);
                break;
            case TreeNodeInfo.COLUMN_FK:
                setFont(font.deriveFont(Font.PLAIN));
                //TODO see if we have another icon
                setIcon(fkIcon);
                break;
        }
    }

    return this;
}
}

