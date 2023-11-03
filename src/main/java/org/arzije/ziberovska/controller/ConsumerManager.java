package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ConsumerManager manages multiple consumers that retrieve items from the buffer.
 * It provides functionalities to initialize a set of consumers, add new consumers,
 * and manage the lifecycle of the consumer threads.
 */

public class ConsumerManager {
    private final Buffer buffer;
    private final List<Consumer> consumerThreads;
    private final Log logger = Log.getInstance();

    /**
     * Constructor for ConsumerManager.
     *
     * @param buffer The buffer from which the consumers will consume.
     */
    public ConsumerManager(Buffer buffer) {
        this.buffer = buffer;
        this.consumerThreads = new ArrayList<>();
    }

    /**
     * Initializes a random number of consumers between 3 and 15 inclusive.
     */
    public void initializeConsumers() {
        int numOfConsumers = new Random().nextInt(13) + 3;
        for (int i = 0; i < numOfConsumers; i++) {
            addConsumer();
        }
        logger.log("Total number of consumers: " + consumerThreads.size());
    }

    /**
     * Adds a new consumer and starts its execution.
     */
    public void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        consumerThreads.add(consumer);
        consumer.start();
    }

    /**
     * Returns the list of consumers.
     *
     * @return List of Consumer objects.
     */
    public List<Consumer> getConsumers() {
        return consumerThreads;
    }
}
