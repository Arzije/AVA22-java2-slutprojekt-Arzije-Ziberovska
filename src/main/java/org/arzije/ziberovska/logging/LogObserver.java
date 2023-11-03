package org.arzije.ziberovska.logging;

/**
 * Interface for observers to be notified of log updates.
 */
public interface LogObserver {
    void updateLog(String logMessage);
}
