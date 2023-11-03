package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsumerManager {
    private final Buffer buffer;
    private final List<Consumer> consumerThreads;
    private final Log logger = Log.getInstance();

    public ConsumerManager(Buffer buffer) {
        this.buffer = buffer;
        this.consumerThreads = new ArrayList<>();
    }

    public void initializeConsumers() {
        int numOfConsumers = new Random().nextInt(13) + 3;
        for (int i = 0; i < numOfConsumers; i++) {
            addConsumer();
        }
        logger.log("Total number of consumers: " + consumerThreads.size());
    }

    public void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        consumerThreads.add(consumer);
        consumer.start();
    }

    public List<Consumer> getConsumers() {
        return consumerThreads;
    }
}
