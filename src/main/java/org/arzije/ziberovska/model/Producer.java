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
                Thread.sleep(sleepTime);

                buffer.add(new Item(""+(char) ((int)(Math.random()*100))));
                System.out.println("Producer added an item."); // Existerande kod
                SwingUtilities.invokeLater(() -> log("Producer added an item.")); // Ny kod

            } catch (InterruptedException e) {
                System.out.println("Producer: Sleep avbruten");

                isRunning = false;
            }
        }
    }
    private void log(String message) {
        if (gui != null) {
            gui.log(message);
        }
    }
}
