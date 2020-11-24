/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent.KeyBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.velocity.runtime.RuntimeConstants;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.mirth.connect.client.core.Client;
import com.mirth.connect.client.core.ClientException;

/**
 * The main mirth class. Sets up the login and then authenticates the login information and sets up
 * Frame (the main application window).
 */
public class Mirth {

    private static Preferences userPreferences;

    /**
     * Construct and show the application.
     */
    public Mirth(Client mirthClient) throws ClientException {
        PlatformUI.MIRTH_FRAME = new Frame();

        UIManager.put("Tree.leafIcon", UIConstants.LEAF_ICON);
        UIManager.put("Tree.openIcon", UIConstants.OPEN_ICON);
        UIManager.put("Tree.closedIcon", UIConstants.CLOSED_ICON);

        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        LoginPanel.getInstance().setStatus("Loading components...");
        PlatformUI.MIRTH_FRAME.setupFrame(mirthClient);

        boolean maximized;
        int width;
        int height;

        if (SystemUtils.IS_OS_MAC) {
            /*
             * The window is only maximized when there is no width or height preference saved.
             * Previously, we just set the dimensions on mac and didn't bother with the maximized
             * state because the user could maximize the window then manually resize it, leaving the
             * maximum state as true. As of MIRTH-3691, this no longer happens.
             */
            maximized = (userPreferences.get("width", null) == null || userPreferences.get("height", null) == null) || (userPreferences.getInt("maximizedState", Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH);

            width = userPreferences.getInt("width", UIConstants.MIRTH_WIDTH);
            height = userPreferences.getInt("height", UIConstants.MIRTH_WIDTH);
        } else {
            /*
             * Maximize it if it's supposed to be maximized or if there is no maximized preference
             * saved. Unmaximizing will bring the window back to default size.
             */
            maximized = (userPreferences.getInt("maximizedState", Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH);

            if (maximized) {
                // If it is maximized, use the default width and height for unmaximizing
                width = UIConstants.MIRTH_WIDTH;
                height = UIConstants.MIRTH_HEIGHT;
            } else {
                // If it's not maximized, get the saved width and height
                width = userPreferences.getInt("width", UIConstants.MIRTH_WIDTH);
                height = userPreferences.getInt("height", UIConstants.MIRTH_HEIGHT);
            }
        }

        // Now set the width and height (saved or default)
        PlatformUI.MIRTH_FRAME.setSize(width, height);
        PlatformUI.MIRTH_FRAME.setLocationRelativeTo(null);

        if (maximized) {
            PlatformUI.MIRTH_FRAME.setExtendedState(Frame.MAXIMIZED_BOTH);
        }

        PlatformUI.MIRTH_FRAME.setVisible(true);
    }

    /**
     * About menu item on Mac OS X
     */
    public static void aboutMac() {
        new AboutMirth();
    }

    /**
     * Quit menu item on Mac OS X. Only exit if on the login window, or if logout is successful
     * 
     * @return quit
     */
    public static boolean quitMac() {
        return (LoginPanel.getInstance().isVisible() || (PlatformUI.MIRTH_FRAME != null && PlatformUI.MIRTH_FRAME.logout(true)));
    }

    /**
     * Create the alternate key bindings for the menu shortcut key mask. This is called if the menu
     * shortcut key mask is not the CTRL key (i.e. COMMAND on OSX)
     */
    private static void createAlternateKeyBindings() {
        int acceleratorKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        // Add the common KeyBindings for macs
        KeyBinding[] defaultBindings = {
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_C, acceleratorKey), DefaultEditorKit.copyAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_V, acceleratorKey), DefaultEditorKit.pasteAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_X, acceleratorKey), DefaultEditorKit.cutAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_A, acceleratorKey), DefaultEditorKit.selectAllAction),
                // deleteNextWordAction and deletePrevWordAction were not available in Java 1.5
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, acceleratorKey), DefaultEditorKit.deleteNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, acceleratorKey), DefaultEditorKit.deletePrevWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, acceleratorKey), DefaultEditorKit.nextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, acceleratorKey), DefaultEditorKit.nextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, acceleratorKey), DefaultEditorKit.previousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, acceleratorKey), DefaultEditorKit.previousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionNextWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionPreviousWordAction),
                new KeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, acceleratorKey | InputEvent.SHIFT_MASK), DefaultEditorKit.selectionPreviousWordAction) };

        keyMapBindings(new javax.swing.JTextField(), defaultBindings);
        keyMapBindings(new javax.swing.JPasswordField(), defaultBindings);
        keyMapBindings(new javax.swing.JTextPane(), defaultBindings);
        keyMapBindings(new javax.swing.JTextArea(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextField(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthPasswordField(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextPane(), defaultBindings);
        keyMapBindings(new com.mirth.connect.client.ui.components.MirthTextArea(), defaultBindings);
    }

    private static void keyMapBindings(JTextComponent comp, KeyBinding[] bindings) {
        JTextComponent.loadKeymap(comp.getKeymap(), bindings, comp.getActions());
    }

    public static void initUIManager() {
        try {
            PlasticLookAndFeel.setPlasticTheme(new MirthTheme());
            PlasticXPLookAndFeel look = new PlasticXPLookAndFeel();
            UIManager.setLookAndFeel(look);
            UIManager.put("win.xpstyle.name", "metallic");
            LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class);

            /*
             * MIRTH-1225 and MIRTH-2019: Create alternate key bindings if CTRL is not the same as
             * the menu shortcut key (i.e. COMMAND on OSX)
             */
            if (InputEvent.CTRL_MASK != Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) {
                createAlternateKeyBindings();
            }

            if (SystemUtils.IS_OS_MAC) {
                OSXAdapter.setAboutHandler(Mirth.class, Mirth.class.getDeclaredMethod("aboutMac", (Class[]) null));
                OSXAdapter.setQuitHandler(Mirth.class, Mirth.class.getDeclaredMethod("quitMac", (Class[]) null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // keep the tooltips from disappearing
        ToolTipManager.sharedInstance().setDismissDelay(3600000);

        // TabbedPane defaults
        // UIManager.put("TabbedPane.selected", new Color(0xffffff));
        // UIManager.put("TabbedPane.background",new Color(225,225,225));
        // UIManager.put("TabbedPane.tabAreaBackground",new Color(225,225,225));
        UIManager.put("TabbedPane.highlight", new Color(225, 225, 225));
        UIManager.put("TabbedPane.selectHighlight", new Color(0xc3c3c3));
        UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));

        // TaskPane defaults
        UIManager.put("TaskPane.titleBackgroundGradientStart", new Color(0xffffff));
        UIManager.put("TaskPane.titleBackgroundGradientEnd", new Color(0xffffff));

        // Set fonts
        UIManager.put("TextPane.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ToggleButton.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("Panel.font", UIConstants.DIALOG_FONT);
        UIManager.put("PopupMenu.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("OptionPane.font", UIConstants.DIALOG_FONT);
        UIManager.put("Label.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("Tree.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ScrollPane.font", UIConstants.DIALOG_FONT);
        UIManager.put("TextField.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("Viewport.font", UIConstants.DIALOG_FONT);
        UIManager.put("MenuBar.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("FormattedTextField.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("DesktopIcon.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("TableHeader.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ToolTip.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("PasswordField.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("TaskPane.font", UIConstants.TEXTFIELD_BOLD_FONT);
        UIManager.put("Table.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("TabbedPane.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ProgressBar.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("CheckBoxMenuItem.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ColorChooser.font", UIConstants.DIALOG_FONT);
        UIManager.put("Button.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("TextArea.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("Spinner.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("RadioButton.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("TitledBorder.font", UIConstants.TEXTFIELD_BOLD_FONT);
        UIManager.put("EditorPane.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("RadioButtonMenuItem.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ToolBar.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("MenuItem.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("CheckBox.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("JXTitledPanel.title.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("Menu.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("ComboBox.font", UIConstants.TEXTFIELD_PLAIN_FONT);
        UIManager.put("JXLoginPanel.banner.font", UIConstants.BANNER_FONT);
        UIManager.put("List.font", UIConstants.TEXTFIELD_PLAIN_FONT);

        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
        im.put(KeyStroke.getKeyStroke("pressed ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

        try {
            UIManager.put("wizard.sidebar.image", ImageIO.read(com.mirth.connect.client.ui.Frame.class.getResource("images/wizardsidebar.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Application entry point. Sets up the login panel and its layout as well.
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        String server = "https://localhost:8443";
        String version = "";
        String username = "";
        String password = "";
        String protocols = "";
        String cipherSuites = "";

        if (args.length > 0) {
            server = args[0];
        }
        if (args.length > 1) {
            version = args[1];
        }
        if (args.length > 2) {
            if (StringUtils.equalsIgnoreCase(args[2], "-ssl")) {
                // <server> <version> -ssl [<protocols> [<ciphersuites> [<username> [<password>]]]]
                if (args.length > 3) {
                    protocols = args[3];
                }
                if (args.length > 4) {
                    cipherSuites = args[4];
                }
                if (args.length > 5) {
                    username = args[5];
                }
                if (args.length > 6) {
                    password = args[6];
                }
            } else {
                // <server> <version> <username> [<password> [-ssl [<protocols> [<ciphersuites>]]]]
                username = args[2];
                if (args.length > 3) {
                    password = args[3];
                }
                if (args.length > 4 && StringUtils.equalsIgnoreCase(args[4], "-ssl")) {
                    if (args.length > 5) {
                        protocols = args[5];
                    }
                    if (args.length > 6) {
                        cipherSuites = args[6];
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(protocols)) {
            PlatformUI.HTTPS_PROTOCOLS = StringUtils.split(protocols, ',');
        }
        if (StringUtils.isNotBlank(cipherSuites)) {
            PlatformUI.HTTPS_CIPHER_SUITES = StringUtils.split(cipherSuites, ',');
        }

        start(server, version, username, password);
    }

    private static void start(final String server, final String version, final String username, final String password) {
        // disable the velocity logging
        Logger velocityLogger = Logger.getLogger(RuntimeConstants.DEFAULT_RUNTIME_LOG_NAME);
        if (velocityLogger != null && velocityLogger.getLevel() == null) {
            velocityLogger.setLevel(Level.OFF);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initUIManager();
                PlatformUI.BACKGROUND_IMAGE = new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/header_nologo.png"));
                LoginPanel.getInstance().initialize(server, version, username, password);
            }
        });
    }
}
