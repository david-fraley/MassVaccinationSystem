/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins;

import java.io.InputStream;

import com.mirth.connect.donkey.model.message.SerializationType;
import com.mirth.connect.donkey.server.channel.SourceConnector;
import com.mirth.connect.donkey.server.message.AutoResponder;
import com.mirth.connect.donkey.server.message.ResponseValidator;
import com.mirth.connect.donkey.server.message.batch.BatchAdaptorFactory;
import com.mirth.connect.donkey.server.message.batch.BatchStreamReader;
import com.mirth.connect.model.converters.IMessageSerializer;
import com.mirth.connect.model.datatype.DataTypeDelegate;
import com.mirth.connect.model.datatype.DataTypeProperties;
import com.mirth.connect.model.datatype.ResponseGenerationProperties;
import com.mirth.connect.model.datatype.ResponseValidationProperties;
import com.mirth.connect.model.datatype.SerializationProperties;
import com.mirth.connect.model.datatype.SerializerProperties;
import com.mirth.connect.model.transmission.TransmissionModeProperties;
import com.mirth.connect.server.message.DefaultAutoResponder;
import com.mirth.connect.server.message.DefaultResponseValidator;

public abstract class DataTypeServerPlugin implements ServerPlugin {

    /**
     * Get an instance of the data type's serializer with the given properties
     */
    final public IMessageSerializer getSerializer(SerializerProperties properties) {
        return getDataTypeDelegate().getSerializer(properties);
    }

    /**
     * Indicates if the data type is in binary format
     */
    final public boolean isBinary() {
        return getDataTypeDelegate().isBinary();
    }

    /**
     * Get the serialization type
     */
    final public SerializationType getDefaultSerializationType() {
        return getDataTypeDelegate().getDefaultSerializationType();
    }

    /**
     * Get the default properties of the data type. Must not return null.
     */
    final public DataTypeProperties getDefaultProperties() {
        return getDataTypeDelegate().getDefaultProperties();
    }

    /**
     * Get the data type delegate that is used for client/server shared methods
     */
    protected abstract DataTypeDelegate getDataTypeDelegate();

    /**
     * Get the batch adaptor for the data type
     */
    public BatchAdaptorFactory getBatchAdaptorFactory(SourceConnector sourceConnector, SerializerProperties properties) {
        return null;
    }

    /**
     * Get the batch stream reader for the data type
     */
    public BatchStreamReader getBatchStreamReader(InputStream inputStream, TransmissionModeProperties properties) {
        return null;
    }

    /**
     * Get the auto responder for the data type
     */
    public AutoResponder getAutoResponder(SerializationProperties serializationProperties, ResponseGenerationProperties generationProperties) {
        return new DefaultAutoResponder();
    }

    /**
     * Get the response validator for the data type
     */
    public ResponseValidator getResponseValidator(SerializationProperties serializationProperties, ResponseValidationProperties responseValidationProperties) {
        return new DefaultResponseValidator();
    }
}
