/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.javascriptrule;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import net.miginfocom.swing.MigLayout;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;

import com.mirth.connect.client.ui.PlatformUI;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.rsta.MirthRTextScrollPane;
import com.mirth.connect.client.ui.editors.EditorPanel;
import com.mirth.connect.model.Rule;
import com.mirth.connect.model.codetemplates.ContextType;
import com.mirth.connect.util.JavaScriptSharedUtil;

public class JavaScriptPanel extends EditorPanel<Rule> {

    public JavaScriptPanel() {
        initComponents();
        initLayout();
    }

    @Override
    public Rule getDefaults() {
        return new JavaScriptRule();
    }

    @Override
    public Rule getProperties() {
        JavaScriptRule props = new JavaScriptRule();

        props.setScript(scriptTextArea.getText().trim());

        return props;
    }

    @Override
    public void setProperties(Rule properties) {
        JavaScriptRule props = (JavaScriptRule) properties;

        scriptTextArea.setText(props.getScript());
    }

    @Override
    public String checkProperties(Rule properties, boolean highlight) {
        JavaScriptRule props = (JavaScriptRule) properties;
        try {
            Context context = JavaScriptSharedUtil.getGlobalContextForValidation();
            context.compileString("function rhinoWrapper() {" + props.getScript() + "\n}", PlatformUI.MIRTH_FRAME.mirthClient.getGuid(), 1, null);
        } catch (EvaluatorException e) {
            return "Error on line " + e.lineNumber() + ": " + e.getMessage() + ".";
        } catch (Exception e) {
            return "Unknown error occurred during validation.";
        } finally {
            Context.exit();
        }
        return null;
    }

    @Override
    public void resetInvalidProperties() {}

    @Override
    public void setNameActionListener(ActionListener actionListener) {}

    public void setContextType(ContextType contextType) {
        scriptTextArea.setContextType(contextType);
    }

    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);

        scriptTextArea = new MirthRTextScrollPane(null, true);
        scriptTextArea.setBorder(BorderFactory.createEtchedBorder());
    }

    private void initLayout() {
        setLayout(new MigLayout("insets 0, novisualpadding, hidemode 3"));

        add(scriptTextArea, "grow, push");
    }

    private MirthRTextScrollPane scriptTextArea;
}