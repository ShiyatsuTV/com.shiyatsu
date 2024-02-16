package com.shiyatsu.http.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServiceResponse {

	private int statusCode;
	private String response;
	private final Map<String, List<String>> headers = new HashMap<>();
	private long executionTime;
	private long dateExecutionTime;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public long getDateExecutionTime() {
		return dateExecutionTime;
	}

	public void setDateExecutionTime(long dateExecutionTime) {
		this.dateExecutionTime = dateExecutionTime;
	}

}
