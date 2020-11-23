/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.components.rsta.actions;

import javax.swing.SwingConstants;

import com.mirth.connect.client.ui.components.rsta.MirthRSyntaxTextArea;

public class MoveUpAction extends org.fife.ui.rtextarea.RTextAreaEditorKit.NextVisualPositionAction {

    public MoveUpAction(boolean select) {
        super(null, select, SwingConstants.NORTH);
        setProperties(MirthRSyntaxTextArea.getResourceBundle(), (select ? ActionInfo.MOVE_UP_SELECT : ActionInfo.MOVE_UP).toString());
    }
}