package com.shiyatsu.lib.message.queue.consumer;

/**
 * Interface for a message queue consumer. This interface defines the essential operation for consuming messages from a queue.
 */
public interface IMessageQueueConsumer {

    /**
     * Receives a message object to be processed. Implementing classes should define how the messages are enqueued and processed.
     * 
     * @param o The message object to be received.
     * @return true if the message was successfully received (and presumably added to a queue for processing), false otherwise.
     */
    boolean receive(Object o);
    
}

