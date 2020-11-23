/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.test.util;

import com.mirth.connect.donkey.model.DonkeyException;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.server.channel.components.ResponseTransformer;

public class TestResponseTransformer implements ResponseTransformer {
    private boolean transformed = false;

    public boolean isTransformed() {
        return transformed;
    }

    @Override
    public void dispose() {}

    @Override
    public String doTransform(Response response, ConnectorMessage connectorMessage) throws DonkeyException, InterruptedException {
        transformed = true;
        return response.getMessage();
    }
}
