/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding.Util;

/**
 * @author greensubmarine
 */
public class TempNodeVectorDietzEncode {
private String TempVectorDietzPreEncode;
private String TempVectorDietzPostEncode;

public TempNodeVectorDietzEncode(String TempVectorDietzPreEncode, String TempVectorDietzPostEncode) {
    this.TempVectorDietzPreEncode = TempVectorDietzPreEncode;
    this.TempVectorDietzPostEncode = TempVectorDietzPostEncode;
}

public String GetTempVectorDietzPreEncode() {
    return TempVectorDietzPreEncode;
}

public String GetTempVectorDietzPostEncode() {
    return TempVectorDietzPostEncode;
}

}
