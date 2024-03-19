package com.shiyatsu.cipher.exception;

public class CipherException extends Exception {

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Exception e) {
        super(message, e);
    }

}
