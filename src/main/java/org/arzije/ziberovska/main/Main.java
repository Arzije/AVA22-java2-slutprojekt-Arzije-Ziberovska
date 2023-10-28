package org.arzije.ziberovska.main;

import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.Consumer;
import org.arzije.ziberovska.model.Producer;
import org.arzije.ziberovska.view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

//        Buffer buffer = new Buffer();
//        Producer producer = new Producer(buffer);
//        Thread producerThread = new Thread(producer);
//        producerThread.start();
//
//        Consumer consumer = new Consumer(buffer);
//        Thread consumerThread = new Thread(consumer);
//        consumerThread.start();
        SwingUtilities.invokeLater(() -> new GUI());
    }
}