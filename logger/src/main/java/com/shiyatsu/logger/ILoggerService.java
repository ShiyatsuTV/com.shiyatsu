package com.shiyatsu.logger;

import org.apache.logging.log4j.Level;

public interface ILoggerService {

	void error(Object theObject, String message, Throwable throwable);

    void error(Class<?> theObject, String message, Throwable throwable);

    void info(Object theObject, String message);

    void info(Class<?> className, String message);

    void debug(Object theObject, String message);

    void debug(Class<?> theObject, String message);

    void warning(Object theObject, String message);

    void warning(Class<?> theObject, String message);

    void trace(Object theObject, String message);

    void trace(Class<?> theObject, String message);

    void updateAllLogLevel(Level level);
    
    void updateLogLevel(Level level);
	
}
