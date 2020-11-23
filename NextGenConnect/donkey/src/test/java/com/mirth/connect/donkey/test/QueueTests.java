/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.donkey.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mirth.connect.donkey.model.channel.DeployedState;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Message;
import com.mirth.connect.donkey.model.message.RawMessage;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.server.Donkey;
import com.mirth.connect.donkey.server.StartException;
import com.mirth.connect.donkey.server.channel.ChannelException;
import com.mirth.connect.donkey.server.channel.DestinationChainProvider;
import com.mirth.connect.donkey.server.channel.DispatchResult;
import com.mirth.connect.donkey.server.controllers.ChannelController;
import com.mirth.connect.donkey.server.data.DonkeyDaoFactory;
import com.mirth.connect.donkey.server.queue.ConnectorMessageQueue;
import com.mirth.connect.donkey.server.queue.ConnectorMessageQueueDataSource;
import com.mirth.connect.donkey.server.queue.SourceQueue;
import com.mirth.connect.donkey.test.util.TestChannel;
import com.mirth.connect.donkey.test.util.TestDispatcher;
import com.mirth.connect.donkey.test.util.TestDispatcherProperties;
import com.mirth.connect.donkey.test.util.TestPostProcessor;
import com.mirth.connect.donkey.test.util.TestPreProcessor;
import com.mirth.connect.donkey.test.util.TestSourceConnector;
import com.mirth.connect.donkey.test.util.TestUtils;

public class QueueTests {
    private static int TEST_SIZE = 10;
    private static String channelId = TestUtils.DEFAULT_CHANNEL_ID;
    private static String channelName = TestUtils.DEFAULT_CHANNEL_ID;
    private static String serverId = TestUtils.DEFAULT_SERVER_ID;
    private static String testMessage = TestUtils.TEST_HL7_MESSAGE;
    private static DonkeyDaoFactory daoFactory;

    @BeforeClass
    final public static void beforeClass() throws StartException {
        Donkey.getInstance().startEngine(TestUtils.getDonkeyTestConfiguration());
        daoFactory = TestUtils.getDaoFactory();
    }

    @AfterClass
    final public static void afterClass() throws StartException {
        Donkey.getInstance().stopEngine();
    }

    /*
     * Create a new DatabaseLinkedBlockingQueue Set the select and count statements, fill the queue
     * buffer, and assert that: - The buffer capacity was set correctly - The buffer size is correct
     * 
     * Send messages, then assert that: - All messages were successfully put in the queue - The
     * buffer size is still correct
     * 
     * Flush the queue, then assert that: - The queue size shrank correctly - The buffer size shrank
     * correctly
     */
    @Test
    public final void testDatabaseLinkedBlockingQueue() throws SQLException {
        int testSize = TEST_SIZE;
        int bufferCapacity = 10;

        TestUtils.initChannel(channelId);

        SourceQueue queue = new SourceQueue();
        queue.setBufferCapacity(bufferCapacity);
        queue.setDataSource(new ConnectorMessageQueueDataSource(channelId, serverId, 0, Status.RECEIVED, false, daoFactory));
        queue.updateSize();
        queue.fillBuffer();

        assertEquals(bufferCapacity, queue.getBufferCapacity());
        assertEquals(Math.min(bufferCapacity, queue.size()), queue.getBufferSize());

        int initialSize = queue.size();
        int initialBufferSize = queue.getBufferSize();

        for (int i = 0; i < testSize; i++) {
            ConnectorMessage connectorMessage = TestUtils.createAndStoreNewMessage(new RawMessage(testMessage), channelId, channelName, serverId, daoFactory).getConnectorMessages().get(0);
            queue.add(connectorMessage);
        }

        assertEquals(initialSize + testSize, queue.size());
        assertEquals(Math.min(bufferCapacity, queue.size()), queue.getBufferSize());

        for (int i = 0; i < testSize; i++) {
            try {
                queue.poll(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(initialSize, queue.size());
        assertEquals(Math.max(0, initialBufferSize - testSize), queue.getBufferSize());
    }

    /*
     * Start up a new channel, and assert that: - The channel's queue thread is currently running
     * Then stop the channel and assert that: - The channel's queue thread has stopped running
     * 
     * While the channel is stopped, send messages, assert that: - The queue size is equal to the
     * test size Start the channel back up, wait a while, assert that: - The queue size is zero
     * 
     * While the channel is still running, send messages, assert that: - The queue size is greater
     * than zero Wait a while, then assert that: - The queue size is zero
     */
    @Test
    public final void testSourceQueue() throws Exception {
        TestChannel channel = (TestChannel) TestUtils.createDefaultChannel(channelId, serverId);
        channel.getSourceConnector().setRespondAfterProcessing(false);

        // Start up a default channel
        channel.deploy();
        channel.start(null);

        // give the queue thread time to start
        Thread.sleep(3000);

        // Assert that the source queue thread is running
        assertTrue(channel.isQueueThreadRunning());

        // Stop the channel
        channel.stop();

        // Assert that the source queue thread is not longer running
        assertFalse(channel.isQueueThreadRunning());

        // Place messages in the channel's source queue while the channel is stopped
        for (int i = 1; i <= TEST_SIZE; i++) {
            ConnectorMessage sourceMessage = TestUtils.createAndStoreNewMessage(new RawMessage(testMessage), channel.getChannelId(), channel.getName(), channel.getServerId(), daoFactory).getConnectorMessages().get(0);
            channel.queue(sourceMessage);
        }

        // Assert that all the messages were successfully placed in the queue
        assertEquals(channel.getSourceQueue().size(), TEST_SIZE);

        // Start up the channel
        channel.start(null);

        Thread.sleep(500 * TEST_SIZE);

        // Assert that the queue has been cleared
        assertEquals(channel.getSourceQueue().size(), 0);

        // Place messages in the channel's source queue while the channel is started
        for (int i = 1; i <= TEST_SIZE; i++) {
            ConnectorMessage sourceMessage = TestUtils.createAndStoreNewMessage(new RawMessage(testMessage), channel.getChannelId(), channel.getName(), channel.getServerId(), daoFactory).getConnectorMessages().get(0);
            channel.queue(sourceMessage);
        }

        // Assert that the queue size is greater than zero (may fail for small test sizes)
        assertTrue(channel.getSourceQueue().size() > 0);

        Thread.sleep(500 * TEST_SIZE);

        // Assert that the queue has cleared
        assertEquals(channel.getSourceQueue().size(), 0);

        channel.stop();
        channel.undeploy();
        ChannelController.getInstance().removeChannel(channelId);
    }

    /*
     * Start up a new channel, queue up more messages in the channel than the size of the source
     * queue buffer
     * 
     * Send a message (not waiting for destinations) that will cause the data to get written to the
     * database, but the queue size is never incremented
     * 
     * Asynchronously queue up another message normally, wait a bit to let the asynchronous message
     * get written to the database and queued
     * 
     * Queue up the message from before that wasn't queued, wait for the queue to clear, then assert
     * that: - The number of messages processed through the channel is less than the initial number
     * of messages sent plus two - The second-to-last message in the messageIds list was processed,
     * but the last one was not
     */
    @Test
    public final void testSourceQueueOrderAsync() throws Exception {
        int testSize = 2;
        final List<Long> messageIds = new ArrayList<Long>();
        final TestChannel channel = (TestChannel) TestUtils.createDefaultChannel(channelId, serverId);
        channel.getSourceConnector().setRespondAfterProcessing(false);

        // Deploy a default channel
        channel.deploy();
        channel.start(null);

        ConnectorMessageQueue sourceQueue = channel.getSourceQueue();
        int initialSize = sourceQueue.getBufferCapacity() * testSize + 1;

        // Queue up messages normally
        System.out.println("Queuing " + initialSize + " messages normally...");
        for (int i = 1; i <= initialSize; i++) {
            DispatchResult dispatchResult = channel.getSourceConnector().dispatchRawMessage(new RawMessage(testMessage, null, null));
            messageIds.add(dispatchResult.getMessageId());
            channel.getSourceConnector().finishDispatch(dispatchResult);
        }

        ConnectorMessage sourceMessage = null;

        /*
         * Send a message (not waiting for destinations) that will cause the data to get written to
         * the database, but the queue size is never incremented
         */
        System.out.println("Saving a message to the database without calling the queue method...");
        RawMessage rawMessage = new RawMessage(testMessage, null, null);
        sourceMessage = TestUtils.createAndStoreNewMessage(rawMessage, channel.getChannelId(), channel.getName(), channel.getServerId(), daoFactory).getConnectorMessages().get(0);
        messageIds.add(sourceMessage.getMessageId());

        // Asynchronously queue up another message normally
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("Queuing another message normally and asynchronously...");
                DispatchResult dispatchResult = null;

                try {
                    dispatchResult = channel.getSourceConnector().dispatchRawMessage(new RawMessage(testMessage, null, null));
                } catch (ChannelException e) {
                    throw new AssertionError(e);
                } finally {
                    channel.getSourceConnector().finishDispatch(dispatchResult);
                }

                messageIds.add(dispatchResult.getMessageId());
            }
        };
        thread.start();

        /*
         * Wait until the queue has cleared to simulate a delay between committing the message to
         * the database and adding it to the channel's queue. Meanwhile the asynchronous message
         * should be queued
         */
        while (sourceQueue.size() > 0 && channel.isQueueThreadRunning() && channel.getCurrentState() == DeployedState.STARTED) {
            System.out.println("Waiting for queue to clear, size: " + sourceQueue.size());
            Thread.sleep(1000);
        }

        /*
         * Queue up the message from before that wasn't queued. Even though this message has the
         * lower message ID, because the asynchronous message was queued before this one (increasing
         * the queue size), the queue buffer will have been filled with THIS message (not the
         * asynchronous one). Therefore, this message should have already been processed through the
         * channel. Queuing the same message again should cause a foreign key constraint violation.
         */
        System.out.println("Calling the queue method for the previous message that wasn't queued...");
        channel.queue(sourceMessage);

        // Wait until the queue has cleared
        while (sourceQueue.size() > 0 && channel.isQueueThreadRunning() && channel.getCurrentState() == DeployedState.STARTED) {
            System.out.println("Waiting for queue to clear, size: " + sourceQueue.size());
            Thread.sleep(5000);
        }

        // Assert that the number of messages processed through the channel is NOT correct
        assertTrue(channel.getNumMessages() < initialSize + 2);

        // Assert that the second-to-last message in the messageIds list was processed, but the last one was not
        assertTrue(channel.getMessageIds().contains(messageIds.get(messageIds.size() - 2)));
        assertFalse(channel.getMessageIds().contains(messageIds.get(messageIds.size() - 1)));

        channel.stop();
        channel.undeploy();

        ChannelController.getInstance().removeChannel(channel.getChannelId());
    }

    /*
     * Start up a new channel, queue up more messages in the channel than the size of the source
     * queue buffer
     * 
     * Synchronize on the channel's source queue so that commits and queue additions happen in the
     * same thread always
     * 
     * Send a message (not waiting for destinations) that will cause the data to get written to the
     * database, but the queue size is never incremented
     * 
     * Asynchronously queue up another message normally, wait a while to simulate a delay between
     * the commit and queue addition (the asynchronous message should not be queued)
     * 
     * Queue up the message from before that wasn't queued, wait for the queue to clear, then assert
     * that: - The number of messages processed through the channel is equal to the initial number
     * of messages sent plus two - The messageIds list is identical (size and order) to the message
     * ID list generated by the test channel (that is, all messages successfully processed through
     * the channel and in the right order)
     */
    @Test
    public final void testSourceQueueOrderSync() throws Exception {
        int testSize = 2;
        final List<Long> messageIds = new ArrayList<Long>();
        final TestChannel channel = (TestChannel) TestUtils.createDefaultChannel(channelId, serverId);
        channel.getSourceConnector().setRespondAfterProcessing(false);

        // Deploy a default channel
        channel.deploy();
        channel.start(null);

        ConnectorMessageQueue sourceQueue = channel.getSourceQueue();
        int initialSize = sourceQueue.getBufferCapacity() * testSize + 1;

        // Queue up messages normally
        System.out.println("Queuing " + initialSize + " messages normally...");
        for (int i = 1; i <= initialSize; i++) {
            DispatchResult dispatchResult = channel.getSourceConnector().dispatchRawMessage(new RawMessage(testMessage, null, null));
            messageIds.add(dispatchResult.getMessageId());
            channel.getSourceConnector().finishDispatch(dispatchResult);
        }

        ConnectorMessage sourceMessage = null;

        synchronized (channel.getSourceQueue()) {
            /*
             * Send a message (not waiting for destinations) that will cause the data to get written
             * to the database, but the queue size is never incremented
             */
            System.out.println("Saving a message to the database without calling the queue method...");
            RawMessage rawMessage = new RawMessage(testMessage, null, null);
            sourceMessage = TestUtils.createAndStoreNewMessage(rawMessage, channel.getChannelId(), channel.getName(), channel.getServerId(), daoFactory).getConnectorMessages().get(0);
            messageIds.add(sourceMessage.getMessageId());

            // Asynchronously queue up another message normally
            Thread thread = new Thread() {
                @Override
                public void run() {
                    System.out.println("Queuing another message normally and asynchronously...");
                    DispatchResult dispatchResult = null;

                    try {
                        dispatchResult = channel.getSourceConnector().dispatchRawMessage(new RawMessage(testMessage, null, null));
                    } catch (ChannelException e) {
                        throw new AssertionError(e);
                    } finally {
                        channel.getSourceConnector().finishDispatch(dispatchResult);
                    }

                    messageIds.add(dispatchResult.getMessageId());
                }
            };
            thread.start();

            /*
             * Wait a while to simulate a delay between committing the message to the database and
             * adding it to the channel's queue. The thread processing the asynchronous message
             * should be waiting on this block to finish, so it should not add the message to the
             * source queue
             */
            System.out.println("Waiting ten seconds...");
            Thread.sleep(10000);

            // Queue up the message from before that wasn't queued
            System.out.println("Calling the queue method for the previous message that wasn't queued...");
            channel.queue(sourceMessage);
        }

        // Wait until the queue has cleared
        while (sourceQueue.size() > 0 && channel.isQueueThreadRunning() && channel.getCurrentState() == DeployedState.STARTED) {
            System.out.println("Waiting for queue to clear, size: " + sourceQueue.size());
            Thread.sleep(5000);
        }

        // Assert that the number of messages processed through the channel is correct
        assertEquals(initialSize + 2, channel.getNumMessages());
        // Assert that all messages were processed through the channel in the order of database insertion
        assertTrue(messageIds.equals(channel.getMessageIds()));

        channel.stop();
        channel.undeploy();

        ChannelController.getInstance().removeChannel(channel.getChannelId());
    }

    /*
     * Create a new channel with a test dispatcher destination connector The dispatcher initially
     * queues all messages Start up the channel, and assert that: - The destination connector queue
     * thread is running
     * 
     * Send messages, and assert that: - The queue size is equal to the test size
     * 
     * Change the response status that the dispatcher returns to SENT Wait a bit, then assert that:
     * - The queue size is zero
     * 
     * Stop the channel, assert that: - The destination connector queue thread is not running
     * 
     * Place messages directly in the destination connector queue, assert that: - All the messages
     * were successfully put in the queue
     * 
     * Start the channel, wait a bit, then assert that: - The queue size is zero
     */
    @Test
    public final void testDestinationQueue() throws Exception {
        TestUtils.initChannel(channelId);

        TestChannel channel = new TestChannel();

        channel.setChannelId(channelId);
        channel.setServerId(serverId);

        channel.setPreProcessor(new TestPreProcessor());
        channel.setPostProcessor(new TestPostProcessor());

        TestSourceConnector sourceConnector = (TestSourceConnector) TestUtils.createDefaultSourceConnector();
        sourceConnector.setChannelId(channel.getChannelId());
        sourceConnector.setChannel(channel);
        channel.setSourceConnector(sourceConnector);
        channel.getSourceConnector().setFilterTransformerExecutor(TestUtils.createDefaultFilterTransformerExecutor());

        // The TestDispatcher send method initially always returns a response of QUEUED
        TestDispatcher destinationConnector = new TestDispatcher();

        TestDispatcherProperties connectorProperties = new TestDispatcherProperties();
        connectorProperties.setTemplate(testMessage);
        connectorProperties.getDestinationConnectorProperties().setQueueEnabled(true);
        connectorProperties.getDestinationConnectorProperties().setRegenerateTemplate(true);

        TestUtils.initDefaultDestinationConnector(destinationConnector, connectorProperties);
        destinationConnector.setChannelId(channelId);

        destinationConnector.setMetaDataReplacer(sourceConnector.getMetaDataReplacer());
        destinationConnector.setMetaDataColumns(channel.getMetaDataColumns());
        destinationConnector.setFilterTransformerExecutor(TestUtils.createDefaultFilterTransformerExecutor());

        DestinationChainProvider chain = new DestinationChainProvider();
        chain.setChannelId(channelId);
        chain.addDestination(1, destinationConnector);
        channel.addDestinationChainProvider(chain);

        // Start up the channel
        channel.deploy();
        channel.start(null);

        Thread.sleep(1000);

        // Assert that the destination connector queue thread is running
        assertTrue(destinationConnector.isQueueThreadRunning());

        // Send messages while the channel is started
        for (int i = 1; i <= TEST_SIZE; i++) {
            sourceConnector.readTestMessage(testMessage);
        }

        // Since the messages should all get queued, assert that the queue size is equal to the test size
        assertEquals(destinationConnector.getQueue().size(), TEST_SIZE);

        // Tell the dispatcher to now send a response status of SENT
        destinationConnector.setReturnStatus(Status.SENT);

        Thread.sleep(500 * TEST_SIZE);

        // Assert that all the queued messages have been sent
        assertEquals(destinationConnector.getQueue().size(), 0);

        // Stop the channel
        channel.stop();

        // Assert that the destination connector queue thread is not running
        assertFalse(destinationConnector.isQueueThreadRunning());

        // Place messages directly into the destination connector's queue
        for (int i = 1; i <= TEST_SIZE; i++) {
            synchronized (destinationConnector.getQueue()) {
                Message message = TestUtils.createAndStoreNewMessage(new RawMessage(testMessage), channelId, channelName, serverId, daoFactory);
                ConnectorMessage destinationMessage = TestUtils.createAndStoreDestinationConnectorMessage(daoFactory, channelId, channelName, serverId, message.getMessageId(), destinationConnector.getMetaDataId(), testMessage, Status.QUEUED);
                destinationConnector.getQueue().add(destinationMessage);
            }
        }

        // Assert that all the messages were successfully placed in the queue
        assertEquals(TEST_SIZE, destinationConnector.getQueue().size() - 1);

        // Start the channel back up
        channel.start(null);

        Thread.sleep(500 * TEST_SIZE);

        // Since the dispatcher should still always be returning a response status of SENT, the queue should be clear again
        assertEquals(0, destinationConnector.getQueue().size());

        channel.stop();
        channel.undeploy();
        ChannelController.getInstance().removeChannel(channelId);
    }
}
