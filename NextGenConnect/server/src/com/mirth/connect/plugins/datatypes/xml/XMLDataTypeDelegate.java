/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.datatypes.xml;

import com.mirth.connect.donkey.model.message.SerializationType;
import com.mirth.connect.model.converters.IMessageSerializer;
import com.mirth.connect.model.datatype.DataTypeDelegate;
import com.mirth.connect.model.datatype.DataTypeProperties;
import com.mirth.connect.model.datatype.SerializerProperties;

public class XMLDataTypeDelegate implements DataTypeDelegate {

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public IMessageSerializer getSerializer(SerializerProperties properties) {
        return new XMLSerializer(properties);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public SerializationType getDefaultSerializationType() {
        return SerializationType.XML;
    }

    @Override
    public DataTypeProperties getDefaultProperties() {
        return new XMLDataTypeProperties();
    }
}
