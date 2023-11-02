package org.arzije.ziberovska.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.arzije.ziberovska.controller.Controller;
import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.logging.LogObserver;
import org.arzije.ziberovska.model.BufferObserver;
import org.arzije.ziberovska.states.AppState;

public class GUI implements BufferObserver, LogObserver {

    private final Controller controller;
    private final JFrame frame;
    private JProgressBar progressBar;
    private JButton producerButton;
    private JButton consumerButton;
    private JButton clearLogButton;

    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private final Log logger = Log.getInstance();

    private JButton saveStateButton;
    private JButton loadStateButton;

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
        saveStateButton = new JButton("Spara tillstånd");
        loadStateButton = new JButton("Ladda tillstånd");


        producerButton.addActionListener(e -> controller.handleProducerButtonClick());
        consumerButton.addActionListener(e -> controller.handleRemoveProducerButtonClick());
        clearLogButton.addActionListener(e -> controller.handleClearLogButtonClick());

        saveStateButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    controller.saveState(file.getPath());
                    log("Tillstånd sparad till " + file.getPath());
                } catch (IOException ex) {
                    log("Fel när man sparar tillstånd: " + ex.getMessage());
                }
            }
        });

        loadStateButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    AppState state = controller.loadState(file.getPath());

                    controller.updateStateFromLoadedData(state);

                    log("Tillstånd laddad från " + file.getPath());
                } catch (IOException | ClassNotFoundException ex) {
                    log("Fel när man laddar tillstånd: " + ex.getMessage());
                }
            }
        });

        frame.add(saveStateButton);
        frame.add(loadStateButton);

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
