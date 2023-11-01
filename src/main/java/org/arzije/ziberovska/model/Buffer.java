package org.arzije.ziberovska.model;

import java.util.*;

/**
 * Observable klass
 */

public class Buffer{

    private Queue<Item> items = new LinkedList<>();
    private List<BufferObserver> observers = new ArrayList<>();
    private int maxCapacity;

    public Buffer(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void add(Item item) {
        while(items.size() >= maxCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        items.add(item);
        notifyObservers();
        notifyAll();
        System.out.println("Items in buffer: " + items.size());
    }

    public synchronized Item remove(){
        while (items.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e){
                return null;
            }
        }
        System.out.println("Items in buffer after removal: " + items.size());
        return items.remove();
    }

    public int size(){
        return items.size();
    }

    public void addObserver(BufferObserver observer){
        observers.add(observer);
    }

    public void removeObserver(BufferObserver observer){
        observers.remove(observer);
    }

    private void notifyObservers(){
        for (BufferObserver observer : observers){
            observer.update();
        }
    }
}