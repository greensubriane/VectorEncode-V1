package cn.edu.xaut.SLGridDataNode.Chat.server.chatroom;

//***********能实现文件拖拽的文本区***************//

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class MyTextArea extends JTextArea implements DropTargetListener {
public MyTextArea() {
    new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
}

public void dragEnter(DropTargetDragEvent dtde) {
}

public void dragOver(DropTargetDragEvent dtde) {
}

public void dropActionChanged(DropTargetDragEvent dtde) {
}

public void dragExit(DropTargetEvent dte) {
}

public void drop(DropTargetDropEvent dtde) {
    try {
        Transferable tr = dtde.getTransferable();

        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            System.out.println("file cp");
            List list = (List) (dtde.getTransferable()
                                        .getTransferData(DataFlavor.javaFileListFlavor));
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                File f = (File) iterator.next();
                this.setText(f.getAbsolutePath());
            }
            dtde.dropComplete(true);
            this.updateUI();
        } else {
            dtde.rejectDrop();
        }
    } catch (IOException ioe) {
        ioe.printStackTrace();
    } catch (UnsupportedFlavorException ufe) {
        ufe.printStackTrace();
    }
}
}
