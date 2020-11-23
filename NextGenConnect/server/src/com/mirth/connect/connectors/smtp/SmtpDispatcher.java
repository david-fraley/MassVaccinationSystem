/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.smtp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.event.ConnectionStatusEventType;
import com.mirth.connect.donkey.model.event.ErrorEventType;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.model.message.attachment.AttachmentHandlerProvider;
import com.mirth.connect.donkey.server.ConnectorTaskException;
import com.mirth.connect.donkey.server.channel.DestinationConnector;
import com.mirth.connect.donkey.server.event.ConnectionStatusEvent;
import com.mirth.connect.donkey.server.event.ErrorEvent;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.userutil.AttachmentEntry;
import com.mirth.connect.util.CharsetUtils;
import com.mirth.connect.util.ErrorMessageBuilder;

public class SmtpDispatcher extends DestinationConnector {
    private Logger logger = Logger.getLogger(this.getClass());
    private EventController eventController = ControllerFactory.getFactory().createEventController();
    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();
    private final TemplateValueReplacer replacer = new TemplateValueReplacer();

    private SmtpConfiguration configuration = null;
    private String charsetEncoding;

    @Override
    public void onDeploy() throws ConnectorTaskException {
        // load the default configuration
        String configurationClass = configurationController.getProperty(getConnectorProperties().getProtocol(), "smtpConfigurationClass");

        try {
            configuration = (SmtpConfiguration) Class.forName(configurationClass).newInstance();
        } catch (Throwable t) {
            logger.trace("could not find custom configuration class, using default");
            configuration = new DefaultSmtpConfiguration();
        }

        try {
            configuration.configureConnectorDeploy(this);
        } catch (Exception e) {
            throw new ConnectorTaskException(e);
        }

        // TODO remove hardcoded HL7v2 reference?
        this.charsetEncoding = CharsetUtils.getEncoding(getConnectorProperties().getCharsetEncoding(), System.getProperty("ca.uhn.hl7v2.llp.charset"));
    }

    @Override
    public void onUndeploy() throws ConnectorTaskException {}

    @Override
    public void onStart() throws ConnectorTaskException {}

    @Override
    public void onStop() throws ConnectorTaskException {}

    @Override
    public void onHalt() throws ConnectorTaskException {}

    @Override
    public void replaceConnectorProperties(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        SmtpDispatcherProperties smtpDispatcherProperties = (SmtpDispatcherProperties) connectorProperties;

        // Replace all values in connector properties
        smtpDispatcherProperties.setSmtpHost(replacer.replaceValues(smtpDispatcherProperties.getSmtpHost(), connectorMessage));
        smtpDispatcherProperties.setSmtpPort(replacer.replaceValues(smtpDispatcherProperties.getSmtpPort(), connectorMessage));
        smtpDispatcherProperties.setLocalAddress(replacer.replaceValues(smtpDispatcherProperties.getLocalAddress(), connectorMessage));
        smtpDispatcherProperties.setLocalPort(replacer.replaceValues(smtpDispatcherProperties.getLocalPort(), connectorMessage));
        smtpDispatcherProperties.setTimeout(replacer.replaceValues(smtpDispatcherProperties.getTimeout(), connectorMessage));

        if (smtpDispatcherProperties.isAuthentication()) {
            smtpDispatcherProperties.setUsername(replacer.replaceValues(smtpDispatcherProperties.getUsername(), connectorMessage));
            smtpDispatcherProperties.setPassword(replacer.replaceValues(smtpDispatcherProperties.getPassword(), connectorMessage));
        }

        smtpDispatcherProperties.setTo(replacer.replaceValues(smtpDispatcherProperties.getTo(), connectorMessage));
        smtpDispatcherProperties.setCc(replacer.replaceValues(smtpDispatcherProperties.getCc(), connectorMessage));
        smtpDispatcherProperties.setBcc(replacer.replaceValues(smtpDispatcherProperties.getBcc(), connectorMessage));
        smtpDispatcherProperties.setReplyTo(replacer.replaceValues(smtpDispatcherProperties.getReplyTo(), connectorMessage));

        smtpDispatcherProperties.setHeadersMap(replacer.replaceValuesInMap(smtpDispatcherProperties.getHeadersMap(), connectorMessage));
        smtpDispatcherProperties.setHeadersVariable(replacer.replaceValues(smtpDispatcherProperties.getHeadersVariable(), connectorMessage));

        smtpDispatcherProperties.setFrom(replacer.replaceValues(smtpDispatcherProperties.getFrom(), connectorMessage));
        smtpDispatcherProperties.setSubject(replacer.replaceValues(smtpDispatcherProperties.getSubject(), connectorMessage));

        smtpDispatcherProperties.setBody(replacer.replaceValues(smtpDispatcherProperties.getBody(), connectorMessage));

        for (Attachment attachment : smtpDispatcherProperties.getAttachmentsList()) {
            attachment.setName(replacer.replaceValues(attachment.getName(), connectorMessage));
            attachment.setMimeType(replacer.replaceValues(attachment.getMimeType(), connectorMessage));
            attachment.setContent(replacer.replaceValues(attachment.getContent(), connectorMessage));
        }
        smtpDispatcherProperties.setAttachmentsVariable(replacer.replaceValues(smtpDispatcherProperties.getAttachmentsVariable(), connectorMessage));
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        SmtpDispatcherProperties smtpDispatcherProperties = (SmtpDispatcherProperties) connectorProperties;
        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;

        String info = "From: " + smtpDispatcherProperties.getFrom() + " To: " + smtpDispatcherProperties.getTo() + " SMTP Info: " + smtpDispatcherProperties.getSmtpHost() + ":" + smtpDispatcherProperties.getSmtpPort();
        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.WRITING, info));

        try {
            Email email = null;

            if (smtpDispatcherProperties.isHtml()) {
                email = new HtmlEmail();
            } else {
                email = new MultiPartEmail();
            }

            email.setCharset(charsetEncoding);

            email.setHostName(smtpDispatcherProperties.getSmtpHost());

            try {
                email.setSmtpPort(Integer.parseInt(smtpDispatcherProperties.getSmtpPort()));
            } catch (NumberFormatException e) {
                // Don't set if the value is invalid
            }

            try {
                int timeout = Integer.parseInt(smtpDispatcherProperties.getTimeout());
                email.setSocketTimeout(timeout);
                email.setSocketConnectionTimeout(timeout);
            } catch (NumberFormatException e) {
                // Don't set if the value is invalid
            }

            // This has to be set before the authenticator because a session shouldn't be created yet
            configuration.configureEncryption(connectorProperties, email);

            if (smtpDispatcherProperties.isAuthentication()) {
                email.setAuthentication(smtpDispatcherProperties.getUsername(), smtpDispatcherProperties.getPassword());
            }

            Properties mailProperties = email.getMailSession().getProperties();
            // These have to be set after the authenticator, so that a new mail session isn't created
            configuration.configureMailProperties(mailProperties);

            if (smtpDispatcherProperties.isOverrideLocalBinding()) {
                mailProperties.setProperty("mail.smtp.localaddress", smtpDispatcherProperties.getLocalAddress());
                mailProperties.setProperty("mail.smtp.localport", smtpDispatcherProperties.getLocalPort());
            }
            /*
             * NOTE: There seems to be a bug when calling setTo with a List (throws a
             * java.lang.ArrayStoreException), so we are using addTo instead.
             */

            for (String to : StringUtils.split(smtpDispatcherProperties.getTo(), ",")) {
                email.addTo(to);
            }

            // Currently unused
            for (String cc : StringUtils.split(smtpDispatcherProperties.getCc(), ",")) {
                email.addCc(cc);
            }

            // Currently unused
            for (String bcc : StringUtils.split(smtpDispatcherProperties.getBcc(), ",")) {
                email.addBcc(bcc);
            }

            // Currently unused
            for (String replyTo : StringUtils.split(smtpDispatcherProperties.getReplyTo(), ",")) {
                email.addReplyTo(replyTo);
            }

            Map<String, String> headers = getHeaders(smtpDispatcherProperties, connectorMessage);
            for (Entry<String, String> header : headers.entrySet()) {
                email.addHeader(header.getKey(), header.getValue());
            }

            email.setFrom(smtpDispatcherProperties.getFrom());
            email.setSubject(smtpDispatcherProperties.getSubject());

            AttachmentHandlerProvider attachmentHandlerProvider = getAttachmentHandlerProvider();

            String body = attachmentHandlerProvider.reAttachMessage(smtpDispatcherProperties.getBody(), connectorMessage, smtpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());

            if (StringUtils.isNotEmpty(body)) {
                if (smtpDispatcherProperties.isHtml()) {
                    ((HtmlEmail) email).setHtmlMsg(body);
                } else {
                    email.setMsg(body);
                }
            }
            List<Attachment> attachmentSource = getAttachments(smtpDispatcherProperties, connectorMessage);
            /*
             * If the MIME type for the attachment is missing, we display a warning and set the
             * content anyway. If the MIME type is of type "text" or "application/xml", then we add
             * the content. If it is anything else, we assume it should be Base64 decoded first.
             */
            for (Attachment attachment : attachmentSource) {
                String name = attachment.getName();
                String mimeType = attachment.getMimeType();
                String content = attachment.getContent();

                byte[] bytes;

                if (StringUtils.indexOf(mimeType, "/") < 0) {
                    logger.warn("valid MIME type is missing for email attachment: \"" + name + "\", using default of text/plain");
                    attachment.setMimeType("text/plain");
                    bytes = attachmentHandlerProvider.reAttachMessage(content, connectorMessage, charsetEncoding, false, smtpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());
                } else if ("application/xml".equalsIgnoreCase(mimeType) || StringUtils.startsWith(mimeType, "text/")) {
                    logger.debug("text or XML MIME type detected for attachment \"" + name + "\"");
                    bytes = attachmentHandlerProvider.reAttachMessage(content, connectorMessage, charsetEncoding, false, smtpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());
                } else {
                    logger.debug("binary MIME type detected for attachment \"" + name + "\", performing Base64 decoding");
                    bytes = attachmentHandlerProvider.reAttachMessage(content, connectorMessage, null, true, smtpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());
                }

                ((MultiPartEmail) email).attach(new ByteArrayDataSource(bytes, mimeType), name, null);
            }

            /*
             * From the Commons Email JavaDoc: send returns
             * "the message id of the underlying MimeMessage".
             */
            responseData = email.send();
            responseStatus = Status.SENT;
            responseStatusMessage = "Email sent successfully.";
        } catch (Exception e) {
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error sending email message", e));
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error sending email message", e);
            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error sending email message", e);

            // TODO: Exception handling
//            connector.handleException(new Exception(e));
        } finally {
            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.IDLE));
        }

        return new Response(responseStatus, responseData, responseStatusMessage, responseError);
    }

    Map<String, String> getHeaders(SmtpDispatcherProperties smtpDispatcherProperties, ConnectorMessage connectorMessage) {
        Map<String, String> headers;

        if (smtpDispatcherProperties.isUseHeadersVariable()) {
            headers = new HashMap<String, String>();

            try {
                Map<?, ?> source = (Map<?, ?>) getMessageMaps().get(smtpDispatcherProperties.getHeadersVariable(), connectorMessage);

                if (source != null) {
                    for (Entry<?, ?> entry : source.entrySet()) {
                        headers.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                    }
                } else {
                    logger.trace("Headers map variable '" + smtpDispatcherProperties.getHeadersVariable() + "' not found.");
                }
            } catch (Exception e) {
                logger.warn("Error getting headers from map " + smtpDispatcherProperties.getHeadersVariable(), e);
            }
        } else {
            headers = smtpDispatcherProperties.getHeadersMap();
        }

        return headers;
    }

    List<Attachment> getAttachments(SmtpDispatcherProperties smtpDispatcherProperties, ConnectorMessage connectorMessage) {
        List<Attachment> attachmentSource;

        if (smtpDispatcherProperties.isUseAttachmentsVariable()) {
            attachmentSource = new ArrayList<Attachment>();

            try {
                List<?> source = (List<?>) getMessageMaps().get(smtpDispatcherProperties.getAttachmentsVariable(), connectorMessage);
                if (source != null) {
                    for (Object entry : source) {
                        if (entry instanceof AttachmentEntry) {
                            Attachment att = new Attachment();
                            att.setName(((AttachmentEntry) entry).getName());
                            att.setContent(((AttachmentEntry) entry).getContent());
                            att.setMimeType(((AttachmentEntry) entry).getMimeType());
                            attachmentSource.add(att);
                        } else {
                            logger.trace("Error getting AttachmentEntry from map '" + smtpDispatcherProperties.getAttachmentsVariable() + "'. Skipping entry.");
                        }
                    }
                } else {
                    logger.warn("Attachments list variable '" + smtpDispatcherProperties.getAttachmentsVariable() + "' not found.");
                }
            } catch (Exception e) {
                logger.warn("Error getting attachments from map " + smtpDispatcherProperties.getAttachmentsVariable(), e);
            }
        } else {
            attachmentSource = smtpDispatcherProperties.getAttachmentsList();
        }

        return attachmentSource;
    }

    @Override
    public SmtpDispatcherProperties getConnectorProperties() {
        return (SmtpDispatcherProperties) super.getConnectorProperties();
    }
}
