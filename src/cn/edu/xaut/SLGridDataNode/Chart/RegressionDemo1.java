/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.Chart;

/**
 * @author Administrator
 */

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;

import static java.awt.Color.blue;
import static org.jfree.chart.JFreeChart.DEFAULT_TITLE_FONT;
import static org.jfree.data.general.DatasetUtilities.sampleFunction2D;
import static org.jfree.data.statistics.Regression.getOLSRegression;
import static org.jfree.data.statistics.Regression.getPowerRegression;
import static org.jfree.ui.RefineryUtilities.centerFrameOnScreen;

public class RegressionDemo1 extends ApplicationFrame {
public RegressionDemo1(String paramString) {
    super(paramString);
    JPanel localJPanel = createDemoPanel();
    getContentPane().add(localJPanel);
}

public static JPanel createDemoPanel() {
    return new DemoPanel();
}

public static void main(String[] paramArrayOfString) {
    RegressionDemo1 localRegressionDemo1 = new RegressionDemo1("Regression Demo 1");
    localRegressionDemo1.pack();
    centerFrameOnScreen(localRegressionDemo1);
    localRegressionDemo1.setVisible(true);
}

static class DemoPanel extends JPanel {
    private XYDataset data1 = createSampleData1();

    public DemoPanel() {
        super();
        add(createContent());
    }

    private XYDataset createSampleData1() {
        XYSeries localXYSeries = new XYSeries("Series 1");
        localXYSeries.add(2.0D, 56.270000000000003D);
        localXYSeries.add(3.0D, 41.32D);
        localXYSeries.add(4.0D, 31.449999999999999D);
        localXYSeries.add(5.0D, 30.050000000000001D);
        localXYSeries.add(6.0D, 24.690000000000001D);
        localXYSeries.add(7.0D, 19.780000000000001D);
        localXYSeries.add(8.0D, 20.940000000000001D);
        localXYSeries.add(9.0D, 16.73D);
        localXYSeries.add(10.0D, 14.210000000000001D);
        localXYSeries.add(11.0D, 12.44D);
        XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection(localXYSeries);
        return localXYSeriesCollection;
    }

    private JTabbedPane createContent() {
        JTabbedPane localJTabbedPane = new JTabbedPane();
        localJTabbedPane.add("Linear", createChartPanel1());
        localJTabbedPane.add("Power", createChartPanel2());
        return localJTabbedPane;
    }

    private ChartPanel createChartPanel1() {
        NumberAxis localNumberAxis1 = new NumberAxis("X");
        localNumberAxis1.setAutoRangeIncludesZero(false);
        NumberAxis localNumberAxis2 = new NumberAxis("Y");
        localNumberAxis2.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer localXYLineAndShapeRenderer1 = new XYLineAndShapeRenderer(false, true);
        XYPlot localXYPlot = new XYPlot(this.data1, localNumberAxis1, localNumberAxis2, localXYLineAndShapeRenderer1);
        double[] arrayOfDouble = getOLSRegression(this.data1, 0);
        LineFunction2D localLineFunction2D = new LineFunction2D(arrayOfDouble[0], arrayOfDouble[1]);
        XYDataset localXYDataset = sampleFunction2D(localLineFunction2D, 2.0D, 11.0D, 100, "Fitted Regression Line");
        localXYPlot.setDataset(1, localXYDataset);
        XYLineAndShapeRenderer localXYLineAndShapeRenderer2 = new XYLineAndShapeRenderer(true, false);
        localXYLineAndShapeRenderer2.setSeriesPaint(0, blue);
        localXYPlot.setRenderer(1, localXYLineAndShapeRenderer2);
        JFreeChart localJFreeChart = new JFreeChart("Linear Regression", DEFAULT_TITLE_FONT, localXYPlot, true);
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart, false);
        return localChartPanel;
    }

    private ChartPanel createChartPanel2() {
        NumberAxis localNumberAxis1 = new NumberAxis("X");
        localNumberAxis1.setAutoRangeIncludesZero(false);
        NumberAxis localNumberAxis2 = new NumberAxis("Y");
        localNumberAxis2.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer localXYLineAndShapeRenderer1 = new XYLineAndShapeRenderer(false, true);
        XYPlot localXYPlot = new XYPlot(this.data1, localNumberAxis1, localNumberAxis2, localXYLineAndShapeRenderer1);
        double[] arrayOfDouble = getPowerRegression(this.data1, 0);
        PowerFunction2D localPowerFunction2D = new PowerFunction2D(arrayOfDouble[0], arrayOfDouble[1]);
        XYDataset localXYDataset = sampleFunction2D(localPowerFunction2D, 2.0D, 11.0D, 100, "Fitted Regression Line");
        XYLineAndShapeRenderer localXYLineAndShapeRenderer2 = new XYLineAndShapeRenderer(true, false);
        localXYLineAndShapeRenderer2.setSeriesPaint(0, blue);
        localXYPlot.setDataset(1, localXYDataset);
        localXYPlot.setRenderer(1, localXYLineAndShapeRenderer2);
        JFreeChart localJFreeChart = new JFreeChart("Power Regression", DEFAULT_TITLE_FONT, localXYPlot, true);
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart, false);
        return localChartPanel;
    }
}
}
