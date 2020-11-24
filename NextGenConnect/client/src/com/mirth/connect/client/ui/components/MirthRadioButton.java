/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.components;

import com.mirth.connect.client.ui.Frame;
import com.mirth.connect.client.ui.PlatformUI;

/**
 * Mirth's implementation of the JRadioButton. Adds enabling of the save button in parent.
 */
public class MirthRadioButton extends javax.swing.JRadioButton {

    private Frame parent;

    public MirthRadioButton() {
        super();
        init();
    }

    public MirthRadioButton(String text) {
        super(text);
        init();
    }

    private void init() {
        this.setFocusable(true);
        this.parent = PlatformUI.MIRTH_FRAME;
        this.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonChanged(evt);
            }
        });
    }

    public void radioButtonChanged(java.awt.event.ActionEvent evt) {
        parent.setSaveEnabled(true);
    }
}
