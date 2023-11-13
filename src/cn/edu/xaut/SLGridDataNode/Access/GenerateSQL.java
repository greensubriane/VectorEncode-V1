/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.ResultElement;
import cn.edu.xaut.SLGridDataNode.Util.XPathNode;
import cn.edu.xaut.SLGridDataNode.dao.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GenerateSQL {
/*create a new instance of GenerateSQL*/
private String XPath;
private ArrayList<XPathNode> NodeList = new ArrayList<XPathNode>();
private ArrayList<ResultElement> ResultList = new ArrayList<ResultElement>();
private int DocID;
private String XMLView;

public GenerateSQL(String XPath, String XMLView) {
    this.XPath = XPath;
    this.XMLView = XMLView;
}

private String GetViewFormat() {
    return "[" + XMLView + "]";
}

private int GetDocID() {
    ResultSet rs = null;
    String DocSQL = "USE xr_system SELECT DocumentID FROM Document WHERE DocumentName = " + "'" + XMLView + "'";
    try {
        rs = new DBConnection().ExecuteSQL(DocSQL);
        while (rs.next()) {
            DocID = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return DocID;
}

private String SQLExpession(int StepCount) {
    int n = StepCount;
    String XMLStructure = this.GetViewFormat();
    String SQLString = "";
    String SELECTstr = "USE xr_system SELECT DISTINCT " + XMLStructure + ".子元素Pre," + XMLStructure + ".子元素Post," + XMLStructure + ".子元素标记";
    String FROMstr = " FROM " + XMLStructure;
    String WHEREstr = "";
    if (n == 1) {
        WHEREstr = " " + GenerateWhereClause(n);
        SQLString = SELECTstr + FROMstr + WHEREstr;
    } else {
        FROMstr = FROMstr + "," + "(" + SQLExpession(n - 1) + ")" + "AS TempTable" + n;
        WHEREstr = " " + GenerateWhereClause(n);
        SQLString = SELECTstr + FROMstr + WHEREstr;
    }
    return SQLString;
}

private String GenerateWhereClause(int m) {
    int count = m;
    String XMLStructure = this.GetViewFormat();
    String WhereClause = "";
    String TagName;
    String ContextNode;
    int SplitType = NodeList.get(count - 1).GetSplitType();
    switch (SplitType) {
        case 0:
            if (count == 1) {
                TagName = NodeList.get(count - 1).GetStepName().toString();
                WhereClause = "WHERE " + XMLStructure + ".子元素标记=" + "'" + TagName + "'" + " AND " + XMLStructure + ".父元素Pre=0";
            } else {
                TagName = NodeList.get(count - 1).GetStepName().toString();

                WhereClause = "WHERE " + XMLStructure + ".父元素Pre=" + "TempTable" + count + ".子元素Pre" + " AND " + XMLStructure + ".父元素Post=" + "TempTable" + count + ".子元素Post" + " AND " + XMLStructure + ".子元素标记=" + "'" + TagName + "'";
            }
            break;
        case 1:
            if (count == 1) {
                TagName = NodeList.get(count - 1).GetStepName().toString();
                WhereClause = "WHERE " + XMLStructure + ".子元素标记=" + "'" + TagName + "'";
            } else {
                TagName = NodeList.get(count - 1).GetStepName().toString();
                WhereClause = "WHERE " + XMLStructure + ".子元素Pre>" + "TempTable" + count + ".子元素Pre" + " AND " + XMLStructure + ".子元素Post<" + "TempTable" + count + ".子元素Post" + " AND " + XMLStructure + ".子元素标记=" + "'" + TagName + "'";
            }
            break;
    }
    return WhereClause;
}

public void DisplayResult(ArrayList<ResultElement> TempList) {
    new DisplayResult(TempList).BuildResult();
}

public void ExecuteQuery() {
    try {
        NodeList = new AnalysisXPath(XPath + "/").SplitXPath();
    } catch (StringIndexOutOfBoundsException e) {
        e.printStackTrace();
    }
    String SQL = SQLExpession(NodeList.size());
    System.out.println("产生的SQL语句是：" + SQL);
    try {
        ResultSet QueryResultSet = new DBConnection().ExecuteSQL(SQL);
        while (QueryResultSet.next()) {
            ResultList.add(new ResultElement(this.GetDocID(), QueryResultSet.getInt(1), QueryResultSet.getString(3), QueryResultSet.getInt(2)));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    this.DisplayResult(ResultList);
}

}
