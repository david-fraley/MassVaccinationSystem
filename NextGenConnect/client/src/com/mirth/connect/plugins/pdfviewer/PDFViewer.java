/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.plugins.pdfviewer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.donkey.model.message.attachment.Attachment;
import com.mirth.connect.plugins.AttachmentViewer;

public class PDFViewer extends AttachmentViewer {

    public PDFViewer(String name) {
        super(name);
    }

    public boolean handleMultiple() {
        return false;
    }

    public void viewAttachments(String channelId, Long messageId, String attachmentId) {

        try {
            Attachment attachment = parent.mirthClient.getAttachment(channelId, messageId, attachmentId);
            byte[] rawData = attachment.getContent();
            Base64InputStream in = new Base64InputStream(new ByteArrayInputStream(rawData));

            File temp = File.createTempFile(attachment.getId(), ".pdf");
            temp.deleteOnExit();

            OutputStream out = new FileOutputStream(temp);
            IOUtils.copy(in, out);
            out.close();

            new MirthPDFViewer(true, temp);

        } catch (Exception e) {
            parent.alertThrowable(parent, e);
        }
    }

    @Override
    public boolean isContentTypeViewable(String contentType) {
        return StringUtils.containsIgnoreCase(contentType, "pdf");
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void reset() {}

    @Override
    public String getPluginPointName() {
        return "PDF Viewer";
    }
}
