/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MapViewofStation.java
 *
 * Created on 2011-8-30, 22:28:07
 */

package cn.edu.xaut.GeoTool;

/**
 * @author Administrator
 */
public class MapViewofStation extends javax.swing.JFrame {

/**
 * Creates new form MapViewofStation
 */
public MapViewofStation() {
    initComponents();
}

/**
 * This method is called from within the constructor to
 * initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is
 * always regenerated by the Form Editor.
 */
@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    jSeparator2 = new javax.swing.JSeparator();
    jSeparator1 = new javax.swing.JSeparator();
    jSplitPane1 = new javax.swing.JSplitPane();
    jLabel1 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox();
    jSplitPane2 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jSeparator3 = new javax.swing.JSeparator();
    jMapPane1 = new org.geotools.swing.JMapPane();
    jToolBar1 = new javax.swing.JToolBar();
    statusBar1 = new org.geotools.swing.StatusBar();
    mapLayerTable1 = new org.geotools.swing.MapLayerTable();

    jLabel2.setText(org.openide.util.NbBundle.getMessage(MapViewofStation.class, "MapViewofStation.jLabel2.text")); // NOI18N
    jLabel2.setName("jLabel2"); // NOI18N

    jSeparator2.setName("jSeparator2"); // NOI18N

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setName("Form"); // NOI18N

    jSeparator1.setName("jSeparator1"); // NOI18N

    jSplitPane1.setName("jSplitPane1"); // NOI18N

    jLabel1.setText(org.openide.util.NbBundle.getMessage(MapViewofStation.class, "MapViewofStation.jLabel1.text")); // NOI18N
    jLabel1.setName("jLabel1"); // NOI18N

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
    jComboBox1.setName("jComboBox1"); // NOI18N

    jSplitPane2.setName("jSplitPane2"); // NOI18N

    jPanel2.setName("jPanel2"); // NOI18N

    jSeparator3.setName("jSeparator3"); // NOI18N

    jMapPane1.setName("jMapPane1"); // NOI18N

    javax.swing.GroupLayout jMapPane1Layout = new javax.swing.GroupLayout(jMapPane1);
    jMapPane1.setLayout(jMapPane1Layout);
    jMapPane1Layout.setHorizontalGroup(
            jMapPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
    );
    jMapPane1Layout.setVerticalGroup(
            jMapPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 282, Short.MAX_VALUE)
    );

    jToolBar1.setRollover(true);
    jToolBar1.setName("jToolBar1"); // NOI18N

    statusBar1.setName("statusBar1"); // NOI18N

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
                                      .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addComponent(statusBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jMapPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                      .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                                      .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jMapPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(statusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                      .addContainerGap())
    );

    jSplitPane2.setRightComponent(jPanel2);

    mapLayerTable1.setName("mapLayerTable1"); // NOI18N
    jSplitPane2.setLeftComponent(mapLayerTable1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                                      .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                          .addComponent(jLabel1)
                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                          .addComponent(jComboBox1, 0, 1049, Short.MAX_VALUE))
                                                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1107, Short.MAX_VALUE)
                                                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1107, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                                        .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    pack();
}// </editor-fold>//GEN-END:initComponents

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new MapViewofStation().setVisible(true);
        }
    });
}

// Variables declaration - do not modify//GEN-BEGIN:variables
private javax.swing.JComboBox jComboBox1;
private javax.swing.JLabel jLabel1;
private javax.swing.JLabel jLabel2;
private org.geotools.swing.JMapPane jMapPane1;
private javax.swing.JPanel jPanel2;
private javax.swing.JSeparator jSeparator1;
private javax.swing.JSeparator jSeparator2;
private javax.swing.JSeparator jSeparator3;
private javax.swing.JSplitPane jSplitPane1;
private javax.swing.JSplitPane jSplitPane2;
private javax.swing.JToolBar jToolBar1;
private org.geotools.swing.MapLayerTable mapLayerTable1;
private org.geotools.swing.StatusBar statusBar1;
// End of variables declaration//GEN-END:variables

}
