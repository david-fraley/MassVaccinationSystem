/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.server.data.passthru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mirth.connect.donkey.model.channel.MetaDataColumn;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.MessageContent;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.model.message.attachment.Attachment;
import com.mirth.connect.donkey.server.channel.Statistics;
import com.mirth.connect.donkey.server.controllers.ChannelController;
import com.mirth.connect.donkey.server.data.DonkeyDao;
import com.mirth.connect.donkey.server.data.StatisticsUpdater;

public class PassthruDao implements DonkeyDao {
    private boolean closed = false;
    private Statistics transactionStats = new Statistics(false, true);
    private Statistics currentStats;
    private Statistics totalStats;
    private Map<String, Map<Integer, Set<Status>>> resetStats = new HashMap<String, Map<Integer, Set<Status>>>();
    private List<String> removedChannelIds = new ArrayList<String>();
    private StatisticsUpdater statisticsUpdater;

    protected PassthruDao() {
        ChannelController channelController = ChannelController.getInstance();
        currentStats = channelController.getStatistics();
        totalStats = channelController.getTotalStatistics();

        // make sure these aren't null, otherwise commit() will break
        if (currentStats == null) {
            currentStats = new Statistics(true);
        }

        if (totalStats == null) {
            totalStats = new Statistics(false);
        }
    }

    public StatisticsUpdater getStatisticsUpdater() {
        return statisticsUpdater;
    }

    public void setStatisticsUpdater(StatisticsUpdater statisticsUpdater) {
        this.statisticsUpdater = statisticsUpdater;
    }

    @Override
    public void setEncryptData(boolean encryptData) {}

    @Override
    public void setDecryptData(boolean decryptData) {}

    @Override
    public void commit() {
        commit(false);
    }

    @Override
    public void commit(boolean durable) {
        // reset stats for any connectors that need to be reset
        for (Entry<String, Map<Integer, Set<Status>>> entry : resetStats.entrySet()) {
            String channelId = entry.getKey();
            Map<Integer, Set<Status>> metaDataIds = entry.getValue();

            for (Entry<Integer, Set<Status>> metaDataEntry : metaDataIds.entrySet()) {
                Integer metaDataId = metaDataEntry.getKey();
                Set<Status> statuses = metaDataEntry.getValue();

                currentStats.resetStats(channelId, metaDataId, statuses);
            }
        }

        // update the in-memory stats with the stats we just saved in storage
        currentStats.update(transactionStats);

        // remove the in-memory stats for any channels that were removed
        for (String channelId : removedChannelIds) {
            currentStats.remove(channelId);
        }

        // update the in-memory total stats with the stats we just saved in storage
        totalStats.update(transactionStats);

        // remove the in-memory total stats for any channels that were removed
        for (String channelId : removedChannelIds) {
            totalStats.remove(channelId);
        }

        if (statisticsUpdater != null) {
            statisticsUpdater.update(transactionStats);
        }

        transactionStats.clear();
    }

    @Override
    public void rollback() {
        transactionStats.clear();
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void insertConnectorMessage(ConnectorMessage connectorMessage, boolean storeMaps, boolean updateStats) {
        if (updateStats) {
            transactionStats.update(connectorMessage.getChannelId(), connectorMessage.getMetaDataId(), connectorMessage.getStatus(), null);
        }
    }

    @Override
    public void updateStatus(ConnectorMessage connectorMessage, Status previousStatus) {
        // don't decrement the previous status if it was RECEIVED
        if (previousStatus == Status.RECEIVED) {
            previousStatus = null;
        }

        transactionStats.update(connectorMessage.getChannelId(), connectorMessage.getMetaDataId(), connectorMessage.getStatus(), previousStatus);
    }

    @Override
    public void removeChannel(String channelId) {
        removedChannelIds.add(channelId);
    }

    @Override
    public void resetStatistics(String channelId, Integer metaDataId, Set<Status> statuses) {
        transactionStats.resetStats(channelId, metaDataId, statuses);

        if (!resetStats.containsKey(channelId)) {
            resetStats.put(channelId, new HashMap<Integer, Set<Status>>());
        }

        Map<Integer, Set<Status>> metaDataIds = resetStats.get(channelId);

        if (!metaDataIds.containsKey(metaDataId)) {
            metaDataIds.put(metaDataId, statuses);
        }
    }

    @Override
    public void resetAllStatistics(String channelId) {
        // Unsupported for this Dao implementation
    }

    @Override
    public void insertMessage(Message message) {}

    @Override
    public void insertMessageContent(MessageContent messageContent) {}

    @Override
    public void insertMessageAttachment(String channelId, long messageId, Attachment attachment) {}

    @Override
    public void updateMessageAttachment(String channelId, long messageId, Attachment attachment) {}

    @Override
    public void insertMetaData(ConnectorMessage connectorMessage, List<MetaDataColumn> metaDataColumns) {}

    @Override
    public void storeMetaData(ConnectorMessage connectorMessage, List<MetaDataColumn> metaDataColumns) {}

    @Override
    public void storeMessageContent(MessageContent messageContent) {}

    @Override
    public void addChannelStatistics(Statistics statistics) {}

    @Override
    public void updateSendAttempts(ConnectorMessage connectorMessage) {}

    @Override
    public void updateErrors(ConnectorMessage connectorMessage) {}

    @Override
    public void updateMaps(ConnectorMessage connectorMessage) {}

    @Override
    public void updateSourceMap(ConnectorMessage connectorMessage) {}

    @Override
    public void updateResponseMap(ConnectorMessage connectorMessage) {}

    @Override
    public void markAsProcessed(String channelId, long messageId) {}

    @Override
    public void resetMessage(String channelId, long messageId) {}

    @Override
    public void createChannel(String channelId, long localChannelId) {}

    @Override
    public boolean initTableStructure() {
        return false;
    }

    @Override
    public void checkAndCreateChannelTables() {}

    @Override
    public Map<String, Long> getLocalChannelIds() {
        return new HashMap<String, Long>();
    }

    @Override
    public Long selectMaxLocalChannelId() {
        return 1L;
    }

    @Override
    public void deleteAllMessages(String channelId) {}

    @Override
    public void deleteMessageContent(String channelId, long messageId) {}

    @Override
    public void deleteMessageContentByMetaDataIds(String channelId, long messageId, Set<Integer> metaDataIds) {}

    @Override
    public void deleteMessageAttachments(String channelId, long messageId) {}

    @Override
    public void addMetaDataColumn(String channelId, MetaDataColumn metaDataColumn) {}

    @Override
    public void removeMetaDataColumn(String channelId, String columnName) {}

    @Override
    public long getMaxMessageId(String channelId) {
        return 1L;
    }

    @Override
    public long getMinMessageId(String channelId) {
        return 1L;
    }

    @Override
    public long getNextMessageId(String channelId) {
        return 1L;
    }

    @Override
    public List<Attachment> getMessageAttachment(String channelId, long messageId) {
        return new ArrayList<Attachment>();
    }

    @Override
    public Attachment getMessageAttachment(String channelId, String attachmentId, Long messageId) {
        return new Attachment();
    }

    @Override
    public List<Message> getMessages(String channelId, List<Long> messageIds) {
        return new ArrayList<Message>();
    }

    @Override
    public Map<Integer, ConnectorMessage> getConnectorMessages(String channelId, long messageId, List<Integer> metaDataIds) {
        return new HashMap<Integer, ConnectorMessage>();
    }

    @Override
    public List<Message> getPendingConnectorMessages(String channelId, String serverId, int limit, Long minMessageId) {
        return new ArrayList<Message>();
    }

    @Override
    public List<ConnectorMessage> getConnectorMessages(String channelId, String serverId, int metaDataId, Status status, int offset, int limit, Long minMessageId, Long maxMessageId) {
        return new ArrayList<ConnectorMessage>();
    }

    @Override
    public Map<Integer, Status> getConnectorMessageStatuses(String channelId, long messageId, boolean checkProcessed) {
        return new HashMap<Integer, Status>();
    }

    @Override
    public List<Message> getUnfinishedMessages(String channelId, String serverId, int limit, Long minMessageId) {
        return new ArrayList<Message>();
    }

    @Override
    public int getConnectorMessageCount(String channelId, String serverId, int metaDataId, Status status) {
        return 0;
    }

    @Override
    public long getConnectorMessageMaxMessageId(String channelId, String serverId, int metaDataId, Status status) {
        return 0;
    }

    @Override
    public List<MetaDataColumn> getMetaDataColumns(String channelId) {
        return new ArrayList<MetaDataColumn>();
    }

    @Override
    public void deleteMessage(String channelId, long messageId) {}

    @Override
    public void deleteConnectorMessages(String channelId, long messageId, Set<Integer> metaDataIds) {}

    @Override
    public void deleteMessageStatistics(String channelId, long messageId, Set<Integer> metaDataIds) {}

    @Override
    public void batchInsertMessageContent(MessageContent messageContent) {}

    @Override
    public void executeBatchInsertMessageContent(String channelId) {}

    @Override
    public Statistics getChannelStatistics(String serverId) {
        return new Statistics(false);
    }

    @Override
    public Statistics getChannelTotalStatistics(String serverId) {
        return new Statistics(false);
    }

    @Override
    public List<ConnectorMessage> getConnectorMessages(String channelId, long messageId, Set<Integer> metaDataIds, boolean includeContent) {
        return null;
    }
}
