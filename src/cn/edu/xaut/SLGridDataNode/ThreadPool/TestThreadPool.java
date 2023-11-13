/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.ThreadPool;

/**
 * @author greensubmarine
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestThreadPool {
public static void main(String[] args) {
    try {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        ThreadPoolManager manager = new ThreadPoolManager(10);
        while ((s = br.readLine()) != null) {
            manager.process(s);
        }
    } catch (IOException e) {
    }
}
}
