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

/**
 * Manages the saving and loading of application state.
 */
public class StateManager {
    private final Buffer buffer;
    private final List<Producer> producerThreads;
    private final List<Consumer> consumerThreads;
    private final Log logger = Log.getInstance();

    /**
     * Constructor initializing the buffer, producer, and consumer threads.
     * @param buffer Shared buffer between producers and consumers.
     * @param producers List of producer threads.
     * @param consumers List of consumer threads.
     */
    public StateManager(Buffer buffer, List<Producer> producers, List<Consumer> consumers) {
        this.buffer = buffer;
        this.producerThreads = producers;
        this.consumerThreads = consumers;
    }

    /**
     * Opens a GUI dialog to save the application state to a chosen file.
     */
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

    /**
     * Opens a GUI dialog to load the application state from a chosen file.
     */
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

    /**
     * Saves the current application state to a specified file.
     * @param filePath Path of the file to save the state to.
     * @throws IOException In case of any I/O exceptions during save.
     */
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

    /**
     * Loads the application state from a specified file.
     * @param filePath Path of the file to load the state from.
     * @return The loaded application state.
     * @throws IOException In case of any I/O exceptions during load.
     * @throws ClassNotFoundException In case the AppState class is not found.
     */
    public AppState loadState(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (AppState) ois.readObject();
        }
    }

    /**
     * Updates the application state using the data loaded from a file.
     * @param state The loaded application state.
     */
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

