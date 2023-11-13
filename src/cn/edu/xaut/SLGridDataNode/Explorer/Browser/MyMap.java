/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Explorer.Browser;

/**
 * @author greensubmarine
 */

import java.util.Hashtable;

public class MyMap extends Hashtable {

private String name;

public String getName() {
    return name;
}

public MyMap(String name) {
    super();
    this.name = name;
}

@Override
public String toString() {
    return getName();
}

public void setName(String name) {
    this.name = name;
}
}

