/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.dimse;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.UserIdentity;
import org.dcm4che2.tool.dcmsnd.CustomDimseRSPHandler;
import org.dcm4che2.tool.dcmsnd.MirthDcmSnd;
import org.dcm4che2.util.StringUtils;

import com.mirth.connect.donkey.model.channel.ConnectorProperties;
import com.mirth.connect.donkey.model.event.ConnectionStatusEventType;
import com.mirth.connect.donkey.model.event.ErrorEventType;
import com.mirth.connect.donkey.model.message.ConnectorMessage;
import com.mirth.connect.donkey.model.message.Response;
import com.mirth.connect.donkey.model.message.Status;
import com.mirth.connect.donkey.server.ConnectorTaskException;
import com.mirth.connect.donkey.server.channel.DestinationConnector;
import com.mirth.connect.donkey.server.event.ConnectionStatusEvent;
import com.mirth.connect.donkey.server.event.ErrorEvent;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.util.ErrorMessageBuilder;

public class DICOMDispatcher extends DestinationConnector {
    private Logger logger = Logger.getLogger(this.getClass());
    private DICOMDispatcherProperties connectorProperties;

    private EventController eventController = ControllerFactory.getFactory().createEventController();
    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();
    private TemplateValueReplacer replacer = new TemplateValueReplacer();
    protected DICOMConfiguration configuration = null;

    @Override
    public void onDeploy() throws ConnectorTaskException {
        this.connectorProperties = (DICOMDispatcherProperties) getConnectorProperties();

        // load the default configuration
        String configurationClass = configurationController.getProperty(connectorProperties.getProtocol(), "dicomConfigurationClass");

        try {
            configuration = (DICOMConfiguration) Class.forName(configurationClass).newInstance();
        } catch (Throwable t) {
            logger.trace("could not find custom configuration class, using default");
            configuration = new DefaultDICOMConfiguration();
        }

        try {
            configuration.configureConnectorDeploy(this);
        } catch (Exception e) {
            throw new ConnectorTaskException(e);
        }
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
        DICOMDispatcherProperties dicomDispatcherProperties = (DICOMDispatcherProperties) connectorProperties;

        dicomDispatcherProperties.setHost(replacer.replaceValues(dicomDispatcherProperties.getHost(), connectorMessage));
        dicomDispatcherProperties.setPort(replacer.replaceValues(dicomDispatcherProperties.getPort(), connectorMessage));

        dicomDispatcherProperties.setLocalHost(replacer.replaceValues(dicomDispatcherProperties.getLocalHost(), connectorMessage));
        dicomDispatcherProperties.setLocalPort(replacer.replaceValues(dicomDispatcherProperties.getLocalPort(), connectorMessage));

        dicomDispatcherProperties.setApplicationEntity(replacer.replaceValues(dicomDispatcherProperties.getApplicationEntity(), connectorMessage));
        dicomDispatcherProperties.setLocalApplicationEntity(replacer.replaceValues(dicomDispatcherProperties.getLocalApplicationEntity(), connectorMessage));

        dicomDispatcherProperties.setUsername(replacer.replaceValues(dicomDispatcherProperties.getUsername(), connectorMessage));
        dicomDispatcherProperties.setPasscode(replacer.replaceValues(dicomDispatcherProperties.getPasscode(), connectorMessage));

        dicomDispatcherProperties.setTemplate(replacer.replaceValues(dicomDispatcherProperties.getTemplate(), connectorMessage));

        dicomDispatcherProperties.setKeyStore(replacer.replaceValues(dicomDispatcherProperties.getKeyStore(), connectorMessage));
        dicomDispatcherProperties.setKeyStorePW(replacer.replaceValues(dicomDispatcherProperties.getKeyStorePW(), connectorMessage));

        dicomDispatcherProperties.setTrustStore(replacer.replaceValues(dicomDispatcherProperties.getTrustStore(), connectorMessage));
        dicomDispatcherProperties.setTrustStorePW(replacer.replaceValues(dicomDispatcherProperties.getTrustStorePW(), connectorMessage));

        dicomDispatcherProperties.setKeyPW(replacer.replaceValues(dicomDispatcherProperties.getKeyPW(), connectorMessage));
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        DICOMDispatcherProperties dicomDispatcherProperties = (DICOMDispatcherProperties) connectorProperties;

        String info = "Host: " + dicomDispatcherProperties.getHost();
        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.WRITING, info));

        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;

        File tempFile = null;
        MirthDcmSnd dcmSnd = getDcmSnd(configuration);

        try {
            tempFile = File.createTempFile("temp", "tmp");

            FileUtils.writeByteArrayToFile(tempFile, getAttachmentHandlerProvider().reAttachMessage(dicomDispatcherProperties.getTemplate(), connectorMessage, null, true, dicomDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments()));

            dcmSnd.setCalledAET("DCMRCV");
            dcmSnd.setRemoteHost(dicomDispatcherProperties.getHost());
            dcmSnd.setRemotePort(NumberUtils.toInt(dicomDispatcherProperties.getPort()));

            if ((dicomDispatcherProperties.getApplicationEntity() != null) && !dicomDispatcherProperties.getApplicationEntity().equals("")) {
                dcmSnd.setCalledAET(dicomDispatcherProperties.getApplicationEntity());
            }

            if ((dicomDispatcherProperties.getLocalApplicationEntity() != null) && !dicomDispatcherProperties.getLocalApplicationEntity().equals("")) {
                dcmSnd.setCalling(dicomDispatcherProperties.getLocalApplicationEntity());
            }

            if ((dicomDispatcherProperties.getLocalHost() != null) && !dicomDispatcherProperties.getLocalHost().equals("")) {
                dcmSnd.setLocalHost(dicomDispatcherProperties.getLocalHost());
                dcmSnd.setLocalPort(NumberUtils.toInt(dicomDispatcherProperties.getLocalPort()));
            }

            dcmSnd.addFile(tempFile);

            //TODO Allow variables
            int value = NumberUtils.toInt(dicomDispatcherProperties.getAcceptTo());
            if (value != 5)
                dcmSnd.setAcceptTimeout(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getAsync());
            if (value > 0)
                dcmSnd.setMaxOpsInvoked(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getBufSize());
            if (value != 1)
                dcmSnd.setTranscoderBufferSize(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getConnectTo());
            if (value > 0)
                dcmSnd.setConnectTimeout(value);
            if (dicomDispatcherProperties.getPriority().equals("med"))
                dcmSnd.setPriority(0);
            else if (dicomDispatcherProperties.getPriority().equals("low"))
                dcmSnd.setPriority(1);
            else if (dicomDispatcherProperties.getPriority().equals("high"))
                dcmSnd.setPriority(2);
            if (dicomDispatcherProperties.getUsername() != null && !dicomDispatcherProperties.getUsername().equals("")) {
                String username = dicomDispatcherProperties.getUsername();
                UserIdentity userId;
                if (dicomDispatcherProperties.getPasscode() != null && !dicomDispatcherProperties.getPasscode().equals("")) {
                    String passcode = dicomDispatcherProperties.getPasscode();
                    userId = new UserIdentity.UsernamePasscode(username, passcode.toCharArray());
                } else {
                    userId = new UserIdentity.Username(username);
                }
                userId.setPositiveResponseRequested(dicomDispatcherProperties.isUidnegrsp());
                dcmSnd.setUserIdentity(userId);
            }
            dcmSnd.setPackPDV(dicomDispatcherProperties.isPdv1());

            value = NumberUtils.toInt(dicomDispatcherProperties.getRcvpdulen());
            if (value != 16)
                dcmSnd.setMaxPDULengthReceive(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getReaper());
            if (value != 10)
                dcmSnd.setAssociationReaperPeriod(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getReleaseTo());
            if (value != 5)
                dcmSnd.setReleaseTimeout(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getRspTo());
            if (value != 60)
                dcmSnd.setDimseRspTimeout(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getShutdownDelay());
            if (value != 1000)
                dcmSnd.setShutdownDelay(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getSndpdulen());
            if (value != 16)
                dcmSnd.setMaxPDULengthSend(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getSoCloseDelay());
            if (value != 50)
                dcmSnd.setSocketCloseDelay(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getSorcvbuf());
            if (value > 0)
                dcmSnd.setReceiveBufferSize(value);

            value = NumberUtils.toInt(dicomDispatcherProperties.getSosndbuf());
            if (value > 0)
                dcmSnd.setSendBufferSize(value);

            dcmSnd.setStorageCommitment(dicomDispatcherProperties.isStgcmt());
            dcmSnd.setTcpNoDelay(!dicomDispatcherProperties.isTcpDelay());

            configuration.configureDcmSnd(dcmSnd, this, dicomDispatcherProperties);

            dcmSnd.setOfferDefaultTransferSyntaxInSeparatePresentationContext(dicomDispatcherProperties.isTs1());
            dcmSnd.configureTransferCapability();
            dcmSnd.start();

            dcmSnd.open();
            CommandDataDimseRSPHandler rspHandler = new CommandDataDimseRSPHandler();
            dcmSnd.send(rspHandler);

            boolean storageCommitmentFailed = false;
            String storageCommitmentFailureReason = "Unknown";
            if (dcmSnd.isStorageCommitment()) {
                if (dcmSnd.commit()) {
                    DicomObject cmtrslt = dcmSnd.waitForStgCmtResult();
                    DicomElement failedSOPSq = cmtrslt.get(Tag.FailedSOPSequence);
                    if (failedSOPSq != null && failedSOPSq.countItems() > 0) {
                        storageCommitmentFailed = true;
                        DicomObject failedSOPItem = failedSOPSq.getDicomObject();
                        int failureReason = failedSOPItem.getInt(Tag.FailureReason);
                        if (failureReason != 0) {
                            storageCommitmentFailureReason = String.valueOf(failureReason);
                        }
                    }
                } else {
                    storageCommitmentFailed = true;
                }
            }

            dcmSnd.close();

            int status = rspHandler.getStatus();

            if (status == 0) {
                responseStatusMessage = "DICOM message successfully sent";
                responseStatus = Status.SENT;
            } else if (status == 0xB000 || status == 0xB006 || status == 0xB007) {
                // These status codes are used in DcmSnd.onDimseRSP to flag warnings
                responseStatusMessage = "DICOM message successfully sent with warning status code: 0x" + StringUtils.shortToHex(status);
                responseStatus = Status.SENT;
            } else {
                // Any other status is considered unsuccessful
                responseStatusMessage = "Error status code received from DICOM server: 0x" + StringUtils.shortToHex(status);
                responseStatus = Status.QUEUED;
            }

            if (storageCommitmentFailed && responseStatus == Status.SENT) {
                responseStatusMessage += " but Storage Commitment failed with reason: " + storageCommitmentFailureReason;
                responseStatus = Status.QUEUED;
            }

            responseData = rspHandler.getCommandData();
        } catch (Exception e) {
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse(e.getMessage(), e);
            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), e.getMessage(), null);
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), e.getMessage(), null));
        } finally {
            dcmSnd.stop();

            if (tempFile != null) {
                tempFile.delete();
            }

            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.IDLE));
        }

        return new Response(responseStatus, responseData, responseStatusMessage, responseError);
    }

    protected MirthDcmSnd getDcmSnd(DICOMConfiguration configuration) {
        return new MirthDcmSnd(configuration);
    }

    protected class CommandDataDimseRSPHandler extends CustomDimseRSPHandler {

        private DicomObject cmd;

        @Override
        public void onDimseRSP(Association as, DicomObject cmd, DicomObject data) {
            this.cmd = cmd;
        }

        public int getStatus() {
            if (cmd != null) {
                return cmd.getInt(Tag.Status);
            } else {
                return 0;
            }
        }

        public String getCommandData() {
            if (cmd instanceof BasicDicomObject) {
                try {
                    DonkeyElement dicom = new DonkeyElement("<dicom/>");

                    for (Iterator<DicomElement> it = ((BasicDicomObject) cmd).commandIterator(); it.hasNext();) {
                        DicomElement element = it.next();
                        String tag = StringUtils.shortToHex(element.tag() >> 16) + StringUtils.shortToHex(element.tag());

                        DonkeyElement child = dicom.addChildElement("tag" + tag, element.getValueAsString(null, 0));
                        child.setAttribute("len", String.valueOf(element.length()));
                        child.setAttribute("tag", tag);
                        child.setAttribute("vr", String.valueOf(element.vr()));
                    }

                    return dicom.toXml();
                } catch (Throwable t) {
                    logger.error("Unable to extract DICOM command data from response", t);
                }
            }
            return null;
        }
    }
}
