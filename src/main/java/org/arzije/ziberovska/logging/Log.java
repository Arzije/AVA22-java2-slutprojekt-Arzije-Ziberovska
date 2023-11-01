package org.arzije.ziberovska.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Log implements LogSubject{
    private static final Logger logger = LogManager.getLogger(Log.class);
    private static final Log instance = new Log();
    private final List<LogObserver> logObservers = new ArrayList<>();//

    public static Log getInstance(){
        return instance;
    }

    public void log(String message){
        logger.info(message);
        notifyObservers(message); //
    }

    @Override
    public void addObserver(LogObserver observer) {
        logObservers.add(observer);
    }

    @Override
    public void removeObserver(LogObserver observer) {
        logObservers.remove(observer);
    }

    @Override
    public void notifyObservers(String logMessage) {
        for (LogObserver observer : logObservers) {
            observer.updateLog(logMessage);
        }
    }








    public void clearLogFile() {
        String logFilePath = "C:\\Users\\arzij\\OneDrive\\IdeaProjects\\SlutprojektAvanceradJava\\logs\\app.log"; // direkt användning av filvägen
        try (FileOutputStream stream = new FileOutputStream(logFilePath, false)) {
            // Inte skriver något till strömmen här
        } catch (IOException e) {
            logger.error("Error clearing the log file.", e);
        }
    }
}
