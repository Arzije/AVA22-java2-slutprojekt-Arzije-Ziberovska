package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable{

    Buffer buffer = null;
    private volatile boolean isRunning = false;
    int sleepTime;
    private Log logger = Log.getInstance();
    private Thread thread;
//    private static int counter = 0;
    private static AtomicInteger counter = new AtomicInteger(0);

    public Producer(Buffer buffer) { //, OnProducedListener producedListener
        this.buffer = buffer;
    }

    @Override
    public void run(){
        this.sleepTime = (new Random().nextInt(10) + 1) * 1000;

        logger.log("Producer created with production interval: " + sleepTime + " ms.");
        while (isRunning){
            try {
                Thread.sleep(sleepTime);
                buffer.add(new Item(""+(char) ((int)(Math.random()*100))));
//                counter++;
                int count = counter.incrementAndGet();
                System.out.println("Producer counter: " + count);

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
