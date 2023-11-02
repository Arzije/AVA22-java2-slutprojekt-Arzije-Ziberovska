package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;

import java.io.Serializable;
import java.util.Random;

public class Producer implements Runnable, Serializable {

    Buffer buffer;
    private volatile boolean isRunning = false;
    int sleepTime;
    private Log logger = Log.getInstance();
    private Thread thread;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public Producer(Buffer buffer, int sleepTime) {
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run(){
        this.sleepTime = (new Random().nextInt(10) + 1) * 1000;
        logger.log("Producer created with production interval: " + sleepTime + " ms.");

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
