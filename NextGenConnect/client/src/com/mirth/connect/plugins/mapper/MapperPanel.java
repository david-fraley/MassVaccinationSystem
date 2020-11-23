/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.mapper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.mirth.connect.client.ui.Mirth;
import com.mirth.connect.client.ui.RefreshTableModel;
import com.mirth.connect.client.ui.TextFieldCellEditor;
import com.mirth.connect.client.ui.UIConstants;
import com.mirth.connect.client.ui.components.MirthComboBox;
import com.mirth.connect.client.ui.components.MirthTable;
import com.mirth.connect.client.ui.editors.EditorPanel;
import com.mirth.connect.model.Step;
import com.mirth.connect.plugins.mapper.MapperStep.Scope;

public class MapperPanel extends EditorPanel<Step> {

    private static final int REGEX_COLUMN = 0;
    private static final int REPLACEMENT_COLUMN = 1;
    private static final String REGEX_COLUMN_NAME = "Regular Expression";
    private static final String REPLACEMENT_COLUMN_NAME = "Replace With";

    private ActionListener nameActionListener;

    public MapperPanel() {
        initComponents();
        initLayout();
    }

    @Override
    public Step getDefaults() {
        return new MapperStep();
    }

    @Override
    public Step getProperties() {
        MapperStep props = new MapperStep();

        props.setVariable(variableField.getText().trim());
        props.setMapping(mappingField.getText().trim());
        props.setDefaultValue(defaultValueField.getText().trim());

        List<Pair<String, String>> replacements = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < regularExpressionsTable.getModel().getRowCount(); i++) {
            String regex = (String) regularExpressionsTable.getModel().getValueAt(i, REGEX_COLUMN);
            if (StringUtils.isNotBlank(regex)) {
                replacements.add(new ImmutablePair<String, String>(regex, (String) regularExpressionsTable.getValueAt(i, REPLACEMENT_COLUMN)));
            }
        }
        props.setReplacements(replacements);

        props.setScope((Scope) addToComboBox.getSelectedItem());

        return props;
    }

    @Override
    public void setProperties(Step properties) {
        MapperStep props = (MapperStep) properties;

        variableField.setText(props.getVariable());
        mappingField.setText(props.getMapping());
        defaultValueField.setText(props.getDefaultValue());

        List<Pair<String, String>> replacements = props.getReplacements();
        if (replacements != null) {
            setRegexProperties(replacements);
        } else {
            setRegexProperties(new ArrayList<Pair<String, String>>());
        }

        addToComboBox.setSelectedItem(props.getScope());
    }

    @Override
    public String checkProperties(Step properties, boolean highlight) {
        MapperStep props = (MapperStep) properties;
        String errors = "";

        if (StringUtils.isBlank(props.getVariable())) {
            errors += "The variable name cannot be blank.\n";
            if (highlight) {
                variableField.setBackground(UIConstants.INVALID_COLOR);
            }
        }

        return errors;
    }

    @Override
    public void resetInvalidProperties() {
        variableField.setBackground(null);
    }

    @Override
    public void setNameActionListener(ActionListener actionListener) {
        nameActionListener = actionListener;
    }

    @Override
    public void stopEditing() {
        if (regularExpressionsTable.isEditing()) {
            regularExpressionsTable.getCellEditor(regularExpressionsTable.getEditingRow(), regularExpressionsTable.getEditingColumn()).stopCellEditing();
        }
    }

    private void setRegexProperties(List<Pair<String, String>> properties) {
        Object[][] tableData = new Object[properties.size()][2];

        if (properties != null) {
            int i = 0;
            for (Pair<String, String> pair : properties) {
                tableData[i][REGEX_COLUMN] = pair.getLeft();
                tableData[i][REPLACEMENT_COLUMN] = pair.getRight();
                i++;
            }
        }

        ((RefreshTableModel) regularExpressionsTable.getModel()).refreshDataVector(tableData);
    }

    /** Clears the selection in the table and sets the tasks appropriately */
    private void deselectRows() {
        regularExpressionsTable.clearSelection();
        deleteButton.setEnabled(false);
    }

    /** Get the currently selected destination index */
    private int getSelectedRow() {
        if (regularExpressionsTable.isEditing()) {
            return regularExpressionsTable.getEditingRow();
        } else {
            return regularExpressionsTable.getSelectedRow();
        }
    }

    private void initComponents() {
        setBackground(UIConstants.BACKGROUND_COLOR);

        variableLabel = new JLabel("Variable:");
        variableField = new JTextField();
        variableField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent evt) {
                documentChanged(evt);
            }

            @Override
            public void insertUpdate(DocumentEvent evt) {
                documentChanged(evt);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                documentChanged(evt);
            }

            private void documentChanged(DocumentEvent evt) {
                try {
                    String text = evt.getDocument().getText(0, evt.getDocument().getLength());
                    if (nameActionListener != null) {
                        nameActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text));
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });

        addToLabel = new JLabel("Add to:");
        addToComboBox = new MirthComboBox<Scope>();
        addToComboBox.setModel(new DefaultComboBoxModel<Scope>(Scope.values()));

        mappingLabel = new JLabel("Mapping:");
        mappingField = new JTextField();

        defaultValueLabel = new JLabel("Default Value:");
        defaultValueField = new JTextField();

        replacementLabel = new JLabel("String Replacement:");

        regularExpressionsTable = new MirthTable();
        regularExpressionsTable.setModel(new RefreshTableModel(new String[] { REGEX_COLUMN_NAME,
                REPLACEMENT_COLUMN_NAME }, 0));

        regularExpressionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (getSelectedRow() != -1) {
                    deleteButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                }
            }
        });

        class RegExTableCellEditor extends TextFieldCellEditor {

            @Override
            public boolean isCellEditable(EventObject evt) {
                boolean editable = super.isCellEditable(evt);

                if (editable) {
                    deleteButton.setEnabled(false);
                }

                return editable;
            }

            @Override
            protected boolean valueChanged(String value) {
                deleteButton.setEnabled(true);
                return true;
            }
        }

        regularExpressionsTable.getColumnModel().getColumn(regularExpressionsTable.getColumnModel().getColumnIndex(REGEX_COLUMN_NAME)).setCellEditor(new RegExTableCellEditor());
        regularExpressionsTable.getColumnModel().getColumn(regularExpressionsTable.getColumnModel().getColumnIndex(REPLACEMENT_COLUMN_NAME)).setCellEditor(new RegExTableCellEditor());
        regularExpressionsTable.setCustomEditorControls(true);

        regularExpressionsTable.setSelectionMode(0);
        regularExpressionsTable.setRowSelectionAllowed(true);
        regularExpressionsTable.setRowHeight(UIConstants.ROW_HEIGHT);
        regularExpressionsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        regularExpressionsTable.setDragEnabled(false);
        regularExpressionsTable.setOpaque(true);
        regularExpressionsTable.setSortable(false);
        regularExpressionsTable.setEditable(true);
        regularExpressionsTable.getTableHeader().setReorderingAllowed(false);

        if (Preferences.userNodeForPackage(Mirth.class).getBoolean("highlightRows", true)) {
            Highlighter highlighter = HighlighterFactory.createAlternateStriping(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR);
            regularExpressionsTable.setHighlighters(highlighter);
        }

        regularExpressionsScrollPane = new JScrollPane(regularExpressionsTable);
        regularExpressionsScrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                deselectRows();
            }
        });

        newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        deleteButton.setEnabled(false);
    }

    private void initLayout() {
        setLayout(new MigLayout("insets 8, novisualpadding, hidemode 3, gap 6"));

        add(variableLabel, "right, gapafter 6");
        add(variableField, "growx, sx, split 3");
        add(addToLabel, "gapbefore 12, gapafter 6");
        add(addToComboBox);
        add(mappingLabel, "newline, right, gapafter 6");
        add(mappingField, "sx, growx");
        add(defaultValueLabel, "newline, right, gapafter 6");
        add(defaultValueField, "sx, growx");
        add(replacementLabel, "newline, top, right, gapafter 6");
        add(regularExpressionsScrollPane, "grow, push, sy");
        add(newButton, "top, flowy, split 2, sgx");
        add(deleteButton, "top, sgx");
    }

    private void deleteButtonActionPerformed(ActionEvent evt) {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1 && !regularExpressionsTable.isEditing()) {
            ((RefreshTableModel) regularExpressionsTable.getModel()).removeRow(regularExpressionsTable.convertRowIndexToModel(selectedRow));

            if (regularExpressionsTable.getRowCount() > 0) {
                if (selectedRow < regularExpressionsTable.getRowCount()) {
                    regularExpressionsTable.setRowSelectionInterval(selectedRow, selectedRow);
                } else {
                    regularExpressionsTable.setRowSelectionInterval(regularExpressionsTable.getRowCount() - 1, regularExpressionsTable.getRowCount() - 1);
                }
            }
        }
    }

    private void newButtonActionPerformed(ActionEvent evt) {
        ((DefaultTableModel) regularExpressionsTable.getModel()).addRow(new Object[] { "", "" });
        regularExpressionsTable.setRowSelectionInterval(regularExpressionsTable.getRowCount() - 1, regularExpressionsTable.getRowCount() - 1);
    }

    private JLabel variableLabel;
    private JTextField variableField;
    private JLabel addToLabel;
    private MirthComboBox<Scope> addToComboBox;
    private JLabel mappingLabel;
    private JTextField mappingField;
    private JLabel defaultValueLabel;
    private JTextField defaultValueField;
    private JLabel replacementLabel;
    private MirthTable regularExpressionsTable;
    private JScrollPane regularExpressionsScrollPane;
    private JButton newButton;
    private JButton deleteButton;
}
