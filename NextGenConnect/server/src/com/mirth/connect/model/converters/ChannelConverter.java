/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.model.converters;

import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.xmlpull.mxp1.MXParser;

import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.DonkeyElement.DonkeyElementException;
import com.mirth.connect.donkey.util.xstream.SerializerException;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.InvalidChannel;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.copy.HierarchicalStreamCopier;
import com.thoughtworks.xstream.io.xml.DocumentReader;
import com.thoughtworks.xstream.io.xml.XppReader;
import com.thoughtworks.xstream.mapper.Mapper;

public class ChannelConverter extends MigratableConverter {

    private HierarchicalStreamCopier copier = new HierarchicalStreamCopier();

    public ChannelConverter(String currentVersion, Mapper mapper) {
        super(currentVersion, mapper);
    }

    @Override
    public boolean canConvert(Class type) {
        return type != null && Channel.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (value instanceof InvalidChannel) {
            try {
                DonkeyElement element = new DonkeyElement(((InvalidChannel) value).getChannelXml());

                String version = element.getAttribute(ObjectXMLSerializer.VERSION_ATTRIBUTE_NAME);
                if (StringUtils.isNotEmpty(version)) {
                    writer.addAttribute(ObjectXMLSerializer.VERSION_ATTRIBUTE_NAME, version);
                }

                for (DonkeyElement child : element.getChildElements()) {
                    copier.copy(new XppReader(new StringReader(child.toXml()), new MXParser()), writer);
                }
            } catch (Exception e) {
                throw new SerializerException(e);
            }
        } else {
            super.marshal(value, writer, context);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        if (reader.underlyingReader() instanceof DocumentReader) {
            DonkeyElement channel = new DonkeyElement((Element) ((DocumentReader) reader.underlyingReader()).getCurrent());
            String preUnmarshalXml = null;

            try {
                try {
                    preUnmarshalXml = channel.toXml();
                } catch (DonkeyElementException e) {
                }

                return super.unmarshal(reader, context);
            } catch (LinkageError e) {
                return new InvalidChannel(preUnmarshalXml, channel, e, reader);
            } catch (Exception e) {
                return new InvalidChannel(preUnmarshalXml, channel, e, reader);
            }
        } else {
            return super.unmarshal(reader, context);
        }
    }
}