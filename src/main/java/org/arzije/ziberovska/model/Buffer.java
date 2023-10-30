package org.arzije.ziberovska.model;

import java.util.*;

/**
 * Observable klass
 */

public class Buffer{

    private Queue<Item> items = new LinkedList<>();
    private List<BufferObserver> observers = new ArrayList<>();
    private int maxCapacity = 100; // Sätt en förvald kapacitet till 100. Detta kan ändras vid behov.

    public Buffer(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void add(Item item){
        if(items.size() < maxCapacity) { // Kontrollera innan tillägg om vi inte överstiger max kapacitet
            items.add(item);
            notifyObservers();
            notifyAll();
        }
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