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
import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("channelStatus")
public class ChannelStatus implements Serializable {

    private Channel channel;
    private Calendar deployedDate;
    private Integer deployedRevisionDelta;
    private boolean codeTemplatesChanged;
    private Long localChannelId;

    public ChannelStatus() {}

    public ChannelStatus(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Calendar getDeployedDate() {
        return deployedDate;
    }

    public void setDeployedDate(Calendar deployedDate) {
        this.deployedDate = deployedDate;
    }

    public Integer getDeployedRevisionDelta() {
        return deployedRevisionDelta;
    }

    public void setDeployedRevisionDelta(Integer deployedRevisionDelta) {
        this.deployedRevisionDelta = deployedRevisionDelta;
    }

    public boolean isCodeTemplatesChanged() {
        return codeTemplatesChanged;
    }

    public void setCodeTemplatesChanged(boolean codeTemplatesChanged) {
        this.codeTemplatesChanged = codeTemplatesChanged;
    }

    public void setLocalChannelId(Long localChannelId) {
        this.localChannelId = localChannelId;
    }

    public Long getLocalChannelId() {
        return this.localChannelId;
    }
}
