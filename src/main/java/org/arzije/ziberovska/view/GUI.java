package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;
import org.arzije.ziberovska.controller.Controller;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.BufferObserver;
import org.arzije.ziberovska.model.OnConsumedListener;
import org.arzije.ziberovska.model.OnProducedListener;

public class GUI implements BufferObserver , OnProducedListener, OnConsumedListener {
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

    @Override
    public void onProduced(String message) {
        log(message);
    }

    @Override
    public void onConsumed(String message) {
        log(message);
    }
}
