/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.util.List;

import com.mirth.connect.model.Connector;

public class DestinationTableCellEditor extends TextFieldCellEditor {

    protected boolean valueChanged(String value) {
        List<Connector> destinationConnectors = getParent().channelEditPanel.currentChannel.getDestinationConnectors();

        // make sure a change was actually made
        if (value.equals(getOriginalValue())) {
            return false;
        }

        // make sure the name doesn't already exist
        for (int i = 0; i < destinationConnectors.size(); i++) {
            if (destinationConnectors.get(i).getName().equalsIgnoreCase(value) && !destinationConnectors.get(i).getName().equalsIgnoreCase(getOriginalValue())) {
                return false;
            }
        }

        getParent().setSaveEnabled(true);
        // set the name to the new name.
        for (int i = 0; i < destinationConnectors.size(); i++) {
            if (destinationConnectors.get(i).getName().equalsIgnoreCase(getOriginalValue())) {
                destinationConnectors.get(i).setName(value);
            }
        }

        return true;
    }
}
