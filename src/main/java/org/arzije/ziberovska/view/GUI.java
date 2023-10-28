package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.model.BufferObserver;
import org.arzije.ziberovska.model.Consumer;
import org.arzije.ziberovska.model.Producer;
import org.arzije.ziberovska.view.listeners.ConsumerButtonListeners;
import org.arzije.ziberovska.view.listeners.ProducerButtonListeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUI implements BufferObserver {
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton producerButton;
    private JButton consumerButton;
    private JButton loadButton;
    private JButton saveButton;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private Buffer buffer;
    private List<Thread> producerThreads;
    private List<Thread> consumerThreads;
    private static final Logger logger = LogManager.getLogger(GUI.class);

    public GUI() {
        this.buffer = new Buffer();
        buffer.addObserver(this);

        this.producerThreads = new ArrayList<>();
        this.consumerThreads = new ArrayList<>();

        frame = new JFrame("Produktionsregulator");
        frame.setLayout(new FlowLayout());

        // Skapar progressbar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        frame.add(progressBar);

        // Skapar producent- och konsumentknappar
        producerButton = new JButton("Lägg till");
        consumerButton = new JButton("Ta bort");
        producerButton.addActionListener(e -> addProducer());
        consumerButton.addActionListener(e -> removeProducer());

        frame.add(producerButton);
        frame.add(consumerButton);

        // Skapar ladda och spara knappar
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        frame.add(loadButton);
        frame.add(saveButton);

        // Skapar logg-textområde med skroll
        logTextArea = new JTextArea(10, 30); // 10 rader hög, 30 tecken bred
        logScrollPane = new JScrollPane(logTextArea);
        logTextArea.setEditable(false);
        frame.add(logScrollPane);

        for (int i = 0; i < 8; i++) {
            Producer producer = new Producer(this.buffer, this); // Använd 'this.buffer' istället för 'sharedBuffer'
            Thread producerThread = new Thread(producer);
            producerThreads.add(producerThread); // Lägg till denna rad
            producerThread.start();
            log("Producers: " + i);
        }

        Random random = new Random();
//        int numOfConsumers = random.nextInt(13) + 3;
                int numOfConsumers = random.nextInt(6) + 5;
        for (int i = 0; i < numOfConsumers; i++) {
            Consumer consumer = new Consumer(buffer, this);
            Thread consumerThread = new Thread(consumer);
            consumerThreads.add(consumerThread);
            consumerThread.start();
            log("Consumers: " + i);
        }



//        loadButton.addActionListener(e -> loadState());
//        saveButton.addActionListener(e -> saveState());

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void handleProducerButtonClick() {
        // Logik för producer-knappen här...
    }

    private void handleConsumerButtonClick() {
        // Logik för consumer-knappen här...
    }


    private void addProducer() {
        Producer producer = new Producer(buffer, this);
        Thread thread = new Thread(producer);
        producerThreads.add(thread);
        thread.start();
//        log("Producer I added.");
        System.out.println("Producer I added " + buffer.size());
    }

        private void removeProducer() {
        if (!producerThreads.isEmpty()) {
            Thread lastProducer = producerThreads.remove(producerThreads.size() - 1);
            // Stoppar producer-tråden. Se till att du har en mekanism i din Producer-klass för att stoppa den
            lastProducer.interrupt();
//            log("Producer removed.");
            System.out.println("Producer removed.");
        } else {
            System.out.println("No producer to remove.");
//            log("No producer to remove.");
        }
    }

    public void log(String message) {
        logTextArea.append(message + "\n");
        logger.info(message);
    }

    private void updateProgressBar() {
        int currentSize = buffer.size();
        progressBar.setValue(currentSize);
        if (currentSize <= 10) {
            progressBar.setForeground(Color.RED);
        } else if (currentSize >= 90) {
            progressBar.setForeground(Color.GREEN);
        } else {
            progressBar.setForeground(Color.GRAY); // Eller någon annan standardfärg
        }
    }

    @Override
    public void update() {
        updateProgressBar();
    }
}
