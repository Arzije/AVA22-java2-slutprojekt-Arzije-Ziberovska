package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Consumer;
import org.arzije.ziberovska.model.Producer;
import org.arzije.ziberovska.states.AppState;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class StateManager {
    private final Buffer buffer;
    private final List<Producer> producerThreads;
    private final List<Consumer> consumerThreads;
    private final Log logger = Log.getInstance();

    public StateManager(Buffer buffer, List<Producer> producers, List<Consumer> consumers) {
        this.buffer = buffer;
        this.producerThreads = producers;
        this.consumerThreads = consumers;
    }

    public void saveStateWithGUI() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                saveState(file.getPath());
                logger.log("State saved in file: " + file.getPath());
            } catch (IOException ex) {
                logger.log("Exception while saving state: " + ex.getMessage());

            }
        }
    }

    public void loadStateWithGUI() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                AppState state = loadState(file.getPath());
                updateStateFromLoadedData(state);
                logger.log("State loaded from file " + file.getPath());
            } catch (IOException | ClassNotFoundException ex) {
                logger.log("Exception while loading state: " + ex.getMessage());
            }
        }
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

