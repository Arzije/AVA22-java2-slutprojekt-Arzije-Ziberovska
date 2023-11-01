package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable{

    private Buffer buffer;
    private int sleepTime;
    private Log logger = Log.getInstance();
    private Thread thread;
    private volatile boolean isRunning = false;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
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

}