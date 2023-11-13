/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.DBtoonto;

/**
 * @author Administrator
 */

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.rdql.ResultBinding;
import com.hp.hpl.jena.reasoner.rulesys.OWLFBRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.OWLMiniReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

import java.util.ArrayList;
import java.util.List;

public class QueryFactory {
private static OntModel ontModel;
private static OWLMiniReasoner reasoner2;
private static InfModel inf;
private static Query query;

//定义需要加入推理机中的规则：985高校是211高校，211高校是一本高校
private static String rule = "[rule1:(?x http://www.owl-ontologies.com/Ontology1276145714.owl#rank_is http://www.owl-ontologies.com/Ontology1276145714.owl#u211) " +
                                     "->(?x http://www.owl-ontologies.com/Ontology1276145714.owl#rank_is http://www.owl-ontologies.com/Ontology1276145714.owl#一本)]" +
                                     "[rule2:(?x http://www.owl-ontologies.com/Ontology1276145714.owl#rank_is http://www.owl-ontologies.com/Ontology1276145714.owl#u985) " +
                                     "->(?x http://www.owl-ontologies.com/Ontology1276145714.owl#rank_is http://www.owl-ontologies.com/Ontology1276145714.owl#u211)]";
private static String rules = "[rule1: (?a fa:hasHusband ?b)(?a fa:isMotherOf ?c)->(?b fa:isFatherOf ?c)][rule2: (?a fa:hasHusband ?b)(?a fa:hasDaughter ?c)->(?b fa:isFartherOf ?c)][rule3: (?a fa:hasHusband ?b)(?a fa:hasDaughter ?c)->(?b fa:hasDaughter ?c)][rule4: (?a fa:hasHusband ?b)(?a fa:hasDaughter ?c)->(?c fa:hasFather ?b)][rule5: (?a fa:hasHusband ?b)(?a fa:hasDaughter ?c)->(?c fa:isDaughterOf ?b)][rule6: (?a fa:hasHusband ?b)(?a fa:hasSon ?c)->(?b fa:isFatherOf ?c)][rule7: (?a fa:hasHusband ?b)(?a fa:hasSon ?c)->(?b fa:hasSon ?c)][rule8: (?a fa:hasHusband ?b)(?a fa:hasSon ?c)->(?c fa:hasFather ?b)][rule9: (?a fa:hasHusband ?b)(?a fa:hasSon ?c)->(?c fa:isSonOf ?b)]" +
                                      "[rule10:(?a fa:hasWife ?b)(?a fa:isFatherOf ?c)->(?b fa:isMotherOf ?c)][rule11:(?a fa:hasWife ?b)(?a fa:haDaughter ?c)->(?b fa:isMotherOf ?c)][rule12:(?a fa:hasWife ?b)(?a fa:haDaughter ?c)->(?b fa:hasDaugher ?c)][rule13:(?a fa:hasWife ?b)(?a fa:haDaughter ?c)->(?c fa:hasMother ?b)][rule14:(?a fa:hasWife ?b)(?a fa:haDaughter ?c)->(?c fa:isDaughterOf ?b)][rule15:(?a fa:hasWife ?b)(?a fa:hasSon ?c)->(?b fa:isMotherOf ?c)][rule16:(?a fa:hasWife ?b)(?a fa:hasSon ?c)->(?b fa:hasSon ?c)][rule17:(?a fa:hasWife ?b)(?a fa:hasSon ?c)->(?c fa:hasMother ?b)][rule18:(?a fa:hasWife ?b)(?a fa:hasSon ?c)->(?c fa:isSonOf ?b)]" +
                                      "[rule19:(?a fa:hasWife ?b)->(?a fa:isHusbandOf ?b)][rule20:(?a fa:hasHusband ?b)->(?a fa:isWifeOf ?b)][rule21:(?a fa:isHusbandOf ?b)->(?a fa:hasWife ?b)][rule22:(?a fa:isWifeOf ?b)->(?a fa:hasHusband ?b)]" +
                                      "[rule23:(?a fa:hasFather ?b) (?c fa:hasFather ?d) (?b fa:hasBrother ?d) notEqual(?b,?d)->(?a fa:hasCousin ?b)][rule24:(?a fa:hasFather ?b)(?c fa:hasFather ?d)(?b fa:hasFather ?e)(?d fa:hasFather ?e) notEqual(?b,?d)->(?a fa:hasCousin ?c)]" +
                                      "[rule25:(?a fa:hasSon ?b)(?b fa:hasSon ?c)->(?a fa:hasGrandSon ?c)][rule26:(?a fa:hasSon ?b)(?b fa:hasDaughter ?c)->(?a fa:hasGrandDaughter ?c)]" +
                                      "[rule27:(?a fa:hasFather ?b)(?b fa:hasBrother ?c)->(?a fa:hasUncle ?c)][rule28:(?a fa:hasFather ?b)(?b fa:hasSister ?c)->(?a fa:hasAunt ?c)]" +
                                      "[rule29:(?a fa:hasGrandSon ?b)->(?b fa:isGrandSonOf ?a)][rule30:(?a fa:hasAunt ?b)->(?b fa:isAuntOf ?a)][rule31:(?a fa:hasFather ?b)->(?b fa:isFatherOf ?a)][rule32:(?a fa:hasMother ?b)->(?b fa:isMotherOf ?a)][rule33:(?a fa:hasBrother ?b)->(?b fa:isBrotherOf ?a)][rule34:(?a fa:hasSister ?b)->(?b fa:isSisterOf ?b)][rule35:(?a fa:hasCousin ?b)->(?b fa:isCousinOf ?a)][rule36:(?a fa:hasSon ?b)->(?b fa:isSonOf ?a)][rule37:(?a fa:hasDaughter ?b)->(?b fa:isDaughterOf ?a)][rule38:(?a fa:hasGrandDaughter ?b)->(?b fa:isGrandDaughterOf ?a)][rule39:(?a fa:hasWife ?b)->(?b fa:isWifeOf ?a)][rule40:(?a fa:hasHusband ?b)->(?b fa:isHusbandOf ?a)][rule41:(?a fa:hasUncle ?b)->(?b fa:isUncleOf ?a)]";

static {
    ontModel = ModelFactory.createOntologyModel();
    ontModel.read("file:/E:/family.owl");
    reasoner2 = new OWLMiniReasoner(new OWLFBRuleReasonerFactory());
    reasoner2.addRules(Rule.parseRules(rules));
    inf = ModelFactory.createInfModel(reasoner2, ontModel);

}

@SuppressWarnings("deprecation")


//传入查询的语句，返回相关结果
public static List<String> query(String statement) {
    List<String> rs = new ArrayList<String>();

    query = new Query(statement);
    // Set the model to run the query against
    query.setSource(inf);


    // Use the query to create a query engine
    QueryEngine qe = new QueryEngine(query);
    // Use the query engine to execute the query
    QueryResults results = qe.exec();
    while (results.hasNext()) {
        ResultBinding binding = (ResultBinding) results.next();
        Resource concept = (Resource) binding.get("x");
        String cur = concept.getLocalName();
        rs.add(cur);
        //System.out.println(concept.getLocalName());
    }

    return rs;
}
}


