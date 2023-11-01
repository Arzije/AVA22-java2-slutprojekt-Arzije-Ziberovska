package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;

import org.arzije.ziberovska.controller.Controller;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.logging.LogObserver;
import org.arzije.ziberovska.model.BufferObserver;

public class GUI implements BufferObserver, LogObserver {
    private Controller controller;
    private JFrame frame;
    private JProgressBar progressBar;
    private JButton producerButton;
    private JButton consumerButton;
    private JButton clearLogButton;

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
        clearLogButton = new JButton("Rensa loggfil");


        producerButton.addActionListener(e -> controller.handleProducerButtonClick());
        consumerButton.addActionListener(e -> controller.handleRemoveProducerButtonClick());
        clearLogButton.addActionListener(e -> {
            logger.clearLogFile();
        });

        frame.add(producerButton);
        frame.add(consumerButton);
        frame.add(clearLogButton);
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
    }

    private void updateProgressBar() {
        int currentSize = controller.getBufferSize(); // Vf hämtar jag buffer.size() från controller?
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
    public void updateLog(String logMessage) {
        SwingUtilities.invokeLater(() -> {
            if (!logMessage.toLowerCase().contains("consumer")) {
                log(logMessage);
            }
        });
    }

}
