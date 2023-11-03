package org.arzije.ziberovska.states;

import org.arzije.ziberovska.model.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Represents the current state of the application, including number of producers and consumers,
 * buffer items, and sleep times for both producers and consumers.
 */
public class AppState implements Serializable {
    private int numOfProducers;
    private int numOfConsumers;
    private Queue<Item> bufferItems;
    private final List<Integer> producerSleepTimes = new ArrayList<>();
    private final List<Integer> consumerSleepTimes = new ArrayList<>();

    public int getNumOfProducers() {
        return numOfProducers;
    }

    public void setNumOfProducers(int numOfProducers) {
        this.numOfProducers = numOfProducers;
    }

    public int getNumOfConsumers() {
        return numOfConsumers;
    }

    public void setNumOfConsumers(int numOfConsumers) {
        this.numOfConsumers = numOfConsumers;
    }

    public Queue<Item> getBufferItems() {
        return bufferItems;
    }

    public void setBufferItems(Queue<Item> bufferItems) {
        this.bufferItems = bufferItems;
    }

    public void addProducerSleepTime(int sleepTime) {
        producerSleepTimes.add(sleepTime);
    }

    public void addConsumerSleepTime(int sleepTime) {
        consumerSleepTimes.add(sleepTime);
    }

    public List<Integer> getProducerSleepTimes() {
        return producerSleepTimes;
    }

    public List<Integer> getConsumerSleepTimes() {
        return consumerSleepTimes;
    }
}
