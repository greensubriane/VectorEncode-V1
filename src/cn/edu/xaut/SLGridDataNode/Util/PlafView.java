package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

import javax.swing.*;

public class PlafView {

/** Creates a new instance of PlafView */
public PlafView() {
}

public void ChangeView() {
    try {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        //UIManager.setLookAndFeel(new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel());
        //UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
        //com.birosoft.liquid.LiquidLookAndFeel.setLiquidDecorations(true);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
