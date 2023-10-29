package org.arzije.ziberovska.model;

import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Producer implements Runnable{
    Buffer buffer = null;
    boolean isRunning = true;
    int sleepTime;
    private GUI gui;


    public Producer(Buffer buffer, GUI gui) {
        this.buffer = buffer;
        this.gui = gui;
        this.sleepTime = new Random().nextInt(9000) + 1000;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
//                Thread.sleep(sleepTime);
                Thread.sleep(3000);
                buffer.add(new Item(""+(char) ((int)(Math.random()*100))));
                System.out.println("Producer SELF added an item." + buffer.size()); // Existerande kod
                SwingUtilities.invokeLater(() -> log("Producer added an item. Production interval: " + sleepTime + " ms. Buffer size: " + buffer.size())); // Uppdaterad kod
                log("Producer added an item. Production interval: " + sleepTime + " ms. Buffer size: " + buffer.size());  // Uppdaterad kod

            } catch (InterruptedException e) {
                log("Producer: Sleep avbruten");  // Ändrat här
                System.out.println("Producer: Sleep avbruten");
                isRunning = false;
                Thread.currentThread().interrupt();
            }
        }
    }
    private void log(String message) {
        if (gui != null) {
            gui.log(message);
        }
    }
}
