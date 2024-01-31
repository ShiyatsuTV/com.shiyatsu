package com.shiyatsu.http.enums;

import com.shiyatsu.http.exception.HttpException;

public class HttpConnectionTimeoutException extends HttpException {
	
	public HttpConnectionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
