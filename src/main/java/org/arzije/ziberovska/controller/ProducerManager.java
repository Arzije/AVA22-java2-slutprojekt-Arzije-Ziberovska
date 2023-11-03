package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Producer;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the producer threads in the application.
 */
public class ProducerManager {
    private final Buffer buffer;
    private final List<Producer> producerThreads;
    private final Log logger = Log.getInstance();

    /**
     * Constructor initializing the buffer and producer list.
     * @param buffer Shared buffer between producers and consumers.
     */
    public ProducerManager(Buffer buffer) {
        this.buffer = buffer;
        this.producerThreads = new ArrayList<>();
    }

    /**
     * Adds a new producer thread and starts it.
     */
    public void addProducer() {
        Producer producer = new Producer(buffer);
        producerThreads.add(producer);
        producer.start();
        logger.log("Producer added. Current number of producers: " + producerThreads.size());
    }

    /**
     * Removes the last producer thread from the list and stops it.
     */
    public void removeProducer() {
        if (!producerThreads.isEmpty()) {
            Producer lastProducer = producerThreads.remove(producerThreads.size() - 1);
            if (lastProducer.getThread().isAlive()) {
                lastProducer.stop();
            }
            logger.log("Producer removed. Current number of producers: " + producerThreads.size());
        }
    }

    /**
     * Returns the list of active producer threads.
     */
    public List<Producer> getProducers() {
        return producerThreads;
    }
}
