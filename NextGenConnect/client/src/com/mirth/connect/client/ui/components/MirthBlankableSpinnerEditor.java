/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.components;

import java.beans.PropertyChangeEvent;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;

public class MirthBlankableSpinnerEditor extends DefaultEditor {
    public MirthBlankableSpinnerEditor(JSpinner spinner) {
        super(spinner);
        getTextField().setEditable(true);
        getTextField().setHorizontalAlignment(JTextField.RIGHT);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        JSpinner spinner = getSpinner();

        if (spinner == null) {
            return;
        }

        super.propertyChange(e);

        if ((e.getSource() instanceof JFormattedTextField) && e.getPropertyName().equals("editValid")) {
            spinner.setValue("");
        }
    }
}
