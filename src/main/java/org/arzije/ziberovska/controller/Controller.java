package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.model.*;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.states.AppState;
import org.arzije.ziberovska.view.GUI;

import java.io.*;
import java.util.*;

public class Controller {
    private Buffer buffer;
    private GUI gui;
    private final List<Producer> producerThreads;
    private final List<Consumer> consumerThreads;
    private final Log logger = Log.getInstance();


    public Controller() {
        buffer = new Buffer(100);

        producerThreads = new ArrayList<>();
        consumerThreads = new ArrayList<>();
    }

    public void initGUI(){
        gui = new GUI(this);
        buffer.addObserver(gui);
        logger.addObserver(gui);
        startBufferMonitoring();
    }

    public void initAfterGUI() {
        initConsumers();
    }

    private void initConsumers() {
        int numOfConsumers = new Random().nextInt(13) + 3;

        for (int i = 0; i < numOfConsumers; i++) {
            addConsumer();
        }
        logger.log("Total number of consumers: " + consumerThreads.size());
    }

    public void handleProducerButtonClick() {
        addProducer();
    }

    public void handleRemoveProducerButtonClick() {
        removeProducer();
    }

    private void addProducer() {
        Producer producer = new Producer(buffer);
        producerThreads.add(producer);
        producer.start();
        logger.log("Producer added. Current number of producers: " + producerThreads.size());
    }


    private void removeProducer() {
        if (!producerThreads.isEmpty()) {
            Producer lastProducer = producerThreads.remove(producerThreads.size() - 1);
            if (lastProducer.getThread().isAlive()) {
                lastProducer.stop();
            }
            logger.log("Producer removed. Current number of producers: " + producerThreads.size());
        }
    }

    private void addConsumer() {
        Consumer consumer = new Consumer(buffer);
        consumerThreads.add(consumer);
        consumer.start();
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

                int sum = 0;
                for (int size : recentBufferSizes) {
                    sum += size;
                }
                double averageBufferSize = sum / (double) recentBufferSizes.size();

                logger.log("Average buffer size over the last 10 seconds: " + averageBufferSize);

                double averageProductionConsumption = 0;
                if (buffer.getProducedCount() != 0) {
                    averageProductionConsumption = ((double) buffer.getConsumedCount() / buffer.getProducedCount()) * 100;
                }
                int roundedAverage = (int) Math.round(averageProductionConsumption);
                logger.log("Average consumption/production over the last 10 seconds: " + roundedAverage + "%");

                int highThreshold = (int) (0.9 * 100);
                int lowThreshold = (int) (0.1 * 100);

                if (buffer.size() >= highThreshold) {
                    logger.log("Buffer size is now high at " + buffer.size() + " items");
                } else if (buffer.size() <= lowThreshold) {
                    logger.log("Buffer size is now low at " + buffer.size() + " items");
                }
                System.out.println("Producer count " + buffer.getProducedCount());
                System.out.println("Consumer count " + buffer.getConsumedCount());
                System.out.println("Buffer size " + buffer.size());

                buffer.resetCounters();
            }
        }, 0, 10 * 1000);
    }


    public void handleClearLogButtonClick() {
        logger.clearLogFile();
    }

    public void saveState(String filePath) throws IOException {
        AppState state = new AppState();
        state.setNumOfProducers(producerThreads.size());
        state.setNumOfConsumers(consumerThreads.size());
        state.setBufferItems(new LinkedList<>(buffer.getItems()));

        for (Producer producer : producerThreads) {
            state.addProducerSleepTime(producer.getSleepTime());
        }

        for (Consumer consumer : consumerThreads) {
            state.addConsumerSleepTime(consumer.getSleepTime());
        }



        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(state);
        }
    }

    public AppState loadState(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (AppState) ois.readObject();
        }
    }

    public void updateStateFromLoadedData(AppState state) {
        for (Producer producer : producerThreads) {
            if (producer.getThread().isAlive()) {
                producer.stop();
            }
        }
        producerThreads.clear();

        for (Consumer consumer : consumerThreads) {
            consumer.stop();
        }
        consumerThreads.clear();

        buffer.setItems(new LinkedList<>(state.getBufferItems()));

        List<Integer> producerSleepTimes = state.getProducerSleepTimes();
        for (int i = 0; i < state.getNumOfProducers(); i++) {
            Producer producer = new Producer(buffer, producerSleepTimes.get(i));
            producerThreads.add(producer);
            producer.start();
        }

        List<Integer> consumerSleepTimes = state.getConsumerSleepTimes();
        for (int i = 0; i < state.getNumOfConsumers(); i++) {
            Consumer consumer = new Consumer(buffer, consumerSleepTimes.get(i));
            consumerThreads.add(consumer);
            consumer.start();
        }

        logger.log("State updated from file. Current number of producers: " + producerThreads.size() + ", consumers: " + consumerThreads.size());
    }
}
