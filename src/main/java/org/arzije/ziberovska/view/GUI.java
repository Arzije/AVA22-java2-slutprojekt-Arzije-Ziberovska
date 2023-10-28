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

        for (int i = 0; i < 10; i++) {
            Producer producer = new Producer(this.buffer, this); // Använd 'this.buffer' istället för 'sharedBuffer'
            new Thread(producer).start();
        }

        Random random = new Random();
        int numOfConsumers = random.nextInt(13) + 3;
        for (int i = 0; i < numOfConsumers; i++) {
            Consumer consumer = new Consumer(buffer, this);
            Thread consumerThread = new Thread(consumer);
            consumerThreads.add(consumerThread);
            consumerThread.start();
        }

        System.out.println("Num of consumers: " + numOfConsumers);

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
        log("Producer added.");
    }

    //    private void removeProducer() {
//        if (!producerThreads.isEmpty()) {
//            Thread lastProducer = producerThreads.remove(producerThreads.size() - 1);
//            // Stoppar producer-tråden. Se till att du har en mekanism i din Producer-klass för att stoppa den
//            lastProducer.interrupt();
//            log("Producer removed.");
//        } else {
//            log("No producer to remove.");
//        }
//    }
    private void removeProducer() {
        // Om bufferten inte är tom, ta bort en enhet
        if (buffer.size() > 0) {
            buffer.remove(); // Detta förutsätter att `remove`-metoden tar bort en enhet
            log("Unit removed from buffer.");
        } else {
            log("Buffer is empty. No unit to remove.");
        }
    }


    public void log(String message) {
        logTextArea.append(message + "\n");
    }

    private void updateProgressBar() {
        int currentSize = buffer.size();
        progressBar.setValue(currentSize);
    }

    @Override
    public void update() {
        updateProgressBar();
    }
}
