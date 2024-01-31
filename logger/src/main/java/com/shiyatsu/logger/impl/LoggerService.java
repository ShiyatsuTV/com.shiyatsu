package com.shiyatsu.logger.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import com.shiyatsu.logger.ILoggerService;

/**
 * LoggerService is a singleton class that provides logging functionalities.
 * It implements the ILoggerService interface and utilizes Log4j for logging.
 * 
 * @author Shiyatsu
 */
public class LoggerService implements ILoggerService {

    // A set to store logger names.
    private static final Set<String> loggersName = new TreeSet<>();
    
    // A map to store Logger instances, keyed by class names.
    private static final Map<String, Logger> LOGGERS = new HashMap<>();

    // A lock object for implementing thread-safe singleton.
    private static final Object lock = new Object();
    
    // The singleton instance of LoggerService.
    private static LoggerService instance = new LoggerService();

    /**
     * Returns the singleton instance of LoggerService.
     * Implements double-checked locking to ensure thread safety.
     *
     * @return ILoggerService instance.
     */
    public static ILoggerService getLoggingService() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoggerService();
                }
            }
        }
        return instance;
    }

    /**
     * Adds a logger name to the set of logger names.
     *
     * @param logger The name of the logger to be added.
     */
    public static void addLoggerName(String logger) {
        LoggerService.loggersName.add(logger);
    }

    /**
     * Retrieves or creates a Logger instance for the given class name.
     *
     * @param className The name of the class for which the logger is requested.
     * @return Logger instance corresponding to the className.
     */
    public static Logger getLogger(String className) {
        Logger logService = LOGGERS.get(className);
        if (logService == null) {
            logService = (Logger) LogManager.getLogger(className);
            LOGGERS.put(className, logService);
        }
        return logService;
    }

    // Log methods for different log levels and parameters, delegating to the private log method.

    @Override
    public void error(Object theObject, String message, Throwable throwable) {
        log(Level.ERROR, theObject, message, throwable);
    }

    @Override
    public void error(Class<?> theClass, String message, Throwable throwable) {
        log(Level.ERROR, theClass, message, throwable);
    }

    @Override
    public void info(Object theObject, String message) {
        log(Level.INFO, theObject, message);
    }

    @Override
    public void info(Class<?> theClass, String message) {
        log(Level.INFO, theClass, message);
    }

    @Override
    public void debug(Object theObject, String message) {
        log(Level.DEBUG, theObject, message);
    }

    @Override
    public void debug(Class<?> theClass, String message) {
        log(Level.DEBUG, theClass, message);
    }

    @Override
    public void trace(Object theObject, String message) {
        log(Level.TRACE, theObject, message);
    }

    @Override
    public void trace(Class<?> theClass, String message) {
        log(Level.TRACE, theClass, message);
    }

    @Override
    public void warning(Object theObject, String message) {
        log(Level.WARN, theObject, message);
    }

    @Override
    public void warning(Class<?> theClass, String message) {
        log(Level.WARN, theClass, message);
    }

    /**
     * Updates the log level of all loggers to the specified level.
     *
     * @param level The new level to be set for all loggers.
     */
    @Override
    public void updateAllLogLevel(Level level) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration conf = context.getConfiguration();
        conf.getLoggers().values().forEach(lc -> lc.setLevel(level));
        context.updateLoggers();
    }

    /**
     * Updates the log level of loggers in the loggersName set to the specified level.
     *
     * @param level The new level to be set for selected loggers.
     */
    @Override
    public void updateLogLevel(Level level) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration conf = context.getConfiguration();
        conf.getLoggers().values().forEach(lc -> {
            if (loggersName.contains(lc.getName())) {
                lc.setLevel(level);
            }
        });
        context.updateLoggers();
    }
    
    /**
     * Private helper method for logging.
     * Handles logging for a given level, object/class, message, and optional throwable.
     *
     * @param level The log level.
     * @param theClass The class from which the log is made.
     * @param message The message to log.
     * @param throwable The throwable to log (can be null).
     */
    private void log(Level level, Class<?> theClass, String message, Throwable throwable) {
        Logger logger = getLogger(theClass.getName());
        if (throwable == null) {
            logger.log(level, message);
        } else {
            logger.log(level, message, throwable);
        }
    }
    
    private void log(Level level, Object theObject, String message, Throwable throwable) {
        log(level, theObject.getClass(), message, throwable);
    }
    
    private void log(Level level, Object theObject, String message) {
        log(level, theObject.getClass(), message, null);
    }
    
    private void log(Level level, Class<?> theClass, String message) {
        log(level, theClass, message, null);
    }
}
