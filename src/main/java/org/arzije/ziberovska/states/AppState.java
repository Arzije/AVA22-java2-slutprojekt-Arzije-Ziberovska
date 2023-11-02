package org.arzije.ziberovska.states;

import org.arzije.ziberovska.model.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AppState implements Serializable {
    private int numOfProducers;
    private int numOfConsumers;
    private Queue<Item> bufferItems;
    private List<Integer> producerSleepTimes = new ArrayList<>();
    private List<Integer> consumerSleepTimes = new ArrayList<>();

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