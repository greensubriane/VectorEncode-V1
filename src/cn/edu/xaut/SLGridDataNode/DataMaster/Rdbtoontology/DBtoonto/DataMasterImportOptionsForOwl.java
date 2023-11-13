package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.util.Collection;
import java.util.Collections;

public class DataMasterImportOptionsForOwl extends DataMasterImportOptions {

public DataMasterImportOptionsForOwl(Collection<? extends OWLClass> superClses, ImportDestination impDest) throws InvalidDataMasterOptionsException {

    super(superClses, impDest, "", "", "", "");

}


public static DataMasterImportOptionsForOwl createDefault(OWLModel owlModel) {
    try {
        return new DataMasterImportOptionsForOwl(
                Collections.singletonList(owlModel.getOWLThingClass()),
                ImportDestination.InCurrentOntology);
    } catch (InvalidDataMasterOptionsException e) {
        e.printStackTrace();
        return null;
    }
}


public void setSuperClasses(Collection<? extends Cls> superClasses) {
    super.setSuperClasses(superClasses);
}

public void setImportDestination(ImportDestination impDest) {
    super.setImportDestination(impDest);
}

}
