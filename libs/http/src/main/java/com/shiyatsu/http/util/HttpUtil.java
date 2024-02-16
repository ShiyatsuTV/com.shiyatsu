package com.shiyatsu.http.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.shiyatsu.http.enums.HttpConnectionTimeoutException;
import com.shiyatsu.http.enums.HttpMethod;
import com.shiyatsu.http.exception.HttpException;
import com.shiyatsu.http.response.WebServiceResponse;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

/**
 * A utility class for making simple HTTP requests and handling responses.
 * 
 * @author Shiyatsu
 */
public class HttpUtil {

    private static ILoggerService logger = LoggerService.getLoggingService();
    private static final HttpClient httpClient;

    /**
     * Initialize the static HttpClient with a 2-minute connection timeout.
     */
    static {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(120))
                .build();
    }

    private HttpUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
	 * Overloaded method for performing an HTTP request with a default UTF-8 charset.
	 *
	 * @param uri      The URI to send the request to.
	 * @param method   The HTTP method (GET, POST, etc.).
	 * @param headers  A map of HTTP headers.
	 * @param body     The request body as a string.
	 * @param timeout  An optional timeout duration for the request.
	 * @return A WebServiceResponse containing the HTTP response details.
	 * @throws HttpException if an error occurs during the HTTP request.
	 */
	public static WebServiceResponse doRequest(String uri, HttpMethod method, Map<String, String> headers, String body, Optional<Duration> timeout) throws HttpException {
	    return doRequest(uri, method, headers, body, StandardCharsets.UTF_8, timeout);
	}
	
	/**
     * Overloaded method for performing an HTTP request without a request body.
     *
     * @param uri      The URI to send the request to.
     * @param method   The HTTP method (GET, POST, etc.).
     * @param headers  A map of HTTP headers.
     * @param timeout  An optional timeout duration for the request.
     * @return A WebServiceResponse containing the HTTP response details.
     * @throws HttpException if an error occurs during the HTTP request.
     */
    public static WebServiceResponse doRequest(String uri, HttpMethod method, Map<String, String> headers, Optional<Duration> timeout) throws HttpException {
    	return doRequest(uri, method, headers, null, null, timeout);
    }

	/**
     * Perform an HTTP request with a specified URI, HTTP method, headers, request body, charset, and timeout.
     *
     * @param uri      The URI to send the request to.
     * @param method   The HTTP method (GET, POST, etc.).
     * @param headers  A map of HTTP headers.
     * @param body     The request body as a string.
     * @param charset  The character encoding for the request body.
     * @param timeout  An optional timeout duration for the request.
     * @return A WebServiceResponse containing the HTTP response details.
     * @throws HttpException if an error occurs during the HTTP request.
     */
    public static WebServiceResponse doRequest(String uri, HttpMethod method, Map<String, String> headers, String body, Charset charset, Optional<Duration> timeout) throws HttpException {
        try {
            return executeRequest(build(uri, method, headers, body != null ? body.getBytes(charset) : null, timeout));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error(HttpUtil.class, "Request Interrupted: Method=" + method + ", URI=" + uri, e);
            throw new HttpException("Request to " + uri + " was interrupted", e);
        } catch (HttpTimeoutException e) {
            logger.error(HttpUtil.class, "Timeout during request: Method=" + method + ", URI=" + uri, e);
            throw new HttpConnectionTimeoutException("Connection issue during request to " + uri, e);
        } catch (IOException e) {
            logger.error(HttpUtil.class, "IO Exception: Method=" + method + ", URI=" + uri, e);
            throw new HttpException("Connection issue during request to " + uri, e);
        } catch (URISyntaxException e) {
            logger.error(HttpUtil.class, "URI Syntax Error: Method=" + method + ", URI=" + uri, e);
            throw new HttpException("Invalid URI syntax for " + uri, e);
        }
    }

    /**
     * Build an HTTP request with a specified URI, HTTP method, headers, request body, and timeout.
     *
     * @param uri      The URI to send the request to.
     * @param method   The HTTP method (GET, POST, etc.).
     * @param headers  A map of HTTP headers.
     * @param body     The request body as a byte array.
     * @param timeout  An optional timeout duration for the request.
     * @return A HttpRequest.Builder instance for building the request.
     * @throws URISyntaxException     if the URI is invalid.
     * @throws IllegalArgumentException if the method or timeout is invalid.
     */
    public static Builder build(String uri, HttpMethod method, Map<String, String> headers, byte[] body, Optional<Duration> timeout) throws URISyntaxException, IllegalArgumentException {
    	Builder builder = HttpRequest.newBuilder().uri(new URI(uri));
        if (body != null && body.length > 0) {
            builder.method(method.getName(), HttpRequest.BodyPublishers.ofByteArray(body));
        } else {
            builder.method(method.getName(), HttpRequest.BodyPublishers.noBody());
        }
        if (headers != null) {
        	headers.forEach((key, value) -> builder.header(key, value));
        }
        timeout.ifPresent(t -> builder.timeout(t));
        return builder;
    }

    /**
     * Build an HTTP request with a specified URI, HTTP method, headers, and timeout.
     *
     * @param uri      The URI to send the request to.
     * @param method   The HTTP method (GET, POST, etc.).
     * @param headers  A map of HTTP headers.
     * @param timeout  An optional timeout duration for the request.
     * @return A HttpRequest.Builder instance for building the request.
     * @throws URISyntaxException     if the URI is invalid.
     * @throws IllegalArgumentException if the method or timeout is invalid.
     */
    public static Builder build(String uri, HttpMethod method, Map<String, String> headers, Optional<Duration> timeout) throws URISyntaxException, IllegalArgumentException {
        return build(uri, method, headers, null, timeout);
    }

    /**
     * Execute an HTTP request using the provided builder and return the response details.
     *
     * @param builder The HttpRequest.Builder instance representing the request.
     * @return A WebServiceResponse containing the HTTP response details.
     * @throws IOException          if an I/O error occurs during the request.
     * @throws InterruptedException if the request is interrupted.
     */
    public static WebServiceResponse executeRequest(Builder builder) throws IOException, InterruptedException {
        WebServiceResponse ret = new WebServiceResponse();
        long start = System.currentTimeMillis();
        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        long delta = System.currentTimeMillis() - start;
        ret.getHeaders().putAll(new HashMap<>(response.headers().map()));
        ret.setDateExecutionTime(start);
        ret.setExecutionTime(delta);
        ret.setResponse(response.body());
        ret.setStatusCode(response.statusCode());
        logger.info(HttpUtil.class, String.format("HTTP request to [%s] using method [%s] completed in %d ms with status code: %d", builder.build().uri().toString(), builder.build().method(), delta, response.statusCode()));
        logger.debug(HttpUtil.class, String.format("HTTP request to [%s] using method [%s] return : %s", builder.build().uri().toString(), builder.build().method(), ret.getResponse()));
        return ret;
    }
}
