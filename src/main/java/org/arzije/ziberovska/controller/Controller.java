package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.model.*;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import java.util.*;

public class Controller {
    private Buffer buffer;
    private GUI gui;
    private List<Thread> producerThreads;
    private List<Thread> consumerThreads;
    private Log logger = Log.getInstance();

    public Controller() {
        buffer = new Buffer(100);
        gui = new GUI(this);

        producerThreads = new ArrayList<>();
        consumerThreads = new ArrayList<>();

        initConsumers();
        buffer.addObserver(gui);

        startBufferMonitoring();
//        setupLogGuiCallback();
    }

    private void initConsumers() {
        Random random = new Random();

        int numOfConsumers = random.nextInt(13) + 3; // Generates a random number between 3 and 15

        for (int i = 0; i < numOfConsumers; i++) {
            addConsumer();
        }
        logger.log("Total number of workers: " + consumerThreads.size());
    }

    public void handleProducerButtonClick() {
        addProducer();
    }

    public void handleConsumerButtonClick() {
        removeProducer();
    }

    private void addProducer() {
        Producer producer = new Producer(buffer);
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
        }
    }

    private void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        Thread consumerThread = new Thread(consumer);
        consumerThreads.add(consumerThread);
        consumerThread.start();
    }

    public int getBufferSize() {
        return buffer.size();
    }

    private void startBufferMonitoring() {
        Queue<Integer> recentBufferSizes = new LinkedList<>();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                recentBufferSizes.add(buffer.size());
                if (recentBufferSizes.size() > 10) {
                    recentBufferSizes.poll();
                }

                // Calculate the average
                int sum = 0;
                for (int size : recentBufferSizes) {
                    sum += size;
                }
                double average = sum / (double) recentBufferSizes.size();

                logger.log("Average buffer size over the last 10 seconds: " + average);

                int highThreshold = (int) (0.9 * 100); // 90% av maxkapaciteten
                int lowThreshold = (int) (0.1 * 100); // 10% av maxkapaciteten

                if (buffer.size() >= highThreshold) {
                    logger.log("Buffer size is now high at " + buffer.size() + " items");
                } else if (buffer.size() <= lowThreshold) {
                    logger.log("Buffer size is now low at " + buffer.size() + " items");
                }
            }
        }, 0, 10 * 1000);
    }

//    private void setupLogGuiCallback() {
//        logger.setGuiCallback(message -> gui.log(message));
//    }

    public void handleClearLogButtonClick() {
        logger.clearLogFile();
    }
}

