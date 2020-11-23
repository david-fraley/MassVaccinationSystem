/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.controllers;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.model.ConnectorMetaData;
import com.mirth.connect.model.MetaData;
import com.mirth.connect.model.PluginMetaData;
import com.mirth.connect.plugins.AuthorizationPlugin;
import com.mirth.connect.plugins.ChannelPlugin;
import com.mirth.connect.plugins.CodeTemplateServerPlugin;
import com.mirth.connect.plugins.DataTypeServerPlugin;
import com.mirth.connect.plugins.MultiFactorAuthenticationPlugin;
import com.mirth.connect.plugins.ResourcePlugin;
import com.mirth.connect.plugins.ServerPlugin;
import com.mirth.connect.plugins.ServicePlugin;
import com.mirth.connect.plugins.TransmissionModeProvider;
import com.mirth.connect.server.tools.ClassPathResource;

public abstract class ExtensionController extends Controller {
    /**
     * If in an IDE, extensions will be on the classpath as a resource. If that's the case, use that
     * directory. Otherwise, use the mirth home directory and append extensions.
     * 
     * @return
     */
    public static String getExtensionsPath() {
        if (ClassPathResource.getResourceURI("extensions") != null) {
            return ClassPathResource.getResourceURI("extensions").getPath() + File.separator;
        } else {
            return ControllerFactory.getFactory().createConfigurationController().getBaseDir() + File.separator + "extensions" + File.separator;
        }
    }

    public static final String EXTENSIONS_UNINSTALL_FILE = "uninstall";
    public static final String EXTENSIONS_UNINSTALL_PROPERTIES_FILE = "uninstallProperties";
    public static final String EXTENSIONS_UNINSTALL_SCRIPTS_FILE = "uninstallScripts";

    public static ExtensionController getInstance() {
        return ControllerFactory.getFactory().createExtensionController();
    }

    /**
     * Returns a list of the names of all of the jar files in the client library directory. If the
     * "client-lib" directory does not exist, it will check build/client-lib and use that to scan
     * for jar files.
     * 
     * @return
     */
    public abstract List<String> getClientLibraries();

    public abstract void setDefaultExtensionStatus();

    /**
     * Iterates through all of the plugin metadata that was loaded on startup.
     */
    public abstract void initPlugins();

    /**
     * Sets the status of an extension.
     * 
     * @throws ControllerException
     */
    public abstract void setExtensionEnabled(String name, boolean enabled) throws ControllerException;

    /**
     * Returns true if the extension (either plugin or connector) with the specified name is
     * enabled, false otherwise.
     * 
     * @param name
     * @return
     */
    public abstract boolean isExtensionEnabled(String name);

    /**
     * Invokes the start method on all loaded server plugins.
     */
    public abstract void startPlugins();

    /**
     * Invoked the stop method on all loaded server plugins.
     */
    public abstract void stopPlugins();

    // ************************************************************
    // Plugins
    // ************************************************************

    /**
     * Returns the metadata for all plugins.
     */
    public abstract Map<String, PluginMetaData> getPluginMetaData();

    // TODO: the following two should be combined

    /**
     * Updates the properties for the specified server plugin. Note that this calls the update on
     * the plugin itself and does not store the properties in the database.
     */
    public abstract void updatePluginProperties(String name, Properties properties);

    /**
     * Stores the properties for the specified plugin in the database. Removes any properties
     * beforehand.
     *
     * @param name
     * @param properties
     * @throws ControllerException
     */
    public void setPluginProperties(String name, Properties properties) throws ControllerException {
        setPluginProperties(name, properties, false);
    }

    /**
     * Stores the properties for the specified plugin in the database.
     * @param name
     * @param properties
     * @param mergeProperties true to merge properties into the database. false to replace all values.
     * @throws ControllerException
     */
    public abstract void setPluginProperties(String name, Properties properties, boolean mergeProperties) throws ControllerException;

    /**
     * Returns properties for the specified plugin from the database.
     * 
     * @param name
     * @return
     * @throws ControllerException
     */
    public Properties getPluginProperties(String name) throws ControllerException {
        return getPluginProperties(name, null);
    }

    /**
     * Returns properties for the specified plugin from the database filtered by propertyKeys.
     *
     * @param name
     * @param propertyKeys
     * @return
     * @throws ControllerException
     */
    public abstract Properties getPluginProperties(String name, Set<String> propertyKeys) throws ControllerException;

    public abstract Map<String, MetaData> getInvalidMetaData();

    // ************************************************************
    // Connectors
    // ************************************************************

    public abstract Map<String, ConnectorMetaData> getConnectorMetaData();

    public abstract ConnectorMetaData getConnectorMetaDataByProtocol(String protocol);

    public abstract ConnectorMetaData getConnectorMetaDataByTransportName(String transportName);

    // ************************************************************
    // Extension installation and unistallation
    // ************************************************************

    /**
     * Extracts the contents of the uploaded zip file into the installation temp directory to be
     * picked up by MirthLauncher on next restart.
     */
    public abstract InstallationResult extractExtension(InputStream inputStream);

    /**
     * Adds the extension's SQL uninstall script to the server uninstallation script. Also adds the
     * extenion path to the uninstallation list that is read on startup.
     * 
     * @param extensionPath
     * @throws ControllerException
     */
    public abstract void prepareExtensionForUninstallation(String extensionPath) throws ControllerException;

    /**
     * Executes the server uninstallation SQL script.
     */
    public abstract void uninstallExtensions();

    /**
     * Removes all properties stored in the database for extensions that were marked for
     * uninstalltion.
     */
    public abstract void removePropertiesForUninstalledExtensions();

    // ************************************************************
    // Maps for different plugins
    // ************************************************************

    public abstract Map<String, ServicePlugin> getServicePlugins();

    public abstract Map<String, ChannelPlugin> getChannelPlugins();

    public abstract Map<String, CodeTemplateServerPlugin> getCodeTemplateServerPlugins();

    public abstract Map<String, DataTypeServerPlugin> getDataTypePlugins();

    public abstract Map<String, ResourcePlugin> getResourcePlugins();

    public abstract Map<String, TransmissionModeProvider> getTransmissionModeProviders();

    public abstract AuthorizationPlugin getAuthorizationPlugin();

    public abstract MultiFactorAuthenticationPlugin getMultiFactorAuthenticationPlugin();

    public abstract List<ServerPlugin> getServerPlugins();

    public class InstallationResult {
        private Throwable cause;
        private Set<MetaData> metaData;

        public InstallationResult(Throwable cause, Set<MetaData> metaData) {
            this.cause = cause;
            this.metaData = metaData;
        }

        public Throwable getCause() {
            return cause;
        }

        public Set<MetaData> getMetaData() {
            return metaData;
        }
    }
}
