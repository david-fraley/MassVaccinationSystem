/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.test.util;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.util.DonkeyElement;

public class TestDispatcherProperties extends ConnectorProperties implements DestinationConnectorPropertiesInterface {
    private DestinationConnectorProperties destinationConnectorProperties;
    private String template;

    public TestDispatcherProperties() {
        this.destinationConnectorProperties = new DestinationConnectorProperties();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public DestinationConnectorProperties getDestinationConnectorProperties() {
        return destinationConnectorProperties;
    }

    @Override
    public ConnectorProperties clone() {
        return this;
    }

    @Override
    public boolean canValidateResponse() {
        return false;
    }

    @Override
    public String getProtocol() {
        return "Test Protocol";
    }

    @Override
    public String getName() {
        return "Test Dispatcher";
    }

    @Override
    public String toFormattedString() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public void migrate3_0_1(DonkeyElement element) {}

    @Override
    public void migrate3_0_2(DonkeyElement element) {}

    @Override
    public void migrate3_1_0(DonkeyElement element) {}

    @Override
    public void migrate3_2_0(DonkeyElement element) {}

    @Override
    public void migrate3_3_0(DonkeyElement element) {}

    @Override
    public void migrate3_4_0(DonkeyElement element) {}

    @Override
    public void migrate3_5_0(DonkeyElement element) {}

    @Override
    public void migrate3_6_0(DonkeyElement element) {}

    @Override
    public void migrate3_7_0(DonkeyElement element) {}
    
    @Override
    public void migrate3_9_0(DonkeyElement element) {}

    @Override
    public Map<String, Object> getPurgedProperties() {
        return null;
    }
}
