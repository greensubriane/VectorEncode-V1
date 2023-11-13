/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Access;

/**
 * @author greensubmarine
 */

import cn.edu.xaut.SLGridDataNode.Util.XPathNode;

import java.util.ArrayList;

public class AnalysisXPath {

private String XPath;
private ArrayList SplitNode = new ArrayList();
private ArrayList NodeList = new ArrayList();

/*Create a new instance of AnalysisXPath*/
public AnalysisXPath(String XPath) {
    this.XPath = XPath;
}

public ArrayList SplitXPath() {

    String InputXPath, Temp;
    InputXPath = XPath;
    int SplitFlag;
    String StepName;

    String[] StepArray = InputXPath.split("/");
    int TIndex = 0;
    int[] SplitIndex = new int[StepArray.length + 1];
    for (int x = 0; x < InputXPath.length(); x++) {
        SplitIndex[TIndex++] = x;
    }

    for (int y = 0; y < SplitIndex.length; ) {
        int start = SplitIndex[y];
        int end = SplitIndex[y + 1];
        int length = InputXPath.length();
        if (start == length - 1) {
            break;
        } else {
            Temp = InputXPath.substring(start, end);
            if (Temp.equals("/")) {
                start = SplitIndex[y + 1] + 1;
                end = SplitIndex[y + 2];
                SplitFlag = 1;
                StepName = InputXPath.substring(start, end);
                NodeList.add(new XPathNode(SplitFlag, StepName));
                y += 1;
            } else {
                SplitFlag = 0;
                start = SplitIndex[y] + 1;
                end = SplitIndex[y + 1];
                StepName = InputXPath.substring(start, end);
                NodeList.add(new XPathNode(SplitFlag, StepName));
            }
            y++;
        }
    }
    return NodeList;
}
}
