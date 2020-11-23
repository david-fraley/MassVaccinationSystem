/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.client.core.VersionMismatchException;
import com.mirth.connect.model.ConnectorMetaData;
import com.mirth.connect.model.ExtensionPermission;
import com.mirth.connect.model.MetaData;
import com.mirth.connect.model.PluginClass;
import com.mirth.connect.model.PluginClassCondition;
import com.mirth.connect.model.PluginMetaData;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.plugins.AuthorizationPlugin;
import com.mirth.connect.plugins.ChannelPlugin;
import com.mirth.connect.plugins.CodeTemplateServerPlugin;
import com.mirth.connect.plugins.DataTypeServerPlugin;
import com.mirth.connect.plugins.MultiFactorAuthenticationPlugin;
import com.mirth.connect.plugins.ResourcePlugin;
import com.mirth.connect.plugins.ServerPlugin;
import com.mirth.connect.plugins.ServicePlugin;
import com.mirth.connect.plugins.TransmissionModeProvider;
import com.mirth.connect.server.ExtensionLoader;
import com.mirth.connect.server.extprops.ExtensionStatuses;
import com.mirth.connect.server.migration.Migrator;
import com.mirth.connect.server.util.DatabaseUtil;
import com.mirth.connect.server.util.ResourceUtil;
import com.mirth.connect.server.util.ServerUUIDGenerator;

public class DefaultExtensionController extends ExtensionController {
    private Logger logger = Logger.getLogger(this.getClass());
    private ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();

    // these are plugins for specific extension points, keyed by plugin name
    // (not path)
    private List<ServerPlugin> serverPlugins = new ArrayList<ServerPlugin>();
    private Map<String, ServicePlugin> servicePlugins = new LinkedHashMap<String, ServicePlugin>();
    private Map<String, ChannelPlugin> channelPlugins = new LinkedHashMap<String, ChannelPlugin>();
    private Map<String, CodeTemplateServerPlugin> codeTemplateServerPlugins = new LinkedHashMap<String, CodeTemplateServerPlugin>();
    private Map<String, DataTypeServerPlugin> dataTypePlugins = new LinkedHashMap<String, DataTypeServerPlugin>();
    private Map<String, ResourcePlugin> resourcePlugins = new LinkedHashMap<String, ResourcePlugin>();
    private Map<String, TransmissionModeProvider> transmissionModeProviders = new LinkedHashMap<String, TransmissionModeProvider>();
    private MultiFactorAuthenticationPlugin multiFactorAuthenticationPlugin = null;
    private AuthorizationPlugin authorizationPlugin = null;
    private ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
    private ExtensionStatuses extensionStatuses = ExtensionStatuses.getInstance();

    // singleton pattern
    private static ExtensionController instance = null;

    public static ExtensionController create() {
        synchronized (DefaultExtensionController.class) {
            if (instance == null) {
                instance = ExtensionLoader.getInstance().getControllerInstance(ExtensionController.class);

                if (instance == null) {
                    instance = new DefaultExtensionController();
                }
            }

            return instance;
        }
    }

    DefaultExtensionController() {

    }

    @Override
    public void removePropertiesForUninstalledExtensions() {
        try {
            File uninstallFile = new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_PROPERTIES_FILE);

            if (uninstallFile.exists()) {
                List<String> extensionPaths = FileUtils.readLines(uninstallFile);

                for (String extensionPath : extensionPaths) {
                    configurationController.removePropertiesForGroup(extensionPath);
                }

                // delete the uninstall file when we're done
                FileUtils.deleteQuietly(uninstallFile);
            }
        } catch (Exception e) {
            logger.error("Error removing properties for uninstalled extensions.", e);
        }
    }

    @Override
    public void setDefaultExtensionStatus() {
        for (MetaData metaData : getPluginMetaData().values()) {
            if (!extensionStatuses.containsKey(metaData.getName())) {
                extensionStatuses.setEnabled(metaData.getName(), true);
            }
        }

        for (MetaData metaData : getConnectorMetaData().values()) {
            if (!extensionStatuses.containsKey(metaData.getName())) {
                extensionStatuses.setEnabled(metaData.getName(), true);
            }
        }

        /*
         * Remove extensions from the extensionProperties if they are not in the pluginMetaDataMap
         * or connectorMetaDataMap
         */
        for (String key : extensionStatuses.keySet()) {
            if (!getPluginMetaData().containsKey(key) && !getConnectorMetaData().containsKey(key)) {
                extensionStatuses.remove(key);
            }
        }

        extensionStatuses.save();
    }

    @Override
    public void initPlugins() {
        // Order all the plugins by their weight before loading any of them.
        Map<String, String> pluginNameMap = new HashMap<String, String>();
        NavigableMap<Integer, List<String>> weightedPlugins = new TreeMap<Integer, List<String>>();
        for (PluginMetaData pmd : getPluginMetaData().values()) {
            if (isExtensionEnabled(pmd.getName())) {
                if (pmd.getServerClasses() != null) {
                    for (PluginClass pluginClass : pmd.getServerClasses()) {
                        String clazzName = pluginClass.getName();
                        int weight = pluginClass.getWeight();
                        String conditionClass = pluginClass.getConditionClass();

                        boolean accept = true;
                        if (StringUtils.isNotBlank(conditionClass)) {
                            try {
                                accept = ((PluginClassCondition) Class.forName(conditionClass).newInstance()).accept(pluginClass);
                            } catch (Exception e) {
                                logger.warn("Error instantiating plugin condition class \"" + conditionClass + "\".");
                            }
                        }

                        if (accept) {
                            pluginNameMap.put(clazzName, pmd.getName());

                            List<String> classList = weightedPlugins.get(weight);
                            if (classList == null) {
                                classList = new ArrayList<String>();
                                weightedPlugins.put(weight, classList);
                            }

                            classList.add(clazzName);
                        }
                    }
                }
            } else {
                logger.warn("Plugin \"" + pmd.getName() + "\" is not enabled.");
            }
        }

        // Load the plugins in order of their weight
        for (List<String> classList : weightedPlugins.descendingMap().values()) {
            for (String clazzName : classList) {
                String pluginName = pluginNameMap.get(clazzName);

                try {
                    ServerPlugin serverPlugin = (ServerPlugin) Class.forName(clazzName).newInstance();

                    if (serverPlugin instanceof ServicePlugin) {
                        ServicePlugin servicePlugin = (ServicePlugin) serverPlugin;
                        /*
                         * load any properties that may currently be in the database
                         */
                        Properties currentProperties = getPluginProperties(pluginName);
                        /* get the default properties for the plugin */
                        Properties defaultProperties = servicePlugin.getDefaultProperties();

                        /*
                         * if there are any properties that not currently set, set them to the the
                         * default
                         */
                        for (Object key : defaultProperties.keySet()) {
                            if (!currentProperties.containsKey(key)) {
                                currentProperties.put(key, defaultProperties.get(key));
                            }
                        }

                        /* save the properties to the database */
                        setPluginProperties(pluginName, currentProperties);

                        /*
                         * initialize the plugin with those properties and add it to the list of
                         * loaded plugins
                         */
                        servicePlugin.init(currentProperties);
                        servicePlugins.put(servicePlugin.getPluginPointName(), servicePlugin);
                        serverPlugins.add(servicePlugin);
                        logger.debug("sucessfully loaded server plugin: " + serverPlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof ChannelPlugin) {
                        ChannelPlugin channelPlugin = (ChannelPlugin) serverPlugin;
                        channelPlugins.put(channelPlugin.getPluginPointName(), channelPlugin);
                        serverPlugins.add(channelPlugin);
                        logger.debug("sucessfully loaded server channel plugin: " + serverPlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof CodeTemplateServerPlugin) {
                        CodeTemplateServerPlugin codeTemplateServerPlugin = (CodeTemplateServerPlugin) serverPlugin;
                        codeTemplateServerPlugins.put(codeTemplateServerPlugin.getPluginPointName(), codeTemplateServerPlugin);
                        serverPlugins.add(codeTemplateServerPlugin);
                        logger.debug("sucessfully loaded server code template plugin: " + serverPlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof DataTypeServerPlugin) {
                        DataTypeServerPlugin dataTypePlugin = (DataTypeServerPlugin) serverPlugin;
                        dataTypePlugins.put(dataTypePlugin.getPluginPointName(), dataTypePlugin);
                        serverPlugins.add(dataTypePlugin);
                        logger.debug("sucessfully loaded server data type plugin: " + serverPlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof ResourcePlugin) {
                        ResourcePlugin resourcePlugin = (ResourcePlugin) serverPlugin;
                        resourcePlugins.put(resourcePlugin.getPluginPointName(), resourcePlugin);
                        serverPlugins.add(resourcePlugin);
                        logger.debug("Successfully loaded resource plugin: " + resourcePlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof TransmissionModeProvider) {
                        TransmissionModeProvider transmissionModeProvider = (TransmissionModeProvider) serverPlugin;
                        transmissionModeProviders.put(transmissionModeProvider.getPluginPointName(), transmissionModeProvider);
                        serverPlugins.add(transmissionModeProvider);
                        logger.debug("Successfully loaded transmission mode provider plugin: " + transmissionModeProvider.getPluginPointName());
                    }

                    if (serverPlugin instanceof AuthorizationPlugin) {
                        AuthorizationPlugin authorizationPlugin = (AuthorizationPlugin) serverPlugin;

                        if (this.authorizationPlugin != null) {
                            throw new Exception("Multiple Authorization Plugins are not permitted.");
                        }

                        this.authorizationPlugin = authorizationPlugin;
                        serverPlugins.add(authorizationPlugin);
                        logger.debug("sucessfully loaded server authorization plugin: " + serverPlugin.getPluginPointName());
                    }

                    if (serverPlugin instanceof MultiFactorAuthenticationPlugin) {
                        MultiFactorAuthenticationPlugin multiFactorAuthenticationPlugin = (MultiFactorAuthenticationPlugin) serverPlugin;

                        if (this.multiFactorAuthenticationPlugin != null) {
                            throw new Exception("Multiple Multi-Factor Authentication Plugins are not permitted.");
                        }

                        this.multiFactorAuthenticationPlugin = multiFactorAuthenticationPlugin;
                        serverPlugins.add(multiFactorAuthenticationPlugin);
                        logger.debug("sucessfully loaded server multi-factor authentication plugin: " + serverPlugin.getPluginPointName());
                    }
                } catch (Exception e) {
                    logger.error("Error instantiating plugin: " + pluginName, e);
                }
            }
        }
    }

    /* These are the maps for the different types of plugins */
    /* ********************************************************************** */

    @Override
    public Map<String, ServicePlugin> getServicePlugins() {
        return servicePlugins;
    }

    @Override
    public Map<String, ChannelPlugin> getChannelPlugins() {
        return channelPlugins;
    }

    @Override
    public Map<String, CodeTemplateServerPlugin> getCodeTemplateServerPlugins() {
        return codeTemplateServerPlugins;
    }

    @Override
    public Map<String, DataTypeServerPlugin> getDataTypePlugins() {
        return dataTypePlugins;
    }

    @Override
    public Map<String, ResourcePlugin> getResourcePlugins() {
        return resourcePlugins;
    }

    @Override
    public Map<String, TransmissionModeProvider> getTransmissionModeProviders() {
        return transmissionModeProviders;
    }

    @Override
    public AuthorizationPlugin getAuthorizationPlugin() {
        return authorizationPlugin;
    }

    @Override
    public MultiFactorAuthenticationPlugin getMultiFactorAuthenticationPlugin() {
        return multiFactorAuthenticationPlugin;
    }

    /* ********************************************************************** */

    @Override
    public void setExtensionEnabled(String extensionName, boolean enabled) throws ControllerException {
        extensionStatuses.setEnabled(extensionName, enabled);
        extensionStatuses.save();
    }

    @Override
    public boolean isExtensionEnabled(String extensionName) {
        return extensionStatuses.isEnabled(extensionName);
    }

    @Override
    public void startPlugins() {
        for (ServerPlugin serverPlugin : serverPlugins) {
            serverPlugin.start();
        }

        // Get all of the server plugin extension permissions and add those to
        // the authorization controller.
        AuthorizationController authorizationController = ControllerFactory.getFactory().createAuthorizationController();

        for (ServicePlugin plugin : servicePlugins.values()) {
            if (plugin.getExtensionPermissions() != null) {
                for (ExtensionPermission extensionPermission : plugin.getExtensionPermissions()) {
                    authorizationController.addExtensionPermission(extensionPermission);
                }
            }
        }
    }

    @Override
    public void stopPlugins() {
        for (ServerPlugin serverPlugin : serverPlugins) {
            serverPlugin.stop();
        }
    }

    @Override
    public void updatePluginProperties(String name, Properties properties) {
        ServicePlugin servicePlugin = servicePlugins.get(name);

        if (servicePlugin != null) {
            servicePlugin.update(properties);
        } else {
            logger.error("Error setting properties for service plugin that has not been loaded: name=" + name);
        }
    }

    @Override
    public InstallationResult extractExtension(InputStream inputStream) {
        Throwable cause = null;
        Set<MetaData> metaDataSet = new HashSet<MetaData>();

        File installTempDir = new File(ExtensionController.getExtensionsPath(), "install_temp");

        if (!installTempDir.exists()) {
            installTempDir.mkdir();
        }

        File tempFile = null;
        FileOutputStream tempFileOutputStream = null;
        ZipFile zipFile = null;

        try {
            /*
             * create a new temp file (in the install temp dir) to store the zip file contents
             */
            tempFile = File.createTempFile(ServerUUIDGenerator.getUUID(), ".zip", installTempDir);
            // write the contents of the multipart fileitem to the temp file
            try {
                tempFileOutputStream = new FileOutputStream(tempFile);
                IOUtils.copy(inputStream, tempFileOutputStream);
            } finally {
                ResourceUtil.closeResourceQuietly(tempFileOutputStream);
            }

            // create a new zip file from the temp file
            zipFile = new ZipFile(tempFile);
            // get a list of all of the entries in the zip file
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.endsWith("plugin.xml") || entryName.endsWith("destination.xml") || entryName.endsWith("source.xml")) {
                    // parse the extension metadata xml file
                    MetaData extensionMetaData = serializer.deserialize(IOUtils.toString(zipFile.getInputStream(entry)), MetaData.class);
                    metaDataSet.add(extensionMetaData);

                    if (!extensionLoader.isExtensionCompatible(extensionMetaData)) {
                        if (cause == null) {
                            cause = new VersionMismatchException("Extension \"" + entry.getName() + "\" is not compatible with this version of Mirth Connect.");
                        }
                    }
                }
            }

            if (cause == null) {
                // reset the entries and extract
                entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    extractZipEntry(entry, installTempDir, zipFile);
                }
            }
        } catch (Throwable t) {
            cause = new ControllerException("Error extracting extension. " + t.toString(), t);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (Exception e) {
                    cause = new ControllerException(e);
                }
            }

            // delete the temp file since it is no longer needed
            FileUtils.deleteQuietly(tempFile);
        }

        return new InstallationResult(cause, metaDataSet);
    }

    /**
     * Adds the specified plugin path to a list of plugins that should be deleted on next server
     * startup. Also deletes the schema version property from the database. If this function fails
     * to add the extension path to the uninstall file, it will still continue to remove add the
     * database uninstall scripts, and the folder must be deleted manually.
     * 
     */
    @Override
    public void prepareExtensionForUninstallation(String pluginPath) throws ControllerException {
        addExtensionToUninstallFile(pluginPath);

        for (PluginMetaData plugin : getPluginMetaData().values()) {
            if (plugin.getPath().equals(pluginPath)) {
                addExtensionToUninstallPropertiesFile(plugin.getName());

                if (plugin.getMigratorClass() != null) {
                    try {
                        Migrator migrator = (Migrator) Class.forName(plugin.getMigratorClass()).newInstance();
                        migrator.setDatabaseType(ConfigurationController.getInstance().getDatabaseType());
                        migrator.setDefaultScriptPath("extensions/" + plugin.getPath());
                        appendToUninstallScript(migrator.getUninstallStatements());
                    } catch (Exception e) {
                        logger.error("Failed to retrieve uninstall database statements for plugin: " + pluginPath, e);
                    }
                }
            }
        }
    }

    /*
     * Parses the uninstallation script and returns a list of statements.
     */
    private List<String> parseUninstallScript(String script) {
        List<String> scriptList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean blankLine = false;
        Scanner scanner = new Scanner(script);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (StringUtils.isNotBlank(line)) {
                sb.append(line + " ");
            } else {
                blankLine = true;
            }

            if (blankLine || !scanner.hasNextLine()) {
                scriptList.add(sb.toString().trim());
                blankLine = false;
                sb.delete(0, sb.length());
            }
        }

        return scriptList;
    }

    /*
     * append the extension path name to a list of extensions that should be deleted on next startup
     * by MirthLauncher
     */
    private void addExtensionToUninstallFile(String pluginPath) {
        File uninstallFile = new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_FILE);
        FileWriter writer = null;

        try {
            writer = new FileWriter(uninstallFile, true);
            writer.write(pluginPath + System.getProperty("line.separator"));
        } catch (IOException e) {
            logger.error("Error adding extension to uninstall file: " + pluginPath, e);
        } finally {
            ResourceUtil.closeResourceQuietly(writer);
        }
    }

    private void addExtensionToUninstallPropertiesFile(String pluginName) {
        File uninstallFile = new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_PROPERTIES_FILE);
        FileWriter writer = null;

        try {
            writer = new FileWriter(uninstallFile, true);
            writer.write(pluginName + System.getProperty("line.separator"));
        } catch (IOException e) {
            logger.error("Error adding extension to uninstall properties file: " + pluginName, e);
        } finally {
            ResourceUtil.closeResourceQuietly(writer);
        }
    }

    private String getUninstallScriptForCurrentDatabase(String pluginSqlScripts) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(pluginSqlScripts)));
        Element uninstallElement = (Element) document.getElementsByTagName("uninstall").item(0);
        String databaseType = ControllerFactory.getFactory().createConfigurationController().getDatabaseType();
        NodeList scriptNodes = uninstallElement.getElementsByTagName("script");
        String script = null;

        for (int i = 0; i < scriptNodes.getLength(); i++) {
            Node scriptNode = scriptNodes.item(i);
            Node scriptType = scriptNode.getAttributes().getNamedItem("type");
            String[] databaseTypes = scriptType.getTextContent().split(",");

            for (int j = 0; j < databaseTypes.length; j++) {
                if (databaseTypes[j].equals("all") || databaseTypes[j].equals(databaseType)) {
                    script = scriptNode.getTextContent().trim();
                }
            }
        }

        return script;
    }

    @Override
    public void setPluginProperties(String pluginName, Properties properties, boolean mergeProperties) throws ControllerException {
        if (!mergeProperties) {
            configurationController.removePropertiesForGroup(pluginName);
        }

        for (Object name : properties.keySet()) {
            configurationController.saveProperty(pluginName, (String) name, (String) properties.get(name));
        }
    }

    @Override
    public Properties getPluginProperties(String pluginName, Set<String> propertyKeys) throws ControllerException {
        return ControllerFactory.getFactory().createConfigurationController().getPropertiesForGroup(pluginName, propertyKeys);
    }

    @Override
    public Map<String, ConnectorMetaData> getConnectorMetaData() {
        return extensionLoader.getConnectorMetaData();
    }

    @Override
    public Map<String, PluginMetaData> getPluginMetaData() {
        return extensionLoader.getPluginMetaData();
    }

    @Override
    public ConnectorMetaData getConnectorMetaDataByProtocol(String protocol) {
        return extensionLoader.getConnectorProtocols().get(protocol);
    }

    @Override
    public ConnectorMetaData getConnectorMetaDataByTransportName(String transportName) {
        return extensionLoader.getConnectorMetaData().get(transportName);
    }

    @Override
    public Map<String, MetaData> getInvalidMetaData() {
        return extensionLoader.getInvalidMetaData();
    }

    /**
     * Executes the script that removes that database tables for plugins that are marked for
     * removal. The actual removal of the plugin directory happens in MirthLauncher.java, before
     * they can be added to the server classpath.
     * 
     */
    @Override
    public void uninstallExtensions() {
        try {
            DatabaseUtil.executeScript(readUninstallScript(), true);
        } catch (Exception e) {
            logger.error("Error uninstalling extensions.", e);
        }

        // delete the uninstall scripts file
        FileUtils.deleteQuietly(new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_SCRIPTS_FILE));
    }

    private void appendToUninstallScript(List<String> uninstallStatements) throws IOException {
        if (uninstallStatements != null) {
            List<String> uninstallScripts = readUninstallScript();
            uninstallScripts.addAll(uninstallStatements);
            File uninstallScriptsFile = new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_SCRIPTS_FILE);
            FileUtils.writeStringToFile(uninstallScriptsFile, serializer.serialize(uninstallScripts));
        }
    }

    /*
     * This MUST return an empty list if there is no uninstall file.
     */
    @SuppressWarnings("unchecked")
    private List<String> readUninstallScript() throws IOException {
        File uninstallScriptsFile = new File(getExtensionsPath(), EXTENSIONS_UNINSTALL_SCRIPTS_FILE);
        List<String> scripts = new ArrayList<String>();

        if (uninstallScriptsFile.exists()) {
            scripts = serializer.deserializeList(FileUtils.readFileToString(uninstallScriptsFile), String.class);
        }

        return scripts;
    }

    public List<String> getClientLibraries() {
        List<String> clientLibFilenames = new ArrayList<String>();
        File clientLibDir = new File("client-lib");

        if (!clientLibDir.exists() || !clientLibDir.isDirectory()) {
            clientLibDir = new File("build/client-lib");
        }

        if (clientLibDir.exists() && clientLibDir.isDirectory()) {
            Collection<File> clientLibs = FileUtils.listFiles(clientLibDir, new SuffixFileFilter(".jar"), FileFilterUtils.falseFileFilter());

            for (File clientLib : clientLibs) {
                clientLibFilenames.add(FilenameUtils.getName(clientLib.getName()));
            }
        } else {
            logger.error("Could not find client-lib directory: " + clientLibDir.getAbsolutePath());
        }

        return clientLibFilenames;
    }

    public List<ServerPlugin> getServerPlugins() {
        return serverPlugins;
    }

    void extractZipEntry(ZipEntry entry, File installTempDir, ZipFile zipFile) throws IOException {
        String canonicalDestinationDirPath = installTempDir.getCanonicalPath();
        File destinationfile = new File(installTempDir, entry.getName());
        String canonicalDestinationFile = destinationfile.getCanonicalPath();

        if (!canonicalDestinationFile.startsWith(canonicalDestinationDirPath + File.separator)) {
            throw new ZipException("Zip file is attempting to traverse out of base directory");
        }

        if (entry.isDirectory()) {
            /*
             * assume directories are stored parents first then children.
             * 
             * TODO: this is not robust, just for demonstration purposes.
             */
            File directory = new File(installTempDir, entry.getName());
            directory.mkdir();
        } else {
            // otherwise, write the file out to the install temp dir
            InputStream zipInputStream = null;
            FileOutputStream fileOutputStream = null;
            OutputStream outputStream = null;
            try {
                zipInputStream = zipFile.getInputStream(entry);
                fileOutputStream = new FileOutputStream(new File(installTempDir, entry.getName()));
                outputStream = new BufferedOutputStream(fileOutputStream);
                IOUtils.copy(zipInputStream, outputStream);
            } finally {
                ResourceUtil.closeResourceQuietly(zipInputStream);
                ResourceUtil.closeResourceQuietly(fileOutputStream);
                ResourceUtil.closeResourceQuietly(outputStream);
            }
        }
    }
}
