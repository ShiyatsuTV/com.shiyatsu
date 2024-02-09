package com.shiyatsu.example.app.http;

import java.time.Duration;
import java.util.Optional;
import com.shiyatsu.example.app.App;
import com.shiyatsu.http.enums.HttpMethod;
import com.shiyatsu.http.exception.HttpException;
import com.shiyatsu.http.util.HttpUtil;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

public class AppHttp {

	private static final String URL = "https://jsonplaceholder.typicode.com/todos/1";
	private static final Duration TIMEOUT = Duration.ofSeconds(120);
	private static ILoggerService logger = LoggerService.getLoggingService();

	private AppHttp() {
		throw new IllegalStateException("Utility class");
	}
	
	public static void exec() {
		try {
			HttpUtil.doRequest(URL, HttpMethod.GET, null, Optional.of(TIMEOUT)); // Optional.empty()
		} catch (HttpException e) {
			logger.error(App.class, String.format("Fail to call url : %s, reason : %s", URL, e.getMessage()), null);
		}
	}
	
}
