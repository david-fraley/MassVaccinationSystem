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
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mirth.connect.client.ui.components.MirthTree;
import com.mirth.connect.client.ui.components.MirthTree.FilterTreeModel;
import com.mirth.connect.client.ui.components.MirthTreeNode;
import com.mirth.connect.client.ui.components.MirthTreeNode.JSONType;
import com.mirth.connect.client.ui.editors.BaseEditorPane;
import com.mirth.connect.client.ui.editors.MessageTreePanel;
import com.mirth.connect.client.ui.editors.filter.FilterPane;
import com.mirth.connect.client.ui.editors.transformer.TransformerPane;
import com.mirth.connect.donkey.model.message.SerializationType;
import com.mirth.connect.model.converters.IMessageSerializer;
import com.mirth.connect.model.datatype.DataTypeProperties;
import com.mirth.connect.model.util.DefaultMetaData;
import com.mirth.connect.model.util.MessageVocabulary;
import com.mirth.connect.model.util.MessageVocabularyFactory;
import com.mirth.connect.plugins.DataTypeClientPlugin;

public class TreePanel extends javax.swing.JPanel {

    private static final String EMPTY = "[empty]";
    private String version = "";
    private String type = "";
    private Logger logger = Logger.getLogger(this.getClass());
    private BaseEditorPane<?, ?> editorPane;
    private String _dropPrefix;
    private String _dropSuffix;
    private String messageName;
    private MessageVocabulary vocabulary;
    private Timer timer;
    private JPopupMenu popupMenu;
    private JMenuItem popupMenuExpand;
    private JMenuItem popupMenuCollapse;
    private JMenuItem popupMenuMapToVariable;
    private JMenuItem popupMenuMapSegmentFilter;
    private JMenuItem popupMenuMapSegment;
    private JMenuItem popupMenuFilterSegment;
    private String lastWorkingId = null;

    /**
     * Creates new form TreePanel
     */
    public TreePanel(BaseEditorPane<?, ?> editorPane) {
        this(editorPane, null, null);
    }

    public TreePanel(BaseEditorPane<?, ?> editorPane, String prefix, String suffix) {
        this.editorPane = editorPane;
        _dropPrefix = prefix;
        _dropSuffix = suffix;

        setup();
    }

    public void setup() {
        initComponents();

        filterTextBox.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent arg0) {}

            public void keyReleased(KeyEvent e) {
                filterActionPerformed();
            }

            public void keyTyped(KeyEvent e) {}
        });

        exact.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                filterActionPerformed();
            }
        });
    }

    private void recursivelyExpandChildren(MirthTreeNode tn) {
        tree.expandPath(new TreePath(tn.getPath()));
        Enumeration<TreeNode> children = tn.children();
        while (children.hasMoreElements()) {
            MirthTreeNode child = (MirthTreeNode) children.nextElement();
            if (child.getChildCount() > 0) {
                recursivelyExpandChildren(child);
            }
            tree.expandPath(new TreePath(child.getPath()));
        }
    }

    private void recursivelyCollapseChildren(MirthTreeNode tn) {
        Enumeration<TreeNode> children = tn.children();
        while (children.hasMoreElements()) {
            MirthTreeNode child = (MirthTreeNode) children.nextElement();
            if (child.getChildCount() > 0) {
                recursivelyCollapseChildren(child);
            }
            tree.collapsePath(new TreePath(child.getPath()));
        }
    }

    public void setPrefix(String prefix) {
        _dropPrefix = prefix;
    }

    public void setSuffix(String suffix) {
        _dropSuffix = suffix;
    }

    public void setupPopupMenu() {
        popupMenu = new JPopupMenu();
        popupMenuExpand = new JMenuItem("Expand");
        popupMenuExpand.setIcon(new ImageIcon(this.getClass().getResource("images/add.png")));
        popupMenuExpand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MirthTreeNode tn;
                if (tree.getSelectionPath() != null) {
                    TreePath tp = tree.getSelectionPath();
                    tn = (MirthTreeNode) tp.getLastPathComponent();
                } else {
                    tn = (MirthTreeNode) tree.getModel().getRoot();
                }
                if (!tn.isLeaf()) {
                    recursivelyExpandChildren(tn);
                    tree.expandPath(new TreePath(tn.getPath()));
                }

            }
        });
        popupMenu.add(popupMenuExpand);

        popupMenuCollapse = new JMenuItem("Collapse");
        popupMenuCollapse.setIcon(new ImageIcon(this.getClass().getResource("images/delete.png")));
        popupMenuCollapse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MirthTreeNode tn;
                if (tree.getSelectionPath() != null) {
                    TreePath tp = tree.getSelectionPath();
                    tn = (MirthTreeNode) tp.getLastPathComponent();
                } else {
                    tn = (MirthTreeNode) tree.getModel().getRoot();
                }
                if (!tn.isLeaf()) {
                    recursivelyCollapseChildren(tn);
                    tree.collapsePath(new TreePath(tn.getPath()));
                }
            }
        });
        popupMenu.add(popupMenuCollapse);

        popupMenu.addSeparator();

        if (_dropPrefix.equals(MessageTreePanel.MAPPER_PREFIX)) {
            popupMenuMapToVariable = new JMenuItem("Map to Variable");
            popupMenuMapToVariable.setIcon(new ImageIcon(this.getClass().getResource("images/book_previous.png")));
            popupMenuMapToVariable.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TreePath path = tree.getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    TreeNode tp = (TreeNode) path.getLastPathComponent();
                    if (tp == null) {
                        return;
                    }

                    String variable = MirthTree.constructVariable(tp);
                    PlatformUI.MIRTH_FRAME.channelEditPanel.transformerPane.addNewElement(variable, variable, MirthTree.constructPath(tp, tree.getPrefix(), tree.getSuffix()).toString(), TransformerPane.MAPPER, true);
                }
            });
            popupMenu.add(popupMenuMapToVariable);

            popupMenuMapSegment = new JMenuItem("Map to Message");
            popupMenuMapSegment.setIcon(new ImageIcon(this.getClass().getResource("images/book_next.png")));
            popupMenuMapSegment.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TreePath path = tree.getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    TreeNode tp = (TreeNode) path.getLastPathComponent();
                    if (tp == null) {
                        return;
                    }

                    PlatformUI.MIRTH_FRAME.channelEditPanel.transformerPane.addNewElement(MirthTree.constructMessageBuilderStepName(null, tp), MirthTree.constructPath(tp, tree.getPrefix(), "").toString(), "", TransformerPane.MESSAGE_BUILDER, true);
                }
            });
            popupMenu.add(popupMenuMapSegment);

        } else if (_dropPrefix.equals(MessageTreePanel.MESSAGE_BUILDER_PREFIX)) {
            popupMenuMapSegmentFilter = new JMenuItem("Map to Message");
            popupMenuMapSegmentFilter.setIcon(new ImageIcon(this.getClass().getResource("images/book_next.png")));
            popupMenuMapSegmentFilter.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TreePath path = tree.getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    TreeNode tp = (TreeNode) path.getLastPathComponent();
                    if (tp == null) {
                        return;
                    }

                    PlatformUI.MIRTH_FRAME.channelEditPanel.transformerPane.addNewElement(MirthTree.constructMessageBuilderStepName(null, tp), MirthTree.constructPath(tp, tree.getPrefix(), tree.getSuffix()).toString(), "", TransformerPane.MESSAGE_BUILDER, true);
                }
            });
            popupMenu.add(popupMenuMapSegmentFilter);
        }

        popupMenuFilterSegment = new JMenuItem("Filter by Value");
        popupMenuFilterSegment.setIcon(new ImageIcon(this.getClass().getResource("images/book_previous.png")));
        popupMenuFilterSegment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) {
                    return;
                }
                TreeNode tp = (TreeNode) path.getLastPathComponent();
                if (tp == null) {
                    return;
                }

                PlatformUI.MIRTH_FRAME.channelEditPanel.filterPane.addNewElement("", MirthTree.constructNodeDescription(tp), MirthTree.constructPath(tp, tree.getPrefix(), tree.getSuffix()).toString(), FilterPane.RULE_BUILDER, true);
            }
        });
        popupMenu.add(popupMenuFilterSegment);
    }

    public void setFilterView() {
        toggleMenuComponent(popupMenuMapToVariable, false);
        toggleMenuComponent(popupMenuFilterSegment, true);
        toggleMenuComponent(popupMenuMapSegment, false);
    }

    public void setTransformerView() {
        toggleMenuComponent(popupMenuFilterSegment, false);
        toggleMenuComponent(popupMenuMapSegment, true);
        toggleMenuComponent(popupMenuMapToVariable, true);
    }

    private void toggleMenuComponent(Component component, boolean show) {
        int index = popupMenu.getComponentIndex(component);
        if (index >= 0) {
            popupMenu.getComponent(index).setVisible(show);
        }
    }

    public void setBorderText(String text) {}

    public void filterActionPerformed() {

        class FilterTimer extends TimerTask {

            @Override
            public void run() {
                filter();
            }
        }

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new FilterTimer(), 1000);
        } else {
            timer.cancel();
            PlatformUI.MIRTH_FRAME.stopWorking(lastWorkingId);
            timer = new Timer();
            timer.schedule(new FilterTimer(), 1000);
        }
    }

    public void filter() {
        final String workingId = PlatformUI.MIRTH_FRAME.startWorking("Filtering...");
        lastWorkingId = workingId;
        FilterTreeModel model = (FilterTreeModel) tree.getModel();

        if (filterTextBox.getText().length() > 0) {
            model.setFiltered(true);
        } else {
            model.setFiltered(false);
        }

        model.performFilter(model.getRoot(), filterTextBox.getText(), exact.isSelected(), false);
        model.updateTreeStructure();
        if (filterTextBox.getText().length() > 0) {
            tree.expandAll();
        }

        PlatformUI.MIRTH_FRAME.stopWorking(workingId);
    }

    public void setMessage(DataTypeProperties dataTypeProperties, String messageType, String source, String ignoreText) {
        Document xmlDoc = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        JsonNode jsonDoc = null;

        messageName = "";
        version = "";
        type = "";
        String messageDescription = "";
        String dataType = null;
        if (source.length() > 0 && !source.equals(ignoreText)) {
            IMessageSerializer serializer;

            if (PlatformUI.MIRTH_FRAME.displayNameToDataType.containsKey(messageType)) {
                dataType = PlatformUI.MIRTH_FRAME.displayNameToDataType.get(messageType);
            } else {
                logger.error("Invalid data type");
                return;
            }

            DataTypeClientPlugin clientPlugin = LoadedExtensions.getInstance().getDataTypePlugins().get(dataType);

            SerializationType serializationType = null;
            if (dataTypeProperties != null && dataTypeProperties.getSerializationProperties() != null) {
                serializationType = dataTypeProperties.getSerializationProperties().getSerializationType();
            }
            if (serializationType == null) {
                serializationType = clientPlugin.getDefaultSerializationType();
            }

            switch (serializationType) {
                case JSON:
                    try {
                        serializer = clientPlugin.getSerializer(dataTypeProperties.getSerializerProperties());
                        Map<String, Object> metadata = serializer.getMetaDataFromMessage(source);

                        if (metadata.get(DefaultMetaData.TYPE_VARIABLE_MAPPING) != null) {
                            type = ((String) metadata.get(DefaultMetaData.TYPE_VARIABLE_MAPPING)).trim();
                        } else {
                            type = "Unknown type";
                        }

                        messageName = type;

                        ObjectMapper mapper = new ObjectMapper();
                        String message;

                        if (clientPlugin.isBinary()) {
                            message = source;
                        } else {
                            message = serializer.toJSON(source);
                        }

                        jsonDoc = mapper.readTree(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonDoc != null) {
                        boolean documentIsArray = false;

                        if (jsonDoc.isArray()) {
                            documentIsArray = true;
                        }

                        MirthTreeNode top = processJsonElement(dataType, messageName, jsonDoc, null, documentIsArray);
                        createTree(top);
                        filter();
                    } else {
                        setInvalidMessage(messageType);
                    }
                    break;
                case XML:
                    try {
                        serializer = clientPlugin.getSerializer(dataTypeProperties.getSerializerProperties());
                        Map<String, Object> metadata = serializer.getMetaDataFromMessage(source);

                        if (metadata.get(DefaultMetaData.VERSION_VARIABLE_MAPPING) != null) {
                            version = ((String) metadata.get(DefaultMetaData.VERSION_VARIABLE_MAPPING)).trim();
                        } else {
                            version = "Unknown version";
                        }

                        if (metadata.get(DefaultMetaData.TYPE_VARIABLE_MAPPING) != null) {
                            type = ((String) metadata.get(DefaultMetaData.TYPE_VARIABLE_MAPPING)).trim();
                        } else {
                            type = "Unknown type";
                        }

                        messageName = type + " (" + version + ")";
                        Map<String, Class<? extends MessageVocabulary>> vocabs = new HashMap<String, Class<? extends MessageVocabulary>>();
                        for (DataTypeClientPlugin dataTypePlugin : LoadedExtensions.getInstance().getDataTypePlugins().values()) {
                            Class<? extends MessageVocabulary> vocabulary = dataTypePlugin.getVocabulary();

                            if (vocabulary != null) {
                                vocabs.put(dataTypePlugin.getPluginPointName(), vocabulary);
                            }
                        }
                        vocabulary = MessageVocabularyFactory.getInstance(PlatformUI.MIRTH_FRAME.mirthClient, vocabs).getVocabulary(dataType, version, type);
                        messageDescription = vocabulary.getDescription(type.replaceAll("-", ""));

                        docBuilder = docFactory.newDocumentBuilder();
                        String message;

                        if (clientPlugin.isBinary()) {
                            message = source;
                        } else {
                            message = serializer.toXML(source);
                        }

                        xmlDoc = docBuilder.parse(new InputSource(new StringReader(message)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (xmlDoc != null) {
                        Element element = xmlDoc.getDocumentElement();
                        MirthTreeNode top;

                        if (messageDescription.length() > 0) {
                            top = new MirthTreeNode(messageName + " (" + messageDescription + ")");
                        } else {
                            top = new MirthTreeNode(messageName);
                        }

                        processAttributes(element, top);

                        NodeList children = element.getChildNodes();

                        for (int i = 0; i < children.getLength(); i++) {
                            processXmlElement(dataType, children.item(i), top);
                        }

                        createTree(top);
                        filter();
                    } else {
                        setInvalidMessage(messageType);
                    }
                    break;

                case RAW:
                    MirthTreeNode top = new MirthTreeNode("Raw Data.");
                    createTree(top);
                    filter();
                    break;
            }
        } else {
            clearMessage();
        }
    }

    /**
     * Shows the trigger-button popup menu.
     */
    private void showTreePopupMenu(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            int row = tree.getRowForLocation(evt.getX(), evt.getY());
            tree.setSelectionRow(row);

            popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    /**
     * Updates the panel with a new Message.
     */
    private void createTree(MirthTreeNode top) {
        tree = new MirthTree(top, _dropPrefix, _dropSuffix);
        tree.setDragEnabled(true);
        tree.setTransferHandler(new TreeTransferHandler());
        tree.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                if (tree.getSelectionPath() != null) {
                    TreePath tp = tree.getSelectionPath();
                    TreeNode tn = (TreeNode) tp.getLastPathComponent();
                    if (tn.isLeaf()) {
                        /*
                         * Update whether the accelerator key is pressed when dragging is started.
                         * This is because a release may have never been triggered the last time it
                         * was pressed if it was released during a mouse drag.
                         */
                        PlatformUI.MIRTH_FRAME.updateAcceleratorKeyPressed(evt);
                    }
                }
            }

            public void mouseMoved(MouseEvent evt) {
                int row = tree.getRowForLocation(evt.getPoint().x, evt.getPoint().y);

                if (!popupMenu.isShowing() && row >= 0 && row < tree.getRowCount()) {
                    tree.setSelectionRow(row);
                }
            }
        });
        tree.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {
                if (!popupMenu.isShowing()) {
                    tree.clearSelection();
                }
            }

            public void mousePressed(MouseEvent e) {
                showTreePopupMenu(e);
            }

            public void mouseReleased(MouseEvent e) {
                showTreePopupMenu(e);
            }
        });

        try {
            tree.setScrollsOnExpand(true);
            treePane.setViewportView(tree);
            tree.revalidate();
        } catch (Exception e) {
            logger.error(e);
        }

        PlatformUI.MIRTH_FRAME.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private MirthTreeNode processJsonElement(String dataType, String fieldName, JsonNode element, MirthTreeNode mtn, boolean parentIsArray) {

        MirthTreeNode currentNode = new MirthTreeNode(fieldName);
        currentNode.setSerializationType(SerializationType.JSON);

        if (parentIsArray) {
            currentNode.setArrayElement(true);
        }

        if (element.isArray()) {
            ArrayNode arrNode = (ArrayNode) element;
            currentNode.setJSONType(JSONType.ARRAY);

            MirthTreeNode childNode = new MirthTreeNode("");
            childNode.setSerializationType(SerializationType.JSON);
            childNode.setArrayElement(true);
            currentNode.add(childNode);

            // If array is empty, add blank node. Otherwise, process each element.
            if (arrNode.get(0) == null) {
                childNode.setUserObject(EMPTY);
            } else {
                childNode.setUserObject(fieldName);

                for (int i = 0; i < arrNode.size(); i++) {
                    // The node name is the index of the array.
                    String arrDescription = "[" + i + "]";
                    // Include the field name as long as the parent isn't also an array.
                    if (!parentIsArray) {
                        arrDescription = fieldName + " " + arrDescription;
                    }
                    processJsonElement(dataType, arrDescription, arrNode.get(i), currentNode, true);
                }
            }
        } else {
            MirthTreeNode childNode;
            // Check if the value of this node "other things" is a container node
            if (element.isValueNode()) {
                // If the element is textual and not blank, use the element's value
                childNode = new MirthTreeNode(element.toString());
            } else if (element.isContainerNode() && element.size() > 0) {
                // If the element is an array or a JSON object and not empty, use the element's field name
                childNode = new MirthTreeNode(fieldName);
            } else {
                childNode = new MirthTreeNode(EMPTY);
            }

            // Set type as object
            if (element.isObject()) {
                currentNode.setJSONType(JSONType.OBJECT);
            }

            childNode.setSerializationType(SerializationType.JSON);
            currentNode.add(childNode);

            for (Iterator<Entry<String, JsonNode>> children = element.fields(); children.hasNext();) {
                Entry<String, JsonNode> child = children.next();
                processJsonElement(dataType, child.getKey(), child.getValue(), currentNode, false);
            }
        }

        if (mtn != null) {
            mtn.setSerializationType(SerializationType.JSON);
            mtn.add(currentNode);
        }

        return currentNode;
    }

    private void processXmlElement(String dataType, Object elo, MirthTreeNode mtn) {
        if (elo instanceof Element) {
            Element element = (Element) elo;
            String description;
            MirthTreeNode currentNode = new MirthTreeNode(LoadedExtensions.getInstance().getDataTypePlugins().get(dataType).getNodeText(vocabulary, element.getNodeName()));

            String text = "";
            if (element.hasChildNodes()) {
                text = element.getFirstChild().getNodeValue();
                if ((text == null) || (text.equals("") || text.trim().length() == 0)) {
                    currentNode.add(new MirthTreeNode(element.getNodeName()));
                } else {
                    currentNode.add(new MirthTreeNode(text));
                }
            } else {
                int minLevel = LoadedExtensions.getInstance().getDataTypePlugins().get(dataType).getMinTreeLevel();
                if (minLevel > 0) {
                    String regex = ".*";
                    for (int i = 0; i < minLevel; i++) {
                        regex += "\\..*";
                    }

                    // build regex
                    if (!element.getNodeName().matches(regex)) {
                        // We have empty node and possibly empty children
                        // Add the sub-node handler (SEG.1)
                        currentNode.add(new MirthTreeNode(element.getNodeName()));
                        // Add a sub node (SEG.1.1)
                        String newNodeName = element.getNodeName() + ".1";
                        description = vocabulary.getDescription(newNodeName);
                        MirthTreeNode parentNode;
                        if (description != null && description.length() > 0) {
                            parentNode = new MirthTreeNode(newNodeName + " (" + description + ")");
                        } else {
                            parentNode = new MirthTreeNode(newNodeName);
                        }
                        parentNode.add(new MirthTreeNode(EMPTY));
                        currentNode.add(parentNode);

                    } else {
                        currentNode.add(new MirthTreeNode(EMPTY));
                    }
                } else {
                    currentNode.add(new MirthTreeNode(EMPTY));
                }

            }

            processAttributes(element, currentNode);

            NodeList children = element.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                processXmlElement(dataType, children.item(i), currentNode);
            }
            mtn.add(currentNode);
        }
    }

    private void processAttributes(Element el, MirthTreeNode dmtn) {
        NamedNodeMap atts = el.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Attr att = (Attr) atts.item(i);
            MirthTreeNode attNode = new MirthTreeNode("@" + att.getName());
            attNode.add(new MirthTreeNode(att.getValue()));
            dmtn.add(attNode);
        }
    }

    public class TreeTransferHandler extends TransferHandler {

        protected Transferable createTransferable(JComponent c) {
            if (c != null) {
                try {
                    TreePath path = ((MirthTree) c).getSelectionPath();
                    if (path == null) {
                        return null;
                    }
                    TreeNode tp = (TreeNode) path.getLastPathComponent();
                    if (tp == null) {
                        return null;
                    }
                    if (!tp.isLeaf()) {
                        return null;
                    }

                    if (_dropPrefix.equals(MessageTreePanel.MAPPER_PREFIX)) {
                        return new TreeTransferable(editorPane, tp, _dropPrefix, _dropSuffix, TreeTransferable.MAPPER_DATA_FLAVOR);
                    } else {
                        return new TreeTransferable(editorPane, tp, _dropPrefix, _dropSuffix, TreeTransferable.MESSAGE_BUILDER_DATA_FLAVOR);
                    }
                } catch (ClassCastException cce) {
                    return null;
                }
            } else {
                return null;
            }
        }

        public int getSourceActions(JComponent c) {
            return COPY;
        }

        public boolean canImport(JComponent c, DataFlavor[] df) {
            return false;
        }
    }

    public void clearMessage() {
        MirthTreeNode top = new MirthTreeNode("Enter a message template.");
        MirthTree tree = new MirthTree(top, _dropPrefix, _dropSuffix);
        treePane.setViewportView(tree);
        revalidate();
    }

    public void setInvalidMessage(String messageType) {
        MirthTreeNode top = new MirthTreeNode("Template is not valid " + messageType + ".");
        MirthTree tree = new MirthTree(top, _dropPrefix, _dropSuffix);
        treePane.setViewportView(tree);
        revalidate();
    }

    // @formatter:off
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        filterTextBox = new javax.swing.JTextField();
        treePane = new javax.swing.JScrollPane();
        tree = new com.mirth.connect.client.ui.components.MirthTree();
        exact = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1), "Message Tree", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel1.setText("Filter:");

        treePane.setViewportView(tree);

        exact.setBackground(new java.awt.Color(255, 255, 255));
        exact.setText("Match Exact");
        exact.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        exact.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(treePane, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterTextBox, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exact)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(filterTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exact))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treePane, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // @formatter:on

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox exact;
    private javax.swing.JTextField filterTextBox;
    private javax.swing.JLabel jLabel1;
    private com.mirth.connect.client.ui.components.MirthTree tree;
    private javax.swing.JScrollPane treePane;
    // End of variables declaration//GEN-END:variables
}
