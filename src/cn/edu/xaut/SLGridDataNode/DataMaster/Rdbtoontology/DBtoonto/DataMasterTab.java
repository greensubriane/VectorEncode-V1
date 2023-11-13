package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import cn.edu.xaut.SLGridDataNode.Util.PlafView;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;


public class DataMasterTab {

private static final long serialVersionUID = 1L;
private OWLModel owlModel = null;
private JFrame frame = new JFrame();
private ConnectInfoPanel connectInfoPanel;

public DataMasterTab() {
    try {
        owlModel = ProtegeOWL.createJenaOWLModel();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

public static void main(String[] args) {
    DataMasterTab datatab = new DataMasterTab();
    datatab.initialize();

}

public void initialize() {
    new PlafView().ChangeView();
    try {
        KnowledgeBase kb = (KnowledgeBase) owlModel;
        DataPreviewPanel previewPanel = new DataPreviewPanel();
        connectInfoPanel = new ConnectInfoPanel(previewPanel, kb);
        //setBounds((ScreenSize.getWidth()-getWidth())/2, (ScreenSize.getHeight()-getHeight())/2, getWidth(), getHeight());

        frame.getContentPane().add(connectInfoPanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(1145, 800);
        frame.setLocation(50, 150);
        frame.setResizable(true);
        frame.setTitle("本体自动生成-数据导入");
        frame.setVisible(true);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

}

