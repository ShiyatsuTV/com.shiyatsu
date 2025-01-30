package com.shiyatsu.ssl.exception;

public class SslException extends Exception {

    public SslException(String message) {
        super(message);
    }

    public SslException(String message, Exception e) {
        super(message, e);
    }

}
