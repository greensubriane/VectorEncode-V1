package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.util.Collection;
import java.util.HashSet;


public class DataMasterImportOptions implements Cloneable {


public enum ImportDestination {InCurrentOntology, InSeparateOntology}

private Collection<? extends Cls> superClses;

private boolean importInCurrOntology;
private boolean importInSepOntology;


protected DataMasterImportOptions clone() {
    try {
        DataMasterImportOptions clone = (DataMasterImportOptions) super.clone();
        clone.superClses = new HashSet<Cls>(superClses);
        return clone;
    } catch (CloneNotSupportedException e) {
        e.printStackTrace();
        return null;
    }
}

DataMasterImportOptions(ConnectInfoPanel importPanel) {
    this.superClses = importPanel.getSuperClasses();

    this.importInCurrOntology = importPanel.importInCurrOntology();
    this.importInSepOntology = importPanel.importInSepOntology();
}

public DataMasterImportOptions(Collection<? extends Cls> superClses, ImportDestination impDest, String clsNamePrefix, String clsNameSuffix, String propNamePrefix, String propNameSuffix) throws InvalidDataMasterOptionsException {

    if (superClses == null || impDest == null) {
        throw new InvalidDataMasterOptionsException("null is not allowed as argument of the DataMasterImportOptions constructor");
    }

    this.setSuperClasses(superClses);

    this.setImportDestination(impDest);

}

public static DataMasterImportOptions createReadOnlyDefault(KnowledgeBase kb) {
    return DataMasterImportOptionsForOwl.createDefault((OWLModel) kb);
}


protected void setSuperClasses(Collection<? extends Cls> superClasses) {
    this.superClses = superClasses;
}

public Collection<? extends Cls> getSuperClasses() {
    return superClses;
}


protected void setImportDestination(ImportDestination impDest) {
    this.importInCurrOntology = (impDest == ImportDestination.InCurrentOntology);
    this.importInSepOntology = (impDest == ImportDestination.InSeparateOntology);
}

public boolean importInCurrOntology() {
    return importInCurrOntology;
}

public boolean importInSepOntology() {
    return importInSepOntology;
}


private String printString(String s) {
    return (s == null ? "null" : "'" + s + "'");
}

}
