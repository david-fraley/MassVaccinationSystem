/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.userutil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.attachment.Attachment;

/**
 * This class represents an overall message and is used to retrieve details such as the message ID,
 * specific connector messages, or the merged connector message.
 */
public class ImmutableMessage {
    private static Logger logger = Logger.getLogger(ImmutableMessage.class);
    private Message message;

    /**
     * Instantiates a new ImmutableMessage object.
     * 
     * @param message
     *            The Message object that this object will reference for retrieving data.
     */
    public ImmutableMessage(Message message) {
        this.message = message;
    }

    /**
     * Returns the sequential ID of this message, as a Long.
     * 
     * @return The sequential ID of this message, as a Long.
     */
    public Long getMessageId() {
        return message.getMessageId();
    }

    /**
     * Returns the ID of the server associated with this message.
     * 
     * @return The ID of the server associated with this message.
     */
    public String getServerId() {
        return message.getServerId();
    }

    /**
     * Returns the ID of the channel associated with this message.
     * 
     * @return The ID of the channel associated with this message.
     */
    public String getChannelId() {
        return message.getChannelId();
    }

    // TODO: Remove in 3.1
    /**
     * Returns the original date/time that this message was created by the channel. If the message
     * is reprocessed at a later point, this date will remain the same and instead the connector
     * message received dates will be updated.
     * 
     * @return The original date/time that this message was created by the channel.
     * 
     * @deprecated This method is deprecated and will soon be removed. This method currently returns
     *             the received date of the source connector message.
     */
    @Deprecated
    public Calendar getReceivedDate() {
        logger.error("This method is deprecated and will soon be removed. This method currently returns the received date of the source connector message.");
        return (Calendar) message.getReceivedDate().clone();
    }

    /**
     * Returns whether this message has finished processing through a channel. A message is
     * considered "processed" if it correctly flows through each applicable connector and the
     * postprocessor script finishes. Even if a non-fatal error occurs on a particular connector
     * message and the status ends up as ERROR, or if a message is queued by a destination and has
     * not yet been sent to the outbound system, it can still be considered processed.
     * 
     * @return A boolean indicating whether this message has finished processing through a channel.
     */
    public boolean isProcessed() {
        return message.isProcessed();
    }

    /**
     * Returns the ID of the original message this one was reprocessed from.
     * 
     * @return The ID of the original message this one was reprocessed from.
     */
    public Long getOriginalId() {
        return message.getOriginalId();
    }

    /**
     * Returns the ID of the original message this one was imported from.
     * 
     * @return The ID of the original message this one was imported from.
     */
    public Long getImportId() {
        return message.getImportId();
    }

    /**
     * Returns the ID of the original channel this message was reprocessed from.
     * 
     * @return The ID of the original channel this message was reprocessed from.
     */
    public String getImportChannelId() {
        return message.getImportChannelId();
    }

    /**
     * Returns a list of attachments associated with this message. This will only be populated in
     * certain cases, such as when a message is being exported or archived.
     * 
     * @return A list of attachments associated with this message.
     */
    public List<ImmutableAttachment> getAttachments() {
        List<ImmutableAttachment> attachments = new ArrayList<ImmutableAttachment>();
        if (CollectionUtils.isNotEmpty(message.getAttachments())) {
            for (Attachment attachment : message.getAttachments()) {
                attachments.add(new ImmutableAttachment(attachment));
            }
        }
        return attachments;
    }

    /**
     * Returns a map of connector messages associated with this message. The keys are the metadata
     * IDs (as Integer objects), and the values are the connector messages themselves.
     * 
     * @return A map of connector messages associated with this message.
     */
    public Map<Integer, ImmutableConnectorMessage> getConnectorMessages() {
        // MIRTH-2523: Overriding the get method to allow doubles to be passed in
        Map<Integer, ImmutableConnectorMessage> map = new LinkedHashMap<Integer, ImmutableConnectorMessage>() {
            @Override
            public ImmutableConnectorMessage get(Object key) {
                if (key instanceof Double) {
                    key = ((Double) key).intValue();
                }
                return super.get(key);
            }
        };

        for (Integer key : message.getConnectorMessages().keySet()) {
            map.put(key, new ImmutableConnectorMessage(message.getConnectorMessages().get(key), false, getDestinationIdMap()));
        }
        return map;
    }

    /**
     * Returns a "merged" connector message containing data from all connector messages combined.
     * The raw and processed raw content is copied from the source connector, while values in the
     * channel and response maps are copied from all connectors.
     * 
     * @return A "merged" connector message containing data from all connector messages combined.
     */
    public ImmutableConnectorMessage getMergedConnectorMessage() {
        return new ImmutableConnectorMessage(message.getMergedConnectorMessage(), false, getDestinationIdMap());
    }

    /**
     * Returns a Map of destination connector names linked to their corresponding "d#" response map
     * keys (where "#" is the destination connector metadata ID).
     * 
     * @return A Map of destination connector names linked to their corresponding "d#" response map
     *         keys.
     * 
     * @deprecated This method is deprecated and will soon be removed. Please use
     *             {@link #getDestinationIdMap() getDestinationIdMap()} instead.
     */
    @Deprecated
    public Map<String, String> getDestinationNameMap() {
        logger.error("This method is deprecated and will soon be removed. Please use getDestinationIdMap() instead.");
        Map<String, String> destinationNameMap = new HashMap<String, String>();

        for (ConnectorMessage destinationMessage : message.getConnectorMessages().values()) {
            destinationNameMap.put(destinationMessage.getConnectorName(), "d" + String.valueOf(destinationMessage.getMetaDataId()));
        }

        return Collections.unmodifiableMap(destinationNameMap);
    }

    /**
     * Returns a Map of destination connector names linked to their corresponding connector metadata
     * ID.
     * 
     * @return A Map of destination connector names linked to their corresponding connector metadata
     *         ID.
     */
    public Map<String, Integer> getDestinationIdMap() {
        Map<String, Integer> destinationIdMap = new LinkedHashMap<String, Integer>();

        for (ConnectorMessage destinationMessage : message.getConnectorMessages().values()) {
            destinationIdMap.put(destinationMessage.getConnectorName(), destinationMessage.getMetaDataId());
        }

        return Collections.unmodifiableMap(destinationIdMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return message.toString();
    }
}