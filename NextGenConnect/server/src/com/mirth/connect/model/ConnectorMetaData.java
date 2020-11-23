/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mirth.connect.donkey.util.purge.Purgable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("connectorMetaData")
public class ConnectorMetaData extends MetaData implements Serializable, Purgable {
    public enum Type {
        SOURCE, DESTINATION
    }

    private String serverClassName;
    private String sharedClassName;
    private String clientClassName;
    private String transformers;
    private String protocol;
    private Type type;

    public String getServerClassName() {
        return this.serverClassName;
    }

    public void setServerClassName(String serverClassName) {
        this.serverClassName = serverClassName;
    }

    public String getSharedClassName() {
        return sharedClassName;
    }

    public void setSharedClassName(String sharedClassName) {
        this.sharedClassName = sharedClassName;
    }

    public String getClientClassName() {
        return clientClassName;
    }

    public void setClientClassName(String clientClassName) {
        this.clientClassName = clientClassName;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTransformers() {
        return this.transformers;
    }

    public void setTransformers(String transformers) {
        this.transformers = transformers;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, CalendarToStringStyle.instance());
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = new HashMap<String, Object>();
        purgedProperties.put("serverClassName", serverClassName);
        purgedProperties.put("sharedClassName", sharedClassName);
        purgedProperties.put("clientClassName", clientClassName);
        purgedProperties.put("transformers", transformers);
        purgedProperties.put("protocol", protocol);
        purgedProperties.put("type", type);
        return purgedProperties;
    }
}
