/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.model;

import java.util.HashMap;
import java.util.Map;

import com.mirth.connect.donkey.model.channel.ConnectorPluginProperties;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.xstream.SerializerException;

public class InvalidConnectorPluginProperties extends ConnectorPluginProperties {

    private String propertiesXml;
    private Throwable cause;
    private String name;

    public InvalidConnectorPluginProperties(String preUnmarshalXml, DonkeyElement properties, Throwable cause) {
        if (preUnmarshalXml == null || properties == null) {
            throw new SerializerException("Could not create invalid connector plugin properties. The properties element or XML is null.");
        }

        this.propertiesXml = preUnmarshalXml;
        this.cause = cause;
        this.name = properties.getNodeName();
    }

    public InvalidConnectorPluginProperties(InvalidConnectorPluginProperties props) {
        this.propertiesXml = props.getPropertiesXml();
        this.cause = props.getCause();
        this.name = props.getName();
    }

    public String getPropertiesXml() {
        return propertiesXml;
    }

    public void setPropertiesXml(String propertiesXml) {
        this.propertiesXml = propertiesXml;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof InvalidConnectorPluginProperties) {
            return propertiesXml.equals(((InvalidConnectorPluginProperties) obj).getPropertiesXml());
        }
        return false;
    }

    @Override
    public InvalidConnectorPluginProperties clone() {
        return new InvalidConnectorPluginProperties(this);
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = new HashMap<String, Object>();
        return purgedProperties;
    }
}
