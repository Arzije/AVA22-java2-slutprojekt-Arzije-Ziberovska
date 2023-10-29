package org.arzije.ziberovska.model;

import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Producer implements Runnable{

    Buffer buffer = null;
    boolean isRunning = true;
    int sleepTime;
    private OnProducedListener producedListener;

    public Producer(Buffer buffer, OnProducedListener producedListener) {
        this.buffer = buffer;
        this.producedListener = producedListener;
        this.sleepTime = new Random().nextInt(9000) + 1000;
    }

    @Override
    public void run(){
        while (isRunning){
            try {
//                Thread.sleep(sleepTime);
                Thread.sleep(3000);
                buffer.add(new Item(""+(char) ((int)(Math.random()*100))));

                if (producedListener != null){
                    producedListener.onProduced("Producer produced an item. Production interval: " + sleepTime + " ms. Buffer size: " + buffer.size());
                }
            } catch (InterruptedException e) {
                if (producedListener != null){
                    producedListener.onProduced("Producer: Sleep avbruten");
                }
                isRunning = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}
