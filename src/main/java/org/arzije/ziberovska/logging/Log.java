package org.arzije.ziberovska.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public class Log {
    private static final Logger logger = LogManager.getLogger(Log.class);
    private static final Log instance = new Log();

    private Log() {}

    public static Log getInstance(){
        return instance;
    }

    public void log(String message){
        logger.info(message);
    }
}
