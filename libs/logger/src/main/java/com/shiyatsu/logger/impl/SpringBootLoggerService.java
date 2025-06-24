package com.shiyatsu.logger.impl;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shiyatsu.logger.ILoggerService;

public class SpringBootLoggerService implements ILoggerService {

	private Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	@Override
	public void error(Object theObject, String message, Throwable throwable) {
		getLogger(theObject.getClass()).error(message, throwable);
	}

	@Override
	public void error(Class<?> theClass, String message, Throwable throwable) {
		getLogger(theClass).error(message, throwable);
	}

	@Override
	public void info(Object theObject, String message) {
		getLogger(theObject.getClass()).info(message);
	}

	@Override
	public void info(Class<?> theClass, String message) {
		getLogger(theClass).info(message);
	}

	@Override
	public void debug(Object theObject, String message) {
		getLogger(theObject.getClass()).debug(message);
	}

	@Override
	public void debug(Class<?> theClass, String message) {
		getLogger(theClass).debug(message);
	}

	@Override
	public void trace(Object theObject, String message) {
		getLogger(theObject.getClass()).trace(message);
	}

	@Override
	public void trace(Class<?> theClass, String message) {
		getLogger(theClass).trace(message);
	}

	@Override
	public void warning(Object theObject, String message) {
		getLogger(theObject.getClass()).warn(message);
	}

	@Override
	public void warning(Class<?> theClass, String message) {
		getLogger(theClass).warn(message);
	}

	@Override
	public void updateAllLogLevel(Level level) {
		// Non supporté avec SLF4J pur, mais tu peux logger un warning si tu veux
		// notifier l'utilisateur.
	}

	@Override
	public void updateLogLevel(Level level) {
		// Non supporté avec SLF4J pur.
	}
}
