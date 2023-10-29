package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.model.*;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
    private Buffer buffer;
    private GUI gui;
    private List<Thread> producerThreads;
    private List<Thread> consumerThreads;
    private Log logger = Log.getInstance();

    public Controller() {
        buffer = new Buffer();
        gui = new GUI(this);

        producerThreads = new ArrayList<>();
        consumerThreads = new ArrayList<>();

        initProducersAndConsumers();
        buffer.addObserver(gui);
    }

    private void initProducersAndConsumers() {
        int numOfProducers = 8;
        for (int i = 0; i < numOfProducers; i++) {
            addProducer();
        }
//        log("Producers added: " + numOfProducers);
        logger.log("Producers added: " + numOfProducers);

        Random random = new Random();
        int numOfConsumers = random.nextInt(6) + 5;
        for (int i = 0; i < numOfConsumers; i++) {
            addConsumer();
        }
//        log("Consumers added: " + numOfConsumers);
        logger.log("Consumers added: " + numOfConsumers);
    }

    public void handleProducerButtonClick() {
        addProducer();
    }

    public void handleConsumerButtonClick() {
        removeProducer();
    }

    private void addProducer() {
        Producer producer = new Producer(buffer, gui);
        Thread thread = new Thread(producer);
        producerThreads.add(thread);
        thread.start();
        logger.log("Producer added. Current number of producers: " + producerThreads.size());
    }

    private void removeProducer() {
        if (!producerThreads.isEmpty()) {
            Thread lastProducer = producerThreads.remove(producerThreads.size() - 1);
            lastProducer.interrupt();
            logger.log("Producer removed. Current number of producers: " + producerThreads.size());
        } else {
            logger.log("No producer to remove.");
        }
    }

    private void addConsumer(){
        Consumer consumer = new Consumer(buffer, gui);
        Thread consumerThread = new Thread(consumer);
        consumerThreads.add(consumerThread);
        consumerThread.start();
//    log("Consumers added: " + numOfConsumers);
        logger.log("Producer removed. Current number of producers: " + consumerThreads.size());
    }

    public int getBufferSize() {
        return buffer.size();
    }
}

