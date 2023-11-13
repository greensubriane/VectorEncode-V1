/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author Greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.dao.DBConnection;
import com.fatdog.xmlEngine.ResultList;
import com.fatdog.xmlEngine.XQEngine;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessQuery {

private String DocumentName;
private String DocURL;
private String XPathExpr;
private XQEngine ThisEngine;
private boolean PRETTYPRINT = true;
private String RunResult;

/** Creates a new instance of ProcessQuery */
public ProcessQuery(String DocumentName, String XPathExpr) {
    this.DocumentName = DocumentName;
    this.XPathExpr = XPathExpr;
}

private String GetDocURL() {

    ResultSet rs = null;
    String SQLString = "USE gmldatabase SELECT storagedpath FROM Document WHERE documentname =" + "'" + DocumentName + "'";
    try {
        rs = new DBConnection().ExecuteSQL(SQLString);
        while (rs.next()) {
            DocURL = rs.getString(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return DocURL;
}

public String RunQuery() {
    ThisEngine = new XQEngine();
    try {
        installSunXMLReader();
        ThisEngine.setDocument(this.GetDocURL());
        ResultList results = ThisEngine.setQuery(XPathExpr);
        RunResult = results.emitXml(PRETTYPRINT) + "\n";
    } catch (Exception e) {
        e.printStackTrace();
    }
    return RunResult;
}

private void installXercesXMLReader() throws Exception {
    String parserName = "org.apache.xerces.parsers.SAXParser";
    XMLReader parser = XMLReaderFactory.createXMLReader(parserName);

    ThisEngine.setXMLReader(parser);
}

private void installSunXMLReader() throws Exception {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    try {
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();

        ThisEngine.setXMLReader(reader);
    } catch (Exception e) {
        throw e;
    }
}



   /* public static void main(String[] args)
    {
        System.out.println(new ProcessQuery("pub.xml","//author").RunQuery());
    }*/

}


