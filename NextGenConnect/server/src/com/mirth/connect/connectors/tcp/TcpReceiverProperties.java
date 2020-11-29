/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.tcp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.channel.ListenerConnectorProperties;
import com.mirth.connect.donkey.model.channel.ListenerConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.channel.SourceConnectorProperties;
import com.mirth.connect.donkey.model.channel.SourceConnectorPropertiesInterface;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.purge.PurgeUtil;
import com.mirth.connect.model.transmission.TransmissionModeProperties;
import com.mirth.connect.model.transmission.framemode.FrameModeProperties;
import com.mirth.connect.util.CharsetUtils;
import com.mirth.connect.util.TcpUtil;

@SuppressWarnings("serial")
public class TcpReceiverProperties extends ConnectorProperties implements ListenerConnectorPropertiesInterface, SourceConnectorPropertiesInterface {
    private ListenerConnectorProperties listenerConnectorProperties;
    private SourceConnectorProperties sourceConnectorProperties;

    public static final String PROTOCOL = "TCP";
    public static final String NAME = "TCP Listener";
    public static final int SAME_CONNECTION = 0;
    public static final int NEW_CONNECTION = 1;
    public static final int NEW_CONNECTION_ON_RECOVERY = 2;

    private TransmissionModeProperties transmissionModeProperties;
    private boolean serverMode;
    private String remoteAddress;
    private String remotePort;
    private boolean overrideLocalBinding;
    private String reconnectInterval;
    private String receiveTimeout;
    private String bufferSize;
    private String maxConnections;
    private boolean keepConnectionOpen;
    private boolean dataTypeBinary;
    private String charsetEncoding;
    private int respondOnNewConnection;
    private String responseAddress;
    private String responsePort;
    private Set<ConnectorPluginProperties> responseConnectorPluginProperties;

    public TcpReceiverProperties() {
        listenerConnectorProperties = new ListenerConnectorProperties("6661");
        sourceConnectorProperties = new SourceConnectorProperties(SourceConnectorProperties.RESPONSE_SOURCE_TRANSFORMED);
        sourceConnectorProperties.setFirstResponse(true);

        FrameModeProperties frameModeProperties = new FrameModeProperties("MLLP");
        frameModeProperties.setStartOfMessageBytes(TcpUtil.DEFAULT_LLP_START_BYTES);
        frameModeProperties.setEndOfMessageBytes(TcpUtil.DEFAULT_LLP_END_BYTES);
        this.transmissionModeProperties = frameModeProperties;

        this.serverMode = true;
        this.remoteAddress = "";
        this.remotePort = "";
        this.overrideLocalBinding = false;
        this.reconnectInterval = "5000";
        this.receiveTimeout = "0";
        this.bufferSize = "65536";
        this.maxConnections = "10";
        this.keepConnectionOpen = true;
        this.dataTypeBinary = false;
        this.charsetEncoding = CharsetUtils.DEFAULT_ENCODING;
        this.respondOnNewConnection = SAME_CONNECTION;
        this.responseAddress = "";
        this.responsePort = "";
    }

    @Override
    public SourceConnectorProperties getSourceConnectorProperties() {
        return sourceConnectorProperties;
    }

    public void setSourceConnectorProperties(SourceConnectorProperties sourceConnectorProperties) {
        this.sourceConnectorProperties = sourceConnectorProperties;
    }

    @Override
    public ListenerConnectorProperties getListenerConnectorProperties() {
        return listenerConnectorProperties;
    }

    public void setListenerConnectorProperties(ListenerConnectorProperties listenerConnectorProperties) {
        this.listenerConnectorProperties = listenerConnectorProperties;
    }

    public TransmissionModeProperties getTransmissionModeProperties() {
        return transmissionModeProperties;
    }

    public void setTransmissionModeProperties(TransmissionModeProperties transmissionModeProperties) {
        this.transmissionModeProperties = transmissionModeProperties;
    }

    public boolean isServerMode() {
        return serverMode;
    }

    public void setServerMode(boolean serverMode) {
        this.serverMode = serverMode;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public boolean isOverrideLocalBinding() {
        return overrideLocalBinding;
    }

    public void setOverrideLocalBinding(boolean overrideLocalBinding) {
        this.overrideLocalBinding = overrideLocalBinding;
    }

    public String getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(String reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public String getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(String receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public String getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(String bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(String maxConnections) {
        this.maxConnections = maxConnections;
    }

    public boolean isKeepConnectionOpen() {
        return keepConnectionOpen;
    }

    public void setKeepConnectionOpen(boolean keepConnectionOpen) {
        this.keepConnectionOpen = keepConnectionOpen;
    }

    public boolean isDataTypeBinary() {
        return dataTypeBinary;
    }

    public void setDataTypeBinary(boolean dataTypeBinary) {
        this.dataTypeBinary = dataTypeBinary;
    }

    public String getCharsetEncoding() {
        return charsetEncoding;
    }

    public void setCharsetEncoding(String charsetEncoding) {
        this.charsetEncoding = charsetEncoding;
    }

    public int getRespondOnNewConnection() {
        return respondOnNewConnection;
    }

    public void setRespondOnNewConnection(int respondOnNewConnection) {
        this.respondOnNewConnection = respondOnNewConnection;
    }

    public String getResponseAddress() {
        return responseAddress;
    }

    public void setResponseAddress(String responseAddress) {
        this.responseAddress = responseAddress;
    }

    public String getResponsePort() {
        return responsePort;
    }

    public void setResponsePort(String responsePort) {
        this.responsePort = responsePort;
    }

    public Set<ConnectorPluginProperties> getResponseConnectorPluginProperties() {
        return responseConnectorPluginProperties;
    }

    public void setResponseConnectorPluginProperties(Set<ConnectorPluginProperties> responseConnectorPluginProperties) {
        this.responseConnectorPluginProperties = responseConnectorPluginProperties;
    }

    @Override
    public String getProtocol() {
        return PROTOCOL;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toFormattedString() {
        return null;
    }

    @Override
    public boolean canBatch() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public void migrate3_0_1(DonkeyElement element) {}

    @Override
    public void migrate3_0_2(DonkeyElement element) {
        String remoteAddress = "";
        String remotePort = "";

        // If client mode is enabled, get the remote address/port from the listener settings
        if (!Boolean.parseBoolean(element.getChildElement("serverMode").getTextContent())) {
            DonkeyElement listenerConnectorProperties = element.getChildElement("listenerConnectorProperties");
            DonkeyElement host = listenerConnectorProperties.getChildElement("host");
            DonkeyElement port = listenerConnectorProperties.getChildElement("port");

            remoteAddress = host.getTextContent();
            remotePort = port.getTextContent();

            // Set the local address/port to the defaults since the channel will be using the remote ones instead
            host.setTextContent("0.0.0.0");
            port.setTextContent("0");
        }

        element.addChildElementIfNotExists("remoteAddress", remoteAddress);
        element.addChildElementIfNotExists("remotePort", remotePort);
        element.addChildElementIfNotExists("overrideLocalBinding", "false");
    }

    @Override
    public void migrate3_1_0(DonkeyElement element) {
        super.migrate3_1_0(element);

        DonkeyElement processBatchElement = element.removeChild("processBatch");
        DonkeyElement sourcePropertiesElement = element.getChildElement("sourceConnectorProperties");
        if (processBatchElement != null && sourcePropertiesElement != null) {
            sourcePropertiesElement.addChildElementIfNotExists("processBatch", processBatchElement.getTextContent());
            sourcePropertiesElement.addChildElementIfNotExists("firstResponse", "true");
        }
    }

    // @formatter:off
    @Override public void migrate3_2_0(DonkeyElement element) {}
    @Override public void migrate3_3_0(DonkeyElement element) {}
    @Override public void migrate3_4_0(DonkeyElement element) {}
    @Override public void migrate3_5_0(DonkeyElement element) {}
    @Override public void migrate3_6_0(DonkeyElement element) {}
    @Override public void migrate3_7_0(DonkeyElement element) {}
    @Override public void migrate3_9_0(DonkeyElement element) {} // @formatter:on

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = super.getPurgedProperties();
        purgedProperties.put("sourceConnectorProperties", sourceConnectorProperties.getPurgedProperties());
        purgedProperties.put("transmissionModeProperties", transmissionModeProperties.getPurgedProperties());
        purgedProperties.put("serverMode", serverMode);
        purgedProperties.put("overrideLocalBinding", overrideLocalBinding);
        purgedProperties.put("reconnectInterval", PurgeUtil.getNumericValue(reconnectInterval));
        purgedProperties.put("receiveTimeout", PurgeUtil.getNumericValue(receiveTimeout));
        purgedProperties.put("bufferSize", PurgeUtil.getNumericValue(bufferSize));
        purgedProperties.put("maxConnections", PurgeUtil.getNumericValue(maxConnections));
        purgedProperties.put("keepConnectionOpen", keepConnectionOpen);
        purgedProperties.put("dataTypeBinary", dataTypeBinary);
        purgedProperties.put("charsetEncoding", charsetEncoding);
        purgedProperties.put("respondOnNewConnection", respondOnNewConnection);
        if (responseConnectorPluginProperties != null) {
            Set<Map<String, Object>> purgedPluginProperties = new HashSet<Map<String, Object>>();
            for (ConnectorPluginProperties cpp : responseConnectorPluginProperties) {
                purgedPluginProperties.add(cpp.getPurgedProperties());
            }
            purgedProperties.put("responseConnectorPluginProperties", purgedPluginProperties);
        }
        return purgedProperties;
    }
}