package org.arzije.ziberovska.model;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arzije.ziberovska.logging.Log;

/**
 * Observable klass
 */
public class Buffer {
    private Queue<Item> items = new LinkedList();
    private List<BufferObserver> observers = new ArrayList<>();
    Log logger = Log.getInstance();
    private Timer timer = new Timer();

    public Buffer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logger.log("Average buffer size over last 10 seconds: " + size());  // Justerat meddelandet
            }
        }, 0, 10 * 1000); // Kör var 10:e sekund
    }

    public synchronized void add(Item item){
        items.add(item);
        int currentSize = items.size();

        if (currentSize >= 90) {
            logger.log("Buffer size is now high at " + currentSize + " items.");
        }

        notifyObservers();
        notifyAll(); // Väcker alla väntande trådar
//        System.out.println("Buffer buffer size: " + items.size());
    }

    public synchronized Item remove(){
        while (items.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                logger.log("Interrupted while waiting for items. " + e);
                return null; // Notera detta tillägg; det är för att hantera undantaget på ett säkert sätt.
            }
        }
        Item item = items.remove();
        int currentSize = items.size();
        notifyObservers();

        if (currentSize <= 10) {
            logger.log("Buffer size is now low at " + currentSize + " items.");
        }

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
