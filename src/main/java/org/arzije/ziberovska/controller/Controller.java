package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;
import org.arzije.ziberovska.model.Buffer;
import org.arzije.ziberovska.view.GUI;

public class Controller {
    private final Buffer buffer;
    private final ProducerManager producerManager;
    private final ConsumerManager consumerManager;
    private final BufferMonitor bufferMonitor;
    private final StateManager stateManager;
    private final LoggerManager loggerManager;

    public Controller(int maxBufferCapacity) {
        buffer = new Buffer(maxBufferCapacity);
        producerManager = new ProducerManager(buffer);
        consumerManager = new ConsumerManager(buffer);
        bufferMonitor = new BufferMonitor(buffer);
        stateManager = new StateManager(buffer,
                producerManager.getProducers(),
                consumerManager.getConsumers());
        loggerManager = new LoggerManager();
    }

    public void initGUI() {
        GUI gui = new GUI(this,
                this.bufferMonitor);
        buffer.addObserver(gui);
        Log.getInstance().addObserver(gui);
        bufferMonitor.startBufferMonitoring();
    }

    public void initAfterGUI() {
        consumerManager.initializeConsumers();
    }

    public void handleProducerButtonClick() {
        producerManager.addProducer();
    }

    public void handleRemoveProducerButtonClick() {
        producerManager.removeProducer();
    }

    public void handleClearLogButtonClick() {
        loggerManager.clearLogFile();
    }

    public void handleSaveButtonClick(){
        stateManager.saveStateWithGUI();
    }

    public void handleLoadButtonClick(){
        stateManager.loadStateWithGUI();
    }

}