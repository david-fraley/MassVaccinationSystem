/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.log4j.Logger;

import com.mirth.commons.encryption.Encryptor;
import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.donkey.model.DonkeyException;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.ContentType;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.MessageContent;
import com.mirth.connect.donkey.model.message.RawMessage;
import com.mirth.connect.donkey.model.message.attachment.Attachment;
import com.mirth.connect.donkey.model.message.attachment.AttachmentHandlerProvider;
import com.mirth.connect.donkey.server.Constants;
import com.mirth.connect.donkey.server.Donkey;
import com.mirth.connect.donkey.server.channel.Channel;
import com.mirth.connect.donkey.server.channel.ChannelException;
import com.mirth.connect.donkey.server.controllers.ChannelController;
import com.mirth.connect.donkey.server.data.DonkeyDao;
import com.mirth.connect.donkey.server.message.DataType;
import com.mirth.connect.donkey.util.MapUtil;
import com.mirth.connect.donkey.util.xstream.SerializerException;
import com.mirth.connect.model.MessageImportResult;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.filters.MessageFilter;
import com.mirth.connect.model.filters.elements.ContentSearchElement;
import com.mirth.connect.model.filters.elements.MetaDataSearchElement;
import com.mirth.connect.server.ExtensionLoader;
import com.mirth.connect.server.channel.ErrorTaskHandler;
import com.mirth.connect.server.mybatis.MessageSearchResult;
import com.mirth.connect.server.mybatis.MessageTextResult;
import com.mirth.connect.server.util.DICOMMessageUtil;
import com.mirth.connect.server.util.ListRangeIterator;
import com.mirth.connect.server.util.ListRangeIterator.ListRangeItem;
import com.mirth.connect.server.util.SqlConfig;
import com.mirth.connect.util.AttachmentUtil;
import com.mirth.connect.util.MessageEncryptionUtil;
import com.mirth.connect.util.MessageExporter;
import com.mirth.connect.util.MessageExporter.MessageExportException;
import com.mirth.connect.util.MessageImporter;
import com.mirth.connect.util.MessageImporter.MessageImportException;
import com.mirth.connect.util.MessageImporter.MessageImportInvalidPathException;
import com.mirth.connect.util.PaginatedList;
import com.mirth.connect.util.messagewriter.AttachmentSource;
import com.mirth.connect.util.messagewriter.MessageWriter;
import com.mirth.connect.util.messagewriter.MessageWriterException;
import com.mirth.connect.util.messagewriter.MessageWriterFactory;
import com.mirth.connect.util.messagewriter.MessageWriterOptions;

public class DonkeyMessageController extends MessageController {
    private static MessageController instance = null;

    public static MessageController create() {
        synchronized (DonkeyMessageController.class) {
            if (instance == null) {
                instance = ExtensionLoader.getInstance().getControllerInstance(MessageController.class);

                if (instance == null) {
                    instance = new DonkeyMessageController();
                }
            }

            return instance;
        }
    }

    private Donkey donkey = Donkey.getInstance();
    private Logger logger = Logger.getLogger(this.getClass());

    private DonkeyMessageController() {}

    private Map<String, Object> getBasicParameters(MessageFilter filter, Long localChannelId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("localChannelId", localChannelId);
        params.put("originalIdLower", filter.getOriginalIdLower());
        params.put("originalIdUpper", filter.getOriginalIdUpper());
        params.put("importIdLower", filter.getImportIdLower());
        params.put("importIdUpper", filter.getImportIdUpper());
        params.put("startDate", filter.getStartDate());
        params.put("endDate", filter.getEndDate());
        params.put("serverId", filter.getServerId());
        params.put("statuses", filter.getStatuses());
        params.put("includedMetaDataIds", filter.getIncludedMetaDataIds());
        params.put("excludedMetaDataIds", filter.getExcludedMetaDataIds());
        params.put("sendAttemptsLower", filter.getSendAttemptsLower());
        params.put("sendAttemptsUpper", filter.getSendAttemptsUpper());
        params.put("attachment", filter.getAttachment());
        params.put("error", filter.getError());
        params.put("textSearch", filter.getTextSearch());
        params.put("textSearchRegex", filter.getTextSearchRegex());

        return params;
    }

    @Override
    public long getMaxMessageId(String channelId, boolean readOnly) {
        DonkeyDao dao = getDao(readOnly);

        try {
            return dao.getMaxMessageId(channelId);
        } finally {
            dao.close();
        }
    }

    @Override
    public long getMinMessageId(String channelId, boolean readOnly) {
        DonkeyDao dao = getDao(readOnly);

        try {
            return dao.getMinMessageId(channelId);
        } finally {
            dao.close();
        }
    }

    @Override
    public Long getMessageCount(MessageFilter filter, String channelId) {
        if (filter.getIncludedMetaDataIds() != null && filter.getIncludedMetaDataIds().isEmpty() && filter.getExcludedMetaDataIds() == null) {
            return 0L;
        }

        long startTime = System.currentTimeMillis();

        FilterOptions filterOptions = new FilterOptions(filter, channelId, true);
        long maxMessageId = filterOptions.getMaxMessageId();
        long minMessageId = filterOptions.getMinMessageId();

        Long localChannelId = ChannelController.getInstance().getLocalChannelId(channelId, true);
        Map<String, Object> params = getBasicParameters(filter, localChannelId);

        try {
            SqlSession session = getSqlSessionManager(true);

            long count = 0;
            long batchSize = 50000;

            while (maxMessageId >= minMessageId) {
                /*
                 * Search in descending order so that messages will be counted from the greatest to
                 * lowest message id
                 */
                long currentMinMessageId = Math.max(maxMessageId - batchSize + 1, minMessageId);
                params.put("maxMessageId", maxMessageId);
                params.put("minMessageId", currentMinMessageId);
                maxMessageId -= batchSize;

                Map<Long, MessageSearchResult> foundMessages = searchAll(session, params, filter, localChannelId, false, filterOptions);

                count += foundMessages.size();
            }

            return count;
        } finally {
            long endTime = System.currentTimeMillis();
            logger.debug("Count executed in " + (endTime - startTime) + "ms");
        }
    }

    @Override
    public List<Message> getMessages(MessageFilter filter, String channelId, Boolean includeContent, Integer offset, Integer limit) {
        List<Message> messages = new ArrayList<Message>();

        if (filter.getIncludedMetaDataIds() != null && filter.getIncludedMetaDataIds().isEmpty() && filter.getExcludedMetaDataIds() == null) {
            return messages;
        }

        List<MessageSearchResult> results = searchMessages(filter, channelId, offset, limit);

        DonkeyDao dao = getDao(true);

        /*
         * If the content is included, we don't want to decrypt because we may want to use the
         * encrypted content directly. If the content is not included, the setting shouldn't matter,
         * but we will set it to decrypt anyways just to be safe.
         */
        dao.setDecryptData(!includeContent);

        try {
            for (MessageSearchResult result : results) {
                Message message = result.getMessage();
                message.setChannelId(channelId);

                List<ConnectorMessage> connectorMessages = dao.getConnectorMessages(channelId, message.getMessageId(), result.getMetaDataIdSet(), includeContent);

                for (ConnectorMessage connectorMessage : connectorMessages) {
                    message.getConnectorMessages().put(connectorMessage.getMetaDataId(), connectorMessage);
                }

                messages.add(message);
            }
        } finally {
            dao.close();
        }

        return messages;
    }

    @Override
    public Message getMessageContent(String channelId, Long messageId, List<Integer> metaDataIds) {
        DonkeyDao dao = getDao(true);

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("localChannelId", ChannelController.getInstance().getLocalChannelId(channelId, true));
            params.put("messageId", messageId);

            Message message = SqlConfig.getInstance().getReadOnlySqlSessionManager().selectOne("Message.selectMessageById", params);

            if (message != null) {
                message.setChannelId(channelId);
            }

            Map<Integer, ConnectorMessage> connectorMessages = dao.getConnectorMessages(channelId, messageId, metaDataIds);

            for (Entry<Integer, ConnectorMessage> connectorMessageEntry : connectorMessages.entrySet()) {
                Integer metaDataId = connectorMessageEntry.getKey();
                ConnectorMessage connectorMessage = connectorMessageEntry.getValue();

                message.getConnectorMessages().put(metaDataId, connectorMessage);
            }

            return message;
        } finally {
            dao.close();
        }
    }

    @Override
    public List<Attachment> getMessageAttachmentIds(String channelId, Long messageId, boolean readOnly) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("localChannelId", ChannelController.getInstance().getLocalChannelId(channelId, readOnly));
        params.put("messageId", messageId);

        return getSqlSessionManager(readOnly).selectList("Message.selectMessageAttachmentIds", params);
    }

    @Override
    public Attachment getMessageAttachment(String channelId, String attachmentId, Long messageId, boolean readOnly) {
        DonkeyDao dao = getDao(readOnly);

        try {
            return dao.getMessageAttachment(channelId, attachmentId, messageId);
        } finally {
            dao.close();
        }
    }

    @Override
    public List<Attachment> getMessageAttachment(String channelId, Long messageId, boolean readOnly) {
        DonkeyDao dao = getDao(readOnly);

        try {
            return dao.getMessageAttachment(channelId, messageId);
        } finally {
            dao.close();
        }
    }

    @Override
    public void removeMessages(String channelId, MessageFilter filter) {
        EngineController engineController = ControllerFactory.getFactory().createEngineController();

        FilterOptions filterOptions = new FilterOptions(filter, channelId, false);
        long maxMessageId = filterOptions.getMaxMessageId();
        long minMessageId = filterOptions.getMinMessageId();

        Long localChannelId = ChannelController.getInstance().getLocalChannelId(channelId);
        Map<String, Object> params = getBasicParameters(filter, localChannelId);
        /*
         * Include the processed boolean with the result set in order to determine whether the
         * message can be deleted if the channel is not stopped
         */
        params.put("includeProcessed", true);

        SqlSession session = getSqlSessionManager(true);

        long batchSize = 50000;

        while (maxMessageId >= minMessageId) {
            /*
             * Search in descending order so that messages will be deleted from the greatest to
             * lowest message id
             */
            long currentMinMessageId = Math.max(maxMessageId - batchSize + 1, minMessageId);
            params.put("maxMessageId", maxMessageId);
            params.put("minMessageId", currentMinMessageId);
            maxMessageId -= batchSize;

            Map<Long, MessageSearchResult> results = searchAll(session, params, filter, localChannelId, true, filterOptions);

            ErrorTaskHandler handler = new ErrorTaskHandler();
            engineController.removeMessages(channelId, results, handler);
            if (handler.isErrored()) {
                logger.error("Remove messages task terminated due to error or halt.", handler.getError());
                break;
            }
        }

        Channel channel = engineController.getDeployedChannel(channelId);
        if (channel != null) {
            // Invalidate the queue buffer to ensure stats are updated.
            channel.invalidateQueues();
        }
    }

    public void reprocessMessages(String channelId, MessageFilter filter, boolean replace, Collection<Integer> reprocessMetaDataIds) throws ControllerException {
        EngineController engineController = ControllerFactory.getFactory().createEngineController();
        Channel deployedChannel = engineController.getDeployedChannel(channelId);
        if (deployedChannel == null) {
            throw new ControllerException("Channel is no longer deployed!");
        }

        AttachmentHandlerProvider attachmentHandlerProvider = deployedChannel.getAttachmentHandlerProvider();
        DataType dataType = deployedChannel.getSourceConnector().getInboundDataType();
        boolean isBinary = ExtensionController.getInstance().getDataTypePlugins().get(dataType.getType()).isBinary();
        Encryptor encryptor = ConfigurationController.getInstance().getEncryptor();

        FilterOptions filterOptions = new FilterOptions(filter, channelId, false);
        long maxMessageId = filterOptions.getMaxMessageId();
        long minMessageId = filterOptions.getMinMessageId();

        Long localChannelId = ChannelController.getInstance().getLocalChannelId(channelId);
        Map<String, Object> params = getBasicParameters(filter, localChannelId);
        /*
         * Include the import id with the result set in order to determine whether the message was
         * imported
         */
        params.put("includeImportId", true);

        SqlSession session = getSqlSessionManager(true);

        long batchSize = 50000;

        while (maxMessageId >= minMessageId) {
            /*
             * Search in ascending order so that messages will be reprocessed from the lowest to
             * greatest message id
             */
            long currentMaxMessageId = Math.min(minMessageId + batchSize - 1, maxMessageId);
            params.put("maxMessageId", currentMaxMessageId);
            params.put("minMessageId", minMessageId);
            minMessageId += batchSize;

            Map<Long, MessageSearchResult> foundMessages = new TreeMap<Long, MessageSearchResult>(searchAll(session, params, filter, localChannelId, true, filterOptions));

            for (Entry<Long, MessageSearchResult> entry : foundMessages.entrySet()) {
                Long messageId = entry.getKey();
                Long importId = entry.getValue().getImportId();
                params.put("messageId", messageId);

                List<MessageContent> contentList = session.selectList("Message.selectMessageForReprocessing", params);

                MessageContent rawContent = null;
                MessageContent sourceMapContent = null;

                if (contentList != null) {
                    for (MessageContent content : contentList) {
                        if (content.getContentType() == ContentType.RAW) {
                            rawContent = content;
                        } else if (content.getContentType() == ContentType.SOURCE_MAP) {
                            sourceMapContent = content;
                        }
                    }
                }

                if (rawContent != null) {
                    if (rawContent.isEncrypted()) {
                        rawContent.setContent(encryptor.decrypt(rawContent.getContent()));
                        rawContent.setEncrypted(false);
                    }

                    ConnectorMessage connectorMessage = new ConnectorMessage();
                    connectorMessage.setChannelId(channelId);
                    connectorMessage.setMessageId(messageId);
                    connectorMessage.setMetaDataId(0);
                    connectorMessage.setRaw(rawContent);

                    RawMessage rawMessage = null;

                    if (isBinary) {
                        rawMessage = new RawMessage(DICOMMessageUtil.getDICOMRawBytes(connectorMessage));
                    } else {
                        rawMessage = new RawMessage(org.apache.commons.codec.binary.StringUtils.newString(attachmentHandlerProvider.reAttachMessage(rawContent.getContent(), connectorMessage, Constants.ATTACHMENT_CHARSET, false, true, true), Constants.ATTACHMENT_CHARSET));
                    }

                    rawMessage.setOverwrite(replace);
                    rawMessage.setImported(importId != null);
                    rawMessage.setOriginalMessageId(messageId);

                    try {
                        Map<String, Object> sourceMap = rawMessage.getSourceMap();
                        if (sourceMapContent != null && sourceMapContent.getContent() != null) {
                            if (sourceMapContent.isEncrypted()) {
                                sourceMapContent.setContent(encryptor.decrypt(sourceMapContent.getContent()));
                                sourceMapContent.setEncrypted(false);
                            }

                            /*
                             * We do putAll instead of setting the source map directly here because
                             * the previously stored map will be unmodifiable. We need to set the
                             * destination metadata IDs after this, so the map needs to be
                             * modifiable.
                             */
                            sourceMap.putAll(MapUtil.deserializeMap(ObjectXMLSerializer.getInstance(), sourceMapContent.getContent()));
                        }

                        sourceMap.put(Constants.REPROCESSED_KEY, true);
                        sourceMap.put(Constants.REPLACED_KEY, replace);

                        // Set the destination metadata ID list here to overwrite anything that was previously stored 
                        rawMessage.setDestinationMetaDataIds(reprocessMetaDataIds);

                        engineController.dispatchRawMessage(channelId, rawMessage, true, false);
                    } catch (SerializerException e) {
                        logger.error("Could not reprocess message " + messageId + " for channel " + channelId + " because the source map content is invalid.", e);
                    } catch (ChannelException e) {
                        if (e.isStopped()) {
                            // This should only return true if the entire channel is stopped, since we are forcing the message even if the source connector is stopped.
                            logger.error("Reprocessing job cancelled because the channel is stopping or stopped.", e);
                            return;
                        }
                    } catch (Throwable e) {
                        // Do nothing. An error should have been logged.
                    }
                } else {
                    logger.error("Could not reprocess message " + messageId + " for channel " + channelId + " because no source raw content was found. The content may have been pruned or the channel may not be configured to store raw content.");
                }
            }
        }
    }

    @Override
    public int exportMessages(final String channelId, final MessageFilter messageFilter, int pageSize, MessageWriterOptions options) throws MessageExportException, InterruptedException {
        final MessageController messageController = this;

        PaginatedList<Message> messageList = new PaginatedList<Message>() {
            @Override
            public Long getItemCount() {
                return messageController.getMessageCount(messageFilter, channelId);
            }

            @Override
            protected List<Message> getItems(int offset, int limit) throws Exception {
                return messageController.getMessages(messageFilter, channelId, true, offset, limit);
            }
        };

        messageList.setPageSize(pageSize);

        try {
            MessageWriter messageWriter = MessageWriterFactory.getInstance().getMessageWriter(options, ConfigurationController.getInstance().getEncryptor());

            AttachmentSource attachmentSource = null;
            if (options.includeAttachments()) {
                attachmentSource = new AttachmentSource() {
                    @Override
                    public List<Attachment> getMessageAttachments(Message message) {
                        return MessageController.getInstance().getMessageAttachment(message.getChannelId(), message.getMessageId(), true);
                    }
                };
            }

            try {
                int numExported = new MessageExporter().exportMessages(messageList, messageWriter, attachmentSource);
                messageWriter.finishWrite();
                return numExported;
            } finally {
                messageWriter.close();
            }
        } catch (MessageWriterException e) {
            throw new MessageExportException(e);
        }
    }

    @Override
    public void exportAttachment(String channelId, String attachmentId, Long messageId, String filePath, boolean binary) throws IOException {
        AttachmentUtil.writeToFile(filePath, getMessageAttachment(channelId, attachmentId, messageId, true), binary);
    }

    @Override
    public void importMessage(String channelId, Message message) throws MessageImportException {
        try {
            MessageEncryptionUtil.decryptMessage(message, ConfigurationController.getInstance().getEncryptor());
            Channel channel = donkey.getDeployedChannels().get(channelId);

            if (channel == null) {
                throw new MessageImportException("Failed to import message, channel ID " + channelId + " is not currently deployed");
            } else {
                channel.importMessage(message);
            }
        } catch (DonkeyException e) {
            throw new MessageImportException(e);
        }
    }

    @Override
    public MessageImportResult importMessagesServer(final String channelId, String path, boolean includeSubfolders) throws MessageImportException, InterruptedException, MessageImportInvalidPathException {
        Channel channel = donkey.getDeployedChannels().get(channelId);

        if (channel == null) {
            throw new MessageImportException("Failed to import message, channel ID " + channelId + " is not currently deployed");
        }

        MessageWriter messageWriter = new MessageWriterChannel(channel);
        return new MessageImporter().importMessages(path, includeSubfolders, messageWriter, System.getProperty("user.dir"));
    }

    private List<MessageSearchResult> searchMessages(MessageFilter filter, String channelId, int offset, int limit) {
        long startTime = System.currentTimeMillis();

        FilterOptions filterOptions = new FilterOptions(filter, channelId, true);
        long maxMessageId = filterOptions.getMaxMessageId();
        long minMessageId = filterOptions.getMinMessageId();

        Long localChannelId = ChannelController.getInstance().getLocalChannelId(channelId, true);
        Map<String, Object> params = getBasicParameters(filter, localChannelId);

        try {
            NavigableMap<Long, MessageSearchResult> messages = new TreeMap<Long, MessageSearchResult>();
            SqlSession session = getSqlSessionManager(true);

            int offsetRemaining = offset;
            /*
             * If the limit is greater than the default batch size, use the limit, but cap it at
             * 50000.
             */
            long batchSize = Math.min(Math.max(limit, 500), 50000);
            long totalSearched = 0;

            while (messages.size() < limit && maxMessageId >= minMessageId) {
                /*
                 * Slowly increase the batch size in case all the necessary results are found early
                 * on.
                 */
                if (totalSearched >= 100000 && batchSize < 50000) {
                    batchSize = 50000;
                } else if (totalSearched >= 10000 && batchSize < 10000) {
                    batchSize = 10000;
                } else if (totalSearched >= 1000 && batchSize < 1000) {
                    batchSize = 1000;
                }

                /*
                 * Search in descending order so that messages will be found from the greatest to
                 * lowest message id
                 */
                long currentMinMessageId = Math.max(maxMessageId - batchSize + 1, minMessageId);
                params.put("maxMessageId", maxMessageId);
                params.put("minMessageId", currentMinMessageId);
                maxMessageId -= batchSize;
                totalSearched += batchSize;

                Map<Long, MessageSearchResult> foundMessages = searchAll(session, params, filter, localChannelId, false, filterOptions);

                if (!foundMessages.isEmpty()) {
                    /*
                     * Skip results until there is no offset remaining. This is required when
                     * viewing results beyond the first page
                     */
                    if (offsetRemaining >= foundMessages.size()) {
                        offsetRemaining -= foundMessages.size();
                    } else if (offsetRemaining == 0) {
                        messages.putAll(foundMessages);
                    } else {
                        NavigableMap<Long, MessageSearchResult> orderedMessages = new TreeMap<Long, MessageSearchResult>(foundMessages);

                        while (offsetRemaining-- > 0) {
                            orderedMessages.pollLastEntry();
                        }

                        messages.putAll(orderedMessages);
                    }
                }
            }

            // Remove results beyond the limit requested
            while (messages.size() > limit) {
                messages.pollFirstEntry();
            }

            List<MessageSearchResult> results = new ArrayList<MessageSearchResult>(messages.size());

            /*
             * Now that we have the message and metadata ids that should be returned as the result,
             * we need to retrieve the message data for those.
             */
            if (!messages.isEmpty()) {
                Iterator<Long> iterator = messages.descendingKeySet().iterator();

                while (iterator.hasNext()) {
                    Map<String, Object> messageParams = new HashMap<String, Object>();
                    messageParams.put("localChannelId", localChannelId);

                    ListRangeIterator listRangeIterator = new ListRangeIterator(iterator, ListRangeIterator.DEFAULT_LIST_LIMIT, false, null);

                    while (listRangeIterator.hasNext()) {
                        ListRangeItem item = listRangeIterator.next();
                        List<Long> list = item.getList();
                        Long startRange = item.getStartRange();
                        Long endRange = item.getEndRange();

                        if (list != null || (startRange != null && endRange != null)) {
                            if (list != null) {
                                messageParams.remove("minMessageId");
                                messageParams.remove("maxMessageId");
                                messageParams.put("includeMessageList", StringUtils.join(list, ","));
                            } else {
                                messageParams.remove("includeMessageList");
                                messageParams.put("minMessageId", endRange);
                                messageParams.put("maxMessageId", startRange);
                            }

                            // Get the current batch of results
                            List<MessageSearchResult> currentResults = session.selectList("Message.selectMessagesById", messageParams);

                            // Add the metadata ids to each result
                            for (MessageSearchResult currentResult : currentResults) {
                                currentResult.setMetaDataIdSet(messages.get(currentResult.getMessageId()).getMetaDataIdSet());
                            }

                            // Add the current batch to the final list of results
                            results.addAll(currentResults);
                        }
                    }
                }
            }

            return results;
        } finally {
            long endTime = System.currentTimeMillis();
            logger.debug("Search executed in " + (endTime - startTime) + "ms");
        }
    }

    private Map<Long, MessageSearchResult> searchAll(SqlSession session, Map<String, Object> params, MessageFilter filter, Long localChannelId, boolean includeMessageData, FilterOptions filterOptions) {
        Map<Long, MessageSearchResult> foundMessages = new HashMap<Long, MessageSearchResult>();

        // Search the message table to find which message ids meet the search criteria.
        List<MessageTextResult> messageResults = session.selectList("Message.searchMessageTable", params);
        /*
         * If the message table search provided no records then there is no need to perform any more
         * searches on this range of message ids.
         */
        if (!messageResults.isEmpty()) {
            Set<Long> messageIdSet = new HashSet<Long>(messageResults.size());
            for (MessageTextResult messageResult : messageResults) {
                messageIdSet.add(messageResult.getMessageId());
            }

            /*
             * Search the metadata table to find which message and metadataids meet the search
             * criteria. If a text search is being performed, we also check the connector name
             * column while we're at it.
             */

            List<MessageTextResult> metaDataResults = session.selectList("Message.searchMetaDataTable", params);
            /*
             * Messages that matched the text search criteria. Since text search spans across
             * multiple tables (metadata, content, custom metadata), the map is created it can be
             * used for the searches on each table.
             */
            Map<Long, MessageSearchResult> textMessages = new HashMap<Long, MessageSearchResult>();
            /*
             * Messages that met the criteria on the message and metadata tables and still need to
             * have lengthy search run on them.
             */
            Map<Long, MessageSearchResult> potentialMessages = new HashMap<Long, MessageSearchResult>();
            for (MessageTextResult metaDataResult : metaDataResults) {
                if (messageIdSet.contains(metaDataResult.getMessageId())) {
                    if (filterOptions.isSearchText() && metaDataResult.isTextFound() != null && metaDataResult.isTextFound()) {
                        /*
                         * Text search was found in the metadata table so add the message/metadata
                         * id to the text messages.
                         */
                        addMessageToMap(textMessages, metaDataResult.getMessageId(), metaDataResult.getMetaDataId());

                        if (filterOptions.isSearchCustomMetaData() || filterOptions.isSearchContent()) {
                            /*
                             * If content or custom metadata is being searched, still add the
                             * message to potentialMessages so the lengthy search will be run.
                             */
                            addMessageToMap(potentialMessages, metaDataResult.getMessageId(), metaDataResult.getMetaDataId());
                        }
                    } else if (filterOptions.isSearchCustomMetaData() || filterOptions.isSearchContent() || filterOptions.isSearchText()) {
                        /*
                         * If no text search was found and any lengthy search is required, add the
                         * message to potentialMessages.
                         */
                        addMessageToMap(potentialMessages, metaDataResult.getMessageId(), metaDataResult.getMetaDataId());
                    } else {
                        /*
                         * If no lengthy search is required, just add the message to foundMesages.
                         */
                        addMessageToMap(foundMessages, metaDataResult.getMessageId(), metaDataResult.getMetaDataId());
                    }
                }
            }

            // These are no longer used so allow GC to reclaim their memory
            metaDataResults = null;
            messageIdSet = null;
            if (!includeMessageData) {
                messageResults = null;
            }

            if (potentialMessages.isEmpty()) {
                // If lengthy search is not being run, add all text messages to found messages
                foundMessages.putAll(textMessages);
            } else {
                long potentialMin = Long.MAX_VALUE;
                long potentialMax = Long.MIN_VALUE;

                for (long key : potentialMessages.keySet()) {
                    if (key < potentialMin) {
                        potentialMin = key;
                    }
                    if (key > potentialMax) {
                        potentialMax = key;
                    }
                }

                Map<String, Object> contentParams = new HashMap<String, Object>();
                contentParams.put("localChannelId", localChannelId);
                contentParams.put("includedMetaDataIds", filter.getIncludedMetaDataIds());
                contentParams.put("excludedMetaDataIds", filter.getExcludedMetaDataIds());
                contentParams.put("minMessageId", potentialMin);
                contentParams.put("maxMessageId", potentialMax);

                boolean searchCustomMetaData = filterOptions.isSearchCustomMetaData();
                boolean searchContent = filterOptions.isSearchContent();
                boolean searchText = filterOptions.isSearchText();

                /*
                 * The map of messages that contains the combined results from all of the lengthy
                 * searches. For all searches that are being performed, a message and metadata id
                 * must be found in all three in order for the message to remain in this map
                 */
                Map<Long, MessageSearchResult> tempMessages = null;

                if (searchCustomMetaData) {
                    tempMessages = new HashMap<Long, MessageSearchResult>();
                    // Perform the custom metadata search
                    searchCustomMetaData(session, new HashMap<String, Object>(contentParams), potentialMessages, tempMessages, filter.getMetaDataSearch());

                    /*
                     * If tempMessages is empty, there is no need to search on either the content or
                     * text because the join will never return any results
                     */
                    if (tempMessages.isEmpty()) {
                        searchContent = false;
                        searchText = false;
                    }
                }
                if (searchContent) {
                    Map<Long, MessageSearchResult> contentMessages = new HashMap<Long, MessageSearchResult>();
                    // Perform the content search
                    searchContent(session, new HashMap<String, Object>(contentParams), potentialMessages, contentMessages, filter.getContentSearch());

                    if (tempMessages == null) {
                        /*
                         * If temp messages has not been created yet, then there is no need to join
                         * the results from this search and previous searches. Just set the current
                         * messages as the temp messages
                         */
                        tempMessages = contentMessages;
                    } else {
                        /*
                         * Otherwise join the two maps so that the only results left in tempMessages
                         * are those that also exist in the current message map
                         */
                        joinMessages(tempMessages, contentMessages);
                    }

                    /*
                     * If tempMessages is empty, there is no need to search on either the text
                     * because the join will never return any results
                     */
                    if (tempMessages.isEmpty()) {
                        searchText = false;
                    }
                }
                if (searchText) {
                    // Perform the text search
                    searchText(session, new HashMap<String, Object>(contentParams), potentialMessages, textMessages, filter.getTextSearchRegex(), filter.getTextSearch(), filter.getTextSearchMetaDataColumns());

                    if (tempMessages == null) {
                        /*
                         * If temp messages has not been created yet, then there is no need to join
                         * the results from this search and previous searches. Just set the current
                         * messages as the temp messages
                         */
                        tempMessages = textMessages;
                    } else {
                        /*
                         * Otherwise join the two maps so that the only results left in tempMessages
                         * are those that also exist in the current message map
                         */
                        joinMessages(tempMessages, textMessages);
                    }
                }

                /*
                 * Add all the results from tempMessages after all the joins have been completed
                 * into foundMessages
                 */
                foundMessages.putAll(tempMessages);
            }

            /*
             * If message data was requested then copy it from the message search into the final
             * results
             */
            if (!foundMessages.isEmpty() && includeMessageData) {
                // Build a map of the message data results for quicker access
                Map<Long, MessageTextResult> messageDataResults = new HashMap<Long, MessageTextResult>(messageResults.size());
                for (MessageTextResult messageResult : messageResults) {
                    messageDataResults.put(messageResult.getMessageId(), messageResult);
                }

                /*
                 * For each found result, copy over any message data that may have been retrieved
                 * already
                 */
                for (Entry<Long, MessageSearchResult> entry : foundMessages.entrySet()) {
                    Long messageId = entry.getKey();
                    MessageSearchResult result = entry.getValue();

                    MessageTextResult textResult = messageDataResults.get(messageId);
                    if (textResult != null) {
                        result.setImportId(textResult.getImportId());
                        result.setProcessed(textResult.getProcessed());
                    }
                }
            }
        }

        return foundMessages;
    }

    private void searchCustomMetaData(SqlSession session, Map<String, Object> params, Map<Long, MessageSearchResult> potentialMessages, Map<Long, MessageSearchResult> customMetaDataMessages, List<MetaDataSearchElement> metaDataSearchElements) {
        params.put("metaDataSearch", metaDataSearchElements);

        /*
         * Search the custom meta data table for message and metadata ids matching the metadata
         * search criteria
         */
        List<MessageTextResult> results = session.selectList("Message.searchCustomMetaDataTable", params);

        for (MessageTextResult result : results) {
            Long messageId = result.getMessageId();
            Integer metaDataId = result.getMetaDataId();

            if (potentialMessages.containsKey(messageId)) {
                Set<Integer> allowedMetaDataIds = potentialMessages.get(messageId).getMetaDataIdSet();
                /*
                 * Ignore the message and metadata id if they are not allowed because they were
                 * already filtered in a previous step
                 */
                if (allowedMetaDataIds.contains(metaDataId)) {
                    addMessageToMap(customMetaDataMessages, messageId, metaDataId);
                }
            }
        }
    }

    private void searchContent(SqlSession session, Map<String, Object> params, Map<Long, MessageSearchResult> potentialMessages, Map<Long, MessageSearchResult> contentMessages, List<ContentSearchElement> contentSearchElements) {
        int index = 0;

        while (index < contentSearchElements.size() && (index == 0 || !contentMessages.isEmpty())) {
            ContentSearchElement element = contentSearchElements.get(index);

            if (CollectionUtils.isNotEmpty(element.getSearches())) {
                params.put("contentType", element.getContentCode());
                params.put("contents", element.getSearches());

                /*
                 * Search the content table for message and metadata ids matching the content search
                 * criteria
                 */
                List<MessageTextResult> results = session.selectList("Message.searchContentTable", params);

                Map<Long, MessageSearchResult> tempMessages = new HashMap<Long, MessageSearchResult>();

                for (MessageTextResult result : results) {
                    Long messageId = result.getMessageId();
                    Integer metaDataId = result.getMetaDataId();

                    if (potentialMessages.containsKey(messageId)) {
                        Set<Integer> allowedMetaDataIds = potentialMessages.get(messageId).getMetaDataIdSet();
                        /*
                         * Ignore the message and metadata id if they are not allowed because they
                         * were already filtered in a previous step
                         */
                        if (allowedMetaDataIds.contains(metaDataId)) {
                            if (index == 0) {
                                /*
                                 * For the first search, add the results to the final result map
                                 * since there is nothing to join from
                                 */
                                addMessageToMap(contentMessages, messageId, metaDataId);
                            } else {
                                /*
                                 * For other searches, add the results to the temp result map so
                                 * they can be joined with the final result map
                                 */
                                addMessageToMap(tempMessages, messageId, metaDataId);
                            }
                        }
                    }
                }

                /*
                 * If the raw content is being searched, perform an additional search on the source
                 * encoded content since the destination
                 */
                if (ContentType.fromCode(element.getContentCode()) == ContentType.RAW) {
                    params.put("metaDataId", 0);
                    params.put("contentType", ContentType.ENCODED.getContentTypeCode());

                    results = session.selectList("Message.searchContentTable", params);
                    params.remove("metaDataId");

                    for (MessageTextResult result : results) {
                        Long messageId = result.getMessageId();

                        if (potentialMessages.containsKey(messageId)) {
                            Set<Integer> allowedMetaDataIds = potentialMessages.get(messageId).getMetaDataIdSet();
                            for (Integer allowedMetaDataId : allowedMetaDataIds) {
                                if (allowedMetaDataId != 0) {
                                    /*
                                     * If the source encoded is found, then all destinations have
                                     * matched on the raw content, so all allowed metadata ids other
                                     * than 0 (source) need to be added
                                     */
                                    if (index == 0) {
                                        /*
                                         * For the first search, add the results to the final result
                                         * map since there is nothing to join from
                                         */
                                        addMessageToMap(contentMessages, messageId, allowedMetaDataId);
                                    } else {
                                        /*
                                         * For other searches, add the results to the temp result
                                         * map so they can be joined with the final result map
                                         */
                                        addMessageToMap(tempMessages, messageId, allowedMetaDataId);
                                    }
                                }
                            }
                        }
                    }
                }

                if (index > 0) {
                    /*
                     * If there are more than one searches performed, join the results since the
                     * message and metadata ids must be found in all searches in order to be
                     * considered "found"
                     */
                    joinMessages(contentMessages, tempMessages);
                }
            }

            index++;
        }
    }

    private void searchText(SqlSession session, Map<String, Object> params, Map<Long, MessageSearchResult> potentialMessages, Map<Long, MessageSearchResult> textMessages, Boolean textSearchRegex, String text, List<String> textSearchMetaDataColumns) {
        params.put("contents", Collections.singletonList(text));
        params.put("textSearch", text);
        params.put("textSearchRegex", textSearchRegex);
        params.put("textSearchMetaDataColumns", textSearchMetaDataColumns);
        List<MessageTextResult> results;

        if (CollectionUtils.isNotEmpty(textSearchMetaDataColumns)) {
            /*
             * Search the custom meta data table for message and metadata ids matching the text
             * search criteria
             */
            results = session.selectList("Message.searchCustomMetaDataTable", params);

            for (MessageTextResult result : results) {
                Long messageId = result.getMessageId();
                Integer metaDataId = result.getMetaDataId();

                if (potentialMessages.containsKey(messageId)) {
                    Set<Integer> allowedMetaDataIds = potentialMessages.get(messageId).getMetaDataIdSet();
                    /*
                     * Ignore the message and metadata id if they are not allowed because they were
                     * already filtered in a previous step
                     */
                    if (allowedMetaDataIds.contains(metaDataId)) {
                        addMessageToMap(textMessages, messageId, metaDataId);
                    }
                }
            }
        }

        /*
         * Search the content table for message and metadata ids matching the text search criteria
         */
        results = session.selectList("Message.searchContentTable", params);

        for (MessageTextResult result : results) {
            Long messageId = result.getMessageId();
            Integer metaDataId = result.getMetaDataId();
            Integer contentCode = result.getContentType();
            ContentType contentType = ContentType.fromCode(contentCode);

            if (potentialMessages.containsKey(messageId)) {
                Set<Integer> allowedMetaDataIds = potentialMessages.get(messageId).getMetaDataIdSet();
                if (metaDataId == 0 && contentType == ContentType.ENCODED) {
                    /*
                     * If the text search is found in the source encoded content, then all the
                     * allowed destinations would match on the raw content so all allowed metadata
                     * ids for this message need to be added
                     */
                    for (Integer allowedMetaDataId : allowedMetaDataIds) {
                        addMessageToMap(textMessages, messageId, allowedMetaDataId);
                    }
                } else if (allowedMetaDataIds.contains(metaDataId)) {
                    /*
                     * Ignore the message and metadata id if they are not allowed because they were
                     * already filtered in a previous step
                     */
                    addMessageToMap(textMessages, messageId, metaDataId);
                }
            }
        }
    }

    private void addMessageToMap(Map<Long, MessageSearchResult> messages, Long messageId, Integer metaDataId) {
        MessageSearchResult result = messages.get(messageId);

        if (result == null) {
            result = new MessageSearchResult();
            result.setMetaDataIdSet(new TreeSet<Integer>());
            messages.put(messageId, result);
        }

        result.getMetaDataIdSet().add(metaDataId);
    }

    /**
     * Removes all message and metadata ids from messages that do not exist in newMessages
     */
    private void joinMessages(Map<Long, MessageSearchResult> messages, Map<Long, MessageSearchResult> newMessages) {
        Iterator<Entry<Long, MessageSearchResult>> iterator = messages.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Long, MessageSearchResult> entry = iterator.next();
            Long tempMessageId = entry.getKey();

            if (!newMessages.containsKey(tempMessageId)) {
                iterator.remove();
            } else {
                Set<Integer> firstMetaDataIds = entry.getValue().getMetaDataIdSet();
                firstMetaDataIds.retainAll(newMessages.get(tempMessageId).getMetaDataIdSet());
            }
        }
    }

    private class MessageWriterChannel implements MessageWriter {
        private Channel channel;
        private DonkeyDao dao;
        private Encryptor encryptor = ConfigurationController.getInstance().getEncryptor();

        public MessageWriterChannel(Channel channel) {
            this.channel = channel;
            this.dao = channel.getDaoFactory().getDao();
        }

        @Override
        public boolean write(Message message) throws MessageWriterException {
            MessageEncryptionUtil.decryptMessage(message, encryptor);

            try {
                channel.importMessage(message, dao);
            } catch (DonkeyException e) {
                throw new MessageWriterException(e);
            }

            return true;
        }

        @Override
        public void finishWrite() {}

        @Override
        public void close() throws MessageWriterException {
            try {
                dao.commit();
            } finally {
                dao.close();
            }
        }
    }

    private class FilterOptions {
        private long minMessageId;
        private long maxMessageId;
        private boolean searchCustomMetaData;
        private boolean searchContent;
        private boolean searchText;

        public FilterOptions(MessageFilter filter, String channelId, boolean readOnly) {
            if (filter.getMinMessageId() != null && filter.getMaxMessageId() != null && filter.getMinMessageId() > filter.getMaxMessageId()) {
                /*
                 * If the min message id is greater than the max, use them directly so they fail at
                 * a later point. If we fix them by getting the actual min and max, we may return
                 * results that we shouldn't.
                 */
                minMessageId = filter.getMinMessageId();
                maxMessageId = filter.getMaxMessageId();
            } else {
                /*
                 * If the min is less than the actual min, set the min message id to the actual min
                 * to prevent unnecessary searches
                 */
                minMessageId = Math.max(filter.getMinMessageId() == null ? 1L : filter.getMinMessageId(), DonkeyMessageController.this.getMinMessageId(channelId, readOnly));
                /*
                 * If the max is greater than the actual max, set the max message id to the actual
                 * max to prevent unnecessary searches
                 */
                if (filter.getMaxMessageId() != null) {
                    maxMessageId = Math.min(filter.getMaxMessageId(), DonkeyMessageController.this.getMaxMessageId(channelId, readOnly));
                } else {
                    maxMessageId = DonkeyMessageController.this.getMaxMessageId(channelId, readOnly);
                }
            }

            searchCustomMetaData = CollectionUtils.isNotEmpty(filter.getMetaDataSearch());
            searchContent = CollectionUtils.isNotEmpty(filter.getContentSearch());
            searchText = filter.getTextSearch() != null;
        }

        public long getMinMessageId() {
            return minMessageId;
        }

        public long getMaxMessageId() {
            return maxMessageId;
        }

        public boolean isSearchCustomMetaData() {
            return searchCustomMetaData;
        }

        public boolean isSearchContent() {
            return searchContent;
        }

        public boolean isSearchText() {
            return searchText;
        }
    }

    private DonkeyDao getDao(boolean readOnly) {
        return (readOnly ? donkey.getReadOnlyDaoFactory() : donkey.getDaoFactory()).getDao();
    }

    private SqlSessionManager getSqlSessionManager(boolean readOnly) {
        return readOnly ? SqlConfig.getInstance().getReadOnlySqlSessionManager() : SqlConfig.getInstance().getSqlSessionManager();
    }
}
