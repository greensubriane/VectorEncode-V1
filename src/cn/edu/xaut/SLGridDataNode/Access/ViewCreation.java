package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.dao.DBConnection;

public class ViewCreation {

private String FileName;

/**
 * Creates a new instance of ViewCreation
 */
public ViewCreation(String FileName) {
    this.FileName = FileName;
}

public void CreateView() {
    String SQLString = "USE xr_system SELECT * FROM XMLFileView WHERE 文档名称 = '" + FileName + "'";
    try {
        new DBConnection().ExecuteSQL(SQLString);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   /* public static void main(String[] args)
    {
        new ViewCreation("Issue.xml").CreateView();
    }*/
}
