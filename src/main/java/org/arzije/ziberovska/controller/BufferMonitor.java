package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * BufferMonitor is responsible for monitoring the status and usage of the buffer.
 * It tracks the current size of the buffer, logs related statistics, and
 * provides functionality to update a progress bar reflecting the buffer's status.
 */

public class BufferMonitor {
    private final Buffer buffer;
    private final Log logger = Log.getInstance();

    /**
     * Constructor for BufferMonitor.
     *
     * @param buffer The buffer that will be monitored.
     */
    public BufferMonitor(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Starts monitoring the buffer for its current size and logs
     * various statistics related to the buffer usage.
     */
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

    /**
     * Updates the progress bar to reflect the current buffer size.
     * Changes the color based on the buffer size.
     *
     * @param progressBar The progress bar to be updated.
     */
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
