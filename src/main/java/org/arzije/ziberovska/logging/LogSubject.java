package org.arzije.ziberovska.logging;

/**
 * Interface for the subject in the Observer pattern for logging.
 */

public interface LogSubject {
    void addObserver(LogObserver observer);
    void notifyObservers(String logMessage);
}
