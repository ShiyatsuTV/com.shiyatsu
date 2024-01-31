package com.shiyatsu.http.enums;

import com.shiyatsu.http.exception.HttpException;

public class HttpConnectionException extends HttpException {

	public HttpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
