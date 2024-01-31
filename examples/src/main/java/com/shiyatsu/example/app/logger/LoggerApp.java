package com.shiyatsu.example.app.logger;

import org.apache.logging.log4j.Level;
import com.shiyatsu.example.app.http.AppHttp;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

public class LoggerApp {

	private static ILoggerService logger = LoggerService.getLoggingService();
	
	private LoggerApp() {
		throw new IllegalStateException("Utility class");
	}
	
	public static void exec() {
		LoggerService.addLoggerName("com.shiyatsu");
		AppHttp.exec();
		logger.info(LoggerApp.class, "Change log level to DEBUG for all com.shiyatsu");
		logger.updateLogLevel(Level.DEBUG);
		AppHttp.exec();
	}
	
}
