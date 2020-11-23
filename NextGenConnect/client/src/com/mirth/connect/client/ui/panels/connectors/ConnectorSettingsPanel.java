/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui.panels.connectors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.glassfish.jersey.internal.util.ReflectionHelper;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.ui.ConnectorTypeDecoration;
import com.mirth.connect.client.ui.PlatformUI;
import com.mirth.connect.client.ui.VariableListHandler.TransferMode;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;

public abstract class ConnectorSettingsPanel extends JPanel {

    protected ConnectorPanel connectorPanel;

    private Map<String, SwingWorker<Object, Void>> workerMap = new ConcurrentHashMap<String, SwingWorker<Object, Void>>();

    /**
     * Gets the name of the connector
     */
    public abstract String getConnectorName();

    /**
     * Gets all of the current data in the connector's form.
     */
    public abstract ConnectorProperties getProperties();

    /**
     * Sets all of the current data in the connector's form to the data in the properties object
     * parameter
     */
    public abstract void setProperties(ConnectorProperties properties);

    /**
     * Gets all of the default connector properties
     */
    public abstract ConnectorProperties getDefaults();

    /**
     * Checks to see if the properties in the connector are all valid. Highlights fields that are
     * not valid if highlight=true.
     * 
     * @param props
     * @param highlight
     * @return
     */
    public abstract boolean checkProperties(ConnectorProperties properties, boolean highlight);

    /**
     * Resets the highlighting on fields that could be highlighted.
     */
    public abstract void resetInvalidProperties();

    /**
     * Runs any custom validation that has been created on the connector. Returns null if
     * successful, and a String error message otherwise. Also validates the form and highlights
     * invalid fields if highlight=true.
     * 
     * @param properties
     * @return
     */
    public String doValidate(ConnectorProperties properties, boolean highlight) {
        return null;
    }

    public TransferMode getTransferMode() {
        return TransferMode.VELOCITY;
    }

    public String getRequiredInboundDataType() {
        return null;
    }

    public String getRequiredOutboundDataType() {
        return null;
    }

    public String getInitialInboundDataType() {
        return null;
    }

    public String getInitialOutboundDataType() {
        return null;
    }

    public String getInitialInboundResponseDataType() {
        return null;
    }

    public String getInitialOutboundResponseDataType() {
        return null;
    }

    public List<String> getScripts(ConnectorProperties properties) {
        return new ArrayList<String>();
    }

    /**
     * An update notification that a specific field was updated. Only some fields are implemented to
     * call this method.
     * 
     * @param field
     */
    public void updatedField(String field) {}

    /**
     * Get the this connector's filled properties. This contains all of the properties this
     * connector inherits.
     * 
     * @return
     */
    public ConnectorProperties getFilledProperties() {
        return connectorPanel.getProperties();
    }

    /**
     * Returns the ConnectorPanel associated with this connector-specific panel.
     */
    public final ConnectorPanel getConnectorPanel() {
        return connectorPanel;
    }

    /**
     * Sets the ConnectorPanel associated with this connector-specific panel.
     * 
     * @param connectorPanel
     */
    public final void setConnectorPanel(ConnectorPanel connectorPanel) {
        this.connectorPanel = connectorPanel;
    }

    /**
     * Returns any special highlighting/etc. that should be done for the connector type in its
     * associated table. Returns null if no decoration should be done, or if not applicable.
     */
    public ConnectorTypeDecoration getConnectorTypeDecoration() {
        return null;
    }

    /**
     * Notifies the overall connector panel that connector type decoration updates should take
     * place.
     */
    protected final void decorateConnectorType() {
        if (connectorPanel != null) {
            connectorPanel.decorateConnectorType();
        }
    }

    /**
     * Using the decoration object parameter, performs any special highlighting/etc. that should be
     * done.
     * 
     * @param connectorTypeDecoration
     */
    public void doLocalDecoration(ConnectorTypeDecoration connectorTypeDecoration) {}

    public final <T> T getServlet(final Class<T> servletInterface, final String workerDisplayText, final String errorText) {
        return getServlet(servletInterface, workerDisplayText, errorText, null);
    }

    public final <T> T getServlet(final Class<T> servletInterface, final String workerDisplayText, final String errorText, final ResponseHandler responseHandler) {
        return getServlet(servletInterface, workerDisplayText, errorText, responseHandler, null);
    }

    /**
     * Invokes a method on a connector service in an asynchronous worker, which passes the returned
     * object to the handlePluginConnectorServiceResponse method.
     * 
     * @param method
     *            The connector service method to invoke
     * @param workerDisplayText
     *            The progress text to display while the worker is running
     * @param errorText
     *            A custom error message to display if an exception occurs during connector service
     *            invocation
     */
    @SuppressWarnings("unchecked")
    public final <T> T getServlet(final Class<T> servletInterface, final String workerDisplayText, final String errorText, final ResponseHandler responseHandler, final String workerId) {
        return (T) Proxy.newProxyInstance(AccessController.doPrivileged(ReflectionHelper.getClassLoaderPA(servletInterface)), new Class[] {
                servletInterface }, new InvocationHandler() {
                    @Override
                    public Object invoke(final Object proxy, final Method method, final Object[] args) throws ClientException {
                        final String workingId = PlatformUI.MIRTH_FRAME.startWorking(workerDisplayText);
                        final ResponseHandler responseHandlerWrapper = connectorPanel.getResponseHandler(responseHandler, method);

                        SwingWorker<Object, Void> worker = new SwingWorker<Object, Void>() {
                            @Override
                            public Object doInBackground() throws ClientException {
                                try {
                                    if (servletInterface.isAssignableFrom(PlatformUI.MIRTH_FRAME.mirthClient.getClass())) {
                                        return method.invoke(PlatformUI.MIRTH_FRAME.mirthClient, args);
                                    }
                                    return method.invoke(PlatformUI.MIRTH_FRAME.mirthClient.getServlet(servletInterface), args);
                                } catch (Throwable t) {
                                    Throwable cause = t;
                                    if (cause instanceof InvocationTargetException && cause.getCause() != null) {
                                        cause = cause.getCause();
                                    }
                                    if (cause instanceof ClientException) {
                                        throw (ClientException) cause;
                                    } else {
                                        throw new ClientException(cause);
                                    }
                                }
                            }

                            @Override
                            public void done() {
                                try {
                                    if (!isCancelled()) {
                                        try {
                                            if (responseHandlerWrapper != null) {
                                                responseHandlerWrapper.handle(get());
                                            }
                                        } catch (Exception e) {
                                            Throwable cause = e;
                                            if (e instanceof ExecutionException && e.getCause() != null) {
                                                cause = e.getCause();
                                            }

                                            PlatformUI.MIRTH_FRAME.alertThrowable(PlatformUI.MIRTH_FRAME, e, errorText + cause.getMessage());
                                        }
                                    }
                                } finally {
                                    PlatformUI.MIRTH_FRAME.stopWorking(workingId);
                                    if (workerId != null) {
                                        workerMap.remove(workerId);
                                    }
                                }
                            }
                        };

                        if (workerId != null) {
                            workerMap.put(workerId, worker);
                        }

                        worker.execute();

                        // Make sure to return the right type
                        if (method.getReturnType().isPrimitive()) {
                            return method.getReturnType() == boolean.class ? false : (byte) 0x00;
                        }
                        return null;
                    }
                });
    }

    public final String getChannelId() {
        return PlatformUI.MIRTH_FRAME.channelEditPanel.currentChannel.getId();
    }

    public final String getChannelName() {
        return PlatformUI.MIRTH_FRAME.channelEditPanel.currentChannel.getName();
    }

    public SwingWorker<Object, Void> getWorker(String workerId) {
        return workerMap.get(workerId);
    }
}
