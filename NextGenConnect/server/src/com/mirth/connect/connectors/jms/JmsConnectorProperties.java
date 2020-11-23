/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.jms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.util.DonkeyElement;

public class JmsConnectorProperties extends ConnectorProperties {
    private boolean useJndi;
    private String jndiProviderUrl;
    private String jndiInitialContextFactory;
    private String jndiConnectionFactoryName;
    private String connectionFactoryClass;
    private Map<String, String> connectionProperties;
    private String username;
    private String password;
    private String destinationName;
    private boolean topic;
    private String clientId;

    public JmsConnectorProperties() {
        useJndi = false;
        jndiProviderUrl = "";
        jndiInitialContextFactory = "";
        jndiConnectionFactoryName = "";
        connectionFactoryClass = "";
        connectionProperties = new LinkedHashMap<String, String>();
        username = "";
        password = "";
        destinationName = "";
        topic = false;
        clientId = "";
    }

    protected JmsConnectorProperties(JmsConnectorProperties props) {
        super(props);
        useJndi = props.isUseJndi();
        jndiProviderUrl = props.getJndiProviderUrl();
        jndiInitialContextFactory = props.getJndiInitialContextFactory();
        jndiConnectionFactoryName = props.getJndiConnectionFactoryName();
        connectionFactoryClass = props.getConnectionFactoryClass();
        connectionProperties = props.getConnectionProperties();
        username = props.getUsername();
        password = props.getPassword();
        destinationName = props.getDestinationName();
        topic = props.isTopic();
        clientId = props.getClientId();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "JMS";
    }

    @Override
    public String toFormattedString() {
        String newLine = "\n";
        StringBuilder builder = new StringBuilder();

        if (useJndi) {
            builder.append("PROVIDER URL: " + jndiProviderUrl + newLine);
            builder.append("INITIAL CONTEXT FACTORY: " + jndiInitialContextFactory + newLine);
            builder.append("CONNECTION FACTORY NAME: " + jndiConnectionFactoryName + newLine);
            builder.append("DESTINATION: " + destinationName + newLine);
        } else {
            builder.append("CONNECTION FACTORY CLASS: " + connectionFactoryClass + newLine);

            if (topic) {
                builder.append("TOPIC: " + destinationName + newLine);
            } else {
                builder.append("QUEUE: " + destinationName + newLine);
            }
        }

        if (!clientId.isEmpty()) {
            builder.append("CLIENT ID: " + clientId + newLine);
        }

        if (!username.isEmpty()) {
            builder.append("USERNAME: " + username + newLine);
        }

        if (!connectionProperties.isEmpty()) {
            builder.append(newLine + "[CONNECTION PROPERTIES]" + newLine);

            for (Entry<String, String> property : connectionProperties.entrySet()) {
                builder.append(property.getKey() + ": " + property.getValue() + newLine);
            }

            builder.append(newLine);
        }

        return builder.toString();
    }

    public boolean isUseJndi() {
        return useJndi;
    }

    public void setUseJndi(boolean useJndi) {
        this.useJndi = useJndi;
    }

    public String getJndiProviderUrl() {
        return jndiProviderUrl;
    }

    public void setJndiProviderUrl(String jndiProviderUrl) {
        this.jndiProviderUrl = jndiProviderUrl;
    }

    public String getJndiInitialContextFactory() {
        return jndiInitialContextFactory;
    }

    public void setJndiInitialContextFactory(String jndiInitialContextFactory) {
        this.jndiInitialContextFactory = jndiInitialContextFactory;
    }

    public String getJndiConnectionFactoryName() {
        return jndiConnectionFactoryName;
    }

    public void setJndiConnectionFactoryName(String jndiConnectionFactoryName) {
        this.jndiConnectionFactoryName = jndiConnectionFactoryName;
    }

    public String getConnectionFactoryClass() {
        return connectionFactoryClass;
    }

    public void setConnectionFactoryClass(String connectionFactoryClass) {
        this.connectionFactoryClass = connectionFactoryClass;
    }

    public Map<String, String> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Map<String, String> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public boolean isTopic() {
        return topic;
    }

    public void setTopic(boolean topic) {
        this.topic = topic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    // @formatter:off
    @Override public void migrate3_0_1(DonkeyElement element) {}
    @Override public void migrate3_0_2(DonkeyElement element) {} // @formatter:on

    @Override
    public void migrate3_1_0(DonkeyElement element) {
        super.migrate3_1_0(element);
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
        purgedProperties.put("useJndi", useJndi);
        purgedProperties.put("connectionPropertyChars", connectionProperties.size());
        purgedProperties.put("topic", topic);
        return purgedProperties;
    }
}
