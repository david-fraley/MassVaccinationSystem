/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.components;

import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.SyntaxDocument;

import com.mirth.connect.client.ui.Frame;
import com.mirth.connect.client.ui.PlatformUI;
import com.mirth.connect.client.ui.actions.CopyAction;
import com.mirth.connect.client.ui.actions.CutAction;
import com.mirth.connect.client.ui.actions.DeleteAction;
import com.mirth.connect.client.ui.actions.FindAndReplaceAction;
import com.mirth.connect.client.ui.actions.PasteAction;
import com.mirth.connect.client.ui.actions.RedoAction;
import com.mirth.connect.client.ui.actions.SelectAllAction;
import com.mirth.connect.client.ui.actions.ShowLineEndingsAction;
import com.mirth.connect.client.ui.actions.UndoAction;

/**
 * Mirth's implementation of the JTextArea. Adds enabling of the save button in parent. Also adds a
 * trigger button (right click) editor menu with Cut, Copy, Paste, Delete, and Select All.
 */
public class MirthSyntaxTextArea extends JEditTextArea implements MirthTextInterface {

    private Frame parent;
    private CutAction cutAction;
    private CopyAction copyAction;
    private PasteAction pasteAction;
    private DeleteAction deleteAction;
    private SelectAllAction selectAllAction;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private FindAndReplaceAction findReplaceAction;
    private ShowLineEndingsAction showLineEndingsAction;
    private boolean saveDisabled;

    public MirthSyntaxTextArea() {
        this(false);
    }

    public MirthSyntaxTextArea(boolean lineNumbers) {
        this.parent = PlatformUI.MIRTH_FRAME;
        this.setFocusable(true);
        this.setCaretVisible(false);
        this.setShowLineEndings(false);
        // This needs to be saveDisabled instead of saveEnabled because JEditTextArea actually calls setDocument before this gets initialized
        this.saveDisabled = false;
        // Setup menu actions
        cutAction = new CutAction(this);
        copyAction = new CopyAction(this);
        pasteAction = new PasteAction(this);
        deleteAction = new DeleteAction(this);
        selectAllAction = new SelectAllAction(this);
        undoAction = new UndoAction(this);
        redoAction = new RedoAction(this);
        findReplaceAction = new FindAndReplaceAction(this);
        showLineEndingsAction = new ShowLineEndingsAction(this);
        popup = new JPopupMenu();

        popup.add(undoAction);
        popup.add(redoAction);
        popup.addSeparator();
        popup.add(cutAction);
        popup.add(copyAction);
        popup.add(pasteAction);
        popup.addSeparator();
        popup.add(deleteAction);
        popup.addSeparator();
        popup.add(selectAllAction);
        popup.add(findReplaceAction);
        popup.add(new JCheckBoxMenuItem(showLineEndingsAction));

        this.popupHandler = new PopUpHandler() {

            public void showPopupMenu(JPopupMenu menu, MouseEvent evt) {
                menu.getComponent(0).setEnabled(undoAction.isEnabled());
                menu.getComponent(1).setEnabled(redoAction.isEnabled());
                menu.getComponent(3).setEnabled(cutAction.isEnabled());
                menu.getComponent(4).setEnabled(copyAction.isEnabled());
                menu.getComponent(5).setEnabled(pasteAction.isEnabled());
                menu.getComponent(7).setEnabled(deleteAction.isEnabled());
                menu.getComponent(9).setEnabled(selectAllAction.isEnabled());
                menu.getComponent(10).setEnabled(findReplaceAction.isEnabled());
                menu.getComponent(11).setEnabled(showLineEndingsAction.isEnabled());
                if (isShowLineEndings()) {
                    ((JCheckBoxMenuItem) menu.getComponent(11)).setState(true);
                } else {
                    ((JCheckBoxMenuItem) menu.getComponent(11)).setState(false);
                }
                menu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        };
    }

    /*
     * Support for undo and redo
     */
    public void undo() {
        if (this.undo.canUndo()) {
            this.undo.undo();
        }
    }

    public void redo() {
        if (this.undo.canRedo()) {
            this.undo.redo();
        }
    }

    public boolean canRedo() {
        return this.undo.canRedo();
    }

    public boolean canUndo() {
        return this.undo.canUndo();
    }

    public void setSaveEnabled(boolean saveEnabled) {
        this.saveDisabled = !saveEnabled;
    }

    /**
     * Overrides setDocument(Document doc) so that a document listener is added to the current
     * document to listen for changes.
     */
    public void setDocument(SyntaxDocument doc) {
        super.setDocument(doc);
        if (!saveDisabled) {
            this.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    parent.setSaveEnabled(true);
                }

                public void removeUpdate(DocumentEvent e) {
                    parent.setSaveEnabled(true);
                }

                public void insertUpdate(DocumentEvent e) {
                    parent.setSaveEnabled(true);
                }
            });
        }
    }

    /**
     * Overrides setText(String t) so that the save button is disabled when Mirth sets the text of a
     * field.
     */
    public void setText(String t) {
        boolean visible = parent.changesHaveBeenMade();
        super.setText(t);

        if (visible) {
            parent.setSaveEnabled(true);
        } else {
            parent.setSaveEnabled(false);
        }
    }

    public String getText() {
        return super.getText();
    }

    public void replaceSelection(String text) {
        setSelectedText(text);
    }
}
