/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import com.mirth.connect.model.ChannelStatus;

public interface ChannelTableNodeFactory {

    public AbstractChannelTableNode createNode(ChannelGroupStatus groupStatus);

    public AbstractChannelTableNode createNode(ChannelStatus channelStatus);
}