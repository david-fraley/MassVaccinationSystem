/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors;

import java.util.ArrayList;
import java.util.List;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.server.ConnectorTaskException;
import com.mirth.connect.donkey.server.channel.DestinationConnector;

public class TestDestinationConnector extends DestinationConnector {
    final public static String TEST_RESPONSE_PREFIX = "response";

    private List<Long> messageIds = new ArrayList<Long>();

    @Override
    public void onDeploy() throws ConnectorTaskException {}

    @Override
    public void onUndeploy() throws ConnectorTaskException {}

    @Override
    public void onStart() throws ConnectorTaskException {}

    @Override
    public void onStop() throws ConnectorTaskException {}

    @Override
    public void onHalt() throws ConnectorTaskException {}

    @Override
    public void replaceConnectorProperties(ConnectorProperties connectorProperties, ConnectorMessage message) {}

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage message) {
        messageIds.add(message.getMessageId());
        return new Response(Status.SENT, TEST_RESPONSE_PREFIX + message.getMessageId());
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }
}
