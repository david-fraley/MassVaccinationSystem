/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.ws;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class DefinitionServiceMap implements Serializable {

    private Map<String, DefinitionPortMap> map = new LinkedHashMap<String, DefinitionPortMap>();

    public Map<String, DefinitionPortMap> getMap() {
        return map;
    }

    public static class DefinitionPortMap implements Serializable {

        private Map<String, PortInformation> map = new LinkedHashMap<String, PortInformation>();

        public Map<String, PortInformation> getMap() {
            return map;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }
    }

    public static class PortInformation implements Serializable {
        private List<String> operations;
        private List<String> actions;
        private String locationURI;

        public PortInformation(List<String> operations) {
            this(operations, null);
        }

        public PortInformation(List<String> operations, List<String> actions) {
            this(operations, actions, null);
        }

        public PortInformation(List<String> operations, List<String> actions, String locationURI) {
            this.operations = operations;
            this.actions = actions;
            this.locationURI = locationURI;
        }

        public List<String> getOperations() {
            return operations;
        }

        public List<String> getActions() {
            return actions;
        }

        public String getLocationURI() {
            return locationURI;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
