/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.Chart;

/**
 * @author HeTing
 */

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Random;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_BEVEL;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Color.lightGray;
import static java.awt.Color.white;
import static java.awt.EventQueue.invokeLater;
import static java.text.NumberFormat.getNumberInstance;
import static org.jfree.chart.ChartFactory.createXYLineChart;
import static org.jfree.chart.axis.NumberAxis.createIntegerTickUnits;
import static org.jfree.chart.labels.StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT;
import static org.jfree.chart.plot.PlotOrientation.VERTICAL;

/**
 * @author John B. Matthews
 */
public class JFreeChartDemo extends JFrame {

private static final int MAX = 8;
private static final Random random = new Random();

/**
 * Construct a new frame
 *
 * @param title the frame title
 */
public JFreeChartDemo(String title) {
    super(title);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    final DefaultXYDataset dataset = new DefaultXYDataset();
    //dataset.addSeries("Series0", createSeries(0));
    //dataset.addSeries("Series1", createSeries(1));
    JFreeChart chart = createChart(dataset);
    ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new Dimension(640, 480));
    this.add(chartPanel, CENTER);

    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Add Series");
    buttonPanel.add(addButton);
    addButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int n = dataset.getSeriesCount();
            dataset.addSeries("Series" + n, createSeries(n));
        }
    });
    JButton remButton = new JButton("Remove Series");
    buttonPanel.add(remButton);
    remButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int n = dataset.getSeriesCount() - 1;
            dataset.removeSeries("Series" + n);
        }
    });
    this.add(buttonPanel, SOUTH);
}

/**
 * Create a series
 *
 * @ return the series
 */
private double[][] createSeries(int mean) {
    double[][] series = new double[2][MAX];
    for (int i = 0; i < MAX; i++) {
        series[0][i] = (double) i;
        series[1][i] = mean + random.nextGaussian() / 2;

    }
    return series;
}

/**
 * Create a chart.
 *
 * @param dataset the dataset
 * @return the chart
 */
private JFreeChart createChart(XYDataset dataset) {

    // create the chart...
    JFreeChart chart = createXYLineChart("Serial Data", // chart title
            "Domain", // domain axis label
            "Range", // range axis label
            dataset, // initial series
            VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
    );

    // set chart background
    chart.setBackgroundPaint(white);

    // set a few custom plot features
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.setBackgroundPaint(new Color(0xff_ffe0));
    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlinePaint(lightGray);
    plot.setRangeGridlinePaint(lightGray);

    // set the plot's axes to display integers
    TickUnitSource ticks = createIntegerTickUnits();
    NumberAxis domain = (NumberAxis) plot.getDomainAxis();
    domain.setStandardTickUnits(ticks);
    NumberAxis range = (NumberAxis) plot.getRangeAxis();
    range.setStandardTickUnits(ticks);

    // render shapes and lines
    XYLineAndShapeRenderer renderer =
            new XYLineAndShapeRenderer(true, true);
    plot.setRenderer(renderer);
    renderer.setBaseShapesVisible(true);
    renderer.setBaseShapesFilled(true);

    // set the renderer's stroke
    Stroke stroke = new BasicStroke(
            3f, CAP_ROUND, JOIN_BEVEL);
    renderer.setBaseOutlineStroke(stroke);

    // label the points
    NumberFormat format = getNumberInstance();
    format.setMaximumFractionDigits(2);
    XYItemLabelGenerator generator =
            new StandardXYItemLabelGenerator(
                    DEFAULT_ITEM_LABEL_FORMAT,
                    format, format);
    renderer.setBaseItemLabelGenerator(generator);
    renderer.setBaseItemLabelsVisible(true);

    return chart;
}

/** Main method */
public static void main(String[] args) {
    invokeLater(new Runnable() {
        @Override
        public void run() {
            JFreeChartDemo demo = new JFreeChartDemo("JFreeChartDemo");
            demo.pack();
            demo.setLocationRelativeTo(null);
            demo.setVisible(true);
        }
    });
}
}
 


