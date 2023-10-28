package org.arzije.ziberovska.model;

import org.arzije.ziberovska.view.GUI;

import javax.swing.*;
import java.util.Random;

public class Consumer implements Runnable{

    Buffer buffer = null;
    boolean isRunning = true;
    int sleepTime;
    private GUI gui;

    public Consumer(Buffer buffer, GUI gui) {
        this.buffer = buffer;
        this.gui = gui;
        this.sleepTime = new Random().nextInt(9000) + 1000;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(sleepTime);
//                System.out.println("Consumed: " + buffer.remove());
                Item consumed = buffer.remove();
                System.out.println("Consumed: " + consumed); // Existerande kod
                SwingUtilities.invokeLater(() -> log("Consumed: " + consumed)); // Ny kod

            } catch (InterruptedException e) {
                System.out.println("Consumer: Sleep avbruten");
                isRunning = false;
//                e.printStackTrace();
            }
        }
    }
    private void log(String message) {
        if (gui != null) {
            gui.log(message);
        }
    }
}
