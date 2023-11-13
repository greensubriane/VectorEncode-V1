/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.Chart;

/**
 * @author Administrator
 */

import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DemoPanel extends JPanel {
List charts = new ArrayList();

public DemoPanel(LayoutManager paramLayoutManager) {
    super(paramLayoutManager);
}

public void addChart(JFreeChart paramJFreeChart) {
    this.charts.add(paramJFreeChart);
}

public JFreeChart[] getCharts() {
    int i = this.charts.size();
    JFreeChart[] arrayOfJFreeChart = new JFreeChart[i];
    for (int j = 0; j < i; j++)
        arrayOfJFreeChart[j] = ((JFreeChart) this.charts.get(j));
    return arrayOfJFreeChart;
}
}
