package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

abstract class AbstractDBImporter implements DBImporter {

protected KnowledgeBase kb;
protected OWLModel owlModel;

protected String dsURL;
protected String schemaName;

protected Collection<Cls> superClses;

protected boolean importInCurrOntology;
protected boolean importInSepOntology;
protected boolean useDiffNamespaces;
protected boolean inclTableNameInColName;
protected int columnType;

protected String tableClassNamePrefix;
protected String tableClassNameSuffix;
protected String columnPropertyNamePrefix;
protected String columnPropertyNameSuffix;

protected static final boolean ANNOTATION_PROPERTY = true;
protected static final boolean NORMAL_PROPERTY = !ANNOTATION_PROPERTY;
protected static final boolean FUNCTIONAL = true;
static final boolean NON_FUNCTIONAL = !FUNCTIONAL;

protected static int instanceCount = 1;

protected String nsPrefix = "";
protected String nsPrefixRelOwl;


@SuppressWarnings("unchecked")
public void init(KnowledgeBase kb, DataMasterConnectionOptions connOptions, DataMasterImportOptions impOptions) {
    this.kb = kb;
    if (kb instanceof OWLModel) {
        this.owlModel = (OWLModel) kb;
    } else {
        this.owlModel = null;
    }

    dsURL = Global.convertJdbcUrlToOwlNamespaceUrl(connOptions.getDataSourceURL(), Global.URL_Conversion.EliminateAllColons);
    schemaName = connOptions.getSchemaName();
    superClses = (Collection<Cls>) impOptions.getSuperClasses();
    importInCurrOntology = impOptions.importInCurrOntology();
    importInSepOntology = impOptions.importInSepOntology();

    if (owlModel == null) {
        nsPrefixRelOwl = "";
    } else {
        nsPrefixRelOwl = getPrefixForNamespace_RelationalOWL();
    }
}

public String getResourceNameForTable(String tableName) {
    if (owlModel == null) {
        return getFramesNameForTable(tableName);
    } else {
        return getOWLNameForTable(tableName);
    }
}

protected String getFramesNameForTable(String tableName) {
    return tableClassNamePrefix + tableName;
}

protected String getOWLNameForTable(String tableName) {
    return nsPrefix + Global.replaceInvalidProtegeCharacters(tableName);
}

protected String getPrefixForNamespace_TableClasses() {
    if (importInCurrOntology) {
        if (useDiffNamespaces) {
            return getPrefixForNamespace(Global.NAMESPACE_TABLE_CLASSES + Global.NAMESPACE_BIND_DSN + dsURL + "#") + ":";
        } else {
            return "";
        }
    } else {
        //TODO see how do we deal with this case!!!!
        return "";
    }
}

protected String getPrefixForNamespace_TableInstances() {
    if (importInCurrOntology) {
        if (useDiffNamespaces) {
            return getPrefixForNamespace(Global.NAMESPACE_TABLE_INSTANCES + Global.NAMESPACE_BIND_DSN + dsURL + "#") + ":";
        } else {
            return "";
        }
    } else {
        //TODO see how do we deal with this case!!!!
        return "";
    }
}

protected String getPrefixForNamespace_TableClassesAndInstances() {
    if (importInCurrOntology) {
        if (useDiffNamespaces) {
            return getPrefixForNamespace(Global.NAMESPACE_TABLE_CLASSES_AND_INSTANCES + Global.NAMESPACE_BIND_DSN + dsURL + "#") + ":";
        } else {
            return "";
        }
    } else {
        //TODO see how do we deal with this case!!!!
        return "";
    }
}

protected String getPrefixForNamespace_RelationalOWL() {
    return getPrefixForNamespace(Global.NAMESPACE_RELATIONAL_OWL, "dbs") + ":";
}

protected String getPrefixForNamespace(String namespace) {
    return getPrefixForNamespace(namespace, "db");
}

protected String getPrefixForNamespace(String namespace, final String preferred_prefix_base) {
    final String prefix_base = preferred_prefix_base;
    NamespaceManager nsmgr = owlModel.getNamespaceManager();

    String prefix = nsmgr.getPrefix(namespace);
    if (prefix != null)
        return prefix;
    prefix = prefix_base;
    int prefix_ind = 1;
    while (nsmgr.getNamespaceForPrefix(prefix) != null) {
        prefix = prefix_base + prefix_ind++;
    }
    nsmgr.setPrefix(namespace, prefix);
    return prefix;
}

protected String getColumnNamePrefix(String strTableName) {
    if (inclTableNameInColName)
        return strTableName + ".";
    else
        return "";
}

protected OWLDatatypeProperty createDatatypePropertySafe(String propertyName, RDFSDatatype datatype, boolean isAnnotationProperty, boolean isFunctional) {
    OWLDatatypeProperty property = owlModel.getOWLDatatypeProperty(propertyName);

    if (property == null) {
        if (isAnnotationProperty)
            property = owlModel.createAnnotationOWLDatatypeProperty(propertyName);
        else
            property = owlModel.createOWLDatatypeProperty(propertyName, datatype);
        property.setRange(datatype);
        property.setFunctional(isFunctional);
    } else if (!datatype.equals(property.getRangeDatatype())) {
        Global.debug("WARNING! " + propertyName + " property is already defined with the wrong type.");
    }

    return property;
}

protected OWLObjectProperty createObjectPropertySafe(String propertyName, Collection allowedClasses, boolean isAnnotationProperty, boolean isFunctional) {
    OWLObjectProperty property = owlModel.getOWLObjectProperty(propertyName);

    if (property == null) {
        if (isAnnotationProperty)
            property = owlModel.createAnnotationOWLObjectProperty(propertyName);
        else
            property = owlModel.createOWLObjectProperty(propertyName, allowedClasses);
        property.setRanges(allowedClasses);
        property.setFunctional(isFunctional);
    } else if (!property.getRanges(false).containsAll(allowedClasses)) {
        Global.debug("WARNING! " + propertyName + " property is already defined with different range specification.");
    }

    return property;
}

protected OWLObjectProperty createObjectPropertySafe(String propertyName, OWLObjectProperty superProperty,
                                                     Collection allowedClasses, boolean isAnnotationProperty, boolean isFunctional) {

    OWLObjectProperty property = createObjectPropertySafe(propertyName, allowedClasses, isAnnotationProperty, isFunctional);
    if (!property.isSubpropertyOf(superProperty, false)) {
        property.addSuperproperty(superProperty);
    }
    return property;
}


protected OWLNamedClass createOWLClassSafe(String className) {
    OWLNamedClass owlClass = owlModel.getOWLNamedClass(className);

    if (owlClass == null) {
        return owlModel.createOWLNamedClass(className);
    }// else if (owlClass.getRDFType() != datatype) {
    //	Global.debug("WARNING! " + className + " property is already defined with the wrong type.");
    //}

    return owlClass;
}

protected RDFResource createOWLIndividualSafe(OWLClass typeClass, String individualName) {
    RDFResource owlIndividual = owlModel.getRDFResource(individualName);

    if (owlIndividual == null) {
        try {
            return typeClass.createInstance(individualName);
        } catch (Exception e) {
            Global.debug("WARNING! Exception by creating individual '" + individualName + "': Protege instance name is already in use! This may cause further NULL POINTER EXCEPTION.");
            e.printStackTrace();
            return null;
        }
    } else {
        if (!owlIndividual.getRDFTypes().contains(typeClass)) {
            Global.debug("WARNING! " + individualName + " individual is already defined with the wrong type!");
            owlIndividual.addRDFType(typeClass);
        }
    }

    return owlIndividual;
}

protected Instance createInstanceSafe(Cls typeClass, String instanceName) {
    Instance inst = kb.getInstance(instanceName);

    if (inst == null) {
        try {
            return kb.createInstance(instanceName, typeClass);
        } catch (Exception e) {
            Global.debug("WARNING! Exception by creating instance '" + instanceName + "': Protege instance name is already in use! This may cause further NULL POINTER EXCEPTION.");
            e.printStackTrace();
            return null;
        }
    } else {
        if (!inst.hasType(typeClass)) {
            Global.debug("WARNING! " + instanceName + " instance is already defined with the wrong type!");
            inst.addDirectType(typeClass);
        }
    }

    return inst;
}

// Returns true if String s is in collection c.
protected boolean contains(Collection<String> c, String s) {
    Iterator iter = c.iterator();
    while (iter.hasNext()) {
        if (iter.next().equals(s))
            return true;
    }

    return false;
}


/**
 * This method creates a new slot in "thisCls" named "name", whose value is of type "allowedCls"
 *
 * @param thisCls
 * @param name
 * @param allowedCls
 * @param allowMult
 * @return
 */
protected Slot generateSlot(Cls thisCls, String name, Cls allowedCls, boolean allowMult) {
    Slot newSlot = this.kb.getSlot(name);
    if (newSlot == null)
        newSlot = this.kb.createSlot(name);
    newSlot.setValueType(ValueType.INSTANCE);
    Collection<Cls> classHolder = Collections.singletonList(allowedCls);
    newSlot.setAllowedClses(classHolder);
    newSlot.setAllowsMultipleValues(allowMult);
    thisCls.addDirectTemplateSlot(newSlot);

    return newSlot;
}

/**
 * This method scans a list of instances for one where the value of slotName matches id.
 * changed because the keys may have types other than Integer (e.g. String)
 * private Instance getMatchingInstance(Collection instances, String slotName, int id)
 *
 * @param instances
 * @param slotName
 * @param id
 * @return
 */
protected Instance getMatchingInstance(Collection<Instance> instances, String slotName, Object id) {
    Slot s = this.kb.getSlot(slotName);
    Iterator iter = instances.iterator();
    while (iter.hasNext()) {
        Instance inst = (Instance) iter.next();
        Object val = inst.getOwnSlotValue(s);
        if (val != null && val.equals(id))
            return inst;
    }

    return null;
}


/**
 * This method creates a new object property in "thisCls" named "name", whose value is of type "allowedCls"
 *
 * @param thisCls
 * @param name
 * @param allowedClass
 * @param allowMult
 * @return
 */
protected OWLObjectProperty generateProperty(OWLClass thisCls, String name, OWLClass allowedClass, boolean allowMult) {
    OWLObjectProperty newProp = createObjectPropertySafe(name, Collections.singletonList(allowedClass), NORMAL_PROPERTY, (!allowMult));

    boolean hasOnlyThingDomain = newProp.getDomains(false).size() == 1 && newProp.getDomain(false).equals(owlModel.getOWLThingClass());
    if (hasOnlyThingDomain)
        newProp.setDomain(thisCls);
    else
        newProp.addUnionDomainClass(thisCls);

    return newProp;
}

/**
 * This method scans a list of OWL individuals for one where the value of slotName matches id.
 *
 * @param instances
 * @param prop
 * @param id
 * @return
 */
protected OWLIndividual getMatchingIndividual(Collection instances, OWLProperty prop, Object id) {
    Iterator iter = instances.iterator();
    while (iter.hasNext()) {
        OWLIndividual inst = (OWLIndividual) iter.next();
        Object val = inst.getPropertyValue(prop);
        if (val != null && val.equals(id))
            return inst;
    }

    return null;
}

}
