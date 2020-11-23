/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.userutil;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.util.Serializer;
import com.mirth.connect.model.converters.ObjectXMLSerializer;

/**
 * This class represents a connector message and is used to retrieve details such as the message ID,
 * metadata ID, status, and various content types.
 */
public class ImmutableConnectorMessage {
    private static Logger logger = Logger.getLogger(ImmutableConnectorMessage.class);

    private ConnectorMessage connectorMessage;
    private boolean modifiableMaps;
    private Map<String, Integer> destinationIdMap;
    private Serializer serializer;

    /**
     * Instantiates a new ImmutableConnectorMessage object.
     * 
     * @param connectorMessage
     *            The connector message that this object will reference for retrieving data.
     */
    public ImmutableConnectorMessage(ConnectorMessage connectorMessage) {
        this(connectorMessage, false);
    }

    /**
     * Instantiates a new ImmutableConnectorMessage object.
     * 
     * @param connectorMessage
     *            The connector message that this object will reference for retrieving data.
     * @param modifiableMaps
     *            If true, variable maps (e.g. connector/channel/response) will be modifiable, and
     *            values may be set in them as well as retrieved. Otherwise, data will only be able
     *            to be retrieved from the maps, and no updates will be allowed.
     */
    public ImmutableConnectorMessage(ConnectorMessage connectorMessage, boolean modifiableMaps) {
        this(connectorMessage, modifiableMaps, null);
    }

    /**
     * Instantiates a new ImmutableConnectorMessage object.
     * 
     * @param connectorMessage
     *            The connector message that this object will reference for retrieving data.
     * @param modifiableMaps
     *            If true, variable maps (e.g. connector/channel/response) will be modifiable, and
     *            values may be set in them as well as retrieved. Otherwise, data will only be able
     *            to be retrieved from the maps, and no updates will be allowed.
     * @param destinationIdMap
     *            A map containing all applicable destination names in the channel and their
     *            corresponding connector metadata ids.
     */
    public ImmutableConnectorMessage(ConnectorMessage connectorMessage, boolean modifiableMaps, Map<String, Integer> destinationIdMap) {
        this.connectorMessage = connectorMessage;
        this.modifiableMaps = modifiableMaps;
        this.destinationIdMap = destinationIdMap;

        // NOTE: this serializer must always be the same type of serializer that is set via Donkey.setSerializer() in Mirth.startup()
        this.serializer = ObjectXMLSerializer.getInstance();
    }

    /**
     * Returns the metadata ID of this connector message. Note that the source connector has a
     * metadata ID of 0.
     * 
     * @return The metadata ID of this connector message.
     */
    public int getMetaDataId() {
        return connectorMessage.getMetaDataId();
    }

    /**
     * Returns the ID of the channel associated with this connector message.
     * 
     * @return The ID of the channel associated with this connector message.
     */
    public String getChannelId() {
        return connectorMessage.getChannelId();
    }

    /**
     * Returns the Name of the channel associated with this connector message.
     * 
     * @return The Name of the channel associated with this connector message.
     */
    public String getChannelName() {
        return connectorMessage.getChannelName();
    }

    /**
     * Returns the name of the connector associated with this connector message.
     * 
     * @return The name of the connector associated with this connector message.
     */
    public String getConnectorName() {
        return connectorMessage.getConnectorName();
    }

    /**
     * Returns the ID of the server associated with this connector message.
     * 
     * @return The ID of the server associated with this connector message.
     */
    public String getServerId() {
        return connectorMessage.getServerId();
    }

    /**
     * Returns the date/time that this connector message was created by the channel.
     * 
     * @return The date/time that this connector message was created by the channel.
     */
    public Calendar getReceivedDate() {
        return (Calendar) connectorMessage.getReceivedDate().clone();
    }

    /**
     * Returns the number of times this message has been attempted to be dispatched by the
     * connector.
     * 
     * @return The number of times this message has been attempted to be dispatched by the
     *         connector.
     */
    public int getSendAttempts() {
        return connectorMessage.getSendAttempts();
    }

    /**
     * Returns the date/time immediately before this connector message's most recent send attempt.
     * Only valid for destination connectors in the response transformer or postprocessor. Returns
     * null otherwise.
     * 
     * @return The date/time immediately before this connector message's most recent send attempt.
     */
    public Calendar getSendDate() {
        return connectorMessage.getSendDate() == null ? null : (Calendar) connectorMessage.getSendDate().clone();
    }

    /**
     * Returns the date/time immediately after this connector message's response is received. Only
     * valid for destination connectors in the response transformer or postprocessor. Returns null
     * otherwise.
     * 
     * @return The date/time immediately after this connector message's response is received.
     */
    public Calendar getResponseDate() {
        return connectorMessage.getResponseDate() == null ? null : (Calendar) connectorMessage.getResponseDate().clone();
    }

    /**
     * Returns the status (e.g. SENT) of this connector message.
     * 
     * @return The status (e.g. SENT) of this connector message.
     */
    public Status getStatus() {
        return Status.fromDonkeyStatus(connectorMessage.getStatus());
    }

    /**
     * Retrieves content associated with this connector message.
     * 
     * @param contentType
     *            The ContentType (e.g. RAW, ENCODED) of the content to retrieve.
     * @return The content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getMessageContent(ContentType contentType) {
        if (contentType == ContentType.SENT) {
            // TODO: Remove in 3.1, change the logger statement
            logger.error("The getSent() and getSentData() methods have been deprecated and will soon be removed. Please use map variables to retrieve post-replacement data instead. This method will always return null for the SENT content type.");
            return null;
        }

        if (connectorMessage.getMessageContent(contentType.toDonkeyContentType()) != null) {
            return new ImmutableMessageContent(connectorMessage.getMessageContent(contentType.toDonkeyContentType()));
        }

        return null;
    }

    /**
     * Retrieves content associated with this connector message.
     * 
     * @param contentType
     *            The ContentType (e.g. RAW, ENCODED) of the content to retrieve.
     * @return The content, as an ImmutableMessageContent object.
     * 
     * @deprecated The getContent(contentType) method has been deprecated and will soon be removed.
     *             Please use getMessageContent(contentType) instead.
     */
    @Deprecated
    public ImmutableMessageContent getContent(ContentType contentType) {
        logger.error("The getContent(contentType) method has been deprecated and will soon be removed. Please use getMessageContent(contentType) instead.");
        return getMessageContent(contentType);
    }

    /**
     * Retrieves raw content associated with this connector message.
     * 
     * @return The raw content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getRaw() {
        if (connectorMessage.getRaw() != null) {
            return new ImmutableMessageContent(connectorMessage.getRaw());
        }

        return null;
    }

    /**
     * Retrieves raw content associated with this connector message.
     * 
     * @return The raw content, as a string.
     */
    public String getRawData() {
        if (connectorMessage.getRaw() != null) {
            return connectorMessage.getRaw().getContent();
        } else {
            return null;
        }
    }

    /**
     * Retrieves processed raw content associated with this connector message.
     * 
     * @return The processed raw content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getProcessedRaw() {
        if (connectorMessage.getProcessedRaw() != null) {
            return new ImmutableMessageContent(connectorMessage.getProcessedRaw());
        }

        return null;
    }

    /**
     * Retrieves processed raw content associated with this connector message.
     * 
     * @return The processed raw content, as a string.
     */
    public String getProcessedRawData() {
        if (connectorMessage.getProcessedRaw() != null) {
            return connectorMessage.getProcessedRaw().getContent();
        } else {
            return null;
        }
    }

    /**
     * Retrieves transformed content associated with this connector message.
     * 
     * @return The transformed content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getTransformed() {
        if (connectorMessage.getTransformed() != null) {
            return new ImmutableMessageContent(connectorMessage.getTransformed());
        }

        return null;
    }

    /**
     * Retrieves transformed content associated with this connector message.
     * 
     * @return The transformed content, as a string.
     */
    public String getTransformedData() {
        if (connectorMessage.getTransformed() != null) {
            return connectorMessage.getTransformed().getContent();
        } else {
            return null;
        }
    }

    /**
     * Retrieves encoded content associated with this connector message.
     * 
     * @return The encoded content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getEncoded() {
        if (connectorMessage.getEncoded() != null) {
            return new ImmutableMessageContent(connectorMessage.getEncoded());
        }

        return null;
    }

    /**
     * Retrieves encoded content associated with this connector message.
     * 
     * @return The encoded content, as a string.
     */
    public String getEncodedData() {
        if (connectorMessage.getEncoded() != null) {
            return connectorMessage.getEncoded().getContent();
        } else {
            return null;
        }
    }

    /**
     * Retrieves response content associated with this connector message.
     * 
     * @return The response content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getResponse() {
        if (connectorMessage.getResponse() != null) {
            return new ImmutableMessageContent(connectorMessage.getResponse());
        }

        return null;
    }

    /**
     * Retrieves response content associated with this connector message.
     * 
     * @return The response content, as a string.
     */
    public Response getResponseData() {
        if (connectorMessage.getResponse() != null) {
            return new Response(serializer.deserialize(connectorMessage.getResponse().getContent(), com.mirth.connect.donkey.model.message.Response.class));
        } else {
            return null;
        }
    }

    /**
     * Retrieves transformed response content associated with this connector message.
     * 
     * @return The transformed response content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getResponseTransformed() {
        if (connectorMessage.getResponseTransformed() != null) {
            return new ImmutableMessageContent(connectorMessage.getResponseTransformed());
        }

        return null;
    }

    /**
     * Retrieves transformed response content associated with this connector message.
     * 
     * @return The transformed response content, as a string.
     */
    public String getResponseTransformedData() {
        if (connectorMessage.getResponseTransformed() != null) {
            return connectorMessage.getResponseTransformed().getContent();
        } else {
            return null;
        }
    }

    /**
     * Retrieves processed response content associated with this connector message.
     * 
     * @return The processed response content, as an ImmutableMessageContent object.
     */
    public ImmutableMessageContent getProcessedResponse() {
        if (connectorMessage.getProcessedResponse() != null) {
            return new ImmutableMessageContent(connectorMessage.getProcessedResponse());
        }

        return null;
    }

    /**
     * Retrieves processed response content associated with this connector message.
     * 
     * @return The processed response content, as a string.
     */
    public Response getProcessedResponseData() {
        if (connectorMessage.getProcessedResponse() != null) {
            return new Response(serializer.deserialize(connectorMessage.getProcessedResponse().getContent(), com.mirth.connect.donkey.model.message.Response.class));
        } else {
            return null;
        }
    }

    /**
     * Returns the sequential ID of the overall Message associated with this connector message.
     * 
     * @return The sequential ID of the overall Message associated with this connector message.
     */
    public long getMessageId() {
        return connectorMessage.getMessageId();
    }

    /**
     * Returns the source map. This map is unmodifiable and only data retrieval will be allowed.
     * 
     * @return The source map.
     */
    public Map<String, Object> getSourceMap() {
        return connectorMessage.getSourceMap();
    }

    /**
     * Returns the connector map. If this connector message was instantiated with a 'true' value for
     * modifiableMaps, then this map will allow both data retrieval and updates. Otherwise, the map
     * will be unmodifiable and only data retrieval will be allowed.
     * 
     * @return The connector map.
     */
    public Map<String, Object> getConnectorMap() {
        if (modifiableMaps) {
            return connectorMessage.getConnectorMap();
        } else {
            return Collections.unmodifiableMap(connectorMessage.getConnectorMap());
        }
    }

    /**
     * Returns the channel map. If this connector message was instantiated with a 'true' value for
     * modifiableMaps, then this map will allow both data retrieval and updates. Otherwise, the map
     * will be unmodifiable and only data retrieval will be allowed.
     * 
     * @return The channel map.
     */
    public Map<String, Object> getChannelMap() {
        if (modifiableMaps) {
            return connectorMessage.getChannelMap();
        } else {
            return Collections.unmodifiableMap(connectorMessage.getChannelMap());
        }
    }

    /**
     * Returns the response map. If this connector message was instantiated with a 'true' value for
     * modifiableMaps, then this map will allow both data retrieval and updates. Otherwise, the map
     * will be unmodifiable and only data retrieval will be allowed. In addition, if this connector
     * message was instantiated with the destinationNameMap parameter, the map will check
     * destination names as well as the proper "d#" keys when retrieving data.
     * 
     * @return The response map.
     */
    public Map<String, Object> getResponseMap() {
        if (modifiableMaps) {
            return new ResponseMap(connectorMessage.getResponseMap(), destinationIdMap);
        } else {
            return new ResponseMap(Collections.unmodifiableMap(connectorMessage.getResponseMap()), destinationIdMap);
        }
    }

    /**
     * Returns the postprocessing error string associated with this connector message, if it exists.
     * 
     * @return The postprocessing error string associated with this connector message, if it exists.
     */
    public String getPostProcessorError() {
        return connectorMessage.getPostProcessorError();
    }

    /**
     * Returns the processing error string associated with this connector message, if it exists.
     * 
     * @return The processing error string associated with this connector message, if it exists.
     */
    public String getProcessingError() {
        return connectorMessage.getProcessingError();
    }

    /**
     * Returns the response error string associated with this connector message, if it exists.
     * 
     * @return The response error string associated with this connector message, if it exists.
     */
    public String getResponseError() {
        return connectorMessage.getResponseError();
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
        Map<String, String> destinationNameMap = null;

        if (destinationIdMap != null) {
            destinationNameMap = new HashMap<String, String>();

            for (Entry<String, Integer> entry : destinationIdMap.entrySet()) {
                destinationNameMap.put(entry.getKey(), "d" + String.valueOf(entry.getValue()));
            }

            destinationNameMap = Collections.unmodifiableMap(destinationNameMap);
        }

        return destinationNameMap;
    }

    /**
     * Returns a Map of destination connector names linked to their corresponding connector metadata
     * ID.
     * 
     * @return A Map of destination connector names linked to their corresponding connector metadata
     *         ID.
     */
    public Map<String, Integer> getDestinationIdMap() {
        return destinationIdMap != null ? Collections.unmodifiableMap(destinationIdMap) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return connectorMessage.toString();
    }
}