package org.arzije.ziberovska.model;

import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Consumer implements Runnable{

    private Buffer buffer = null;
    private boolean isRunning = true;
    private int sleepTime;
    private OnConsumedListener consumedListener;

    public Consumer(Buffer buffer, OnConsumedListener listener) {
        this.buffer = buffer;
        this.consumedListener = listener;
        this.sleepTime = new Random().nextInt(9000) + 1000;
    }

    @Override
    public void run(){
        while (isRunning){
            try {
//                Thread.sleep(sleepTime);
                Thread.sleep(3000);
                Item consumed = buffer.remove();

                if (consumedListener != null){
                    consumedListener.onConsumed("Consumer consumed an item. Consumption interval: " + sleepTime + " ms. Buffer size: " + buffer.size());
                }

            } catch (InterruptedException e) {
                if (consumedListener != null){
                    consumedListener.onConsumed("Consumer: Sleep avbruten");
                }
                isRunning = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}