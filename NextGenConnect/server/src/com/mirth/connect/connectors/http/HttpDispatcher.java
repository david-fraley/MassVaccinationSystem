/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.connectors.http;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

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
import com.mirth.connect.donkey.util.Base64Util;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.server.controllers.ControllerFactory;
import com.mirth.connect.server.controllers.EventController;
import com.mirth.connect.server.util.TemplateValueReplacer;
import com.mirth.connect.userutil.MessageHeaders;
import com.mirth.connect.util.CharsetUtils;
import com.mirth.connect.util.ErrorMessageBuilder;
import com.mirth.connect.util.HttpUtil;

public class HttpDispatcher extends DestinationConnector {

    private static final String PROXY_CONTEXT_KEY = "dispatcherProxy";
    private static final Pattern AUTH_HEADER_PATTERN = Pattern.compile("([^\\s=,]+)\\s*=\\s*([^=,;\"\\s]+|\"([^\"]|\\\\[\\s\\S])*(?<!\\\\)\")");
    private static final int MAX_MAP_SIZE = 100;

    protected Logger logger = Logger.getLogger(this.getClass());

    protected ConfigurationController configurationController = ControllerFactory.getFactory().createConfigurationController();
    protected EventController eventController = ControllerFactory.getFactory().createEventController();
    protected TemplateValueReplacer replacer = new TemplateValueReplacer();

    private Map<Long, CloseableHttpClient> clients = new ConcurrentHashMap<Long, CloseableHttpClient>();
    private HttpConfiguration configuration;
    private RegistryBuilder<ConnectionSocketFactory> socketFactoryRegistry;
    private Map<String, String[]> binaryMimeTypesArrayMap;
    private Map<String, Pattern> binaryMimeTypesRegexMap;

    @Override
    public void onDeploy() throws ConnectorTaskException {
        // load the default configuration
        String configurationClass = getConfigurationClass();

        try {
            configuration = (HttpConfiguration) Class.forName(configurationClass).newInstance();
        } catch (Exception e) {
            logger.trace("could not find custom configuration class, using default");
            configuration = new DefaultHttpConfiguration();
        }

        try {
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory());
            configuration.configureConnectorDeploy(this);
        } catch (Exception e) {
            throw new ConnectorTaskException(e);
        }

        if (getConnectorProperties().isResponseBinaryMimeTypesRegex()) {
            binaryMimeTypesRegexMap = new ConcurrentHashMap<String, Pattern>();
        } else {
            binaryMimeTypesArrayMap = new ConcurrentHashMap<String, String[]>();
        }
    }

    @Override
    public void onUndeploy() throws ConnectorTaskException {
        configuration.configureConnectorUndeploy(this);
    }

    @Override
    public void onStart() throws ConnectorTaskException {}

    @Override
    public void onStop() throws ConnectorTaskException {
        for (CloseableHttpClient client : clients.values().toArray(new CloseableHttpClient[clients.size()])) {
            HttpClientUtils.closeQuietly(client);
        }

        clients.clear();
    }

    @Override
    public void onHalt() throws ConnectorTaskException {
        for (CloseableHttpClient client : clients.values().toArray(new CloseableHttpClient[clients.size()])) {
            HttpClientUtils.closeQuietly(client);
        }

        clients.clear();
    }

    @Override
    public void replaceConnectorProperties(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        HttpDispatcherProperties httpDispatcherProperties = (HttpDispatcherProperties) connectorProperties;

        // Replace all values in connector properties
        httpDispatcherProperties.setHost(replacer.replaceValues(httpDispatcherProperties.getHost(), connectorMessage));
        httpDispatcherProperties.setProxyAddress(replacer.replaceValues(httpDispatcherProperties.getProxyAddress(), connectorMessage));
        httpDispatcherProperties.setProxyPort(replacer.replaceValues(httpDispatcherProperties.getProxyPort(), connectorMessage));
        httpDispatcherProperties.setResponseBinaryMimeTypes(replacer.replaceValues(httpDispatcherProperties.getResponseBinaryMimeTypes(), connectorMessage));
        httpDispatcherProperties.setHeadersMap(replacer.replaceKeysAndValuesInMap(httpDispatcherProperties.getHeadersMap(), connectorMessage));
        httpDispatcherProperties.setHeadersVariable(replacer.replaceValues(httpDispatcherProperties.getHeadersVariable(), connectorMessage));
        httpDispatcherProperties.setParametersMap(replacer.replaceKeysAndValuesInMap(httpDispatcherProperties.getParametersMap(), connectorMessage));
        httpDispatcherProperties.setParametersVariable(replacer.replaceValues(httpDispatcherProperties.getParametersVariable(), connectorMessage));
        httpDispatcherProperties.setUsername(replacer.replaceValues(httpDispatcherProperties.getUsername(), connectorMessage));
        httpDispatcherProperties.setPassword(replacer.replaceValues(httpDispatcherProperties.getPassword(), connectorMessage));
        httpDispatcherProperties.setContent(replacer.replaceValues(httpDispatcherProperties.getContent(), connectorMessage));
        httpDispatcherProperties.setContentType(replacer.replaceValues(httpDispatcherProperties.getContentType(), connectorMessage));
        httpDispatcherProperties.setSocketTimeout(replacer.replaceValues(httpDispatcherProperties.getSocketTimeout(), connectorMessage));
    }

    @Override
    public Response send(ConnectorProperties connectorProperties, ConnectorMessage connectorMessage) {
        HttpDispatcherProperties httpDispatcherProperties = (HttpDispatcherProperties) connectorProperties;
        eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.WRITING));

        String responseData = null;
        String responseError = null;
        String responseStatusMessage = null;
        Status responseStatus = Status.QUEUED;
        boolean validateResponse = false;

        CloseableHttpClient client = null;
        HttpRequestBase httpMethod = null;
        CloseableHttpResponse httpResponse = null;
        File tempFile = null;
        int socketTimeout = NumberUtils.toInt(httpDispatcherProperties.getSocketTimeout(), 30000);

        long dispatcherId = connectorMessage.getDispatcherId();

        try {
            configuration.configureDispatcher(this, httpDispatcherProperties);

            client = clients.get(dispatcherId);
            if (client == null) {
                BasicHttpClientConnectionManager httpClientConnectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry.build());
                httpClientConnectionManager.setSocketConfig(SocketConfig.custom().setSoTimeout(socketTimeout).build());
                HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(httpClientConnectionManager);
                HttpUtil.configureClientBuilder(clientBuilder);

                if (httpDispatcherProperties.isUseProxyServer()) {
                    clientBuilder.setRoutePlanner(new DynamicProxyRoutePlanner());
                }

                client = clientBuilder.build();
                clients.put(dispatcherId, client);
            }

            URI hostURI = new URI(httpDispatcherProperties.getHost());
            String host = hostURI.getHost();
            String scheme = hostURI.getScheme();
            int port = hostURI.getPort();
            if (port == -1) {
                if (scheme.equalsIgnoreCase("https")) {
                    port = 443;
                } else {
                    port = 80;
                }
            }

            // Parse the content type field first, and then add the charset if needed
            ContentType contentType = ContentType.parse(httpDispatcherProperties.getContentType());

            /*
             * If a charset is set in the Content Type field, use that.
             * 
             * If the charset is NONE, keep it as null.
             * 
             * Otherwise, use the charset from the Character Encoding drop-down menu.
             */
            Charset charset = null;
            if (contentType.getCharset() != null) {
                charset = contentType.getCharset();
            } else if (!StringUtils.equalsIgnoreCase(httpDispatcherProperties.getCharset(), CharsetUtils.NONE)) {
                charset = Charset.forName(CharsetUtils.getEncoding(httpDispatcherProperties.getCharset()));
            }

            if (httpDispatcherProperties.isMultipart()) {
                tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
            }

            HttpHost target = new HttpHost(host, port, scheme);

            httpMethod = buildHttpRequest(hostURI, httpDispatcherProperties, connectorMessage, tempFile, contentType, charset);

            HttpClientContext context = HttpClientContext.create();

            // authentication
            if (httpDispatcherProperties.isUseAuthentication()) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
                Credentials credentials = new UsernamePasswordCredentials(httpDispatcherProperties.getUsername(), httpDispatcherProperties.getPassword());
                credsProvider.setCredentials(authScope, credentials);
                AuthCache authCache = new BasicAuthCache();
                RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.<AuthSchemeProvider> create();

                if (AuthSchemes.DIGEST.equalsIgnoreCase(httpDispatcherProperties.getAuthenticationType())) {
                    logger.debug("using Digest authentication");
                    registryBuilder.register(AuthSchemes.DIGEST, new DigestSchemeFactory(charset));

                    if (httpDispatcherProperties.isUsePreemptiveAuthentication()) {
                        processDigestChallenge(authCache, target, credentials, httpMethod, context);
                    }
                } else {
                    logger.debug("using Basic authentication");
                    registryBuilder.register(AuthSchemes.BASIC, new BasicSchemeFactory(charset));

                    if (httpDispatcherProperties.isUsePreemptiveAuthentication()) {
                        authCache.put(target, new BasicScheme());
                    }
                }

                context.setCredentialsProvider(credsProvider);
                context.setAuthSchemeRegistry(registryBuilder.build());
                context.setAuthCache(authCache);

                logger.debug("using authentication with credentials: " + credentials);
            }

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(socketTimeout).setSocketTimeout(socketTimeout).setStaleConnectionCheckEnabled(true).build();
            context.setRequestConfig(requestConfig);

            // Set proxy information
            if (httpDispatcherProperties.isUseProxyServer()) {
                context.setAttribute(PROXY_CONTEXT_KEY, new HttpHost(httpDispatcherProperties.getProxyAddress(), Integer.parseInt(httpDispatcherProperties.getProxyPort())));
            }

            // execute the method
            logger.debug("executing method: type=" + httpMethod.getMethod() + ", uri=" + httpMethod.getURI().toString());
            httpResponse = client.execute(target, httpMethod, context);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            logger.debug("received status code: " + statusCode);

            Map<String, List<String>> headers = new HashMap<String, List<String>>();
            for (Header header : httpResponse.getAllHeaders()) {
                List<String> list = headers.get(header.getName());

                if (list == null) {
                    list = new ArrayList<String>();
                    headers.put(header.getName(), list);
                }

                list.add(header.getValue());
            }

            connectorMessage.getConnectorMap().put("responseStatusLine", statusLine.toString());
            connectorMessage.getConnectorMap().put("responseHeaders", new MessageHeaders(new CaseInsensitiveMap(headers)));

            ContentType responseContentType = ContentType.get(httpResponse.getEntity());
            if (responseContentType == null) {
                responseContentType = ContentType.TEXT_PLAIN;
            }

            Charset responseCharset = charset;
            if (responseContentType.getCharset() != null) {
                responseCharset = responseContentType.getCharset();
            }

            final String responseBinaryMimeTypes = httpDispatcherProperties.getResponseBinaryMimeTypes();
            BinaryContentTypeResolver binaryContentTypeResolver = new BinaryContentTypeResolver() {
                @Override
                public boolean isBinaryContentType(ContentType contentType) {
                    return HttpDispatcher.this.isBinaryContentType(responseBinaryMimeTypes, contentType);
                }
            };

            /*
             * First parse out the body of the HTTP response. Depending on the connector settings,
             * this could end up being a string encoded with the response charset, a byte array
             * representing the raw response payload, or a MimeMultipart object.
             */
            Object responseBody = "";

            // The entity could be null in certain cases such as 204 responses
            if (httpResponse.getEntity() != null) {
                // Only parse multipart if XML Body is selected and Parse Multipart is enabled
                if (httpDispatcherProperties.isResponseXmlBody() && httpDispatcherProperties.isResponseParseMultipart() && responseContentType.getMimeType().startsWith(FileUploadBase.MULTIPART)) {
                    responseBody = new MimeMultipart(new ByteArrayDataSource(httpResponse.getEntity().getContent(), responseContentType.toString()));
                } else if (binaryContentTypeResolver.isBinaryContentType(responseContentType)) {
                    responseBody = IOUtils.toByteArray(httpResponse.getEntity().getContent());
                } else {
                    responseBody = IOUtils.toString(httpResponse.getEntity().getContent(), responseCharset);
                }
            }

            /*
             * Now that we have the response body, we need to create the actual Response message
             * data. Depending on the connector settings this could be our custom serialized XML, a
             * Base64 string encoded from the raw response payload, or a string encoded from the
             * payload with the request charset.
             */
            if (httpDispatcherProperties.isResponseXmlBody()) {
                responseData = HttpMessageConverter.httpResponseToXml(statusLine.toString(), headers, responseBody, responseContentType, httpDispatcherProperties.isResponseParseMultipart(), httpDispatcherProperties.isResponseIncludeMetadata(), binaryContentTypeResolver);
            } else if (responseBody instanceof byte[]) {
                responseData = new String(Base64Util.encodeBase64((byte[]) responseBody), "US-ASCII");
            } else {
                responseData = (String) responseBody;
            }

            validateResponse = httpDispatcherProperties.getDestinationConnectorProperties().isValidateResponse();

            if (statusCode < HttpStatus.SC_BAD_REQUEST) {
                responseStatus = Status.SENT;
            } else {
                eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Received error response from HTTP server.", null));
                responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Received error response from HTTP server.", null);
                responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), responseData, null);
            }
        } catch (Throwable t) {
            eventController.dispatchEvent(new ErrorEvent(getChannelId(), getMetaDataId(), connectorMessage.getMessageId(), ErrorEventType.DESTINATION_CONNECTOR, getDestinationName(), connectorProperties.getName(), "Error connecting to HTTP server.", t));
            responseStatusMessage = ErrorMessageBuilder.buildErrorResponse("Error connecting to HTTP server", t);
            responseError = ErrorMessageBuilder.buildErrorMessage(connectorProperties.getName(), "Error connecting to HTTP server", t);

            if (t instanceof Error || t instanceof IllegalStateException) {
                // If an error occurred we can't guarantee the state of the client, so close it
                HttpUtil.closeVeryQuietly(httpResponse);
                HttpClientUtils.closeQuietly(client);
                clients.remove(dispatcherId);
            }
        } finally {
            try {
                HttpUtil.closeVeryQuietly(httpResponse);

                // Delete temp files if we created them
                if (tempFile != null) {
                    tempFile.delete();
                    tempFile = null;
                }
            } finally {
                eventController.dispatchEvent(new ConnectionStatusEvent(getChannelId(), getMetaDataId(), getDestinationName(), ConnectionStatusEventType.IDLE));
            }
        }

        return new Response(responseStatus, responseData, responseStatusMessage, responseError, validateResponse);
    }

    @Override
    protected String getConfigurationClass() {
        return configurationController.getProperty(getConnectorProperties().getProtocol(), "httpConfigurationClass");
    }

    public RegistryBuilder<ConnectionSocketFactory> getSocketFactoryRegistry() {
        return socketFactoryRegistry;
    }

    private HttpRequestBase buildHttpRequest(URI hostURI, HttpDispatcherProperties httpDispatcherProperties, ConnectorMessage connectorMessage, File tempFile, ContentType contentType, Charset charset) throws Exception {
        String method = httpDispatcherProperties.getMethod();
        boolean isMultipart = httpDispatcherProperties.isMultipart();
        Map<String, List<String>> headers = getHeaders(httpDispatcherProperties, connectorMessage);
        Map<String, List<String>> parameters = getParameters(httpDispatcherProperties, connectorMessage);

        Object content = null;
        if (httpDispatcherProperties.isDataTypeBinary()) {
            content = getAttachmentHandlerProvider().reAttachMessage(httpDispatcherProperties.getContent(), connectorMessage, null, true, httpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());
        } else {
            content = getAttachmentHandlerProvider().reAttachMessage(httpDispatcherProperties.getContent(), connectorMessage, httpDispatcherProperties.getDestinationConnectorProperties().isReattachAttachments());

            // If text mode is used and a specific charset isn't already defined, use the one from the connector properties
            if (contentType.getCharset() == null && charset != null) {
                contentType = HttpMessageConverter.setCharset(contentType, charset);
            }
        }

        // populate the query parameters
        List<NameValuePair> queryParameters = new ArrayList<NameValuePair>(parameters.size());

        for (Entry<String, List<String>> parameterEntry : parameters.entrySet()) {
            for (String value : parameterEntry.getValue()) {
                logger.debug("setting query parameter: [" + parameterEntry.getKey() + ", " + value + "]");
                queryParameters.add(new BasicNameValuePair(parameterEntry.getKey(), value));
            }
        }

        HttpRequestBase httpMethod = null;
        HttpEntity httpEntity = null;
        URIBuilder uriBuilder = new URIBuilder(hostURI);

        // create the method
        if ("GET".equalsIgnoreCase(method)) {
            setQueryString(uriBuilder, queryParameters);
            httpMethod = new HttpGet(uriBuilder.build());
        } else if ("POST".equalsIgnoreCase(method)) {
            if (isMultipart) {
                logger.debug("setting multipart file content");
                setQueryString(uriBuilder, queryParameters);
                httpMethod = new HttpPost(uriBuilder.build());

                if (content instanceof String) {
                    FileUtils.writeStringToFile(tempFile, (String) content, contentType.getCharset(), false);
                } else {
                    FileUtils.writeByteArrayToFile(tempFile, (byte[]) content, false);
                }

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.addPart(tempFile.getName(), new FileBody(tempFile, contentType, tempFile.getName()));
                httpEntity = multipartEntityBuilder.build();
            } else if (StringUtils.startsWithIgnoreCase(contentType.getMimeType(), ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
                httpMethod = new HttpPost(uriBuilder.build());
                httpEntity = new UrlEncodedFormEntity(queryParameters, contentType.getCharset());
            } else {
                setQueryString(uriBuilder, queryParameters);
                httpMethod = new HttpPost(uriBuilder.build());

                if (content instanceof String) {
                    httpEntity = new StringEntity((String) content, contentType);
                } else {
                    httpEntity = new ByteArrayEntity((byte[]) content);
                }
            }
        } else if ("PUT".equalsIgnoreCase(method)) {
            if (StringUtils.startsWithIgnoreCase(contentType.getMimeType(), ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
                httpMethod = new HttpPut(uriBuilder.build());
                httpEntity = new UrlEncodedFormEntity(queryParameters, contentType.getCharset());
            } else {
                setQueryString(uriBuilder, queryParameters);
                httpMethod = new HttpPut(uriBuilder.build());

                if (content instanceof String) {
                    httpEntity = new StringEntity((String) content, contentType);
                } else {
                    httpEntity = new ByteArrayEntity((byte[]) content);
                }
            }
        } else if ("DELETE".equalsIgnoreCase(method)) {
            setQueryString(uriBuilder, queryParameters);
            httpMethod = new HttpDelete(uriBuilder.build());
        } else if ("PATCH".equalsIgnoreCase(method)) {
            if (StringUtils.startsWithIgnoreCase(contentType.getMimeType(), ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
                httpMethod = new HttpPatch(uriBuilder.build());
                httpEntity = new UrlEncodedFormEntity(queryParameters, contentType.getCharset());
            } else {
                setQueryString(uriBuilder, queryParameters);
                httpMethod = new HttpPatch(uriBuilder.build());

                if (content instanceof String) {
                    httpEntity = new StringEntity((String) content, contentType);
                } else {
                    httpEntity = new ByteArrayEntity((byte[]) content);
                }
            }
        }

        if (httpMethod instanceof HttpEntityEnclosingRequestBase) {
            // Compress the request entity if necessary
            List<String> contentEncodingList = (List<String>) new CaseInsensitiveMap(headers).get(HTTP.CONTENT_ENCODING);
            if (CollectionUtils.isNotEmpty(contentEncodingList)) {
                for (String contentEncoding : contentEncodingList) {
                    if (contentEncoding != null && (contentEncoding.equalsIgnoreCase("gzip") || contentEncoding.equalsIgnoreCase("x-gzip"))) {
                        httpEntity = new GzipCompressingEntity(httpEntity);
                        break;
                    }
                }
            }

            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(httpEntity);
        }

        // set the headers
        for (Entry<String, List<String>> headerEntry : headers.entrySet()) {
            for (String value : headerEntry.getValue()) {
                logger.debug("setting method header: [" + headerEntry.getKey() + ", " + value + "]");
                httpMethod.addHeader(headerEntry.getKey(), value);
            }
        }

        // Only set the Content-Type for entity-enclosing methods, but not if multipart is used
        if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) && !isMultipart) {
            httpMethod.setHeader(HTTP.CONTENT_TYPE, contentType.toString());
        }

        return httpMethod;
    }

    Map<String, List<String>> getHeaders(HttpDispatcherProperties httpDispatcherProperties, ConnectorMessage connectorMessage) {
        return HttpUtil.getTableMap(httpDispatcherProperties.isUseHeadersVariable(), httpDispatcherProperties.getHeadersVariable(), httpDispatcherProperties.getHeadersMap(), getMessageMaps(), connectorMessage);
    }

    Map<String, List<String>> getParameters(HttpDispatcherProperties httpDispatcherProperties, ConnectorMessage connectorMessage) {
        return HttpUtil.getTableMap(httpDispatcherProperties.isUseParametersVariable(), httpDispatcherProperties.getParametersVariable(), httpDispatcherProperties.getParametersMap(), getMessageMaps(), connectorMessage);
    }

    private void setQueryString(URIBuilder uriBuilder, List<NameValuePair> queryParameters) {
        if (queryParameters.size() > 0) {
            uriBuilder.setParameters(queryParameters);
        }
    }

    private class DynamicProxyRoutePlanner implements HttpRoutePlanner {
        @Override
        public HttpRoute determineRoute(final HttpHost target, final HttpRequest request, final HttpContext context) throws HttpException {
            HttpHost proxy = (HttpHost) context.getAttribute(PROXY_CONTEXT_KEY);
            boolean secure = target.getSchemeName().equals("https");

            if (proxy != null) {
                logger.debug("Using proxy: " + proxy.toString());
                return new HttpRoute(target, null, proxy, secure);
            }

            return new HttpRoute(target, null, secure);
        }
    }

    private void processDigestChallenge(AuthCache authCache, HttpHost target, Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
        Header authHeader = request.getFirstHeader("Authorization");
        /*
         * Since we're going to be replacing the header, we remove it here. If the header is invalid
         * or the challenge fails, we still want to remove the header, because otherwise it will
         * interfere with reactive authentication.
         */
        request.removeHeaders("Authorization");

        if (authHeader != null) {
            String authValue = authHeader.getValue();

            // The Authorization header value will be in the form: Digest param1="value1", param2="value2"
            if (StringUtils.startsWithIgnoreCase(authValue, AuthSchemes.DIGEST)) {
                DigestScheme digestScheme = new DigestScheme();

                // Get the actual parameters by stripping off the "Digest"
                authValue = StringUtils.removeStartIgnoreCase(authValue, AuthSchemes.DIGEST).trim();
                Matcher matcher = AUTH_HEADER_PATTERN.matcher(authValue);

                while (matcher.find()) {
                    // We found a param="value" group
                    String group = matcher.group();
                    int index = group.indexOf('=');
                    String name = group.substring(0, index).trim();
                    String value = group.substring(index + 1).trim();

                    // Strip off any quotes in the value
                    if (value.startsWith("\"")) {
                        value = value.substring(1);
                    }
                    if (value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 1);
                    }

                    logger.debug("Overriding Digest Parameter: " + name + "=\"" + value + "\"");
                    digestScheme.overrideParamter(name, value);
                }

                // Since this is preemptive, we need to actually process the challenge beforehand
                request.addHeader(digestScheme.authenticate(credentials, request, context));
                authCache.put(target, digestScheme);
            }
        }
    }

    private boolean isBinaryContentType(String binaryMimeTypes, ContentType contentType) {
        String mimeType = contentType.getMimeType();

        if (getConnectorProperties().isResponseBinaryMimeTypesRegex()) {
            Pattern binaryMimeTypesRegex = binaryMimeTypesRegexMap.get(binaryMimeTypes);

            if (binaryMimeTypesRegex == null) {
                try {
                    binaryMimeTypesRegex = Pattern.compile(binaryMimeTypes);

                    if (binaryMimeTypesRegexMap.size() >= MAX_MAP_SIZE) {
                        binaryMimeTypesRegexMap.clear();
                    }

                    binaryMimeTypesRegexMap.put(binaryMimeTypes, binaryMimeTypesRegex);
                } catch (PatternSyntaxException e) {
                    logger.warn("Invalid binary MIME types regular expression: " + binaryMimeTypes, e);
                    return false;
                }
            }

            return binaryMimeTypesRegex.matcher(mimeType).matches();
        } else {
            String[] binaryMimeTypesArray = binaryMimeTypesArrayMap.get(binaryMimeTypes);

            if (binaryMimeTypesArray == null) {
                binaryMimeTypesArray = StringUtils.split(binaryMimeTypes.replaceAll("\\s*,\\s*", ",").trim(), ',');

                if (binaryMimeTypesArrayMap.size() >= MAX_MAP_SIZE) {
                    binaryMimeTypesArrayMap.clear();
                }

                binaryMimeTypesArrayMap.put(binaryMimeTypes, binaryMimeTypesArray);
            }

            return StringUtils.startsWithAny(mimeType, binaryMimeTypesArray);
        }
    }

    @Override
    public HttpDispatcherProperties getConnectorProperties() {
        return (HttpDispatcherProperties) super.getConnectorProperties();
    }
}