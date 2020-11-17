/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.server.channel.components;

import com.mirth.connect.donkey.model.DonkeyException;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.Response;

public interface PostProcessor {
    public Response doPostProcess(Message message) throws DonkeyException, InterruptedException;
}
