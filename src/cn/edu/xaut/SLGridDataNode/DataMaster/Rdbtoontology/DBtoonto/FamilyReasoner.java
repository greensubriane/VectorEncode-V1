/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * @author Administrator
 */

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

import java.util.List;

public class FamilyReasoner {
public void InferenceRelation(Resource a, Resource b) {
    Model model = ModelFactory.createDefaultModel();
    model.read("file:/E:/family.owl");
    List rules = Rule.rulesFromURL("file:/E:/family.rules");


    GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
    reasoner.setOWLTranslation(true);
    reasoner.setDerivationLogging(true);
    reasoner.setTransitiveClosureCaching(true);
    OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
    Resource configuration = om.createResource();
    configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

    InfModel inf = ModelFactory.createInfModel(reasoner, om);
    StmtIterator stmtIter = inf.listStatements(a, null, b);
    if (!stmtIter.hasNext()) {
        System.out.println("there is no relation between "
                                   + a.getLocalName() + " and " + b.getLocalName());
        System.out.println("\n-------------------\n");
    }
    while (stmtIter.hasNext()) {
        Statement s = stmtIter.nextStatement();
        System.out.println("Relation between " + a.getLocalName() + " and " + b.getLocalName() + " is :");
        System.out.println(a.getLocalName() + " "
                                   + s.getPredicate().getLocalName() + " " + b.getLocalName());
        System.out.println("\n-------------------\n");
        // System.out.println(s);
    }
}

public static void main(String[] args) {
    FamilyReasoner f = new FamilyReasoner();
    Model m = FileManager.get().loadModel("E:\\family.owl");
    String NS = "http://www.semanticweb.org/ontologies/2010/0/family.owl#";
    Resource Jim = m.getResource(NS + "Jim");
    Resource John = m.getResource(NS + "John");
    Resource Lucy = m.getResource(NS + "Lucy");
    Resource Kate = m.getResource(NS + "Kate");
    Resource Sam = m.getResource(NS + "Sam");
    Resource James = m.createResource(NS + "James");
    f.InferenceRelation(Jim, John);
    f.InferenceRelation(John, Jim);
    f.InferenceRelation(John, Sam);
    f.InferenceRelation(Lucy, John);
    f.InferenceRelation(Kate, Sam);
    f.InferenceRelation(Sam, Kate);
    f.InferenceRelation(James, John);
}
}


