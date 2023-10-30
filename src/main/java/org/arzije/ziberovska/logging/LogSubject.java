package org.arzije.ziberovska.logging;

public interface LogSubject {
    void addObserver(LogObserver observer);
    void removeObserver(LogObserver observer);
    void notifyObservers(String logMessage);
}
