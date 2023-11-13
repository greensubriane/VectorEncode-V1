/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author Administrator
 */

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class C3P0ConfigFileParsing {

C3P0Bean bean = new C3P0Bean();
//AddPoolBean addpoolbean = new AddPoolBean();

public static void main(String args[]) {
    read("c3p0-config.xml");
}

public static List<C3P0Bean> read() {
    return read("c3p0-config.xml");
}

public static List<C3P0Bean> read(String FilePath) {
    FileInputStream fs = null;
    List<C3P0Bean> pools = new ArrayList<>();
    try {
        fs = new FileInputStream(FilePath);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(fs);
        Element root = doc.getRootElement();
        List child = root.getChildren();
        Iterator allpool = child.iterator();
        while (allpool.hasNext()) {
            Element pool = (Element) allpool.next();
            C3P0Bean bcBean = new C3P0Bean();
            bcBean.setPoolName(pool.getChildText("name"));
            bcBean.setUser(pool.getChildText("user"));
            bcBean.setPassword(pool.getChildText("password"));
            bcBean.setjdbcUrl(pool.getChildText("jdbcUrl"));
            bcBean.setdriverClass(pool.getChildText("driverClass"));
            bcBean.setAcquireIncrement(parseInt(pool.getChildText("acquireIncrement")));
            bcBean.setinitialPoolSize(parseInt(pool.getChildText("initialPoolSize")));
            bcBean.setminPoolSize(parseInt(pool.getChildText("minPoolSize")));
            bcBean.setmaxPoolSize(parseInt(pool.getChildText("maxPoolSize")));
            bcBean.setmaxStatements(parseInt(pool.getChildText("maxStatements")));
            bcBean.setStatementsperConnection(parseInt(pool.getChildText("maxStatementsPerConnection")));
            pools.add(bcBean);
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        out.println("File Not Found");
    } catch (JDOMException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return pools;
}

}
