package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;

import javax.swing.*;

public class LoggerManager {
    private Log logger = Log.getInstance();
    public void clearLogFile() {
        logger.clearLogFile();
    }
}
