package org.arzije.ziberovska.model;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Producer implements Runnable{

    Buffer buffer = null;
    boolean isRunning = true;
    int sleepTime;
    private Log logger = Log.getInstance();

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

            } catch (InterruptedException e) {

                isRunning = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}
