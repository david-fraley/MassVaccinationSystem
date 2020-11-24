/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.jdbc;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mirth.connect.donkey.model.event.ConnectionStatusEventType;
import com.mirth.connect.donkey.model.event.ErrorEventType;
import com.mirth.connect.donkey.model.message.BatchRawMessage;
import com.mirth.connect.donkey.model.message.RawMessage;
import com.mirth.connect.donkey.server.ConnectorTaskException;
import com.mirth.connect.donkey.server.channel.ChannelException;
import com.mirth.connect.donkey.server.channel.DispatchResult;
import com.mirth.connect.donkey.server.channel.PollConnector;
import com.mirth.connect.donkey.server.event.ConnectionStatusEvent;
import com.mirth.connect.donkey.server.event.ErrorEvent;
import com.mirth.connect.donkey.server.message.batch.BatchMessageReader;
import com.mirth.connect.donkey.server.message.batch.ResponseHandler;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.model.converters.DocumentSerializer;
import com.mirth.connect.server.controllers.ChannelController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.ResourceUtil;
import com.mirth.connect.util.CharsetUtils;

public class DatabaseReceiver extends PollConnector {

    /**
     * @formatter:off
     * 
     * NameStartChar    ::=    ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] |
     *                  [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] |
     *                  [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
     *                  [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] |
     *                  [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     *                  
     * NameChar         ::=    NameStartChar | "-" | "." | [0-9] | #xB7 |
     *                         [#x0300-#x036F] | [#x203F-#x2040] 
     *
     * Name             ::=    NameStartChar (NameChar)* 
     * 
     * @formatter:on
     */
    private static Pattern INVALID_XML_ELEMENT_NAMESTARTCHAR = Pattern.compile("[^:A-Z_a-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD\\x{10000}-\\x{EFFFF}]");
    private static Pattern INVALID_XML_ELEMENT_NAMECHAR = Pattern.compile("[^:A-Z_a-z-\\.0-9\\xB7\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0300-\\u036F\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u203F-\\u2040\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD\\x{10000}-\\x{EFFFF}]+");

    protected DatabaseReceiverProperties connectorProperties;
    private DatabaseReceiverDelegate delegate;
    private EventController eventController = ControllerFactory.getFactory().createEventController();
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void onDeploy() throws ConnectorTaskException {
        connectorProperties = (DatabaseReceiverProperties) getConnectorProperties();

        /*
         * A delegate object is used to handle the polling operations, since the polling logic is
         * very different depending on whether JavaScript is enabled or not
         */
        if (connectorProperties.isUseScript()) {
            delegate = new DatabaseReceiverScript(this);
        } else {
            delegate = new DatabaseReceiverQuery(this);
        }

        delegate.deploy();

        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getSourceName(), ConnectionStatusEventType.IDLE));
    }

    @Override
    public void onUndeploy() throws ConnectorTaskException {
        delegate.undeploy();
    }

    @Override
    public void onStart() throws ConnectorTaskException {
        delegate.start();
    }

    @Override
    public void onStop() throws ConnectorTaskException {
        delegate.stop();
    }

    @Override
    public void onHalt() throws ConnectorTaskException {
        onStop();
    }

    @Override
    public void handleRecoveredResponse(DispatchResult dispatchResult) {
        finishDispatch(dispatchResult);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void poll() throws InterruptedException {
        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getSourceName(), ConnectionStatusEventType.POLLING));
        Object result = null;

        try {
            result = delegate.poll();

            if (isTerminated()) {
                return;
            }

            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getSourceName(), ConnectionStatusEventType.READING));

            // the result object will be a ResultSet or if JavaScript is used, we also allow the user to return a List<Map<String, Object>>
            if (result instanceof ResultSet) {
                processResultSet((ResultSet) result);
            } else if (result instanceof List) {
                // if the result object is a List, then assume it is a list of maps representing a row to process
                processResultList((List<Map<String, Object>>) result);
            } else {
                throw new DatabaseReceiverException("Unrecognized result: " + result.toString());
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to poll for messages from the database in channel \"" + ChannelController.getInstance().getDeployedChannelById(getChannelId()).getName() + "\"", e);
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), null, ErrorEventType.SOURCE_CONNECTOR, getSourceName(), connectorProperties.getName(), null, e.getCause()));
            return;
        } finally {
            if (result instanceof ResultSet) {
                DbUtils.closeQuietly((ResultSet) result);
            }

            try {
                delegate.afterPoll();
            } catch (DatabaseReceiverException e) {
                logger.error("Error in channel \"" + ChannelController.getInstance().getDeployedChannelById(getChannelId()).getName() + "\": " + e.getMessage(), ExceptionUtils.getRootCause(e));
                eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), null, ErrorEventType.SOURCE_CONNECTOR, getSourceName(), connectorProperties.getName(), null, e.getCause()));
            }

            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getSourceName(), ConnectionStatusEventType.IDLE));
        }
    }

    /**
     * For each record in the given ResultSet, convert it to XML and dispatch it as a raw message to
     * the channel. Then run the post-process if applicable.
     */
    @SuppressWarnings("unchecked")
    private void processResultSet(ResultSet resultSet) throws SQLException, InterruptedException, DatabaseReceiverException {
        BasicRowProcessor basicRowProcessor = new BasicRowProcessor();

        try {
            List<Map<String, Object>> resultsList = null;
            if (connectorProperties.isAggregateResults()) {
                resultsList = new ArrayList<Map<String, Object>>();
            }

            // loop through the ResultSet rows and convert them into hash maps for processing
            while (resultSet.next()) {
                if (isTerminated()) {
                    return;
                }

                Map<String, Object> resultMap = (Map<String, Object>) basicRowProcessor.toMap(resultSet);

                if (connectorProperties.isAggregateResults()) {
                    resultsList.add(resultMap);
                } else {
                    processRecord(resultMap);
                }
            }

            if (connectorProperties.isAggregateResults() && CollectionUtils.isNotEmpty(resultsList)) {
                if (isTerminated()) {
                    return;
                }
                processAggregateRecord(resultsList);
            }
        } catch (Exception e) {
            if (e instanceof DatabaseReceiverException) {
                throw (DatabaseReceiverException) e;
            }
            throw new DatabaseReceiverException(e);
        }
    }

    /**
     * For each record in the given list, convert it to XML and dispatch it as a raw message to the
     * channel. Then run the post-process if applicable.
     */
    private void processResultList(List<Map<String, Object>> resultList) throws InterruptedException, DatabaseReceiverException {
        for (Object object : resultList) {
            if (isTerminated()) {
                return;
            }

            if (object instanceof Map) {
                Map<String, Object> caseInsensitiveMap = new BasicRowProcessor.CaseInsensitiveHashMap();
                caseInsensitiveMap.putAll((Map<String, Object>) object);
                processRecord(caseInsensitiveMap);
            } else {
                String errorMessage = "Received invalid list entry in channel \"" + ChannelController.getInstance().getDeployedChannelById(getChannelId()).getName() + "\", expected Map<String, Object>: " + object.toString();
                logger.error(errorMessage);
                eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), null, ErrorEventType.SOURCE_CONNECTOR, getSourceName(), connectorProperties.getName(), errorMessage, null));
            }
        }
    }

    /**
     * Convert the given resultMap into XML and dispatch it as a raw message to the channel. Then
     * run the post-process if applicable.
     */
    private void processRecord(Map<String, Object> resultMap) throws InterruptedException, DatabaseReceiverException {
        try {
            if (isProcessBatch()) {
                BatchRawMessage batchRawMessage = new BatchRawMessage(new BatchMessageReader(resultMapToXml(resultMap)));

                dispatchBatchMessage(batchRawMessage, new DatabaseResponseHandler(resultMap));
            } else {
                DispatchResult dispatchResult = null;

                try {
                    dispatchResult = dispatchRawMessage(new RawMessage(resultMapToXml(resultMap)));
                } finally {
                    finishDispatch(dispatchResult);
                }

                // if the message was persisted (dispatchResult != null), then run the on-update SQL
                if (dispatchResult != null) {
                    if (dispatchResult.getProcessedMessage() != null) {
                        delegate.runPostProcess(resultMap, dispatchResult.getProcessedMessage().getMergedConnectorMessage());
                    } else {
                        delegate.runPostProcess(resultMap, null);
                    }
                }
            }
        } catch (Exception e) {
            String errorMessage = "Failed to process row retrieved from the database in channel \"" + ChannelController.getInstance().getDeployedChannelById(getChannelId()).getName() + "\"";
            logger.error(errorMessage, e);
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), null, ErrorEventType.SOURCE_CONNECTOR, getSourceName(), connectorProperties.getName(), errorMessage, e));
        }
    }

    private void processAggregateRecord(List<Map<String, Object>> resultsList) throws Exception {
        if (isProcessBatch()) {
            BatchRawMessage batchRawMessage = new BatchRawMessage(new BatchMessageReader(resultsListToXml(resultsList)));

            dispatchBatchMessage(batchRawMessage, new AggregateResponseHandler(resultsList));
        } else {
            DispatchResult dispatchResult = null;

            try {
                dispatchResult = dispatchRawMessage(new RawMessage(resultsListToXml(resultsList)));
            } finally {
                finishDispatch(dispatchResult);
            }

            runAggregatePostProcess(dispatchResult, resultsList);
        }
    }

    private void runAggregatePostProcess(DispatchResult dispatchResult, List<Map<String, Object>> resultsList) throws Exception {
        // If the message was persisted (dispatchResult != null), then run the post-process SQL
        if (dispatchResult != null) {
            if (connectorProperties.getUpdateMode() == DatabaseReceiverProperties.UPDATE_ONCE) {
                // Run the post-process SQL once, passing in all results
                if (dispatchResult.getProcessedMessage() != null) {
                    delegate.runAggregatePostProcess(resultsList, dispatchResult.getProcessedMessage().getMergedConnectorMessage());
                } else {
                    delegate.runAggregatePostProcess(resultsList, null);
                }
            } else if (connectorProperties.getUpdateMode() == DatabaseReceiverProperties.UPDATE_EACH) {
                // Iterate through each row and run the post-process SQL
                for (Map<String, Object> resultMap : resultsList) {
                    if (dispatchResult.getProcessedMessage() != null) {
                        delegate.runPostProcess(resultMap, dispatchResult.getProcessedMessage().getMergedConnectorMessage());
                    } else {
                        delegate.runPostProcess(resultMap, null);
                    }
                }
            }
        }
    }

    private String resultsListToXml(List<Map<String, Object>> resultsList) throws Exception {
        DonkeyElement results = new DonkeyElement("<results/>");
        for (Map<String, Object> resultMap : resultsList) {
            results.addChildElementFromXml(resultMapToXml(resultMap));
        }
        return results.toXml();
    }

    /**
     * Convert a resultMap representing a message into XML.
     */
    String resultMapToXml(Map<String, Object> resultMap) throws Exception {
        /*
         * Fixing column names can take a few milliseconds per column, so we should try to do
         * without and only fix if a DOMException was thrown when creating the XML.
         */
        try {
            return doResultMapToXml(resultMap, false);
        } catch (DOMException e) {
            return doResultMapToXml(resultMap, true);
        }
    }

    private String doResultMapToXml(Map<String, Object> resultMap, boolean fixColumnNames) throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("result");
        document.appendChild(root);

        for (Entry<String, Object> entry : resultMap.entrySet()) {
            String value = objectToString(entry.getValue());

            if (value != null) {
                String key = entry.getKey();
                if (fixColumnNames) {
                    key = fixColumnName(key);
                }

                Element child = document.createElement(key);
                child.appendChild(document.createTextNode(value));
                root.appendChild(child);
            }
        }

        return new DocumentSerializer().toXML(document);
    }

    String fixColumnName(String columnName) {
        if (StringUtils.isNotBlank(columnName)) {
            Matcher matcher = INVALID_XML_ELEMENT_NAMESTARTCHAR.matcher(Character.toString(columnName.charAt(0)));
            if (matcher.find()) {
                columnName = "_" + columnName.substring(1);
            }

            matcher = INVALID_XML_ELEMENT_NAMECHAR.matcher(columnName);
            if (matcher.find()) {
                columnName = matcher.replaceAll("_");
            }
        } else {
            columnName = "_";
        }

        return columnName;
    }

    /**
     * Convert an object into a string for insertion in the XML
     */
    private String objectToString(Object object) throws Exception {
        if (object == null) {
            return null;
        }

        String charsetEncoding = CharsetUtils.getEncoding(connectorProperties.getEncoding(), System.getProperty("ca.uhn.hl7v2.llp.charset"));

        if (object instanceof byte[]) {
            return new String((byte[]) object, charsetEncoding);
        }

        if (object instanceof Clob) {
            return clobToString((Clob) object);
        }

        if (object instanceof Blob) {
            Blob blob = (Blob) object;
            return new String(blob.getBytes(1, (int) blob.length()), charsetEncoding);
        }

        return object.toString();
    }

    private String clobToString(Clob clob) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Reader reader = null;
        BufferedReader bufferedReader = null;

        try {
            reader = clob.getCharacterStream();
            bufferedReader = new BufferedReader(reader);
            int c;
            while ((c = bufferedReader.read()) != -1) {
                stringBuilder.append((char) c);
            }

            return stringBuilder.toString();
        } finally {
            ResourceUtil.closeResourceQuietly(bufferedReader);
            ResourceUtil.closeResourceQuietly(reader);
        }
    }

    public class DatabaseResponseHandler extends ResponseHandler {

        private Map<String, Object> resultMap;

        public DatabaseResponseHandler(Map<String, Object> resultMap) {
            this.resultMap = resultMap;
        }

        @Override
        public void responseProcess(int batchSequenceId, boolean batchComplete) throws Exception {
            if (dispatchResult.getProcessedMessage() != null) {
                delegate.runPostProcess(resultMap, dispatchResult.getProcessedMessage().getMergedConnectorMessage());
            } else {
                delegate.runPostProcess(resultMap, null);
            }
        }

        @Override
        public void responseError(ChannelException e) {}
    }

    public class AggregateResponseHandler extends ResponseHandler {

        private List<Map<String, Object>> resultsList;

        public AggregateResponseHandler(List<Map<String, Object>> resultsList) {
            this.resultsList = resultsList;
        }

        @Override
        public void responseProcess(int batchSequenceId, boolean batchComplete) throws Exception {
            if ((isUseFirstResponse() && batchSequenceId == 1) || (!isUseFirstResponse() && batchComplete)) {
                runAggregatePostProcess(dispatchResult, resultsList);
            }
        }

        @Override
        public void responseError(ChannelException e) {}
    }
}
