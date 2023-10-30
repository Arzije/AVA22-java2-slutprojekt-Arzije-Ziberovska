package org.arzije.ziberovska.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class Log {
    private static final Logger logger = LogManager.getLogger(Log.class);
    private static final Log instance = new Log();
    private Consumer<String> guiCallback;
    private final LogReader logReader;

    private Log() { //ny
        logReader = new LogReader("C:\\Users\\arzij\\OneDrive\\IdeaProjects\\SlutprojektAvanceradJava\\logs\\app.log", this::logToGui);
        new Thread(logReader).start();
    }

    public static Log getInstance(){
        return instance;
    }

    public void setGuiCallback(Consumer<String> callback){ //ny
        this.guiCallback = callback;
    }

    public void log(String message){
        logger.info(message);
    }

    private void logToGui(String message){ //ny
        if (guiCallback != null){
            guiCallback.accept(message);
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
