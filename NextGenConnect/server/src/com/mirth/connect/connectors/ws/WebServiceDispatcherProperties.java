/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.ws;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.connectors.ws.DefinitionServiceMap.DefinitionPortMap;
import com.mirth.connect.connectors.ws.DefinitionServiceMap.PortInformation;
import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.DonkeyElement.DonkeyElementException;
import com.mirth.connect.donkey.util.purge.PurgeUtil;
import com.mirth.connect.donkey.util.xstream.SerializerException;
import com.mirth.connect.model.converters.ObjectXMLSerializer;

public class WebServiceDispatcherProperties extends ConnectorProperties implements DestinationConnectorPropertiesInterface {

    private DestinationConnectorProperties destinationConnectorProperties;

    private String wsdlUrl;
    private String service;
    private String port;
    private String operation;
    private String locationURI;
    private String socketTimeout;
    private boolean useAuthentication;
    private String username;
    private String password;
    private String envelope;
    private boolean oneWay;
    private Map<String, List<String>> headers;
    private String headersVariable;
    private boolean isUseHeadersVariable;
    private boolean useMtom;
    private List<String> attachmentNames;
    private List<String> attachmentContents;
    private List<String> attachmentTypes;
    private String attachmentsVariable;
    private boolean isUseAttachmentsVariable; // true to use attachments, false to use attachmentsVariable
    private String soapAction;
    private DefinitionServiceMap wsdlDefinitionMap;

    public static final String WEBSERVICE_DEFAULT_DROPDOWN = "Press Get Operations";

    public WebServiceDispatcherProperties() {
        destinationConnectorProperties = new DestinationConnectorProperties(false);

        this.wsdlUrl = "";
        this.operation = WEBSERVICE_DEFAULT_DROPDOWN;
        this.wsdlDefinitionMap = new DefinitionServiceMap();
        this.service = "";
        this.port = "";
        this.locationURI = "";
        this.socketTimeout = "30000";
        this.useAuthentication = false;
        this.username = "";
        this.password = "";
        this.envelope = "";
        this.oneWay = false;
        this.headers = new LinkedHashMap<String, List<String>>();
        this.isUseHeadersVariable = false;
        this.headersVariable = "";
        this.useMtom = false;
        this.attachmentNames = new ArrayList<String>();
        this.attachmentContents = new ArrayList<String>();
        this.attachmentTypes = new ArrayList<String>();
        this.isUseAttachmentsVariable = false;
        this.attachmentsVariable = "";
        this.soapAction = "";
    }

    public WebServiceDispatcherProperties(WebServiceDispatcherProperties props) {
        super(props);
        destinationConnectorProperties = new DestinationConnectorProperties(props.getDestinationConnectorProperties());

        wsdlUrl = props.getWsdlUrl();
        operation = props.getOperation();
        wsdlDefinitionMap = props.getWsdlDefinitionMap();
        service = props.getService();
        port = props.getPort();
        locationURI = props.getLocationURI();
        socketTimeout = props.getSocketTimeout();
        useAuthentication = props.isUseAuthentication();
        username = props.getUsername();
        password = props.getPassword();
        envelope = props.getEnvelope();
        oneWay = props.isOneWay();

        Map<String, List<String>> headerCopy = new LinkedHashMap<String, List<String>>();
        for (Entry<String, List<String>> entry : props.getHeadersMap().entrySet()) {
            headerCopy.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
        }
        headers = headerCopy;
        isUseHeadersVariable = props.isUseHeadersVariable();
        headersVariable = props.getHeadersVariable();

        useMtom = props.isUseMtom();
        attachmentNames = new ArrayList<String>(props.getAttachmentNames());
        attachmentContents = new ArrayList<String>(props.getAttachmentContents());
        attachmentTypes = new ArrayList<String>(props.getAttachmentTypes());
        isUseAttachmentsVariable = props.isUseAttachmentsVariable();
        attachmentsVariable = props.getAttachmentsVariable();
        soapAction = props.getSoapAction();
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLocationURI() {
        return locationURI;
    }

    public void setLocationURI(String locationURI) {
        this.locationURI = locationURI;
    }

    public String getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(String socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isUseAuthentication() {
        return useAuthentication;
    }

    public void setUseAuthentication(boolean useAuthentication) {
        this.useAuthentication = useAuthentication;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnvelope() {
        return envelope;
    }

    public void setEnvelope(String envelope) {
        this.envelope = envelope;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public String getHeadersVariable() {
        return headersVariable;
    }

    public void setHeadersVariable(String headersVariable) {
        this.headersVariable = headersVariable;
    }

    public boolean isUseHeadersVariable() {
        return isUseHeadersVariable;
    }

    public void setUseHeadersVariable(boolean isUseHeadersVariable) {
        this.isUseHeadersVariable = isUseHeadersVariable;
    }

    public Map<String, List<String>> getHeadersMap() {
        return headers;
    }

    public void setHeadersMap(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public boolean isUseMtom() {
        return useMtom;
    }

    public void setUseMtom(boolean useMtom) {
        this.useMtom = useMtom;
    }

    public String getAttachmentsVariable() {
        return attachmentsVariable;
    }

    public void setAttachmentsVariable(String attachmentsVariable) {
        this.attachmentsVariable = attachmentsVariable;
    }

    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    public void setAttachmentNames(List<String> attachmentNames) {
        this.attachmentNames = attachmentNames;
    }

    public List<String> getAttachmentContents() {
        return attachmentContents;
    }

    public void setAttachmentContents(List<String> attachmentContents) {
        this.attachmentContents = attachmentContents;
    }

    public List<String> getAttachmentTypes() {
        return attachmentTypes;
    }

    public void setAttachmentTypes(List<String> attachmentTypes) {
        this.attachmentTypes = attachmentTypes;
    }

    public boolean isUseAttachmentsVariable() {
        return isUseAttachmentsVariable;
    }

    public void setUseAttachmentsVariable(boolean isUseAttachmentsVariable) {
        this.isUseAttachmentsVariable = isUseAttachmentsVariable;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public DefinitionServiceMap getWsdlDefinitionMap() {
        return wsdlDefinitionMap;
    }

    public void setWsdlDefinitionMap(DefinitionServiceMap wsdlDefinitionMap) {
        this.wsdlDefinitionMap = wsdlDefinitionMap;
    }

    @Override
    public String getProtocol() {
        return "WS";
    }

    @Override
    public String getName() {
        return "Web Service Sender";
    }

    @Override
    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "\n";

        builder.append("WSDL URL: ");
        builder.append(wsdlUrl);
        builder.append(newLine);

        if (StringUtils.isNotBlank(username)) {
            builder.append("USERNAME: ");
            builder.append(username);
            builder.append(newLine);
        }

        if (StringUtils.isNotBlank(service)) {
            builder.append("SERVICE: ");
            builder.append(service);
            builder.append(newLine);
        }

        if (StringUtils.isNotBlank(port)) {
            builder.append("PORT / ENDPOINT: ");
            builder.append(port);
            builder.append(newLine);
        }

        if (StringUtils.isNotBlank(locationURI)) {
            builder.append("LOCATION URI: ");
            builder.append(locationURI);
            builder.append(newLine);
        }

        if (StringUtils.isNotBlank(soapAction)) {
            builder.append("SOAP ACTION: ");
            builder.append(soapAction);
            builder.append(newLine);
        }

        if (isUseHeadersVariable()) {
            builder.append(newLine);
            builder.append("[HEADERS]");
            builder.append(newLine);
            builder.append("Using variable '" + getHeadersVariable() + "'");
        } else if (MapUtils.isNotEmpty(headers)) {
            builder.append(newLine);
            builder.append("[HEADERS]");
            builder.append(newLine);
            for (Map.Entry<String, List<String>> header : headers.entrySet()) {
                for (String value : (ArrayList<String>) header.getValue()) {
                    builder.append(header.getKey().toString());
                    builder.append(": ");
                    builder.append(value.toString());
                    builder.append(newLine);
                }
            }
        }

        builder.append(newLine);
        builder.append("[ATTACHMENTS]");
        if (isUseAttachmentsVariable()) {
            builder.append("Using variable '" + getAttachmentsVariable() + "'");
        } else {
            for (int i = 0; i < attachmentNames.size(); i++) {
                builder.append(newLine);
                builder.append(attachmentNames.get(i));
                builder.append(" (");
                builder.append(attachmentTypes.get(i));
                builder.append(")");
            }
        }
        builder.append(newLine);

        builder.append(newLine);
        builder.append("[CONTENT]");
        builder.append(newLine);
        builder.append(envelope);
        return builder.toString();
    }

    @Override
    public DestinationConnectorProperties getDestinationConnectorProperties() {
        return destinationConnectorProperties;
    }

    @Override
    public ConnectorProperties clone() {
        return new WebServiceDispatcherProperties(this);
    }

    @Override
    public boolean canValidateResponse() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    // @formatter:off
    @Override public void migrate3_0_1(DonkeyElement element) {}
    @Override public void migrate3_0_2(DonkeyElement element) {} // @formatter:on

    @Override
    public void migrate3_1_0(DonkeyElement element) {
        super.migrate3_1_0(element);

        element.removeChild("wsdlCacheId");
        element.addChildElementIfNotExists("locationURI", "");

        DonkeyElement operations = element.removeChild("wsdlOperations");
        String service = element.getChildElement("service").getTextContent();
        String port = element.getChildElement("port").getTextContent();

        if (StringUtils.isNotBlank(service) && StringUtils.isNotBlank(port)) {
            DefinitionServiceMap wsdlDefinitionMap = new DefinitionServiceMap();
            DefinitionPortMap portMap = new DefinitionPortMap();
            List<String> operationList = new ArrayList<String>();

            if (operations != null) {
                for (DonkeyElement operation : operations.getChildElements()) {
                    operationList.add(operation.getTextContent());
                }
            }

            portMap.getMap().put(port, new PortInformation(operationList));
            wsdlDefinitionMap.getMap().put(service, portMap);

            try {
                if (element.getChildElement("wsdlDefinitionMap") == null) {
                    DonkeyElement definitionMapElement = element.addChildElementFromXml(ObjectXMLSerializer.getInstance().serialize(wsdlDefinitionMap));
                    definitionMapElement.setNodeName("wsdlDefinitionMap");
                }
            } catch (DonkeyElementException e) {
                throw new SerializerException("Failed to migrate Web Service Sender operation list.", e);
            }
        }

        element.addChildElementIfNotExists("socketTimeout", "0");

        Map<String, String> headers = new LinkedHashMap<String, String>();
        try {
            if (element.getChildElement("headers") == null) {
                DonkeyElement headersElement = element.addChildElementFromXml(ObjectXMLSerializer.getInstance().serialize(headers));
                headersElement.setNodeName("headers");
            }
        } catch (DonkeyElementException e) {
            throw new SerializerException("Failed to migrate Web Service Sender headers.", e);
        }
    }

    @Override
    public void migrate3_2_0(DonkeyElement element) {
        if (element.getChildElement("headers") != null) {
            DonkeyElement oldHeaders = element.removeChild("headers");
            DonkeyElement newHeaders = element.addChildElement("headers");
            newHeaders.setAttribute("class", "linked-hash-map");

            for (DonkeyElement oldEntry : oldHeaders.getChildElements()) {
                if (oldEntry.getChildElements().size() >= 2) {
                    DonkeyElement entry = newHeaders.addChildElement("entry");
                    entry.addChildElement("string", oldEntry.getChildElements().get(0).getTextContent());
                    entry.addChildElement("list").addChildElement("string", oldEntry.getChildElements().get(1).getTextContent());
                }
            }
        }
    }

    // @formatter:off
    @Override public void migrate3_3_0(DonkeyElement element) {}
    @Override public void migrate3_4_0(DonkeyElement element) {}
    @Override public void migrate3_5_0(DonkeyElement element) {}
    @Override public void migrate3_6_0(DonkeyElement element) {}
    @Override public void migrate3_7_0(DonkeyElement element) {}
    @Override public void migrate3_9_0(DonkeyElement element) {} // @formatter:on

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = super.getPurgedProperties();
        purgedProperties.put("destinationConnectorProperties", destinationConnectorProperties.getPurgedProperties());
        purgedProperties.put("useAuthentication", useAuthentication);
        purgedProperties.put("envelopeLines", PurgeUtil.countLines(envelope));
        purgedProperties.put("oneWay", oneWay);
        purgedProperties.put("headersCount", headers.size());
        purgedProperties.put("useMtom", useMtom);
        purgedProperties.put("attachmentNamesCount", attachmentNames.size());
        purgedProperties.put("attachmentContentCount", attachmentContents.size());
        purgedProperties.put("wsdlDefinitionMapCount", wsdlDefinitionMap != null ? wsdlDefinitionMap.getMap().size() : 0);
        purgedProperties.put("socketTimeout", PurgeUtil.getNumericValue(socketTimeout));
        return purgedProperties;
    }
}
