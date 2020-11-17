/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.mllpmode;

import java.io.InputStream;
import java.io.OutputStream;

import com.mirth.connect.donkey.server.message.StreamHandler;
import com.mirth.connect.donkey.server.message.batch.BatchStreamReader;
import com.mirth.connect.model.transmission.TransmissionModeProperties;
import com.mirth.connect.model.transmission.framemode.FrameStreamHandler;
import com.mirth.connect.plugins.TransmissionModeProvider;

public class MLLPModeProvider extends TransmissionModeProvider {

    @Override
    public String getPluginPointName() {
        return MLLPModeProperties.PLUGIN_POINT;
    }

    @Override
    public StreamHandler getStreamHandler(InputStream inputStream, OutputStream outputStream, BatchStreamReader batchStreamReader, TransmissionModeProperties transmissionModeProperties) {
        if (transmissionModeProperties instanceof MLLPModeProperties && ((MLLPModeProperties) transmissionModeProperties).isUseMLLPv2()) {
            return new MLLPv2StreamHandler(inputStream, outputStream, batchStreamReader, transmissionModeProperties);
        } else {
            return new FrameStreamHandler(inputStream, outputStream, batchStreamReader, transmissionModeProperties);
        }
    }
}
