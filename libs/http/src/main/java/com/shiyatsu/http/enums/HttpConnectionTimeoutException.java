package com.shiyatsu.http.enums;

import com.shiyatsu.http.exception.HttpException;

public class HttpConnectionTimeoutException extends HttpException {
	
	private static final long serialVersionUID = 3847921620257204754L;

	public HttpConnectionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
