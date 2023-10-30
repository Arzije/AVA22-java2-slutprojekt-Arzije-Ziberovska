package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Consumer implements Runnable{

    private Buffer buffer = null;
    private boolean isRunning = true;
    private int sleepTime;
    private Log logger = Log.getInstance();

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

            } catch (InterruptedException e) {
                isRunning = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}