/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author Administrator
 */

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static cn.edu.xaut.SLGridDataNode.Util.RainFallCurveBean.*;
import static com.jgoodies.forms.factories.CC.xy;
import static com.jgoodies.forms.factories.DefaultComponentFactory.getInstance;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.Color.lightGray;
import static java.awt.Color.white;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.System.err;
import static org.jfree.chart.ChartFactory.createXYBarChart;
import static org.jfree.chart.axis.DateTickMarkPosition.MIDDLE;
import static org.jfree.chart.plot.PlotOrientation.VERTICAL;


/**
 * @author heting
 */
public class RainFallGUI extends JFrame {

Map<String, String> results = new HashMap<>();
TimeSeriesCollection datasets = new TimeSeriesCollection();

public RainFallGUI() {
    initComponents();
}

private void button1ActionPerformed(ActionEvent e) {
    SetStartDate(textField1.getText());
    SetEndDate(textField2.getText());
    TimeSeries localTimeSeries = new TimeSeries("rainfall [unit:mm/day]", Day.class);

    try {
        ResultSet rs = null;
        // ResultSet rs = GetRainFallIntensity("gmldatabase", getIP(), getDBType(), GetSTCD(), GetstartDate(), GetEndDate());
        while (rs.next()) {
            String drp = rs.getString(3);
            String tm = rs.getString(2);
            results.put(tm, drp);
        }
        if (localTimeSeries != null) {
            localTimeSeries.clear();
        }
        for (Map.Entry<String, String> entryset : results.entrySet()) {
            localTimeSeries.add(new Day(parseInt(entryset.getKey().substring(8, 10)), parseInt(entryset.getKey().substring(6, 7)), parseInt(entryset.getKey().substring(0, 4))), parseDouble(entryset.getValue()));
        }
    } catch (SQLException | NumberFormatException localException) {
        err.println(localException.getMessage());
    }
    if (datasets != null) {
        datasets.removeAllSeries();
    }
    datasets.addSeries(localTimeSeries);
}

private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    DefaultComponentFactory compFactory = getInstance();
    panel1 = new JPanel();
    label1 = compFactory.createLabel("   Begin Date:");
    textField1 = new JTextField();
    label2 = compFactory.createLabel("     End Date:");
    textField2 = new JTextField();
    button1 = new JButton();

    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    ChartPanel chartpanel = new ChartPanel(createChart(datasets), false);
    //======== panel1 ========
    {
        panel1.setLayout(new FormLayout(
                "75dlu, 2*(79dlu), 83dlu, 67dlu",
                "default"));
        panel1.add(label1, xy(1, 1));
        panel1.add(textField1, xy(2, 1));
        panel1.add(label2, xy(3, 1));
        panel1.add(textField2, xy(4, 1));

        //---- button1 ----
        button1.setText("Query");
        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }

        });
        panel1.add(button1, xy(5, 1));
    }
    contentPane.add(panel1, NORTH);
    contentPane.add(chartpanel, CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
}


private JFreeChart createChart(TimeSeriesCollection localTimeSeriesCollection) {

    JFreeChart localJFreeChart = createXYBarChart("Daily rainfall from station " + GetSTCD(), "Date", true, "rainfall intensity", localTimeSeriesCollection, VERTICAL, true, false, false);
    localJFreeChart.setBackgroundPaint(white);
    XYPlot localXYPlot = localJFreeChart.getXYPlot();
    XYItemRenderer localXYItemRenderer = localXYPlot.getRenderer();
    StandardXYToolTipGenerator localStandardXYToolTipGenerator = new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("dd-MM-yyyy"), new DecimalFormat("0.00"));
    localXYItemRenderer.setToolTipGenerator(localStandardXYToolTipGenerator);
    localXYPlot.setBackgroundPaint(lightGray);
    localXYPlot.setRangeGridlinePaint(white);
    ValueAxis valueaxis = localXYPlot.getRangeAxis();
    valueaxis.setRange(0.0D, 300D);
    DateAxis localDateAxis = (DateAxis) localXYPlot.getDomainAxis();
    localDateAxis.setTickMarkPosition(MIDDLE);
    localDateAxis.setLowerMargin(0.01D);
    localDateAxis.setUpperMargin(0.01D);
    return localJFreeChart;
}

// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
private JPanel panel1;
private JLabel label1;
private JTextField textField1;
private JLabel label2;
private JTextField textField2;
private JButton button1;
// JFormDesigner - End of variables declaration  //GEN-END:variables
}

