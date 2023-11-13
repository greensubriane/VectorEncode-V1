/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.GUI;

/**
 * @author Administrator
 */

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

public class ConverterFactoryExample extends JPanel {
public ConverterFactoryExample() {
    DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(new FormLayout("p, 2dlu, p:g"));
    defaultFormBuilder.setDefaultDialogBorder();

    ValueModel booleanModel = new ValueHolder(true);
    ValueModel negativeBooleanModel = ConverterFactory.createBooleanNegator(booleanModel);
    ValueModel stringModel = ConverterFactory.createBooleanToStringConverter(booleanModel, "Is True", "Is Not True");

    defaultFormBuilder.append("CheckBox1:", BasicComponentFactory.createCheckBox(booleanModel, "True Is Checked"));
    defaultFormBuilder.append("CheckBox2:", BasicComponentFactory.createCheckBox(negativeBooleanModel, "True Is NOT Checked"));
    defaultFormBuilder.append("Text Field:", BasicComponentFactory.createTextField(stringModel));

    add(defaultFormBuilder.getPanel());
}


public static void main(String[] a) {
    JFrame f = new JFrame("Converter Factory Example");
    f.setDefaultCloseOperation(2);
    f.add(new ConverterFactoryExample());
    f.pack();
    f.setVisible(true);
}
}

