/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.file;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorProperties;
import com.mirth.connect.donkey.model.channel.DestinationConnectorPropertiesInterface;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.purge.PurgeUtil;
import com.mirth.connect.util.CharsetUtils;

public class FileDispatcherProperties extends ConnectorProperties implements DestinationConnectorPropertiesInterface {
    public static final String NAME = "File Writer";

    private DestinationConnectorProperties destinationConnectorProperties;

    private FileScheme scheme;
    private SchemeProperties schemeProperties;
    private String host;
    private String outputPattern;
    private boolean anonymous;
    private String username;
    private String password;
    private String timeout;
    private boolean keepConnectionOpen;
    private String maxIdleTime;
    private boolean secure;
    private boolean passive;
    private boolean validateConnection;
    private boolean outputAppend;
    private boolean errorOnExists;
    private boolean temporary;
    private boolean binary;
    private String charsetEncoding;
    private String template;

    public FileDispatcherProperties() {
        destinationConnectorProperties = new DestinationConnectorProperties();

        scheme = FileScheme.FILE;
        schemeProperties = null;
        host = "";
        outputPattern = "";
        anonymous = true;
        username = "anonymous";
        password = "anonymous";
        timeout = "10000";
        keepConnectionOpen = true;
        maxIdleTime = "0";
        secure = true;
        passive = true;
        validateConnection = true;
        outputAppend = true;
        errorOnExists = false;
        temporary = false;
        binary = false;
        charsetEncoding = CharsetUtils.DEFAULT_ENCODING;
        template = "";
    }

    public FileDispatcherProperties(FileDispatcherProperties props) {
        super(props);
        destinationConnectorProperties = new DestinationConnectorProperties(props.getDestinationConnectorProperties());

        scheme = props.getScheme();

        SchemeProperties properties = props.getSchemeProperties();
        if (properties != null) {
            schemeProperties = properties.clone();
        }

        host = props.getHost();
        outputPattern = props.getOutputPattern();
        anonymous = props.isAnonymous();
        username = props.getUsername();
        password = props.getPassword();
        timeout = props.getTimeout();
        keepConnectionOpen = props.isKeepConnectionOpen();
        maxIdleTime = props.getMaxIdleTime();
        secure = props.isSecure();
        passive = props.isPassive();
        validateConnection = props.isValidateConnection();
        outputAppend = props.isOutputAppend();
        errorOnExists = props.isErrorOnExists();
        temporary = props.isTemporary();
        binary = props.isBinary();
        charsetEncoding = props.getCharsetEncoding();
        template = props.getTemplate();
    }

    public FileScheme getScheme() {
        return scheme;
    }

    public void setScheme(FileScheme scheme) {
        this.scheme = scheme;
    }

    public SchemeProperties getSchemeProperties() {
        return schemeProperties;
    }

    public void setSchemeProperties(SchemeProperties schemeProperties) {
        this.schemeProperties = schemeProperties;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOutputPattern() {
        return outputPattern;
    }

    public void setOutputPattern(String outputPattern) {
        this.outputPattern = outputPattern;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
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

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public boolean isKeepConnectionOpen() {
        return keepConnectionOpen;
    }

    public void setKeepConnectionOpen(boolean keepConnectionOpen) {
        this.keepConnectionOpen = keepConnectionOpen;
    }

    public String getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(String maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isPassive() {
        return passive;
    }

    public void setPassive(boolean passive) {
        this.passive = passive;
    }

    public boolean isValidateConnection() {
        return validateConnection;
    }

    public void setValidateConnection(boolean validateConnection) {
        this.validateConnection = validateConnection;
    }

    public boolean isOutputAppend() {
        return outputAppend;
    }

    public void setOutputAppend(boolean outputAppend) {
        this.outputAppend = outputAppend;
    }

    public boolean isErrorOnExists() {
        return errorOnExists;
    }

    public void setErrorOnExists(boolean errorOnExists) {
        this.errorOnExists = errorOnExists;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public String getCharsetEncoding() {
        return charsetEncoding;
    }

    public void setCharsetEncoding(String charsetEncoding) {
        this.charsetEncoding = charsetEncoding;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public DestinationConnectorProperties getDestinationConnectorProperties() {
        return destinationConnectorProperties;
    }

    @Override
    public String getProtocol() {
        return "File";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "\n";

        builder.append("URI: ");
        appendURIString(builder);
        builder.append(newLine);

        if (!anonymous) {
            builder.append("USERNAME: ");
            builder.append(username);
            builder.append(newLine);
        }

        if (schemeProperties != null) {
            builder.append(schemeProperties.toFormattedString());
        }

        builder.append(newLine);
        builder.append("[CONTENT]");
        builder.append(newLine);
        builder.append(template);
        return builder.toString();
    }

    public String toURIString() {
        StringBuilder builder = new StringBuilder();
        appendURIString(builder);
        return builder.toString();
    }

    private void appendURIString(StringBuilder builder) {
        if (scheme == FileScheme.FTP) {
            builder.append("ftp://");
        } else if (scheme == FileScheme.SFTP) {
            builder.append("sftp://");
        } else if (scheme == FileScheme.S3) {
            builder.append("s3://");
        } else if (scheme == FileScheme.SMB) {
            builder.append("smb://");
        } else if (scheme == FileScheme.WEBDAV) {
            if (secure) {
                builder.append("https://");
            } else {
                builder.append("http://");
            }
        }

        builder.append(host);
        if (host.charAt(host.length() - 1) != '/') {
            builder.append("/");
        }
        builder.append(outputPattern);
    }

    @Override
    public ConnectorProperties clone() {
        return new FileDispatcherProperties(this);
    }

    @Override
    public boolean canValidateResponse() {
        return false;
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
    }

    @Override
    public void migrate3_2_0(DonkeyElement element) {}

    @Override
    public void migrate3_3_0(DonkeyElement element) {
        if (element.getChildElement("scheme").getTextContent().equalsIgnoreCase("sftp")) {
            DonkeyElement schemeProperties = element.addChildElementIfNotExists("schemeProperties");
            if (schemeProperties != null) {
                schemeProperties.setAttribute("class", "com.mirth.connect.connectors.file.SftpSchemeProperties");

                schemeProperties.addChildElementIfNotExists("passwordAuth", "true");
                schemeProperties.addChildElementIfNotExists("keyAuth", "false");
                schemeProperties.addChildElementIfNotExists("keyFile");
                schemeProperties.addChildElementIfNotExists("passPhrase");
                schemeProperties.addChildElementIfNotExists("hostKeyChecking", "ask");
                schemeProperties.addChildElementIfNotExists("knownHostsFile");
                schemeProperties.addChildElementIfNotExists("configurationSettings");
            }
        }
    }

    // @formatter:off
    @Override public void migrate3_4_0(DonkeyElement element) {}
    @Override public void migrate3_5_0(DonkeyElement element) {}
    @Override public void migrate3_6_0(DonkeyElement element) {} // @formatter:on

    @Override
    public void migrate3_7_0(DonkeyElement element) {
        element.addChildElementIfNotExists("keepConnectionOpen", "true");
        element.addChildElementIfNotExists("maxIdleTime", "0");
    }
    
    @Override
    public void migrate3_9_0(DonkeyElement element) {
    	if (element.getChildElement("scheme").getTextContent().equalsIgnoreCase("smb")) {
            DonkeyElement schemeProperties = element.addChildElementIfNotExists("schemeProperties");
            if (schemeProperties != null) {
                schemeProperties.setAttribute("class", "com.mirth.connect.connectors.file.SmbSchemeProperties");
                schemeProperties.addChildElementIfNotExists("smbMinVersion", "SMB1");
                schemeProperties.addChildElementIfNotExists("smbMaxVersion", "SMB311");
            }
        }
    }

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = super.getPurgedProperties();
        purgedProperties.put("destinationConnectorProperties", destinationConnectorProperties.getPurgedProperties());
        purgedProperties.put("scheme", scheme);
        purgedProperties.put("schemePurgedProperties", schemeProperties.getPurgedProperties());
        purgedProperties.put("timeout", PurgeUtil.getNumericValue(timeout));
        purgedProperties.put("keepConnectionOpen", keepConnectionOpen);
        purgedProperties.put("maxIdleTime", PurgeUtil.getNumericValue(maxIdleTime));
        purgedProperties.put("secure", secure);
        purgedProperties.put("passive", passive);
        purgedProperties.put("validateConnection", validateConnection);
        purgedProperties.put("outputAppend", outputAppend);
        purgedProperties.put("errorOnExists", errorOnExists);
        purgedProperties.put("temporary", temporary);
        purgedProperties.put("binary", binary);
        purgedProperties.put("charsetEncoding", charsetEncoding);
        purgedProperties.put("templateLines", PurgeUtil.countLines(template));
        return purgedProperties;
    }
}
