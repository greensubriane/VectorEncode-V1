/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.VectorEncoding;

/**
 * @author greensubmarine
 */

import java.util.HashMap;
import java.util.Map;

public class VectorEncodingFunctions {

public static Map<Integer, int[]> GetDietzVectorMap(int counts) {
    Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    int[][] code = new int[2][counts];
    code[0][0] = 1;
    code[1][0] = 0;
    code[0][counts - 1] = 0;
    code[1][counts - 1] = 1;
    VectorDietzencode(code, 0, counts - 1);
    result = VectorDietzEncodeResult(code);
    return result;
}

public static Map<Integer, int[]> VectorDietzEncodeResult(int[][] code) {
    Map<Integer, int[]> coderesult = new HashMap<Integer, int[]>();
    int rows = code.length;
    int cols = code[0].length;
    int[][] transcode = new int[cols][rows];
    for (int i = 0; i < cols; i++) {
        for (int j = 0; j < rows; j++) {
            transcode[i][j] = code[j][i];
        }
    }
    for (int n = 0; n < transcode.length; n++) {
        int[] codearray = new int[2];
        codearray[0] = transcode[n][0];
        codearray[1] = transcode[n][1];
        coderesult.put(n + 1, codearray);
    }
    return coderesult;
}

public static void VectorDietzencode(int[][] code, int start, int end) {
    //这里的code为二维矩阵，用于存放文档的元素的向量编码
    //执行取整运算
    int m = (int) Math.floor((start + end) / 2);
    if ((m < end) && (m > start)) {
        code[0][m] = code[0][start] + code[0][end];
        code[1][m] = code[1][start] + code[1][end];
        VectorDietzencode(code, start, m);
        VectorDietzencode(code, m, end);
    } else {
        return;
    }
}

}
