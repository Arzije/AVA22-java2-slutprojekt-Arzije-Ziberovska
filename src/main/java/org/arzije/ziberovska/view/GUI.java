package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;

import org.arzije.ziberovska.controller.BufferMonitor;
import org.arzije.ziberovska.controller.Controller;
import org.arzije.ziberovska.logging.LogObserver;
import org.arzije.ziberovska.model.BufferObserver;

/**
 * Represents the graphical user interface (GUI) of the application, providing a way for
 * users to interact with the application's features. The GUI consists of progress bars, buttons,
 * and a log area to display messages.
 */

public class GUI implements BufferObserver, LogObserver {
    private final Controller controller;
    private final JFrame frame;
    private JProgressBar progressBar;
    private JTextArea logTextArea;
    private final BufferMonitor bufferMonitor;

    /**
     * Constructs the GUI with a given controller and buffer monitor.
     * @param controller The main controller for the application.
     * @param bufferMonitor Monitors the buffer state and updates the progress bar accordingly.
     */
    public GUI(Controller controller, BufferMonitor bufferMonitor) {
        this.controller = controller;
        this.bufferMonitor = bufferMonitor;

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
        JButton producerButton = new JButton("Lägg till");
        JButton consumerButton = new JButton("Ta bort");
        JButton clearLogButton = new JButton("Rensa loggfil");
        JButton saveStateButton = new JButton("Spara tillstånd");
        JButton loadStateButton = new JButton("Ladda tillstånd");

        producerButton.addActionListener(e -> controller.handleProducerButtonClick());
        consumerButton.addActionListener(e -> controller.handleRemoveProducerButtonClick());
        clearLogButton.addActionListener(e -> controller.handleClearLogButtonClick());
        saveStateButton.addActionListener(e -> controller.handleSaveButtonClick());
        loadStateButton.addActionListener(e -> controller.handleLoadButtonClick());

        frame.add(saveStateButton);
        frame.add(loadStateButton);

        frame.add(producerButton);
        frame.add(consumerButton);
        frame.add(clearLogButton);
    }

    private void initLogArea() {
        logTextArea = new JTextArea(25, 55);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logTextArea.setEditable(false);
        frame.add(logScrollPane);
    }

    public void log(String message) {
        logTextArea.insert(message + "\n", 0);
        logTextArea.setCaretPosition(0);
    }

    /**
     * Updates the progress bar based on buffer usage.
     */
    @Override
    public void update() {
        bufferMonitor.updateProgressBar(progressBar);
    }

    /**
     * Updates the log area with a new log message.
     * @param logMessage The new log message to add.
     */
    @Override
    public void updateLog(String logMessage) {
        SwingUtilities.invokeLater(() -> {
            if (!logMessage.toLowerCase().contains("consumer")) {
                log(logMessage);
            }
        });
    }

}
