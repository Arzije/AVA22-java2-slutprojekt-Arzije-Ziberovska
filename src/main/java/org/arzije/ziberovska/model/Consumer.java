package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;

import java.io.Serializable;
import java.util.Random;

public class Consumer implements Runnable, Serializable {

    private Buffer buffer;
    private int sleepTime;
    private Log logger = Log.getInstance();
    private Thread thread;
    private volatile boolean isRunning = false;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public Consumer(Buffer buffer, int sleepTime) {
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run(){
        sleepTime = (new Random().nextInt(10) + 1) * 1000;
        logger.log("Consumer created with consumption interval: " + sleepTime + " ms.");

        while (isRunning){
            try {
                Thread.sleep(sleepTime);
                Item consumed = buffer.remove();

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

    public int getSleepTime() {
        return sleepTime;
    }

}