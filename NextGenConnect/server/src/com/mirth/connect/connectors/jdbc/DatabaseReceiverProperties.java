/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.jdbc;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.channel.PollConnectorProperties;
import com.mirth.connect.donkey.model.channel.PollConnectorPropertiesInterface;
import com.mirth.connect.donkey.model.channel.SourceConnectorProperties;
import com.mirth.connect.donkey.model.channel.SourceConnectorPropertiesInterface;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.purge.PurgeUtil;
import com.mirth.connect.util.CharsetUtils;

public class DatabaseReceiverProperties extends ConnectorProperties implements PollConnectorPropertiesInterface, SourceConnectorPropertiesInterface {
    public static final String NAME = "Database Reader";
    public static final String DRIVER_DEFAULT = "Please Select One";
    public static final String DRIVER_CUSTOM = "Custom";
    public static final int UPDATE_NEVER = 1;
    public static final int UPDATE_ONCE = 2;
    public static final int UPDATE_EACH = 3;

    private PollConnectorProperties pollConnectorProperties;
    private SourceConnectorProperties sourceConnectorProperties;
    private String driver;
    private String url;
    private String username;
    private String password;
    private String select;
    private String update;
    private boolean useScript;
    private boolean aggregateResults;
    private boolean cacheResults;
    private boolean keepConnectionOpen;
    private int updateMode;
    private String retryCount;
    private String retryInterval;
    private String fetchSize;
    private String encoding;

    public DatabaseReceiverProperties() {
        pollConnectorProperties = new PollConnectorProperties();
        sourceConnectorProperties = new SourceConnectorProperties();
        driver = DRIVER_DEFAULT;
        url = "";
        username = "";
        password = "";
        select = "";
        update = "";
        useScript = false;
        aggregateResults = false;
        cacheResults = true;
        keepConnectionOpen = true;
        updateMode = UPDATE_NEVER;
        retryCount = "3";
        retryInterval = "10000";
        fetchSize = "1000";
        encoding = CharsetUtils.DEFAULT_ENCODING;
    }

    @Override
    public String getProtocol() {
        return "jdbc";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toFormattedString() {
        return null;
    }

    @Override
    public PollConnectorProperties getPollConnectorProperties() {
        return pollConnectorProperties;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public boolean isUseScript() {
        return useScript;
    }

    public void setUseScript(boolean useScript) {
        this.useScript = useScript;
    }

    public boolean isAggregateResults() {
        return aggregateResults;
    }

    public void setAggregateResults(boolean aggregateResults) {
        this.aggregateResults = aggregateResults;
    }

    public boolean isCacheResults() {
        return cacheResults;
    }

    public void setCacheResults(boolean cacheResults) {
        this.cacheResults = cacheResults;
    }

    public boolean isKeepConnectionOpen() {
        return keepConnectionOpen;
    }

    public void setKeepConnectionOpen(boolean keepConnectionOpen) {
        this.keepConnectionOpen = keepConnectionOpen;
    }

    public int getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(int updateMode) {
        this.updateMode = updateMode;
    }

    public String getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    public String getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }

    public String getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(String fetchSize) {
        this.fetchSize = fetchSize;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public SourceConnectorProperties getSourceConnectorProperties() {
        return sourceConnectorProperties;
    }

    @Override
    public boolean canBatch() {
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
    }

    @Override
    public void migrate3_2_0(DonkeyElement element) {}

    @Override
    public void migrate3_3_0(DonkeyElement element) {
        element.addChildElementIfNotExists("encoding", CharsetUtils.DEFAULT_ENCODING);
    }

    // @formatter:off
    @Override public void migrate3_4_0(DonkeyElement element) {}
    @Override public void migrate3_5_0(DonkeyElement element) {}
    @Override public void migrate3_6_0(DonkeyElement element) {}
    @Override public void migrate3_7_0(DonkeyElement element) {}
    @Override public void migrate3_9_0(DonkeyElement element) {} // @formatter:on

    @Override
    public Map<String, Object> getPurgedProperties() {
        Map<String, Object> purgedProperties = super.getPurgedProperties();
        purgedProperties.put("pollConnectorProperties", pollConnectorProperties.getPurgedProperties());
        purgedProperties.put("sourceConnectorProperties", sourceConnectorProperties.getPurgedProperties());
        purgedProperties.put("driver", driver);
        purgedProperties.put("selectLines", PurgeUtil.countLines(select));
        purgedProperties.put("updateLines", PurgeUtil.countLines(update));
        purgedProperties.put("useScript", useScript);
        purgedProperties.put("aggregateResults", aggregateResults);
        purgedProperties.put("cacheResults", cacheResults);
        purgedProperties.put("keepConnectionOpen", keepConnectionOpen);
        purgedProperties.put("updateMode", updateMode);
        purgedProperties.put("retryCount", PurgeUtil.getNumericValue(retryCount));
        purgedProperties.put("retryInterval", PurgeUtil.getNumericValue(retryInterval));
        purgedProperties.put("fetchSize", PurgeUtil.getNumericValue(fetchSize));
        purgedProperties.put("encoding", encoding);
        return purgedProperties;
    }
}
