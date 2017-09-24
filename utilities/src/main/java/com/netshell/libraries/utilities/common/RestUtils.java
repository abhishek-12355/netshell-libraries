package com.netshell.libraries.utilities.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Abhishek Shekhar
 * @since 08/01/2017
 */
public final class RestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);

    /**
     * It contains the configuration parameters for REST request and response
     *
     * @param restContext context which will be used to send and parse request.
     * @return {@link ResponseMessage} containing the response message
     * @throws RuntimeException if any error occurs.
     */
    public static ResponseMessage send(final RestContext restContext) {
        final long startTime = System.currentTimeMillis();
        LOGGER.trace("Invoking send with URL {0} {1}", restContext.getMethod(), restContext.getRequestURL());
        HttpURLConnection connection = null;
        try {
            URL url = new URL(restContext.getRequestURL());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(restContext.getMethod());
            connection.setDoOutput(restContext.isDoOutput());
            for (Map.Entry<String, String> header : restContext.getRequestHeaders().entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            connection.setConnectTimeout(restContext.getConnectionTimeout());
            connection.setReadTimeout(restContext.getReadTimeout());

            if (!CommonMethods.isEmpty(restContext.getRequestMessageString())) {
                try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
                    out.write(restContext.getRequestMessageString());
                }
            }
            return handleResponse(restContext, connection);
        } catch (IOException e) {
            final long duration = System.currentTimeMillis() - startTime;
            final String message = createExceptionString(restContext, "duration: %s" + Duration.ofMillis(duration).toString());
            throw new RuntimeException(message, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static ResponseMessage handleResponse(final RestContext restContext, final HttpURLConnection connection) throws IOException {
        return handleResponseImpl(restContext, connection, restContext.getRetryCount() - 1);
    }

    private static ResponseMessage handleResponseImpl(final RestContext restContext, final HttpURLConnection connection, final int attempt)
            throws IOException {
        Status status = Status.fromStatusCode(connection.getResponseCode());
        if (restContext.getExpectedStatus() == null && !Family.SUCCESSFUL.equals(status.getFamily()) ||
                restContext.getExpectedStatus() != null && !status.equals(restContext.getExpectedStatus())) {
            if (attempt <= 0) {
                LOGGER.warn("Number of attempts exhausted. Request failed");
                try (InputStream err = connection.getErrorStream()) {
                    final String message = read(err);
                    LOGGER.error(message);
                    throw new RuntimeException(createExceptionString(restContext, message));
                }
            }

            try {
                LOGGER.debug("Attempt {0} Failed with status {1}. Sleeping for {2} milliseconds", attempt, status, restContext.getRetryInterval());
                Thread.sleep(restContext.getRetryInterval());
            } catch (InterruptedException e) {
                // ignore
                LOGGER.error(e.getMessage(), e);
            }

            return handleResponseImpl(restContext, connection, attempt - 1);
        } else {
            LOGGER.debug("Attempt {0} was successful with status {1}", attempt, status);
            ResponseMessage message = new ResponseMessage();
            try (InputStream in = Family.SUCCESSFUL.equals(status.getFamily()) ? connection.getInputStream() : connection.getErrorStream()) {
                message.setResponse(read(in));
            }
            message.setStatusText(connection.getResponseMessage());
            message.setStatus(status);
            message.setHeaders(new HashMap<>(connection.getHeaderFields()));
            return message;
        }
    }

    private static String createExceptionString(final RestContext restContext, final String message) {
        return String.format("URI: %s; Method: %s; message: %s", restContext.getRequestURL(), restContext.getMethod(), message);
    }

    private static String read(final InputStream inputStream) throws IOException {

        if (inputStream == null) {
            LOGGER.debug("inputStream is null. Returning empty string");
            return "";
        }

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtils.inputToOutputStream(inputStream, outputStream);
            return outputStream.toString();
        }
    }

    public static final class ResponseMessage {
        private Map<String, List<String>> headers = Collections.emptyMap();
        private String response;
        private Response.Status status;
        private String statusText;

        private ResponseMessage() {
        }

        /**
         * @return Map of headers
         */
        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        void setHeaders(final Map<String, List<String>> headers) {
            this.headers = headers;
        }

        /**
         * @return response message as string
         */
        public String getResponse() {
            return response;
        }

        void setResponse(final String response) {
            this.response = response;
        }

        /**
         * @param tClass class object.
         * @param <T>    Type of Class in which message should be parsed into
         * @return response message as T object
         * @throws IOException
         */
        public <T> T getResponseObject(final Class<T> tClass) throws IOException {
            return JsonUtils.readValue(this.getResponse(), tClass);
        }

        /**
         * @param ttTypeReference class object.
         * @param <T>             Type of Class in which message should be parsed into
         * @return response message as T object
         * @throws IOException
         */
        public <T> T getResponseObject(final TypeReference<T> ttTypeReference) throws IOException {
            return JsonUtils.readValue(this.getResponse(), ttTypeReference);
        }

        /**
         * @return the status of the response
         */
        public Response.Status getStatus() {
            return status;
        }

        void setStatus(final Response.Status status) {
            this.status = status;
        }

        /**
         * @return the status text of the response
         */
        public String getStatusText() {
            return statusText;
        }

        void setStatusText(final String statusText) {
            this.statusText = statusText;
        }

        /**
         * if key is not present it will return null. In other cases it would return the first value from the list.
         *
         * @param key of which header is desired
         * @return header value corresponding to the key
         * @see {@link ResponseMessage#getHeaderList(String)}
         */
        public String getHeader(final String key) {
            final List<String> list = headers.get(key);
            return list == null ? null : list.size() == 1 ? list.get(0) : list.toString();
        }

        /**
         * @param key for which header is desired.
         * @return List of headers associated with the key.
         */
        public List<String> getHeaderList(final String key) {
            return headers.get(key);
        }

        /**
         * It returns if the status of the response is success.
         *
         * @return true if the {@literal status.getFamilty()} is {@link Family#SUCCESSFUL}
         */
        public boolean isSuccess() {
            return status != null && status.getFamily() == Response.Status.Family.SUCCESSFUL;
        }
    }

    /**
     * RestContext class stores the information required to make a connection to the server and
     * send the request to it. It also supports number of times the request should be tried before failing.
     */
    public static final class RestContext {

        private final String requestURL;
        private int connectionTimeout = 5_000;
        private boolean doOutput = true;
        private int readTimeout = 5_000;
        private String requestMessageString;
        private String method = "POST";
        private Response.Status expectedStatus;
        private int retryCount = 1;
        private long retryInterval = 1000;
        private Map<String, String> requestHeaders = new HashMap<>();

        private RestContext(final String requestURL) {
            this.requestURL = requestURL;
        }

        public long getRetryInterval() {
            return retryInterval;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public Map<String, String> getRequestHeaders() {
            return requestHeaders;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public String getRequestMessageString() {
            return requestMessageString;
        }

        public String getRequestURL() {
            return requestURL;
        }

        public boolean isDoOutput() {
            return doOutput;
        }

        public Response.Status getExpectedStatus() {
            return expectedStatus;
        }

        public String getMethod() {
            return method;
        }

        public int getRetryCount() {
            return retryCount;
        }

    }

    /**
     * RestContextBuilder gives a handy way to create {@link RestContext}
     */
    public static final class RestContextBuilder {
        private final RestContext restContext;

        private Supplier<String> authorizationSupplier;

        /**
         * Cretae RestContext. {@literal requestUrl} is a mandatory field
         *
         * @param requestUrl The Url which would be used to send the REST request.
         */
        public RestContextBuilder(final String requestUrl) {
            this.restContext = new RestContext(requestUrl);
        }

        /**
         * Connection Timeout which will be used in the URLConnection
         *
         * @param timeout timeout value
         * @return {@link RestContextBuilder}
         */
        public RestContextBuilder withConnectionTimeout(final int timeout) {
            restContext.connectionTimeout = timeout;
            return this;
        }

        /**
         * Adds a Content-Type header with value "application/json"
         *
         * @return {@link RestContextBuilder}
         */
        public RestContextBuilder withContentTypeJson() {
            return withHeader("Content-Type", "application/json");
        }

        /**
         * Adds a Content-Type header with value "application/xml"
         *
         * @return {@link RestContextBuilder}
         */
        public RestContextBuilder withContentTypeApplicationXml() {
            return withHeader("Content-Type", "application/xml");
        }

        /**
         * Adds a Content-Type header with value {@literal contentType}
         *
         * @return {@link RestContextBuilder}
         */
        public RestContextBuilder withContentType(final String contentType) {
            return withHeader("Content-Type", contentType);
        }

        /**
         * Adds a Authorization header with value {@literal authorization}
         *
         * @return {@link RestContextBuilder}
         */
        public RestContextBuilder withAuthorization(final String authorization) {
            return withHeader("Authorization", authorization);
        }

        public RestContextBuilder withAuthorization(final Supplier<String> authorization) {
            this.authorizationSupplier = authorization;
            return this;
        }

        public RestContextBuilder withHeader(final String key, final String value) {
            restContext.requestHeaders.put(key, value);
            return this;
        }

        public RestContextBuilder withDoOutput(final boolean doOutput) {
            restContext.doOutput = doOutput;
            return this;
        }

        public RestContextBuilder withReadTimeout(final int readTimeout) {
            restContext.readTimeout = readTimeout;
            return this;
        }

        public RestContextBuilder withExpectedStatus(final Response.Status expectedStatus) {
            restContext.expectedStatus = expectedStatus;
            return this;
        }

        public RestContextBuilder withExpectedStatus(final int expectedStatus) {
            restContext.expectedStatus = Status.fromStatusCode(expectedStatus);
            return this;
        }

        public RestContextBuilder withRequestMessageString(final String requestMessageString) {
            restContext.requestMessageString = requestMessageString;
            return this;
        }

        public RestContextBuilder withRequestMessageJSONObject(final Object requestMessage) throws JsonProcessingException {
            restContext.requestMessageString = JsonUtils.writeValueAsString(requestMessage);
            return this;
        }

        public RestContextBuilder withMethod(final String method) {
            restContext.method = method;
            return this;
        }

        public RestContextBuilder withRetryCount(final int retryCount) {
            restContext.retryCount = retryCount;
            return this;
        }

        public RestContextBuilder withRetryInterval(final long retryInterval) {
            restContext.retryInterval = retryInterval;
            return this;
        }

        /**
         * Build RestContext.
         *
         * @return {@link RestContext}
         */
        public RestContext build() {
            if (this.authorizationSupplier != null) {
                this.withAuthorization(this.authorizationSupplier.get());
            }
            return restContext;
        }
    }
}
