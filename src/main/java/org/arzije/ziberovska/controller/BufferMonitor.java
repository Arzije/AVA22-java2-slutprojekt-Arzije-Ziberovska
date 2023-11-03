package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class BufferMonitor {
    private final Buffer buffer;
    private final Log logger = Log.getInstance();

    public BufferMonitor(Buffer buffer) {
        this.buffer = buffer;
    }

        public void startBufferMonitoring() {
        Queue<Integer> recentBufferSizes = new LinkedList<>();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                recentBufferSizes.add(buffer.size());
                if (recentBufferSizes.size() > 10) {
                    recentBufferSizes.poll();
                }

                double averageProductionConsumption = 0;
                if (buffer.getProducedCount() != 0) {
                    averageProductionConsumption = ((double) buffer.getConsumedCount() / buffer.getProducedCount()) * 100;
                }
                int roundedAverage = (int) Math.round(averageProductionConsumption);
                logger.log("Average consumption/production over the last 10 seconds: " + roundedAverage + "%");

                int bufferCapacity = buffer.getMaxCapacity();
                int highThreshold = (int) (0.9 * bufferCapacity);
                int lowThreshold = (int) (0.1 * bufferCapacity);

                if (buffer.size() >= highThreshold) {
                    logger.log("Buffer size is now high at " + buffer.size() + " items");
                } else if (buffer.size() <= lowThreshold) {
                    logger.log("Buffer size is now low at " + buffer.size() + " items");
                }

                buffer.resetCounters();
            }
        }, 10 * 1000, 10 * 1000);
    }

    public void updateProgressBar(JProgressBar progressBar) {
        int currentSize = buffer.size();
        progressBar.setValue(currentSize);
        if (currentSize <= 10) {
            progressBar.setForeground(Color.RED);
        } else if (currentSize >= 90) {
            progressBar.setForeground(Color.GREEN);
        } else {
            progressBar.setForeground(Color.GRAY);
        }
    }
}
