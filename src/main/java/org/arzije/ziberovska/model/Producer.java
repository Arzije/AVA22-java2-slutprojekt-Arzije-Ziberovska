package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a producer that adds items to the buffer at regular intervals.
 */

public class Producer implements Runnable, Serializable {

    private final Buffer buffer;
    private volatile boolean isRunning = false;
    private int sleepTime;
    private final Log logger = Log.getInstance();
    private Thread thread;

    /**
     * Constructor initializing the producer with a buffer.
     * @param buffer Buffer to add items to.
     */

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Constructor initializing the producer with a buffer and a specific sleep time.
     * @param buffer Buffer to add items to.
     * @param sleepTime Time between item additions.
     */
    public Producer(Buffer buffer, int sleepTime) {
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }


    @Override
    public void run(){
        this.sleepTime = (new Random().nextInt(10) + 1) * 1000;
        logger.log("Producer created with production interval: " + sleepTime + " ms.");

        // Continuously produce items to the buffer until isRunning becomes false.
        while (isRunning){
            try {
                Thread.sleep(sleepTime);
                buffer.add(new Item(""+(char) ((int)(Math.random()*100))));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void start() {
        if (thread == null || !thread.isAlive()) {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        isRunning = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public Thread getThread() {
        return thread;
    }

    public int getSleepTime() { //
        return sleepTime;
    }
}
