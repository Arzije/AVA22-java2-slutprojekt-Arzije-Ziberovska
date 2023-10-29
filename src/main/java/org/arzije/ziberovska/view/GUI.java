package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;
import org.arzije.ziberovska.controller.Controller;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.BufferObserver;

public class GUI implements BufferObserver {
    private Controller controller;
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton producerButton;
    private JButton consumerButton;
    private JButton loadButton;
    private JButton saveButton;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private Log logger = Log.getInstance();

    public GUI(Controller controller) {
        this.controller = controller;

        frame = new JFrame("Produktionsregulator");
        frame.setLayout(new FlowLayout());

        initProgressBar();
        initButtons();
        initLogArea();

        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initProgressBar() {
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        frame.add(progressBar);
    }

    private void initButtons() {
        producerButton = new JButton("Lägg till");
        consumerButton = new JButton("Ta bort");

        producerButton.addActionListener(e -> controller.handleProducerButtonClick());
        consumerButton.addActionListener(e -> controller.handleConsumerButtonClick());

        frame.add(producerButton);
        frame.add(consumerButton);
    }

    private void initLogArea() {
        logTextArea = new JTextArea(25, 55);
        logScrollPane = new JScrollPane(logTextArea);
        logTextArea.setEditable(false);
        frame.add(logScrollPane);
    }

    public void log(String message) {
        logTextArea.insert(message + "\n", 0);
        logTextArea.setCaretPosition(0);
        logger.log(message);
    }

    private void updateProgressBar() {
        int currentSize = controller.getBufferSize();
        progressBar.setValue(currentSize);
        if (currentSize <= 10) {
            progressBar.setForeground(Color.RED);
        } else if (currentSize >= 90) {
            progressBar.setForeground(Color.GREEN);
        } else {
            progressBar.setForeground(Color.GRAY);
        }
    }

    @Override
    public void update() {
        updateProgressBar();
    }
}




//package org.arzije.ziberovska.view;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//import org.arzije.ziberovska.logging.Log;
//import org.arzije.ziberovska.model.Buffer;
//import org.arzije.ziberovska.model.BufferObserver;
//import org.arzije.ziberovska.model.Consumer;
//import org.arzije.ziberovska.model.Producer;
//
//public class GUI implements BufferObserver {
//    private JFrame frame;
//    private JProgressBar progressBar;
//    private JButton producerButton;
//    private JButton consumerButton;
//    private JButton loadButton;
//    private JButton saveButton;
//    private JTextArea logTextArea;
//    private JScrollPane logScrollPane;
//    private Buffer buffer;
//    private List<Thread> producerThreads;
//    private List<Thread> consumerThreads;
//    private Log logger = Log.getInstance();
//
//    public GUI() {
//        this.buffer = new Buffer();
//        buffer.addObserver(this);
//
//        this.producerThreads = new ArrayList<>();
//        this.consumerThreads = new ArrayList<>();
//
//        frame = new JFrame("Produktionsregulator");
//        frame.setLayout(new FlowLayout());
//
//        // Skapar progressbar
//        progressBar = new JProgressBar();
//        progressBar.setStringPainted(true);
//        progressBar.setMinimum(0);
//        progressBar.setMaximum(100);
//        frame.add(progressBar);
//
//        // Skapar producent- och konsumentknappar
//        producerButton = new JButton("Lägg till");
//        consumerButton = new JButton("Ta bort");
//        producerButton.addActionListener(e -> addProducer());
//        consumerButton.addActionListener(e -> removeProducer());
//
//        frame.add(producerButton);
//        frame.add(consumerButton);
//
//        // Skapar ladda och spara knappar
//        loadButton = new JButton("Load");
//        saveButton = new JButton("Save");
//        frame.add(loadButton);
//        frame.add(saveButton);
//
//        // Skapar logg-textområde med skroll
//        logTextArea = new JTextArea(25, 55);
//        logScrollPane = new JScrollPane(logTextArea);
//        logTextArea.setEditable(false);
//        frame.add(logScrollPane);
//
//        int numOfProducers = 8;
//        for (int i = 0; i < numOfProducers; i++) {
//            Producer producer = new Producer(this.buffer, this);
//            Thread producerThread = new Thread(producer);
//            producerThreads.add(producerThread);
//            producerThread.start();
//        }
//        log("Producers added: " + numOfProducers);
//        logger.log("Producers added: " + numOfProducers);
//
//        Random random = new Random();
////        int numOfConsumers = random.nextInt(13) + 3;
//                int numOfConsumers = random.nextInt(6) + 5;
//        for (int i = 0; i < numOfConsumers; i++) {
//            Consumer consumer = new Consumer(buffer, this);
//            Thread consumerThread = new Thread(consumer);
//            consumerThreads.add(consumerThread);
//            consumerThread.start();
//        }
//        log("Consumers added: " + numOfConsumers);
//        logger.log("Consumers added: " + numOfConsumers);
//
////        loadButton.addActionListener(e -> loadState());
////        saveButton.addActionListener(e -> saveState());
//
//        frame.setSize(600, 500);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
//
//    private void handleProducerButtonClick() {
//        // Logik för producer-knappen här...
//    }
//
//    private void handleConsumerButtonClick() {
//        // Logik för consumer-knappen här...
//    }
//
//
//    private void addProducer() {
//        Producer producer = new Producer(buffer, this);
//        Thread thread = new Thread(producer);
//        producerThreads.add(thread);
//        thread.start();
////        log("Producer I added.");
//        log("Producer added by user. Current number of producers: " + producerThreads.size());  // Justerat meddelandet
//        logger.log("Producer added by user. Current number of producers: " + producerThreads.size());
//
//    }
//
//        private void removeProducer() {
//        if (!producerThreads.isEmpty()) {
//            Thread lastProducer = producerThreads.remove(producerThreads.size() - 1);
//            // Stoppar producer-tråden. Se till att du har en mekanism i din Producer-klass för att stoppa den
//            lastProducer.interrupt();
//            log("Producer removed. Current number of producers: " + producerThreads.size());  // Justerat meddelandet
//            logger.log("Producer removed. Current number of producers: " + producerThreads.size());
//            System.out.println("Producer removed.");
//        } else {
//            log("No producer to remove.");  // Ändrat här
//            logger.log("No producer to remove.");
//            System.out.println("No producer to remove.");
//        }
//    }
//
//    public void log(String message) {
//        logTextArea.insert(message + "\n", 0);
//        logTextArea.setCaretPosition(0);
//        logger.log(message);
//    }
//
//    private void updateProgressBar() {
//        int currentSize = buffer.size();
//        progressBar.setValue(currentSize);
//        if (currentSize <= 10) {
//            progressBar.setForeground(Color.RED);
//        } else if (currentSize >= 90) {
//            progressBar.setForeground(Color.GREEN);
//        } else {
//            progressBar.setForeground(Color.GRAY); // Eller någon annan standardfärg
//        }
//    }
//
//    @Override
//    public void update() {
//        updateProgressBar();
//    }
//}
