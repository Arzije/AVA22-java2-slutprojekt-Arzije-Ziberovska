package org.arzije.ziberovska.model;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arzije.ziberovska.logging.Log;

/**
 * Observable klass
 */

public class Buffer{

    private Queue<Item> items = new LinkedList<>();
    private List<BufferObserver> observers = new ArrayList<>();

    public synchronized void add(Item item){
        items.add(item);
        notifyObservers();
        notifyAll();
    }

    public synchronized Item remove(){
        while (items.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e){
                return null;
            }
        }
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