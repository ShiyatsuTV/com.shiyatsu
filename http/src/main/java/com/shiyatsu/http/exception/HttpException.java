package com.shiyatsu.http.exception;

/**
 * Custom exception class for handling HTTP related exceptions.
 */
public class HttpException extends Exception {

    /**
     * Constructor for HttpException with a message.
     *
     * @param message The error message.
     */
    public HttpException(String message) {
        super(message);
    }

    /**
     * Constructor for HttpException with a message and cause.
     *
     * @param message The error message.
     * @param cause   The cause of this exception.
     */
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for HttpException with a cause.
     *
     * @param cause The cause of this exception.
     */
    public HttpException(Throwable cause) {
        super(cause);
    }
}
