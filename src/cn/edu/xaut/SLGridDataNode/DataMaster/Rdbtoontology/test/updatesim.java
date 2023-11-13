package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;


import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;


public class updatesim {
public updatesim() {
    try {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("sim.xml"));

        Node node = document.selectSingleNode("/root/filenameA");//filenameA
        String filenameA = node.getText();

        node = document.selectSingleNode("/root/filenameB");
        String filenameB = node.getText();

        filenameA = filenameA.substring(0, filenameA.length() - 4);
        filenameB = filenameB.substring(0, filenameB.length() - 4);

        node = document.selectSingleNode("/root/nameA");
        String nameA = node.getText();

        node = document.selectSingleNode("/root/nameB");
        String nameB = node.getText();

        node = document.selectSingleNode("/root/sim");
        String sim = node.getText();

        String simfilename = filenameA + filenameB + "result.txt";
        String simendfilename = filenameA + filenameB + "endresult.txt";
        updatefile(simfilename, nameA, nameB, sim);
        String a = "";
        a = a + "1";
        updatefile(simendfilename, nameA, nameB, sim);

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void updatefile(String filename, String nameA, String nameB, String sim) {
    try {
        String line, s = ""; // 用来保存每行读取的内容
        InputStream is = new FileInputStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行

        boolean in = false;
        String[] str = null;
        String simline = "";

        while (line != null && !line.equals("")) {

            str = line.split(";");
            System.out.print(str[0] + "*******\n");
            int index = str[0].lastIndexOf("#");
            System.out.print("*\n");
            index = str[0].lastIndexOf("#");
            System.out.print("***\n");
            String NameA = str[0].substring(index + 1, str[0].length());

            System.out.print(str[1] + "*******\n");
            index = str[1].lastIndexOf("#");
            System.out.print("*\n");
            index = str[1].lastIndexOf("#");
            System.out.print("***\n");
            String NameB = str[1].substring(index + 1, str[1].length());


            if (nameA.equals(NameA) && nameB.equals(NameB)) {
                line = str[0].substring(0, str[0].lastIndexOf("#") + 1) + nameA + ";" + str[1].substring(0, str[1].lastIndexOf("#") + 1) + nameB + ";" + sim + "\n";
                in = true;
                s = s + line + "\n";
            } else {
                s = s + line + "\n";
            }

            line = reader.readLine(); // 读取第一行
        }

        if (!in) {
            simline = str[0].substring(0, str[0].lastIndexOf("#") + 1) + nameA + ";" + str[1].substring(0, str[1].lastIndexOf("#") + 1) + nameB + ";" + sim + "\n";
            s = s + simline + "\n";
        }
        reader.close();

        File f = new File(filename);   //删除旧文件
        f.delete();

        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(s);
        bw.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
}


/*
 				while (line!=null)
				{
					String[] str=line.split(";");

	    			int index=str[0].lastIndexOf("#");
	    			index=str[0].lastIndexOf("#");
					String NameA=str[0].substring(index+1,str[0].length());

					index=str[1].lastIndexOf("#");
	    			index=str[1].lastIndexOf("#");
					String NameB=str[1].substring(index+1,str[1].length());


					if (ontoA.equals(NameA))
					{
						return NameB;
					}

					if (ontoA.equals(NameB))
					{
						return NameA;
					}
		    		line = reader.readLine(); // 读取下一行
				}
  */
