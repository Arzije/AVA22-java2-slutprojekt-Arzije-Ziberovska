package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a consumer that retrieves items from the buffer at regular intervals.
 */

public class Consumer implements Runnable, Serializable {

    private final Buffer buffer;
    private int sleepTime;
    private final Log logger = Log.getInstance();
    private Thread thread;
    private volatile boolean isRunning = false;

    /**
     * Constructor initializing the consumer with a buffer.
     * @param buffer Buffer to retrieve items from.
     */
    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Constructor initializing the consumer with a buffer and a specific sleep time.
     * @param buffer Buffer to retrieve items from.
     * @param sleepTime Time between item retrievals.
     */
    public Consumer(Buffer buffer, int sleepTime) {
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run(){

        // Assign a random sleep duration between 1 and 10 seconds.
        sleepTime = (new Random().nextInt(10) + 1) * 1000;

        logger.log("Consumer created with consumption interval: " + sleepTime + " ms.");

        // Continuously consume items from the buffer until isRunning becomes false.
        while (isRunning){
            try {
                Thread.sleep(sleepTime);
                Item consumed = buffer.remove();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Method to start the Consumer's thread.
    public void start() {
        if (thread == null || !thread.isAlive()) {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    // Method to stop the Consumer's thread.
    public void stop() {
        isRunning = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public int getSleepTime() {
        return sleepTime;
    }

}