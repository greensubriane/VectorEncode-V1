package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.ui.SelectClsesPanel;

import java.util.ArrayList;
import java.util.Collection;

class SuperClsPicker extends SelectClsesPanel {

public SuperClsPicker(final KnowledgeBase kb) {
    super(kb, new ArrayList(), true);
    //setMaximumSize(new Dimension(150,150));
}

@SuppressWarnings("unchecked")
public Collection<Cls> getSelection() {
    Collection<Cls> clses;
    	/*
        SelectClsesPanel p = new SelectClsesPanel(kb, rootClses, multiple);
        int result = ModalDialog.showDialog(component, p, label, ModalDialog.MODE_OK_CANCEL);
        if (result == ModalDialog.OPTION_OK) {
            clses = p.getSelection();
        } else {
            clses = Collections.EMPTY_LIST;
        }
        */
    clses = super.getSelection();
    return clses;
}

}
