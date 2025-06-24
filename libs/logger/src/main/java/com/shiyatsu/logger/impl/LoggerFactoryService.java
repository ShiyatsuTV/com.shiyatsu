package com.shiyatsu.logger.impl;

import com.shiyatsu.logger.ILoggerService;

public class LoggerFactoryService {

	public enum LoggerType {
		LOG4J, SPRING_BOOT
	}

	public static ILoggerService getLoggerService(LoggerType type) {
		if (type == LoggerType.SPRING_BOOT) {
			return new SpringBootLoggerService();
		} else {
			return LoggerService.getLoggingService();
		}
	}

}
