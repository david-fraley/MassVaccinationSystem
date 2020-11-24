/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.mirth.connect.client.ui.util.DisplayUtil;

// TODO: Maybe add a type definition to determine if this is a float or an int cell

public class NumberCellRenderer extends DefaultTableCellRenderer {

    private boolean padding = false;

    public NumberCellRenderer() {
        this(RIGHT, true);
    }

    public NumberCellRenderer(int alignment, boolean padding) {
        super();
        this.padding = padding;
        this.setHorizontalAlignment(alignment);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        if (value == null) {
            setText("--");
        } else if (value instanceof Integer) {
            String displayText = DisplayUtil.formatNumber((Integer) value);
            if (padding) {
                setText(displayText + " ");
            } else {
                setText(displayText);
            }
        } else if (value instanceof Long) {
            String displayText = DisplayUtil.formatNumber((Long) value);
            if (padding) {
                setText(displayText + " ");
            } else {
                setText(displayText);
            }
        } else if (value instanceof BigDecimal) {
            String displayText = ((BigDecimal) value).stripTrailingZeros().toString();
            if (padding) {
                setText(displayText + " ");
            } else {
                setText(displayText);
            }
        }
        return this;
    }
}
