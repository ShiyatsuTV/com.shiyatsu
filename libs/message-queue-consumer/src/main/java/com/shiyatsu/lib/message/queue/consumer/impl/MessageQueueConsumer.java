package com.shiyatsu.lib.message.queue.consumer.impl;

import java.util.concurrent.LinkedBlockingQueue;

import com.shiyatsu.lib.message.queue.consumer.IMessageQueueConsumer;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

/**
 * Abstract class representing a message queue consumer. This class implements the {@link IMessageQueueConsumer} interface
 * to provide basic functionality for consuming messages from a queue. It uses a {@link LinkedBlockingQueue} to hold the messages
 * and a separate thread to process them.
 */
abstract class MessageQueueConsumer implements IMessageQueueConsumer {

    /**
     * Logger service for logging information, warnings, and errors.
     */
    private static ILoggerService logger = LoggerService.getLoggingService();
    
    /**
     * Queue holding the messages to be processed.
     */
    private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    
    /**
     * Thread that reads and processes messages from the queue.
     */
    private Thread reader = null;
    
    /**
     * Flag indicating whether the consumer is running and should continue processing messages.
     */
    private boolean isRunning = true;

    /**
     * Constructor for the message queue consumer. It initializes and starts a new thread to read and process messages.
     */
    MessageQueueConsumer() {
        reader = new Thread(new Reader());
        reader.setName("MessageQueueConsumer reader");
        reader.start();
    }
    
    /**
     * Private inner class implementing {@link Runnable} to read and process messages from the queue in a separate thread.
     */
    private class Reader implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    logger.info(MessageQueueConsumer.class, "Waiting message to process...");
                    Object o = queue.take();
                    process(o);
                } catch (InterruptedException interruptedException) {
                    logger.error(MessageQueueConsumer.class, "Reader interrupted", interruptedException);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    logger.error(MessageQueueConsumer.class, "Error processing message: " + e.getMessage(), e);
                }
            } while (isRunning);
            reader.interrupt();
        }
    }
    
    /**
     * Receives an object to be added to the queue for processing.
     * 
     * @param o The object to be processed.
     * @return true if the object was successfully added to the queue, false otherwise.
     */
    @Override
    public boolean receive(Object o) {
        return queue.offer(o);
    }
    
    /**
     * Abstract method to process a message. Implementations of this class must provide an implementation of this method
     * to define how messages are processed.
     * 
     * @param wsm The message object to be processed.
     */
    abstract void process(Object wsm);
    
    /**
     * Stops the message queue consumer from processing messages. This will set the running flag to false, causing the 
     * message processing loop to terminate.
     */
    protected void stop() {
        isRunning = false;
    }

}

