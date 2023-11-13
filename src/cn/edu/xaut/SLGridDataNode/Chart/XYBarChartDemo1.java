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
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static java.awt.BorderLayout.CENTER;
import static java.awt.Color.lightGray;
import static java.awt.Color.white;
import static java.lang.Class.forName;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.System.err;
import static java.sql.DriverManager.getConnection;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static org.jfree.chart.ChartFactory.createXYBarChart;
import static org.jfree.chart.axis.DateTickMarkPosition.MIDDLE;
import static org.jfree.chart.plot.PlotOrientation.VERTICAL;
import static org.jfree.ui.RefineryUtilities.centerFrameOnScreen;

public class XYBarChartDemo1 extends JFrame {
Map<String, String> results = new HashMap<>();

public XYBarChartDemo1(String paramString) {
    super(paramString);
    JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(500, 270));
    //setContentPane(localJPanel);
    this.setLayout(new BorderLayout());

    this.add(localJPanel, CENTER);
}

private static JFreeChart createChart(IntervalXYDataset paramIntervalXYDataset) {
    JFreeChart localJFreeChart = createXYBarChart("Daily rainfall from station ", "Date", true, "rainfall intensity", paramIntervalXYDataset, VERTICAL, true, false, false);
    //localJFreeChart.addSubtitle(new TextTitle("Source: http://www.amnestyusa.org/abolish/listbyyear.do", new Font("Dialog", 2, 10)));
    localJFreeChart.setBackgroundPaint(white);
    XYPlot localXYPlot = localJFreeChart.getXYPlot();
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    StandardXYToolTipGenerator localStandardXYToolTipGenerator = new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("dd-mm-yyyy"), new DecimalFormat("0.00"));
    localXYItemRenderer.setToolTipGenerator(localStandardXYToolTipGenerator);
    localXYPlot.setBackgroundPaint(lightGray);
    localXYPlot.setRangeGridlinePaint(white);
    DateAxis localDateAxis = (DateAxis) localXYPlot.getDomainAxis();
    localDateAxis.setTickMarkPosition(MIDDLE);
    localDateAxis.setLowerMargin(0.01D);
    localDateAxis.setUpperMargin(0.01D);
    return localJFreeChart;
}

private IntervalXYDataset createDataset() {
    TimeSeries localTimeSeries = new TimeSeries("rainfall [unit:mm/day]", Day.class);

    try {
        forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=WHALLDB", "sa", "ikoiko");
        Statement stat = conn.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
        String sql = "USE WHALLDB SELECT STCD, TM, RainfallIntensity FROM [WHALLDB].[dbo].[rainfall_day] WHERE STCD = 41841250 AND (TM BETWEEN '2000-6-1' and '2002-9-30') ORDER BY TM ASC";
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            String drp = rs.getString(3);
            String tm = rs.getString(2);
            results.put(tm, drp);

        }
        for (Map.Entry<String, String> entryset : results.entrySet()) {
            localTimeSeries.add(new Day(parseInt(entryset.getKey().substring(8, 10)), parseInt(entryset.getKey().substring(6, 7)), parseInt(entryset.getKey().substring(0, 4))), parseDouble(entryset.getValue()));
        }
    } catch (ClassNotFoundException | SQLException | NumberFormatException localException) {
        err.println(localException.getMessage());
    }
    TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection(localTimeSeries);
    return localTimeSeriesCollection;
}

public JPanel createDemoPanel() {
    return new ChartPanel(createChart(createDataset()));
}

public static void main(String[] paramArrayOfString) {
    XYBarChartDemo1 localXYBarChartDemo1 = new XYBarChartDemo1("Daily Rainfall of catchment");
    localXYBarChartDemo1.pack();
    centerFrameOnScreen(localXYBarChartDemo1);
    localXYBarChartDemo1.setVisible(true);
}
}