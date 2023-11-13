package cn.edu.xaut.SLGridDataNode.Util;

/**
 * @author �greensubmarine��
 */

public class XPathNode {

/** Creates a new instance of XPathNode */
private int SplitType;
private String StepName;

public XPathNode(int SplitType, String StepName) {
    this.SplitType = SplitType;
    this.StepName = StepName;
}

public int GetSplitType() {
    return SplitType;
}

public String GetStepName() {
    return StepName;
}

}
