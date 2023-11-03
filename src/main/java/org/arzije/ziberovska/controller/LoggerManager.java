package org.arzije.ziberovska.controller;

import org.arzije.ziberovska.logging.Log;

import javax.swing.*;

/**
 * Manages the logging functionality in the application.
 */
public class LoggerManager {

    // The singleton instance of the logger.
    private Log logger = Log.getInstance();

    /**
     * Clears the log file.
     */
    public void clearLogFile() {
        logger.clearLogFile();
    }
}
