package com.shiyatsu.lib.db.handler.exception;

public class DatabaseException extends Exception {

    private static final long serialVersionUID = 7410130514081038170L;

    public DatabaseException(String message, Exception e) {
        super(message, e);
    }

}
