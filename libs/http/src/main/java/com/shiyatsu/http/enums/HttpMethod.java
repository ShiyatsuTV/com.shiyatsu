package com.shiyatsu.http.enums;

public enum HttpMethod {
	GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT");
	
	private String name;
	
	HttpMethod(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
