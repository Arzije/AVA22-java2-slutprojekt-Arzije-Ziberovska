package org.arzije.ziberovska.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Observable klass
 */
public class Buffer {

    private Queue<Item> items = new LinkedList();
    private List<BufferObserver> observers = new ArrayList<>();

    public synchronized void add(Item item){
        items.add(item);
        notifyObservers();
        notifyAll(); // Väcker alla väntande trådar
//        System.out.println("Buffer buffer size: " + items.size());
    }

    public synchronized Item remove(){
        while (items.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Item item = items.remove();
        return item;
    }

    public int size() {
        return items.size();
    }

    public void addObserver(BufferObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BufferObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (BufferObserver observer : observers) {
            observer.update();
        }
    }


}
