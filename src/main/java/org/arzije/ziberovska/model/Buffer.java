package org.arzije.ziberovska.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a thread-safe buffer to store items.
 * Uses the Observer pattern to notify registered observers of changes to the buffer.
 */

public class Buffer{
    private Queue<Item> items = new LinkedList<>();
    private final List<BufferObserver> observers = new ArrayList<>();
    private final int maxCapacity;
    private final AtomicInteger producedCounter = new AtomicInteger(0);
    private final AtomicInteger consumedCounter = new AtomicInteger(0);

    /**
     * Constructor initializing the buffer with a max capacity.
     * @param maxCapacity Maximum capacity of the buffer.
     */
    public Buffer(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Adds an item to the buffer. Blocks if the buffer is full.
     * @param item The item to add.
     */
    public synchronized void add(Item item) {
        while(items.size() >= maxCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        items.add(item);
        producedCounter.incrementAndGet();
        notifyObservers();
        notifyAll();
    }

    /**
     * Removes and returns an item from the buffer. Blocks if the buffer is empty.
     * @return The removed item.
     */

    public synchronized Item remove(){
        while (items.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e){
                return null;
            }
        }
        consumedCounter.incrementAndGet();
        return items.remove();
    }

    public int size(){
        return items.size();
    }

    public int getProducedCount() {
        return producedCounter.get();
    }

    public int getConsumedCount() {
        return consumedCounter.get();
    }

    public void resetCounters() {
        producedCounter.set(0);
        consumedCounter.set(0);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void addObserver(BufferObserver observer){
        observers.add(observer);
    }

    private void notifyObservers(){
        for (BufferObserver observer : observers){
            observer.update();
        }
    }

    public Queue<Item> getItems() {
        return new LinkedList<>(items);
    }

    public void setItems(Queue<Item> items) {
        this.items = items;
    }

}