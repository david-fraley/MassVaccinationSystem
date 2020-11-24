/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
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
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.DonkeyElement.DonkeyElementException;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.userutil.AttachmentEntry;
import com.mirth.connect.util.ErrorMessageBuilder;
import com.mirth.connect.util.HttpUtil;

public class WebServiceDispatcher extends DestinationConnector {

    // The system property actually ends up being the maximum request count
    private static final int MAX_REDIRECTS = NumberUtils.toInt(System.getProperty("http.maxRedirects"), 20);

    private Logger logger = Logger.getLogger(this.getClass());
    private EventController eventController = ControllerFactory.getFactory().createEventController();
    private ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();
    private TemplateValueReplacer replacer = new TemplateValueReplacer();
    private WebServiceConfiguration configuration;
    private RegistryBuilder<ConnectionSocketFactory> socketFactoryRegistry;
    private ExecutorService executor;
    private Set<DispatchTask<SOAPMessage>> dispatchTasks;

    /*
     * Dispatch object used for pooling the soap connection, and the current properties used to
     * create the dispatch object
     */
    private Map<Long, DispatchContainer> dispatchContainers = new ConcurrentHashMap<Long, DispatchContainer>();

    /*
     * CloseableHttpClient objects used to request the initial WSDL
     */
    private Set<CloseableHttpClient> clients = Collections.newSetFromMap(new ConcurrentHashMap<CloseableHttpClient, Boolean>());

    @Override
    public void onDeploy() throws ConnectorTaskException {
        // load the default configuration
        String configurationClass = getConfigurationClass();

        try {
            configuration = (WebServiceConfiguration) Class.forName(configurationClass).newInstance();
        } catch (Exception e) {
            logger.trace("could not find custom configuration class, using default");
            configuration = new DefaultWebServiceConfiguration();
        }

        try {
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory());
            configuration.configureConnectorDeploy(this);
        } catch (Exception e) {
            throw new ConnectorTaskException(e);
        }
    }

    @Override
    public void onUndeploy() throws ConnectorTaskException {
        configuration.configureConnectorUndeploy(this);
    }

    @Override
    public void onStart() throws ConnectorTaskException {
        executor = Executors.newCachedThreadPool();
        dispatchTasks = Collections.newSetFromMap(new ConcurrentHashMap<DispatchTask<SOAPMessage>, Boolean>());
    }

    @Override
    public void onStop() throws ConnectorTaskException {
        for (CloseableHttpClient client : clients.toArray(new CloseableHttpClient[clients.size()])) {
            HttpClientUtils.closeQuietly(client);
        }
        clients.clear();

        if (executor != null) {
            executor.shutdown();
        }

        for (DispatchContainer dispatchContainer : dispatchContainers.values()) {
            for (File tempFile : dispatchContainer.getTempFiles()) {
                tempFile.delete();
            }
        }
        dispatchContainers.clear();
    }

    @Override
    public void onHalt() throws ConnectorTaskException {
        for (CloseableHttpClient client : clients.toArray(new CloseableHttpClient[clients.size()])) {
            HttpClientUtils.closeQuietly(client);
        }
        clients.clear();

        if (executor != null) {
            boolean shutdown = executor.isShutdown();
            executor.shutdownNow();

            // Only log an error out if this is the first time the executor was shutdown
            if (!shutdown) {
                try {
                    // Wait a bit for tasks to finish and remove themselves from the set
                    executor.awaitTermination(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new ConnectorTaskException("Halt task interrupted while waiting for executor to shutdown.", e);
                }

                int numTasks = dispatchTasks.size();
                if (numTasks > 0) {
                    String message = "Error halting Web Service Sender: " + numTasks + " request" + (numTasks == 1 ? "" : "s") + " failed to be halted. This can potentially lead to a thread leak if the requests continue to hang.";
                    logger.error(message);
                    eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), null, ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), getConnectorProperties().getName(), message, null));
                }
            }
        }

        for (DispatchContainer dispatchContainer : dispatchContainers.values().toArray(new DispatchContainer[dispatchContainers.size()])) {
            for (File tempFile : dispatchContainer.getTempFiles().toArray(new File[dispatchContainer.getTempFiles().size()])) {
                tempFile.delete();
            }
        }
        dispatchContainers.clear();
    }

    @Override
    protected String getConfigurationClass() {
        return configurationController.getProperty(getConnectorProperties().getProtocol(), "wsConfigurationClass");
    }

    private String sourceToXmlString(Source source) throws TransformerConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        Writer writer = new StringWriter();
        transformer.transform(source, new StreamResult(writer));
        return writer.toString();
    }

    private void createDispatch(WebServiceDispatcherProperties webServiceDispatcherProperties, DispatchContainer dispatchContainer, int timeout) throws Exception {
        String wsdlUrl = webServiceDispatcherProperties.getWsdlUrl();
        String username = webServiceDispatcherProperties.getUsername();
        String password = webServiceDispatcherProperties.getPassword();
        String serviceName = webServiceDispatcherProperties.getService();
        String portName = webServiceDispatcherProperties.getPort();

        /*
         * The dispatch needs to be created if it hasn't been created yet (null). It needs to be
         * recreated if any of the above variables are different than what were used to create the
         * current dispatch object. This could happen if variables are being used for these
         * properties.
         */
        if (dispatchContainer.getDispatch() == null || !StringUtils.equals(wsdlUrl, dispatchContainer.getCurrentWsdlUrl()) || !StringUtils.equals(username, dispatchContainer.getCurrentUsername()) || !StringUtils.equals(password, dispatchContainer.getCurrentPassword()) || !StringUtils.equals(serviceName, dispatchContainer.getCurrentServiceName()) || !StringUtils.equals(portName, dispatchContainer.getCurrentPortName())) {
            dispatchContainer.setCurrentWsdlUrl(wsdlUrl);
            dispatchContainer.setCurrentUsername(username);
            dispatchContainer.setCurrentPassword(password);
            dispatchContainer.setCurrentServiceName(serviceName);
            dispatchContainer.setCurrentPortName(portName);

            URL endpointUrl = getWsdlUrl(webServiceDispatcherProperties, dispatchContainer, timeout);
            QName serviceQName = QName.valueOf(serviceName);
            QName portQName = QName.valueOf(portName);

            // create the service and dispatch
            logger.debug("Creating web service: url=" + endpointUrl.toString() + ", service=" + serviceQName + ", port=" + portQName);
            Service service = Service.create(endpointUrl, serviceQName);

            Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);

            if (timeout > 0) {
                dispatch.getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", timeout);
                dispatch.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", timeout);
                dispatch.getRequestContext().put("com.sun.xml.ws.connect.timeout", timeout);
                dispatch.getRequestContext().put("com.sun.xml.ws.request.timeout", timeout);
            }

            Map<String, List<String>> requestHeaders = (Map<String, List<String>>) dispatch.getRequestContext().get(MessageContext.HTTP_REQUEST_HEADERS);
            if (requestHeaders == null) {
                requestHeaders = new HashMap<String, List<String>>();
            }
            dispatchContainer.setDefaultRequestHeaders(requestHeaders);

            dispatchContainer.setDispatch(dispatch);
        }
    }

    /**
     * Returns the URL for the passed in String. If the URL requires authentication, then the WSDL
     * is saved as a temp file and the URL for that file is returned.
     * 
     * @param wsdlUrl
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    protected URL getWsdlUrl(WebServiceDispatcherProperties webServiceDispatcherProperties, DispatchContainer dispatchContainer, int timeout) throws Exception {
        URI uri = new URI(dispatchContainer.getCurrentWsdlUrl());

        // If the URL points to file, just return it
        if (!uri.getScheme().equalsIgnoreCase("file")) {
            BasicHttpClientConnectionManager httpClientConnectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry.build());
            httpClientConnectionManager.setSocketConfig(SocketConfig.custom().setSoTimeout(timeout).build());
            HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(httpClientConnectionManager);
            HttpUtil.configureClientBuilder(clientBuilder);
            CloseableHttpClient client = clientBuilder.build();

            try {
                clients.add(client);
                HttpClientContext context = HttpClientContext.create();

                if (dispatchContainer.getCurrentUsername() != null && dispatchContainer.getCurrentPassword() != null) {
                    CredentialsProvider credsProvider = new BasicCredentialsProvider();
                    AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
                    Credentials credentials = new UsernamePasswordCredentials(dispatchContainer.getCurrentUsername(), dispatchContainer.getCurrentPassword());
                    credsProvider.setCredentials(authScope, credentials);
                    AuthCache authCache = new BasicAuthCache();
                    RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.<AuthSchemeProvider> create();
                    registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory());

                    context.setCredentialsProvider(credsProvider);
                    context.setAuthSchemeRegistry(registryBuilder.build());
                    context.setAuthCache(authCache);
                }

                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
                context.setRequestConfig(requestConfig);

                return getWsdl(client, context, dispatchContainer, new HashMap<String, File>(), dispatchContainer.getCurrentWsdlUrl()).toURI().toURL();
            } finally {
                HttpClientUtils.closeQuietly(client);
                clients.remove(client);
            }
        }

        return uri.toURL();
    }

    private File getWsdl(CloseableHttpClient client, HttpContext context, DispatchContainer dispatchContainer, Map<String, File> visitedUrls, String wsdlUrl) throws Exception {
        if (visitedUrls.containsKey(wsdlUrl)) {
            return visitedUrls.get(wsdlUrl);
        }

        String wsdl = null;
        StatusLine responseStatusLine = null;
        CloseableHttpResponse response = client.execute(new HttpGet(wsdlUrl), context);

        try {
            responseStatusLine = response.getStatusLine();

            if (responseStatusLine.getStatusCode() == HttpStatus.SC_OK) {
                ContentType responseContentType = ContentType.get(response.getEntity());
                if (responseContentType == null) {
                    responseContentType = ContentType.TEXT_XML;
                }

                Charset responseCharset = responseContentType.getCharset();
                if (responseContentType.getCharset() == null) {
                    responseCharset = ContentType.TEXT_XML.getCharset();
                }

                wsdl = IOUtils.toString(response.getEntity().getContent(), responseCharset);
            }
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        if (StringUtils.isNotBlank(wsdl)) {
            File tempFile = File.createTempFile("WebServiceSender", ".wsdl");
            tempFile.deleteOnExit();
            visitedUrls.put(wsdlUrl, tempFile);

            try {
                DonkeyElement element = new DonkeyElement(wsdl);
                for (DonkeyElement child : element.getChildElements()) {
                    if (child.getLocalName().equals("import") && child.hasAttribute("location")) {
                        String location = new URI(wsdlUrl).resolve(child.getAttribute("location")).toString();
                        child.setAttribute("location", getWsdl(client, context, dispatchContainer, visitedUrls, location).toURI().toURL().toString());
                    }
                }

                wsdl = element.toXml();
            } catch (Exception e) {
                logger.warn("Unable to cache imports for WSDL at URL: " + wsdlUrl, e);
            }

            FileUtils.writeStringToFile(tempFile, wsdl);
            dispatchContainer.getTempFiles().add(tempFile);

            return tempFile;
        } else {
            throw new Exception("Unable to load WSDL at URL \"" + wsdlUrl + "\": " + String.valueOf(responseStatusLine));
        }
    }

    @Override
    public void replaceConnectorProperties(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        WebServiceDispatcherProperties webServiceDispatcherProperties = (WebServiceDispatcherProperties) connectorProperties;

        // Replace all values in connector properties
        webServiceDispatcherProperties.setWsdlUrl(replacer.replaceValues(webServiceDispatcherProperties.getWsdlUrl(), connectorMessage));
        webServiceDispatcherProperties.setUsername(replacer.replaceValues(webServiceDispatcherProperties.getUsername(), connectorMessage));
        webServiceDispatcherProperties.setPassword(replacer.replaceValues(webServiceDispatcherProperties.getPassword(), connectorMessage));
        webServiceDispatcherProperties.setService(replacer.replaceValues(webServiceDispatcherProperties.getService(), connectorMessage));
        webServiceDispatcherProperties.setPort(replacer.replaceValues(webServiceDispatcherProperties.getPort(), connectorMessage));
        webServiceDispatcherProperties.setLocationURI(replacer.replaceValues(webServiceDispatcherProperties.getLocationURI(), connectorMessage));
        webServiceDispatcherProperties.setSocketTimeout(replacer.replaceValues(webServiceDispatcherProperties.getSocketTimeout(), connectorMessage));

        webServiceDispatcherProperties.setSoapAction(replacer.replaceValues(webServiceDispatcherProperties.getSoapAction(), connectorMessage));
        webServiceDispatcherProperties.setEnvelope(replacer.replaceValues(webServiceDispatcherProperties.getEnvelope(), connectorMessage));

        Map<String, List<String>> headers = webServiceDispatcherProperties.getHeadersMap();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            replacer.replaceValuesInList(entry.getValue(), connectorMessage);
        }
        webServiceDispatcherProperties.setHeadersMap(headers);
        webServiceDispatcherProperties.setHeadersVariable(replacer.replaceValues(webServiceDispatcherProperties.getHeadersVariable(), connectorMessage));

        if (webServiceDispatcherProperties.isUseMtom()) {
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentNames(), connectorMessage);
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentContents(), connectorMessage);
            replacer.replaceValuesInList(webServiceDispatcherProperties.getAttachmentTypes(), connectorMessage);

            webServiceDispatcherProperties.setAttachmentsVariable(replacer.replaceValues(webServiceDispatcherProperties.getAttachmentsVariable(), connectorMessage));

        }
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        WebServiceDispatcherProperties webServiceDispatcherProperties = (WebServiceDispatcherProperties) connectorProperties;

        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.SENDING));

        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;
        boolean validateResponse = false;

        try {
            long dispatcherId = connectorMessage.getDispatcherId();
            DispatchContainer dispatchContainer = dispatchContainers.get(dispatcherId);
            if (dispatchContainer == null) {
                dispatchContainer = new DispatchContainer();
                dispatchContainers.put(dispatcherId, dispatchContainer);
            }

            int timeout = NumberUtils.toInt(webServiceDispatcherProperties.getSocketTimeout(), 30000);

            /*
             * Initialize the dispatch object if it hasn't been initialized yet, or create a new one
             * if the connector properties have changed due to variables.
             */
            createDispatch(webServiceDispatcherProperties, dispatchContainer, timeout);

            Dispatch<SOAPMessage> dispatch = dispatchContainer.getDispatch();

            configuration.configureDispatcher(this, webServiceDispatcherProperties, dispatch.getRequestContext());

            SOAPBinding soapBinding = (SOAPBinding) dispatch.getBinding();

            if (webServiceDispatcherProperties.isUseAuthentication()) {
                String currentUsername = dispatchContainer.getCurrentUsername();
                String currentPassword = dispatchContainer.getCurrentPassword();

                dispatch.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, currentUsername);
                dispatch.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, currentPassword);
            }

            // See: http://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383528
            String soapAction = webServiceDispatcherProperties.getSoapAction();

            if (StringUtils.isNotEmpty(soapAction)) {
                dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true); // MIRTH-2109
                dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
            }

            // Get default headers
            Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>(dispatchContainer.getDefaultRequestHeaders());

            // Add custom headers
            Map<String, List<String>> headersSource = getHeaders(webServiceDispatcherProperties, connectorMessage);

            if (MapUtils.isNotEmpty(headersSource)) {
                for (Entry<String, List<String>> entry : headersSource.entrySet()) {
                    List<String> valueList = requestHeaders.get(entry.getKey());

                    if (valueList == null) {
                        valueList = new ArrayList<String>();
                        requestHeaders.put(entry.getKey(), valueList);
                    }

                    valueList.addAll(entry.getValue());
                }
            }

            dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

            // build the message
            logger.debug("Creating SOAP envelope.");
            AttachmentHandlerProvider attachmentHandlerProvider = getAttachmentHandlerProvider();
            String content = attachmentHandlerProvider.reAttachMessage(webServiceDispatcherProperties.getEnvelope(), connectorMessage, webServiceDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());
            Source source = new StreamSource(new StringReader(content));
            SOAPMessage message = soapBinding.getMessageFactory().createMessage();
            message.getSOAPPart().setContent(source);

            if (webServiceDispatcherProperties.isUseMtom()) {
                soapBinding.setMTOMEnabled(true);
                List<AttachmentEntry> attachmentEntries = getAttachments(webServiceDispatcherProperties, connectorMessage);

                for (AttachmentEntry entry : attachmentEntries) {
                    String attachmentContentId = entry.getName();
                    String attachmentContentType = entry.getMimeType();
                    String attachmentContent = attachmentHandlerProvider.reAttachMessage(entry.getContent(), connectorMessage, webServiceDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());

                    AttachmentPart attachment = message.createAttachmentPart();
                    attachment.setBase64Content(new ByteArrayInputStream(attachmentContent.getBytes("UTF-8")), attachmentContentType);
                    attachment.setContentId(attachmentContentId);
                    message.addAttachmentPart(attachment);
                }
            } else {
                soapBinding.setMTOMEnabled(false);
            }

            message.saveChanges();

            if (StringUtils.isNotBlank(webServiceDispatcherProperties.getLocationURI())) {
                dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceDispatcherProperties.getLocationURI());
            }

            boolean redirect = false;
            int tryCount = 0;

            /*
             * Attempt the invocation until we hit the maximum allowed redirects. The redirections
             * we handle are when the scheme changes (i.e. from HTTP to HTTPS).
             */
            do {
                redirect = false;
                tryCount++;

                try {
                    DispatchTask<SOAPMessage> task = new DispatchTask<SOAPMessage>(dispatch, message, webServiceDispatcherProperties.isOneWay());
                    SOAPMessage result;

                    /*
                     * If the timeout is set to zero, we need to do the invocation in a separate
                     * thread. This is because there's no way to get a reference to the underlying
                     * JAX-WS socket, so there's no way to forcefully halt the dispatch. If the
                     * socket is truly hung and the user halts the channel, the best we can do is
                     * just interrupt and ignore the thread. This means that a thread leak is
                     * potentially introduced, so we need to notify the user appropriately.
                     */
                    if (timeout == 0) {
                        // Submit the task to an executor so that it's interruptible
                        Future<SOAPMessage> future = executor.submit(task);
                        // Keep track of the task by adding it to our set
                        dispatchTasks.add(task);
                        result = future.get();
                    } else {
                        // Call the task directly
                        result = task.call();
                    }

                    handleSOAPResult(connectorProperties, connectorMessage, result);

                    if (webServiceDispatcherProperties.isOneWay()) {
                        responseStatusMessage = "Invoked one way operation successfully.";
                    } else {
                        responseData = sourceToXmlString(result.getSOAPPart().getContent());
                        responseStatusMessage = "Invoked two way operation successfully.";
                    }
                    logger.debug("Finished invoking web service, got result.");

                    // Automatically accept message; if a SOAP fault occurred an exception will have been thrown
                    responseStatus = Status.SENT;
                } catch (Throwable e) {
                    // Unwrap the exception if it came from the executor
                    if (e instanceof ExecutionException && e.getCause() != null) {
                        e = e.getCause();
                    }

                    // If the dispatch was interrupted, make sure to reset the interrupted flag
                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }

                    Integer responseCode = null;
                    String location = null;

                    if (dispatch.getResponseContext() != null) {
                        responseCode = (Integer) dispatch.getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE);

                        Map<String, List<String>> headers = (Map<String, List<String>>) dispatch.getResponseContext().get(MessageContext.HTTP_RESPONSE_HEADERS);
                        if (MapUtils.isNotEmpty(headers)) {
                            List<String> locations = headers.get("Location");
                            if (CollectionUtils.isNotEmpty(locations)) {
                                location = locations.get(0);
                            }
                        }
                    }

                    if (tryCount < MAX_REDIRECTS && responseCode != null && responseCode >= 300 && responseCode < 400 && StringUtils.isNotBlank(location)) {
                        redirect = true;

                        // Replace the endpoint with the redirected URL
                        dispatch.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, location);
                    } else {
                        if (e instanceof NoRouteToHostException || ((e.getCause() != null) && (e.getCause() instanceof NoRouteToHostException))) {
                            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("HTTP transport error", e);
                            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "HTTP transport error", e);
                            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "HTTP transport error.", e));
                        } else if ((e.getClass() == ConnectException.class) || ((e.getCause() != null) && (e.getCause().getClass() == ConnectException.class))) {
                            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Connection refused.", e);
                            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Connection refused", e);
                            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Connection refused.", e));
                        } else {
                            if (e instanceof SOAPFaultException) {
                                // Only set the status to ERROR if a SOAP fault occurred
                                responseStatus = Status.ERROR;
                                try {
                                    responseData = new DonkeyElement(((SOAPFaultException) e).getFault()).toXml();
                                } catch (DonkeyElementException e2) {
                                }
                            }
                            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error invoking web service", e);
                            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error invoking web service", e);
                            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error invoking web service.", e));
                        }
                    }
                }
            } while (redirect && tryCount < MAX_REDIRECTS);
        } catch (Exception e) {
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error creating web service dispatch", e);
            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error creating web service dispatch", e);
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error creating web service dispatch.", e));
        } finally {
            eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.IDLE));
        }

        return new Response(responseStatus, responseData, responseStatusMessage, responseError, validateResponse);
    }

    protected void handleSOAPResult(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage, SOAPMessage result) throws Exception {}

    protected class DispatchContainer {
        /*
         * Dispatch object used for pooling the soap connection, and the current properties used to
         * create the dispatch object
         */
        private Dispatch<SOAPMessage> dispatch = null;
        private String currentWsdlUrl = null;
        private String currentUsername = null;
        private String currentPassword = null;
        private String currentServiceName = null;
        private String currentPortName = null;
        private List<File> tempFiles = new ArrayList<File>();
        private Map<String, List<String>> defaultRequestHeaders;

        public Dispatch<SOAPMessage> getDispatch() {
            return dispatch;
        }

        public void setDispatch(Dispatch<SOAPMessage> dispatch) {
            this.dispatch = dispatch;
        }

        public String getCurrentWsdlUrl() {
            return currentWsdlUrl;
        }

        public void setCurrentWsdlUrl(String currentWsdlUrl) {
            this.currentWsdlUrl = currentWsdlUrl;
        }

        public String getCurrentUsername() {
            return currentUsername;
        }

        public void setCurrentUsername(String currentUsername) {
            this.currentUsername = currentUsername;
        }

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getCurrentServiceName() {
            return currentServiceName;
        }

        public void setCurrentServiceName(String currentServiceName) {
            this.currentServiceName = currentServiceName;
        }

        public String getCurrentPortName() {
            return currentPortName;
        }

        public void setCurrentPortName(String currentPortName) {
            this.currentPortName = currentPortName;
        }

        public List<File> getTempFiles() {
            return tempFiles;
        }

        public Map<String, List<String>> getDefaultRequestHeaders() {
            return defaultRequestHeaders;
        }

        public void setDefaultRequestHeaders(Map<String, List<String>> defaultRequestHeaders) {
            this.defaultRequestHeaders = defaultRequestHeaders;
        }
    }

    public RegistryBuilder<ConnectionSocketFactory> getSocketFactoryRegistry() {
        return socketFactoryRegistry;
    }

    private class DispatchTask<T> implements Callable<T> {

        private Dispatch<T> dispatch;
        private T message;
        private boolean oneWay;

        public DispatchTask(Dispatch<T> dispatch, T message, boolean oneWay) {
            this.dispatch = dispatch;
            this.message = message;
            this.oneWay = oneWay;
        }

        @Override
        public T call() throws Exception {
            try {
                if (oneWay) {
                    logger.debug("Invoking one way service...");
                    dispatch.invokeOneWay(message);
                    return null;
                } else {
                    logger.debug("Invoking web service...");
                    return dispatch.invoke(message);
                }
            } finally {
                if (dispatchTasks != null) {
                    dispatchTasks.remove(this);
                }
            }
        }
    }

    Map<String, List<String>> getHeaders(WebServiceDispatcherProperties webServiceDispatcherProperties, ConnectorMessage connectorMessage) {
        return HttpUtil.getTableMap(webServiceDispatcherProperties.isUseHeadersVariable(), webServiceDispatcherProperties.getHeadersVariable(), webServiceDispatcherProperties.getHeadersMap(), getMessageMaps(), connectorMessage);
    }

    List<AttachmentEntry> getAttachments(WebServiceDispatcherProperties webServiceDispatcherProperties, ConnectorMessage connectorMessage) {
        List<AttachmentEntry> attachmentList = new ArrayList<>();

        if (webServiceDispatcherProperties.isUseAttachmentsVariable()) {
            try {
                List<?> attachmentEntries = (List<?>) getMessageMaps().get(webServiceDispatcherProperties.getAttachmentsVariable(), connectorMessage);
                if (attachmentEntries != null) {
                    for (Object entry : attachmentEntries) {
                        if (entry instanceof AttachmentEntry) {
                            attachmentList.add(new AttachmentEntry((AttachmentEntry) entry));
                        } else {
                            logger.trace("Error getting attachment entry from map '" + webServiceDispatcherProperties.getAttachmentsVariable() + "'. Skipping entry.");
                        }
                    }
                } else {
                    logger.warn("Attachments list variable '" + webServiceDispatcherProperties.getAttachmentsVariable() + "' not found.");
                }
            } catch (Exception e) {
                logger.warn("Error getting attachments from map '" + webServiceDispatcherProperties.getHeadersVariable() + "'.", e);
            }
        } else {
            List<String> attachmentIds = new ArrayList<String>(webServiceDispatcherProperties.getAttachmentNames());
            List<String> attachmentContents = new ArrayList<String>(webServiceDispatcherProperties.getAttachmentContents());
            List<String> attachmentTypes = new ArrayList<String>(webServiceDispatcherProperties.getAttachmentTypes());

            for (int i = 0; i < attachmentIds.size(); i++) {
                attachmentList.add(new AttachmentEntry(attachmentIds.get(i), attachmentContents.get(i), attachmentTypes.get(i)));
            }
        }

        return attachmentList;
    }

    @Override
    public WebServiceDispatcherProperties getConnectorProperties() {
        return (WebServiceDispatcherProperties) super.getConnectorProperties();
    }
}