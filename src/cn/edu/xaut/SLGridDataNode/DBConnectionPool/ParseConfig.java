/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.DBConnectionPool;

/**
 * @author greensubmarine
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
//import org.jdom.output.*;

public class ParseConfig {
BaseConnBean bean = new BaseConnBean();
AddPoolBean addpoolbean = new AddPoolBean();

public static void main(String args[]) {
    read("config.xml");
}

public static List<BaseConnBean> read() {
    return read("config.xml");
}

public static List<BaseConnBean> read(String FilePath) {
    FileInputStream fs = null;
    List<BaseConnBean> pools = new ArrayList<BaseConnBean>();
    try {
        fs = new FileInputStream(FilePath);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(fs);
        Element root = doc.getRootElement();
        List child = root.getChildren();

        Iterator allpool = child.iterator();
        while (allpool.hasNext()) {
            Element pool = (Element) allpool.next();
            BaseConnBean bcBean = new BaseConnBean();
            bcBean.setName(pool.getChildText("name"));
            bcBean.setUsername(pool.getChildText("username"));
            bcBean.setPassword(pool.getChildText("password"));
            bcBean.setStandardjdbcurl(pool.getChildText("Standardjdbcurl"));
            bcBean.setJdbcurl(pool.getChildText("jdbcurl"));
            bcBean.setmaxActive(Integer.parseInt(pool.getChildText("maxActive")));
            bcBean.setmaxWait(Integer.parseInt(pool.getChildText("maxwait")));
            bcBean.setDriver(pool.getChildText("driver"));
            bcBean.settestOnBorrow(true);
            bcBean.settestOnResturn(false);
            bcBean.settestwhileIdle(true);
            bcBean.settimeBetweenEvictionRunsMillis(Long.parseLong(pool.getChildText("timeBetweenEvictionRunsMillis")));
            bcBean.setnumTestsPerEvictionRun(Integer.parseInt(pool.getChildText("numTestsPerEvictionRun")));
            bcBean.setminEvictableIdletimeMillis(Long.parseLong(pool.getChildText("minEvictableIdletimeMillis")));
            bcBean.setinitialSize(Integer.parseInt(pool.getChildText("initialSize")));
            bcBean.setmaxIdle(Integer.parseInt(pool.getChildText("maxIdle")));
            pools.add(bcBean);
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.out.println("File Not Found");
    } catch (JDOMException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return pools;
}
}