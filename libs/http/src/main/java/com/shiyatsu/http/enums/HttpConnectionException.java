package com.shiyatsu.http.enums;

import com.shiyatsu.http.exception.HttpException;

public class HttpConnectionException extends HttpException {

	private static final long serialVersionUID = 373226746143245536L;

	public HttpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
