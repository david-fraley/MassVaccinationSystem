/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.server.data.jdbc;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mirth.connect.donkey.model.channel.MetaDataColumn;
import com.mirth.connect.donkey.model.channel.MetaDataColumnType;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.ContentType;
import com.mirth.connect.donkey.model.message.ErrorContent;
import com.mirth.connect.donkey.model.message.MapContent;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.MessageContent;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.model.message.attachment.Attachment;
import com.mirth.connect.donkey.server.Donkey;
import com.mirth.connect.donkey.server.Encryptor;
import com.mirth.connect.donkey.server.channel.Channel;
import com.mirth.connect.donkey.server.channel.Statistics;
import com.mirth.connect.donkey.server.data.ChannelDoesNotExistException;
import com.mirth.connect.donkey.server.data.DonkeyDao;
import com.mirth.connect.donkey.server.data.DonkeyDaoException;
import com.mirth.connect.donkey.server.data.StatisticsUpdater;
import com.mirth.connect.donkey.util.MapUtil;
import com.mirth.connect.donkey.util.SerializerProvider;

public class JdbcDao implements DonkeyDao {
    private Donkey donkey;
    private Connection connection;
    private QuerySource querySource;
    private PreparedStatementSource statementSource;
    private SerializerProvider serializerProvider;
    private boolean encryptData;
    private boolean decryptData;
    private StatisticsUpdater statisticsUpdater;
    private Set<ContentType> alwaysDecrypt = new HashSet<ContentType>();
    private Encryptor encryptor;
    private Statistics currentStats;
    private Statistics totalStats;
    private Statistics transactionStats = new Statistics(false, true);
    private Map<String, Map<Integer, Set<Status>>> resetCurrentStats = new HashMap<String, Map<Integer, Set<Status>>>();
    private Map<String, Map<Integer, Set<Status>>> resetTotalStats = new HashMap<String, Map<Integer, Set<Status>>>();
    private List<String> removedChannelIds = new ArrayList<String>();
    private String asyncCommitCommand;
    private Map<String, Long> localChannelIds;
    private String statsServerId;
    private boolean transactionAlteredChannels = false;
    private char quoteChar = '"';
    private Logger logger = Logger.getLogger(this.getClass());

    protected JdbcDao(Donkey donkey, Connection connection, QuerySource querySource, PreparedStatementSource statementSource, SerializerProvider serializerProvider, boolean encryptData, boolean decryptData, StatisticsUpdater statisticsUpdater, Statistics currentStats, Statistics totalStats, String statsServerId) {
        this.donkey = donkey;
        this.connection = connection;
        this.querySource = querySource;
        this.statementSource = statementSource;
        this.serializerProvider = serializerProvider;
        this.encryptData = encryptData;
        this.decryptData = decryptData;
        this.statisticsUpdater = statisticsUpdater;
        this.currentStats = currentStats;
        this.totalStats = totalStats;
        this.statsServerId = statsServerId;
        encryptor = donkey.getEncryptor();
        alwaysDecrypt.addAll(Arrays.asList(ContentType.getMapTypes()));
        alwaysDecrypt.addAll(Arrays.asList(ContentType.getErrorTypes()));

        logger.debug("Opened connection");
    }

    @Override
    public void setEncryptData(boolean encryptData) {
        this.encryptData = encryptData;
    }

    @Override
    public void setDecryptData(boolean decryptData) {
        this.decryptData = decryptData;
    }

    @Override
    public void setStatisticsUpdater(StatisticsUpdater statisticsUpdater) {
        this.statisticsUpdater = statisticsUpdater;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    @Override
    public void insertMessage(Message message) {
        logger.debug(message.getChannelId() + "/" + message.getMessageId() + ": inserting message");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("insertMessage", message.getChannelId());
            statement.setLong(1, message.getMessageId());
            statement.setString(2, message.getServerId());
            statement.setTimestamp(3, new Timestamp(message.getReceivedDate().getTimeInMillis()));
            statement.setBoolean(4, message.isProcessed());

            Long originalId = message.getOriginalId();

            if (originalId != null) {
                statement.setLong(5, originalId);
            } else {
                statement.setNull(5, Types.BIGINT);
            }

            Long importId = message.getImportId();

            if (importId != null) {
                statement.setLong(6, importId);
            } else {
                statement.setNull(6, Types.BIGINT);
            }

            String importChannelId = message.getImportChannelId();

            if (importChannelId != null) {
                statement.setString(7, message.getImportChannelId());
            } else {
                statement.setNull(7, Types.VARCHAR);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void updateSendAttempts(ConnectorMessage connectorMessage) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + ": updating send attempts");

        Calendar sendDate = connectorMessage.getSendDate();
        Calendar responseDate = connectorMessage.getResponseDate();

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("updateSendAttempts", connectorMessage.getChannelId());
            statement.setInt(1, connectorMessage.getSendAttempts());
            statement.setTimestamp(2, sendDate == null ? null : new Timestamp(sendDate.getTimeInMillis()));
            statement.setTimestamp(3, responseDate == null ? null : new Timestamp(responseDate.getTimeInMillis()));
            statement.setInt(4, connectorMessage.getMetaDataId());
            statement.setLong(5, connectorMessage.getMessageId());
            statement.setString(6, connectorMessage.getServerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void insertMessageContent(MessageContent messageContent) {
        logger.debug(messageContent.getChannelId() + "/" + messageContent.getMessageId() + "/" + messageContent.getMetaDataId() + ": inserting message content (" + messageContent.getContentType().toString() + ")");

        insertContent(messageContent.getChannelId(), messageContent.getMessageId(), messageContent.getMetaDataId(), messageContent.getContentType(), messageContent.getContent(), messageContent.getDataType(), messageContent.isEncrypted());
    }

    @Override
    public void batchInsertMessageContent(MessageContent messageContent) {
        logger.debug(messageContent.getChannelId() + "/" + messageContent.getMessageId() + "/" + messageContent.getMetaDataId() + ": batch inserting message content (" + messageContent.getContentType().toString() + ")");

        PreparedStatement statement = null;
        try {
            String content;
            boolean encrypted;

            // Only encrypt if the content is not already encrypted
            if (encryptData && encryptor != null && !messageContent.isEncrypted()) {
                content = encryptor.encrypt(messageContent.getContent());
                encrypted = true;
            } else {
                content = messageContent.getContent();
                encrypted = messageContent.isEncrypted();
            }

            statement = prepareStatement("batchInsertMessageContent", messageContent.getChannelId());
            statement.setInt(1, messageContent.getMetaDataId());
            statement.setLong(2, messageContent.getMessageId());
            statement.setInt(3, messageContent.getContentType().getContentTypeCode());
            statement.setString(4, content);
            statement.setString(5, messageContent.getDataType());
            statement.setBoolean(6, encrypted);

            statement.addBatch();
            statement.clearParameters();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void executeBatchInsertMessageContent(String channelId) {
        logger.debug(channelId + ": executing batch message content insert");

        PreparedStatement statement = null;
        try {
            /*
             * MIRTH-3597 Batch statements need to have their own prepared statement object due to a
             * bug? in the Oracle 12 JDBC driver. Therefore we need to have both
             * insertMessageContent and insertMessageContentBatch even though they are the same
             * query. We should also call clearBatch() after each batch execution to verify that all
             * internal buffers are cleared.
             */
            statement = prepareStatement("batchInsertMessageContent", channelId);
            statement.executeBatch();
            statement.clearBatch();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void storeMessageContent(MessageContent messageContent) {
        logger.debug(messageContent.getChannelId() + "/" + messageContent.getMessageId() + "/" + messageContent.getMetaDataId() + ": updating message content (" + messageContent.getContentType().toString() + ")");

        storeContent(messageContent.getChannelId(), messageContent.getMessageId(), messageContent.getMetaDataId(), messageContent.getContentType(), messageContent.getContent(), messageContent.getDataType(), messageContent.isEncrypted());
    }

    private void insertContent(String channelId, long messageId, int metaDataId, ContentType contentType, String content, String dataType, boolean encrypted) {
        PreparedStatement statement = null;
        try {
            // Only encrypt if the content is not already encrypted
            if (encryptData && encryptor != null && !encrypted) {
                content = encryptor.encrypt(content);
                encrypted = true;
            }

            statement = prepareStatement("insertMessageContent", channelId);
            statement.setInt(1, metaDataId);
            statement.setLong(2, messageId);
            statement.setInt(3, contentType.getContentTypeCode());
            statement.setString(4, content);
            statement.setString(5, dataType);
            statement.setBoolean(6, encrypted);

            statement.executeUpdate();
            statement.clearParameters();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    public void storeContent(String channelId, long messageId, int metaDataId, ContentType contentType, String content, String dataType, boolean encrypted) {
        PreparedStatement statement = null;
        try {
            // Only encrypt if the content is not already encrypted
            if (encryptData && encryptor != null && !encrypted) {
                content = encryptor.encrypt(content);
                encrypted = true;
            }

            statement = prepareStatement("storeMessageContent", channelId);

            if (content == null) {
                statement.setNull(1, Types.LONGVARCHAR);
            } else {
                statement.setString(1, content);
            }

            statement.setString(2, dataType);
            statement.setBoolean(3, encrypted);
            statement.setInt(4, metaDataId);
            statement.setLong(5, messageId);
            statement.setInt(6, contentType.getContentTypeCode());

            int rowCount = statement.executeUpdate();
            statement.clearParameters();

            if (rowCount == 0) {
                // This is the same code as insertContent, without going through the encryption process again
                logger.debug(channelId + "/" + messageId + "/" + metaDataId + ": updating message content (" + contentType.toString() + ")");

                closeDatabaseObjectIfNeeded(statement);
                statement = prepareStatement("insertMessageContent", channelId);
                statement.setInt(1, metaDataId);
                statement.setLong(2, messageId);
                statement.setInt(3, contentType.getContentTypeCode());
                statement.setString(4, content);
                statement.setString(5, dataType);
                statement.setBoolean(6, encrypted);

                statement.executeUpdate();
                statement.clearParameters();
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void addChannelStatistics(Statistics statistics) {
        Set<String> failedChannelIds = null;

        for (Entry<String, Map<Integer, Map<Status, Long>>> channelEntry : statistics.getStats().entrySet()) {
            try {
                String channelId = channelEntry.getKey();
                Map<Integer, Map<Status, Long>> channelAndConnectorStats = channelEntry.getValue();
                Map<Integer, Map<Status, Long>> connectorStatsToUpdate = new HashMap<Integer, Map<Status, Long>>();
                Map<Status, Long> channelStats = channelAndConnectorStats.get(null);

                for (Entry<Integer, Map<Status, Long>> entry : channelAndConnectorStats.entrySet()) {
                    Integer metaDataId = entry.getKey();

                    // only add connector stats to the statsToUpdate list, not the channel stats
                    if (metaDataId != null) {
                        Map<Status, Long> connectorStats = entry.getValue();

                        if (hasUpdatableStatistics(connectorStats)) {
                            connectorStatsToUpdate.put(metaDataId, connectorStats);
                        }
                    }
                }

                /*
                 * MIRTH-3042: With certain channel configurations, SQL Server will encounter a
                 * deadlock scenario unless we always update the channel stats row and update it
                 * before the connector stats. We determined that this is because SQL Server creates
                 * a page lock when the statistics update statement references the existing row
                 * values in order to increment them (RECEIVED = RECEIVED + ?). Other databases such
                 * as Postgres use only row locks in this situation so they were not deadlocking.
                 * The deadlock scenario was only confirmed to happen with a channel with multiple
                 * asynchronous destinations and destination queues enabled.
                 */
                if (!connectorStatsToUpdate.isEmpty() || hasUpdatableStatistics(channelStats)) {
                    updateStatistics(channelId, null, channelStats);

                    for (Entry<Integer, Map<Status, Long>> entry : connectorStatsToUpdate.entrySet()) {
                        updateStatistics(channelId, entry.getKey(), entry.getValue());
                    }
                }
            } catch (ChannelDoesNotExistException e) {
                if (failedChannelIds == null) {
                    failedChannelIds = new HashSet<String>();
                }
                failedChannelIds.addAll(e.getChannelIds());
            }
        }

        if (failedChannelIds != null) {
            throw new ChannelDoesNotExistException(failedChannelIds);
        }
    }

    private boolean hasUpdatableStatistics(Map<Status, Long> stats) {
        return (stats.get(Status.RECEIVED) != 0 || stats.get(Status.FILTERED) != 0 || stats.get(Status.SENT) != 0 || stats.get(Status.ERROR) != 0);
    }

    private void updateStatistics(String channelId, Integer metaDataId, Map<Status, Long> stats) {
        long received = stats.get(Status.RECEIVED);
        long filtered = stats.get(Status.FILTERED);
        long sent = stats.get(Status.SENT);
        long error = stats.get(Status.ERROR);

        logger.debug(channelId + "/" + metaDataId + ": saving statistics");

        PreparedStatement statement = null;

        try {
            /*
             * Indicates whether case statements are used in the update statement, in which case the
             * number of bound parameters for each statistic will double.
             */
            boolean usingCase = false;

            if (metaDataId == null) {
                if (querySource.queryExists("updateChannelStatisticsWithCase")) {
                    usingCase = true;
                    statement = prepareStatement("updateChannelStatisticsWithCase", channelId);
                } else {
                    statement = prepareStatement("updateChannelStatistics", channelId);
                }
            } else {
                if (querySource.queryExists("updateConnectorStatisticsWithCase")) {
                    usingCase = true;
                    statement = prepareStatement("updateConnectorStatisticsWithCase", channelId);
                } else {
                    statement = prepareStatement("updateConnectorStatistics", channelId);
                }
            }

            // Keep track of the index since it will change depending on whether case statements are used
            int paramIndex = 1;

            if (usingCase) {
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, error);
                statement.setLong(paramIndex++, error);
                statement.setLong(paramIndex++, error);
                statement.setLong(paramIndex++, error);
            } else {
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, received);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, filtered);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, sent);
                statement.setLong(paramIndex++, error);
                statement.setLong(paramIndex++, error);
            }

            if (metaDataId != null) {
                statement.setInt(paramIndex++, metaDataId);
                statement.setString(paramIndex++, statsServerId);
            } else {
                statement.setString(paramIndex++, statsServerId);
            }

            if (statement.executeUpdate() == 0) {
                closeDatabaseObjectIfNeeded(statement);
                statement = prepareStatement("insertChannelStatistics", channelId);

                if (metaDataId == null) {
                    statement.setNull(1, Types.INTEGER);
                } else {
                    statement.setInt(1, metaDataId);
                }

                statement.setString(2, statsServerId);
                statement.setLong(3, received);
                statement.setLong(4, received);
                statement.setLong(5, filtered);
                statement.setLong(6, filtered);
                statement.setLong(7, sent);
                statement.setLong(8, sent);
                statement.setLong(9, error);
                statement.setLong(10, error);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void insertMessageAttachment(String channelId, long messageId, Attachment attachment) {
        logger.debug(channelId + "/" + messageId + ": inserting message attachment");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("insertMessageAttachment", channelId);
            statement.setString(1, attachment.getId());
            statement.setLong(2, messageId);
            statement.setString(3, attachment.getType());

            // The size of each segment of the attachment.
            int chunkSize = 10000000;

            if (attachment.getContent().length <= chunkSize) {
                // If there is only one segment, just store it
                statement.setInt(4, 1);
                statement.setInt(5, attachment.getContent().length);
                statement.setBytes(6, attachment.getContent());
                statement.executeUpdate();
            } else {
                // Use an input stream on the attachment content to segment the data.
                ByteArrayInputStream inputStream = new ByteArrayInputStream(attachment.getContent());
                // The order of the segment
                int segmentIndex = 1;

                // As long as there are bytes left
                while (inputStream.available() > 0) {
                    // Set the segment number
                    statement.setInt(4, segmentIndex++);
                    // Determine the segment size. If there are more bytes left than the chunk size, the size is the chunk size. Otherwise it is the number of remaining bytes
                    int segmentSize = Math.min(chunkSize, inputStream.available());
                    // Create a byte array to store the chunk
                    byte[] segment = new byte[segmentSize];
                    // Read the chunk from the input stream to the byte array
                    inputStream.read(segment, 0, segmentSize);
                    // Set the segment size
                    statement.setInt(5, segmentSize);
                    // Set the byte data
                    statement.setBytes(6, segment);
                    // Perform the insert
                    statement.executeUpdate();
                }
            }

            // Clear the parameters because the data held in memory could be quite large.
            statement.clearParameters();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void updateMessageAttachment(String channelId, long messageId, Attachment attachment) {
        logger.debug(channelId + "/" + messageId + ": updating message attachment");

        PreparedStatement segmentCountStatement = null;
        ResultSet segmentCountResult = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;

        try {
            segmentCountStatement = prepareStatement("selectMessageAttachmentSegmentCount", channelId);
            segmentCountStatement.setString(1, attachment.getId());
            segmentCountStatement.setLong(2, messageId);

            segmentCountResult = segmentCountStatement.executeQuery();
            segmentCountResult.next();
            int totalSegmentCount = segmentCountResult.getInt(1);

            updateStatement = prepareStatement("updateMessageAttachment", channelId);
            updateStatement.setString(1, attachment.getType());
            updateStatement.setString(4, attachment.getId());
            updateStatement.setLong(5, messageId);

            insertStatement = prepareStatement("insertMessageAttachment", channelId);
            insertStatement.setString(1, attachment.getId());
            insertStatement.setLong(2, messageId);
            insertStatement.setString(3, attachment.getType());

            // The size of each segment of the attachment.
            int chunkSize = 10000000;

            // Use an input stream on the attachment content to segment the data.
            ByteArrayInputStream inputStream = null;

            boolean done = false;
            int currentSegmentId = 1;

            while (!done) {
                PreparedStatement statement;
                int segmentIdIndex;
                int attachmentSizeIndex;
                int contentIndex;

                if (currentSegmentId <= totalSegmentCount) {
                    statement = updateStatement;
                    segmentIdIndex = 6;
                    attachmentSizeIndex = 2;
                    contentIndex = 3;
                } else {
                    statement = insertStatement;
                    segmentIdIndex = 4;
                    attachmentSizeIndex = 5;
                    contentIndex = 6;
                }

                // Set the segment number
                statement.setInt(segmentIdIndex, currentSegmentId);

                if (attachment.getContent().length <= chunkSize) {
                    // If there is only one segment, just store it
                    statement.setInt(attachmentSizeIndex, attachment.getContent().length);
                    statement.setBytes(contentIndex, attachment.getContent());
                    statement.executeUpdate();
                    currentSegmentId++;
                    done = true;
                } else {
                    if (inputStream == null) {
                        inputStream = new ByteArrayInputStream(attachment.getContent());
                    }

                    // As long as there are bytes left
                    if (inputStream.available() > 0) {
                        // Determine the segment size. If there are more bytes left than the chunk size, the size is the chunk size. Otherwise it is the number of remaining bytes
                        int segmentSize = Math.min(chunkSize, inputStream.available());
                        // Create a byte array to store the chunk
                        byte[] segment = new byte[segmentSize];
                        // Read the chunk from the input stream to the byte array
                        inputStream.read(segment, 0, segmentSize);
                        // Set the segment size
                        statement.setInt(attachmentSizeIndex, segmentSize);
                        // Set the byte data
                        statement.setBytes(contentIndex, segment);
                        // Perform the update
                        statement.executeUpdate();
                        currentSegmentId++;
                    } else {
                        done = true;
                    }
                }
            }

            // Clear the parameters because the data held in memory could be quite large.
            updateStatement.clearParameters();
            insertStatement.clearParameters();

            // Delete lingering segments
            if (totalSegmentCount >= currentSegmentId) {
                PreparedStatement deleteStatement = prepareStatement("deleteMessageAttachmentLingeringSegments", channelId);
                deleteStatement.setString(1, attachment.getId());
                deleteStatement.setLong(2, messageId);
                deleteStatement.setInt(3, currentSegmentId);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            List<AutoCloseable> dbObjects = new ArrayList<>();
            dbObjects.add(segmentCountStatement);
            dbObjects.add(segmentCountResult);
            dbObjects.add(updateStatement);
            dbObjects.add(insertStatement);
            closeDatabaseObjectsIfNeeded(dbObjects);
        }
    }

    @Override
    public void insertMetaData(ConnectorMessage connectorMessage, List<MetaDataColumn> metaDataColumns) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": inserting custom meta data");
        PreparedStatement statement = null;

        try {
            List<String> metaDataColumnNames = new ArrayList<String>();
            Map<String, Object> metaDataMap = connectorMessage.getMetaDataMap();

            for (MetaDataColumn metaDataColumn : metaDataColumns) {
                Object value = metaDataMap.get(metaDataColumn.getName());

                if (value != null) {
                    metaDataColumnNames.add(metaDataColumn.getName());
                }
            }

            // Don't do anything if all values were null
            if (!metaDataColumnNames.isEmpty()) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("localChannelId", getLocalChannelId(connectorMessage.getChannelId()));
                values.put("metaDataColumnNames", quoteChar + StringUtils.join(metaDataColumnNames, quoteChar + "," + quoteChar) + quoteChar);
                values.put("metaDataColumnPlaceholders", "?" + StringUtils.repeat(", ?", metaDataColumnNames.size() - 1));

                statement = connection.prepareStatement(querySource.getQuery("insertMetaData", values));
                statement.setInt(1, connectorMessage.getMetaDataId());
                statement.setLong(2, connectorMessage.getMessageId());
                int n = 3;

                for (MetaDataColumn metaDataColumn : metaDataColumns) {
                    Object value = metaDataMap.get(metaDataColumn.getName());

                    if (value != null) {
                        // @formatter:off
                        switch (metaDataColumn.getType()) {
                            case STRING: statement.setString(n, (String) value); break;
                            case NUMBER: statement.setBigDecimal(n, (BigDecimal) value); break;
                            case BOOLEAN: statement.setBoolean(n, (Boolean) value); break;
                            case TIMESTAMP: statement.setTimestamp(n, new Timestamp(((Calendar) value).getTimeInMillis())); break;
                        }
                        // @formatter:on

                        n++;
                    }
                }

                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DonkeyDaoException("Failed to insert connector message meta data", e);
        } finally {
            close(statement);
        }
    }

    @Override
    public void storeMetaData(ConnectorMessage connectorMessage, List<MetaDataColumn> metaDataColumns) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating custom meta data");
        PreparedStatement statement = null;

        try {
            List<String> metaDataColumnNames = new ArrayList<String>();
            Map<String, Object> metaDataMap = connectorMessage.getMetaDataMap();

            for (MetaDataColumn metaDataColumn : metaDataColumns) {
                Object value = metaDataMap.get(metaDataColumn.getName());

                if (value != null) {
                    metaDataColumnNames.add(metaDataColumn.getName());
                }
            }

            // Don't do anything if all values were null
            if (!metaDataColumnNames.isEmpty()) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("localChannelId", getLocalChannelId(connectorMessage.getChannelId()));
                values.put("metaDataColumnPlaceholders", quoteChar + StringUtils.join(metaDataColumnNames, quoteChar + " = ?, " + quoteChar) + quoteChar + " = ?");

                statement = connection.prepareStatement(querySource.getQuery("storeMetaData", values));
                int n = 1;

                for (MetaDataColumn metaDataColumn : metaDataColumns) {
                    Object value = metaDataMap.get(metaDataColumn.getName());

                    if (value != null) {
                        // @formatter:off
                        switch (metaDataColumn.getType()) {
                            case STRING: statement.setString(n, (String) value); break;
                            case NUMBER: statement.setBigDecimal(n, (BigDecimal) value); break;
                            case BOOLEAN: statement.setBoolean(n, (Boolean) value); break;
                            case TIMESTAMP: statement.setTimestamp(n, new Timestamp(((Calendar) value).getTimeInMillis())); break;
                        }
                        // @formatter:on

                        n++;
                    }
                }

                statement.setLong(n++, connectorMessage.getMessageId());
                statement.setInt(n, connectorMessage.getMetaDataId());

                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DonkeyDaoException("Failed to update connector message meta data", e);
        } finally {
            close(statement);
        }
    }

    @Override
    public void insertConnectorMessage(ConnectorMessage connectorMessage, boolean storeMaps, boolean updateStats) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": inserting connector message with" + (storeMaps ? "" : "out") + " maps");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("insertConnectorMessage", connectorMessage.getChannelId());
            statement.setInt(1, connectorMessage.getMetaDataId());
            statement.setLong(2, connectorMessage.getMessageId());
            statement.setString(3, connectorMessage.getServerId());
            statement.setTimestamp(4, new Timestamp(connectorMessage.getReceivedDate().getTimeInMillis()));
            statement.setString(5, Character.toString(connectorMessage.getStatus().getStatusCode()));

            if (connectorMessage.getConnectorName() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, connectorMessage.getConnectorName());
            }

            statement.setInt(7, connectorMessage.getSendAttempts());

            if (connectorMessage.getSendDate() == null) {
                statement.setNull(8, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(8, new Timestamp(connectorMessage.getSendDate().getTimeInMillis()));
            }

            if (connectorMessage.getResponseDate() == null) {
                statement.setNull(9, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(9, new Timestamp(connectorMessage.getResponseDate().getTimeInMillis()));
            }

            statement.setInt(10, connectorMessage.getErrorCode());
            statement.setInt(11, connectorMessage.getChainId());
            statement.setInt(12, connectorMessage.getOrderId());
            statement.executeUpdate();

            if (storeMaps) {
                updateSourceMap(connectorMessage);
                updateMaps(connectorMessage);
            }

            updateErrors(connectorMessage);

            if (updateStats) {
                transactionStats.update(connectorMessage.getChannelId(), connectorMessage.getMetaDataId(), connectorMessage.getStatus(), null);
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void deleteMessage(String channelId, long messageId) {
        logger.debug(channelId + "/" + messageId + ": deleting message");

        PreparedStatement statement = null;
        try {
            cascadeMessageDelete("deleteMessageCascadeAttachments", messageId, channelId);
            cascadeMessageDelete("deleteMessageCascadeMetadata", messageId, channelId);
            cascadeMessageDelete("deleteMessageCascadeContent", messageId, channelId);
            cascadeMessageDelete("deleteMessageCascadeConnectorMessage", messageId, channelId);

            statement = prepareStatement("deleteMessage", channelId);
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void deleteConnectorMessages(String channelId, long messageId, Set<Integer> metaDataIds) {
        logger.debug(channelId + "/" + messageId + ": deleting connector messages");
        long localChannelId = getLocalChannelId(channelId);

        PreparedStatement statement = null;
        try {
            if (metaDataIds == null) {
                cascadeMessageDelete("deleteMessageCascadeMetadata", messageId, channelId);
                cascadeMessageDelete("deleteMessageCascadeContent", messageId, channelId);

                statement = prepareStatement("deleteConnectorMessages", channelId);
                statement.setLong(1, messageId);
                statement.executeUpdate();
            } else {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("localChannelId", localChannelId);
                values.put("metaDataIds", StringUtils.join(metaDataIds, ','));

                cascadeMessageDelete("deleteConnectorMessagesByMetaDataIdsCascadeContent", messageId, values);
                cascadeMessageDelete("deleteConnectorMessagesByMetaDataIdsCascadeMetadata", messageId, values);

                statement = null;

                try {
                    statement = getConnection().prepareStatement(querySource.getQuery("deleteConnectorMessagesByMetaDataIds", values));
                    statement.setLong(1, messageId);
                    statement.executeUpdate();
                } finally {
                    close(statement);
                }
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void deleteMessageStatistics(String channelId, long messageId, Set<Integer> metaDataIds) {
        Map<Integer, ConnectorMessage> connectorMessages = getConnectorMessages(channelId, messageId, new ArrayList<Integer>(metaDataIds));
        ConnectorMessage sourceMessage = connectorMessages.get(0);

        /*
         * The server id on the source message indicates which server last processed/reprocessed the
         * message. We only want to delete the statistics if the current stats server
         * (statsServerId) is the server that last processed or reprocessed the message.
         */
        if (sourceMessage != null && sourceMessage.getServerId().equals(statsServerId)) {
            for (Entry<Integer, ConnectorMessage> entry : connectorMessages.entrySet()) {
                Integer metaDataId = entry.getKey();
                ConnectorMessage connectorMessage = entry.getValue();

                /*
                 * We also test if each connector message belongs to the statsServerId before
                 * deleting.
                 */
                if (connectorMessage.getServerId().equals(statsServerId) && (metaDataIds == null || metaDataIds.contains(metaDataId))) {
                    Status status = connectorMessage.getStatus();

                    Map<Status, Long> statsDiff = new HashMap<Status, Long>();
                    statsDiff.put(Status.RECEIVED, -1L);
                    statsDiff.put(status, -1L);

                    transactionStats.update(channelId, metaDataId, statsDiff);
                }
            }
        }
    }

    @Override
    public void updateStatus(ConnectorMessage connectorMessage, Status previousStatus) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating status from " + previousStatus.getStatusCode() + " to " + connectorMessage.getStatus().getStatusCode());

        PreparedStatement statement = null;
        try {
            // don't decrement the previous status if it was RECEIVED
            if (previousStatus == Status.RECEIVED) {
                previousStatus = null;
            }

            transactionStats.update(connectorMessage.getChannelId(), connectorMessage.getMetaDataId(), connectorMessage.getStatus(), previousStatus);

            statement = prepareStatement("updateStatus", connectorMessage.getChannelId());
            statement.setString(1, Character.toString(connectorMessage.getStatus().getStatusCode()));
            statement.setInt(2, connectorMessage.getMetaDataId());
            statement.setLong(3, connectorMessage.getMessageId());
            statement.setString(4, connectorMessage.getServerId());

            if (statement.executeUpdate() == 0) {
                throw new DonkeyDaoException("Failed to update connector message status, the connector message was removed from this server.");
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void updateErrors(ConnectorMessage connectorMessage) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating errors");

        boolean errorsUpdated = false;

        if (updateError(connectorMessage.getProcessingErrorContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.PROCESSING_ERROR)) {
            errorsUpdated = true;
        }

        if (updateError(connectorMessage.getPostProcessorErrorContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.POSTPROCESSOR_ERROR)) {
            errorsUpdated = true;
        }

        if (updateError(connectorMessage.getResponseErrorContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.RESPONSE_ERROR)) {
            errorsUpdated = true;
        }

        if (errorsUpdated) {
            updateErrorCode(connectorMessage);
        }
    }

    private boolean updateError(ErrorContent errorContent, String channelId, long messageId, int metaDataId, ContentType contentType) {
        String error = errorContent.getContent();
        boolean encrypted = errorContent.isEncrypted();
        boolean persisted = errorContent.isPersisted();

        if (StringUtils.isNotEmpty(error)) {
            if (persisted) {
                storeContent(channelId, messageId, metaDataId, contentType, error, null, encrypted);
            } else {
                insertContent(channelId, messageId, metaDataId, contentType, error, null, encrypted);
                errorContent.setPersisted(true);
            }
        } else if (persisted) {
            deleteMessageContentByMetaDataIdAndContentType(channelId, messageId, metaDataId, contentType);
        } else {
            return false;
        }

        return true;
    }

    private void updateErrorCode(ConnectorMessage connectorMessage) {
        PreparedStatement statement = null;
        try {
            statement = prepareStatement("updateErrorCode", connectorMessage.getChannelId());

            statement.setInt(1, connectorMessage.getErrorCode());
            statement.setInt(2, connectorMessage.getMetaDataId());
            statement.setLong(3, connectorMessage.getMessageId());
            statement.setString(4, connectorMessage.getServerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void updateMaps(ConnectorMessage connectorMessage) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating maps");

        // We do not include the source map here because that should only be inserted once with the raw content, and after that it's read-only
        updateMap(connectorMessage.getConnectorMapContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.CONNECTOR_MAP);
        updateMap(connectorMessage.getChannelMapContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.CHANNEL_MAP);
        updateMap(connectorMessage.getResponseMapContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.RESPONSE_MAP);
    }

    private void updateMap(MapContent mapContent, String channelId, long messageId, int metaDataId, ContentType contentType) {
        if (mapContent != null) {
            boolean encrypted = mapContent.isEncrypted();
            boolean persisted = mapContent.isPersisted();

            String content = null;
            if (encrypted) {
                content = (String) mapContent.getContent();
            } else {
                Map<String, Object> map = mapContent.getMap();
                if (MapUtils.isNotEmpty(map)) {
                    content = MapUtil.serializeMap(serializerProvider.getSerializer(metaDataId), map);
                }
            }

            if (content != null) {
                if (persisted) {
                    storeContent(channelId, messageId, metaDataId, contentType, content, null, encrypted);
                } else {
                    insertContent(channelId, messageId, metaDataId, contentType, content, null, encrypted);
                    mapContent.setPersisted(true);
                }
            } else if (persisted) {
                deleteMessageContentByMetaDataIdAndContentType(channelId, messageId, metaDataId, contentType);
            }
        }
    }

    @Override
    public void updateSourceMap(ConnectorMessage connectorMessage) {
        // Only insert the source map content for the source connector message
        if (connectorMessage.getMetaDataId() == 0) {
            logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating source map");

            updateMap(connectorMessage.getSourceMapContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.SOURCE_MAP);
        }
    }

    @Override
    public void updateResponseMap(ConnectorMessage connectorMessage) {
        logger.debug(connectorMessage.getChannelId() + "/" + connectorMessage.getMessageId() + "/" + connectorMessage.getMetaDataId() + ": updating response map");

        updateMap(connectorMessage.getResponseMapContent(), connectorMessage.getChannelId(), connectorMessage.getMessageId(), connectorMessage.getMetaDataId(), ContentType.RESPONSE_MAP);
    }

    @Override
    public void markAsProcessed(String channelId, long messageId) {
        logger.debug(channelId + "/" + messageId + ": marking as processed");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("markAsProcessed", channelId);
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void resetMessage(String channelId, long messageId) {
        logger.debug(channelId + "/" + messageId + ": resetting message");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("resetMessage", channelId);
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public Map<String, Long> getLocalChannelIds() {
        if (localChannelIds == null) {
            ResultSet resultSet = null;

            try {
                localChannelIds = new HashMap<String, Long>();
                resultSet = prepareStatement("getLocalChannelIds", null).executeQuery();

                while (resultSet.next()) {
                    localChannelIds.put(resultSet.getString(1), resultSet.getLong(2));
                }
            } catch (SQLException e) {
                throw new DonkeyDaoException(e);
            } finally {
                close(resultSet);
            }
        }

        return localChannelIds;
    }

    @Override
    public void removeChannel(String channelId) {
        if (!getLocalChannelIds().containsKey(channelId)) {
            return;
        }

        logger.debug(channelId + ": removing channel");

        transactionAlteredChannels = true;

        List<AutoCloseable> statements = new ArrayList<>();
        try {
            statements.add(prepareStatement("dropStatisticsTable", channelId));
            statements.add(prepareStatement("dropAttachmentsTable", channelId));
            statements.add(prepareStatement("dropCustomMetadataTable", channelId));
            statements.add(prepareStatement("dropMessageContentTable", channelId));
            statements.add(prepareStatement("dropMessageMetadataTable", channelId));
            statements.add(prepareStatement("dropMessageSequence", channelId));
            statements.add(prepareStatement("dropMessageTable", channelId));
            statements.add(prepareStatement("deleteChannel", channelId));

            for (AutoCloseable statement : statements) {
                ((PreparedStatement) statement).executeUpdate();
            }

            removedChannelIds.add(channelId);
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectsIfNeeded(statements);
        }
    }

    @Override
    public Long selectMaxLocalChannelId() {
        ResultSet resultSet = null;

        try {
            Long maxLocalChannelId = 0L;
            resultSet = prepareStatement("selectMaxLocalChannelId", null).executeQuery();

            if (resultSet.next()) {
                maxLocalChannelId = resultSet.getLong(1);
            }

            resultSet.close();
            return maxLocalChannelId;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public void deleteAllMessages(String channelId) {
        logger.debug(channelId + ": deleting all messages");

        List<AutoCloseable> statements = new ArrayList<>();
        try {
            // delete tables without constraints
            cascadeMessageDelete("deleteAllMessagesCascadeAttachments", channelId);
            cascadeMessageDelete("deleteAllMessagesCascadeMetadata", channelId);
            cascadeMessageDelete("deleteAllMessagesCascadeContent", channelId);

            PreparedStatement statement = null;
            // remove constraints so truncate can execute
            if (querySource.queryExists("dropConstraintMessageContentTable")) {
                statement = prepareStatement("dropConstraintMessageContentTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }
            if (querySource.queryExists("dropConstraintMessageCustomMetaDataTable")) {
                statement = prepareStatement("dropConstraintMessageCustomMetaDataTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }

            // delete
            cascadeMessageDelete("deleteAllMessagesCascadeConnectorMessage", channelId);

            // re-add constraints
            if (querySource.queryExists("addConstraintMessageCustomMetaDataTable")) {
                statement = prepareStatement("addConstraintMessageCustomMetaDataTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }
            if (querySource.queryExists("addConstraintMessageContentTable")) {
                statement = prepareStatement("addConstraintMessageContentTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }

            // remove constraints so truncate can execute
            if (querySource.queryExists("dropConstraintConnectorMessageTable")) {
                statement = prepareStatement("dropConstraintConnectorMessageTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }
            if (querySource.queryExists("dropConstraintAttachmentTable")) {
                statement = prepareStatement("dropConstraintAttachmentTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }

            // delete
            statement = prepareStatement("deleteAllMessages", channelId);
            statements.add(statement);
            statement.executeUpdate();

            // re-add constraints
            if (querySource.queryExists("addConstraintAttachmentTable")) {
                statement = prepareStatement("addConstraintAttachmentTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }
            if (querySource.queryExists("addConstraintConnectorMessageTable")) {
                statement = prepareStatement("addConstraintConnectorMessageTable", channelId);
                statements.add(statement);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectsIfNeeded(statements);
        }
    }

    @Override
    public void deleteMessageContent(String channelId, long messageId) {
        logger.debug(channelId + "/" + messageId + ": deleting content");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("deleteMessageContent", channelId);
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void deleteMessageContentByMetaDataIds(String channelId, long messageId, Set<Integer> metaDataIds) {
        logger.debug(channelId + "/" + messageId + ": deleting content by metadata IDs: " + String.valueOf(metaDataIds));

        PreparedStatement statement = null;
        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));
            values.put("metaDataIds", StringUtils.join(metaDataIds, ','));

            statement = getConnection().prepareStatement(querySource.getQuery("deleteMessageContentByMetaDataIds", values));
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    private void deleteMessageContentByMetaDataIdAndContentType(String channelId, long messageId, int metaDataId, ContentType contentType) {
        logger.debug(channelId + "/" + messageId + ": deleting content");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("deleteMessageContentByMetaDataIdAndContentType", channelId);
            statement.setLong(1, messageId);
            statement.setInt(2, metaDataId);
            statement.setInt(3, contentType.getContentTypeCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void deleteMessageAttachments(String channelId, long messageId) {
        logger.debug(channelId + "/" + messageId + ": deleting attachments");

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("deleteMessageAttachments", channelId);
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public List<MetaDataColumn> getMetaDataColumns(String channelId) {
        ResultSet columns = null;

        try {
            List<MetaDataColumn> metaDataColumns = new ArrayList<MetaDataColumn>();
            long localChannelId = getLocalChannelId(channelId);

            columns = connection.getMetaData().getColumns(connection.getCatalog(), null, "d_mcm" + localChannelId, null);

            if (!columns.next()) {
                columns.close();
                columns = connection.getMetaData().getColumns(connection.getCatalog(), null, "D_MCM" + localChannelId, null);

                if (!columns.next()) {
                    return metaDataColumns;
                }
            }

            do {
                String name = columns.getString("COLUMN_NAME");

                if (!name.equalsIgnoreCase("METADATA_ID") && !name.equalsIgnoreCase("MESSAGE_ID")) {
                    MetaDataColumnType columnType = MetaDataColumnType.fromSqlType(columns.getInt("DATA_TYPE"));

                    if (columnType == null) {
                        logger.error("Invalid custom metadata column: " + name + " (type: " + sqlTypeToString(columns.getInt("DATA_TYPE")) + ").");
                    } else {
                        metaDataColumns.add(new MetaDataColumn(name, columnType, null));
                    }
                }
            } while (columns.next());

            return metaDataColumns;
        } catch (Exception e) {
            throw new DonkeyDaoException("Failed to retrieve meta data columns", e);
        } finally {
            close(columns);
        }
    }

    @Override
    public void removeMetaDataColumn(String channelId, String columnName) {
        logger.debug(channelId + "/" + ": removing custom meta data column (" + columnName + ")");
        Statement statement = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));
            values.put("columnName", columnName);

            statement = connection.createStatement();

            if (querySource.queryExists("removeMetaDataColumnIndex")) {
                statement.executeUpdate(querySource.getQuery("removeMetaDataColumnIndex", values));
            }

            statement.executeUpdate(querySource.getQuery("removeMetaDataColumn", values));
        } catch (SQLException e) {
            throw new DonkeyDaoException("Failed to remove meta-data column", e);
        } finally {
            close(statement);
        }
    }

    @Override
    public long getMaxMessageId(String channelId) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = prepareStatement("getMaxMessageId", channelId);
            resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getLong(1) : null;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public long getMinMessageId(String channelId) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = prepareStatement("getMinMessageId", channelId);
            resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getLong(1) : null;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public long getNextMessageId(String channelId) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));

            statement = connection.createStatement();

            if (querySource.queryExists("lockMessageSequenceTable")) {
                statement.executeUpdate(querySource.getQuery("lockMessageSequenceTable", values));
            }

            resultSet = statement.executeQuery(querySource.getQuery("getNextMessageId", values));
            resultSet.next();
            long id = resultSet.getLong(1);
            close(resultSet);

            if (querySource.queryExists("incrementMessageIdSequence")) {
                statement.executeUpdate(querySource.getQuery("incrementMessageIdSequence", values));
            }

            return id;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public List<Attachment> getMessageAttachment(String channelId, long messageId) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            // Get the total size of each attachment by summing the sizes of its segments
            statement = prepareStatement("selectMessageAttachmentSizeByMessageId", channelId);
            statement.setLong(1, messageId);
            resultSet = statement.executeQuery();

            Map<String, Integer> attachmentSize = new HashMap<String, Integer>();
            while (resultSet.next()) {
                // Store the attachment size in a map with the attachment id as the key
                attachmentSize.put(resultSet.getString("id"), resultSet.getInt("attachment_size"));
            }

            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);

            // Get the attachment data
            statement = prepareStatement("selectMessageAttachmentByMessageId", channelId);
            statement.setLong(1, messageId);
            // Set the number of rows to be fetched into memory at a time. This limits the amount of memory required for the query.
            statement.setFetchSize(1);
            resultSet = statement.executeQuery();

            // Initialize the return object
            List<Attachment> attachments = new ArrayList<Attachment>();
            // The current attachment id that is being stitched together
            String currentAttachmentId = null;
            // The type of the current attachment
            String type = null;
            // Use an byte array to combine the segments
            byte[] content = null;
            int offset = 0;

            while (resultSet.next()) {
                // Get the attachment id of the current segment
                String attachmentId = resultSet.getString("id");

                // Ensure that the attachmentId is in the map we created earlier, otherwise don't return this attachment
                if (attachmentSize.containsKey(attachmentId)) {
                    // If starting a new attachment
                    if (!attachmentId.equals(currentAttachmentId)) {
                        // If there was a previous attachment, we need to finish it.
                        if (content != null) {
                            // Add the data in the output stream to the list of attachments to return
                            attachments.add(new Attachment(currentAttachmentId, content, type));
                        }
                        currentAttachmentId = attachmentId;
                        type = resultSet.getString("type");

                        // Initialize the byte array size to the exact size of the attachment. This should minimize the memory requirements if the numbers are correct.
                        // Use 0 as a backup in case the size is not in the map. (If trying to return an attachment that no longer exists)
                        content = new byte[attachmentSize.get(attachmentId)];
                        offset = 0;
                    }

                    // write the current segment to the output stream buffer
                    byte[] segment = resultSet.getBytes("content");

                    if (segment != null) {
                        System.arraycopy(segment, 0, content, offset, segment.length);

                        offset += segment.length;
                    }
                }
            }

            // Finish the message if one exists by adding it to the list of attachments to return
            if (content != null) {
                attachments.add(new Attachment(currentAttachmentId, content, type));
            }
            content = null;

            return attachments;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public Attachment getMessageAttachment(String channelId, String attachmentId, Long messageId) {
        ResultSet resultSet = null;
        Attachment attachment = new Attachment();
        PreparedStatement statement = null;
        try {
            // Get the total size of each attachment by summing the sizes of its segments
            statement = prepareStatement("selectMessageAttachmentSize", channelId);
            statement.setString(1, attachmentId);
            statement.setLong(2, messageId);
            resultSet = statement.executeQuery();

            int size = 0;
            if (resultSet.next()) {
                // Store the attachment size in a map with the attachment id as the key
                size = resultSet.getInt("attachment_size");
            }

            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);

            // Get the attachment data
            statement = prepareStatement("selectMessageAttachment", channelId);
            statement.setString(1, attachmentId);
            statement.setLong(2, messageId);
            // Set the number of rows to be fetched into memory at a time. This limits the amount of memory required for the query.
            statement.setFetchSize(1);
            resultSet = statement.executeQuery();

            // The type of the current attachment
            String type = null;

            // Initialize the output stream's buffer size to the exact size of the attachment. This should minimize the memory requirements if the numbers are correct.
            byte[] content = null;
            int offset = 0;

            while (resultSet.next()) {
                if (content == null) {
                    type = resultSet.getString("type");
                    content = new byte[size];
                }

                // write the current segment to the output stream buffer
                byte[] segment = resultSet.getBytes("content");

                if (segment != null) {
                    System.arraycopy(segment, 0, content, offset, segment.length);

                    offset += segment.length;
                }
            }

            // Finish the message if one exists by adding it to the list of attachments to return
            if (content != null) {
                attachment.setId(attachmentId);
                attachment.setContent(content);
                attachment.setType(type);
            }
            content = null;

            return attachment;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public List<Message> getUnfinishedMessages(String channelId, String serverId, int limit, Long minMessageId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<Long, Message> messageMap = new HashMap<Long, Message>();
            List<Message> messageList = new ArrayList<Message>();
            List<Long> messageIds = new ArrayList<Long>();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));
            params.put("limit", limit);

            statement = connection.prepareStatement(querySource.getQuery("getUnfinishedMessages", params));
            statement.setLong(1, minMessageId);
            statement.setString(2, serverId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = getMessageFromResultSet(channelId, resultSet);
                messageMap.put(message.getMessageId(), message);
                messageList.add(message);
                messageIds.add(message.getMessageId());
            }

            close(resultSet);
            close(statement);

            if (!messageIds.isEmpty()) {
                params = new HashMap<String, Object>();
                params.put("localChannelId", getLocalChannelId(channelId));
                params.put("messageIds", StringUtils.join(messageIds, ","));

                statement = connection.prepareStatement(querySource.getQuery("getConnectorMessagesByMessageIds", params));
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    ConnectorMessage connectorMessage = getConnectorMessageFromResultSet(channelId, resultSet, true, true);
                    messageMap.get(connectorMessage.getMessageId()).getConnectorMessages().put(connectorMessage.getMetaDataId(), connectorMessage);
                }
            }

            return messageList;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public List<Message> getPendingConnectorMessages(String channelId, String serverId, int limit, Long minMessageId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<Long, Message> messageMap = new HashMap<Long, Message>();
            List<Message> messageList = new ArrayList<Message>();
            List<Long> messageIds = new ArrayList<Long>();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));
            params.put("limit", limit);

            statement = connection.prepareStatement(querySource.getQuery("getPendingMessageIds", params));
            statement.setLong(1, minMessageId);
            statement.setString(2, serverId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message();
                message.setMessageId(resultSet.getLong(1));
                messageMap.put(message.getMessageId(), message);
                messageList.add(message);
                messageIds.add(message.getMessageId());
            }

            close(resultSet);
            close(statement);

            if (!messageIds.isEmpty()) {
                params = new HashMap<String, Object>();
                params.put("localChannelId", getLocalChannelId(channelId));
                params.put("messageIds", StringUtils.join(messageIds, ","));

                statement = connection.prepareStatement(querySource.getQuery("getPendingConnectorMessages", params));
                statement.setString(1, serverId);

                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    ConnectorMessage connectorMessage = getConnectorMessageFromResultSet(channelId, resultSet, true, true);
                    messageMap.get(connectorMessage.getMessageId()).getConnectorMessages().put(connectorMessage.getMetaDataId(), connectorMessage);
                }
            }

            return messageList;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public List<Message> getMessages(String channelId, List<Long> messageIds) {
        if (messageIds.size() > 1000) {
            throw new DonkeyDaoException("Only up to 1000 message Ids at a time are supported.");
        }

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<Long, Message> messageMap = new LinkedHashMap<Long, Message>();

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));
            params.put("messageIds", StringUtils.join(messageIds, ","));

            statement = connection.prepareStatement(querySource.getQuery("getMessagesByMessageIds", params));
            resultSet = statement.executeQuery();

            // Get all message objects in the list and store them so they are accessible by message Id
            while (resultSet.next()) {
                Message message = getMessageFromResultSet(channelId, resultSet);
                messageMap.put(message.getMessageId(), message);
            }

            if (!messageIds.isEmpty()) {
                close(resultSet);
                close(statement);

                // Cache all message content for all messages in the list
                Map<Long, Map<Integer, List<MessageContent>>> messageContentMap = getMessageContent(channelId, messageIds);
                // Cache all metadata maps for all messages in the list
                Map<Long, Map<Integer, Map<String, Object>>> metaDataMaps = getMetaDataMaps(channelId, messageIds);

                params = new HashMap<String, Object>();
                params.put("localChannelId", getLocalChannelId(channelId));
                params.put("messageIds", StringUtils.join(messageIds, ","));

                // Perform a single query to retrieve all connector messages in the list at once
                statement = connection.prepareStatement(querySource.getQuery("getConnectorMessagesByMessageIds", params));
                resultSet = statement.executeQuery();

                ConnectorMessage sourceConnectorMessage = null;
                long currentMessageId = 0;
                while (resultSet.next()) {
                    // Create the connector from the result set without content or metadata map
                    ConnectorMessage connectorMessage = getConnectorMessageFromResultSet(channelId, resultSet, false, false);
                    // Store the reference to the connector in the message
                    messageMap.get(connectorMessage.getMessageId()).getConnectorMessages().put(connectorMessage.getMetaDataId(), connectorMessage);

                    // Whenever we start a new message, null the sourceConnectorMessage. This is in case the message has no source connector so we don't use the previous message's source
                    if (currentMessageId != connectorMessage.getMessageId()) {
                        currentMessageId = connectorMessage.getMessageId();
                        sourceConnectorMessage = null;
                    }

                    if (connectorMessage.getMetaDataId() == 0) {
                        // Store a reference to a message's source connector
                        sourceConnectorMessage = connectorMessage;
                    } else if (sourceConnectorMessage != null) {
                        // Load the destination's raw content and source map from the source
                        connectorMessage.setSourceMap(sourceConnectorMessage.getSourceMap());
                        MessageContent sourceEncoded = sourceConnectorMessage.getEncoded();
                        if (sourceEncoded != null) {
                            // If the source encoded exists, set it as the destination raw
                            MessageContent destinationRaw = new MessageContent(channelId, sourceEncoded.getMessageId(), connectorMessage.getMetaDataId(), ContentType.RAW, sourceEncoded.getContent(), sourceEncoded.getDataType(), sourceEncoded.isEncrypted());
                            connectorMessage.setRaw(destinationRaw);
                        }
                    }

                    // Get this connector's message content from the cache and store it in the connector
                    Map<Integer, List<MessageContent>> connectorMessageContentMap = messageContentMap.get(connectorMessage.getMessageId());
                    if (connectorMessageContentMap != null) {
                        List<MessageContent> messageContents = connectorMessageContentMap.get(connectorMessage.getMetaDataId());
                        if (messageContents != null) {
                            loadMessageContent(connectorMessage, messageContents);
                        }
                    }

                    // Get this connector's metadata map from the cache and store it in the connector
                    Map<Integer, Map<String, Object>> connectorMetaDataMap = metaDataMaps.get(connectorMessage.getMessageId());
                    if (connectorMetaDataMap != null) {
                        Map<String, Object> metaDataMap = connectorMetaDataMap.get(connectorMessage.getMetaDataId());
                        if (metaDataMap != null) {
                            connectorMessage.setMetaDataMap(metaDataMap);
                        }
                    }
                }
            }

            return new ArrayList<Message>(messageMap.values());
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public List<ConnectorMessage> getConnectorMessages(String channelId, String serverId, int metaDataId, Status status, int offset, int limit, Long minMessageId, Long maxMessageId) {
        List<ConnectorMessage> connectorMessages = new ArrayList<ConnectorMessage>();

        if (limit == 0) {
            return connectorMessages;
        }

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));
            params.put("offset", offset);
            params.put("limit", limit);

            int index = 1;

            if (minMessageId == null || maxMessageId == null) {
                statement = connection.prepareStatement(querySource.getQuery(serverId != null ? "getConnectorMessagesByMetaDataIdAndStatusWithLimit" : "getConnectorMessagesByMetaDataIdAndStatusWithLimitAllServers", params));
                statement.setInt(index++, metaDataId);
                statement.setString(index++, Character.toString(status.getStatusCode()));
                if (serverId != null) {
                    statement.setString(index++, serverId);
                }
            } else {
                statement = connection.prepareStatement(querySource.getQuery(serverId != null ? "getConnectorMessagesByMetaDataIdAndStatusWithLimitAndRange" : "getConnectorMessagesByMetaDataIdAndStatusWithLimitAndRangeAllServers", params));
                statement.setInt(index++, metaDataId);
                statement.setString(index++, Character.toString(status.getStatusCode()));
                if (serverId != null) {
                    statement.setString(index++, serverId);
                }
                statement.setLong(index++, minMessageId);
                statement.setLong(index++, maxMessageId);
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                connectorMessages.add(getConnectorMessageFromResultSet(channelId, resultSet, true, true));
            }

            return connectorMessages;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public List<ConnectorMessage> getConnectorMessages(String channelId, long messageId, Set<Integer> metaDataIds, boolean includeContent) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));
            values.put("metaDataIds", StringUtils.join(metaDataIds, ','));

            statement = connection.prepareStatement(querySource.getQuery("getConnectorMessagesByMessageIdAndMetaDataIds", values));
            statement.setLong(1, messageId);
            resultSet = statement.executeQuery();

            List<ConnectorMessage> connectorMessages = new ArrayList<ConnectorMessage>();

            while (resultSet.next()) {
                connectorMessages.add(getConnectorMessageFromResultSet(channelId, resultSet, includeContent, true));
            }

            return connectorMessages;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    @Override
    public Map<Integer, ConnectorMessage> getConnectorMessages(String channelId, long messageId, List<Integer> metaDataIds) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            boolean includeMetaDataIds = CollectionUtils.isNotEmpty(metaDataIds);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));

            if (includeMetaDataIds) {
                params.put("metaDataIds", StringUtils.join(metaDataIds, ','));
            }

            statement = getConnection().prepareStatement(querySource.getQuery(includeMetaDataIds ? "getConnectorMessagesByMessageIdAndMetaDataIds" : "getConnectorMessagesByMessageId", params));
            statement.setLong(1, messageId);
            resultSet = statement.executeQuery();

            Map<Integer, ConnectorMessage> connectorMessages = new HashMap<Integer, ConnectorMessage>();

            while (resultSet.next()) {
                ConnectorMessage connectorMessage = getConnectorMessageFromResultSet(channelId, resultSet, true, true);
                connectorMessages.put(connectorMessage.getMetaDataId(), connectorMessage);
            }

            return connectorMessages;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public Map<Integer, Status> getConnectorMessageStatuses(String channelId, long messageId, boolean checkProcessed) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = prepareStatement(checkProcessed ? "getConnectorMessageStatusesCheckProcessed" : "getConnectorMessageStatuses", channelId);
            statement.setLong(1, messageId);
            resultSet = statement.executeQuery();

            Map<Integer, Status> statusMap = new HashMap<Integer, Status>();

            while (resultSet.next()) {
                statusMap.put(resultSet.getInt(1), Status.fromChar(resultSet.getString(2).charAt(0)));
            }

            return statusMap;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public int getConnectorMessageCount(String channelId, String serverId, int metaDataId, Status status) {
        if (donkey.getDeployedChannels().get(channelId) != null || getLocalChannelIds().get(channelId) != null) {
            ResultSet resultSet = null;
            PreparedStatement statement = null;

            try {
                statement = statementSource.getPreparedStatement(serverId != null ? "getConnectorMessageCountByMetaDataIdAndStatus" : "getConnectorMessageCountByMetaDataIdAndStatusAllServers", getLocalChannelId(channelId));
                statement.setInt(1, metaDataId);
                statement.setString(2, Character.toString(status.getStatusCode()));
                if (serverId != null) {
                    statement.setString(3, serverId);
                }
                resultSet = statement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1);
            } catch (SQLException e) {
                throw new DonkeyDaoException(e);
            } finally {
                close(resultSet);
                closeDatabaseObjectIfNeeded(statement);
            }
        } else {
            // the channel has never been deployed
            return 0;
        }
    }

    @Override
    public long getConnectorMessageMaxMessageId(String channelId, String serverId, int metaDataId, Status status) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = prepareStatement(serverId != null ? "getConnectorMessageMaxMessageIdByMetaDataIdAndStatus" : "getConnectorMessageMaxMessageIdByMetaDataIdAndStatusAllServers", channelId);
            statement.setInt(1, metaDataId);
            statement.setString(2, Character.toString(status.getStatusCode()));
            if (serverId != null) {
                statement.setString(3, serverId);
            }
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    @Override
    public void addMetaDataColumn(String channelId, MetaDataColumn metaDataColumn) {
        logger.debug(channelId + ": adding custom meta data column (" + metaDataColumn.getName() + ")");
        Statement statement = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));
            values.put("columnName", metaDataColumn.getName());

            String queryName = "addMetaDataColumn" + StringUtils.capitalize(StringUtils.lowerCase(metaDataColumn.getType().toString()));

            statement = connection.createStatement();
            statement.executeUpdate(querySource.getQuery(queryName, values));

            if (querySource.queryExists(queryName + "Index")) {
                statement.executeUpdate(querySource.getQuery(queryName + "Index", values));
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException("Failed to add meta-data column", e);
        } finally {
            close(statement);
        }
    }

    @Override
    public void createChannel(String channelId, long localChannelId) {
        logger.debug(channelId + ": creating channel");
        Statement initSequenceStatement = null;
        transactionAlteredChannels = true;

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("createChannel", null);
            statement.setString(1, channelId);
            statement.setLong(2, localChannelId);
            statement.executeUpdate();

            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", localChannelId);

            createTable("createMessageTable", values);
            createTable("createConnectorMessageTable", values);
            createTable("createMessageContentTable", values);
            createTable("createMessageCustomMetaDataTable", values);
            createTable("createMessageAttachmentTable", values);
            createTable("createMessageStatisticsTable", values);
            createTable("createMessageSequence", values);

            if (querySource.queryExists("initMessageSequence")) {
                initSequenceStatement = getConnection().createStatement();
                initSequenceStatement.executeUpdate(querySource.getQuery("initMessageSequence", values));
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(initSequenceStatement);
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    private void createTable(String query, Map<String, Object> values) {
        if (querySource.queryExists(query)) {
            Statement statement = null;
            int n = 1;

            try {
                statement = connection.createStatement();
                statement.executeUpdate(querySource.getQuery(query, values));

                String sequenceQuery = querySource.getQuery(query + "Sequence", values);

                if (sequenceQuery != null) {
                    statement.executeUpdate(sequenceQuery);
                }

                String indexQuery = querySource.getQuery(query + "Index" + n, values);

                while (indexQuery != null) {
                    statement.executeUpdate(indexQuery);
                    indexQuery = querySource.getQuery(query + "Index" + (++n), values);
                }
            } catch (SQLException e) {
                throw new DonkeyDaoException(e);
            } finally {
                close(statement);
            }
        }
    }

    @Override
    public void checkAndCreateChannelTables() {
        Map<String, Long> channelIds = getLocalChannelIds();
        Map<String, String> channelTablesMap = new LinkedHashMap<String, String>();
        for (Long localChannelId : channelIds.values()) {
            channelTablesMap.put("d_m" + localChannelId, "createMessageTable");
            channelTablesMap.put("d_mm" + localChannelId, "createConnectorMessageTable");
            channelTablesMap.put("d_mc" + localChannelId, "createMessageContentTable");
            channelTablesMap.put("d_mcm" + localChannelId, "createMessageCustomMetaDataTable");
            channelTablesMap.put("d_ma" + localChannelId, "createMessageAttachmentTable");
            channelTablesMap.put("d_ms" + localChannelId, "createMessageStatisticsTable");
            channelTablesMap.put("d_msq" + localChannelId, "createMessageSequence");
        }

        ResultSet rs = null;
        Statement statement = null;
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            rs = dbMetaData.getTables(null, null, "%", null);

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME").toLowerCase();
                channelTablesMap.remove(tableName);
            }
            close(rs);

            /*
             * MIRTH-2851 - Oracle does not return sequences in the DatabaseMetaData.getTables
             * method. If we dont remove the d_msq sequence from oracle then it will try to recreate
             * it which will throw an exception. If any database needs an additional query for
             * sequences then it could just be added for that database.
             */

            if (querySource.queryExists("getSequenceMetadata")) {
                statement = connection.createStatement();
                rs = statement.executeQuery(querySource.getQuery("getSequenceMetadata"));
                while (rs.next()) {
                    String sequenceName = rs.getString("SEQUENCE_NAME").toLowerCase();
                    channelTablesMap.remove(sequenceName);
                }
            }
        } catch (Exception e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(rs);
            close(statement);
        }

        for (Entry<String, String> entry : channelTablesMap.entrySet()) {
            Statement initSequenceStatement = null;
            try {
                Long localChannelId = Long.parseLong(entry.getKey().replaceAll("[^0-9]", ""));
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("localChannelId", localChannelId);
                createTable(entry.getValue(), values);

                if (entry.getKey().toLowerCase().contains("d_msq") && querySource.queryExists("initMessageSequence")) {
                    initSequenceStatement = connection.createStatement();
                    initSequenceStatement.executeUpdate(querySource.getQuery("initMessageSequence", values));
                }
            } catch (Exception e) {
                throw new DonkeyDaoException(e);
            } finally {
                close(initSequenceStatement);
            }
        }
    }

    @Override
    public void resetStatistics(String channelId, Integer metaDataId, Set<Status> statuses) {
        logger.debug(channelId + ": resetting statistics" + (metaDataId == null ? "" : (" for metadata id " + metaDataId)));
        PreparedStatement statement = null;

        try {
            if (statuses == null || statuses.size() == 0) {
                return;
            }

            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));

            int count = 0;
            StringBuilder builder = new StringBuilder();
            for (Status status : statuses) {
                count++;

                if (count > 1) {
                    builder.append(",");
                }
                builder.append(status.toString() + " = 0");
            }
            values.put("statuses", builder.toString());

            String queryName = (metaDataId == null) ? "resetChannelStatistics" : "resetConnectorStatistics";
            statement = connection.prepareStatement(querySource.getQuery(queryName, values));
            statement.setString(1, statsServerId);

            if (metaDataId != null) {
                statement.setInt(2, metaDataId);
            }

            statement.executeUpdate();

            if (!resetCurrentStats.containsKey(channelId)) {
                resetCurrentStats.put(channelId, new HashMap<Integer, Set<Status>>());
            }

            Map<Integer, Set<Status>> metaDataIds = resetCurrentStats.get(channelId);

            if (!metaDataIds.containsKey(metaDataId)) {
                metaDataIds.put(metaDataId, statuses);
            }

        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(statement);
        }
    }

    @Override
    public void resetAllStatistics(String channelId) {
        logger.debug(channelId + ": resetting all statistics (including lifetime)");
        PreparedStatement statement = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));

            statement = connection.prepareStatement(querySource.getQuery("resetAllStatistics", values));
            statement.setString(1, statsServerId);
            statement.executeUpdate();

            Set<Status> statuses = Statistics.getTrackedStatuses();

            Map<Integer, Set<Status>> metaDataIdsCurrent = resetCurrentStats.get(channelId);
            if (metaDataIdsCurrent == null) {
                metaDataIdsCurrent = new HashMap<Integer, Set<Status>>();
                resetCurrentStats.put(channelId, metaDataIdsCurrent);
            }

            Map<Integer, Map<Status, Long>> channelCurrentStats = currentStats.getChannelStats(channelId);
            if (channelCurrentStats != null) {
                for (Entry<Integer, Map<Status, Long>> channelEntry : channelCurrentStats.entrySet()) {
                    metaDataIdsCurrent.put(channelEntry.getKey(), statuses);
                }
            }

            Map<Integer, Set<Status>> metaDataIdsTotal = resetTotalStats.get(channelId);
            if (metaDataIdsTotal == null) {
                metaDataIdsTotal = new HashMap<Integer, Set<Status>>();
                resetTotalStats.put(channelId, metaDataIdsTotal);
            }

            Map<Integer, Map<Status, Long>> channelTotalStats = totalStats.getChannelStats(channelId);
            if (channelTotalStats != null) {
                for (Entry<Integer, Map<Status, Long>> channelEntry : channelTotalStats.entrySet()) {
                    metaDataIdsTotal.put(channelEntry.getKey(), statuses);
                }
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(statement);
        }
    }

    @Override
    public Statistics getChannelStatistics(String serverId) {
        return getChannelStatistics(serverId, false);
    }

    @Override
    public Statistics getChannelTotalStatistics(String serverId) {
        return getChannelStatistics(serverId, true);
    }

    private Statistics getChannelStatistics(String serverId, boolean total) {
        Map<String, Long> channelIds = getLocalChannelIds();
        String queryId = (total) ? "getChannelTotalStatistics" : "getChannelStatistics";
        Statistics statistics = new Statistics(!total);
        ResultSet resultSet = null;

        for (String channelId : channelIds.keySet()) {
            PreparedStatement statement = null;
            try {
                statement = prepareStatement(queryId, channelId);
                statement.setString(1, serverId);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Integer metaDataId = resultSet.getInt("metadata_id");

                    if (resultSet.wasNull()) {
                        metaDataId = null;
                    }

                    Map<Status, Long> stats = new HashMap<Status, Long>();
                    stats.put(Status.RECEIVED, resultSet.getLong("received"));
                    stats.put(Status.FILTERED, resultSet.getLong("filtered"));
                    stats.put(Status.SENT, resultSet.getLong("sent"));
                    stats.put(Status.ERROR, resultSet.getLong("error"));

                    statistics.overwrite(channelId, metaDataId, stats);
                }
            } catch (SQLException e) {
                throw new DonkeyDaoException(e);
            } finally {
                close(resultSet);
                closeDatabaseObjectIfNeeded(statement);
            }
        }

        return statistics;
    }

    @Override
    public void commit() {
        commit(true);
    }

    @Override
    public void commit(boolean durable) {
        logger.debug("Committing transaction" + (durable ? "" : " asynchronously"));

        try {
            if (!durable && asyncCommitCommand != null) {
                Statement statement = null;

                try {
                    statement = connection.createStatement();
                    statement.execute(asyncCommitCommand);
                } finally {
                    close(statement);
                }
            } else {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }

        if (statisticsUpdater != null) {
            statisticsUpdater.update(transactionStats);
        }

        if (transactionAlteredChannels) {
            localChannelIds = null;
            transactionAlteredChannels = false;
        }

        if (currentStats != null) {
            // reset stats for any connectors that need to be reset
            for (Entry<String, Map<Integer, Set<Status>>> entry : resetCurrentStats.entrySet()) {
                String channelId = entry.getKey();
                Map<Integer, Set<Status>> metaDataIds = entry.getValue();

                for (Entry<Integer, Set<Status>> metaDataEntry : metaDataIds.entrySet()) {
                    Integer metaDataId = metaDataEntry.getKey();
                    Set<Status> statuses = metaDataEntry.getValue();

                    currentStats.resetStats(channelId, metaDataId, statuses);
                }
            }

            // Clear the reset stats map because we've just reset the stats
            resetCurrentStats.clear();

            // update the in-memory stats with the stats we just saved in storage
            currentStats.update(transactionStats);

            // remove the in-memory stats for any channels that were removed
            for (String channelId : removedChannelIds) {
                currentStats.remove(channelId);
            }
        }

        if (totalStats != null) {
            // reset stats for any connectors that need to be reset
            for (Entry<String, Map<Integer, Set<Status>>> entry : resetTotalStats.entrySet()) {
                String channelId = entry.getKey();
                Map<Integer, Set<Status>> metaDataIds = entry.getValue();

                for (Entry<Integer, Set<Status>> metaDataEntry : metaDataIds.entrySet()) {
                    Integer metaDataId = metaDataEntry.getKey();
                    Set<Status> statuses = metaDataEntry.getValue();

                    totalStats.resetStats(channelId, metaDataId, statuses);
                }
            }

            // Clear the reset stats map because we've just reset the stats
            resetTotalStats.clear();

            // update the in-memory total stats with the stats we just saved in storage
            totalStats.update(transactionStats);

            // remove the in-memory total stats for any channels that were removed
            for (String channelId : removedChannelIds) {
                totalStats.remove(channelId);
            }
        }

        transactionStats.clear();
    }

    @Override
    public void rollback() {
        logger.debug("Rolling back transaction");

        try {
            connection.rollback();
            transactionStats.clear();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }
    }

    @Override
    public void close() {
        logger.debug("Closing connection");

        try {
            if (!connection.isClosed()) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    logger.warn("Failed to rollback transaction", e);
                }
                connection.close();
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }
    }

    /*
     * Closes Statement or ResultSet, if needed
     */
    protected void closeDatabaseObjectIfNeeded(AutoCloseable dbObject) {
        // no op
    }

    /*
     * Closes any Statements or ResultSets, if needed
     */
    protected void closeDatabaseObjectsIfNeeded(List<AutoCloseable> dbObjects) {
        // no op
    }

    @Override
    public boolean initTableStructure() {
        if (!tableExists("d_channels")) {
            logger.debug("Creating channels table");
            createTable("createChannelsTable", null);
            return true;
        } else {
            return false;
        }
    }

    private boolean tableExists(String tableName) {
        ResultSet resultSet = null;

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getTables(null, null, tableName, null);

            if (resultSet.next()) {
                return true;
            }

            resultSet = metaData.getTables(null, null, tableName.toUpperCase(), null);
            return resultSet.next();
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getAsyncCommitCommand() {
        return asyncCommitCommand;
    }

    public void setAsyncCommitCommand(String asyncCommitCommand) {
        this.asyncCommitCommand = asyncCommitCommand;
    }

    private Message getMessageFromResultSet(String channelId, ResultSet resultSet) {
        try {
            Message message = new Message();
            long messageId = resultSet.getLong("id");
            Calendar receivedDate = Calendar.getInstance();
            receivedDate.setTimeInMillis(resultSet.getTimestamp("received_date").getTime());

            message.setMessageId(messageId);
            message.setChannelId(channelId);
            message.setReceivedDate(receivedDate);
            message.setProcessed(resultSet.getBoolean("processed"));
            message.setServerId(resultSet.getString("server_id"));
            message.setImportChannelId(resultSet.getString("import_channel_id"));

            long importId = resultSet.getLong("import_id");
            if (!resultSet.wasNull()) {
                message.setImportId(importId);
            }

            long originalId = resultSet.getLong("original_id");
            if (!resultSet.wasNull()) {
                message.setOriginalId(originalId);
            }

            return message;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }
    }

    private ConnectorMessage getConnectorMessageFromResultSet(String channelId, ResultSet resultSet, boolean includeContent, boolean includeMetaDataMap) {
        try {
            ConnectorMessage connectorMessage = new ConnectorMessage();
            long messageId = resultSet.getLong("message_id");
            int metaDataId = resultSet.getInt("id");
            Calendar receivedDate = Calendar.getInstance();
            receivedDate.setTimeInMillis(resultSet.getTimestamp("received_date").getTime());

            Calendar sendDate = null;
            Timestamp sendDateTimestamp = resultSet.getTimestamp("send_date");
            if (sendDateTimestamp != null) {
                sendDate = Calendar.getInstance();
                sendDate.setTimeInMillis(sendDateTimestamp.getTime());
            }

            Calendar responseDate = null;
            Timestamp responseDateTimestamp = resultSet.getTimestamp("response_date");
            if (responseDateTimestamp != null) {
                responseDate = Calendar.getInstance();
                responseDate.setTimeInMillis(responseDateTimestamp.getTime());
            }

            connectorMessage.setChannelName(getDeployedChannelName(channelId));
            connectorMessage.setMessageId(messageId);
            connectorMessage.setMetaDataId(metaDataId);
            connectorMessage.setChannelId(channelId);
            connectorMessage.setServerId(resultSet.getString("server_id"));
            connectorMessage.setConnectorName(resultSet.getString("connector_name"));
            connectorMessage.setReceivedDate(receivedDate);
            connectorMessage.setStatus(Status.fromChar(resultSet.getString("status").charAt(0)));
            connectorMessage.setSendAttempts(resultSet.getInt("send_attempts"));
            connectorMessage.setSendDate(sendDate);
            connectorMessage.setResponseDate(responseDate);
            connectorMessage.setErrorCode(resultSet.getInt("error_code"));
            connectorMessage.setChainId(resultSet.getInt("chain_id"));
            connectorMessage.setOrderId(resultSet.getInt("order_id"));

            if (includeContent) {
                if (metaDataId > 0) {
                    // For destination connectors, retrieve and load any content that is stored on the source connector
                    loadMessageContent(connectorMessage, getDestinationMessageContentFromSource(channelId, messageId, metaDataId));
                }

                // Retrive all content for the connector and load it into the connector message
                loadMessageContent(connectorMessage, getMessageContent(channelId, messageId, metaDataId));
            }

            if (includeMetaDataMap) {
                connectorMessage.setMetaDataMap(getMetaDataMap(channelId, messageId, metaDataId));
            }
            return connectorMessage;
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        }
    }

    /**
     * Get all message content for a messageId and metaDataId
     */
    private List<MessageContent> getMessageContent(String channelId, long messageId, int metaDataId) {
        List<MessageContent> messageContents = new ArrayList<MessageContent>();
        ResultSet resultSet = null;

        PreparedStatement statement = null;
        try {
            statement = prepareStatement("getMessageContent", channelId);
            statement.setLong(1, messageId);
            statement.setInt(2, metaDataId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String content = resultSet.getString("content");
                ContentType contentType = ContentType.fromCode(resultSet.getInt("content_type"));
                String dataType = resultSet.getString("data_type");
                boolean encrypted = resultSet.getBoolean("is_encrypted");

                if ((decryptData || alwaysDecrypt.contains(contentType)) && encrypted && encryptor != null) {
                    content = encryptor.decrypt(content);
                    encrypted = false;
                }

                messageContents.add(new MessageContent(channelId, messageId, metaDataId, contentType, content, dataType, encrypted));
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }

        return messageContents;
    }

    private Map<Long, Map<Integer, List<MessageContent>>> getMessageContent(String channelId, List<Long> messageIds) {
        if (messageIds.size() > 1000) {
            throw new DonkeyDaoException("Only up to 1000 message Ids at a time are supported.");
        }

        Map<Long, Map<Integer, List<MessageContent>>> messageContentMap = new LinkedHashMap<Long, Map<Integer, List<MessageContent>>>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", getLocalChannelId(channelId));
            params.put("messageIds", StringUtils.join(messageIds, ","));

            statement = connection.prepareStatement(querySource.getQuery("getMessageContentByMessageIds", params));

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long messageId = resultSet.getLong("message_id");
                Integer metaDataId = resultSet.getInt("metadata_id");
                String content = resultSet.getString("content");
                ContentType contentType = ContentType.fromCode(resultSet.getInt("content_type"));
                String dataType = resultSet.getString("data_type");
                boolean encrypted = resultSet.getBoolean("is_encrypted");

                if ((decryptData || alwaysDecrypt.contains(contentType)) && encrypted && encryptor != null) {
                    content = encryptor.decrypt(content);
                    encrypted = false;
                }

                Map<Integer, List<MessageContent>> connectorMessageContentMap = messageContentMap.get(messageId);
                if (connectorMessageContentMap == null) {
                    connectorMessageContentMap = new HashMap<Integer, List<MessageContent>>();
                    messageContentMap.put(messageId, connectorMessageContentMap);
                }

                List<MessageContent> messageContents = connectorMessageContentMap.get(metaDataId);
                if (messageContents == null) {
                    messageContents = new ArrayList<MessageContent>();
                    connectorMessageContentMap.put(metaDataId, messageContents);
                }

                messageContents.add(new MessageContent(channelId, messageId, metaDataId, contentType, content, dataType, encrypted));
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }

        return messageContentMap;
    }

    /**
     * Get all content for a destination connector that is stored with the source connector
     */
    private List<MessageContent> getDestinationMessageContentFromSource(String channelId, long messageId, int metaDataId) {
        List<MessageContent> messageContents = new ArrayList<MessageContent>();
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = prepareStatement("getDestinationMessageContentFromSource", channelId);
            statement.setLong(1, messageId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String content = resultSet.getString("content");
                ContentType contentType = ContentType.fromCode(resultSet.getInt("content_type"));
                String dataType = resultSet.getString("data_type");
                boolean encrypted = resultSet.getBoolean("is_encrypted");

                if ((decryptData || alwaysDecrypt.contains(contentType)) && encrypted && encryptor != null) {
                    content = encryptor.decrypt(content);
                    encrypted = false;
                }

                if (contentType == ContentType.ENCODED) {
                    contentType = ContentType.RAW;
                }

                messageContents.add(new MessageContent(channelId, messageId, metaDataId, contentType, content, dataType, encrypted));
            }
        } catch (SQLException e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            closeDatabaseObjectIfNeeded(statement);
        }

        return messageContents;
    }

    /**
     * Load message content into the connector message based on the content type
     */
    private void loadMessageContent(ConnectorMessage connectorMessage, List<MessageContent> messageContents) {
        for (MessageContent messageContent : messageContents) {
            switch (messageContent.getContentType()) {
                case RAW:
                    connectorMessage.setRaw(messageContent);
                    break;
                case PROCESSED_RAW:
                    connectorMessage.setProcessedRaw(messageContent);
                    break;
                case TRANSFORMED:
                    connectorMessage.setTransformed(messageContent);
                    break;
                case ENCODED:
                    connectorMessage.setEncoded(messageContent);
                    break;
                case SENT:
                    connectorMessage.setSent(messageContent);
                    break;
                case RESPONSE:
                    connectorMessage.setResponse(messageContent);
                    break;
                case RESPONSE_TRANSFORMED:
                    connectorMessage.setResponseTransformed(messageContent);
                    break;
                case PROCESSED_RESPONSE:
                    connectorMessage.setProcessedResponse(messageContent);
                    break;
                case CONNECTOR_MAP:
                    connectorMessage.setConnectorMapContent(getMapContentFromMessageContent(messageContent));
                    break;
                case CHANNEL_MAP:
                    connectorMessage.setChannelMapContent(getMapContentFromMessageContent(messageContent));
                    break;
                case RESPONSE_MAP:
                    connectorMessage.setResponseMapContent(getMapContentFromMessageContent(messageContent));
                    break;
                case PROCESSING_ERROR:
                    connectorMessage.setProcessingErrorContent(getErrorContentFromMessageContent(messageContent));
                    break;
                case POSTPROCESSOR_ERROR:
                    connectorMessage.setPostProcessorErrorContent(getErrorContentFromMessageContent(messageContent));
                    break;
                case RESPONSE_ERROR:
                    connectorMessage.setResponseErrorContent(getErrorContentFromMessageContent(messageContent));
                    break;
                case SOURCE_MAP:
                    connectorMessage.setSourceMapContent(getMapContentFromMessageContent(messageContent));
                    break;
            }
        }
    }

    private MapContent getMapContentFromMessageContent(MessageContent content) {
        if (content == null) {
            return new MapContent(new HashMap<String, Object>(), false);
        } else if (StringUtils.isBlank(content.getContent())) {
            return new MapContent(new HashMap<String, Object>(), true);
        }

        return new MapContent(MapUtil.deserializeMap(serializerProvider.getSerializer(content.getMetaDataId()), content.getContent()), true);
    }

    private ErrorContent getErrorContentFromMessageContent(MessageContent content) {
        if (content == null) {
            return new ErrorContent(null, false);
        }

        return new ErrorContent(content.getContent(), true);
    }

    private Map<String, Object> getMetaDataMap(String channelId, long messageId, int metaDataId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));

            // do not cache this statement since metadata columns may be added/removed
            statement = connection.prepareStatement(querySource.getQuery("getMetaDataMap", values));
            statement.setLong(1, messageId);
            statement.setInt(2, metaDataId);

            Map<String, Object> metaDataMap = new HashMap<String, Object>();
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    MetaDataColumnType metaDataColumnType = MetaDataColumnType.fromSqlType(resultSetMetaData.getColumnType(i));
                    Object value = null;

                    switch (metaDataColumnType) {//@formatter:off
                        case STRING: value = resultSet.getString(i); break;
                        case NUMBER: value = resultSet.getBigDecimal(i); break;
                        case BOOLEAN: value = resultSet.getBoolean(i); break;
                        case TIMESTAMP:
                            
                            Timestamp timestamp = resultSet.getTimestamp(i);
                            if (timestamp != null) {
                                value = Calendar.getInstance();
                                ((Calendar) value).setTimeInMillis(timestamp.getTime());
                            }
                            break;

                        default: throw new Exception("Unrecognized MetaDataColumnType");
                    } //@formatter:on

                    metaDataMap.put(resultSetMetaData.getColumnName(i).toUpperCase(), value);
                }
            }

            return metaDataMap;
        } catch (Exception e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    private Map<Long, Map<Integer, Map<String, Object>>> getMetaDataMaps(String channelId, List<Long> messageIds) {
        if (messageIds.size() > 1000) {
            throw new DonkeyDaoException("Only up to 1000 message Ids at a time are supported.");
        }

        Map<Long, Map<Integer, Map<String, Object>>> metaDataMaps = new HashMap<Long, Map<Integer, Map<String, Object>>>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("localChannelId", getLocalChannelId(channelId));
            values.put("messageIds", StringUtils.join(messageIds, ","));

            // do not cache this statement since metadata columns may be added/removed
            statement = connection.prepareStatement(querySource.getQuery("getMetaDataMapByMessageId", values));
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long messageId = resultSet.getLong("message_id");
                Integer metaDataId = resultSet.getInt("metadata_id");

                Map<Integer, Map<String, Object>> connectorMetaDataMap = metaDataMaps.get(messageId);
                if (connectorMetaDataMap == null) {
                    connectorMetaDataMap = new HashMap<Integer, Map<String, Object>>();
                    metaDataMaps.put(messageId, connectorMetaDataMap);
                }

                Map<String, Object> metaDataMap = connectorMetaDataMap.get(metaDataId);
                if (metaDataMap == null) {
                    metaDataMap = new HashMap<String, Object>();
                    connectorMetaDataMap.put(metaDataId, metaDataMap);
                }

                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    MetaDataColumnType metaDataColumnType = MetaDataColumnType.fromSqlType(resultSetMetaData.getColumnType(i));
                    Object value = null;

                    switch (metaDataColumnType) {//@formatter:off
                        case STRING: value = resultSet.getString(i); break;
                        case NUMBER: value = resultSet.getBigDecimal(i); break;
                        case BOOLEAN: value = resultSet.getBoolean(i); break;
                        case TIMESTAMP:
                            
                            Timestamp timestamp = resultSet.getTimestamp(i);
                            if (timestamp != null) {
                                value = Calendar.getInstance();
                                ((Calendar) value).setTimeInMillis(timestamp.getTime());
                            }
                            break;

                        default: throw new Exception("Unrecognized MetaDataColumnType");
                    } //@formatter:on

                    metaDataMap.put(resultSetMetaData.getColumnName(i).toUpperCase(), value);
                }
            }

            return metaDataMaps;
        } catch (Exception e) {
            throw new DonkeyDaoException(e);
        } finally {
            close(resultSet);
            close(statement);
        }
    }

    /**
     * When using Derby, we manually cascade the deletion of records from dependent tables rather
     * than relying on ON DELETE CASCADE behavior. Derby uses a table-level lock when cascading
     * deletes, which hinders concurrency and can increase the risk of deadlocks.
     */
    private void cascadeMessageDelete(String queryId, String channelId) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = prepareStatement(queryId, channelId);

            if (statement != null) {
                statement.executeUpdate();
            }
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    /**
     * When using Derby, we manually cascade the deletion of records from dependent tables rather
     * than relying on ON DELETE CASCADE behavior. Derby uses a table-level lock when cascading
     * deletes, which hinders concurrency and can increase the risk of deadlocks.
     */
    private void cascadeMessageDelete(String queryId, long messageId, String channelId) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = prepareStatement(queryId, channelId);

            if (statement != null) {
                statement.setLong(1, messageId);
                statement.executeUpdate();
            }
        } finally {
            closeDatabaseObjectIfNeeded(statement);
        }
    }

    /**
     * When using Derby, we manually cascade the deletion of records from dependent tables rather
     * than relying on ON DELETE CASCADE behavior. Derby uses a table-level lock when cascading
     * deletes, which hinders concurrency and can increase the risk of deadlocks.
     */
    private void cascadeMessageDelete(String queryId, long messageId, Map<String, Object> values) throws SQLException {
        String query = querySource.getQuery(queryId, values);

        if (query != null) {
            PreparedStatement statement = null;

            try {
                statement = connection.prepareStatement(query);
                statement.setLong(1, messageId);
                statement.executeUpdate();
            } finally {
                close(statement);
            }
        }
    }

    /**
     * Returns a prepared statement from the statementSource for the given channelId.
     */
    PreparedStatement prepareStatement(String queryId, String channelId) throws SQLException {
        Long localChannelId = null;

        if (channelId != null) {
            localChannelId = getLocalChannelId(channelId);
        }

        return statementSource.getPreparedStatement(queryId, localChannelId);
    }

    protected void close(Statement statement) {
        try {
            DbUtils.close(statement);
        } catch (SQLException e) {
            logger.error("Failed to close JDBC statement", e);
        }
    }

    protected void close(ResultSet resultSet) {
        try {
            DbUtils.close(resultSet);
        } catch (SQLException e) {
            logger.error("Failed to close JDBC result set", e);
        }
    }

    private static String sqlTypeToString(int sqlType) {
        switch (sqlType) {
            case Types.ARRAY:
                return "ARRAY";
            case Types.BIGINT:
                return "BIGINT";
            case Types.BINARY:
                return "BINARY";
            case Types.BIT:
                return "BIT";
            case Types.BLOB:
                return "BLOB";
            case Types.BOOLEAN:
                return "BOOLEAN";
            case Types.CHAR:
                return "CHAR";
            case Types.CLOB:
                return "CLOB";
            case Types.DATALINK:
                return "DATALINK";
            case Types.DATE:
                return "DATE";
            case Types.DECIMAL:
                return "DECIMAL";
            case Types.DISTINCT:
                return "DISTINCT";
            case Types.DOUBLE:
                return "DOUBLE";
            case Types.FLOAT:
                return "FLOAT";
            case Types.INTEGER:
                return "INTEGER";
            case Types.JAVA_OBJECT:
                return "JAVA_OBJECT";
            case Types.LONGNVARCHAR:
                return "LONGNVARCHAR";
            case Types.LONGVARBINARY:
                return "LONGVARBINARY";
            case Types.LONGVARCHAR:
                return "LONGVARCHAR";
            case Types.NCHAR:
                return "NCHAR";
            case Types.NCLOB:
                return "NCLOB";
            case Types.NULL:
                return "NULL";
            case Types.NUMERIC:
                return "NUMERIC";
            case Types.NVARCHAR:
                return "NVARCHAR";
            case Types.OTHER:
                return "OTHER";
            case Types.REAL:
                return "REAL";
            case Types.REF:
                return "REF";
            case Types.ROWID:
                return "ROWID";
            case Types.SMALLINT:
                return "SMALLINT";
            case Types.SQLXML:
                return "SQLXML";
            case Types.STRUCT:
                return "STRUCT";
            case Types.TIME:
                return "TIME";
            case Types.TIMESTAMP:
                return "TIMESTAMP";
            case Types.TINYINT:
                return "TINYINT";
            case Types.VARBINARY:
                return "VARBINARY";
            case Types.VARCHAR:
                return "VARCHAR";
            default:
                return "UNKNOWN";
        }
    }

    protected long getLocalChannelId(String channelId) {
        Channel channel = donkey.getDeployedChannels().get(channelId);

        if (channel == null) {
            Long localChannelId = getLocalChannelIds().get(channelId);

            if (localChannelId == null) {
                throw new ChannelDoesNotExistException(channelId);
            } else {
                return localChannelId;
            }
        } else {
            return channel.getLocalChannelId();
        }
    }

    protected String getDeployedChannelName(String channelId) {
        Channel channel = donkey.getDeployedChannels().get(channelId);
        return channel != null ? channel.getName() : "";
    }
}
