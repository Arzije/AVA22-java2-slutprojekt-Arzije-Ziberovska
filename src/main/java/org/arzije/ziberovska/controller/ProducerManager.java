package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Producer;

import java.util.ArrayList;
import java.util.List;

public class ProducerManager {
    private final Buffer buffer;
    private final List<Producer> producerThreads;
    private final Log logger = Log.getInstance();

    public ProducerManager(Buffer buffer) {
        this.buffer = buffer;
        this.producerThreads = new ArrayList<>();
    }

    public void addProducer() {
        Producer producer = new Producer(buffer);
        producerThreads.add(producer);
        producer.start();
        logger.log("Producer added. Current number of producers: " + producerThreads.size());
    }

    public void removeProducer() {
        if (!producerThreads.isEmpty()) {
            Producer lastProducer = producerThreads.remove(producerThreads.size() - 1);
            if (lastProducer.getThread().isAlive()) {
                lastProducer.stop();
            }
            logger.log("Producer removed. Current number of producers: " + producerThreads.size());
        }
    }

    public List<Producer> getProducers() {
        return producerThreads;
    }
}
