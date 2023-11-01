package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable{

    private Buffer buffer = null;
    private int sleepTime;
    private Log logger = Log.getInstance();
    private Thread thread;
    private volatile boolean isRunning = false;
//    private static int counter = 0;

    private static AtomicInteger counter = new AtomicInteger(0);

    public Consumer(Buffer buffer) {//, OnConsumedListener listener
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
//                counter++;
                int count = counter.incrementAndGet();
                System.out.println("Consumer counter: " + count);

            } catch (InterruptedException e) {
                isRunning = false;
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

    public static AtomicInteger getCounter() {
        return counter;
    }

}